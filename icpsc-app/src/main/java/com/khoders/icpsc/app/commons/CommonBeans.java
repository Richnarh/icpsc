/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.commons;

import com.khoders.icpsc.entities.enums.MessagingType;
import com.khoders.resource.enums.UnitsOfQuantity;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "commonBeans")
@SessionScoped
public class CommonBeans implements Serializable
{
    public List<UnitsOfQuantity> getUnitsOfQuantityList()
    {
        return Arrays.asList(UnitsOfQuantity.values());
    }
    public List<MessagingType> getMessagingTypeList()
    {
        return Arrays.asList(MessagingType.values());
    }
}
