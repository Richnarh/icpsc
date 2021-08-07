/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import com.khoders.resource.enums.Months;
import com.khoders.resource.enums.UnitOfMeasurement;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "inventory_item")
public class InventoryItem extends UserAccountRecord implements Serializable
{
    @Column(name = "order_item_id")
    private String orderItemId;
    
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private Product product;
    
    @JoinColumn(name = "inventory", referencedColumnName = "id")
    @ManyToOne
    private Inventory inventory;
    
    @Column(name = "total_price")
    private double totalPrice;
    
    @Column(name = "quantity")
    private int quantity;

    @Column(name="description")
    @Lob
    private String description;
    
    @Column(name = "selling_price")
    private double sellingPrice;
    
    @Column(name = "cost_price")
    private double costPrice;
    
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

    public String getOrderItemId()
    {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId)
    {
        this.orderItemId = orderItemId;
    }

    public double getCostPrice()
    {
        return costPrice;
    }

    public void setCostPrice(double costPrice)
    {
        this.costPrice = costPrice;
    }
    
    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public String getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
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

    public Inventory getInventory()
    {
        return inventory;
    }

    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
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
    public void genCode()
    {
        if(getOrderItemId()!= null)
        {
           setOrderItemId(getOrderItemId());
        }
        else
        {
            setOrderItemId(SystemUtils.generateCode());
        }
    }
    @Override
    public String toString() {
        return product+" "+potencyValue+" "+unit;
    }
}
