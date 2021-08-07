/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.admin.jbeans;

import com.khoders.icpsc.admin.services.CompanyService;
import com.khoders.icpsc.entities.Branch;
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
@Named(value = "branchController")
@SessionScoped
public class BranchController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private CompanyService companyService;
    
    private List<Branch> branchList = new LinkedList<>();
    private Branch branch = new Branch();
    
    private String optionText;
    
    @PostConstruct
    private void init()
    {
        optionText = "Save Changes";
        branchList = companyService.getBranchList();
    }
    
    public void saveBranch()
    {
        try
        {
            if(crudApi.save(branch) != null)
            {
                branchList = CollectionList.washList(branchList, branch);
            }
            clearBranch();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editBranch(Branch branch)
    {
        this.branch = branch;
        optionText = "Update";
    }
    
    public void deleteBranch(Branch branch)
    {
        try
        {
            if(crudApi.delete(branch))
            {
                branchList.remove(branch);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clearBranch()
    {
        branch = new Branch();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }
    
    

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch companyBranch)
    {
        this.branch = companyBranch;
    }

    public List<Branch> getBranchList()
    {
        return branchList;
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
