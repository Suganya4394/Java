package testCases;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.testng.TestNG;

import utils.Log;

public class INDUSINDMainPgm {

	public static String manualBankName = "";
	public static String selectedAmc = null;
	
	public  static String initialStartRunTimeSession=null;


	public static void main(String[] args) throws IOException {
		Log.info("Execution started main");
		initialStartRunTimeSession=getAppRunningDateTime();
		Log.info("Initial starttime during execution"+initialStartRunTimeSession);
		
		try {
			// Log.info("inside main");
			if (args.length > 0) {
				

			
				// System.out.println(Integer.parseInt(args[0]) +
				// Integer.parseInt(args[1]));
				Properties prop = new Properties();
				OutputStream output = null;
				output = new FileOutputStream("D:\\DLL\\config1.properties");
				// int value=Integer.parseInt(args[0]) +
				// Integer.parseInt(args[1]);
				prop.setProperty("Banks", "Inside arguments");
				prop.store(output, null);
				manualBankName = args[0];
				selectedAmc = args[1];
				
				Log.info("manualBankName"+manualBankName);
				Log.error("Amc code Required1" + selectedAmc);
				System.out.println("selectedAmc" + selectedAmc);
				if (selectedAmc.equals(null)) {

					Log.error("Amc code Required" + selectedAmc);
					throw new RuntimeException("FAILED");

				}
				
				// Added by Sandeep for generating different Log files based on AMC Names -- Start
//				try
//				{
//					Log.info("INDUSIND BANK_"+selectedAmc.substring(0, 4).trim()+".log");
//					
//System.setProperty("logFilename", "INDUSINDBANK_"+selectedAmc.substring(0, 4).trim()+".log");
//				
//				org.apache.logging.log4j.core.LoggerContext ctx =
//					    (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
//					ctx.reconfigure();
//				}
//				catch (Exception e) {
//					
//					Log.info("error in creating log file");
//					Log.info(e.getMessage());
//					
//					System.setProperty("logFilename", "INDUSINDBank.log");
//					
//					org.apache.logging.log4j.core.LoggerContext ctx =
//						    (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
//						ctx.reconfigure();
//					
//				}
				// Added by Sandeep for generating different Log files based on AMC Names -- End

				
			}
//			manualBankName="ICICI BANK LTD";
//			selectedAmc = "ICICI Mutual Fund - UAT1";
//	selectedAmc = "HDFC Mutual Fund - UAT1";
		selectedAmc ="QAT - Union KBC Fund";
//			selectedAmc="Union KBC Fund (DEVELOPMENT)";
			
			// Added by Sandeep for generating different Log files based on AMC Names -- Start
//			try
//			{
//System.setProperty("logFilename", "INDUSIND_BANK_"+selectedAmc.substring(0, 4).trim()+".log");
//			
//			org.apache.logging.log4j.core.LoggerContext ctx =
//				    (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
//				ctx.reconfigure();
//			}
//			catch (Exception e) {
//				
//				Log.info("error in creating log file");
//				Log.info(e.getMessage());
//				
//				System.setProperty("logFilename", "ICICIBank.log");
//				
//				org.apache.logging.log4j.core.LoggerContext ctx =
//					    (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
//					ctx.reconfigure();
//				
//			}
			// Added by Sandeep for generating different Log files based on AMC Names -- End

			TestNG testng = new TestNG();
			Class[] classes = new Class[] { TC001_Login.class };
			testng.setTestClasses(classes);
			testng.run();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.info("Error when execution started "+e.getMessage());
		}

	}
	public static String getAppRunningDateTime() {

		Calendar currentDate = Calendar.getInstance();
		String TIME_NOW = "MM-dd-yyyy HH-mm-ss";
		SimpleDateFormat stf1 = new SimpleDateFormat(TIME_NOW);
		String reportTime = stf1.format(currentDate.getTime());

		return reportTime;

	}

}
