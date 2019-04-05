package wrappers;

import static org.testng.Assert.assertEquals;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import oracle.jdbc.OracleTypes;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import General.Decrypt;

import com.relevantcodes.extentreports.ExtentTest;

import testCases.INDUSINDMainPgm;
//import testCases.MainPgm;

//import testCases.MainPgm;
import utils.Log;
import utils.Reporter;

public class GenericWrappers extends Reporter implements Wrappers {

	public GenericWrappers(RemoteWebDriver driver, ExtentTest test) {
		this.driver = driver;
		this.test=test;
	}
	private CallableStatement callSt = null; 
	public String downloadDate="";

	public String endTime="";

	public String sql="";
	public static String bankName;
	public RemoteWebDriver driver;
	protected static Properties prop;
	public String sUrl,primaryWindowHandle,sHubUrl,sHubPort,dbDriver,dbServer,dbPort,dbName,dbUser,dbPwd;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	public String dbuname;
	public static String proxyIp=null;
	public static String proxyPortNumber=null;
	private ResultSet resultSet = null;
	public static String reportDate,cDate,reportTime;
	static Cipher cipher;  
	SecretKey key;
	public String amc= INDUSINDMainPgm.selectedAmc;
	public GenericWrappers() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./drivers/resources/config.properties")));
			//dbuname = prop.getProperty("DBNAME");
			bankName = prop.getProperty("BANKNAME");
			
			System.out.println("bankName"+ prop.getProperty("BANKNAME"));
			proxyIp= prop.getProperty("PROXYIP");
			System.out.println("proxyIp"+  prop.getProperty("PROXYIP"));
			proxyPortNumber= prop.getProperty("PROXYPORTNUMBER");
			System.out.println("proxyPortNumber"+ prop.getProperty("PROXYPORTNUMBER"));
			
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			//sUrl = prop.getProperty("URL");
			dbDriver = prop.getProperty("DBDRIVER");
			dbServer = prop.getProperty("DBSERVER");
			dbPort = prop.getProperty("DBPORT");
			dbName = prop.getProperty("SID");
			dbUser = prop.getProperty("DBUSERNAME");
			dbPwd = prop.getProperty("DBPASSWORD");



		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadObjects() {
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./drivers/resources/object.properties")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void unloadObjects() {
		prop = null;
	}

	/**
	 * This method will launch the browser in local machine and maximise the browser and set the
	 * wait for 30 seconds and load the url
	 * @author Sterling Software
	 * @param url - The url with http or https
	 * @return 
	 * 
	 */
	public RemoteWebDriver invokeApp(String browser,String url,String bankName,String accountNo,String amcCode,String startRunTime,String sysPath) throws Exception  {
		return invokeApp(browser,false,url,bankName,accountNo,amcCode,startRunTime,sysPath);
	}
	
	
	public static boolean isDirectoryExists(String directoryPath)

	  {
	    if (!Paths.get(directoryPath).toFile().isDirectory())
	    {
	      return false;
	    }
	    return true;
	  }

