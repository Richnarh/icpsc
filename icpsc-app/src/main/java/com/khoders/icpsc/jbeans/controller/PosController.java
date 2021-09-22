/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.jbeans.controller;

import Zenoph.SMSLib.Enums.REQSTATUS;
import Zenoph.SMSLib.ZenophSMS;
import com.khoders.icpsc.entities.Cart;
import com.khoders.icpsc.entities.Inventory;
import com.khoders.icpsc.entities.InventoryItem;
import com.khoders.icpsc.entities.Product;
import com.khoders.icpsc.entities.SalesCatalogue;
import com.khoders.icpsc.dto.PosReceipt;
import com.khoders.icpsc.entities.sms.Sms;
import com.khoders.icpsc.jbeans.ReportFiles;
import com.khoders.icpsc.listener.AppSession;
import com.khoders.icpsc.services.InventoryService;
import com.khoders.icpsc.services.PosService;
import com.khoders.icpsc.services.SmsService;
import com.khoders.icpsc.services.XtractService;
import com.khoders.icpsc.entities.enums.SMSType;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author richa
 */
@Named(value = "posController")
@SessionScoped
public class PosController implements Serializable
{

    @Inject private CrudApi crudApi; 
    @Inject private AppSession appSession; 
    @Inject private InventoryService inventoryService;
    @Inject private PosService posService;
    @Inject private ReportHandler reportHandler;
    @Inject private XtractService xtractService;
    @Inject private SmsService smsService;

    private Inventory selectedInventory = null;
    private InventoryItem inventoryItem = new InventoryItem();
    private SalesCatalogue salesCatalogue = new SalesCatalogue();
    private Sms sms = new Sms();
    
    private String userId;

    private List<Cart> cartList = new LinkedList<>();
    private Cart cart = new Cart();
    private List<InventoryItem> inventoryItemList = new LinkedList<>();
    

    private double totalAmount = 0.0;
    private String receiptLink,customerPhone;
    
    @PostConstruct
    private void init()
    {
        clear();
        inventoryItemList = inventoryService.getInventoryItemList();
    }

    public void selectProduct(Product product)
    {
        List<InventoryItem> inventorySalesList = new LinkedList<>();

        inventorySalesList = posService.getInventoryProduct(product);

        cart = new Cart();
        for (InventoryItem item : inventorySalesList)
        {
            cart.setQuantity(1);
            cart.setInventoryItem(item);
            cart.setUnitPrice(item.getSellingPrice());
            cartList.add(cart);
        }
    }

