/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.config;

import com.khoders.icpsc.entities.UserAccount;
import com.khoders.icpsc.jbeans.UserModel;
import com.khoders.icpsc.services.UserAccountService;
import com.khoders.resource.enums.AccessLevel;
import com.khoders.resource.jpa.CrudApi;
import static com.khoders.resource.utilities.SecurityUtil.hashText;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author richa
 */
@Singleton
@Startup
public class AppInint
{

    @Inject private CrudApi crudApi;
    @Inject private UserAccountService userAccountService;

    private UserModel userModel = new UserModel();

    @PostConstruct
    public void init()
    {
        System.out.println("******************************************");
        System.out.println("******************************************");

        System.out.println("application started at - " + LocalDateTime.now());
        System.out.println("****  Going to create default user *******");
        String defaultUser = "info@icpsc.com";

        System.out.println("******************************************");
        System.out.println("******************************************");

        try
        {
            userModel.setUserEmail(defaultUser);
            userModel.setPassword(defaultUser);
            UserAccount userAccount = userAccountService.login(userModel);

            if (userAccount != null)
            {
                return;
            }

            userAccount = new UserAccount();
            userAccount.setEmail(defaultUser);
            userAccount.setAccessLevel(AccessLevel.SUPER_USER);
            userAccount.setPassword(hashText(defaultUser));
            userAccount.setFullname(defaultUser);

            crudApi.save(userAccount);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
