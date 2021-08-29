/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.entities;

import com.khoders.icpsc.entities.UserAccountRecord;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "item_type")
public class ItemType extends UserAccountRecord implements Serializable
{
    @Column(name = "item_type_name")
    private String itemTypeName;

    public String getItemTypeName()
    {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName)
    {
        this.itemTypeName = itemTypeName;
    }

    @Override
    public String toString()
    {
        return itemTypeName;
    }
    
    
}