	/**
	 * This method will launch the browser in grid node (if remote) and maximise the browser and set the
	 * wait for 30 seconds and load the url 
	 * @author Sterling Software
	 * @param url - The url with http or https
	 * @return 
	 * 
	 */
	@SuppressWarnings("deprecation")
	public RemoteWebDriver invokeApp(String browser, boolean bRemote,String url,String bankName,String accountNo,String amcCode,String startTime,String sysPath) throws Exception {
		try {

			System.out.println("App is invoked");

			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browser);
			dc.setPlatform(Platform.WINDOWS);



			boolean dirExists=isDirectoryExists(sysPath);

			if(!dirExists)
			{

				Log.error("The Output path directory is not available in the system"); 
				createDirectory(sysPath);


			}

			Log.error("The Output path directory is available in the system");  

		
			 
				 

			// sysPath = "//192.168.3.1/sspl user/Saranya SS/recon";
			// sysPath = "D:";
			String outputFilePath=syspathValidation(sysPath);
			String dirPath =outputFilePath+bankName;
			//String dirPath =sysPath+"\\"+bankName;
			System.out.println("Directory path"+dirPath);
			boolean temporarydirExists = isDirectoryExists(dirPath);
			
			if (!temporarydirExists)
			{
			// Temporavary folder is created
			createDirectory(dirPath);
			
			
			}
			

			//Temporavary folder is created
			//createDirectory(dirPath);
			
		//	fileDelete(dirPath);
			// this is for grid run
			if(bRemote)
				driver = new RemoteWebDriver(new URL("http://"+sHubUrl+":"+sHubPort+"/wd/hub"), dc);
			else{ // this is for local run
				if(browser.equalsIgnoreCase("chrome")){


					System.out.println("Launch chrome driver");
					/*System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
					driver = new ChromeDriver();*/

					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");






					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("profile.default_content_settings.popups", 0);
					System.out.println("Directory path before download"+dirPath);
					chromePrefs.put("download.default_directory",dirPath);
					ChromeOptions options = new ChromeOptions();

					options.setExperimentalOption("prefs", chromePrefs);
					proxyIp = GenericWrappers.proxyIp;
					proxyPortNumber = GenericWrappers.proxyPortNumber;
					
					
					// String PROXY = "localhost:8080";
					
					String PROXY = GenericWrappers.proxyIp+":"+GenericWrappers.proxyPortNumber;
					  //Bellow given syntaxes will set browser proxy settings using DesiredCapabilities.
					  Proxy proxy = new Proxy();
					  proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY)
					    .setSocksProxy(PROXY);
					DesiredCapabilities cap = DesiredCapabilities.chrome();
					cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					cap.setCapability(ChromeOptions.CAPABILITY, options);
					  cap.setCapability(CapabilityType.PROXY, proxy);
					driver = new ChromeDriver(cap); 





				}


				else if(browser.equalsIgnoreCase("ie"))
				{

					System.out.println("Launch ie driver");
					System.setProperty("webdriver.ie.driver", "./drivers/IEDriverServer.exe");
					/*	driver = new InternetExplorerDriver();\\
					 */


					// Create the DesiredCapability object of InternetExplorer
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();

					// Settings to Accept the SSL Certificate in the Capability object
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

					driver = new InternetExplorerDriver(capabilities);
				}








				else{

					System.out.println("Launching firefox browser.....");



					System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");


					FirefoxProfile profile = new FirefoxProfile();


					profile.setPreference("browser.download.folderList",2); 
					profile.setPreference("browser.download.dir", dirPath);
					profile.setPreference("browser.download.manager.showWhenStarting",false);


					profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "fcatjava/download"); 


					profile.setPreference("pdfjs.disabled", true);  // disable the built-in PDF viewer



					FirefoxOptions options = new FirefoxOptions();
					options.setProfile(profile);

					//	driver = new FirefoxDriver(options);

				}
			}
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			try {
				driver.get(url);
		
				String titleText = driver.getTitle();

				if (!(titleText.contains("Welcome to IndusInd ConnectOnline Portal - Login Screen")))

				{
					Log.info("Loading page tittle is not matched");

					boolean b = verifyTextPresentErrMsg("This site can’t be reached");
					
					boolean b1 = verifyTextPresentErrMsg("There is no Internet connection");

					if (b)

					{
						
						System.out.println("This site can’t be reached");

						logReportBackend(bankName, accountNo, amcCode,startTime, "This site can’t be reached", "FAIL");
						//throw new RuntimeException("FAILED");
						quitBrowser();
						System.exit(1);

					}
					else if(b1)
					{
						System.out.println("There is no Internet connection");
						
						logReportBackend(bankName, accountNo, amcCode,startTime, "There is no Internet connection", "FAIL");
						
						System.out.println("There is no Internet connection-donne");
						quitBrowser();
						System.exit(1);
						//throw new RuntimeException("FAILED");

						
											}
					else
					{
						logReportBackend(bankName, accountNo, amcCode,
								startTime, "Unable to identify site loaded status", "FAIL");
						quitBrowser();
						System.exit(1);
						
					} 

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				String expMsg = getExceptionMessageUpTo2000Charcters(e
						.getMessage());

				Log.error(expMsg);

				logReportBackend(bankName, accountNo, amcCode, startTime,
						expMsg, "FAIL");
			}


			primaryWindowHandle = driver.getWindowHandle();		
			reportStep("The browser:" + browser + " launched successfully", "PASS");

			Log.info("The browser:" + browser + " launched successfully");

		}

		catch(NullPointerException nexp)
		{

			Log.error("Error during when browser launched"+nexp.getMessage());
			reportStep("The browser:" + browser + " could not be launched", "FAIL");
			logReportBackend(bankName,accountNo,amcCode,startTime,"The browser:" + browser + " could not be launched", "FAIL");
		}

		catch (Exception e) {
			e.printStackTrace();
			//insertReconBankStatementLog(String downloadDate,String bankName,String accountNo,String amcCode,String startTime,String endTime,String status,String reason)
			Log.error(e.getMessage());
			logReportBackend(bankName,accountNo,amcCode,startTime,"The browser:" + browser + " could not be launched", "FAIL");
			System.out.println("error from browser"+e.getMessage());
			// logReportBackend(bankName,accountNo,amcCode,startTime,"could not be launched","FAIL");
			reportStep("The browser:" + browser + " could not be launched", "FAIL");
		}


		return driver;
	}
		
		
		public boolean createDirectory(String dirPath )
		{

			boolean isDir=false;
			System.out.println("dirPath"+dirPath);
			// Check If Directory Already Exists Or Not?
			Path dirPathObj = Paths.get(dirPath);
			boolean dirExists = Files.exists(dirPathObj);
			if(dirExists) {
				System.out.println("! Directory Already Exists !");
				isDir=true;
			} else {
				try {
					// Creating The New Directory Structure
					Files.createDirectories(dirPathObj);

					System.out.println("dirPathObj"+dirPathObj);
					System.out.println("! New Directory Successfully Created !");
				} catch (IOException ioExceptionObj) {
					Log.error(ioExceptionObj.getMessage());
					System.out.println("Problem Occured While Creating The Directory Structure= " + ioExceptionObj.getMessage());
				}
			}
			return isDir;	

		}
		
		public String getFileNameFromdirectory(String dirPath) throws InterruptedException
		{
			String filename=null;
			File dir = new File(dirPath);
			
			System.out.println("file directory"+dirPath);
			
			Thread.sleep(2000);
			File[] files = dir.listFiles();
			if (files.length == 0) {
				System.out.println("The directory is empty");
			} else {
				for (File aFile : files) {
					System.out.println(aFile.getName() + " - " + aFile.length());

					filename=aFile.getName();
				}
			}
			return filename;

		}
		

		public boolean fileRenameAndMove(String downloadedfileName,String permanentPathFileRename,String fileExtn)
		{
			
			boolean isFileRename=false;
			System.out.println("Enter in to file rename process");

			try {




				//File oldFile = new File(downloadedfileName);
				String newFilepath=permanentPathFileRename+"."+fileExtn;
			//	File newFile = new File(newFilepath);

				System.out.println("downloadedfileName"+downloadedfileName);
				System.out.println("newFilepath"+newFilepath);


				Path temp = Files.move
						(Paths.get(downloadedfileName), 
								Paths.get(newFilepath));

				if(temp != null)
				{
					System.out.println("File renamed and moved successfully");
					isFileRename=true;
				}
				else
				{
					System.out.println("Failed to move the file");
				}
				/*		if (oldFile.renameTo(newFile)) {


				    System.out.println("The file was moved successfully to the new folder");

				} else {

				    System.out.println("The File was not moved.");

				}*/
			} catch (Exception e) {
				Log.error(e.getMessage());
			
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return isFileRename;


		}
	/*public RemoteWebDriver invokeApp(String browser, boolean bRemote,String url,String downloadPath) {
		try {
			
			System.out.println("App is invoked");

			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browser);
			dc.setPlatform(Platform.WINDOWS);

			// this is for grid run
			if(bRemote)
				driver = new RemoteWebDriver(new URL("http://"+sHubUrl+":"+sHubPort+"/wd/hub"), dc);
			else{ // this is for local run
				if(browser.equalsIgnoreCase("chrome")){
						System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
					driver = new ChromeDriver();

					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");


					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory",downloadPath);

					ChromeOptions options = new ChromeOptions();

					HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
					options.setExperimentalOption("prefs", chromePrefs);

					options.addArguments("--test-type");
				    options.addArguments("--disable-extensions"); //to disable browser extension popup

			       // options.addArguments("--headless");

					
				      DesiredCapabilities cap = DesiredCapabilities.chrome();
				       cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
				       cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				       cap.setCapability(ChromeOptions.CAPABILITY, options);
					driver = new ChromeDriver(options);  


				}
				
				
				
				
				
				else{
					 FirefoxBinary firefoxBinary = new FirefoxBinary();
					   firefoxBinary.addCommandLineOptions("--headless");
					   System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
					   FirefoxOptions firefoxOptions = new FirefoxOptions();
					   firefoxOptions.setBinary(firefoxBinary);
					System.out.println("Launching firefox browser.....");
					
					driver = new FirefoxDriver(firefoxOptions);
					System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
					driver = new FirefoxDriver();
				}
			}

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(url);

			primaryWindowHandle = driver.getWindowHandle();		
			reportStep("The browser:" + browser + " launched successfully", "PASS");

		} catch (Exception e) {
			//e.printStackTrace();
			reportStep("The browser:" + browser + " could not be launched", "FAIL");
		}

		return driver;
	}

*/
	public List<String> getUrlAndSytemPath(String urlsqlQuery) throws Exception
	{

		List <String> liData = null;

		try {


			//String sqlQuery2="SELECT URL FROM recon_bank_stmt_setup";
			liData=readStringDataFromDataBase(urlsqlQuery);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  liData;
	}
	public String getDecryptPwd(String psqlQuery) throws Exception
	{
		String dPwd = null;

		try {

			List <String> peData=readStringDataFromDataBase(psqlQuery);

			dPwd=peData.get(0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  dPwd;
	}
	/**
	 * This method will enter the value to the text field using id attribute to locate
	 * 
	 * @param idValue - id of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author Sterling Software
	 * @throws Exception 
	 * @throws IOException 
	 * @throws COSVisitorException 
	 */
	//
	public void enterById(String idValue, String data,String bankName,String accountNo,String amcCode,String startRunTime ) throws Exception {
		try {
			//	driver.findElement(By.id(idValue)).clear();
			driver.findElement(By.id(idValue)).sendKeys(data);	
			reportStep("The data: "+data+" entered successfully in field :"+idValue, "PASS");
		} catch (NoSuchElementException e) {
			Log.error(e.getMessage());
			//verifySessionTerminatedErrorMsg(bankName, accountNo, amcCode, startRunTime);
			logReportBackend(bankName,accountNo,amcCode,startRunTime,"The element with ID: "+idValue+" could not be clicked.", "FAIL");
			reportStep("The data: "+data+" could not be entered in the field :"+idValue, "FAIL");
		}
		catch (WebDriverException e) {
			Log.error(e.getMessage());
			//verifySessionTerminatedErrorMsg(bankName, accountNo, amcCode, startRunTime);
			logReportBackend(bankName,accountNo,amcCode,startRunTime,"The element with ID: "+idValue+" could not be clicked.", "FAIL");
			reportStep("Unknown exception occured while entering "+data+" in the field :"+idValue, "FAIL");
		}
		catch (Exception e) {
			Log.error(e.getMessage());
			logReportBackend(bankName,accountNo,amcCode,startRunTime,"The element with ID: "+idValue+" could not be clicked.", "FAIL");
			reportStep("Unknown exception occured while entering "+data+" in the field :"+idValue, "FAIL");
		}
	}

	/**
	 * This method will enter the value to the text field using name attribute to locate
	 * 
	 * @param nameValue - name of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author Sterling Software
	 * @throws IOException 
	 * @throws COSVisitorException 
	 */

	//Enter the values using Name Locator
	public void enterByName(String nameValue, String data,String bankName,String accountNo,String amcCode,String startRunTime ) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name(nameValue)));  

			driver.findElement(By.name(nameValue)).clear();
			driver.findElement(By.name(nameValue)).sendKeys(data);	
			reportStep("The data: "+data+" entered successfully in field :"+nameValue, "PASS");

		}catch(NullPointerException nexp)
		{

			Log.error("Error during when ID is clicked"+nexp.getMessage());
			logReportBackend(bankName,accountNo,amcCode,startRunTime,"The element with name: "+nameValue+" could not be clicked.", "FAIL");
			reportStep("The data: "+data+" entered successfully in field :"+nameValue, "PASS");
		} catch (NoSuchElementException e) {
			
			//Log.error(e.getMessage());
			String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
			
			//verifySessionTerminatedErrorMsg(bankName, accountNo, amcCode, startRunTime);
			logReportBackend(bankName,accountNo,amcCode,startRunTime,expMsg, "FAIL");
			System.out.println("NoSuchElementException"+bankName);
			System.out.println("Error message"+e.getMessage());
			reportStep("The data: "+data+" could not be entered in the field :"+nameValue, "FAIL");
			
		
		} catch (Exception e) {
			
			//Log.error(e.getMessage());
			String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
		//	verifySessionTerminatedErrorMsg(bankName, accountNo, amcCode, startRunTime);
			logReportBackend(bankName,accountNo,amcCode,startRunTime,expMsg, "FAIL");
		
			System.out.println("Error message1"+e.getMessage());
			reportStep("Unknown exception occured while entering "+data+" in the field :"+nameValue, "FAIL");
			
		}

	}
	public void enterByNameAsPwd(String nameValue, String data) {
		try {
			driver.findElement(By.name(nameValue)).clear();
			driver.findElement(By.name(nameValue)).sendKeys(data);	
			reportStep("The Password XXXXXX data entered successfully in field :"+nameValue, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+nameValue, "FAIL");
		} catch (Exception e) {
			reportStep("Unknown exception occured while entering "+data+" in the field :"+nameValue, "FAIL");
		}

	}

	/**
	 * This method will enter the value to the text field using name attribute to locate
	 * 
	 * @param xpathValue - xpathValue of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author Sterling Software
	 * @throws Exception 
	 * @throws IOException 
	 * @throws COSVisitorException 
	 */
	public void enterByXpath(String xpathValue, String data,String bankName,String amcCode,String startRunTime) throws Exception {
		try {
			driver.findElement(By.xpath(xpathValue)).clear();
			driver.findElement(By.xpath(xpathValue)).sendKeys(data);	
			reportStep("The data: "+data+" entered successfully in field :"+xpathValue, "PASS");

		} catch (NoSuchElementException e) {
			reportStep("The data: "+data+" could not be entered in the field :"+xpathValue, "FAIL");
			logReportBackend(bankName,data,amcCode,startRunTime,"The element with XPath: "+xpathValue+" could not be clicked.", "FAIL");
		} catch (Exception e) {
			logReportBackend(bankName,data,amcCode,startRunTime,"The element with XPath: "+xpathValue+" could not be clicked.", "FAIL");
			reportStep("Unknown exception occured while entering "+data+" in the field :"+xpathValue, "FAIL");
		}

	}

	/**
	 * This method will verify the title of the browser 
	 * @param title - The expected title of the browser
	 * @author Sterling Software
	 */
	public boolean verifyTitle(String title){
		boolean bReturn = false;
		try{
			if (driver.getTitle().contains(title)){
				reportStep("The title of the page matches with the value :"+title, "PASS");
				bReturn = true;
			}else
				reportStep("The title of the page:"+driver.getTitle()+" did not match with the value :"+title, "SUCCESS");

		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
		return bReturn;
	}

	/**
	 * This method will verify the given text matches in the element text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author Sterling Software
	 */
	public void verifyTextByXpath(String xpath, String text){
		try {
			String sText = driver.findElementByXPath(xpath).getText();
			if (sText.equalsIgnoreCase(text)){
				reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not match with the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
	}

	/**
	 * This method will verify the given text is available in the element text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author Sterling Software
	 */
	public boolean verifyTextContainsByXpath(String xpath, String text){
		boolean res=false;
		try{

			String sText = driver.findElementByXPath(xpath).getText();
			if (sText.contains(text)){
				res=true;
				reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			}else{
				res=false;
				reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");

			}
		}catch (Exception e) {

			System.out.println("Execption message"+e.getMessage());


			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}

		return res;
	}

	/**
	 * This method will verify the given text is available in the element text
	 * @param id - The locator of the object in id
	 * @param text  - The text to be verified
	 * @author Sterling Software
	 */
	public void verifyTextById(String id, String text) {
		try{
			String sText = driver.findElementById(id).getText();
			if (sText.equalsIgnoreCase(text)){
				reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not match with the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
	}

	/**
	 * This method will verify the given text is available in the element text
	 * @param id - The locator of the object in id
	 * @param text  - The text to be verified
	 * @author Sterling Software
	 */
	public void verifyTextContainsById(String id, String text) {
		try{
			String sText = driver.findElementById(id).getText();
			if (sText.contains(text)){
				reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			}else{
				reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			}
		}catch (Exception e) {
			reportStep("Unknown exception occured while verifying the title", "FAIL");
		}
	}

	/**
	 * This method will close all the browsers
	 * @author Sterling Software
	 */
	/*public void quitBrowser() {
		try {
			driver.quit();

			System.out.println("All the windows are closed");
		} catch (Exception e) {
			reportStep("The browser:"+driver.getCapabilities().getBrowserName()+" could not be closed.", "FAIL");
		}

	}

	*//**
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author Sterling Software
	 * @throws Exception 
	 */
	public void quitBrowser() {
		try {
			
			
			
			
			driver.quit();
			
			/*driver.close();
			Thread.sleep(4000);
			driver.quit();*/
			
			String bankName = GenericWrappers.bankName;
			
			String	startRunTime=INDUSINDMainPgm.initialStartRunTimeSession;
			
			String endTime=getAppRunningDateTime();
			
			
			
			iterationInsertReconBankStatementLogFromStoredProcedure(bankName,startRunTime,endTime);

			System.out.println("All the windows are closed");
		} catch (Exception e) {
			reportStep("The browser:"
					+ driver.getCapabilities().getBrowserName()
					+ " could not be closed.", "FAIL");
		}

	}
	
	public void iterationInsertReconBankStatementLogFromStoredProcedure(String bankName,
			 String startTime, String endTime) throws Exception {
		// TODO Auto-generated method stub

		try {
			
			Log.info("Inside iteration update procedure");
			Connection con;

			con = Decrypt.GetConnection(amc);
			CallableStatement callSt1 = null;
			Log.error("After Connecting to db: " + con);
			System.out.println("Con:" + con);
			System.out.println("userName:" + Decrypt.AmcCodeSelected);
			if (con != null) {
				// WriteToLog("Connection Created and bank Name NULL");
				Statement stmt = con.createStatement();
				System.out.println("Before Alter schema:"
						+ Decrypt.SchemaSelected);
				stmt.execute("alter session set current_schema= "
						+ Decrypt.SchemaSelected + "");
				System.out.println("after Alter schema:"
						+ Decrypt.SchemaSelected);
				String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_log(?,?,?,?,?,?,?,?,?,?)}";// 3
				callSt1 = con.prepareCall(getDBUSERCursorSql);
				// // callSt.setString("flag", "MANUAL");
				callSt1.setString(1, "ITERATION");
				callSt1.setString(2, bankName);
				callSt1.setString(3, "");
				callSt1.setString(4, "");
				callSt1.setString(5, "");
				callSt1.setString(6, "");
				callSt1.setString(7, startTime);
				callSt1.setString(8, endTime);
				callSt1.setString(9,"");
				callSt1.registerOutParameter(10, OracleTypes.VARCHAR);
				callSt1.executeUpdate();

				/*
				 * if
				 * (!(callSt1.getObject(9).toString().equals("Failure during Log"
				 * ))) {
				 * 
				 * System.out.println("Record is inserted:"+callSt1.getObject(9).
				 * toString());
				 * 
				 * } else {
				 * 
				 * System.out.println("Record is not inserted"); }
				 */
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("Error is throwing while inserting data from store procedure"
					+ e.getMessage());
			e.printStackTrace();
		}

	}
	

	public void runInsertReconBankStatementLogFromStoredProcedure(
			String bankName, String startTime, String endTime) throws Exception {
		// TODO Auto-generated method stub

		try {

			Log.info("Inside run count update procedure");
			Connection con;

			con = Decrypt.GetConnection(amc);
			CallableStatement callSt1 = null;
			// Log.error("After Connecting to db: " + con);
			// System.out.println("Con:" + con);
			System.out.println("userName:" + Decrypt.AmcCodeSelected);
			if (con != null) {
				// WriteToLog("Connection Created and bank Name NULL");
				Statement stmt = con.createStatement();
				/*
				 * System.out.println("Before Alter schema:" +
				 * Decrypt.SchemaSelected);
				 */
				stmt.execute("alter session set current_schema= "
						+ Decrypt.SchemaSelected + "");
				/*
				 * System.out.println("after Alter schema:" +
				 * Decrypt.SchemaSelected);
				 */
				String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_log(?,?,?,?,?,?,?,?,?,?)}";// 3
				callSt1 = con.prepareCall(getDBUSERCursorSql);
				// // callSt.setString("flag", "MANUAL");
				callSt1.setString(1, "RUN");
				callSt1.setString(2, bankName);
				callSt1.setString(3, "");
				callSt1.setString(4, "");
				callSt1.setString(5, "");
				callSt1.setString(6, "");
				callSt1.setString(7, startTime);
				callSt1.setString(8, endTime);
				callSt1.setString(9, "");
				callSt1.registerOutParameter(10, OracleTypes.VARCHAR);
				callSt1.executeUpdate();

				
//				  if
//				  (!(callSt1.getObject(9).toString().equals("Failure during Log"
//				  ))) {
//				 
//				  System.out.println("Record is inserted:"+callSt1.getObject(9).
//				  toString());
//				  
//				  } else {
//				  
//				  System.out.println("Record is not inserted"); }
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("Error is throwing while inserting data from store procedure"
					+ e.getMessage());
			e.printStackTrace();
		}

	}
	
	
	
	public void clickById(String id,String bankName,String accountNo,String amcCode,String startRunTime ) throws Exception {
		try{

			driver.findElement(By.id(id)).click();
			reportStep("The element with id: "+id+" is clicked.", "PASS");

		}catch(NullPointerException nexp)
		{

			Log.error("Error during when ID is clicked"+nexp.getMessage());
			logReportBackend(bankName,accountNo,amcCode,startRunTime,"The element with ID: "+id+" could not be clicked.", "FAIL");
			reportStep("The element with id: "+id+" could not be clicked.", "FAIL");
		}catch (NoSuchElementException e) {
			 String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
			//Log.error(e.getMessage());
		//	verifySessionTerminatedErrorMsg(bankName, accountNo, amcCode, startRunTime);
			logReportBackend(bankName,accountNo,amcCode,startRunTime,expMsg, "FAIL");
			reportStep("The element with id: "+id+" could not be clicked.", "FAIL");
			
		}


		catch (Exception e) {
			 String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
		//	Log.error(e.getMessage());
			//verifySessionTerminatedErrorMsg(bankName, accountNo, amcCode, startRunTime);
			logReportBackend(bankName,accountNo,amcCode,startRunTime,expMsg, "FAIL");
			reportStep("The element with id: "+id+" could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author Sterling Software
	 */
	public void clickByClassName(String classVal) {
		try{
			driver.findElement(By.className(classVal)).click();
			reportStep("The button with class Name: "+classVal+" is clicked.", "PASS");
		}catch (NoSuchElementException e){
			reportStep("The button with class Name: "+classVal+" could not be found.", "FAIL");
		}catch (Exception e) {
			reportStep("The button with class Name: "+classVal+" could not be clicked.", "FAIL");
		}
	}
	/**
	 * This method will click the element using name as locator
	 * @param name  The name (locator) of the element to be clicked
	 * @author Sterling Software
	 */
	public void clickByName(String name) {
		try{
			driver.findElement(By.name(name)).click();
			reportStep("The element with name: "+name+" is clicked.", "PASS");
		} catch (Exception e) {
			reportStep("The element with name: "+name+" could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will click the element using link name as locator
	 * @param name  The link name (locator) of the element to be clicked
	 * @author Sterling Software
	 */
	public void clickByLink(String name,String bankName,String accountNo,String amcCode,String startRunTime ) throws Exception {
		//	driver.findElementByLinkText(name).click();
		try{
			driver.findElementByPartialLinkText(name).click();
			reportStep("The element with link name: "+name+" is clicked.", "PASS");
		} catch(NullPointerException nexp)
		{

			Log.error("Error during when ID is clicked"+nexp.getMessage());
			logReportBackend(bankName,accountNo,amcCode,startRunTime,"The element with ID: "+name+" could not be clicked.", "FAIL");
			reportStep("The element with link name: "+name+" could not be clicked.", "FAIL");
		}
		catch (WebDriverException e) {
		//	Log.error(e.getMessage());
			 String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
			logReportBackend(bankName,accountNo,amcCode,startRunTime,expMsg, "FAIL");
			reportStep("The element with link name: "+name+" could not be clicked.", "FAIL");
		}
	}

	public void clickByLinkNoSnap(String name) {
		try{
			driver.findElement(By.linkText(name)).click();
			//reportStep("The element with link name: "+name+" is clicked.", "PASS");
		} catch (Exception e) {
			reportStep("The element with link name: "+name+" could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will click the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be clicked
	 * @author Sterling Software
	 * @throws Exception 
	 */
	public void clickByXpath(String xpathVal,String bankName,String accountNo,String amcCode,String startRunTime ) throws Exception {
		try{
			driver.findElement(By.xpath(xpathVal)).click();
			reportStep("The element : "+xpathVal+" is clicked.", "PASS");
		} catch(NullPointerException nexp)
		{

			Log.error("Error during when ID is clicked"+nexp.getMessage());
			logReportBackend(bankName,accountNo,amcCode,startRunTime,"The element with ID: "+xpathVal+" could not be clicked.", "FAIL");
			reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
		}
		catch (Exception e) {
			 String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
			
		//	Log.error(e.getMessage());
			//verifySessionTerminatedErrorMsg(bankName, accountNo, amcCode, startRunTime);
			logReportBackend(bankName,accountNo,amcCode,startRunTime,expMsg, "FAIL");

		//	logReportBackend(bankName,accountNo,amcCode,startRunTime,"The element with XPath: "+xpathVal+" could not be clicked.", "FAIL");
			reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
		}
	}

	public void clickByXpathNoSnap(String xpathVal) {
		try{
			driver.findElement(By.xpath(xpathVal)).click();
			System.out.println("1st Lead clicked");
			//reportStep("The element : "+xpathVal+" is clicked.", "PASS");
		} catch (WebDriverException e) {
			reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
		}
	}

	/**
	 * This method will mouse over on the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be moused over
	 * @author Sterling Software
	 */
	public void mouseOverByXpath(String xpathVal) {
		try{
			new Actions(driver).moveToElement(driver.findElement(By.xpath(xpathVal))).build().perform();
			reportStep("The mouse over by xpath : "+xpathVal+" is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over by xpath : "+xpathVal+" could not be performed.", "FAIL");
		}
	}

	/**
	 * This method will mouse over on the element using link name as locator
	 * @param xpathVal  The link name (locator) of the element to be moused over
	 * @author Sterling Software
	 */
	public void mouseOverByLinkText(String linkName) {
		try{
			new Actions(driver).moveToElement(driver.findElement(By.linkText(linkName))).build().perform();
			reportStep("The mouse over by link : "+linkName+" is performed.", "PASS");
		} catch (Exception e) {
			reportStep("The mouse over by link : "+linkName+" could not be performed.", "FAIL");
		}
	}

	/**
	 * This method will return the text of the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element
	 * @author Sterling Software
	 */
	public String getTextByXpath(String xpathVal){
		String bReturn = "";
		try{
			return driver.findElement(By.xpath(xpathVal)).getText();
		} catch (Exception e) {
			reportStep("The element with xpath: "+xpathVal+" could not be found.", "FAIL");
		}
		return bReturn; 
	}

	/**
	 * This method will return the text of the element using id as locator
	 * @param xpathVal  The id (locator) of the element
	 * @author Sterling Software
	 */
	public String getTextById(String idVal) {
		String bReturn = "";
		try{
			return driver.findElementById(idVal).getText();
		} catch (Exception e) {
			reportStep("The element with id: "+idVal+" could not be found.", "FAIL");
		}
		return bReturn; 
	}


	/**
	 * This method will select the drop down value using id as locator
	 * @param id The id (locator) of the drop down element
	 * @param value The value to be selected (visibletext) from the dropdown 
	 * @author Sterling Software
	 */
	public void selectVisibileTextById(String id, String value) {
		try{
			new Select(driver.findElement(By.id(id))).selectByVisibleText(value);;
			reportStep("The element with id: "+id+" is selected with value :"+value, "PASS");
		} catch (Exception e) {
		
			reportStep("The value: "+value+" could not be selected.", "FAIL");
		}
	}
	public void selectVisibileTextByName(String name, String value) {
		try{
			new Select(driver.findElement(By.name(name))).selectByVisibleText(value);;
			reportStep("The element with name: "+name+" is selected with value :"+value, "PASS");
		} catch (Exception e) {
			reportStep("The value: "+value+" could not be selected.", "FAIL");
		}
	}


	public void selectVisibileTextByXPath(String xpath, String value,String bankName,String accountNo,String amcCode,String startRunTime) throws Exception {
		try{
			new Select(driver.findElement(By.xpath(xpath))).selectByVisibleText(value);;
			reportStep("The element with xpath: "+xpath+" is selected with value :"+value, "PASS");
		} catch (Exception e) {
		//	verifySessionTerminatedErrorMsg(bankName, accountNo, amcCode, startRunTime);
			reportStep("The value: "+value+" could not be selected.", "FAIL");
		}
	}

	public void selectIndexById(String id, String value) {
		try{
			new Select(driver.findElement(By.id(id))).selectByIndex(Integer.parseInt(value));;
			reportStep("The element with id: "+id+" is selected with index :"+value, "PASS");
		} catch (Exception e) {
			reportStep("The index: "+value+" could not be selected.", "FAIL");
		}
	}

	public void switchToParentWindow() {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String wHandle : winHandles) {
				driver.switchTo().window(wHandle);
				break;
			}
		} catch (Exception e) {
			reportStep("The window could not be switched to the first window.", "FAIL");
		}
	}

	public void switchToLastWindow() {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String wHandle : winHandles) {
				driver.switchTo().window(wHandle);
			}
		} catch (Exception e) {
			reportStep("The window could not be switched to the last window.", "FAIL");
		}
	}

	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}

	}


	public String getAlertText() {		
		String text = null;
		try {
			driver.switchTo().alert().dismiss();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}
		return text;

	}

	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
		} catch (NoAlertPresentException e) {
			reportStep("The alert could not be found.", "FAIL");
		} catch (Exception e) {
			reportStep("The alert could not be accepted.", "FAIL");
		}

	}

	public long takeSnap(){
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L; 
		try {
			FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE) , new File("./reports/images/"+number+".jpg"));
		} catch (WebDriverException e) {
			reportStep("The browser has been closed.", "FAIL");
		} catch (IOException e)
		{
			reportStep("The snapshot could not be taken", "WARN");
		}
		return number;
	}
	public  void takeSnapShotFailcase(String fileWithPath,String accNo,String bName,String amccode,String sTime,String outputFileName,String browserName) throws Exception{

		try {
			
			System.out.println("Bankname for takeSnapShotFailcase"+bName);
			
			System.out.println("Account number for takeSnapShotFailcase"+accNo);

			String dateAndTime =getAppRunningDateAndTime();       
			String outputFilePath=syspathValidation(fileWithPath);
			System.out.println("outputFilePath"+outputFilePath);  
			String FilePath=outputFilePath+"//NT//"+outputFileName+"_"+"NT"+"_"+dateAndTime+".png"; 

			File noTransFilepath=new File(FilePath);
			FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE),noTransFilepath);                  

			//FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE) , new File(fileWithPath+"\\"+accNo+"_"+bName+"_"+downloadDate+"_"+downloadEndTime+".png"));

			//insertReconBankStatementLog(downloadDate,bName, accNo, "H",sTime, downloadEndDateTime,"FAIL","No transactions");
			
//			logReportBackend(bName,accNo,amccode,sTime,"No transactions", "PASS");
			
			logReportBackend(bankName, accNo, amccode,sTime,"No Transaction - "
					+ browserName, "PASS");

		} catch (WebDriverException e) {
			//Log.error(e.getMessage());
			 String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
			//insertReconBankStatementLog(downloadDate,bName, accNo, "H",sTime, downloadEndDateTime,"FAIL",e.getMessage());
			logReportBackend(bName,accNo,amccode,sTime,expMsg, "FAIL");
			reportStep("The browser has been closed.", "FAIL");
		} catch (IOException e)
		{
			 String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
			//Log.error(e.getMessage());
			reportStep("The snapshot could not be taken", "WARN");
		}
	//	quitBrowser();
	}
	
	//Added by Sandeep for Login Failed - Start
	
	public  void takeSnapShotLoginFail(String fileWithPath,String accNo,String bName,String amccode,String sTime,String outputFileName) throws Exception{

		try {
			
			System.out.println("Bankname for takeSnapShotFailcase"+bName);
			
			System.out.println("Account number for takeSnapShotFailcase"+accNo);

			String dateAndTime =getAppRunningDateAndTime();       
			String outputFilePath=syspathValidation(fileWithPath);
			System.out.println("outputFilePath"+outputFilePath);  
			String FilePath=outputFilePath+outputFileName+"_"+dateAndTime+".png"; 

			File noTransFilepath=new File(FilePath);
			FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE),noTransFilepath);                  


		} catch (WebDriverException e) {
			//Log.error(e.getMessage());
			 String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
			//insertReconBankStatementLog(downloadDate,bName, accNo, "H",sTime, downloadEndDateTime,"FAIL",e.getMessage());
			logReportBackend(bName,accNo,amccode,sTime,expMsg, "FAIL");
			reportStep("The browser has been closed.", "FAIL");
		} catch (IOException e)
		{
			 String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
			//Log.error(e.getMessage());
			reportStep("The snapshot could not be taken", "WARN");
		}
	//	quitBrowser();
	}
	
	
	//Added by Sandeep for Login Failed - End
	
	
	
	
