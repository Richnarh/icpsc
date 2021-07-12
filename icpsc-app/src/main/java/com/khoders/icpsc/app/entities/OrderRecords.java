/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author richa
 */
@MappedSuperclass
public class OrderRecords extends UserAccountRecord implements Serializable
{
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "batch_number")
    private String batchNumber;
    
    @Column(name = "description")
    @Lob
    private String description;
    
    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;
    
    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getBatchNumber()
    {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber)
    {
        this.batchNumber = batchNumber;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }
    
   public void genCode()
    {
        if(getOrderId()!= null)
        {
           setOrderId(getOrderId());
        }
        else
        {
            setOrderId(SystemUtils.generateCode());
        }
    }
}
