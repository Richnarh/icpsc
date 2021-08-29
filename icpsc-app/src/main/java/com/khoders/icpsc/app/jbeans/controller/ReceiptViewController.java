/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans.controller;

import com.khoders.icpsc.entities.Cart;
import com.khoders.icpsc.app.services.PosService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "receiptViewController")
@ViewScoped
public class ReceiptViewController implements Serializable
{
    @Inject private PosService posService;
   private String userId;
   private List<Cart> receiptItemList = new LinkedList<>();
   
   double receiptTotal = 0.0;
        
    @PostConstruct
    private void init()
    {
            fetchClientReceipt();
            System.out.println("Over here");
       if(userId != null)
        {
        }
       
    }
    
    public void customer(String id)
    {
        userId = id;
        System.out.println("UserID => "+userId);
        System.out.println("ID => "+id);
        
        fetchClientReceipt();
    }
    
    public void fetchClientReceipt()
    {
        System.out.println("Over here");
        System.out.println("receiptItemList => "+receiptItemList.size());
        try
        {
            receiptItemList = posService.getClientReceipt(userId);
            
            for (Cart cart : receiptItemList) {
                receiptTotal+=(cart.getQuantity() * cart.getUnitPrice());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
         
    }
        
    public List<Cart> getReceiptItemList()
    {
        return receiptItemList;
    }

    public double getReceiptTotal()
    {
        return receiptTotal;
    }

}
