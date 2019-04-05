package wrappers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;


import org.openqa.selenium.remote.RemoteWebDriver;



public interface Wrappers {
	
	List<String> getUrlAndSytemPath(String urlsqlQuery) throws Exception;
		
	public List<String> readStringDataFromDataBase(String sqlQuery) throws Exception;
	public ResultSet readDataFromDataBase(String sqlQuery) throws Exception;
	
	public int selectSqlQuery(String sqlQuery) throws Exception;
	
	public int insertReconBankStatementLog(String downloadDate,String bankName,String accountNo,String amcCode,String startTime,String endTime,String status,String reason) throws Exception;
	
	/**
		 * This method will launch the given browser and maximize the browser and set the
		 * wait for 30 seconds and load the url
		 * @author Sterling Software
		 * @param browser - The browser of the application to be launched
		 * @param url - The url with http or https
		 * @return 
		 * 
		 */
	public RemoteWebDriver invokeApp(String browser,String url,String bankName,String amcCode,String startRunTime,String accountNo,String sysPath)throws Exception;
		
		
		
		public String getDecryptPwd(String psqlQuery) throws Exception;

		/**
		 * This method will enter the value to the text field using id attribute to locate
		 * 
		 * @param idValue - id of the webelement
		 * @param data - The data to be sent to the webelement
		 * @author Sterling Software
		 * @throws Exception 
		 * 
		 */
		public void enterById(String idValue, String data,String bankName,String accountNo,String amcCode,String startRunTime) throws Exception;
		
		/**
		 * This method will enter the value to the text field using name attribute to locate
		 * 
		 * @param nameValue - name of the webelement
		 * @param data - The data to be sent to the webelement
		 * @author Sterling Software
		 */
		public void enterByName(String nameValue, String data,String bankName,String accountNo,String amcCode,String startRunTime) throws Exception;		
		
		/**
		 * This method will enter the value to the text field using xpath attribute to locate
		 * 
		 * @param xpathValue - name of the webelement
		 * @param data - The data to be sent to the webelement
		 * @author Sterling Software
		 * @throws Exception 
		 */
		public void enterByXpath(String xpathValue, String data, String bankName,String amcCode,String startRunTime) throws Exception;
  
		public void clickMenu(String mainMenu,String menuOption);
		 
		public void clickMenu(String mainMenu,String subMenu,String subMenuOption);

		/**
		 * This method will verify the title of the browser 
		 * @param title - The expected title of the browser
		 * @author Sterling Software
		 * @return 
		 */
		public boolean verifyTitle(String title);
		
		/**
		 * This method will verify the given text
		 * @param id - The locator of the object in id
		 * @param text  - The text to be verified
		 * @author Sterling Software
		 */
		public void verifyTextById(String id, String text);
		
		/**
		 * This method will verify the given text
		 * @param xpath - The locator of the object in xpath
		 * @param text  - The text to be verified
		 * @author Sterling Software
		 */
		public void verifyTextByXpath(String xpath, String text);
		
		/**
		 * This method will verify the given text
		 * @param xpath - The locator of the object in xpath
		 * @param text  - The text to be verified
		 * @author Sterling Software
		 */
		public boolean verifyTextContainsByXpath(String xpath, String text);

		/**
		 * This method will verify the given text
		 * @param xpath - The locator of the object in xpath
		 * @param text  - The text to be verified
		 * @author Sterling Software
		 */
		public void verifyTextContainsById(String id, String text);

		public boolean verifyTextPresentErrMsg(String strText);

		/**
		 * This method will click the element using id as locator
		 * @param id  The id (locator) of the element to be clicked
		 * @author Sterling Software
		 * @throws Exception 
		 */
		public void clickById(String id,String bankName,String accountNo,String amcCode,String startRunTime) throws Exception;

		/**
		 * This method will click the element using id as locator
		 * @param id  The id (locator) of the element to be clicked
		 * @author Sterling Software
		 */
		public void clickByClassName(String classVal);
		
		/**
		 * This method will click the element using name as locator
		 * @param name  The name (locator) of the element to be clicked
		 * @author Sterling Software
		 */
		public void clickByName(String name);
		

		/**
		 * This method will click the element using link name as locator
		 * @param name  The link name (locator) of the element to be clicked
		 * @author Sterling Software
		 * @throws Exception 
		 */
		public void clickByLink(String name,String bankName,String accountNo,String amcCode,String startRunTime) throws Exception;

		/**
		 * This method will click the element using xpath as locator
		 * @param xpathVal  The xpath (locator) of the element to be clicked
		 * @author Sterling Software
		 */
		public void clickByXpath(String xpathVal,String bankName,String accountNo,String amcCode,String startRunTime) throws Exception;
		
		/**
		 * This method will get the text of the element using id as locator
		 * @param xpathVal  The id (locator) of the element 
		 * @author Sterling Software
		 */
		public String getTextById(String idVal);

		/**
		 * This method will get the text of the element using xpath as locator
		 * @param xpathVal  The xpath (locator) of the element 
		 * @author Sterling Software
		 */
		public String getTextByXpath(String xpathVal);

		/**
		 * This method will select the drop down visible text using id as locator
		 * @param id The id (locator) of the drop down element
		 * @param value The value to be selected (visibletext) from the dropdown 
		 * @author Sterling Software
		 */
		public void selectVisibileTextById(String id, String value);
		
		/**
		 * This method will select the drop down using index as id locator
		 * @param id The id (locator) of the drop down element
		 * @param value The value to be selected (visibletext) from the dropdown 
		 * @author Sterling Software
		 * 
		 * 
		 */
		
		public void selectVisibileTextByName(String name, String value);
		public void selectIndexById(String id, String value);
		
		/**
		 * This method will switch to the parent Window
		 * @author Sterling Software
		 */
		public void switchToParentWindow();
		
		/**
		 * This method will move the control to the last window
		 * @author Sterling Software
		 */
		public void switchToLastWindow();
		
		/**
		 * This method will accept the alert opened
		 * @author Sterling Software
		 */
		public void acceptAlert();
		
			/**
		 * This method will close all the browsers
		 * @author Sterling Software
		 */
		public void quitBrowser();
		
		public String getAppRunningTime();
		
		public String getAppRunningDate();

}
