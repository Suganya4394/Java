package wrappers;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import pages.INDUSINDHomePage;

import pages.INDUSINDLoginPage;
import pages.INDUSINDStmtDownloadPage;
import testCases.INDUSINDMainPgm;
import testCases.INDUSINDMainPgm;

import utils.DataInputProvider;
import utils.Log;

public class ReconWrappers extends GenericWrappers {

	public String browserName;
	public String dataSheetName;
	public String url;
	public String downloadPath;
	public String reScheduled = null;

	/*
	 * Sequence of execution of annotations BeforeSuite BeforeTest BeforeClass
	 * BeforeMethod Test AfterMethod AfterClass AfterTest AfterSuite
	 */

	@BeforeSuite
	public void beforeSuite() {
		startResult();
	}

	@BeforeTest
	public void beforeTest() {
		loadObjects();
	}

	@BeforeMethod
	public void beforeMethod() {
		test = startTestCase(testCaseName, testDescription);
		test.assignCategory(category);
		test.assignAuthor(authors);

		// invokeApp(browserName,url,downloadPath);

	}

	@AfterSuite
	public void afterSuite() {
		endResult();
		
	}

	@AfterTest
	public void afterTest() {
		unloadObjects();
	}

	@AfterMethod
	public void afterMethod() {
	endTestcase();
		quitBrowser();
		Log.info("Finally all the browsers are closed");
		
		

	}

	@DataProvider(name = "fetchData")
	public Object[][] getData() {
		return DataInputProvider.getSheet(dataSheetName);
	}

