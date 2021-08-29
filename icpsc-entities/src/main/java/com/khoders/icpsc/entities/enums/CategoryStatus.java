/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.entities.enums;

import com.khoders.resource.utilities.MsgResolver;

/**
 *
 * @author richa
 */
public enum CategoryStatus implements MsgResolver
{
    ACTIVE("ACTIVE", "Active"),
    IN_ACTIVE("IN_ACTIVE", "In active");
    
    private final String label;
    private final String code;
    
    private CategoryStatus(String label, String code)
    {
        this.label = label;
        this.code = code;
    }
    
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }
    
    @Override
    public String toString() {
        return label;
    }
}
