/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.jbeans.controller;

import com.khoders.icpsc.entities.Inventory;
import com.khoders.icpsc.entities.InventoryItem;
import com.khoders.icpsc.entities.PurchaseOrder;
import com.khoders.icpsc.entities.PurchaseOrderItem;
import com.khoders.icpsc.listener.AppSession;
import com.khoders.icpsc.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.FormView;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "purchaseOrderController")
@SessionScoped
public class PurchaseOrderController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;
    
    private PurchaseOrder purchaseOrder = new PurchaseOrder();
    private List<PurchaseOrder> purchaseOrderList = new LinkedList<>();
    
    private PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
    private List<PurchaseOrderItem> purchaseOrderItemList = new LinkedList<>();
    private List<PurchaseOrderItem> removedOrderItemList = new LinkedList<>();
    
    private FormView pageView = FormView.listForm();
    private String optionText;
    
     private double totalAmount = 0.0;
    
    @PostConstruct
    private void init()
    {
        purchaseOrderList = inventoryService.getPurchaseOrderList();
        
        clearPurchaseOrder();
    }
    
    public void initPurchaseOrder()
    {
        clearPurchaseOrder();
        pageView.restToCreateView();
    }
    
    public void savePurchaseOrder()
    {
        try
        {
           if(crudApi.save(purchaseOrder)!=null)
           {
               purchaseOrderList = CollectionList.washList(purchaseOrderList, purchaseOrder);
               FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Purchase order saved"), null)); 
               
               closePage();
           }
           
           clearPurchaseOrder();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
  public void deletePurchaseOrder(PurchaseOrder purchaseOrder)
    {
        try 
        {
          if(crudApi.delete(purchaseOrder))
          {
              purchaseOrderList.remove(purchaseOrder);
              
              FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.SUCCESS_MESSAGE, null)); 
          }
          else
          {
              FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.FAILED_MESSAGE, null));
          }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    public void managePurchaseOrderItem(PurchaseOrder purchaseOrder)
    {
        this.purchaseOrder = purchaseOrder;
        pageView.restToDetailView();
        totalAmount = 0.0;
        clearPurchaseOrderItem();
        
        purchaseOrderItemList = inventoryService.getPurchaseOrderItem(purchaseOrder);
              
        for (PurchaseOrderItem items : purchaseOrderItemList) 
        {
            totalAmount += (items.getQuantity() * items.getUnitPrice());
        }
       
    }

    public void addPurchaseOrderItem()
    {
        try
        {
            Predicate<PurchaseOrderItem> predicate = orderItem -> orderItem.getQuantity() <= 0;
            
            if(predicate.test(purchaseOrderItem))
            {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please enter quantity"), null));
                return;
            }
            
//            if (purchaseOrderItem.getQuantity() <= 0)
//            {
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please enter quantity"), null));
//                return;
//            }
            
            if (purchaseOrderItem.getUnitPrice() <= 0.0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please enter price"), null));
                return;
            }

            if (purchaseOrderItem != null) {
               
                totalAmount += purchaseOrderItem.getQuantity() * purchaseOrderItem.getUnitPrice();
                
                purchaseOrderItemList.add(purchaseOrderItem);
                purchaseOrderItemList = CollectionList.washList(purchaseOrderItemList, purchaseOrderItem);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Order item added"), null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Order item removed!"), null));
            }
            clearPurchaseOrderItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveAll()
    {
        try 
        {
            for (PurchaseOrderItem orderItem : purchaseOrderItemList)
            {
//                if (totalAmount != purchaseOrder.getTotalAmount())
//                {
//                    FacesContext.getCurrentInstance().addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("The item total sum: " + (totalAmount) + " is not equivalent to the purchase order total: " + purchaseOrder.getTotalAmount()), null));
//                        return;
//                }
                crudApi.save(orderItem);
            }

            for (PurchaseOrderItem orderItem : removedOrderItemList)
            {
                crudApi.delete(orderItem);
                removedOrderItemList.remove(orderItem);
            }
            System.out.println("Remove order size after -- " + removedOrderItemList.size());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Purchase order item list saved!"), null));

        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void editPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem)
    {
        this.purchaseOrderItem = purchaseOrderItem;
        totalAmount -= (purchaseOrderItem.getQuantity() * purchaseOrderItem.getUnitPrice());
        purchaseOrderItemList.remove(purchaseOrderItem);
    }
    
    public void removePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem)
    {
        totalAmount -= (purchaseOrderItem.getQuantity() * purchaseOrderItem.getUnitPrice());
        removedOrderItemList = CollectionList.washList(removedOrderItemList, purchaseOrderItem);
        purchaseOrderItemList.remove(purchaseOrderItem);
        
        System.out.println("Size on removing --- "+removedOrderItemList.size());
    }
    
    public void postToInventory(PurchaseOrder purchaseOrder)
    {
        InventoryItem inventoryItem = null;
        
        try
        {
             List<PurchaseOrderItem> orderItemList = inventoryService.getPurchaseOrderItem(purchaseOrder);
             
             if(orderItemList.isEmpty())
             {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please add order items before posting!."), null));  
                 return; 
             }
             
             double itemCost = 0.0;
             
            for (PurchaseOrderItem items : orderItemList)
            {
                itemCost += (items.getQuantity() * items.getUnitPrice());
            }
            
//             if(purchaseOrder.getTotalAmount() != itemCost)
//             {
//                 FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("The total cost of items is different from the entered total amount."), null));  
//                 return; 
//             }
             
            for (PurchaseOrderItem item : orderItemList)
            {
                Inventory inventory = new Inventory();
                inventory.setOrderId(purchaseOrder.getOrderId());
                inventory.setPostedDate(purchaseOrder.getReceivedDate());
                inventory.setTotalAmount(purchaseOrder.getTotalAmount());
                inventory.setBatchNumber(purchaseOrder.getBatchNumber());
                inventory.setDescription(purchaseOrder.getDescription());
                
                if(crudApi.save(inventory) != null)
                {
                    inventoryItem = new InventoryItem();
                    inventoryItem.setOrderItemId(item.getOrderItemCode());
                    inventoryItem.setTotalPrice(itemCost);
                    inventoryItem.setProduct(item.getProduct());
                    inventoryItem.setDescription(purchaseOrder.getDescription());
                    inventoryItem.setQuantity(item.getQuantity());
                    inventoryItem.setCostPrice(item.getUnitPrice());
                    inventoryItem.setUserAccount(appSession.getCurrentUser());

                    crudApi.save(inventoryItem);  
                }
                
            }
            
        if(inventoryItem != null)
        {
            purchaseOrder = crudApi.getEm().find(PurchaseOrder.class, purchaseOrder.getId());
            purchaseOrder.setPostedToInventory(true);
            crudApi.save(purchaseOrder);
            
            init();
            
           FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Purchase order posted to inventory successfully!"), null));  
           
        }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editPurchaseOrder(PurchaseOrder purchaseOrder)
    {
       pageView.restToCreateView();
       this.purchaseOrder=purchaseOrder;
       optionText = "Update";
    }
    
    public void clearPurchaseOrder() 
    {
        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserAccount(appSession.getCurrentUser());
        purchaseOrder.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }
    
    public void closePage()
    {
       purchaseOrder = new PurchaseOrder();
       optionText = "Save Changes";
       pageView.restToListView();
    }
    public void clearPurchaseOrderItem()
    {
        purchaseOrderItem = new PurchaseOrderItem();
        optionText = "Save Changes";
        purchaseOrderItem.setPurchaseOrder(purchaseOrder);
        purchaseOrderItem.setUserAccount(appSession.getCurrentUser());
        SystemUtils.resetJsfUI();
    }
    
    public PurchaseOrder getPurchaseOrder()
    {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder)
    {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrderItem getPurchaseOrderItem()
    {
        return purchaseOrderItem;
    }

    public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem)
    {
        this.purchaseOrderItem = purchaseOrderItem;
    }

    public List<PurchaseOrder> getPurchaseOrderList()
    {
        return purchaseOrderList;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItemList()
    {
        return purchaseOrderItemList;
    }

    public FormView getPageView()
    {
        return pageView;
    }

    public void setPageView(FormView pageView)
    {
        this.pageView = pageView;
    }

    public String getOptionText()
    {
        return optionText;
    }

    public void setOptionText(String optionText)
    {
        this.optionText = optionText;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }
    
    
}
