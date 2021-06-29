/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans.converters;

import com.khoders.icpsc.app.entities.Product;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import org.omnifaces.converter.SelectItemsConverter;

/**
 *
 * @author khoders
 */
@FacesConverter(forClass = Product.class)
public class ProductConverter extends SelectItemsConverter{
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if(value == null)
        {
            return null;
        }
        return ((Product)value).getId();
    }
}
