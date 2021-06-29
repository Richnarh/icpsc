/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans;

import com.khoders.icpsc.app.entities.ItemType;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.icpsc.app.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "itemTypeController")
@SessionScoped
public class ItemTypeController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;
    
    private ItemType itemType = new ItemType();
    private List<ItemType> itemTypeList = new LinkedList<>();
    
    private String optionText;
    
    @PostConstruct
    private void init()
    {
        clearItem();
        itemTypeList = inventoryService.getItemTypeList();
    }
    
    public void saveItemType()
    {
        try 
        {
            if(crudApi.save(itemType) != null)
            {
                itemTypeList = CollectionList.washList(itemTypeList, itemType);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.SUCCESS_MESSAGE, null));
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Oops! something went wrong, could not save item!"), null));
            }
            clearItem();
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void editItemType(ItemType itemType)
    {
        optionText = "Update";
        this.itemType = itemType;
    }
    
    public void deleteItemType(ItemType itemType)
    {
        try 
        {
         if(crudApi.delete(itemType))
         {
             itemTypeList.remove(itemType);
             FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Item deleted!"), null));
         }
         else
         {
             FacesContext.getCurrentInstance().addMessage(null, 
             new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Item deleted!"), null));
         }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void clearItem() 
    {
        itemType = new ItemType();
        itemType.setUserAccount(appSession.getCurrentUser());
        optionText = "Save";
        SystemUtils.resetJsfUI();
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public List<ItemType> getItemTypeList() {
        return itemTypeList;
    }

    public void setItemTypeList(List<ItemType> itemTypeList) {
        this.itemTypeList = itemTypeList;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
    
    
}
