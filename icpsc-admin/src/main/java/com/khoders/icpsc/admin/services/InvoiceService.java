/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.admin.services;

import com.khoders.icpsc.entities.CompanyBranch;
import com.khoders.icpsc.entities.InventoryItem;
import com.khoders.resource.jpa.CrudApi;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author richa
 */
@Stateless
public class InvoiceService
{
    @Inject private CrudApi crudApi;
     
    public List<InventoryItem> getStockList(CompanyBranch companyBranch)
    {
        try
        {
          String qryString = "SELECT e FROM InventoryItem e WHERE e.companyBranch=?1 ORDER BY e.product ASC";
          return crudApi.getEm().createQuery(qryString, InventoryItem.class)
                  .setParameter(1, companyBranch)
                  .getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return Collections.emptyList();
    }
    
    public List<InventoryItem> getStockShortageList(CompanyBranch companyBranch)
    {
        try
        {
          String qryString = "SELECT e FROM InventoryItem e WHERE e.companyBranch=?1 AND e.quantity <= 1 ORDER BY e.product ASC";
          return crudApi.getEm().createQuery(qryString, InventoryItem.class)
                  .setParameter(1, companyBranch)
                  .getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return Collections.emptyList();
    }
}
