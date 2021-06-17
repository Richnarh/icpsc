/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.commons;

import com.khoders.icpsc.app.entities.ItemType;
import com.khoders.resource.jpa.CrudApi;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "usercommonClazz")
@SessionScoped
public class UsercommonClazz implements Serializable
{
    @Inject private CrudApi crudApi;
    private List<ItemType> itemtypeList = new LinkedList<>();
    
    @PostConstruct
    private void init()
    {
        
    }
    
    
}
