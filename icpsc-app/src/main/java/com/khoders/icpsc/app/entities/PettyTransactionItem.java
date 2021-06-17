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
@Table(name = "petty_transaction_item")
public class PettyTransactionItem extends UserAccountRecord implements Serializable{
   @Column(name = "quantity")
   private int quantity;
   
   @Column(name = "item_id")
   private String itemId;
   
   @JoinColumn(name = "petty_transaction")
   @ManyToOne
   private PettyTransaction pettyTransaction;
   
   @Column(name = "item_name")
   private String itemName;
      
   @Column(name = "unit_price")
   private double unitPrice;
   
   @Column(name = "discount")
   private double discount;
   
   @Column(name = "charges")
   private double charges;
   
   @Column(name = "amount")
   private double amount;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public PettyTransaction getPettyTransaction() {
        return pettyTransaction;
    }

    public void setPettyTransaction(PettyTransaction pettyTransaction) {
        this.pettyTransaction = pettyTransaction;
    }
    
   
    public void genCode()
    {
        if(getItemId() != null)
        {
           setItemId(getItemId());
        }
        else
        {
          setItemId(SystemUtils.generateCode());
        }
        
        
    }
   
}
