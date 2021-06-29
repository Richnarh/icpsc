/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import com.khoders.resource.enums.Months;
import com.khoders.resource.enums.UnitOfMeasurement;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "inventory")
public class Inventory extends OrderRecords implements Serializable
{
    @Column(name="posted_date")
    private LocalDate postedDate = LocalDate.now();
    
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private Product product;
    
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "selling_price")
    private double sellingPrice;
    
    @Column(name = "unit_price")
    private double unitPrice;
    
    @Column(name = "expiry_date")
    private String expiryDate;
    
    @Column(name = "potency_value")
    private int potencyValue;
    
    @Column(name = "unit")
    @Enumerated(EnumType.STRING)
    private UnitOfMeasurement unit;
    
    @Transient
    private Months expiryMonth;
    @Transient
    private long expiryYear;

    public LocalDate getPostedDate()
    {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate)
    {
        this.postedDate = postedDate;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public double getSellingPrice()
    {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice)
    {
        this.sellingPrice = sellingPrice;
    }

    public double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public String getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public int getPotencyValue()
    {
        return potencyValue;
    }

    public void setPotencyValue(int potencyValue)
    {
        this.potencyValue = potencyValue;
    }

    public UnitOfMeasurement getUnit()
    {
        return unit;
    }

    public void setUnit(UnitOfMeasurement unit)
    {
        this.unit = unit;
    }

    public Months getExpiryMonth()
    {
        return expiryMonth;
    }

    public void setExpiryMonth(Months expiryMonth)
    {
        this.expiryMonth = expiryMonth;
    }

    public long getExpiryYear()
    {
        return expiryYear;
    }

    public void setExpiryYear(long expiryYear)
    {
        this.expiryYear = expiryYear;
    }
    
    public String expiredString()
    {
        return expiryDate =  expiryMonth +"/"+ expiryYear+"";
    } 
    
    @Override
    public String toString() {
        return product+" "+potencyValue+" "+unit;
    }
}
