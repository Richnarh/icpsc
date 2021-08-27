/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.services;

import com.khoders.icpsc.app.entities.Cart;
import com.khoders.icpsc.app.entities.InventoryItem;
import com.khoders.icpsc.app.entities.Product;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import static org.apache.commons.math3.stat.StatUtils.product;

/**
 *
 * @author richa
 */
@Stateless
public class PosService
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    
    public List<InventoryItem> getInventoryProduct(Product product)
    {
        try
        {
           TypedQuery<InventoryItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM InventoryItem e WHERE e.product=?1 AND e.userAccount=?2", InventoryItem.class);
                            typedQuery.setParameter(1, product);
                            typedQuery.setParameter(2, appSession.getCurrentUser());
                            
                            return  typedQuery.getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<Cart> getClientReceipt(String id)
    {
        String qryString = "SELECT e FROM Cart e WHERE e.customerPhone=?1";
        try 
        {
            TypedQuery<Cart> typedQuery = crudApi.getEm().createQuery(qryString, Cart.class);
                    typedQuery.setParameter(1, id);
                    
                return typedQuery.getResultList();
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
