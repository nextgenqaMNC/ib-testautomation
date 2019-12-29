package com.mnc.ib.pages;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.WebDriverBaseTestPage;
import com.qmetry.qaf.automation.ui.annotations.FindBy;
import com.qmetry.qaf.automation.ui.api.PageLocator;
import com.qmetry.qaf.automation.ui.api.WebDriverTestPage;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.Reporter;
import com.mnc.ib.utils.PageUtils;

public class FbLoginPage extends WebDriverBaseTestPage<WebDriverTestPage> {

	@FindBy(locator = "//input[@id='email']")
	public QAFWebElement useridTextBox;

	@FindBy(locator = "//input[@id='pass']")
	public QAFWebElement pwdTextBox;

	@FindBy(locator = "//input[@value='Log In']")
	public QAFWebElement loginBtn;

	@FindBy(locator = "//input[@name='firstname']")
	public QAFWebElement firstNameTxtBox;

	@FindBy(locator = "//input[@name='lastname']")
	public QAFWebElement lastNameTxtBox;

	@FindBy(locator = "//input[@name='reg_passwd__']")
	public QAFWebElement newPwdTxtBox;

	@FindBy(locator = "//input[@name='reg_email__']")
	public QAFWebElement emailTxtBox;

	@Override
	public void openPage(PageLocator arg0, Object... arg1) {
		driver.manage().deleteAllCookies();
		driver.get(ConfigurationManager.getBundle().getPropertyValue(
				"env.baseurl"));
		driver.manage().window().maximize();
		login((HashMap<String, String>) arg1[0]);
	}

	public void login(HashMap<String, String> testData) {
		useridTextBox.sendKeys(testData.get("userid"));
		pwdTextBox.sendKeys(testData.get("password"));
		loginBtn.click();
		PageUtils.waitForAjaxToComplete(driver);
		takeScreenshot("Login");
		Reporter.log("testing to just log");
		Reporter.logWithScreenShot("testing to just log with screenshot");
	}

	public void takeScreenshot(String name) {
		File srcFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File("C:\\testresults\\" + name
					+ ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
