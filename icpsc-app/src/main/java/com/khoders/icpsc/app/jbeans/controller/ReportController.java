/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.icpsc.app.jbeans.controller;

import com.khoders.icpsc.app.dto.CartDto;
import com.khoders.icpsc.app.jbeans.ReportFiles;
import com.khoders.icpsc.app.listener.AppSession;
import com.khoders.icpsc.app.services.XtractService;
import com.khoders.resource.utilities.DateRangeUtil;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author richa
 */
@Named(value = "reportController")
@SessionScoped
public class ReportController implements Serializable
{
    @Inject private XtractService xtractService;
    @Inject private ReportHandler reportHandler;
    @Inject private AppSession appSession;
    
    private DateRangeUtil dateRange = new DateRangeUtil();
    
    public void generateSalesReport()
    {
//        List<CartDto> cartDtoList = new LinkedList<>();
        try
        {
            List cartDtoList = xtractService.extractToCart(dateRange);
            
            

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cartDtoList);

            InputStream coverStream = getClass().getResourceAsStream(ReportFiles.SALES_REPORT);
            reportHandler.reportParams.put("logo", ReportFiles.LOGO);
            JasperPrint receiptPrint = JasperFillManager.fillReport(coverStream, reportHandler.reportParams, dataSource);
            HttpServletResponse servletResponse = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            servletResponse.setContentType("application/pdf");
            ServletOutputStream servletStream = servletResponse.getOutputStream();
            JasperExportManager.exportReportToPdfStream(receiptPrint, servletStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e)
        {
          e.printStackTrace();
        }
    }

    public DateRangeUtil getDateRange()
    {
        return dateRange;
    }

    public void setDateRange(DateRangeUtil dateRange)
    {
        this.dateRange = dateRange;
    }
    
    
}
