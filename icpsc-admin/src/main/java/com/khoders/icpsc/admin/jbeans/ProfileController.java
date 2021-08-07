/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.admin.jbeans;

import com.khoders.icpsc.admin.services.CompanyService;
import com.khoders.icpsc.entities.Profile;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.SystemUtils;
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
@Named(value = "profileController")
@SessionScoped
public class ProfileController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private CompanyService companyService;
    private List<Profile> profileList = new LinkedList<>();
    private Profile profile = new Profile();
    private String optionText;
    
    @PostConstruct
    private void init()
    {
        optionText="Save Changes";
        profileList = companyService.getProfileList();
    }
    
    public void saveProfile()
    {
        try
        {
            if(crudApi.save(profile) != null)
            {
                profileList = CollectionList.washList(profileList, profile);
            }
            clearProfile();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editProfile(Profile profile)
    {
        this.profile = profile;
        optionText = "Update";
    }
    
    public void deleteProfile(Profile profile)
    {
        try
        {
            if(crudApi.delete(profile))
            {
                profileList.remove(profile);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clearProfile()
    {
        profile = new Profile();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public Profile getProfile()
    {
        return profile;
    }

    public void setProfile(Profile profile)
    {
        this.profile = profile;
    }

    public List<Profile> getProfileList()
    {
        return profileList;
    }
    
    public String getOptionText()
    {
        return optionText;
    }

    public void setOptionText(String optionText)
    {
        this.optionText = optionText;
    }
    
}
