/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.admin.jbeans;

import com.khoders.icpsc.admin.Pages;
import com.khoders.icpsc.admin.listener.AppSession;
import com.khoders.icpsc.admin.services.UserAccountService;
import com.khoders.icpsc.entities.UserAccount;
import com.khoders.resource.enums.AccessLevel;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Faces;

/**
 *
 * @author richa
 */
@RequestScoped
@Named(value = "loginController")
public class LoginController implements Serializable
{
    @Inject private AppSession appSession;
    @Inject private UserAccountService userAccountService;
    
    private String UserEmail;
    private String password;
    
    private UserModel userModel = new UserModel();
    
    private List<UserAccount> userAccountList = new LinkedList<>();
    
    private void init()
    {
        userAccountList = userAccountService.getAccountList();
    }
    
    public String doLogin()
    {
        try
        {

            userModel.setUserEmail(UserEmail);
            userModel.setPassword(password);

            UserAccount account = userAccountService.login(userModel);

            if (account == null)
            {
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Wrong username or Password"), null));
                return null;
            }
           if(account.getAccessLevel() != AccessLevel.SUPER_USER)
           {
               FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("You are not permitted to access this system!"), null));
                return null;
           }
               

            initLogin(account);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    public String initLogin(UserAccount userAccount)
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
            
            Faces.redirect(Pages.index);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    public String doLogout()
    {
        try
        {
            
            Faces.invalidateSession();
            Faces.logout();
            
            Faces.redirect(Pages.login);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public void login(UserAccount userAccount)
    {
        
            Faces.redirect("http://localhost:8080/icpsc-app/access.xhtml?id="+userAccount.getId());
    }
  
    public String getUserEmail()
    {
        return UserEmail;
    }

    public void setUserEmail(String UserEmail)
    {
        this.UserEmail = UserEmail;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public UserModel getUserModel()
    {
        return userModel;
    }

    public void setUserModel(UserModel userModel)
    {
        this.userModel = userModel;
    }
    
    
}
