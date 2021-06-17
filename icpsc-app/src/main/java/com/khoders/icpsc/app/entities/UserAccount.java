package com.khoders.icpsc.app.entities;

import com.khoders.resource.enums.Currency;
import com.khoders.resource.jpa.BaseModel;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author 
 */
@Entity
@Table(name = "user_account")
public class UserAccount extends BaseModel implements Serializable{

  @Column(name = "shop_name")
  private String shopName;
  
  @Column(name = "email_address")
  private String emailAddress;
  
  @Column(name = "phone_number")
  private String phoneNumber;
  
  @Column(name = "password")
  private String password;
  
  @Column(name = "website")
  private String website;
  
  @Column(name = "address")
  private String address;
  
  @Column(name = "currency")
  @Enumerated(EnumType.STRING)
  private Currency currency=Currency.GHS;
  
  @Transient
  private String password2;

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
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

    public Currency getCurrency()
    {
        return currency;
    }

    public void setCurrency(Currency currency)
    {
        this.currency = currency;
    }

    public String getPassword2()
    {
        return password2;
    }

    public void setPassword2(String password2)
    {
        this.password2 = password2;
    }

    @Override
    public String toString()
    {
        return shopName;
    }
  
  
}
