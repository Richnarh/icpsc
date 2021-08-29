/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.entities;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "purchase_order")
public class PurchaseOrder extends OrderRecords implements Serializable
{
    @Column(name = "received_date")
    private LocalDate receivedDate;
    

    @Column(name = "posted_to_inventory")
    private boolean postedToInventory;

    public LocalDate getReceivedDate()
    {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate)
    {
        this.receivedDate = receivedDate;
    }

    public boolean isPostedToInventory()
    {
        return postedToInventory;
    }

    public void setPostedToInventory(boolean postedToInventory)
    {
        this.postedToInventory = postedToInventory;
    }
 
}
