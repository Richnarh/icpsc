/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans.controller;

import com.khoders.icpsc.entities.Cart;
import com.khoders.icpsc.entities.SalesCatalogue;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.icpsc.app.services.InventoryService;
import com.khoders.icpsc.app.services.SalesService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "salesLogController")
@SessionScoped
public class SalesLogController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private SalesService salesService;
    @Inject private InventoryService inventoryService;
    
    private SalesCatalogue salesCatalogue = new SalesCatalogue();
    private List<SalesCatalogue> salesCatalogueList = new LinkedList<>();
    private List<Cart> cartList = new LinkedList<>();
    private Cart cart= new Cart();
    DateRangeUtil dateRange = new DateRangeUtil();
    
    private double totalProfitSum = 0.0;
    private String month;
    
    public void fetchSummary()
    {
        salesCatalogueList = salesService.getSalesFromCatalogue(dateRange);
    }

    public void selectSale(SalesCatalogue salesCatalogue)
    {
        this.salesCatalogue = salesCatalogue;
        cartList = salesService.getSalesList(salesCatalogue);
    }
    
    public void profitPerMonth()
    {
        cartList = salesService.getCartList(dateRange);
        
        totalProfitSum = cartList.stream().mapToDouble(Cart::getProfit).sum();
        
        System.out.println("totalProfitSum => "+totalProfitSum);
    }
    
    public void resetPage()
    {
        totalProfitSum = 0.00;
        salesCatalogueList = new LinkedList<>();
        cartList = new LinkedList<>();
    }

    public List<Cart> getCartList() {
        return cartList;
    }
    
    public SalesCatalogue getSalesCatalogue() {
        return salesCatalogue;
    }

    public void setSalesCatalogue(SalesCatalogue salesCatalogue) {
        this.salesCatalogue = salesCatalogue;
    }

    public List<SalesCatalogue> getSalesCatalogueList() {
        return salesCatalogueList;
    }

    public void setSalesCatalogueList(List<SalesCatalogue> salesCatalogueList) {
        this.salesCatalogueList = salesCatalogueList;
    }

    public DateRangeUtil getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRangeUtil dateRange) {
        this.dateRange = dateRange;
    }

    public double getTotalProfitSum() {
        return totalProfitSum;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
}
