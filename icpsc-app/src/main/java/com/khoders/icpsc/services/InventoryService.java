/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.services;

import com.khoders.icpsc.entities.Customer;
import com.khoders.icpsc.entities.Inventory;
import com.khoders.icpsc.entities.InventoryItem;
import com.khoders.icpsc.entities.ItemType;
import com.khoders.icpsc.entities.Product;
import com.khoders.icpsc.entities.PurchaseOrder;
import com.khoders.icpsc.entities.PurchaseOrderItem;
import com.khoders.icpsc.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author pascal
 */
@Stateless
public class InventoryService
{
     private @Inject CrudApi crudApi;
     private @Inject AppSession appSession;
     
    public List<Product> getProductList()
    {
        try
        {
            String qryString = "SELECT e FROM Product e WHERE e.companyBranch=?1";
            TypedQuery<Product> typedQuery = crudApi.getEm().createQuery(qryString, Product.class)
                                .setParameter(1, appSession.getCompanyBranch());
                            return typedQuery.getResultList();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    
    
    public List<Inventory> getInventoryList()
    {
        try
        {
            String qryString = "SELECT e FROM Inventory e WHERE e.companyBranch=?1";
            TypedQuery<Inventory> typedQuery = crudApi.getEm().createQuery(qryString, Inventory.class);
                            typedQuery.setParameter(1, appSession.getCompanyBranch());
                            return typedQuery.getResultList();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    
    public List<PurchaseOrder> getPurchaseOrderList()
    {
        try
        {
            String qryString = "SELECT e FROM PurchaseOrder e WHERE e.companyBranch=?1";
            TypedQuery<PurchaseOrder> typedQuery = crudApi.getEm().createQuery(qryString, PurchaseOrder.class);
                                   typedQuery.setParameter(1, appSession.getCompanyBranch());
                            return typedQuery.getResultList();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    
    public List<PurchaseOrderItem> getPurchaseOrderItem(PurchaseOrder purchaseOrder)
    {
        try
        {
           TypedQuery<PurchaseOrderItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM PurchaseOrderItem e WHERE e.purchaseOrder=?1 AND e.companyBranch=?2", PurchaseOrderItem.class);
                            typedQuery.setParameter(1, purchaseOrder);
                            typedQuery.setParameter(2, appSession.getCompanyBranch());
                            
                            return  typedQuery.getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<InventoryItem> getInventoryItem(Inventory inventory)
    {
        try
        {
           TypedQuery<InventoryItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM InventoryItem e WHERE e.inventory=?1 AND e.companyBranch=?2", InventoryItem.class);
                            typedQuery.setParameter(1, inventory);
                            typedQuery.setParameter(2, appSession.getCompanyBranch());
                            
                            return  typedQuery.getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<InventoryItem> getInventoryItemList()
    {
        try
        {
           TypedQuery<InventoryItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM InventoryItem e WHERE  e.companyBranch=?1", InventoryItem.class);
                            typedQuery.setParameter(1, appSession.getCompanyBranch());
                            
                            return  typedQuery.getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<Customer> getCustomerList()
    {
        try
        {
            String qryString = "SELECT e FROM Customer e WHERE e.companyBranch=?1";
            TypedQuery<Customer> typedQuery = crudApi.getEm().createQuery(qryString, Customer.class);
                                typedQuery.setParameter(1, appSession.getCompanyBranch());
                            return typedQuery.getResultList();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    
    public List<ItemType> getItemTypeList()
    {
        try
        {
            String qryString = "SELECT e FROM ItemType e WHERE e.companyBranch=?1";
            TypedQuery<ItemType> typedQuery = crudApi.getEm().createQuery(qryString, ItemType.class);
                                typedQuery.setParameter(1, appSession.getCompanyBranch());
                            return typedQuery.getResultList();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    
    public List<InventoryItem> getShortageList()
    {
        String qryString = "SELECT e FROM InventoryItem e WHERE e.companyBranch=?1 AND e.quantity <= 5";
        try {
            
            TypedQuery<InventoryItem> typedQuery = crudApi.getEm().createQuery(qryString, InventoryItem.class);
               typedQuery.setParameter(1, appSession.getCompanyBranch());
               
               return typedQuery.getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<InventoryItem> getExpiredProductList(String expiredMonthYear)
    {
        String qryString = "SELECT e FROM InventoryItem e WHERE e.companyBranch=?1 AND EXTRACT(YEAR_MONTH FROM e.expiryDate) <= ?2";
              
        try {
            
            TypedQuery<InventoryItem> typedQuery = crudApi.getEm().createQuery(qryString, InventoryItem.class);
               typedQuery.setParameter(1, appSession.getCompanyBranch());
               typedQuery.setParameter(2, expiredMonthYear);
               
               return typedQuery.getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<InventoryItem> getExpiredProductList(String companyId, String expiredMonthYear)
    {
        String qryString = "SELECT e FROM InventoryItem e WHERE e.companyBranch=?1 AND EXTRACT(YEAR_MONTH FROM e.expiryDate) <= ?2";
              
        try {
            
            TypedQuery<InventoryItem> typedQuery = crudApi.getEm().createQuery(qryString, InventoryItem.class);
            typedQuery.setParameter(1, companyId);
            typedQuery.setParameter(2, expiredMonthYear);
               
               return typedQuery.getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