public void robot() throws AWTException, InterruptedException, IOException
{
	
	Robot robot = new Robot();
	Thread.sleep(3000);

	//setting up the location to download the image D:\Jars\(filename)
	robot.keyPress(KeyEvent.VK_D);

	//example of KeyRelease
	//robot.keyPress(KeyEvent.VK_COLON);

	robot.keyPress(KeyEvent.VK_SHIFT);
	robot.keyPress(KeyEvent.VK_SEMICOLON);
	robot.keyRelease(KeyEvent.VK_SHIFT);

	robot.keyPress(KeyEvent.VK_BACK_SLASH);

	// writing the folder name jars
	robot.keyPress(KeyEvent.VK_J);
	robot.keyPress(KeyEvent.VK_A);
	robot.keyPress(KeyEvent.VK_R);
	robot.keyPress(KeyEvent.VK_S);

	robot.keyPress(KeyEvent.VK_BACK_SLASH);

	//Filename
	robot.keyPress(KeyEvent.VK_C);
	robot.keyPress(KeyEvent.VK_E);
	robot.keyPress(KeyEvent.VK_N);
	robot.keyPress(KeyEvent.VK_A);
	File f=new File("D://newtxtfile.txt");
/*	File scrfile = (((TakesScreenshot)driver)).getScreenshotAs(OutputType.FILE);
	FileUtils.copyFile(scrfile, f);*/
	// pressing Enter to save the location
	robot.keyPress(KeyEvent.VK_ENTER);
}

	public  File lastFileModified(String dir) {
		File fl = new File(dir);
		File[] files = fl.listFiles(new FileFilter() {          
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File choice = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choice = file;
				lastMod = file.lastModified();
			}
		}
		return choice;
	}



	public void clickMenu(String mainMenu,String menuOption)  {
		// TODO Auto-generated method stub


		//locate the menu to hover over using its xpath
		try {
			WebElement menu = driver.findElement(By.id(mainMenu));


			Actions builder = new Actions(driver);    


			builder.moveToElement(menu).build().perform();


			WebDriverWait wait = new WebDriverWait(driver, 5); 
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(menuOption)));  


			WebElement menuOp = driver.findElement(By.id(menuOption));

			menuOp.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	public void clickMenu(String mainMenu,String subMenu,String subMenuOption)
	{
		// TODO Auto-generated method stub
		try {
			WebElement menu1 = driver.findElement(By.id(mainMenu));


			Actions builder1 = new Actions(driver);    


			builder1.moveToElement(menu1).build().perform();


			WebElement subMenuele = driver.findElement(By.id(subMenu));


			Actions builder2 = new Actions(driver);    

			builder2.moveToElement(subMenuele).build().perform();


			WebDriverWait wait = new WebDriverWait(driver, 5); 
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(subMenuOption)));  


			WebElement menuOp = driver.findElement(By.id(subMenuOption));

			menuOp.click();


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean verifyTextPresentErrMsg(String text) {
		// TODO Auto-generated method stub
		boolean res=false;
		try{

			String sText = driver.findElement(By.cssSelector("body")).getText();

			if (sText.contains(text)){
				res=true;
				//reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			}
		}catch (Exception e) {
			Log.error(e.getMessage());
			//reportStep("Unknown exception occured while verifying the content", "FAIL");
		}

		return res;	
	}

	public List<String> readStringDataFromDataBase(String sqlQuery) throws Exception {
		// store all the values to list
				List<String> lst = new ArrayList<String>();

				int r = 0;
				int  RowCount;
				try {

					// This will load the MySQL driver, each DB has its own driver
		/*			Class.forName(dbDriver);

					// Setup the connection with the DB
					connect = DriverManager
							.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);

					// Statements allow to issue SQL queries to the database
					statement = connect.createStatement();
					
		*/			Connection con;
					con=Decrypt.GetConnection("ICICI Mutual Fund - UAT1");

					statement = con.createStatement();
					statement.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");


					// Result set get the result of the SQL query
					resultSet = statement
							.executeQuery(sqlQuery);  
					//int i = 1;
					// While Loop to iterate through all data and print results    
					ResultSetMetaData metadata =resultSet.getMetaData();
					int numberOfColumns = metadata.getColumnCount();
					while (resultSet.next()){
						int i = 1;
						while(i <= numberOfColumns) {

							// System.out.println("Column value"+resultSet.getString(i));
							lst.add(resultSet.getString(i++));
							/*for (int j = 0; j <=numberOfColumns-1; j++){
			        	   System.out.println("USERID" + resultSet.getString(j));

			           };*/
						}  
					}
					// closing DB Connection       
					//connect.close();      

				} catch (Exception e) {
					
					 Log.error(e.getMessage());
					// cMethods.justReport("The sql query has not been executed due to exception :", "FAILED");
					e.printStackTrace();
					throw e;
				} finally {
					//   close();
				}

				return lst;

		/*// store all the values to list
		List<String> lst = new ArrayList<String>();

		int r = 0;
		int  RowCount;
		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriver);

			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();


			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sqlQuery);  
			//int i = 1;
			// While Loop to iterate through all data and print results    
			ResultSetMetaData metadata =resultSet.getMetaData();
			int numberOfColumns = metadata.getColumnCount();
			while (resultSet.next()){
				int i = 1;
				while(i <= numberOfColumns) {

					// System.out.println("Column value"+resultSet.getString(i));
					lst.add(resultSet.getString(i++));
					for (int j = 0; j <=numberOfColumns-1; j++){
	        	   System.out.println("USERID" + resultSet.getString(j));

	           };
				}  
			}
			// closing DB Connection       
			connect.close();      

		} catch (Exception e) {
			// cMethods.justReport("The sql query has not been executed due to exception :", "FAILED");
			e.printStackTrace();
			throw e;
		} finally {
			//   close();
		}

		return lst;*/

	}


	public ResultSet readDataFromDataBase(String sqlQuery) throws Exception {
		// store all the values to list


		List<String> titles1 = new ArrayList();  



		ResultSetMetaData metadata;
		int r = 0;
		int  RowCount;
		try {

			// This will load the MySQL driver, each DB has its own driver
			//Class.forName(dbDriver);

			// Setup the connection with the DB
			/*	connect = DriverManager
					.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();*/

			Connection con;
			con=Decrypt.GetConnection(dbuname);

			statement = con.createStatement();
			statement.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sqlQuery);  



		}  


		// closing DB Connection       
		// connect.close();      

		catch (Exception e) {

			Log.error(e.getMessage());
			// cMethods.justReport("The sql query has not been executed due to exception :", "FAILED");
			/*	e.printStackTrace();
			throw e;*/
		} finally {
			//   close();
		}

		return resultSet;



	/*	// store all the values to list


		List<String> titles1 = new ArrayList();  



		ResultSetMetaData metadata;
		int r = 0;
		int  RowCount;
		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriver);

			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();


			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sqlQuery);  
			//int i = 1;
			// While Loop to iterate through all data and print results    
			   metadata =resultSet.getMetaData();
	   int numberOfColumns = metadata.getColumnCount();
	   while (resultSet.next()){

		   userDetails.setLoginId(resultSet.getString("LOGIN_ID"));
	    	  userDetails.setAccountno(resultSet.getString("ACCOUNT_NO"));
	    	 System.out.println("Login id"+userDetails.getLoginId());
	    	  System.out.println("Accountno"+userDetails.getAccountno());
	    	  users.add(userDetails);
	    	//  



	  // }  
	   }
			// closing DB Connection       
			// connect.close();      

		} catch (Exception e) {
			
			 Log.error(e.getMessage());
			// cMethods.justReport("The sql query has not been executed due to exception :", "FAILED");
			e.printStackTrace();
			throw e;
		} finally {
			//   close();
		}

		return resultSet;
*/
	}

	public void closeDB() throws SQLException
	{
		connect.close();


	}
	public String getAppRunningTime() {

		try {
			Calendar currentDate = Calendar.getInstance();
			String TIME_NOW = "HH-mm-ss";
			SimpleDateFormat stf1 = new SimpleDateFormat(TIME_NOW); 
			reportTime = stf1.format(currentDate.getTime());
		} catch (Exception e) {
			 Log.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reportTime;

	}
	public String getAppRunningDateTime() {

		try {
			Calendar currentDate = Calendar.getInstance();
			String TIME_NOW = "MM-dd-yyyy HH-mm-ss";
			SimpleDateFormat stf1 = new SimpleDateFormat(TIME_NOW); 
			reportTime = stf1.format(currentDate.getTime());
		} catch (Exception e) {
			 Log.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reportTime;

	}

	public String getAppRunningDate()
	{

		try {
			String DATE_NOW = "yyyy-MM-dd";

			//String DATE_NOW = "MM/DD/YYYY";
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_NOW); 
			reportDate = sdf.format(currentDate.getTime());



			
		} catch (Exception e) {
			Log.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reportDate;
	}
	public String getCurrentDate()
	{

		String DATE_NOW = "dd/MM/yyyy";

	
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_NOW); 
		cDate = sdf.format(currentDate.getTime());



		return cDate;
	}
	
	
	

	public String getYesterdayDate()
	{
		String yesterdayDate;
		String DATE_NOW =  "dd/MM/yyyy";

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_NOW); 
		/*reportDate = sdf.format(currentDate.getTime());*/
		currentDate.add(Calendar.DATE, -1);
		yesterdayDate= sdf.format(currentDate.getTime());

		System.out.println("yesterdayDate"+yesterdayDate);
		return yesterdayDate;
	}
	
	   public String getLastMonthLastDate() {
	      
		   
		   Calendar calendar = Calendar.getInstance();
	        calendar.add(Calendar.MONTH, -1);

	        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	        //calendar.set(Calendar.DAY_OF_MONTH, max);
	        System.out.println(max);
	        
	        String lastmonthdate=Integer.toString(max);
	        return lastmonthdate;
	    }
	
	public boolean checkCurrentdateGreater3PM() throws ParseException {
		
		boolean result;
		
		
		Calendar currentDate = Calendar.getInstance();
		String TIME_NOW = "HH:mm:ss";
		SimpleDateFormat currentdateFormat = new SimpleDateFormat(TIME_NOW); 
		String currentTime = currentdateFormat.format(currentDate.getTime());
			 
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");

		dateFormat1.parse(currentTime);
//		    if(dateFormat1.parse(currentTime).after(dateFormat.parse("18:59:59"))){   // Identified Bug
		    if(dateFormat1.parse(currentTime).after(dateFormat.parse("14:59:59"))){
		        System.out.println("Current time greater than 3 PM");
		        result=true;
		    }
		    else
		    {
		    	
		    	  System.out.println("Current time greater than 3 PM");
		    	 result=false;
		    }

		
		return result;
	}

