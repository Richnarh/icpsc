/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.services;

import com.khoders.icpsc.app.entities.Cart;
import com.khoders.icpsc.app.entities.dto.PosReceipt;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.resource.utilities.SystemUtils;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author richa
 */
@Stateless
public class XtractService
{
  @Inject private AppSession appSession;
  
    public PosReceipt extractToPosReceipt(List<Cart> cartList, PosReceipt posReceipt)
    {
        System.out.println("Cart size => "+cartList.size());
        double receiptTotal = 0.0;
        try 
        {
            List<PosReceipt.InvoiceItem> invoiceItemsList = new LinkedList<>();
            if(appSession.getCurrentUser() != null)
            {
                if(appSession.getCurrentUser().getBranch() != null)
                {
                    posReceipt.setBranchName(appSession.getCurrentUser().getBranch().getBranchName());
                }
            }
            posReceipt.setBranchName("ICPSC - Lapaz Branch");
            posReceipt.setReceiptNumber(SystemUtils.generateRefNo());
            posReceipt.setDate(LocalDate.now());
            
            for (Cart posCart : cartList)
            {
                PosReceipt.InvoiceItem invoiceITem = new PosReceipt.InvoiceItem();
                if(posCart.getInventoryItem() != null)
                {
                    if(posCart.getInventoryItem().getProduct() != null)
                    {
                        invoiceITem.setProduct(posCart.getInventoryItem().getProduct().getProductName());
                    }
                }
                invoiceITem.setQuantity(posCart.getQuantity());
                invoiceITem.setUnitPrice(posCart.getUnitPrice());
                
                receiptTotal+=(posCart.getQuantity()*posCart.getUnitPrice());
                
             invoiceItemsList.add(invoiceITem);
            }
            System.out.println("Receipt total => "+receiptTotal);
            posReceipt.setTotalAmount(receiptTotal);
            posReceipt.setInvoiceItemsList(invoiceItemsList);
            
            return posReceipt;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
}
