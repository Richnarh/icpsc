/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.jbeans.controller.sms;

import Zenoph.SMSLib.Enums.MSGTYPE;
import Zenoph.SMSLib.Enums.REQSTATUS;
import static Zenoph.SMSLib.Enums.REQSTATUS.ERR_INSUFF_CREDIT;
import Zenoph.SMSLib.ZenophSMS;
import com.khoders.icpsc.entities.Customer;
import com.khoders.icpsc.entities.enums.MessagingType;
import com.khoders.icpsc.entities.enums.SMSType;
import com.khoders.icpsc.entities.sms.GroupContact;
import com.khoders.icpsc.entities.sms.MessageTemplate;
import com.khoders.icpsc.entities.sms.SMSGrup;
import com.khoders.icpsc.entities.sms.SenderId;
import com.khoders.icpsc.entities.sms.Sms;
import com.khoders.icpsc.jbeans.SmsAccess;
import com.khoders.icpsc.listener.AppSession;
import com.khoders.icpsc.services.InventoryService;
import com.khoders.icpsc.services.SmsService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
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
 * @author pascal
 */
@Named(value = "smsController")
@SessionScoped
public class SmsController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private SmsService smsService;
    @Inject private InventoryService inventoryService;

    private Sms sms = new Sms();
    private Customer selectedCustomer;
    private SMSGrup smsGrup = new SMSGrup();

    private List<Customer> customerList = new LinkedList<>();
    private List<Sms> smsList = new LinkedList<>();
    
     private List<GroupContact> groupContactList = new LinkedList<>();

    private SenderId senderId = new SenderId();
    private MessageTemplate selectedMessageTemplate;
    
    private MessagingType selectedMessagingType = MessagingType.TEXT_MESSAGING;
    private String connectionStatus;
    private String textMessage;
    
    private boolean flag = false;

    @PostConstruct
    private void init()
    {
        customerList = smsService.getContactList();
        loadSmslog();
        getConnection();
        
    }
    
    public void loadSmslog()
    {
        smsList =smsService.loadSmslogList();
    }
    
    public void loadCustomers()
    {
        customerList = inventoryService.getCustomerList();
    }
    
    private void getConnection()
    {
        if(smsService.isInternetAccessVailable() == true)
        {
            connectionStatus = "Internet Access";
        }
        else
        {
            connectionStatus = "No Internet Access";
        }
    }
    
    public void selectMessagingType()
    {
        flag = selectedMessagingType == MessagingType.TEMPLATE_MESSAGING;
    }
    
    public void activateSenderId()
    {
        sms.setSenderId(senderId);
    }

    public void processMessage()
    {
        if (selectedCustomer == null)
        {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please select contact"), null));
            return;
        }
        if (sms.getSenderId() == null)
        {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please set sender ID"), null));
            return;
        }

        try
        {
            if (smsService.isInternetAccessVailable() == true)
            {
                clearSMS();
                
                ZenophSMS zsms = smsService.extractParams();

                // set message parameters.
                if (selectedMessagingType == MessagingType.TEMPLATE_MESSAGING)
                {
                    zsms.setMessage(selectedMessageTemplate.getTemplateText());

                    System.out.println("TEMPLATE_MESSAGING -- " + selectedMessageTemplate.getTemplateText());
                } else
                {
                    if(textMessage.isEmpty())
                    {
                         FacesContext.getCurrentInstance().addMessage(null,
                                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.setMsg("Please type a message"), null));
                        
                        return;
                    }
                    zsms.setMessage(textMessage);
                }
                
                String phoneNumber = selectedCustomer.getPhone();
                List<String> numbers = zsms.extractPhoneNumbers(phoneNumber);

                for (String number : numbers)
                {
                    zsms.addRecipient(number);
                }
                
                zsms.setSenderId(sms.getSenderId().getSenderId());
                

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

            } else
            {
                System.out.println("--------- INTERNET CONNECTION NOT AVAILABLE ----");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveMessage()
    {
        try
        {
            sms.setSmsTime(LocalDateTime.now());
            sms.setMessage(textMessage);
            sms.setCustomer(selectedCustomer);
            sms.setsMSType(SMSType.SINGLE_SMS);
            sms.setUserAccount(appSession.getCurrentUser());
            sms.setCompanyBranch(appSession.getCompanyBranch());
           if(crudApi.save(sms) != null)
           {
               FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("SMS sent to "+selectedCustomer.getCustomerName()), null));
               
               System.out.println("SMS sent and saved -- ");
           }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
       

    public void loadContactGroup()
    {
        groupContactList = smsService.getContactGroupList(smsGrup);
    }
    
    public void deleteSms(Sms sms)
    {
        try
        {
            if(crudApi.delete(sms))
            {
                smsList.remove(sms);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.SUCCESS_MESSAGE, null)); 
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.FAILED_MESSAGE, null)); 
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void clearSMS()
    {
        sms = new Sms();
        sms.setSenderId(senderId);
        sms.setUserAccount(appSession.getCurrentUser());
        SystemUtils.resetJsfUI();
    }

    public void manage(Customer customer)
    {
        this.selectedCustomer = customer;
    }
   
    public Sms getSms()
    {
        return sms;
    }

    public void setSms(Sms sms)
    {
        this.sms = sms;
    }

    public List<Sms> getSmsList()
    {
        return smsList;
    }

    public String getConnectionStatus()
    {
        return connectionStatus;
    }
    
    public SenderId getSenderId()
    {
        return senderId;
    }

    public void setSenderId(SenderId senderId)
    {
        this.senderId = senderId;
    }

    public Customer getSelectedCustomer()
    {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer)
    {
        this.selectedCustomer = selectedCustomer;
    }

    public List<Customer> getCustomerList()
    {
        return customerList;
    }

    public MessageTemplate getSelectedMessageTemplate()
    {
        return selectedMessageTemplate;
    }

    public void setSelectedMessageTemplate(MessageTemplate selectedMessageTemplate)
    {
        this.selectedMessageTemplate = selectedMessageTemplate;
    }

    public MessagingType getSelectedMessagingType()
    {
        return selectedMessagingType;
    }

    public void setSelectedMessagingType(MessagingType selectedMessagingType)
    {
        this.selectedMessagingType = selectedMessagingType;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public String getTextMessage()
    {
        return textMessage;
    }

    public void setTextMessage(String textMessage)
    {
        this.textMessage = textMessage;
    }

    public SMSGrup getSmsGrup()
    {
        return smsGrup;
    }

    public void setSmsGrup(SMSGrup smsGrup)
    {
        this.smsGrup = smsGrup;
    }

    public List<GroupContact> getGroupContactList()
    {
        return groupContactList;
    }

 
}
