/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans.controller;

import com.khoders.icpsc.app.entities.Inventory;
import com.khoders.icpsc.app.entities.InventoryItem;
import com.khoders.icpsc.app.entities.Inventory;
import com.khoders.icpsc.app.entities.InventoryItem;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.icpsc.app.services.InventoryService;
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
 * @author richard
 */
@Named(value = "inventoryController")
@SessionScoped
public class InventoryController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;
    
    private Inventory inventory = new Inventory();
    private List<Inventory> inventoryList = new LinkedList<>();
    
    private InventoryItem inventoryItem = new InventoryItem();
    private List<InventoryItem> inventoryItemList = new LinkedList<>();
    private List<InventoryItem> removedOrderItemList = new LinkedList<>();
    
    private FormView pageView = FormView.listForm();
    private String optionText;
    
     private double totalAmount = 0.0;
    
    @PostConstruct
    private void init()
    {
        inventoryList = inventoryService.getInventoryList();
        
        clearInventory();
    }
    
    public void initInventory()
    {
        clearInventory();
        pageView.restToCreateView();
    }
    
    public void saveInventory()
    {
        try
        {
           if(crudApi.save(inventory)!=null)
           {
               inventoryList = CollectionList.washList(inventoryList, inventory);
               FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Purchase order saved"), null)); 
               
               closePage();
           }
           
           clearInventory();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
  public void deleteInventory(Inventory inventory)
    {
        try 
        {
          if(crudApi.delete(inventory))
          {
              inventoryList.remove(inventory);
              
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
    public void manageInventoryItem(Inventory inventory)
    {
        this.inventory = inventory;
        pageView.restToDetailView();
        totalAmount = 0.0;
        clearInventoryItem();
        
        inventoryItemList = inventoryService.getInventoryItem(inventory);
              
        for (InventoryItem items : inventoryItemList) 
        {
            totalAmount += (items.getQuantity() * items.getCostPrice());
        }
       
    }

    public void addInventoryItem()
    {
        try
        {
            Predicate<InventoryItem> predicate = invItem -> invItem.getQuantity() <= 0;
            
            if(predicate.test(inventoryItem))
            {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please enter quantity"), null));
                return;
            }
            
//            if (inventoryItem.getQuantity() <= 0)
//            {
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please enter quantity"), null));
//                return;
//            }
            
            if (inventoryItem.getCostPrice() <= 0.0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please enter price"), null));
                return;
            }

            if (inventoryItem != null) {
               
                totalAmount += inventoryItem.getQuantity() * inventoryItem.getCostPrice();
                
                inventoryItemList.add(inventoryItem);
                inventoryItemList = CollectionList.washList(inventoryItemList, inventoryItem);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Order item added"), null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Order item removed!"), null));
            }
            clearInventoryItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveAll()
    {
        try 
        {
            for (InventoryItem orderItem : inventoryItemList)
            {
//                if (totalAmount != inventory.getTotalAmount())
//                {
//                    FacesContext.getCurrentInstance().addMessage(null,
//                            new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("The item total sum: " + (totalAmount) + " is not equivalent to the purchase order total: " + inventory.getTotalAmount()), null));
//                        return;
//                }
                orderItem.genCode();
                crudApi.save(orderItem);
            }

            for (InventoryItem orderItem : removedOrderItemList)
            {
                crudApi.delete(orderItem);
                removedOrderItemList.remove(orderItem);
            }
            System.out.println("Remove order size after -- " + removedOrderItemList.size());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Inventory order item list saved!"), null));

        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void editInventoryItem(InventoryItem inventoryItem)
    {
        this.inventoryItem = inventoryItem;
        totalAmount -= (inventoryItem.getQuantity() * inventoryItem.getCostPrice());
        inventoryItemList.remove(inventoryItem);
    }
    
    public void removeInventoryItem(InventoryItem inventoryItem)
    {
        totalAmount -= (inventoryItem.getQuantity() * inventoryItem.getCostPrice());
        removedOrderItemList = CollectionList.washList(removedOrderItemList, inventoryItem);
        inventoryItemList.remove(inventoryItem);
        
        System.out.println("Size on removing --- "+removedOrderItemList.size());
    }
  
    public void editInventory(Inventory inventory)
    {
       pageView.restToCreateView();
       this.inventory=inventory;
       optionText = "Update";
    }
    
    public void clearInventory() 
    {
        inventory = new Inventory();
        inventory.setUserAccount(appSession.getCurrentUser());
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }
    
    public void closePage()
    {
       inventory = new Inventory();
       optionText = "Save Changes";
       pageView.restToListView();
    }
    public void clearInventoryItem()
    {
        inventoryItem = new InventoryItem();
        optionText = "Save Changes";
        inventoryItem.setInventory(inventory);
        inventoryItem.setUserAccount(appSession.getCurrentUser());
        SystemUtils.resetJsfUI();
    }
    
    public Inventory getInventory()
    {
        return inventory;
    }

    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
    }

    public InventoryItem getInventoryItem()
    {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem)
    {
        this.inventoryItem = inventoryItem;
    }

    public List<Inventory> getInventoryList()
    {
        return inventoryList;
    }

    public List<InventoryItem> getInventoryItemList()
    {
        return inventoryItemList;
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
