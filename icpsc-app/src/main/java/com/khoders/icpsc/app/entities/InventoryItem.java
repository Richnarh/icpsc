/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "inventory_item")
public class InventoryItem extends UserAccountRecord implements Serializable
{
    
}
