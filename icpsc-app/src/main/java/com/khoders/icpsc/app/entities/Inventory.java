/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import java.io.Serializable;
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
@Table(name = "inventory_item")
public class Inventory extends UserAccountRecord implements Serializable
{
    @Column(name="inventory_id")
    private String inventoryId;
    
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private Product product;
    
    @Column(name = "total_amount")
    private double totalAmount;

    public String getInventoryId()
    {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId)
    {
        this.inventoryId = inventoryId;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }
    
    
}
