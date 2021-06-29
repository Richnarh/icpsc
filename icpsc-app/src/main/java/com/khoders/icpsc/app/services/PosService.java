/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.services;

import com.khoders.icpsc.app.entities.Inventory;
import com.khoders.icpsc.app.entities.InventoryItem;
import com.khoders.icpsc.app.entities.Product;
import com.khoders.icpsc.app.entities.PurchaseOrderItem;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author richa
 */
@Stateless
public class PosService
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    
    public List<Inventory> getInventoryProduct(Product product)
    {
        try
        {
           TypedQuery<Inventory> typedQuery = crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.inventory=?1 AND e.userAccount=?2", Inventory.class);
                            typedQuery.setParameter(1, product);
                            typedQuery.setParameter(2, appSession.getCurrentUser());
                            
                            return  typedQuery.getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
