/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.services;

import com.khoders.icpsc.entities.Cart;
import com.khoders.icpsc.entities.InventoryItem;
import com.khoders.icpsc.entities.Product;
import com.khoders.icpsc.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
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
    
    public List<InventoryItem> getInventoryProduct(Product product)
    {
        try
        {
           TypedQuery<InventoryItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM InventoryItem e WHERE e.product=?1 AND e.companyBranch=?2", InventoryItem.class);
                            typedQuery.setParameter(1, product);
                            typedQuery.setParameter(2, appSession.getCompanyBranch());
                            
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
    
    public List<Cart> getTransactionByDates(DateRangeUtil dateRange)
    {
        try 
        {
             if(dateRange.getFromDate() == null || dateRange.getToDate() == null)
            {
                  String  queryString = "SELECT e FROM Cart e WHERE e.userAccount=?1 ORDER BY e.valueDate DESC";
                  TypedQuery<Cart> typedQuery = crudApi.getEm().createQuery(queryString, Cart.class)
                                              .setParameter(1, appSession.getCompanyBranch());
                                    return typedQuery.getResultList();
            }
            
            String qryString = "SELECT e FROM Cart e WHERE e.valueDate BETWEEN ?1 AND ?2 AND e.userAccount=?3 ORDER BY e.valueDate DESC";
            
            TypedQuery<Cart> typedQuery = crudApi.getEm().createQuery(qryString, Cart.class)
                    .setParameter(1, dateRange.getFromDate())
                    .setParameter(2, dateRange.getToDate())
                    .setParameter(3, appSession.getCurrentUser());
           return typedQuery.getResultList();
           
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
