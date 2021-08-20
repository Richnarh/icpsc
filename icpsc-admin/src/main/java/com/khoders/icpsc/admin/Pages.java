package com.khoders.icpsc.admin;

import org.omnifaces.util.Faces;

/**
 * Configures JAX-RS for the application.
 * @author Juneau
 */
public class Pages{
    public static String index = Faces.getRequestBaseURL() + "app/index.xhtml?faces-redirect=true";
    public static String index2 = Faces.getRequestBaseURL() + "secured/index.xhtml?faces-redirect=true";
    public static String login = Faces.getRequestBaseURL();
}
