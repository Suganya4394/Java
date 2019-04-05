package pages;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentTest;

import utils.Log;
import wrappers.ReconWrappers;

public class INDUSINDLoginPage extends ReconWrappers {

	public INDUSINDLoginPage(RemoteWebDriver driver, ExtentTest test) {
		this.driver = driver;
		this.test = test;

	}

	public INDUSINDLoginPage enterCorporateID(String CorpID, String bankName,String Accountno,
			 String amcCode, String startRunTime)
			throws Exception {
		Log.info("Started entering Corporate ID" );
		
		driver.findElement(By.id(prop.getProperty("INDUSINDLoginPage.CorporateID.ID"))).sendKeys(CorpID);
		
//		enterByName(prop.getProperty("ICICLoginPage.CorporateID.NAME"), CorpID,
//				bankName, Accountno, amcCode, startRunTime);
		
		Log.info("Corporate ID entered Successfully" );
		
		Log.info("Ended entering Corporate ID" );
		return this;
	}

	// bankName,accountNo,amcCode,startRunTime

	public INDUSINDLoginPage enterUserID(String UserID,String bankName, String Accountno,
			 String amcCode, String startRunTime)
			throws Exception {
		Log.info("Started entering User ID" );
//		enterByName(prop.getProperty("ICICLoginPage.UserID.NAME"), UserID,
//				bankName, Accountno, amcCode, startRunTime);
		
		driver.findElement(By.id(prop.getProperty("INDUSINDLoginPage.UserID.ID"))).sendKeys(UserID);
		
		Log.info("UserName entered Successfully" );
		Log.info("Ended entering User ID" );
		return this;
	}

	public INDUSINDLoginPage enterPassword(String pwd,String bankName, String Accountno,
			 String amcCode, String startRunTime)
			throws Exception {
		Log.info("Started entering Password" );
//		enterByName(prop.getProperty("ICICLoginPage.Password.NAME"), pwd,
//				bankName, Accountno, amcCode, startRunTime);
		
		driver.findElement(By.id(prop.getProperty("INDUSINDLoginPage.Password.ID"))).sendKeys(pwd);
		
		Log.info("Password entered Successfully" );
		
		Log.info("Ended entering Password" );
		return this;
	}
	
	public INDUSINDLoginPage enterCaptcha(String bankName, String Accountno,
			 String amcCode, String startRunTime)
			throws Exception {
		Log.info("Started entering Captcha" );
//		enterByName(prop.getProperty("ICICLoginPage.Password.NAME"), pwd,
//				bankName, Accountno, amcCode, startRunTime);
		
		 WebElement barcodeImage = driver.findElement(By.xpath("//*[@id=\"captcha-i\"]/img"));
	        File imageFile = captureElementPicture(barcodeImage);
		
		
		
		TessBaseAPI instance = new TessBaseAPI();
		 
		 instance.Init("D:\\Recon_Automation\\OCR\\tesseract\\tessdata", "eng");
		 
		 System.out.println("image file path : "+imageFile.getPath());
		 
		 PIX image = lept.pixRead(imageFile.getPath());
		 instance.SetImage(image);
		 BytePointer bytepointer = instance.GetUTF8Text();
		 String output = bytepointer.getString();
		 System.out.println("Text from image :"+ output);
		
		driver.findElement(By.id(prop.getProperty("INDUSINDLoginPage.Captcha.ID"))).sendKeys(output);
		
		Log.info("Captcha entered Successfully" );
		
		Log.info("Ended entering Password" );
		return this;
	}

	private File captureElementPicture(WebElement element) throws IOException {


		 // get the WrapsDriver of the WebElement
        WrapsDriver wrapsDriver = (WrapsDriver) element;
 
        // get the entire screenshot from the driver of passed WebElement
        File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver())
                .getScreenshotAs(OutputType.FILE);
 
        // create an instance of buffered image from captured screenshot
        BufferedImage img = ImageIO.read(screen);
 
        // get the width and height of the WebElement using getSize()
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
 
        // create a rectangle using width and height
        Rectangle rect = new Rectangle(width, height);
 
        // get the location of WebElement in a Point.
        // this will provide X & Y co-ordinates of the WebElement
        Point p = element.getLocation();
 
        // create image  for element using its location and size.
        // this will give image data specific to the WebElement
        BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width,
                rect.height);
 
        // write back the image data for element in File object
        ImageIO.write(dest, "png", screen);
 
        // return the File object containing image data
        return screen;
		
	}

	public INDUSINDHomePage clickLogin(String browserName,String appUrl,String CorporateID,String loginID,String lpwd, String sysPath,String bankName,String Accountno,String startRunTime,String amccode,String isManual,String downloadedStmtFileType,String outputFileName) throws Exception {
		
		boolean b=verifySessionTerminatedForLogin(browserName,appUrl,CorporateID,loginID,lpwd,sysPath,Accountno,bankName,startRunTime,amccode,isManual,downloadedStmtFileType,outputFileName);
		if(!b)
		{
			
		
		if(browserName.equalsIgnoreCase("firefox"))
		{

		Robot robot = new Robot();
		robot.setAutoDelay(3000);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		
	robot.keyPress(KeyEvent.VK_ENTER);
	robot.keyRelease(KeyEvent.VK_ENTER);
		}
		else{
			System.out.println("chrome entered");
			
			Log.info("Started click login" );
			
				clickById(prop.getProperty("INDUSINDLoginPage.Submit.ID"), bankName,
				Accountno, amccode, startRunTime);
			
		}
		
		Log.info("Logged in Successfully" );
		
		Log.info("Ended click login" );
		}

		return null;
//		return new ICICIHomePage(driver, test);

	
	}
}
