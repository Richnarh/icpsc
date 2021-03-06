/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.jbeans.controller;

import com.khoders.icpsc.Pages;
import com.khoders.icpsc.listener.AppSession;
import com.khoders.icpsc.entities.UserAccount;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Faces;

/**
 *
 * @author richa
 */
@Named(value="backdoorController")
@ViewScoped
public class backdoorController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    private UserAccount userAccount;
    
    
    private String userId;
    
    public void user(String id)
    {
        userId = id;
        System.out.println("UserID => "+userId);
        System.out.println("ID => "+id);
    }
    
    public String backdoorAccess()
    {
        try
        {
            UserAccount thisAccount = crudApi.find(UserAccount.class, userId);
            System.out.println("ID -- " + userId);
            if (thisAccount == null)
            {
                return null;
            }
           initAccountLogin(thisAccount);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    
    public String initAccountLogin(UserAccount userAccount)
    {
        try
        {
            if (userAccount == null)
            {
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Wrong username or Password"), null));
                return null;
            }
            appSession.login(userAccount);
            appSession.initBranch(userAccount.getCompanyBranch());
            Faces.redirect(Pages.index);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

        
}