    public void processCart()
    {
        double totalCost = 0.0;
        try
        {
            totalAmount = 0.0;
            for (Cart cartItem : cartList)
            {
                if (cartItem.getQuantity() <= 0)
                {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please enter a valid quantity!"), null));

                    return;
                }

                if (cartItem.getQuantity() > cartItem.getInventoryItem().getQuantity())
                {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    Msg.setMsg("Limited stock for this product: "
                                            + cartItem.getInventoryItem().getProduct().getProductName())
                                    + "\n Quantity remaining is: " + cartItem.getInventoryItem().getQuantity(), null));
                    return;
                }

                totalCost = (cartItem.getQuantity() * cartItem.getUnitPrice());
                cartItem.setTotal(totalCost);

                totalAmount += totalCost;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveCart()
    {

        if (!cartList.isEmpty())
        {

            salesCatalogue.genCode();
            salesCatalogue.setPurchaseDate(LocalDateTime.now());
            salesCatalogue.setTotalAmount(totalAmount);
            salesCatalogue.setUserAccount(appSession.getCurrentUser());

            if (crudApi.save(salesCatalogue) != null)
            {
                cartList.forEach(salesCart ->
                {
                    int qtyPurchased = salesCart.getQuantity();
                    int qtyAtInventory = salesCart.getInventoryItem().getQuantity();

                    int qtyAtHand = qtyAtInventory - qtyPurchased;

                    try
                    {
                        inventoryItem = crudApi.getEm().find(InventoryItem.class, salesCart.getInventoryItem().getId());
                        inventoryItem.setQuantity(qtyAtHand);
                        crudApi.save(inventoryItem);

                        salesCart.genCode();
                        salesCart.setSalesCatalogue(salesCatalogue);
                        salesCart.setUserAccount(appSession.getCurrentUser());
                        salesCart.setCustomerPhone(customerPhone);
                        crudApi.save(salesCart);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                });

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Cart saved successfully!"), null));
            }

        }
    }

    public void printPosReceipt()
    {
        List<PosReceipt> invoiceItemList = new LinkedList<>();
        try
        {
            if(!cartList.isEmpty())
            {
                PosReceipt posReceipt = new PosReceipt();
                PosReceipt extractedItem = xtractService.extractToPosReceipt(cartList, posReceipt);
                
                invoiceItemList.add(extractedItem);
            }
            
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoiceItemList);
        
            InputStream coverStream = getClass().getResourceAsStream(ReportFiles.RECEIPT_FILE);
            reportHandler.reportParams.put("logo", ReportFiles.LOGO);
            JasperPrint receiptPrint = JasperFillManager.fillReport(coverStream, reportHandler.reportParams, dataSource);
            HttpServletResponse servletResponse = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            servletResponse.setContentType("application/pdf");
            ServletOutputStream servletStream = servletResponse.getOutputStream();
            JasperExportManager.exportReportToPdfStream(receiptPrint, servletStream);
            FacesContext.getCurrentInstance().responseComplete();
                
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
 
    public void processReceiptSMS()
    {
       receiptLink = "http://localhost:8080/icpsc-app/client-receipt.xhtml?id=";    
//       receiptLink = "http://209.145.49.185:8080/icpsc/client-receipt.xhtml?id=";    
        try 
        {

        try
        {
            ZenophSMS zsms = smsService.extractParams();

            String phoneNumber;
            if(customerPhone != null)
            {
                System.out.println("customerPhone => "+customerPhone);
                zsms.setMessage("Please click the link below to view your tranaction details:\n\n"+receiptLink+customerPhone);
                phoneNumber = customerPhone;
                
                System.out.println("Please click the link below to view your tranaction details:\n\n"+receiptLink+customerPhone);
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please select contact or enter phone number"), null));
                return;
            }
            
            List<String> numbers = zsms.extractPhoneNumbers(phoneNumber);

            for (String number : numbers)
            {
                zsms.addRecipient(number);
            }

            zsms.setSenderId("ICPSC_CHEM");

            List<String[]> response = zsms.submit();
            for (String[] destination : response)
            {
                REQSTATUS reqstatus = REQSTATUS.fromInt(Integer.parseInt(destination[0]));
                if (reqstatus == null)
                {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("failed to send message"), null));
                    break;
                } else
                {
                    switch (reqstatus)
                    {
                        case SUCCESS:
                            System.out.println("Message sent");
//                            FacesContext.getCurrentInstance().addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Message sent"), null));
                                saveMessage();
                            break;
                        case ERR_INSUFF_CREDIT:
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Insufficeint Credit"), null));
                        default:
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Failed to send message"), null));
                            return;
                    }
                }
            }

            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void saveMessage()
    {
        try
        {
            sms.setSmsTime(LocalDateTime.now());
            sms.setMessage("Please click the link below to view your tranaction details: "+receiptLink+customerPhone);
            sms.setMobileNo(customerPhone);
            sms.setsMSType(SMSType.SINGLE_SMS);
            sms.setUserAccount(appSession.getCurrentUser());
           if(crudApi.save(sms) != null)
           {
               FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("SMS sent to "+customerPhone), null));
               
               System.out.println("SMS sent and saved -- ");
           }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clear()
    {
        cart = new Cart();
        cart.setUserAccount(appSession.getCurrentUser());
        cart.setCompanyBranch(appSession.getCompanyBranch());
        cartList = new LinkedList<>();
        totalAmount = 0.0;
    }

    public void removeFromCart(Cart cart)
    {
        System.out.println("Before total amount => " + totalAmount);

        totalAmount -= (cart.getQuantity() * cart.getUnitPrice());
        cartList.remove(cart);

        System.out.println("\n\n");

        System.out.println("After total amount => " + totalAmount);
    }

    public List<Cart> getCartList()
    {
        return cartList;
    }

    public List<InventoryItem> getInventoryItemList()
    {
        return inventoryItemList;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public Sms getSms()
    {
        return sms;
    }

    public void setSms(Sms sms)
    {
        this.sms = sms;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getCustomerPhone()
    {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone)
    {
        this.customerPhone = customerPhone;
    }

}
