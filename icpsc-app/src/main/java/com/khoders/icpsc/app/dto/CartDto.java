/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.dto;

import java.time.LocalDate;

/**
 *
 * @author richa
 */
public class CartDto
{
    private String cartItemId;
    private int quantity;
    private double unitPrice;
    private String customerPhone;
    private String product;
    private String description;
    private String companyName;
    private LocalDate valueDate;

    public String getCartItemId()
    {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId)
    {
        this.cartItemId = cartItemId;
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

    public String getCustomerPhone()
    {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone)
    {
        this.customerPhone = customerPhone;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDate getValueDate()
    {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate)
    {
        this.valueDate = valueDate;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    
}
