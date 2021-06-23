/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.admin.commons;

import com.khoders.icpsc.admin.services.CompanyService;
import com.khoders.icpsc.entities.Branch;
import com.khoders.icpsc.entities.Profile;
import com.khoders.resource.jpa.CrudApi;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "userCommonClazz")
@SessionScoped
public class UserCommonClazz implements Serializable
{
   @Inject private CrudApi crudApi;
   @Inject private CompanyService companyService;
   
   private List<Branch> branchList = new LinkedList<>();
   private List<Profile> profileList = new LinkedList<>();
   
   @PostConstruct
   public void init()
   {
       branchList = companyService.getBranchList();
       profileList = companyService.getProfileList();
   }

    public List<Branch> getBranchList()
    {
        return branchList;
    }

    public List<Profile> getProfileList()
    {
        return profileList;
    }
   
}
