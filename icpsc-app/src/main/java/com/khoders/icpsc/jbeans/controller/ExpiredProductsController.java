/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.jbeans.controller;

import com.khoders.icpsc.services.InventoryService;
import com.khoders.icpsc.entities.InventoryItem;
import com.khoders.resource.utilities.DateRangeUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "expiredProductsController")
@SessionScoped
public class ExpiredProductsController implements Serializable
{
   @Inject private InventoryService inventoryService;
   
   private List<InventoryItem> expiredInventoryList = new LinkedList<>();
   
   private final DateRangeUtil dateRange = new DateRangeUtil();
   
   @PostConstruct
   public void init()
   { 
        String expiredYearMonth = dateRange.getYear()+dateRange.getMonth();   
        expiredInventoryList = inventoryService.getExpiredProductList(expiredYearMonth);
   }

    public List<InventoryItem> getExpiredInventoryList()
    {
        return expiredInventoryList;
    }
}
