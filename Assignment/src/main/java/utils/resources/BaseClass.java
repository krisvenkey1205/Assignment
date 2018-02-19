package utils.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import junit.framework.Assert;

public class BaseClass {
	
	public static Logger log = LogManager.getLogger(BaseClass.class.getName());
	public static WebDriver driver;
	public static int count = 0;
	public static Properties prop;
	
	//Method to initialize WebDriver 
	public WebDriver initializeDriver() throws IOException{
		
		String browserName = null;
		int implicitWait;
		
		//Load properties file through File Input Stream	
		prop = new Properties();
		log.info("Assign properties file to prop");		
				
		FileInputStream fis = new FileInputStream("C:\\workspace\\Assignment\\src\\main\\java\\utils\\resources\\PropertyFile.properties");
		prop.load(fis);
		
		//Read browser name from properties file
		log.info("Get property value of browser");
		browserName=prop.getProperty("browser");
		log.info("Browser property value:"+browserName);
		
		//Assert if browser name is initial
		log.info("Assert if browser value is maintained");
		Assert.assertNotNull(browserName,"Please maintain browser property in propeties file");
		
		//Create web driver based on browser value
		if(browserName.equals("chrome")){
		//Create chrome driver and set chrome options to disable
		//infobars
			log.info("Creating chrome driver");
			System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			options.addArguments("--incognito");
			driver = new ChromeDriver(options);
			log.info("Chrome Driver created successfully");
		}
		
		else if(browserName.equals("firefox")){
		//Create Firefox webdriver
			log.info("Create Firefox Driver");
			System.setProperty("webdriver.gecko.driver","C:\\geckodriver.exe");
			driver = new FirefoxDriver();
			log.info("Firefox driver created successfully");
		}
		
		else if(browserName.equals("ie")){
		//Create Internet Explorer driver 
			log.info("Create IE Driver");
			System.setProperty("webdriver.ie.driver", "C:\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			log.info("IE Driver created successfully");
		}	
		
		//Maximize the brower window on load
		log.info("Maximizing driver window");
		driver.manage().window().maximize();
		
		//Check if timeout property is maintained in properties file
		//If yes, set the implicit wait time
		log.info("Checking if implicit wait property is available");
		implicitWait = Integer.parseInt(prop.getProperty("timeout"));
		if(implicitWait >0){
			driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		}	
		log.info("Returning driver object");
		return driver;
	}
	
	public boolean checkPriceFormat(String price){
		return price.matches("^\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d{3})?\\d))(\\.\\d{2})?$");
	}
	
	public boolean checkTimeLeft(String timeLeft){
		return timeLeft.matches("\\d{1,2}[dhm] \\d{1,2}[hms]");
	}
	
	public String getScreenshot(String name) throws IOException{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		String str = "C:\\workspace\\Assignment\\Screenshot\\"+name+"_"+dateFormat.format(date)+".png";
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src,new File(str));
		return str;
	}
}
