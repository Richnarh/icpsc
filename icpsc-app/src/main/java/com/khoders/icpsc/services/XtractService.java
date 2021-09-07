/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.services;

import Zenoph.SMSLib.ZenophSMS;
import com.khoders.icpsc.entities.Cart;
import com.khoders.icpsc.dto.CartDto;
import com.khoders.icpsc.dto.PosReceipt;
import com.khoders.icpsc.jbeans.SmsAccess;
import com.khoders.icpsc.listener.AppSession;
import com.khoders.resource.fluent.Builder;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.resource.utilities.SystemUtils;
import java.time.LocalDate;
import java.util.Collections;
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
  @Inject private PosService posService;
  
    public PosReceipt extractToPosReceipt(List<Cart> cartList, PosReceipt posReceipt)
    {
        double receiptTotal = 0.0;
        try 
        {
            List<PosReceipt.InvoiceItem> invoiceItemsList = new LinkedList<>();
            if(appSession.getCurrentUser() != null)
            {
                if(appSession.getCurrentUser().getCompanyBranch() != null)
                {
                    posReceipt.setBranchName(appSession.getCurrentUser().getCompanyBranch().getBranchName());
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
    
    public List<CartDto> extractToCart(DateRangeUtil dateRange)
    {
        List<CartDto> cartDtoList = new LinkedList<>();
        List<Cart> cartList = new LinkedList<>();
        try
        {
           CartDto cartDto=null;
           cartList = posService.getTransactionByDates(dateRange);
            for (Cart cart : cartList)
            {
                cartDto = new CartDto();
                cartDto.setCartItemId(cart.getCartItemId());
                cartDto.setCustomerPhone(cart.getCustomerPhone());
                cartDto.setQuantity(cart.getQuantity());
                cartDto.setUnitPrice(cart.getUnitPrice());
                cartDto.setValueDate(cart.getValueDate());
                cartDto.setDescription(cart.getDescription());  
                
                if(cart.getInventoryItem() != null)
                {
                    if(cart.getInventoryItem().getProduct() != null)
                    {
                       cartDto.setProduct(cart.getInventoryItem().getProduct().getProductName()); 
                    }
                }
                if(appSession.getCurrentUser().getCompanyBranch() != null)
                {
                   cartDto.setCompanyName("ICPSC - "+appSession.getCurrentUser().getCompanyBranch().getBranchName());
                }
            }
                cartDtoList.add(cartDto);
            
            
           return cartDtoList;
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return Collections.emptyList();
    }
}
