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
    private List<Inventory> inventoryList = new LinkedList<>();
    
    @PostConstruct
    private void init()
    {
        inventoryList = inventoryService.getInventoryList();
    }
    
    public void selectProduct(Product product)
    {
        List<Inventory> inventorySalesList = new LinkedList<>();
        
        inventorySalesList = posService.getInventoryProduct(product);
        
        for (Inventory item : inventorySalesList)
        {
            Cart cart = new Cart();
            cart.setProduct(item.getProduct());
            cart.setUnitPrice(item.getUnitPrice());
            cart.setQuantity(item.getQuantity());
            cart.setTotal(item.getUnitPrice() * item.getQuantity());
            cartList.add(cart);
        }
        
    }
    
    public void saveCart()
    {
        cartList.forEach(cart->{
            cart.genCode();
           crudApi.save(cart);
        });
    }

    public List<Cart> getCartList()
    {
        return cartList;
    }

    public List<Inventory> getInventoryList()
    {
        return inventoryList;
    }

}
