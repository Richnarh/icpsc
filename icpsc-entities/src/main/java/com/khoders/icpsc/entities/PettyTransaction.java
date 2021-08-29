/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.entities;

import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author khoders
 */
@Entity
@Table(name = "petty_transaction")
public class PettyTransaction extends UserAccountRecord implements Serializable{
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "receipt_no")
    private String receiptNo;
    
    @Column(name = "purchase_date")
    private LocalDate transactionDate;
    
    @Column(name = "errand_by")
    private String errandBy;
    
    @Column(name = "purpose")
    @Lob
    private String purpose;
    
    @Column(name = "note")
    @Lob
    private String note;
    
    @Column(name = "total_amount")
    private double totalAmount;
       
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getErrandBy() {
        return errandBy;
    }

    public void setErrandBy(String errandBy) {
        this.errandBy = errandBy;
    }
    
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
    
    public void genCode()
    {
       if(getTransactionId() != null)
        {
           setTransactionId(getTransactionId());
        }
        else
        {
          setTransactionId(SystemUtils.generateCode());
        }
        
    }
}
