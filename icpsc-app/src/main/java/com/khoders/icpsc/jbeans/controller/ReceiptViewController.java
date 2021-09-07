/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.jbeans.controller;

import com.khoders.icpsc.entities.Cart;
import com.khoders.icpsc.services.PosService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
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
   private List<Cart> receiptItemList = new LinkedList<>();
   
   double receiptTotal = 0.0;

    public void customer(String id)
    {
        System.out.println("ID => "+id);
        
        try
        {
            receiptItemList = posService.getClientReceipt(id);
            
            System.out.println("receiptItemList => "+receiptItemList.size());
            
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
