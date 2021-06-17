/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder extends UserAccountRecord implements Serializable
{
    @Column(name = "purchase_order_code")
    private String purchaseOrderCode = SystemUtils.generateId();
    
    @Column(name = "total_amount")
    private double totalAmount;
    
    @Column(name = "company")
    private String company;
    
    @Column(name = "received_date")
    private LocalDate receivedDate = LocalDate.now();

    @Column(name = "posted_to_inventory")
    private boolean postedToInventory;
    
    @Column(name = "description")
    private String description;
    
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private Product product;

    public String getPurchaseOrderCode()
    {
        return purchaseOrderCode;
    }

    public void setPurchaseOrderCode(String purchaseOrderCode)
    {
        this.purchaseOrderCode = purchaseOrderCode;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }
    
    public LocalDate getReceivedDate()
    {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate)
    {
        this.receivedDate = receivedDate;
    }

    public boolean isPostedToInventory()
    {
        return postedToInventory;
    }

    public void setPostedToInventory(boolean postedToInventory)
    {
        this.postedToInventory = postedToInventory;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }
    
    
}
