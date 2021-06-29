/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans;

import com.khoders.icpsc.app.entities.Inventory;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.icpsc.app.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "stockController")
@SessionScoped
public class StockController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;
    
    private List<Inventory> inventoryStockList = new LinkedList<>();
    private List<Inventory> inventoryShortageStockList = new LinkedList<>();
    
    private LocalDate receivedDate;
    
    @PostConstruct
    private void init()
    {
        String qryString = "SELECT e FROM Inventory e WHERE e.userAccount=?1";
        inventoryStockList = crudApi
                            .getEm()
                            .createQuery(qryString, Inventory.class)
                            .setParameter(1, appSession.getCurrentUser())
                            .getResultList();
        
       inventoryShortageStockList =  inventoryService.getShortageList();
    }

    public void searchStock()
    {
        String query = "SELECT e FROM Inventory e WHERE e.userAccount=?1";
        try 
        {
           inventoryStockList = crudApi
                                .getEm()
                                .createQuery(query, Inventory.class)
                                .setParameter(1, appSession.getCurrentUser())
                                .getResultList();
           
           if(inventoryStockList.isEmpty())
           {
               init();
           }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    
    
    public List<Inventory> getInventoryStockList() {
        return inventoryStockList;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public List<Inventory> getInventoryShortageStockList() {
        return inventoryShortageStockList;
    }
 
    
}