	public void downloadAccountStmt(String browserName, String appUrl,
			String corporateID, String UserID, String lpwd, String sysPath
			, String bankName,	String Accountno, String startRunTime,
			String amccode, String isManual,String downloadedStmtFileType,String outputFileName,String sessionFlag) throws Exception {
		System.out.println(sysPath);
		System.out.println(amccode);
		System.out.println("bankname:"+bankName);
		// Execution started browser launch
		// invokeApp(browserName,appUrl,bankName,amccode,startRunTime);
		
		

		
		invokeApp(browserName, appUrl, bankName,Accountno, amccode, startRunTime, sysPath);
		
		// bankName,accountNo,amcCode,startRunTime
		
		driver.findElement(By.id("continue")).click();
		new INDUSINDLoginPage(driver, test)
				.enterCorporateID(corporateID, bankName, Accountno, amccode,
						startRunTime)
				.enterUserID(UserID, bankName, Accountno, amccode, startRunTime)
				.enterPassword(lpwd, bankName, Accountno, amccode, startRunTime)
				.enterCaptcha(bankName, Accountno, amccode, startRunTime);
//				.clickLogin(browserName,appUrl,corporateID,UserID,lpwd,sysPath,bankName,Accountno,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		
		boolean b=verifyTextPresentErrMsg("Sorry, the entered CAPTCHA is incorrect");
		boolean b1=verifyTextPresentErrMsg("Sorry your username or password is incorrect for that company");
		
		if(b)
		{
//			Thread.sleep(2000);
//			driver.close();
//			driver.quit();
//			i++;
//			downloadAccountStmt(browserName, appUrl, corporateID, UserID, lpwd, sysPath, bankName, Accountno, startRunTime, amccode, isManual, downloadedStmtFileType, outputFileName, sessionFlag);
			String Captcha_error_msg1="Invalid CAPTCHA has captured or your old session has become invalid. This is due to session timeout. Please try now...!";
			String Captcha_error_msg2="Sorry, the entered CAPTCHA is incorrect";
			while(verifyTextPresentErrMsg(Captcha_error_msg2)||verifyTextPresentErrMsg(Captcha_error_msg1))
			{
				new INDUSINDLoginPage(driver, test)
				.enterCorporateID(corporateID, bankName, Accountno, amccode,
						startRunTime)
				.enterUserID(UserID, bankName, Accountno, amccode, startRunTime)
				.enterPassword(lpwd, bankName, Accountno, amccode, startRunTime)
				.enterCaptcha(bankName, Accountno, amccode, startRunTime);
				Thread.sleep(1000);
			}
			
		}
		if(b1)
		{
			Log.info("Invalid UserName or Password");
			logReportBackend(bankName, Accountno, amccode, startRunTime, "Invalid UserName or Password", "FAIL");
		}
		else
		{
		
		
		new INDUSINDHomePage(driver, test)
				.clickAccounts(browserName,appUrl,corporateID,UserID,lpwd,sysPath,bankName,Accountno,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);

	
		ResultSet rdata = null;
		isManual = INDUSINDMainPgm.manualBankName;
	

		if (sessionFlag != null) {
			
			
		 String	sessionStartRunTime=null;
			
		 sessionStartRunTime=INDUSINDMainPgm.initialStartRunTimeSession;
		 
		 Log.info("SessionStartRunTime to fetch account details"+sessionStartRunTime);
			
			if (!isManual.equals("")) {
				
				 rdata = readAccNoDataWhenSessionOutFromStoredProcedure(
						bankName, "SESSIONRESHEDULED", sessionStartRunTime);
				 
				  
				    
				    
			}
			
			else {

				System.out.println("Manual bank is not there");
			
				 rdata = readAccNoDataWhenSessionOutFromStoredProcedure(
						bankName, "SESSIONRETURN", sessionStartRunTime);
				 
				 
			}}

					
					
			
			else {

				if (!isManual.equals("")) {
					

					 rdata = readAccNoDataFromStoredProcedure(bankName,
							"RESCHEDULED");
				}
			 else {

				System.out.println("Manual bank is not there");
				

				 rdata = readAccNoDataFromStoredProcedure(bankName,
						"BANKDETAILS");}}
		int i=0;
			while (rdata.next()) {

				i++;
				System.out.println("loop entered for non manual");

				String accountNo = rdata.getString("ACCOUNT_NO");
				// String accountNo ="039305004802";
				String ftpFlag = rdata.getString("ftp_flag");
				

				String ftpPath = rdata.getString("ftp_path");

				String ftpUsername = rdata.getString("ftp_user_name");

				String ftpPassword = rdata.getString("ftp_password");
				
				String filename = rdata.getString("OUTPUT_FILE_NAME");
				
				String txtseperator = rdata.getString("TXT_SEPERATOR");
				//Added by Sandeep For MF_COMP CR 27579-1  - START
				
				String ftpdomain=rdata.getString("domain_name");
				String mfcomp_domain_name=rdata.getString("mfcomp_domain_name");
				String mfcomp_ftp_path=rdata.getString("mfcomp_ftp_path");
				String mfcomp_ftp_user_name=rdata.getString("mfcomp_ftp_user_name");
				String mfcomp_ftp_password=rdata.getString("mfcomp_ftp_password");
				
				//Added by Sandeep For MF_COMP CR 27579-1  - END
				
				
				
				System.out.println("Accountno" + accountNo);
				
				 System.out.println("TextSeperator"+txtseperator);
				System.out.println(filename);
				Thread.sleep(2000);
				
				Log.info("Date Range selection radio button click Start");
				driver.findElement(By.id("dateRange")).click();
				Log.info("Date Range selection radio button click End");
				
				Log.info("accounts Search button click Start");
				driver.findElement(By.xpath("//*[@id=\"account_no_row\"]/a")).click();
				Log.info("accounts Search button click Start");
				
				Thread.sleep(1500);
				Log.info("List of accounts Search account number entry Start");
				driver.findElement(By.id("accountNo")).sendKeys(accountNo);
				Log.info("List of accounts Search account number entry End");
				
				Log.info("List of accounts Search button click Start");
				driver.findElement(By.id("dijit_form_Button_"+i+"_label")).click();
				Log.info("List of accounts Search button click Start");
				
				if(verifyTextPresentErrMsg("No accounts found"))
				{
					System.out.println("Account_No : "+Accountno+" Is mismatched");
					logReportBackend(bankName, accountNo, amccode, startRunTime, "Invalid Account_No", "FAIL");
					
					Log.info("List of accounts window close button click Start");
					driver.findElement(By.xpath("//*[@id=\"xhrDialog\"]/div[1]/span[2]")).click();
					Log.info("List of accounts window close button click End");
					
					
					continue;
				}
				else
				{
				driver.findElementByLinkText(accountNo).click();
				
//					new INDUSINDHomePage(driver, test)
//
//					.enterAccountNo(browserName,appUrl,corporateID,UserID,lpwd,sysPath,bankName,accountNo,startRunTime,amccode,isManual,downloadedStmtFileType,filename);
					
					new INDUSINDStmtDownloadPage(driver, test)
					.enterTransactionFromDate(browserName,appUrl,corporateID,UserID,lpwd,sysPath,bankName,accountNo,startRunTime,amccode,isManual,downloadedStmtFileType,filename)
//					.enterTransactionToDate(browserName,appUrl,corporateID,UserID,lpwd,sysPath,bankName,accountNo,startRunTime,amccode,isManual,downloadedStmtFileType,filename)
					.SelectCreditDebits(browserName,appUrl,corporateID,UserID,lpwd,sysPath,bankName,accountNo,startRunTime,amccode,isManual,downloadedStmtFileType,filename);
					
					driver.findElement(By.id("submit_label")).click();
					Thread.sleep(2000);
					
					
					
				
					
					
//					.GetStatement(browserName,appUrl,corporateID,UserID,lpwd,sysPath,bankName,accountNo,startRunTime,amccode,isManual,downloadedStmtFileType,filename)
//					
					new INDUSINDStmtDownloadPage(driver, test)
					.DocumentType(browserName,appUrl,corporateID,UserID,lpwd,sysPath,bankName,accountNo,startRunTime,amccode,isManual,downloadedStmtFileType,filename,ftpFlag, ftpdomain,ftpPath,
							ftpUsername, ftpPassword,txtseperator,mfcomp_domain_name,mfcomp_ftp_path,mfcomp_ftp_user_name,mfcomp_ftp_password);
				}
			}
			System.out.println(" loop completed");
		
			Thread.sleep(1000);
			Log.info("Logout Click Start");
				driver.findElement(By.xpath("//*[@id=\"layout\"]/div[1]/div[1]/div/div[2]/div/span/span")).click();
				Log.info("Logout Click End");
				Thread.sleep(1000);
			//Added by Sandeep for Run Count - Start
			
			
			String endTimeDef_run_count = getAppRunningDateTime();
			
			try {
						Log.info("Run count incriment started");
						runInsertReconBankStatementLogFromStoredProcedure(bankName,INDUSINDMainPgm.initialStartRunTimeSession, endTimeDef_run_count);
						Log.info("Run count incriment ended");
						
					} catch (Exception e) {
						
						Log.info(e.getMessage());
						Log.info("Error Run count incriment");
						
						
					}
		}
			//Added by Sandeep for Run Count - End
		
		
		//quitBrowser();

	}
	
	
	
//--------Project Specific methods-------	
	
	
	
