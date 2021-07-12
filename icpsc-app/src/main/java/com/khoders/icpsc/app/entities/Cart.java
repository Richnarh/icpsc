/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
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
    
    @Column(name = "cart_id")
    private String cartId;
    
    @Column(name = "receipt_number")
    private String receiptNumber = SystemUtils.generateRefNo();
    
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

    @JoinColumn(name = "product")
    @ManyToOne
    private Product product;

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
    
    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public String getReceiptNumber()
    {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber)
    {
        this.receiptNumber = receiptNumber;
    }
    
    
    public void genCode()
    {
        if(getCartId()!= null)
        {
           setCartId(getCartId());
        }
        else
        {
            setCartId(SystemUtils.generateCode());
        }
    }
}
