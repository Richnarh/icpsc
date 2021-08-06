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
 * @author khoders
 */
@Entity
@Table(name = "cart")
public class Cart extends UserAccountRecord implements Serializable{
    
    @Column(name = "cart_item_id")
    private String cartItemId;
    
    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "cost_price")
    private double costPrice;
    
    @Column(name = "profit")
    private double profit;
    
    @Column(name = "total")
    private double total;

    @JoinColumn(name = "customer")
    @ManyToOne
    private Customer customer;
    
    @JoinColumn(name = "sales_catalogue", referencedColumnName = "id")
    @ManyToOne
    private SalesCatalogue salesCatalogue;
    
    @JoinColumn(name = "inventory")
    @ManyToOne
    private InventoryItem inventoryItem;

    @Column(name = "description")
    private String description;
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
    }
    
    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCartItemId()
    {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId)
    {
        this.cartItemId = cartItemId;
    }
    
    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public InventoryItem getInventoryItem()
    {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem)
    {
        this.inventoryItem = inventoryItem;
    }

    public SalesCatalogue getSalesCatalogue()
    {
        return salesCatalogue;
    }

    public void setSalesCatalogue(SalesCatalogue salesCatalogue)
    {
        this.salesCatalogue = salesCatalogue;
    }

    public void genCode()
    {
        if(getCartItemId()!= null)
        {
           setCartItemId(getCartItemId());
        }
        else
        {
            setCartItemId(SystemUtils.generateCode());
        }
    }
}
