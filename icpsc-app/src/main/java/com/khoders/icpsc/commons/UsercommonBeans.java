/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.commons;

import com.khoders.icpsc.entities.Customer;
import com.khoders.icpsc.entities.ItemType;
import com.khoders.icpsc.entities.Product;
import com.khoders.icpsc.entities.sms.MessageTemplate;
import com.khoders.icpsc.entities.sms.SenderId;
import com.khoders.icpsc.services.InventoryService;
import com.khoders.icpsc.services.SmsService;
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
@Named(value = "usercommonBeans")
@SessionScoped
public class UsercommonBeans implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private InventoryService inventoryService;
    @Inject private SmsService smsService;
    
    private List<ItemType> itemtypeList = new LinkedList<>();
    private List<Customer> customerList = new LinkedList<>();
    private List<Product> productList = new LinkedList<>();
    private List<SenderId> senderIdList = new LinkedList<>();
    private List<MessageTemplate> messageTemplateList = new LinkedList<>();
    
    @PostConstruct
    public void init()
    {
       customerList = inventoryService.getCustomerList();
       productList = inventoryService.getProductList();
       itemtypeList = inventoryService.getItemTypeList();
       senderIdList = smsService.getSenderIdList();
       messageTemplateList = smsService.getMessageTemplateList();
    }

    public List<ItemType> getItemtypeList()
    {
        return itemtypeList;
    }

    public List<Customer> getCustomerList()
    {
        return customerList;
    }

    public List<Product> getProductList()
    {
        return productList;
    }

    public List<SenderId> getSenderIdList()
    {
        return senderIdList;
    }

    public List<MessageTemplate> getMessageTemplateList()
    {
        return messageTemplateList;
    }
    
}
