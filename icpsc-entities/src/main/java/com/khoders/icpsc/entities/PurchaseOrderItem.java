/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.entities;

import com.khoders.resource.utilities.SystemUtils;
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
    
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private Product product;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "total_amount")
    private double totalAmount;
    
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
    
    public void genCode()
    {
        if(getOrderItemCode()!= null)
        {
           setOrderItemCode(getOrderItemCode());
        }
        else
        {
            setOrderItemCode(SystemUtils.generateCode());
        }
    }
}
