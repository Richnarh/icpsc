/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.services;

import com.khoders.icpsc.app.entities.Cart;
import com.khoders.icpsc.app.entities.SalesCatalogue;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.resource.utilities.Msg;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author richa
 */
@Stateless
public class SalesService
{
    @Inject CrudApi crudApi;
    @Inject AppSession appSession;
    
    public List<SalesCatalogue> getSalesFromCatalogue(DateRangeUtil dateRange)
    {
        try 
        {  
            if(dateRange.getFromDate() == null || dateRange.getToDate() == null)
                {
                  String  query = "SELECT e FROM SalesCatalogue e WHERE e.userAccount=?1";
                  TypedQuery<SalesCatalogue> typedQuery = crudApi.getEm().createQuery(query, SalesCatalogue.class)
                          .setParameter(1, appSession.getCurrentUser());

                return typedQuery.getResultList();
            }
            
            String query = "SELECT e FROM SalesCatalogue e WHERE e.userAccount=?1 AND e.valueDate BETWEEN ?2 AND ?3";
            TypedQuery<SalesCatalogue> typedQuery = crudApi.getEm().createQuery(query, SalesCatalogue.class)
                            .setParameter(1, appSession.getCurrentUser())
                            .setParameter(2, dateRange.getFromDate())
                            .setParameter(3, dateRange.getToDate());
            
                return typedQuery.getResultList();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
       return Collections.emptyList();
    }
    
    public List<Cart> getSalesList(SalesCatalogue salesCatalogue)
    {
        String qryString = "SELECT e FROM Cart e WHERE e.userAccount=?1 AND e.salesCatalogue=?2";
        try 
        {
            TypedQuery<Cart> typedQuery = crudApi.getEm().createQuery(qryString, Cart.class);
                    typedQuery.setParameter(1, appSession.getCurrentUser());
                    typedQuery.setParameter(2, salesCatalogue);
                    
                return typedQuery.getResultList();
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
   public List<Cart> getCartList(DateRangeUtil dateRange)
   {
       try
       {
        if(dateRange.getFromDate() == null || dateRange.getToDate() == null)
        {
            String  query = "SELECT e FROM Cart e WHERE e.userAccount=?1";
                  TypedQuery<Cart> typedQuery = crudApi.getEm().createQuery(query, Cart.class)
                          .setParameter(1, appSession.getCurrentUser());

                return typedQuery.getResultList();
        }

        String qryString = "SELEC e FROM Cart e WHERE e.userAccount=?1 AND e.valueDate BETWEEN ?2 AND ?3";
        TypedQuery<Cart> query = crudApi.getEm().createQuery(qryString,  Cart.class);
            query.setParameter(1, appSession.getCurrentUser());
            query.setParameter(2, dateRange.getFromDate());
            query.setParameter(3, dateRange.getToDate());
            
            return query.getResultList();
            
       } catch (Exception e)
       {
           e.printStackTrace();
       }
       return Collections.emptyList();
   }
}
