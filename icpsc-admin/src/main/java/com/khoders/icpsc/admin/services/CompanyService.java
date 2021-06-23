/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.admin.services;

import com.khoders.icpsc.entities.Branch;
import com.khoders.icpsc.entities.Profile;
import com.khoders.resource.jpa.CrudApi;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author richa
 */
@Stateless
public class CompanyService
{
    @Inject private CrudApi crudApi;
    
   public List<Profile> getProfileList()
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM Profile e", Profile.class).getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<Branch> getBranchList()
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM Branch e ORDER BY e.branchName ASC", Branch.class).getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