/*	public int insertReconBankStatementLog(String downloadDate,
			String bankName, String accountNo, String amcCode,
			String startTime, String endTime, String status, String reason)
					throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement preparedStatement = null;

		int rowCount = 0;

		String insertTableSQL = "INSERT INTO RECON_BANK_STMT_LOG"
				+ "(download_date, bank_name,account_no,amc_code,start_time,end_time,status,reason) VALUES"
				+ "(?,?,?,?,?,?,?,?)";


		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriver);

			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);


			preparedStatement = connect.prepareStatement(insertTableSQL);

			//  String startDate="12-31-2014";
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf1.parse(downloadDate);
			java.sql.Date sqlStartDate = new Date(date.getTime()); 
			System.out.println(sqlStartDate);  

			preparedStatement.setDate(1,sqlStartDate);
			preparedStatement.setString(2,bankName);
			preparedStatement.setString(3,accountNo);
			preparedStatement.setString(4,amcCode);
			preparedStatement.setString(5,startTime);
			preparedStatement.setString(6,endTime);
			preparedStatement.setString(7,status);
			preparedStatement.setString(8,reason);

			// execute insert SQL stetement
			rowCount=preparedStatement.executeUpdate();

			// Commit data here.
			System.out.println("Commiting data here....");
			// connect.commit();

			System.out.println("Record is inserted into DBUSER table!"+rowCount);




		} catch (Exception e) {
			//   cMethods.justReport("The sql query has not been executed due to exception :", "FAILED");
			e.printStackTrace();
			throw e;
		} finally {
			connect.close();
		}
		return rowCount;
	}*/
	public int insertReconBankStatementLog(String downloadDate,
			String bankName, String accountNo, String amcCode,
			String startTime, String endTime, String status, String reason)
					throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement preparedStatement = null;

		int rowCount = 0;

		String insertTableSQL = "INSERT INTO RECON_BANK_STMT_LOG"
				+ "(download_date, bank_name,account_no,amc_code,start_time,end_time,status,reason) VALUES"
				+ "(?,?,?,?,?,?,?,?)";


		try {

			// This will load the MySQL driver, each DB has its own driver
			/*Class.forName(dbDriver);

			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);
			*/
			
			Connection connect;
			connect=Decrypt.GetConnection(dbuname);
			Statement stmt = connect.createStatement();
			stmt.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");

	         System.out.println("SchemaSelected"+Decrypt.SchemaSelected);
	         System.out.println("Afer alter session");
			preparedStatement = connect.prepareStatement(insertTableSQL);
			
		//	statement.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");

			//  String startDate="12-31-2014";
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf1.parse(downloadDate);
			java.sql.Date sqlStartDate = new Date(date.getTime()); 
			System.out.println(sqlStartDate);  

			preparedStatement.setDate(1,sqlStartDate);
			preparedStatement.setString(2,bankName);
			preparedStatement.setString(3,accountNo);
			preparedStatement.setString(4,amcCode);
			preparedStatement.setString(5,startTime);
			preparedStatement.setString(6,endTime);
			preparedStatement.setString(7,status);
			preparedStatement.setString(8,reason);

			// execute insert SQL stetement
			rowCount=preparedStatement.executeUpdate();
			
		

			// Commit data here.
			System.out.println("Commiting data here....");
			// connect.commit();

			System.out.println("Record is inserted into DBUSER table!"+rowCount);




		} catch (Exception e) {
			//   cMethods.justReport("The sql query has not been executed due to exception :", "FAILED");
			
			e.printStackTrace();
			
			System.out.println("Error excep"+e.getMessage());
			throw e;
		} finally {
			//connect.close();
		}
		return rowCount;
	}
	//SARANYA CREATED NEW METHOD TO VERIFY BACKEND -Return rowcount
	public int selectSqlQuery(String sqlQuery) throws Exception {


		// store all the values to list
		List<String> lst = new ArrayList<String>();
		int RowCount = 0;

		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriver);

			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);

			// Statements allow to issue SQL queries to the database

			statement = connect.createStatement();

			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(sqlQuery);  

			//STEP 5: Extract data from result set
			while(resultSet.next()){

				// System.out.println("Query fired.....................");
				RowCount = resultSet.getRow();

				//System.out.println(RowCount);
			}



		} catch (Exception e) {
			//   cMethods.justReport("The sql query has not been executed due to exception :", "FAILED");
			e.printStackTrace();
			throw e;
		} finally {
			// close();
		}
		return RowCount;

	}
