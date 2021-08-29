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
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "inventory")
public class Inventory extends OrderRecords implements Serializable
{
    @Column(name="posted_date")
    private LocalDate postedDate = LocalDate.now();
    
    public LocalDate getPostedDate()
    {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate)
    {
        this.postedDate = postedDate;
    }

}
