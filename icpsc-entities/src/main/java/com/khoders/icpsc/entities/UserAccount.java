package com.khoders.icpsc.entities;

import com.khoders.resource.enums.AccessLevel;
import com.khoders.resource.enums.Currency;
import com.khoders.resource.enums.Status;
import com.khoders.resource.jpa.BaseModel;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author 
 */
@Entity
@Table(name = "user_account")
public class UserAccount extends BaseModel implements Serializable{
  @JoinColumn(name = "company_branch", referencedColumnName = "id")
  @ManyToOne
  private CompanyBranch companyBranch;
  
  @Column(name = "shop_name")
  private String shopName;
  
  @Column(name = "fullname")
  private String fullname;
  
  @Column(name = "email_address")
  private String email;
  
  @Column(name = "phone_number")
  private String phoneNumber;
  
  @Column(name = "password")
  private String password;
  
  @Column(name = "website")
  private String website;
  
  @Column(name = "address")
  private String address;
  
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status = Status.ACTIVE;
  
  @Column(name = "perm_delete")
  private boolean permDelete = false;

  @Column(name = "perm_update")
  private boolean permUpdate = false;

  @Column(name = "perm_save")
  private boolean permSave = true;

  @Column(name = "perm_print")
  private boolean permPrint = false;

  @Column(name = "perm_convert")
  private boolean permConvert = false;

  @Column(name = "perm_send_sms")
  private boolean permSendSMS = false;
  
  @Column(name = "access_level")
  @Enumerated(EnumType.STRING)
  private AccessLevel accessLevel;  
      
  @Transient
  private String password2;

    public String getFullname()
    {
        return fullname;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPassword2()
    {
        return password2;
    }

    public void setPassword2(String password2)
    {
        this.password2 = password2;
    }

    public boolean isPermDelete()
    {
        return permDelete;
    }

    public void setPermDelete(boolean permDelete)
    {
        this.permDelete = permDelete;
    }

    public boolean isPermUpdate()
    {
        return permUpdate;
    }

    public void setPermUpdate(boolean permUpdate)
    {
        this.permUpdate = permUpdate;
    }

    public boolean isPermSave()
    {
        return permSave;
    }

    public void setPermSave(boolean permSave)
    {
        this.permSave = permSave;
    }

    public boolean isPermPrint()
    {
        return permPrint;
    }

    public void setPermPrint(boolean permPrint)
    {
        this.permPrint = permPrint;
    }

    public boolean isPermConvert()
    {
        return permConvert;
    }

    public void setPermConvert(boolean permConvert)
    {
        this.permConvert = permConvert;
    }

    public boolean isPermSendSMS()
    {
        return permSendSMS;
    }

    public void setPermSendSMS(boolean permSendSMS)
    {
        this.permSendSMS = permSendSMS;
    }

    public CompanyBranch getCompanyBranch()
    {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch)
    {
        this.companyBranch = companyBranch;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public AccessLevel getAccessLevel()
    {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel)
    {
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString()
    {
        return shopName;
    }
  
  
}
