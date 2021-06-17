/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "product")
public class Product extends UserAccountRecord implements Serializable
{
    @Column(name = "product_code")
    private String productCode;
    
    public static final String _productName = "productName";
    @Column(name = "product_name")
    private String productName;
    
    @Column(name = "product_location")
    private String productLocation;
    
    @Lob
    @Column(name = "description")
    private String description;
    
    @Column(name = "dose")
    private String dose;
    
    @Column(name = "frequency")
    private String frequency;
    
    @Column(name = "route")
    private String route;

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductLocation()
    {
        return productLocation;
    }

    public void setProductLocation(String productLocation)
    {
        this.productLocation = productLocation;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDose()
    {
        return dose;
    }

    public void setDose(String dose)
    {
        this.dose = dose;
    }

    public String getFrequency()
    {
        return frequency;
    }

    public void setFrequency(String frequency)
    {
        this.frequency = frequency;
    }

    public String getRoute()
    {
        return route;
    }

    public void setRoute(String route)
    {
        this.route = route;
    }

}
