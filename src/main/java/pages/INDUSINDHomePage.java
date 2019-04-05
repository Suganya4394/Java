package pages;



import java.awt.Desktop.Action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;




import com.relevantcodes.extentreports.ExtentTest;

import utils.Log;
import wrappers.ReconWrappers;

public class INDUSINDHomePage extends ReconWrappers{

	public INDUSINDHomePage(RemoteWebDriver driver,ExtentTest test) {
		this.driver = driver;
		this.test = test;
		switchToLastWindow();
		
		System.out.println(driver.getTitle());

		/*if (!verifyTitle(driver.getTitle())) {
			reportStep("This Not Home Page", "FAIL");
		}*/

	}


	public INDUSINDHomePage clickAccounts(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean c=verifySessionTerminatedForLogin(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		if (!c) {
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		if (!b) {
			
			String s1 = "Your password is disabled. Please reset your password by using 'Forgot your Sign on Password. Click to Generate Password' link on the login page";
			
			boolean b1=verifyTextPresentErrMsg(s1);
			if(!b1)
			{
//				Log.info("Started clickAccounts");
//				////*[@id='outerTab']/form/div[3]/ul/li[2]/div[1]/span
//				clickByXpath(prop.getProperty("ICICIHomePage.Accounts.XPATH"),
//						 bankName,	Accountno, amccode, startRunTime);
//				Log.info("Clicked clickAccounts successfully.");
//				Log.info("Ended clickAccounts");
				
				Thread.sleep(1000);
				WebElement Account_Services = driver.findElement(By.id("MODULE_ACCOUNT_SERVICES"));
				Thread.sleep(1000);
				Actions action = new Actions(driver);
				 
		        action.moveToElement(Account_Services).build().perform();
		        
		        WebElement Account_Statement = driver.findElement(By.id("dijit_PopupMenuItem_0_text"));
		        Thread.sleep(1000);
		        action.moveToElement(Account_Statement).build().perform();
		        
		        WebElement Online_Statement = driver.findElement(By.id("dijit_MenuItem_2_text"));
		        Thread.sleep(1000);
		        action.moveToElement(Online_Statement).build().perform();
		        
		        Online_Statement.click();
		        
				
				
			}
			else
			{
				
				String outputFilePath = syspathValidation(sysPath);
				System.out.println("outputFilePath" + outputFilePath);
				String dateAndTime = getAppRunningDateAndTime();


				takeSnapShotLoginFail(sysPath, Accountno, bankName, amccode, startRunTime, outputFileName);
				
				logReportBackend(bankName, Accountno, amccode,startRunTime,"Login Failed - "
						+ browserName, "FAIL");
				System.out.println("Login Failed");
				Log.info("Login Failed");
				quitBrowser();
				System.exit(0);
				
			}
		}
		
		
		}
		return this;
	}
	
	public INDUSINDHomePage clickOperativeAccounts(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		if (!b) {
			Log.info("Started clickOperativeAccounts");
			
			Thread.sleep(3000);
			clickByLink(prop.getProperty("ICICIHomePage.OperAcc.LinkText"),
					 bankName,	Accountno, amccode, startRunTime);
			Log.info("Clicked clickOperativeAccounts successfully.");
			Log.info("Ended clickOperativeAccounts");
		}
		return this;
	}
	public INDUSINDHomePage clickSearch(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String Accountno,String bankName,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		if (!b) {
			Log.info("Started clickSearch");
			clickByXpath(prop.getProperty("ICICIHomePage.Search.XPATH"),
					 bankName,	Accountno, amccode, startRunTime);
			Log.info("Clicked clickSearch successfully.");
			Log.info("Ended clickSearch");
		}
		return this;
	}
	
	public INDUSINDHomePage enterAccountNo(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		
		if (!b) {
			Log.info("Started enterAccountNo");
//			enterById(prop.getProperty("ICICIHomePage.EnterAccNumber.ID"),
//					Accountno,bankName, Accountno,  amccode, startRunTime);
			
			driver.findElement(By.id("account_no")).sendKeys(Accountno);
			
			Thread.sleep(1000);
			Log.info("Clicked enterAccountNo successfully.");
			Log.info("Ended enterAccountNo");
		}
		return this;
	}
	
	public INDUSINDHomePage clickSearch2(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		if (!b) {
			Log.info("Started Additional search");
			clickByXpath(prop.getProperty("ICICIHomePage.Search2.XPATH"),
					bankName,Accountno,  amccode, startRunTime);
			Thread.sleep(2000);
			Log.info("Clicked Additional search successfully.");
			Log.info("Ended Additional search");
		}
		return this;
	}
	
	public INDUSINDHomePage clickActions(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		if (!b) {
			Log.info("Started clickActions");
			clickByXpath(prop.getProperty("ICICIHomePage.Actions.XPATH"),
					bankName,Accountno,  amccode, startRunTime);
			Log.info("Clicked clickActions successfully.");
			Log.info("Ended clickActions");
		}
		return this;
	}
	
	public INDUSINDStmtDownloadPage ViewTransHist(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		if (!b) {
			Log.info("Started ViewTransactionHistory");
			clickByLink(prop.getProperty("ICICIHomePage.TransHist.LinkText"),
					bankName,Accountno,  amccode, startRunTime);
			Log.info("Clicked ViewTransactionHistory successfully.");
			Log.info("Ended ViewTransactionHistory");
		}
		return new INDUSINDStmtDownloadPage(driver, test);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	




}
