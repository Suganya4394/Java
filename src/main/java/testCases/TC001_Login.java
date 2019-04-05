package testCases;

import java.io.File;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import General.Decrypt;
import utils.Log;
import wrappers.GenericWrappers;
import wrappers.ReconWrappers;


public class TC001_Login extends ReconWrappers{

	//String bankName = "PUNJAB NATIONAL BANK";
	
		String manualBankName;

		String isManual = "";

		String bankName = null;
		List<String> bList =null;
		
		String amc;
		
		public String sessionFlag = null;
		//String isManual=null;

	@BeforeClass
	public void setData() throws Exception {

		System.out.println("Bank name retrieve"+INDUSINDMainPgm.manualBankName);
		testCaseName =  "TC001_Login";
		testDescription = "Login to ICICILogin With IciciAmc ";
		authors = "Suganya";
		category = "functional";
		//browserName = "chrome";

	}


	@Test
	public void ICICILoginWithIciciAmc() throws Exception {
		
		

		try {

			isManual = INDUSINDMainPgm.manualBankName;
			bankName = GenericWrappers.bankName;

			Log.info("Bank name from config file" + bankName);
			amc = INDUSINDMainPgm.selectedAmc;

			Log.info("Bank Name from config file " + bankName);

			Log.info("Manual bank name Retrieved" + isManual);

			// List<String> bList =
			// readStringDataFromDataBase("SELECT URL,output_file_path,USER_ID,ACCOUNT_NO,decript(password) as PASSWORD,PREF_BROWSER,downloaded_stmt_file_type,output_file_name  FROM recon_bank_stmt_setup where Bank_Name='"
			// +bankName+ "'");
			System.out.println("STARTED");
			// Log.info("ismanual"+isManual.equals(null));
			// Log.info("ismanual"+isManual.equals(""));
			// Log.info("ismanual"+isManual.toString().equals(null));
			// Log.info("ismanual"+isManual.toString().equals(""));
			// Log.info("ismanual"+(isManual.equals(null)&&isManual.equals("")));

			if (!isManual.equals("")) {
				Log.info("INSIDE MANUAL PROCESS");
				 bList = readLoginDataFromStoredProcedure(bankName,
						"RESCHEDULED");

		
				Log.info("Login credentials" + bList.size());

			}

			else {

				Log.info("INSIDE AUTOMATIC PROCESS");
				 bList = readLoginDataFromStoredProcedure(bankName,
						"BANKDETAILS");
				
			}
				
			/* readStringDataFromDataBase("SELECT URL,output_file_path,USER_ID,ACCOUNT_NO,decript(password) as PASSWORD,PREF_BROWSER,downloaded_stmt_file_type,output_file_name  FROM recon_bank_stmt_setup where Bank_Name='"
						 +bankName+ "'");*/

/*
		String appUrl=bList.get(0);

		String sysPath=bList.get(1);
		String CorporateID=bList.get(8);
		String UserID=bList.get(2);

		String Accountno=bList.get(3);

		String appPwd=bList.get(4);
		
		String browserName=bList.get(5);
		
		String downloadedStmtFileType=bList.get(6);
		String outputFileName=bList.get(7);
	//	String amcCode=Decrypt.AmcCode;
		String amcCode=Decrypt.AmcCodeSelected; 

		System.out.println("appUrl"+appUrl);
		System.out.println("sysPath"+sysPath);
		System.out.println("CorporateID"+CorporateID);
		System.out.println("Login id"+UserID);
		System.out.println("Accountno"+Accountno);
		System.out.println("Application password"+appPwd);
		 System.out.println(amcCode);
		 
		 System.out.println("downloadedStmtFileType"+downloadedStmtFileType);  
		 System.out.println("outputFileName"+outputFileName); 

		//Get start time
		String startRunTime = getAppRunningDateTime();
	
		//System.out.println(amcCode);
		//Download account stmt
		downloadAccountStmt(browserName,appUrl,CorporateID,UserID,appPwd,sysPath,Accountno,bankName,startRunTime,amcCode,isManual,downloadedStmtFileType,outputFileName,sessionFlag);
			}
		

		else {

			Log.info("INSIDE AUTOMATIC PROCESS");
			List<String> bList = readLoginDataFromStoredProcedure(bankName,
					"BANKDETAILS");

			// System.out.println("Listsize" + bList.size());*/

			Log.info("Login credentials" + bList.size());
			

			if (bList != null) {
		
		

				String appUrl=bList.get(0);

				String sysPath=bList.get(1);
				String CorporateID=bList.get(8);
				String UserID=bList.get(2);

				String Accountno=bList.get(3);

				String appPwd=bList.get(4);
				
				String browserName=bList.get(5);
				
				String downloadedStmtFileType=bList.get(6);
				String outputFileName=bList.get(7);
			//	String amcCode=Decrypt.AmcCode;
				String amcCode=Decrypt.AmcCodeSelected; 
				
				String textsep=bList.get(13);
				

				/*for(int i=0;i<10;i++){
					//System.out.println("Size"+bList.size());
				    System.out.println(bList.get(i));
				} */
				
				System.out.println("appUrl"+appUrl);
				System.out.println("sysPath"+sysPath);
				System.out.println("CorporateID"+CorporateID);
				System.out.println("Login id"+UserID);
				System.out.println("Accountno"+Accountno);
//				System.out.println("Application password"+appPwd);
				 System.out.println(amcCode);
				 
				 System.out.println("downloadedStmtFileType"+downloadedStmtFileType);  
				 System.out.println("outputFileName"+outputFileName); 
			//	 System.out.println("TextSeperator"+textsep); 
				//Get start time
				String startRunTime = getAppRunningDateTime();
				
				
//				/** To delete the old files */
//
//				delete(Long.parseLong(prop.getProperty("TC001_Login.DaysCount.Todelete")), ".png",prop.getProperty("TC001_Login.Path.Todelete"));
//				/** To delete the old files */
				
				/** To delete the old files */
				
				try
				{

				delete(Long.parseLong(prop.getProperty("TC001_Login.DaysCount.Todelete")), ".png",prop.getProperty("TC001_Login.Path.Todelete"));
				
				
				}
				catch (Exception e) {

	Log.info("Error in deleting image files from reports");
	Log.info(e.getMessage());
				}
				
				/** To delete the old files */
			
				//System.out.println(amcCode);
				//Download account stmt
				
				downloadAccountStmt(browserName,appUrl,CorporateID,UserID,appPwd,sysPath,bankName,Accountno,startRunTime,amcCode,isManual,downloadedStmtFileType,outputFileName,sessionFlag);

			}

		

	} catch (NullPointerException ne) {
		// TODO Auto-generated catch block

		Log.error(ne.getMessage());

	}

	catch (Exception e) {
		// TODO Auto-generated catch block

		Log.error(e.getMessage());
		e.printStackTrace();
	}
	quitBrowser();

}
	
	public void delete(long days, String fileExtension, String Path) {

		File folder = new File(Path);

		if (folder.exists()) {

			File[] listFiles = folder.listFiles();

			long eligibleForDeletion = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000);

			for (File listFile : listFiles) {

				if ((listFile.getName().endsWith(fileExtension) || listFile.getName().endsWith(".jpg"))
						&& listFile.lastModified() < eligibleForDeletion) {

					if (!listFile.delete()) {

						System.out.println("Sorry Unable to Delete Files..");

					}
					else 
					{
						System.out.println("File has been successfully deleted..");
					}
				}
			}
		}
	}

}
	





