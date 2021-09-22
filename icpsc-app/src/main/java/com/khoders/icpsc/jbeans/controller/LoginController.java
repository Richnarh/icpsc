/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.jbeans.controller;

import Zenoph.SMSLib.Enums.REQSTATUS;
import Zenoph.SMSLib.ZenophSMS;
import com.khoders.icpsc.Pages;
import com.khoders.icpsc.jbeans.UserModel;
import com.khoders.icpsc.entities.UserAccount;
import com.khoders.icpsc.listener.AppSession;
import com.khoders.icpsc.services.InventoryService;
import com.khoders.icpsc.services.SmsService;
import com.khoders.icpsc.services.UserAccountService;
import com.khoders.icpsc.entities.InventoryItem;
import com.khoders.icpsc.entities.enums.SMSType;
import com.khoders.icpsc.entities.sms.Sms;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @author khoders
 */
@Named(value="loginController")
@RequestScoped
public class LoginController implements Serializable
{
    @Inject private AppSession appSession;
    @Inject private UserAccountService userAccountService;
    @Inject private InventoryService inventoryService;
    @Inject private SmsService smsService;
    @Inject private CrudApi crudApi;
    
    private List<InventoryItem> expiredInventoryList = new LinkedList<>();
    
    private String userEmail;
    private String password,expiredProductLink;
    
    private UserModel userModel = new UserModel();
    
    private final DateRangeUtil dateRange = new DateRangeUtil();
    
    private void fetchExpiredProduct()
    {
       String expiredYearMonth = dateRange.getYear() + dateRange.getMonth();
       expiredInventoryList = inventoryService.getExpiredProductList(expiredYearMonth);
       
       if(expiredInventoryList.size() > 0)
       {
           processExpiredInventoryViaSMS();
       }
    }
    
    public String doLogin()
    {
        try
        {
            userModel.setUserEmail(userEmail);
            userModel.setPassword(password);

            UserAccount account = userAccountService.login(userModel);

            if (account == null)
            {
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Wrong username or Password"), null));
                return null;
            }

            initLogin(account);
            
            fetchExpiredProduct();

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
            appSession.initBranch(userAccount.getCompanyBranch());
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
 
    public void processExpiredInventoryViaSMS()
    {
       expiredProductLink = "http://localhost:8080/icpsc-app/expired-product.xhtml?id=";    
//       expiredProductLink = "http://209.145.49.185:8080/icpsc/expired-product.xhtml?id=";    
        try 
        {

        try
        {
            ZenophSMS zsms = smsService.extractParams();

            String phoneNumber;
            if(appSession.getCompanyBranch().getTelephoneNo() != null)
            {
                System.out.println("Phone number => "+appSession.getCompanyBranch().getTelephoneNo());
                zsms.setMessage("Please click the link below to view expired inventory details: \n\n"+expiredProductLink+appSession.getCompanyBranch().getTelephoneNo());
                phoneNumber = appSession.getCompanyBranch().getTelephoneNo();
                
                System.out.println("Please click the link below to view expired inventory details: \n\n"+expiredProductLink+appSession.getCompanyBranch().getTelephoneNo());
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please update your profile with phone number to receive sms messages"), null));
                return;
            }
            
            List<String> numbers = zsms.extractPhoneNumbers(phoneNumber);

            for (String number : numbers)
            {
                zsms.addRecipient(number);
            }

            zsms.setSenderId("ICPSC_CHEM");

            List<String[]> response = zsms.submit();
            for (String[] destination : response)
            {
                REQSTATUS reqstatus = REQSTATUS.fromInt(Integer.parseInt(destination[0]));
                if (reqstatus == null)
                {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("failed to send message"), null));
                    break;
                } else
                {
                    switch (reqstatus)
                    {
                        case SUCCESS:
                            FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Message sent"), null));
                                saveMessage();
                            break;
                        case ERR_INSUFF_CREDIT:
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Insufficeint Credit"), null));
                        default:
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Failed to send message"), null));
                            return;
                    }
                }
            }

            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void saveMessage()
    {
        
        Sms sms = new Sms();
        
        try
        {
            sms.setSmsTime(LocalDateTime.now());
            sms.setMessage("Please click the link below to view expired Inventory details: "+expiredProductLink+appSession.getCompanyBranch().getTelephoneNo());
            sms.setMobileNo(appSession.getCompanyBranch().getTelephoneNo());
            sms.setsMSType(SMSType.SINGLE_SMS);
            sms.setUserAccount(appSession.getCurrentUser());
           if(crudApi.save(sms) != null)
           {
               FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("SMS sent to "+appSession.getCompanyBranch().getTelephoneNo()), null));
               
               System.out.println("SMS sent and saved -- ");
           }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AppSession getAppSession()
    {
        return appSession;
    }

    public void setAppSession(AppSession appSession)
    {
        this.appSession = appSession;
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