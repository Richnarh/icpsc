/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans;

import com.khoders.icpsc.app.entities.Cart;
import com.khoders.icpsc.app.entities.SalesCatalogue;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.icpsc.app.services.InventoryService;
import com.khoders.icpsc.app.services.SalesService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

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
        salesCatalogueList = salesService.getSummaryInfo(dateRange);
    }

    public void manageSalesLog(SalesCatalogue salesCatalogue)
    {
        this.salesCatalogue = salesCatalogue;
        cartList = salesService.getSalesList(salesCatalogue);
    }
    
    public void profitPerMonth()
    {
        
        if(dateRange.getFromDate() == null || dateRange.getToDate() == null)
        {
            FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                Msg.setMsg("Please choose a date and click on search"), null));
                return;
        }

        String qryString = "SELECT SUM(e.profit) FROM Cart e WHERE e.userAccount=?1 AND e.valueDate BETWEEN ?2 AND ?3";
        Query query = crudApi.getEm().createQuery(qryString,  Cart.class);
            query.setParameter(1, appSession.getCurrentUser());
            query.setParameter(2, dateRange.getFromDate());
            query.setParameter(3, dateRange.getToDate());
            
            Double result = (Double) query.getSingleResult();
            
            if(result == null)
            {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                Msg.setMsg("Either date is not selected or there was no sales for this date(s)!"), null));
                return;
            }
            
            totalProfitSum=result;
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