/*	public boolean verifySessionTerminatedErrorMsg(String eMsg,String appUrl,String sysPath,String accountNo,String bankName,String loginID,String lpwd) throws Exception  {	
		//enterById(prop.getProperty("LoginPage.Password.ID"),data);
		
		
	
		boolean b=false;
		
	    b=verifyTextPresentErrMsg("Session Terminated. Please login again.");
	    
		System.out.println("verify session error msg"+b);
	    
	    
	    if(b)
	    {
	    	System.out.println("Session msg is present in page");
	    	b=true;
	    	String downloadEndTime= getAppRunningTime1();
	    	String downloadDate=getAppRunningDate();        
	    	Thread.sleep(1000);
	    	insertReconBankStatementLog(downloadDate,"hdfc", "1234","H",downloadEndTime, downloadEndTime,"FAIL","Session Terminated. Please login again msg is present");
	    	quitBrowser();
	    	
	    	//Get start time
			String startRunTime = getAppRunningTime1();
	    	//downloadAccountStmt("firefox", appUrl,loginID,lpwd,sysPath,accountNo,bankName,startRunTime);
	    	
	    }
		
	    
	    return b;
	
		
	}	
	*/





	public void fileRenameUsingRobot(String browserName,String newpath)
	{
		
		
		
		StringSelection file = new StringSelection(newpath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(file, null);

		try {
			

			if(browserName.equalsIgnoreCase("firefox"))
			{
				System.out.println("Robot class for firefox browser");

			Robot rb = new Robot();

			rb.setAutoDelay(2000); // Similar to thread.sleep

            //Click Open as
			
			rb.keyPress(KeyEvent.VK_ENTER);

		    rb.keyRelease(KeyEvent.VK_ENTER);
			//rb.setAutoDelay(2000);
		    //Click document
		    rb.keyPress(KeyEvent.VK_ENTER);

		    rb.keyRelease(KeyEvent.VK_ENTER);
		    
	//	  rb.setAutoDelay(3000);
			
			//click save as
		 //   rb.setAutoDelay(1000);
			
			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_SHIFT);
			rb.keyPress(KeyEvent.VK_S);
			
			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.keyRelease(KeyEvent.VK_SHIFT);
			rb.keyRelease(KeyEvent.VK_S);
		    
			//paste path
			
			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_V);

			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.keyRelease(KeyEvent.VK_V);
			
			//click enter

		//	rb.setAutoDelay(4000);
	      rb.keyPress(KeyEvent.VK_ENTER);

		  rb.keyRelease(KeyEvent.VK_ENTER);
		  
		  
		//Enter Current Format

			
	      rb.keyPress(KeyEvent.VK_ENTER);

		  rb.keyRelease(KeyEvent.VK_ENTER);
		  
		  //file close
		  rb.setAutoDelay(3000);
		  
		  rb.keyPress(KeyEvent.VK_ALT);

			rb.keyPress(KeyEvent.VK_F);
		  
		  rb.keyRelease(KeyEvent.VK_ALT);

			rb.keyRelease(KeyEvent.VK_F);

			rb.keyPress(KeyEvent.VK_X);

			rb.keyRelease(KeyEvent.VK_X);
		
		  
		  
		
	}
			
			else if(browserName.equalsIgnoreCase("chrome"))
			{
				System.out.println("Robot class for chrome browser");
				
				Robot robot = new Robot();
				robot.setAutoDelay(2000);
				System.out.println("entered robot");
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
		
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_V);	
				robot.setAutoDelay(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
							
			
			}
			
			
			else
			{
				
				
				System.out.println("Default browser");
			}
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
			//insertReconBankStatementLog("","", accountNo,"H","", "","FAIL",e.getMessage());

		}
	    }
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
	
	/*public boolean updateSqlQuery(String sql) throws Exception {


		boolean b;
		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriver);

			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);

			//String sql = "UPDATE amc SET amc_code=? WHERE amc_code=?";

			PreparedStatement statement = connect.prepareStatement(sql);

			int rowsUpdated = statement.executeUpdate();
			System.out.println("Row count: "+rowsUpdated);
			//connect .commit();
			if (rowsUpdated > 0) {

				b=true;

			}else{


				System.out.println("The sql query has not been updated." +"FAILED");
				b=false;
			}


		} catch (Exception e) {
			System.out.println("The sql query has not been updated due to exception :"+ "FAILED");
			e.printStackTrace();
			throw e;
		} finally {
			close();
		}
		return b;

	}
	*/
	
	public boolean updateSqlQuery(String sql) throws Exception {


		boolean b;
		try {

		/*	// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriver);

			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:oracle:thin:@"+dbServer+":"+dbPort+":"+dbName, dbUser, dbPwd);*/

			Connection connect;
			connect=Decrypt.GetConnection(dbuname);
			Statement stmt = connect.createStatement();
			stmt.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");

			PreparedStatement statement = connect.prepareStatement(sql);

			int rowsUpdated = statement.executeUpdate();
			System.out.println("Row count: "+rowsUpdated);
			//connect .commit();
			if (rowsUpdated > 0) {

				b=true;

			}else{


				System.out.println("The sql query has not been updated." +"FAILED");
				b=false;
			}


		} catch (Exception e) {
			System.out.println("The sql query has not been updated due to exception :"+ "FAILED");
			e.printStackTrace();
			throw e;
		} finally {
			close();
		}
		return b;

	}
	
	/*public void logReportBackend(String bankName,String accountNo,String amcCode,String startTime,String reason,String status) throws Exception
	{



		try {

		 downloadDate=getAppRunningDate();

			 endTime=getAppRunningDateTime();

			if(status.equals("PASS"))

			{


			//	sql="update recon_bank_stmt_setup r set r.rescheduled='Y' WHERE r.bank_name='" +bankName+ "' and r.account_no='" +accountNo+ "' ";

			//	insertReconBankStatementLog(downloadDate, bankName,accountNo, amcCode,startTime,endTime,status,reason);

				insertReconBankStatementLogFromStoredProcedure(bankName,accountNo, amcCode,startTime,endTime,status,reason);

				if(accountNo!=null)
				{

					//updateSqlQuery(sql);
					 updateScheduleFlagFromStoredProcedure(bankName,accountNo);

					System.out.println("Schedule is Y");


				}

			}

			else
			{
				insertReconBankStatementLogFromStoredProcedure(bankName,accountNo, amcCode,startTime,endTime,status,reason);


			//	insertReconBankStatementLog(downloadDate, bankName,accountNo, amcCode,startTime,endTime,status,reason);

			}

		} catch (Exception e) {
			String expMsg= getExceptionMessageUpTo2000Charcters(e.getMessage());
			 Log.error(expMsg);
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}*/
	public void logReportBackend(String bankName, String accountNo,
			String amcCode, String startTime, String reason, String status)
			throws Exception {

		try {
			
	
			downloadDate = getAppRunningDate();

			endTime = getAppRunningDateTime();
			
			Log.info("ScheduletypeType"+ INDUSINDMainPgm.manualBankName);
			
			String isManual =INDUSINDMainPgm.manualBankName;
			
			String	processType;
			
			
			if (!isManual.equals("")) {
				
				
				processType="MANUAL";
				
				Log.info("schedlerType-insideMANUAL"+processType);
				if (status.equals("PASS"))

				{

					// sql="update recon_bank_stmt_setup r set r.rescheduled='Y' WHERE r.bank_name='"
					// +bankName+ "' and r.account_no='" +accountNo+ "' ";

					// insertReconBankStatementLog(downloadDate, bankName,accountNo,
					// amcCode,startTime,endTime,status,reason);

					insertReconBankStatementLogFromStoredProcedure(bankName,
							accountNo, amcCode, startTime, endTime, status, reason,processType);

					if (accountNo != null) {

						// updateSqlQuery(sql);

						updateScheduleFlagFromStoredProcedure(bankName, accountNo);
						System.out.println("Schedule is Y");

					}

				}

				else {

					insertReconBankStatementLogFromStoredProcedure(bankName,
							accountNo, amcCode, startTime, endTime, status, reason,processType);

				}
				
			}
			
			else
			{
				
				
//				processType="AUTOMATIC";
				
				Properties prop=new Properties();
				prop.load(new FileInputStream(new File("./drivers/resources/config.properties")));
				
				processType=prop.getProperty("processType");
				
				Log.info("schedlerType-insideAUTOMATIC"+processType);
				if (status.equals("PASS"))

				{

					// sql="update recon_bank_stmt_setup r set r.rescheduled='Y' WHERE r.bank_name='"
					// +bankName+ "' and r.account_no='" +accountNo+ "' ";

					// insertReconBankStatementLog(downloadDate, bankName,accountNo,
					// amcCode,startTime,endTime,status,reason);
					
					Log.info("schedlerType-insideAUTOMATIC-insidepass"+processType);

					insertReconBankStatementLogFromStoredProcedure(bankName,
							accountNo, amcCode, startTime, endTime, status, reason,processType);

					if (accountNo != null) {

						// updateSqlQuery(sql);

						updateScheduleFlagFromStoredProcedure(bankName, accountNo);
						System.out.println("Schedule is Y");

					}

				}

				else {

					insertReconBankStatementLogFromStoredProcedure(bankName,
							accountNo, amcCode, startTime, endTime, status, reason,processType);

				}
			}
			
			

			

		} catch (Exception e) {
			// TODO Auto-generated catch block

			Log.info("Log entry is not created");
			e.printStackTrace();
		}
	}
	

	public String getExceptionMessageUpTo2000Charcters(String oldExceptionMessage)
	{  String newExceptionMessage=null; 	
		try {
			System.out.println("oldExceptionMessageLength"+oldExceptionMessage.length());

			
			
			
			  if(oldExceptionMessage.trim().length()>2000)
			    {
			   newExceptionMessage= oldExceptionMessage.substring(0,2000);
			  System.out.println("newExceptionMessage"+newExceptionMessage);
			  
			    }
			    
			    else
			    {
			    	  newExceptionMessage= oldExceptionMessage;
			    	System.out.println("oldExceptionMessage"+oldExceptionMessage);
			    	
			    }
			  
			  
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return newExceptionMessage;
		
		
		
	
	}
	public void fileDelete(String exFile)
	{

		try {
			File file = new File(exFile);

			if(file.delete())
			{
				System.out.println("File deleted successfully");
			}
			else
			{
				System.out.println("Failed to delete the file");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	public String syspathValidation(String sysPath)
	{



		String npath=null;
		try {
			if(sysPath.endsWith("\\"))

			{
				npath=sysPath;	
				//npath=sysPath.substring(0,sysPath.length()-1);
				System.out.println("systempath"+npath);
			}
			else
			{

				npath=sysPath+"\\";
				System.out.println("systempath"+npath);
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block

			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return npath;	

	

	}

	public boolean createFile(String path)
	{

		System.out.println("Enter in to file creation process");
		File file = new File(path);

		boolean blnCreated = false;
		try
		{
			blnCreated = file.createNewFile();
		}
		catch(IOException ioe)
		{

			Log.error(ioe.getMessage());
			System.out.println("Error while creating a new empty file :" + ioe);
		}

		System.out.println("File " + file.getPath() + " created ? : " + blnCreated);




		return blnCreated;



	}

	/*public List<String> readLoginDataFromStoredProcedure(String bankName) throws Exception {


		// store all the values to list
		List<String> lst = new ArrayList<String>();


		try {


			Connection con;

			con=Decrypt.GetConnection(dbuname);


			Statement stmt = con.createStatement();
			System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
			stmt.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");
			System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
			String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_setup(?,?,?,?,?,?)}";// 3
			callSt = con.prepareCall(getDBUSERCursorSql);

			callSt.setString(1, "BANKDETAILS");
			callSt.setString(2, bankName);
			callSt.setString(3, "");
			callSt.setString(4, "");
			callSt.registerOutParameter(5, OracleTypes.VARCHAR);
			callSt.registerOutParameter(6, OracleTypes.CURSOR);
			callSt.executeUpdate();

			if (callSt.getObject(5).toString().equals("SUCCESS")) {
				System.out.println("Status:"+callSt.getObject(5).toString());
				//// get cursor and cast it to ResultSet
				resultSet = (ResultSet) callSt.getObject(6);
				ResultSetMetaData metadata =resultSet.getMetaData();
				int numberOfColumns = metadata.getColumnCount();
				System.out.println("count of columns:"+numberOfColumns);
				while (resultSet .next()){
					int i = 1;
					while(i <= numberOfColumns) {
						System.out.println(i);
						// System.out.println("Column value"+resultSet.getString(i));
						lst.add(resultSet.getString(i++));

					}  
				}





			}
			else
			{


				Log.error("BANKDETAILS-During bank details fetching"+callSt.getObject(5).toString());

			}

		}
		catch (Exception e) {
		    Log.error("Error is throwing while getting data from store procedure"+e.getMessage()); 

			//Log.error(e.getMessage());

			e.printStackTrace();
			throw e;
		} 

		return lst;

	}*/
	
	public List<String> readLoginDataFromStoredProcedure(String bankName,String flag)
			throws Exception {

		// store all the values to list
		List<String> lst = new ArrayList<String>();
		String startTime = INDUSINDMainPgm.initialStartRunTimeSession; 
		try {

			Log.info("Started Login details retrieval ");
			
			
			Log.info("Before connection amc"+amc);
			
			Connection con;

			con = Decrypt.GetConnection(amc);
			Log.info("After connection AmcCodeSelected"+Decrypt.AmcCodeSelected);
			Log.info("After connection AmcCode"+Decrypt.AmcCode);
			Log.info("After connection AmcName"+Decrypt.AmcName);
			Log.info("After connection AmcSelected"+Decrypt.AmcSelected);
		//	Log.info("After connection AmcPassword"+Decrypt.AmcPassword);
		

			Statement stmt = con.createStatement();
			
			
			Log.info("After connection amc"+amc);
			
		
			Log.info("Before Alter SchemaSelected"+Decrypt.SchemaSelected);
			
			System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
			stmt.execute("alter session set current_schema= "
					+ Decrypt.SchemaSelected + "");
			System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
			
			Log.info("After Alter SchemaSelected"+Decrypt.SchemaSelected);
			
			System.out.println("USERNAME"+Decrypt.AmcCodeSelected);
			
			Log.info("After connection AmcCodeSelected"+Decrypt.AmcCodeSelected);
			Log.info("After connection AmcCode"+Decrypt.AmcCode);
			Log.info("After connection AmcName"+Decrypt.AmcName);
			Log.info("After connection AmcSelected"+Decrypt.AmcSelected);
			//Log.info("After connection AmcPassword"+Decrypt.AmcPassword);
		
			String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_setup(?,?,?,?,?,?)}";// 3
			callSt = con.prepareCall(getDBUSERCursorSql);

			callSt.setString(1, flag);
			callSt.setString(2, bankName);
			callSt.setString(3, "");
			callSt.setString(4, "");
			callSt.registerOutParameter(5, OracleTypes.VARCHAR);
			callSt.registerOutParameter(6, OracleTypes.CURSOR);
			callSt.executeUpdate();

			if (callSt.getObject(5).toString().equals("SUCCESS")) {
				System.out.println("Status:" + callSt.getObject(5).toString());
				// // get cursor and cast it to ResultSet
				resultSet = (ResultSet) callSt.getObject(6);
				ResultSetMetaData metadata = resultSet.getMetaData();
				int numberOfColumns = metadata.getColumnCount();
				System.out.println("count of columns:" + numberOfColumns);
				while (resultSet.next()) {
					int i = 1;
					while (i <= numberOfColumns) {
						//System.out.println(i);
						// System.out.println("Column value"+resultSet.getString(i));
						lst.add(resultSet.getString(i++));

					}
				}

				Log.info("Login details retrieved successfully");

			} else {

				Log.error("BANKDETAILS-During bank details fetching"
						+ callSt.getObject(5).toString());
				logReportBackend(bankName,"", Decrypt.AmcCodeSelected, startTime,callSt.getObject(5).toString(),"FAIL");
				
				assertEquals("SUCCESS", callSt.getObject(5).toString());
				quitBrowser();
				System.exit(0);
				
			}
			Log.info("Ended Login details retrieved");
			
			logReportBackend(bankName,"", Decrypt.AmcCodeSelected, startTime,"DB CONNECTION CONNECTED", "CONNECTED"); 

		} catch (Exception e) {
			Log.error("Error is throwing while getting data from store procedure"+e.getMessage()); 
			logReportBackend(bankName,"", Decrypt.AmcCodeSelected, startTime, "DB CONNECTION FAILED", "FAIL");

			e.printStackTrace();
			// throw e;
		}

		return lst;

	}

	public ResultSet readAccNoDataFromStoredProcedureAutomaticProcess(String bankName) throws Exception {




		try {



			Connection con;

			con=Decrypt.GetConnection(dbuname);


			Statement stmt = con.createStatement();
			System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
			stmt.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");
			System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
			String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_setup(?,?,?,?,?,?)}";// 3
			callSt = con.prepareCall(getDBUSERCursorSql);

			callSt.setString(1, "BANKDETAILS");
			callSt.setString(2, bankName);
			callSt.setString(3, "");
			callSt.setString(4, "");
			callSt.registerOutParameter(5, OracleTypes.VARCHAR);
			callSt.registerOutParameter(6, OracleTypes.CURSOR);
			callSt.executeUpdate();

			if (callSt.getObject(5).toString().equals("SUCCESS")) {
				System.out.println("Status:"+callSt.getObject(5).toString());
				//// get cursor and cast it to ResultSet
				resultSet = (ResultSet) callSt.getObject(6);

			}

			else
			{


				Log.error(callSt.getObject(5).toString());

			}
		}  


		catch (Exception e) {
		    Log.error("Error is throwing while getting data from store procedure"+e.getMessage()); 
			//Log.error(e.getMessage());
			Log.error(callSt.getObject(5).toString());

		} 

		return resultSet;

	}
	public ResultSet readAccNoDataFromStoredProcedureManualProcess(String bankName) throws Exception {




		try {



			Connection con;

			con=Decrypt.GetConnection(dbuname);


			Statement stmt = con.createStatement();
			System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
			stmt.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");
			System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
			String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_setup(?,?,?,?,?,?)}";// 3
			callSt = con.prepareCall(getDBUSERCursorSql);

			callSt.setString(1, "RESCHEDULED");
			callSt.setString(2, bankName);
			callSt.setString(3, "");
			callSt.setString(4, "");
			callSt.registerOutParameter(5, OracleTypes.VARCHAR);
			callSt.registerOutParameter(6, OracleTypes.CURSOR);
			callSt.executeUpdate();

			if (callSt.getObject(5).toString().equals("SUCCESS")) {
				System.out.println("Status:"+callSt.getObject(5).toString());
				//// get cursor and cast it to ResultSet
				resultSet = (ResultSet) callSt.getObject(6);

			}

			else
			{


				Log.error(callSt.getObject(5).toString());

			}
		}  


		catch (Exception e) {
		    Log.error("Error is throwing while getting data from store procedure"+e.getMessage()); 
			//Log.error(e.getMessage());
			Log.error(callSt.getObject(5).toString());

		} 

		return resultSet;

	}

	public void updateScheduleFlagFromStoredProcedure(String bankName,String accNo)
					throws Exception {
		// TODO Auto-generated method stub



		try {
			Connection con;

			con=Decrypt.GetConnection(dbuname);
			CallableStatement callSt2 = null;
			Log.error("After Connecting to db: " + con);
			System.out.println("Con:" + con);
			System.out.println("userName:" + Decrypt.AmcCodeSelected);
			if (con != null) {
				// WriteToLog("Connection Created and bank Name NULL");
				Statement stmt = con.createStatement();
				System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
				stmt.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");
				System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
				String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_setup(?,?,?,?,?,?)}";// 3
				callSt2 = con.prepareCall(getDBUSERCursorSql);
				//// callSt.setString("flag", "MANUAL");
				callSt2.setString(1, "UPDATE");
				callSt2.setString(2,  bankName);
				callSt2.setString(3, "Y");
				callSt2.setString(4, accNo);
				callSt2.registerOutParameter(5, OracleTypes.VARCHAR);
				callSt2.registerOutParameter(6, OracleTypes.CURSOR);
				callSt2.executeUpdate();
				
				if (callSt2.getObject(5).toString().equals("UPDATED")) {
					System.out.println("Status:"+callSt2.getObject(5).toString());
					
				}
				else
				{
					
					System.out.println("Not updated");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			  Log.error("Error is throwing while updating data through store procedure"+e.getMessage()); 

			//Log.error(e.getMessage());
			e.printStackTrace();
		}

	}
	public void insertReconBankStatementLogFromStoredProcedure(String bankName,
			String accountNo, String amcCode, String startTime, String endTime,
			String status, String reason,String processType) throws Exception {
		// TODO Auto-generated method stub

		try {
			
			Log.info("Inside insert procedure log"+processType);
			Connection con;

			con = Decrypt.GetConnection(amc);
			CallableStatement callSt1 = null;
			Log.error("After Connecting to db: " + con);
			System.out.println("Con:" + con);
			System.out.println("userName:" + Decrypt.AmcCodeSelected);
			if (con != null) {
				// WriteToLog("Connection Created and bank Name NULL");
				Statement stmt = con.createStatement();
				System.out.println("Before Alter schema:"
						+ Decrypt.SchemaSelected);
				stmt.execute("alter session set current_schema= "
						+ Decrypt.SchemaSelected + "");
				System.out.println("after Alter schema:"
						+ Decrypt.SchemaSelected);
				String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_log(?,?,?,?,?,?,?,?,?,?)}";// 3
				callSt1 = con.prepareCall(getDBUSERCursorSql);
				// // callSt.setString("flag", "MANUAL");
				callSt1.setString(1, "LOG");
				callSt1.setString(2, bankName);
				callSt1.setString(3, accountNo);
				callSt1.setString(4, amcCode);
				callSt1.setString(5, status);
				callSt1.setString(6, reason);
				callSt1.setString(7, startTime);
				callSt1.setString(8, endTime);
				callSt1.setString(9, processType);
				callSt1.registerOutParameter(10, OracleTypes.VARCHAR);
				callSt1.executeUpdate();

				/*
				 * if
				 * (!(callSt1.getObject(9).toString().equals("Failure during Log"
				 * ))) {
				 * 
				 * System.out.println("Record is inserted:"+callSt1.getObject(9).
				 * toString());
				 * 
				 * } else {
				 * 
				 * System.out.println("Record is not inserted"); }
				 */
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("Error is throwing while inserting data from store procedure"
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	/*public void insertReconBankStatementLogFromStoredProcedure(
			String bankName, String accountNo, String amcCode,
			String startTime, String endTime, String status, String reason)
					throws Exception {
		// TODO Auto-generated method stub



		try {
			Connection con;

			con=Decrypt.GetConnection(dbuname);
			CallableStatement callSt1 = null;
			Log.error("After Connecting to db: " + con);
			System.out.println("Con:" + con);
			System.out.println("userName:" + Decrypt.AmcCodeSelected);
			if (con != null) {
				// WriteToLog("Connection Created and bank Name NULL");
				Statement stmt = con.createStatement();
				System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
				stmt.execute("alter session set current_schema= " + Decrypt.SchemaSelected + "");
				System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
				String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_log(?,?,?,?,?,?,?,?,?)}";// 3
				callSt1 = con.prepareCall(getDBUSERCursorSql);
				//// callSt.setString("flag", "MANUAL");
				callSt1.setString(1, "LOG");
				callSt1.setString(2, bankName);
				callSt1.setString(3, accountNo);
				callSt1.setString(4,amcCode);
				callSt1.setString(5, status);
				callSt1.setString(6, reason);
				callSt1.setString(7, startTime);
				callSt1.setString(8, endTime);
				callSt1.registerOutParameter(9, OracleTypes.VARCHAR);
				callSt1.executeUpdate();
				
				if (!(callSt1.getObject(9).toString().equals("Failure during Log"))) {
					
					System.out.println("Record is inserted:"+callSt1.getObject(9).toString());
					
				}
				else
				{
					
					System.out.println("Record is not inserted");
				}
			}
		} catch (Exception e) {
			  Log.error("Error is throwing while inserting data through store procedure"+e.getMessage()); 
				 

			// TODO Auto-generated catch block
		//	Log.error(e.getMessage());
			e.printStackTrace();
		}

	}*/
	
	public String getAppRunningDateAndTime() {

		Calendar currentDate = Calendar.getInstance();
		String TIME_NOW = "ddMMMyyyyHHmmss";
		SimpleDateFormat stf1 = new SimpleDateFormat(TIME_NOW); 
		reportTime = stf1.format(currentDate.getTime());

		return reportTime;

	}
	
	public ResultSet readAccNoDataWhenSessionOutFromStoredProcedure(
			String bankName, String flag, String initialExecutionStartTime)
			throws Exception {
		String startTime = INDUSINDMainPgm.initialStartRunTimeSession;
		try {

			Connection con;

			con = Decrypt.GetConnection(amc);

			Statement stmt = con.createStatement();
			System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
			stmt.execute("alter session set current_schema= "
					+ Decrypt.SchemaSelected + "");
			System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
			String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_setup(?,?,?,?,?,?)}";// 3
			callSt = con.prepareCall(getDBUSERCursorSql);

			callSt.setString(1, flag);
			callSt.setString(2, bankName);
			callSt.setString(3, "");
			callSt.setString(4, initialExecutionStartTime);
			callSt.registerOutParameter(5, OracleTypes.VARCHAR);
			callSt.registerOutParameter(6, OracleTypes.CURSOR);
			callSt.executeUpdate();

			if (callSt.getObject(5).toString().equals("SUCCESS")) {
				System.out.println("Status:" + callSt.getObject(5).toString());
				// // get cursor and cast it to ResultSet
				resultSet = (ResultSet) callSt.getObject(6);

			}

			else {
				logReportBackend(bankName,"", Decrypt.AmcCodeSelected, startTime,callSt.getObject(5).toString(),"FAIL");

				Log.error(callSt.getObject(5).toString());

			}
		}

		catch (Exception e) {

			Log.error("Error is throwing while getting data from store procedure"
					+ e.getMessage());
			Log.error(callSt.getObject(5).toString());

		}

		return resultSet;

	}
	
	public ResultSet readAccNoDataFromStoredProcedure(
			String bankName,String flag) throws Exception {
		String startTime = INDUSINDMainPgm.initialStartRunTimeSession;
		try {

			Connection con;

			con = Decrypt.GetConnection(amc);

			Statement stmt = con.createStatement();
			System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
			stmt.execute("alter session set current_schema= "
					+ Decrypt.SchemaSelected + "");
			System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
			String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_setup(?,?,?,?,?,?)}";// 3
			callSt = con.prepareCall(getDBUSERCursorSql);

			callSt.setString(1, flag);
			callSt.setString(2, bankName);
			callSt.setString(3, "");
			callSt.setString(4, "");
			callSt.registerOutParameter(5, OracleTypes.VARCHAR);
			callSt.registerOutParameter(6, OracleTypes.CURSOR);
			callSt.executeUpdate();

			if (callSt.getObject(5).toString().equals("SUCCESS")) {
				System.out.println("Status:" + callSt.getObject(5).toString());
				// // get cursor and cast it to ResultSet
				resultSet = (ResultSet) callSt.getObject(6);

			}

			else {
				logReportBackend(bankName,"", Decrypt.AmcCodeSelected, startTime,callSt.getObject(5).toString(),"FAIL"); 
				Log.error(callSt.getObject(5).toString());

			}
		}

		catch (Exception e) {

			Log.error("Error is throwing while getting data from store procedure"+e.getMessage()); 
			Log.error(callSt.getObject(5).toString());

		}

		return resultSet;

	}
	
//	Added by Sandeep for selection of From Date through Database -- Start
	
	public ResultSet readFromDateFromStoredProcedure(
			String bankName,String AccountNo,String flag) throws Exception {
		String startTime = INDUSINDMainPgm.initialStartRunTimeSession;
		try {

			Connection con;

			con = Decrypt.GetConnection(amc);

			Statement stmt = con.createStatement();
			System.out.println("Before Alter schema:" + Decrypt.SchemaSelected);
			stmt.execute("alter session set current_schema= "
					+ Decrypt.SchemaSelected + "");
			System.out.println("after Alter schema:" + Decrypt.SchemaSelected);
			String getDBUSERCursorSql = "{call pkg_recon_automation.itn_plp_recon_setup(?,?,?,?,?,?)}";// 3
			callSt = con.prepareCall(getDBUSERCursorSql);

			callSt.setString(1, flag);
			callSt.setString(2, bankName);
			callSt.setString(3, "");
			callSt.setString(4, AccountNo);
			callSt.registerOutParameter(5, OracleTypes.VARCHAR);
			callSt.registerOutParameter(6, OracleTypes.CURSOR);
			callSt.executeUpdate();

			if (callSt.getObject(5).toString().equals("SUCCESS")) {
				System.out.println("Status:" + callSt.getObject(5).toString());
				// // get cursor and cast it to ResultSet
				resultSet = (ResultSet) callSt.getObject(6);

			}

			else {
				logReportBackend(bankName,"", Decrypt.AmcCodeSelected, startTime,callSt.getObject(5).toString(),"FAIL"); 
				Log.error(callSt.getObject(5).toString());

			}
		}

		catch (Exception e) {

			Log.error("Error is throwing while getting data from store procedure"+e.getMessage()); 
			Log.error(callSt.getObject(5).toString());

		}

		return resultSet;

	}

	
	
//	Added by Sandeep for selection of From Date through Database -- End
	
	

}