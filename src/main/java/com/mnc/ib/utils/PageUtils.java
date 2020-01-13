package com.mnc.ib.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;

import com.mnc.ib.BaseTest.BaseTestCase;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;

public class PageUtils extends BaseTestCase {	

	public static void verificationData(String strFielName, String strExp, String strAct) {
		String strMsg = "Verification of the field - " + strFielName + ", Expected Value - " + strExp
				+ ", Actual Value - " + strAct;
		verifyTrue(strExp.equalsIgnoreCase(strAct), "Verification not successful - " + strMsg,
				"Verification was successful - " + strMsg);
	}	

	public static void verifyElement(QAFExtendedWebDriver driver, QAFWebElement ele, String eleName) {
		if(ele != null){
			if (ele.isPresent()) {
				try {
					if (ele.isDisplayed()) {
						scrollIntoView(driver, ele);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ele.verifyPresent(eleName);
		} else{			
			verifyTrue(false,"Element was not present - " + eleName,"");
		}
	}


	public static int getElementCount(QAFExtendedWebDriver driver, String sXPath) {
		List<QAFWebElement> elements = driver.findElements("xpath=" + sXPath);
		return elements.size();
	}

	
	public static QAFWebElement getWebElement(QAFExtendedWebDriver driver, String sIdentifier, String sVal) {
		QAFWebElement ele = null;
		// sVal = sVal.replace("'", "\'");
		try {
			switch (sIdentifier) {
			case "XPATH":
				ele = driver.findElementByXPath(sVal);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			com.qmetry.qaf.automation.util.Reporter.logWithScreenShot("Element was not found. identifier: "
					+ sIdentifier + ", Value: " + sVal + ". Possible reason - element type got changed.");
		}
		return ele;
	}
	
	public static String getCurrentDate(String sDateFormat) {
		DateFormat dateFormat = new SimpleDateFormat(sDateFormat);
		TimeZone sng=TimeZone.getTimeZone("Singapore");
		dateFormat.setTimeZone(sng);
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		return dateFormat.format(date);
	}

	public static boolean isElementDispalyed(QAFExtendedWebDriver driver, QAFWebElement elePageHeader) {
		switchToFrame(driver, "user_area");
		switchToFrame(driver, "iframe1");
		boolean result = false;
		if (elePageHeader.isPresent()) {
			result = elePageHeader.isDisplayed();
		}
		switchToDefaultFrame(driver);
		return result;
	}

	public static void switchToFrame(QAFExtendedWebDriver driver, String sFrame) {
		try {
			driver.switchTo().frame(sFrame);
		} catch (Exception e) {
			Reporter.log("Application is not loaded. Frame - " + sFrame);
			e.printStackTrace();
		}
	}
	
	public static void switchToDefaultFrame(QAFExtendedWebDriver driver) {
		try {
			if(!isAlertPresent(driver)){
				driver.switchTo().defaultContent();
			}else{
				com.qmetry.qaf.automation.util.Reporter.log("Alert was present!");
			}
			
		} catch (Exception e) {
			Reporter.log("Application is not loaded.");
			e.printStackTrace();
		}
	}


	public static void scrollIntoView(QAFExtendedWebDriver driver, QAFWebElement element) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].scrollIntoView(false);", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void waitForAjaxToComplete(QAFExtendedWebDriver driver) {
		if (!isAlertPresent(driver)) {
			for (int i = 0; i <= 200; i++) {

				try {
					Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor) driver)
							.executeScript("return jQuery.active == 0");
					if (ajaxIsComplete) {
						break;
					}
					Thread.sleep(100);
				} catch (Exception e) {
					//e.printStackTrace();
					//System.out.println("waiting for page load to overcome synchronization issues");
				}
			}
		}
	}

	public static String getElementText(QAFExtendedWebDriver driver, QAFWebElement ele) {
		switchToFrame(driver, "user_area");
		switchToFrame(driver, "iframe1");
		String sRtnVal = "";
		if (ele.isPresent()) {
			PageUtils.scrollIntoView(driver, ele);
			sRtnVal = ele.getText().trim().replaceAll("\n", " ");
		}
		switchToDefaultFrame(driver);
		return sRtnVal;
	}



	public static void pageVerification(QAFExtendedWebDriver driver, QAFWebElement elePageHeader, String sPageName) {
		String sMsg = "Page Verification - " + sPageName;
		String sPass = ". Expected: " + sPageName + " - should be displayed. Actual: " + sPageName
				+ " - Page was displayed";
		String sFail = ". Expected: " + sPageName + " - should be displayed. Actual: " + sPageName
				+ " - Page was not displayed";
		verifyTrue(isElementDispalyed(driver, elePageHeader), sMsg + sFail, sMsg + sPass);
	}
		
	public static boolean isAlertPresent(QAFExtendedWebDriver driver) {
		boolean bResult = false;
		try {
			driver.switchTo().alert();
			bResult = true;
		} catch (NoAlertPresentException Ex) {
			bResult = false;
		}
		return bResult;
	}

	public static String getAlertText(QAFExtendedWebDriver driver) {
		String strAlertText = "";
		if (isAlertPresent(driver)) {
			strAlertText = driver.switchTo().alert().getText();
		}
		return strAlertText;
	}

	public static void acceptAlert(QAFExtendedWebDriver driver) {
		if (isAlertPresent(driver)) {
			driver.switchTo().alert().accept();
		}
	}

	public static void enterTextField(QAFWebElement element, String strValue) {
		if(element.isPresent()){
			element.clear();
			element.sendKeys(strValue);			
		}		
	}

	public static void selectFromDropDown(QAFWebElement element, String selectByVisibleText) {
		Select select = new Select(element);
		select.selectByVisibleText(selectByVisibleText);
	}
	
	public static void enterTextByChar(QAFExtendedWebDriver driver, QAFWebElement ele, String strVal)
			throws InterruptedException {
		ele.clear();
		waitForAjaxToComplete(driver);
		for (int i = 0; i < strVal.length(); i++) {
			char temp = strVal.charAt(i);
			ele.sendKeys(Character.toString(temp));
			Thread.sleep(100);
		}
		waitForAjaxToComplete(driver);
		Thread.sleep(3000);
	}	
}