	public boolean verifySessionTerminatedErrorMsg(
			String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String Accountno,String bankName,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception  {	

		boolean b=false;

		b=verifyTextPresentErrMsg("APPLICATION SECURITY ERROR !!!");

		System.out.println("verify session error msg"+b);


		if(b)
		{
			System.out.println("Session msg is present in page");
			b=true;
			String downloadEndTime= getAppRunningDateTime();
			String downloadDate=getAppRunningDate();        
			Thread.sleep(1000);

			logReportBackend(bankName,"",amccode, startRunTime, "Session Terminated. Please login again msg is present", "FAIL");
			//insertReconBankStatementLog(downloadDate,bankName,accountNo,"H",downloadEndTime, downloadEndTime,"FAIL","Session Terminated. Please login again msg is present");
			quitBrowser();
			Thread.sleep(3000);
			downloadAccountStmt(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,bankName,Accountno,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName,"Y");
			//quitBrowser();
			//Get start time
			//String startRunTime = getAppRunningDateTime();

			Thread.sleep(2000);
			

		}


		return b;


	}
	
	
	public boolean verifySessionTerminatedForLogin(
			String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String Accountno,String bankName,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception  {	

		boolean b=false;

		b=verifyTextPresentErrMsg("Your Session has expired");

		System.out.println("verify session error msg"+b);


		if(b)
		{
			System.out.println("Session msg is present in page");
			b=true;
			String downloadEndTime= getAppRunningDateTime();
			String downloadDate=getAppRunningDate();        
			Thread.sleep(1000);
			clickByLink("Click here to go to Login Page", bankName, Accountno, amccode, startRunTime);
			//logReportBackend(bankName,"",amccode, startRunTime, "Session Terminated. Please login again msg is present", "FAIL");
			//insertReconBankStatementLog(downloadDate,bankName,accountNo,"H",downloadEndTime, downloadEndTime,"FAIL","Session Terminated. Please login again msg is present");
			quitBrowser();
			Thread.sleep(3000);
			downloadAccountStmt(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,bankName,Accountno,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName,"Y");

			//Get start time
			//String startRunTime = getAppRunningDateTime();
			//quitBrowser();
			Thread.sleep(2000);
			

		}


		return b;


	}
	
	
	
	
	
	
	
	
	
	
	
}
