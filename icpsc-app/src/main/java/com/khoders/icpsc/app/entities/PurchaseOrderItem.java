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
@Table(name = "purchase_order_item")
public class PurchaseOrderItem extends UserAccountRecord implements Serializable
{
    @Column(name = "order_item_code")
    private String orderItemCode;
    
    @JoinColumn(name = "purchase_order", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder purchaseOrder;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "unit_price")
    private double unitPrice;

    public String getOrderItemCode()
    {
        return orderItemCode;
    }

    public void setOrderItemCode(String orderItemCode)
    {
        this.orderItemCode = orderItemCode;
    }

    public PurchaseOrder getPurchaseOrder()
    {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder)
    {
        this.purchaseOrder = purchaseOrder;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
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

}
