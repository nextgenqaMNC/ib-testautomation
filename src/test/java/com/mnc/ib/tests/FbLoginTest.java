package com.mnc.ib.tests;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.util.Reporter;
import com.mnc.ib.BaseTest.BaseTestCase;
import com.mnc.ib.pages.FbHomePage;
import com.mnc.ib.pages.FbLoginPage;
import com.mnc.ib.utils.ExcelUtil;
import com.mnc.ib.utils.PageUtils;

public class FbLoginTest extends BaseTestCase{
	public static Logger logger = Logger.getLogger("devpinoyLogger");
	private FbLoginPage fblp = new FbLoginPage();
	private FbHomePage fbhp = new FbHomePage();
	ExcelUtil xlsUtils = new ExcelUtil();
	
	@DataProvider(name = "positiveFlow")
	public static Object[][] getPositiveTcs(){
		return new Object[][]{
				{"TC_001", "Smoke Test 1"}
		};
	}
	
	@Test(enabled = true, dataProvider = "positiveFlow")
	public void fbLogin(String sTCNo, String testdescription) throws InterruptedException{
		
		HashMap<String, String> LoginCredentials = new HashMap<String, String>();
		HashMap<String, String> ExcelAPIData = xlsUtils.getLisaDataMapForTC(sTCNo);
		LoginCredentials.put("userid", ExcelAPIData.get("REQ_UID"));
		LoginCredentials.put("password", ExcelAPIData.get("REQ_PIN"));
		Reporter.log("started launching application");
		logger.debug("starting launching application");
		fblp.openPage(null, LoginCredentials);
		PageUtils.waitForAjaxToComplete(getDriver());
		Reporter.logWithScreenShot("Logging into the application successful");
		logger.debug("Logging into the application successful");	
		
		PageUtils.verificationData("Fb user Title Name", fbhp.FbUserTitle.getText(), "Paramesh");
		PageUtils.verificationData("Fb user Home Link", fbhp.HomeLink.getText(), "Home");
		PageUtils.verificationData("Fb user Create Link", fbhp.CreateLink.getText(), "Create");		
		
	}

}
