/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.services;

import com.khoders.icpsc.app.entities.ItemType;
import com.khoders.resource.jpa.CrudApi;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author richa
 */
@Stateless
public class InventoryService
{
    @Inject private CrudApi crudApi;
    
    public List<ItemType> getItemtypeList()
    {
        try
        {
//            return crudApi.getEm().createQuery(deleteQuery)
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
