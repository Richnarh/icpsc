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
import com.khoders.icpsc.app.entities.SalesCatalogue;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.icpsc.app.services.InventoryService;
import com.khoders.icpsc.app.services.PosService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    private InventoryItem inventoryItem = new InventoryItem();
    private SalesCatalogue salesCatalogue = new SalesCatalogue();
    
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
        
        cart = new Cart();
        for (InventoryItem item : inventorySalesList)
        {
            cart.setQuantity(1);
            cart.setInventoryItem(item);
            cart.setUnitPrice(item.getUnitPrice());
            cartList.add(cart);
            
        }
    }
    public void processCart()
    {
        double totalCost = 0.0;
        try 
        {
            for (Cart cartItem : cartList) 
            {
                if(cartItem.getQuantity() <= 0)
                {
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please enter a valid quantity!"), null));

                    return;
                }
                
                if (cartItem.getQuantity() > cartItem.getInventoryItem().getQuantity()) 
                {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    Msg.setMsg("Limited stock for this product: "
                                            +cartItem.getInventoryItem().getProduct().getProductName()) 
                                            +"\n Quantity remaining is: "+cartItem.getInventoryItem().getQuantity(), null));
                    return;
                }
                
                totalCost = (cartItem.getQuantity() * cartItem.getUnitPrice());
                cartItem.setTotal(totalCost);
                
                totalAmount += cartItem.getTotal();
           }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }    
    public void saveCart()
    {
    
        if (!cartList.isEmpty())
        {
            
            salesCatalogue.genCode();
            salesCatalogue.setPurchaseDate(LocalDateTime.now());
            salesCatalogue.setTotalAmount(totalAmount);
            salesCatalogue.setUserAccount(appSession.getCurrentUser());
            
             if(crudApi.save(salesCatalogue) != null)
             {
                 cartList.forEach(salesCart ->
                 {  
                     int qtyPurchased = salesCart.getQuantity();
                     int qtyAtInventory = salesCart.getInventoryItem().getQuantity();

                     int qtyAtHand = qtyAtInventory - qtyPurchased;

                     try
                     {
                         inventoryItem = crudApi.getEm().find(InventoryItem.class, salesCart.getInventoryItem().getId());
                         inventoryItem.setQuantity(qtyAtHand);
                         crudApi.save(inventoryItem);

                         salesCart.genCode();
                         salesCart.setSalesCatalogue(salesCatalogue);
                         salesCart.setUserAccount(appSession.getCurrentUser());
                         
                         crudApi.save(salesCart);

                     } catch (Exception e)
                     {
                         e.printStackTrace();
                     }

                 });

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Cart saved successfully!"), null));
             }
           
        }
    }
  
    
    public void clear()
    {
        cart = new Cart();
        cart.setUserAccount(appSession.getCurrentUser());
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
