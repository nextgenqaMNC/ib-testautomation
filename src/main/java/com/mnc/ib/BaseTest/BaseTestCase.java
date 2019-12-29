package com.mnc.ib.BaseTest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.mnc.ib.utils.ExcelUtil;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.WebDriverTestCase;

public class BaseTestCase extends WebDriverTestCase{
	
	
	public ExcelUtil xlsUtil;
	public HashMap<String,String> testData;
	public HashMap<String, String> expectedTD;
	public StringBuilder htmlStringBuilder=new StringBuilder();
	String projBasedir =  System.getProperty("user.dir");
	public String TestCaseName = null;
	String country = null;
	String className = "";
//	ITestResult result;
	
	@BeforeClass
	public void beforeClass(){
		//HashMap<String, String> testData;
		xlsUtil = ExcelUtil.getInstance();
	}	
	
	@BeforeSuite
	public void beforSuite() throws IOException{
		//table("JBOSS Regression Suite");
	}
	
	@BeforeMethod
	public void beforeMethod(){
		
	}
	
	@AfterMethod()
	public void aftermethod(ITestResult result){	
		TestCaseName =	result.getMethod().getMethodName();		
		
		result = Reporter.getCurrentTestResult();
		//System.out.println("\n\n TC NAME : -----"+TestCaseName+"\n TC Status is: "+result.getStatus()+"\n ITestResult.SUCCESS: "+ITestResult.SUCCESS+"\n"+"\n ITestResult.FAILURE: "+ITestResult.FAILURE+"\n\n\n");
		if(result.getStatus() == ITestResult.SUCCESS){
			htmlStringBuilder.append("<tr><td width=250px>"+TestCaseName+"</td><td bgcolor='green'>PASS</td></tr>");
		} else //if(result.getStatus() == ITestResult.FAILURE) 
		{
			htmlStringBuilder.append("<tr><td width=250px>"+TestCaseName+"</td><td bgcolor='red'>FAIL</td></tr>");
		}
	}
	
	
	@AfterSuite
	public void sendEmail() throws IOException{

		getDriver().switchTo().defaultContent();
		System.out.println(htmlStringBuilder.toString());
	//	sendMail(htmlStringBuilder.toString());
	
	}
	public String convertToUSD(String format){
        DateFormat formatter= new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("UnitedStates"));
        return formatter.format(new Date());
    }
	
	public void sendMail(String body) throws IOException{
		String[] parms = {"wscript", projBasedir + "\\resources\\mailSend.vbs", body, convertToUSD("dd MMM yyyy HH:mm:ss z")};
		Runtime.getRuntime().exec(parms);
	}                                                 
	// Table Creation
		public void table(String proj) throws IOException{
			String url = ConfigurationManager.getBundle().getPropertyValue("env.baseurl");
			htmlStringBuilder.append("<html><head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}table{width: 700px}</style> <title>iWealthHealthCheck</title> </head>");
		    htmlStringBuilder.append("<body><table>");
		    htmlStringBuilder.append("<tr style='color: white; background: navy;'><th colspan='3'>JBOSS Regression Suite</th>");
		    htmlStringBuilder.append("<tr><td><b>Project Name</b></td><td>"+proj+"</td></tr>");
		    htmlStringBuilder.append("<tr><td><b>Test Date</b></td><td>"+ convertToUSD("dd MMM yyyy") +"</td></tr>");
		    htmlStringBuilder.append("<tr><td><b>Test Time</b></td><td>"+ convertToUSD("HH:mm:ss z") +"</td></tr>");
		    htmlStringBuilder.append("<tr><td><b>Test Environment </b></td><td>"+"JBOSS Regression - "+ " " + url.split("//")[1].split("/")[0] +"</td></tr>");
		    htmlStringBuilder.append("<tr><td><b>Detailed TestReport</b></td><td><a href=\"http://10.216.5.70:8082/dashboard.htm\">Report Link</a></td></tr></table>");
		    htmlStringBuilder.append("<table style='border: 0px solid black';><tr><td height='25'><b></b></td></table>");
		    htmlStringBuilder.append("</body></html>");
		    
		    htmlStringBuilder.append("<table>");
		    htmlStringBuilder.append("<tr style='color: white; background: navy;'><th width=250px>Test Case Name</th><th>Status</th></tr>");
		}
		
		public void addLog(String func, String stat, String desc){
		    htmlStringBuilder.append("<tr><td>"+func+"</td>");
		    if(stat.equalsIgnoreCase("pass"))
		    	htmlStringBuilder.append("<td bgcolor='green'>"+stat+"</td>");
		    else
		    	htmlStringBuilder.append("<td bgcolor='red'>"+stat+"</td>");	
			htmlStringBuilder.append("<td>"+desc+"</td></tr>");
		}
		// Take Screen short
		public void takeScreenshot(String fileName) throws IOException{
			File src = getDriver().getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File(projBasedir + "\\resources\\Screenshots\\" + fileName + ".png"));
		}
}
