package pages;


import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;

import utils.Log;
import wrappers.ReconWrappers;

public class INDUSINDStmtDownloadPage extends ReconWrappers{

	public INDUSINDStmtDownloadPage(RemoteWebDriver driver,ExtentTest test) {
		this.driver = driver;
		this.test = test;
		/*if (!verifyTitle("Home")) {
			reportStep("This Not Home Page", "FAIL");
		}*/
		
	

		
	}
	
	public INDUSINDStmtDownloadPage enterTransactionFromDate(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);

		if (!b) {
			boolean dateAfter3PM = checkCurrentdateGreater3PM();
			System.out.println(dateAfter3PM);
			System.out.println("after");
		
//			Added by Sandeep for fromdate - Start
			
			
			
			String fromDate = getCurrentDate();
			
			
			ResultSet rdata = null;
			
			rdata = readFromDateFromStoredProcedure(bankName,Accountno,"FROM_DATE_SELECTION");
			System.out.println("");
			String FromDate = null;
			
			while (rdata.next()) {
			
				 FromDate= rdata.getString("FromDate");
				
				System.out.println(FromDate);

				
			}
			
			try
			{
			WebElement fromdateselection=driver.findElement(By.id("from_date"));
			
			
			System.out.println(FromDate.format("YYYY-MM-dd"));
			
			try
			{
			String[] Seperater = FromDate.split(" ");
			FromDate=Seperater[0];
			}
			catch (Exception e) {

				Log.info("Error while reading RPBS_UPLOAD_DATE");
				

			}
			
			System.out.println(FromDate);
			
//			String pattern = "MM-dd-yyyy";
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//			String date = simpleDateFormat.format(FromDate);
//			System.out.println(date);
			

			System.out.println(FromDate);
			
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat format2 = new SimpleDateFormat(prop.getProperty("INDUSStmtDownloadPage.Date.Format"));
		    Date date = format1.parse(FromDate);
		    System.out.println(format2.format(date));
			
		    fromDate=format2.format(date);
		    System.out.println("From Date Value : "+fromDate);
		    
//			String[] DateFormat = FromDate.split("-");
//			String Day,Month,Year;
//			Day=DateFormat[2];
//			Month=DateFormat[1];
//			Year=DateFormat[0];
			Thread.sleep(1000);
			fromdateselection.click();
			fromdateselection.clear();
			Thread.sleep(1000);
			fromdateselection.sendKeys(fromDate);
			Thread.sleep(1000);
			
			}
			catch (Exception e) {
				Log.info(e.getMessage());
			}
//			Added by Sandeep for fromdate - End	
			
			
		}
		return this;

	}
	
	
	public INDUSINDStmtDownloadPage enterTransactionToDate(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
	
			if (!b) {
				try {
					Log.info("Started clicking calender icon");
//					
					
					WebElement todate = driver.findElement(By.id("to_date"));
					
					String To_Date = getCurrentDate();
					
//					
//					SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//				    SimpleDateFormat format2 = new SimpleDateFormat(prop.getProperty("INDUSStmtDownloadPage.Date.Format"));
//				    Date date = format1.parse(To_Date);
//				    System.out.println(format2.format(date));
//					
//				    To_Date=format2.format(date);
				    System.out.println("To Date Value : "+To_Date);
				    
				    todate.click();
				    todate.clear();
				    todate.sendKeys(To_Date);
					

				} catch (Exception e) {
					Log.error(e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return this;
	}
	
	public INDUSINDStmtDownloadPage SelectCreditDebits(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);

		if (!b) {
			Log.info("Started selecting credits and debits" );
			/*clickByXpath(
					prop.getProperty("ICICIStmtDownloadPage.Credits.XPATH"),
					Accountno, bankName, amccode, startRunTime);
			clickByXpath(
					prop.getProperty("ICICIStmtDownloadPage.Debits.XPATH"),
					Accountno, bankName, amccode, startRunTime);*/
		//	driver.findElementByXPath("//*[@id='PageConfigurationMaster_W112__2:section47.row86.C2']/span/label").click();
		//	driver.findElementByXPath("//*[@id='PageConfigurationMaster_W112__2:section85.row39.C2']/span/label").click();
			//WebElement we = driver.findElementByCssSelector("#PageConfigurationMaster_W112__2\3a section2\2e row37\2e C2 > span > label");
			
		//	we.click();
			
			
		
			
			
			
			Log.info("selected credits and debits Successfully" );
			Log.info("Ended selecting credits and debits" );
		}
		//driver.findElementByXPath("/html/body/div[4]/div[2]/div/div[3]/div/div[2]/div/div/ul/li/div[3]/div/div[1]/div/div[2]/div[2]/div[2]/div[3]/div/div[2]/div/span/div/p/span[1]/span/label").click();
		return this;
	}
	
	
	public INDUSINDStmtDownloadPage GetStatement(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception
	{
		boolean b=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);

		if (!b) {
			Log.info("Started clicking GetStatement ");
			clickByXpath(
					prop.getProperty("ICICIStmtDownloadPage.GetStmt.XPATH"),
					 bankName,	Accountno, amccode, startRunTime);
			Log.info("Clicked GetStatement Successfully");
			Log.info("Ended clicking GetStatement ");
		}
		return this;
	}
		
	public INDUSINDStmtDownloadPage AscendingOrder(String bankName,String Accountno, 
			String amccode, String startRunTime) throws Exception
	{
		Thread.sleep(3000);
		
		clickByXpath(prop.getProperty("ICICIStmtDownloadPage.AscOrder.XPATH"),bankName,Accountno,amccode,startRunTime);
		return this;
	}
	
	
public INDUSINDStmtDownloadPage DocumentType(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName, String ftpFlag,String ftpdomain , String ftpPath,
		String ftpUsername, String ftpPassword,String txtseperator,String mfcomp_domain_name,String mfcomp_ftp_path,String mfcomp_ftp_user_name,String mfcomp_ftp_password) throws Exception
{
	
	System.out.println("Bankname for document type"+bankName);
	
	System.out.println("Account number for document type"+Accountno);
	 //txtseperator="|";
	boolean c=verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);

	if (!c) {
		System.out.println(sysPath);
		Thread.sleep(10000);
		String recordtext = "No Records Found";
		boolean b = verifyTextPresentErrMsg(recordtext);
		System.out.println(b);
		Thread.sleep(1000);
		if (b) {
			System.out.println("Download no trxn error screenshot in a path");
			
			// Start To create an empty file
						String outputFilePath = syspathValidation(sysPath);
						System.out.println("outputFilePath" + outputFilePath);
						String dateAndTime = getAppRunningDateAndTime();

						String emptyFilePath = outputFilePath+"//NT//" + outputFileName + "_" + "NT"
								+ "_" + dateAndTime + "." + downloadedStmtFileType;

						createFile(emptyFilePath);
						// End To create an empty file
						
						System.out.println("amc code for no transaction"+amccode);

			takeSnapShotFailcase(sysPath,Accountno,bankName,   amccode,
					startRunTime, outputFileName, browserName);
			Log.info("No transaction so screen captured successfully.");
			
			
		} else {
			Thread.sleep(5000);
			String dateAndTime = getAppRunningDateAndTime();

			String outputFilePath = syspathValidation(sysPath);
			//	String outputftpPath=syspathValidation(ftpPath);
			System.out.println("outputFilePath" + outputFilePath);
			//Added on 10-Apr-2018
			
			System.out.println("Bank Name:"+bankName);
			String tempFileDownloadedPath = outputFilePath + bankName;

			String getdownloadedFile = getFileNameFromdirectory(tempFileDownloadedPath);
			System.out.println("Started getdownloadedFile type");
			if (getdownloadedFile != null) {
				String notDownloadedfile = tempFileDownloadedPath + "\\"
						+ getdownloadedFile;

				System.out.println("notDownloadedfile" + notDownloadedfile);

				fileDelete(notDownloadedfile);
			}
			Log.info("Started selecting menu type");

			Thread.sleep(5000);
			//selectIndexById(prop.getProperty("ICICIStmtDownloadPage.DocType.ID"), "2");
			verifySessionTerminatedErrorMsg(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
			
			//Thread.sleep(5000);
			//Log.info("Started selecting menu type");
		/*clickByXpath(
					prop.getProperty("ICICIStmtDownloadPage.ToSelectDocType.XPATH"),
					Accountno, bankName, amccode, startRunTime);*/
//			clickById(
//					prop.getProperty("ICICIStmtDownloadPage.ToSelectDocType.ID")
//					, bankName,Accountno, amccode, startRunTime);
//			System.out.println("Started selecting menu type");
			//driver.findElementById("PageConfigurationMaster_W112__2:TransactionHistoryFG.OUTFORMATSelectBoxItText").click();
		//	driver.findElementByXPath("//*[@id='PageConfigurationMaster_W112__2:TransactionHistoryFG.OUTFORMATSelectBoxItArrowContainer']").click();
			Thread.sleep(3000);
			if (browserName.equalsIgnoreCase("firefox")) {

				Robot robot = new Robot(); // Robot class throws AWT Exception	
				Thread.sleep(2000); // Thread.sleep throws InterruptedException	
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyRelease(KeyEvent.VK_DOWN);
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyRelease(KeyEvent.VK_DOWN);
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyRelease(KeyEvent.VK_DOWN);
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyRelease(KeyEvent.VK_DOWN);
				robot.setAutoDelay(3000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				Thread.sleep(3000);

			} else {
				
				Thread.sleep(2000);
				
				WebDriverWait wait = new WebDriverWait(driver, 10); 
				
				WebElement transaction_value = driver.findElement(By.xpath("//*[@id=\"dojox_grid__View_1\"]/div/div/div/div[1]/table/tbody/tr/td[1]"));
				
				wait.until(ExpectedConditions.visibilityOf(transaction_value));
				
				driver.findElement(By.id("dijit_form_DropDownButton_0")).click();
				
				Log.info("Started selecting document type");
				if(downloadedStmtFileType.equalsIgnoreCase("XLS"))
				{
					System.out.println("downloadedStmtFileType"+downloadedStmtFileType);
				new Actions(driver)
						.moveToElement(driver.findElementByLinkText("EXCEL"))
						.click().perform();
				}
				else if(downloadedStmtFileType.equalsIgnoreCase("PDF"))
				{
					System.out.println("downloadedStmtFileType"+downloadedStmtFileType);
				new Actions(driver)
						.moveToElement(driver.findElementByLinkText("PDF"))
						.click().perform();
				}
				else if(downloadedStmtFileType.equalsIgnoreCase("CSV"))
				{
					System.out.println("downloadedStmtFileType"+downloadedStmtFileType);
				new Actions(driver)
						.moveToElement(driver.findElementByLinkText("CSV"))
						.click().perform();
				Thread.sleep(3000);
				
				
				}
				Log.info("Selected document type successfully");
				Log.info("Ended selecting document type");
				//Thread.sleep(8000); 
			}

			System.out.println(b);

			//The transactions do not exist for the account with the entered criteria.
			String downloadDate = getAppRunningDate();
			String downloadEndTime = getAppRunningTime();

			System.out.println("Browser name for robot class" + browserName);
			//String newpath=sysPath+"\\"+Accountno+"_"+bankName+"_"+downloadDate+"_"+downloadEndTime+".xls";
			//System.out.println(newpath);
			//fileRenameUsingRobot(browserName,newpath);
			//	Thread.sleep(5000); 
			String downloadEndTime1 = getAppRunningDateTime();

			//String newpath=sysPath+"\\"+Accountno+"_"+bankName+"_"+downloadDate+"_"+downloadEndTime;

			//System.out.println(newpath);
			//	insertReconBankStatementLog(downloadDate,bankName, accountNo,"H",sTime, downloadEndTime1,"PASS","File is downloaded");
			//Retrieve file from temporavary path 

			try {
				//String tempFileDownloadedPath =sysPath+"\\"+bankName;

				String getFile = getFileNameFromdirectory(tempFileDownloadedPath);

				//Thread.sleep(3000); 

				System.out.println("Get file from temporavary path" + getFile);
 
				if (getFile != null) {

					//String downloadedfileName=tempFileDownloadedPath+"\\"+getFile;
					String newpath = outputFilePath + outputFileName + "_"
							+ dateAndTime;
					
					// To be Removed Again - Start
					
					
//					newpath=newpath.replace(" ", "_");
//					outputFileName=outputFileName.replace(" ", "_");
					
					// To be Removed Again - END
					
					
					
					
					
					
					//String downloadedfileName=tempFileDownloadedPath+"\\"+getFile.getName();

					String downloadedfileName = tempFileDownloadedPath + "\\"
							+ getFile;
					boolean fileRenamenMove = fileRenameAndMove(
							downloadedfileName, newpath, downloadedStmtFileType);

					Log.info("file renamed");

					if (fileRenamenMove) {
						Log.info("File is renamed and moved to output filepath");
						logReportBackend(bankName, Accountno, amccode,
								startRunTime, "File is renamed and moved to output filepath-"
										+ browserName,"PASS");
						
						if (ftpFlag.equalsIgnoreCase("Y")) {

							if ((ftpPath != null) && (ftpUsername != null)
									&& (ftpPassword != null))

							{

								String outputftpPath = syspathValidation(ftpPath);
								

								try {
									/*
									ProcessBuilder pb=new ProcessBuilder("Sterling_Sftp_Utility.exe docmission "
											+ ftpUsername+" "
											+ftpPassword + " "
											+ "D:\\Recon_Automation_Output\\BIRLA\\IDBI BANK LTD_004103000032407_04Jan2019142036.TXT "
											+ "\\\\192.168.3.163\\mfcomp_scheduler"
											+ "\\test\\IDBI BANK LTD_004103000032407_04Jan2019142036.TXT");
									pb.start();
									pb.notify();*/
									
									new ProcessBuilder(
											"./drivers/Sterling_Sftp_Utility.exe",
											ftpUsername, ftpPassword, newpath
													+ "."
													+ downloadedStmtFileType,
											outputftpPath + outputFileName
													+ "_" + dateAndTime + "."
													+ downloadedStmtFileType)
											.start();
									
									
									
									Log.info("moved to ftp" + ftpUsername + "-"
											+ ftpPassword + "-" + newpath + "-"
											+ outputftpPath + outputFileName
											+ "_" + dateAndTime);

									logReportBackend(bankName, Accountno,
											amccode, startRunTime,
											"File is  moved to FTP path-"
													+ browserName, "PASS");
								} catch (Exception e) {
									e.printStackTrace();
									
									logReportBackend(bankName, Accountno,
											amccode, startRunTime,
											"File is not moved to FTP path-"
													+ browserName, "FAIL");
									
									// TODO: handle exception
								}
							}
						}
						else
						{
							
							Log.info("FTP CREDENTIALS ARE REQUIRED IF FTPFlag IS Y ");
							
							
						}
						
						// Added by Sandeep for 27579-1  -- Start
						
						if(mfcomp_domain_name!=null&&mfcomp_ftp_path!=null&&mfcomp_ftp_user_name!=null&&mfcomp_ftp_password!=null&&
						   mfcomp_domain_name.trim()!=""&&mfcomp_ftp_path.trim()!=""&&mfcomp_ftp_user_name.trim()!=""&&mfcomp_ftp_password.trim()!="")
						{
							
								
								try
								{
								String mfcomp_outputftpPath = syspathValidation(mfcomp_ftp_path);
								
								System.out.println("MF_COMP_OUTPUT FILE PATH "+mfcomp_outputftpPath);
								
//								new ProcessBuilder(
//										"./drivers/Sterling_Sftp_Utility.exe",
//										ftpUsername, ftpPassword, newpath
//												+ "."
//												+ downloadedStmtFileType,
//										ftpPath + outputFileName
//												+ "_" + dateAndTime + "."
//												+ downloadedStmtFileType)
//										.start();
								
//								new ProcessBuilder(
//										"./drivers/Sterling_Sftp_Utility.exe"
//										+" "
//										+ mfcomp_domain_name
//										+ " "
//										+ mfcomp_ftp_user_name
//										+ " "
//										+ mfcomp_ftp_password
//										+ " "
//										+ newpath
//										+ "."
//										+ downloadedStmtFileType
//										+ " "
//										+ mfcomp_outputftpPath
//										+ outputFileName
//										+ "_" + dateAndTime + "."
//										+ downloadedStmtFileType)
//										.start();
								
								new ProcessBuilder(
										"./drivers/Sterling_Sftp_Utility.exe",mfcomp_domain_name,
										mfcomp_ftp_user_name,
										mfcomp_ftp_password,
										newpath
										+ "."
										+ downloadedStmtFileType,
										mfcomp_outputftpPath
										+ outputFileName
										+ "_" + dateAndTime + "."
										+ downloadedStmtFileType)
										.start();
								
								
								Log.info("File is moved to MF_COMP_FTP path");
								logReportBackend(bankName, Accountno,
										amccode, startRunTime,
										"File is moved to MF_COMP_FTP path-"
												+ browserName, "PASS");
								}
								catch (Exception e) {

									Log.info("Error in moving file to MF_COMP_FTP");
									Log.info(e.getMessage());
									logReportBackend(bankName, Accountno,
											amccode, startRunTime,
											"File is not moved to MF_COMP_FTP path-"
													+ browserName, "FAIL");
								}
								
							
						}
						else
						{
							Log.info("Error in moving file to MF_COMP_FTP due to InValid MF_COMP_FTP Credentials");
							logReportBackend(bankName, Accountno,
									amccode, startRunTime,
									"File is not moved to MF_COMP_FTP path-"
											+ browserName, "FAIL");
						}
						
						// Added by Sandeep for 27579-1  -- End
					}

					else {
						Log.info("File is not renamed and moved to output filepath");
						logReportBackend(bankName, Accountno, amccode,
								startRunTime, "File is not Renamed and moved ",
								"FAIL");
					}

				} else {
					Log.info("File is not downloaded in the folder");
					logReportBackend(bankName, Accountno, amccode,
							startRunTime,
							"File is not downloaded in the folder ", "FAIL");
					System.out.println("File is not downloaded in the folder");
					
					System.out.println("Bankname for log"+bankName);
					
					System.out.println("Account number for log"+Accountno);
				}
				Log.info("Ended clickDownloadInAccountActivityPage");
			} catch (Exception e) {

				Log.error(e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//	insertReconBankStatementLog(downloadDate,bankName, accountNo,"H",sTime, downloadEndTime1,"PASS","File is downloaded");
	}
//logReportBackend(bankName, Accountno, amccode, startRunTime,"File is downloaded", "PASS");
return this;
	
}
	
	
	
	
	
	
			
		}







