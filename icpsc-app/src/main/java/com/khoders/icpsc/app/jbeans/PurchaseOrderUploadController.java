/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans;

import com.khoders.icpsc.app.entities.Product;
import com.khoders.icpsc.app.entities.PurchaseOrder;
import com.khoders.icpsc.app.entities.PurchaseOrderItem;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.resource.excel.FileExtension;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.NumberUtil;
import com.khoders.resource.utilities.SystemUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author khoders
 */
@Named(value = "purchaseOrderUploadController")
@SessionScoped
public class PurchaseOrderUploadController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    
    private PurchaseOrderItem purchaseOrderItem;
    private PurchaseOrder purchaseOrder = new PurchaseOrder();
    private List<PurchaseOrderItem> purchaseOrderItemList = new LinkedList<>();
    private UploadedFile file = null;
    
    public String getFileExtension(String filename) {
        if(filename == null)
        {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }
    
    public void uploadOrder()
    {

        try
        {
               String extension = getFileExtension(file.getFileName());
               
               System.out.println("file extention ==> "+extension);
               
//               if(!extension.equals(FileExtension.xls.name()) || !extension.equals(FileExtension.xlsx.name()))
//               {
//                 FacesContext.getCurrentInstance().addMessage(null, 
//                new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("File not supported!"), null));  
//                 return;
//               }
                
                InputStream inputStream = file.getInputStream();
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet  sheet =  workbook.getSheetAt(0);
                
                sheet.removeRow(sheet.getRow(0));
                
                Iterator<Row> iterator = sheet.iterator();
                System.out.println("Starting....");
                while(iterator.hasNext())
                {
                  purchaseOrderItem = new PurchaseOrderItem();
                  System.out.println("initializing....");
                  Row currentRow = iterator.next();
                  
                      System.out.println("going through cells....");
                      purchaseOrderItem.setOrderItemCode(NumberUtil.objToString(currentRow.getCell(0)));
                      purchaseOrderItem.setQuantity(NumberUtil.objToInteger(currentRow.getCell(1)));
                      purchaseOrderItem.setUnitPrice(NumberUtil.objToDouble(currentRow.getCell(2)));
                      purchaseOrderItem.setTotalAmount(NumberUtil.objToDouble(currentRow.getCell(3)));
                     
                      String prdt = NumberUtil.objToString(currentRow.getCell(4));
                      
                      Product product = crudApi.getEm().createQuery("SELECT e FROM Product e WHERE e.productName=?1", Product.class)
                                            .setParameter(1, prdt)
                                            .getResultStream().findFirst().orElse(null);
                      
                      if(product != null)
                      {
                        purchaseOrderItem.setProduct(product);
                      }
                      
                      purchaseOrderItemList.add(purchaseOrderItem);
                          
                      
                      System.out.println("Done!!!");
                      System.out.println("Size => "+purchaseOrderItemList.size());
                }
                workbook.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void saveUpload()
    {
        try
        {
            if(crudApi.save(purchaseOrder) != null)
            {
               purchaseOrderItemList.forEach(order ->{
                order.genCode();
                order.setUserAccount(appSession.getCurrentUser());
                order.setPurchaseOrder(purchaseOrder);
                crudApi.save(order);
            }); 
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("File upload successful!"), null));   
            }
            
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void clear()
    {
        purchaseOrderItemList = new LinkedList<>();
        file = null;
        purchaseOrder = new PurchaseOrder();
        SystemUtils.resetJsfUI();
    }
        
    public PurchaseOrderItem getPurchaseOrderItem()
    {
        return purchaseOrderItem;
    }

    public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem)
    {
        this.purchaseOrderItem = purchaseOrderItem;
    }

    public PurchaseOrder getPurchaseOrder()
    {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder)
    {
        this.purchaseOrder = purchaseOrder;
    }

    public UploadedFile getFile()
    {
        return file;
    }

    public void setFile(UploadedFile file)
    {
        this.file = file;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItemList()
    {
        return purchaseOrderItemList;
    }
}
