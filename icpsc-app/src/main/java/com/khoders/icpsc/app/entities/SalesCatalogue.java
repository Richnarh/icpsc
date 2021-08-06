/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author khoders
 */
@Entity
@Table(name = "sales_catalogue")
public class SalesCatalogue extends UserAccountRecord implements Serializable{
    @Column(name = "catalogue_Id")
    private String catalogueId;
    
    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;
    
    @Column(name = "receipt_number")
    private String receiptNumber = SystemUtils.generateRefNo();
    
    @Column(name = "total_amount")
    private double totalAmount;
    
    public LocalDateTime getPurchaseDate()
    {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }
   
    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
    
    public String getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(String catalogueId) {
        this.catalogueId = catalogueId;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }
    
   public void genCode()
    {
        if(getCatalogueId()!= null)
        {
           setCatalogueId(getCatalogueId());
        }
        else
        {
            setCatalogueId(SystemUtils.generateCode());
        }
    }
}
