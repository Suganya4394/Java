package pages;


import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentTest;

import wrappers.ReconWrappers;

public class LogoffPage extends ReconWrappers{

	public LogoffPage(RemoteWebDriver driver,ExtentTest test) {
		this.driver = driver;
		this.test = test;
		if (!verifyTitle("Home")) {
			reportStep("This Not Home Page", "FAIL");
		}

		
	}	

	public LogoffPage clickLogout() throws InterruptedException

	{
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frame_top");
		Thread.sleep(1000);
		
	//	//clickByLink("Logout");
	//	clickByLink(prop.getProperty("LogoffPage.Logout.LINKTEXT"));
	

		return this;
	}




}
