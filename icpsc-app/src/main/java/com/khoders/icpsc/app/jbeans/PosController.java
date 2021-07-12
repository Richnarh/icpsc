/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans;

import com.khoders.icpsc.app.entities.Cart;
import com.khoders.icpsc.app.entities.Inventory;
import com.khoders.icpsc.app.entities.InventoryItem;
import com.khoders.icpsc.app.entities.Product;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.icpsc.app.services.InventoryService;
import com.khoders.icpsc.app.services.PosService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "posController")
@SessionScoped
public class PosController implements Serializable
{
    @Inject CrudApi crudApi;
    @Inject AppSession appSession;
    @Inject InventoryService inventoryService;
    @Inject PosService posService;
    
    private Inventory selectedInventory = null;
    private List<Cart> cartList = new LinkedList<>();
    private Cart cart = new Cart();
    private List<InventoryItem> inventoryItemList = new LinkedList<>();
    
    private double totalAmount=0.0;
    
    @PostConstruct
    private void init()
    {
        clear();
        inventoryItemList = inventoryService.getInventoryItemList();
    }
    
    public void selectProduct(Product product)
    {
        List<InventoryItem> inventorySalesList = new LinkedList<>();
        
        inventorySalesList = posService.getInventoryProduct(product);
        
        
        for (InventoryItem item : inventorySalesList)
        {
            cart = new Cart();
            cart.setProduct(item.getProduct());
            cart.setUnitPrice(item.getUnitPrice());
            cart.setQuantity(item.getQuantity());
            cart.setTotal(item.getUnitPrice() * item.getQuantity());
            cartList.add(cart);
            
        }
        
//        totalAmount+=cart.getTotal();
    }
    
    public void saveCart()
    {
        if (!cartList.isEmpty())
        {
            cartList.forEach(cart ->
            {
                cart.genCode();
                crudApi.save(cart);
            });
            
            FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Cart saved successfully!"), null));
        }
    }
        
    public void clear()
    {
        cartList = new LinkedList<>();
        totalAmount = 0.0;
    }
        
    
    public void removeFromCart(Cart cart)
    {
        System.out.println("Before total amount => "+totalAmount);
        
        totalAmount -= (cart.getQuantity() * cart.getUnitPrice());
        cartList.remove(cart);
        
        
        System.out.println("\n\n");
        
        System.out.println("After total amount => "+totalAmount);
    }
    
    public void processCart()
    {
        totalAmount=0.0;
        if (!cartList.isEmpty())
        {
            cartList.forEach(cart ->
            {
                totalAmount += cart.getTotal();
            });
        }
    }

    public List<Cart> getCartList()
    {
        return cartList;
    }

    public List<InventoryItem> getInventoryItemList()
    {
        return inventoryItemList;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

}
