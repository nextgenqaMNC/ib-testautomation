package com.mnc.ib.pages;

import com.qmetry.qaf.automation.ui.WebDriverBaseTestPage;
import com.qmetry.qaf.automation.ui.annotations.FindBy;
import com.qmetry.qaf.automation.ui.api.PageLocator;
import com.qmetry.qaf.automation.ui.api.WebDriverTestPage;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;

public class FbHomePage extends WebDriverBaseTestPage<WebDriverTestPage> {
	
	@FindBy(locator = "//*[@id='u_0_a']/div[1]/div[1]/div/a/span/span")
	public QAFWebElement FbUserTitle;
	
	@FindBy(locator = "//a[text()='Home']")
	public QAFWebElement HomeLink;
	
	@FindBy(locator = "//a[text()='Create']")
	public QAFWebElement CreateLink;
	
	
	

	@Override
	protected void openPage(PageLocator p0, Object... p1) {
		// TODO Auto-generated method stub		
	}
	
	

}
