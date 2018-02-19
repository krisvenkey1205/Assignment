package tests;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;
import pageobjects.*;
import utils.resources.BaseClass;
import utils.resources.DataProviderClass;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TS01_LandingPageTest extends BaseClass{
	
	public static ExtentReports extent;
	public static ExtentTest logger;
	public static Logger log = LogManager.getLogger(BaseClass.class.getName());
	WebDriver driver= null;
	JavascriptExecutor jse;
	
	@BeforeTest
	public void initialize() throws IOException{
		log.info("Calling Initialize driver before test in LandingPageTest class");
		driver = initializeDriver();
		log.info("Driver initialized successfully");	
		
		log.info("Assign Javascript Executor");
		jse = ((JavascriptExecutor) driver);
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		extent = new ExtentReports (System.getProperty("user.dir") +"/ExtentReports/ExtentReport_"+dateFormat.format(date)+".html", true);
		extent
              .addSystemInfo("OS", System.getProperty("os.name"))
              .addSystemInfo("Java Version", System.getProperty("java.version"))
              .addSystemInfo("User Name", System.getProperty("user.name"));
        extent.loadConfig(new File(System.getProperty("user.dir")+"\\src\\main\\java\\utils\\resources\\extent-config.xml"));
	}
	
	@AfterMethod
	public void handleError(ITestResult itr){
		if(itr.getStatus() == ITestResult.FAILURE){
			logger.log(LogStatus.FAIL, "Test Case Failed is "+itr.getName());
			logger.log(LogStatus.FAIL, "Test Case Failed is "+itr.getThrowable()); 			
            String screenshotPath = null;
			try {
				screenshotPath = getScreenshot(itr.getName());
			} catch (IOException e) {}
			
			logger.log(LogStatus.FAIL, logger.addScreenCapture(screenshotPath));
		}
		extent.endTest(logger);
		
	}
	
	@Test(dataProvider="getData")
	public void loadPage(String searchTerm) throws InterruptedException, IOException{
		WebElement we = null;
		int listSize,screenSize;
		String listText, screenText, itemCondition = null;
		String timeLeft, price, prodName;
		Boolean b = false;
		String[] words;
		
		count+=1;
		if(count>1){
			driver.close();
			driver = null;
			driver = initializeDriver();
		}
		logger = extent.startTest("ebay checkout. Search Term:"+searchTerm);
		
		log.info("Create object for landing page class");
		LandingPage landpg = new LandingPage(driver);				//Landing Page object
		
		log.info("Create object for DisplayList Page class");
		DisplayListPage listPage = new DisplayListPage(driver);		//Display List Page object 
		
		log.info("Create object for Display product details page class");
		DisplayProductDetailsPage prodDetails = new DisplayProductDetailsPage(driver); //DisplayProductDetailsPage object
		
		log.info("Create object for CheckOutPage class");
		CheckOutPage checkOut = new CheckOutPage(driver); 			//CheckOutPage object
		
		log.info("Create object for EbayLoginPage class");
		EbayLoginPage login = new EbayLoginPage(driver); 			//CheckOutPage object
		
		log.info("Call URL from properties file");
		driver.get(prop.getProperty("url"));

		log.info("Send search term:"+searchTerm+" to the website");
		
		log.info("Assert if search term is not empty");
		Assert.assertNotNull(searchTerm);
		logger.log(LogStatus.PASS, "Assert: search term is not empty successful");
		
/////////////////////////////////////////////////////////////////////////////////////////		
//1: go to www.ebay.com and search for “sony tv” (this text is should be reading from file)
//2: check that the list of related products are showed, like below screenshot 
///////////////////////////////////////////////////////////////////////////////////////////		
		landpg.getSearchText().sendKeys(searchTerm);
		log.info("Click on search button");
		landpg.getSearchButton().click();
		
/////////////////////////////////////////////////////////////////////////////////////////		
//3. Check that all the listed product has the word “tv” and “sony” in it – read the items 
///////////////////////////////////////////////////////////////////////////////////////////
		log.info("Split the search term");
		words = searchTerm.split("\\s+");
		
		log.info("Get the size of the product list displayed");
		listSize=listPage.getListPanel().findElements(By.tagName("h3")).size();
		
		if(prop.getProperty("checklist").equals("true")){
			log.info("Loop throught the list of product displayed");
			for(int i=1; i<listSize;i++){
				log.info("Get the text of the list displayed");
				listText=listPage.getListPanel().findElements(By.tagName("h3")).get(i).getText();
			
				log.info("Inititlize boolean variable as true");
				b=true;
				log.info("Loop through the search term");
				for(int j=0; j<words.length;j++){
					log.info("Check if the search term is present in list text");
					if(listText.toLowerCase().contains(words[j].toLowerCase()) != true){
						log.info("If search term is not present, set boolean variable as false and exit the loop");
						b=false;
						break;
					}
				}		
				log.info("Assert true/false based on boolean variable");
				Assert.assertTrue("Listed Product:"+listText+",does not contain all the search term:"+searchTerm, b);
			}
		}
		logger.log(LogStatus.PASS, "Listed Products contain the search term");
		
/////////////////////////////////////////////////////////////////////////////////////////		
//4. Select the screen size 50” – 60” (in red square of step 2) 
//5. Check that the new list is showed for screen size 50” – 60”
///////////////////////////////////////////////////////////////////////////////////////////
		log.info("Get the list of screen sizes displayed");
		screenSize = listPage.getScreenSize().findElements(By.tagName("li")).size();
	
		log.info("Loop through the list and find 50\"-60\"");
		b= false;
		for(int i=0;i<screenSize;i++){
			log.info("Assign the list tag to we object");
			we=listPage.getScreenSize().findElements(By.tagName("li")).get(i);
			screenText=we.getText();
			log.info("Compare if screen size text");
			if(screenText.equals(prop.getProperty("screensize"))){
				b = true;	
				log.info("If found, click and break the loop");
				we.click();
				break;
			}
		}
		
		if(b==false){
			listPage.getClick().click();
			Thread.sleep(2000);
			we = (WebElement)(new WebDriverWait(driver,10)).until(ExpectedConditions.elementToBeClickable(listPage.getScreenSize().findElement(By.xpath("//*[text()='"+prop.getProperty("screensize")+"']"))));
			we.click();
		}
		we=null;
		
		log.info("Check if filter criteria applied for selected screen size");
		Assert.assertEquals("Filter criteria is not applied for screen size", prop.getProperty("screensize"), listPage.getFilter().getText());
		logger.log(LogStatus.PASS, "Screen Size filter selected");

/////////////////////////////////////////////////////////////////////////////////////////		
//6: Randomly select a product in the current listed page, check that the product detail page is showed 
///////////////////////////////////////////////////////////////////////////////////////////		
		log.info("Get a random number to click in the displayed list");
		Random ran = new Random();
		int random = ran.nextInt(listSize)+1;
		log.info("Randomly selected listnumber:"+random);
	
		log.info("Assign the random list to webelement");
		we=listPage.getListPanel().findElement(By.xpath("//ul[@id='ListViewInner']/li["+random+"]/h3/a")); 
		//we= listPage.getListPanel().findElement(By.linkText("Sony XBR55X700D 55\" 4K Ultra HD 2160p 60Hz LED Smart HDTV"));
		prodName = we.getText();
		while(true){
			log.info("Check if the web element is diplayed. If not scroll down the page");
			if(we.isDisplayed()==true){
				log.info("If element found, click and break the loop");
				we.click();
				break;
			}else{
				log.info("Continue until web element is displayed in page");
				jse.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			}
		}	
		
/////////////////////////////////////////////////////////////////////////////////////////		
//7: Verify below information:
//	-	Item condition: is not empty
//	-	Time left: either in following format: xxd yyh (example 10d 08h), xxh yym (02h 35m), or xxm yys (30m 45s)
//	-	Price in correct format: $X,XXX.YY
//	-	Create an object to store: product name, price, seller name to compare with other screens
//	-	Click on “Add to cart” button. If any popup for additional purchase showed, just close it
///////////////////////////////////////////////////////////////////////////////////////////		
		log.info("Get Item condition text");
		itemCondition = prodDetails.getItemCondition().getText();
	
		log.info("Assert if item condition text is not empty");
		Assert.assertNotNull("Item condition of the product is empty", itemCondition);
		logger.log(LogStatus.PASS, "Item condition of product is not empty");
		log.info("Item condition text is not empty");
		
		try
	    {	
			log.info("Check if Time Left field is displayed");
			if(prodDetails.getTimeLeft().isDisplayed()==true){
				log.info("Get time Left vallue");
				timeLeft=prodDetails.getTimeLeft().getText();
				
				log.info("Assert if time left matches format");
				Assert.assertTrue("Time Left:"+timeLeft+" does not match format xxd yyh or xxh yym or xxm yys",checkTimeLeft(timeLeft));
				logger.log(LogStatus.PASS, "Time Left matches format");
				log.info("Time left matches format");
			}
	    }
	    catch (NoSuchElementException e)
	    {}
		
		log.info("Get price value of the product");
		price = prodDetails.getPrice().getText();
		
		log.info("Get price value without currency key");
		int index = price.indexOf("$");
		price = price.substring(index);
		
		log.info("Assert if price matches format");
		Assert.assertTrue("Price:"+price+" does not match format $X,XXX.YY",checkPriceFormat(price));
		logger.log(LogStatus.PASS, "Price matches format $X,XXX.YY");
		log.info("Price format check successful");
		
		log.info("Assign product name, price and seller information to objects");
		prodDetails.setProdName(prodName);	 							//Set Product name
		prodDetails.setProdPrice(price);								//Set Product prie
		prodDetails.setSellerName(prodDetails.getSeller().getText());	//Set Seller Name
		
		log.info("Click Add to cart button");
		try{
			prodDetails.getAddToCart().click();	
		}catch (NoSuchElementException e){
			logger.log(LogStatus.FAIL, "Add to cart button does not exists in page");
			Assert.assertTrue("Add to cart button does not exists in page",false);
		}
		
		try
	    {	
			log.info("Switch to frame window if a pop up appears");
			driver.switchTo().frame(driver.findElement(By.xpath("//*[@name='google_osd_static_frame']")));
			driver.findElement(By.xpath("//*[@id='atcRedesignId_overlay-atc-container']/div/div[1]/div/div[2]/a[2]/span/span")).click();
			driver.switchTo().defaultContent();
			log.info("Exiting from frame window");
	    }
	    catch (NoSuchElementException e){}
		
/////////////////////////////////////////////////////////////////////////////////////////		
//Step 8: Verify that that the shopping card is showed
//Step 9: verify the information in Shopping Cart
//	-	The product name is the same as the value you stored from last screen
//	-	The price is same as the value you stored from last screen
//	-	The seller name is same as the value you stored from last screen
//	-	In Cart Summary: verify that the total price is equal to the single unit price
//	-	Click on “Proceed to checkout” button
///////////////////////////////////////////////////////////////////////////////////////////		
		log.info("Assert is shopping cart page is displayed");
		Assert.assertEquals("Shopping cart page is not displayed", "Your eBay Shopping Cart", prodDetails.getCart().getText());
		logger.log(LogStatus.PASS, "Shopping Cart page is displayed");
		
		log.info("Assert if product name is same in check out page");
		//Assert product name in chekout page
		Assert.assertEquals("Product name is not same in check out page", prodDetails.getProdName(), checkOut.getProdname().getText());
		logger.log(LogStatus.PASS, "Product name is same in shopping cart page");
		
		log.info("Assert if price is same in check out page");
		//Assert Price in check out page
		try{
			Assert.assertEquals("Price is not same in check out page", prodDetails.getProdPrice(), checkOut.getPrice().getText());
			logger.log(LogStatus.PASS, "Price is same in shopping cart page");
		}
		catch (NoSuchElementException e)
	    {
			Assert.assertEquals("Price is not same in check out page", prodDetails.getProdPrice(), checkOut.getPrice1().getText());
			logger.log(LogStatus.PASS, "Price is same in shopping cart page");
	    }
		
		log.info("Assert Seller name is same in check out page");
		//Assert price in check out page
		Assert.assertEquals("Seller name is not same in check out page", prodDetails.getSellerName(), checkOut.getSellerName().getText());
		logger.log(LogStatus.PASS, "Seller name is same in shopping cart page");
		
		log.info("Assert if total price in check outp page is equal to single unit price");
		//Assert total price in check out page
		Assert.assertEquals("Total price is not equal to single unit price", prodDetails.getProdPrice(), checkOut.getTotalPrice().getText());
		logger.log(LogStatus.PASS, "Total price in shopping cart page is equal to single unit price");
		
		log.info("Click proceed to checkout button");
		checkOut.getProceedButton().click();
		
/////////////////////////////////////////////////////////////////////////////////////////		
//Step 10: Proceed checkout as guest
///////////////////////////////////////////////////////////////////////////////////////////		
		log.info("Sign in to review cart");
		login.getUsername().sendKeys(prop.getProperty("username"));
		login.getPassword().sendKeys(prop.getProperty("password"));
		login.getStaySign().click();
		login.getSubmit().click();
	
/////////////////////////////////////////////////////////////////////////////////////////		
//11: check that the guest checkout screen is showed
///////////////////////////////////////////////////////////////////////////////////////////
		if(checkOut.getProdNameCheckout().isDisplayed() == false){
			jse.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		}
		
		Assert.assertEquals("Product name is not same in checkout screen", prodDetails.getProdName() , checkOut.getProdNameCheckout().getText());
		logger.log(LogStatus.PASS, "Product name is same in checkout screen");
		Assert.assertEquals("Order total is not equal to single unit in checkout page",prodDetails.getProdPrice() , checkOut.getOrderTotal().getText());
		logger.log(LogStatus.PASS, "Order total is equal to single unit price in checkout page");
		logger.log(LogStatus.PASS,"Test Case passed");
	}
	
	@DataProvider
	public String[] getData() throws IOException{
		String[] result;
		log.info("Start @DataProvider");
		DataProviderClass dpc = new DataProviderClass();
		log.info("Call GetData method in DPC class to read excel file");
		result= dpc.getData();
		log.info("No. of entries in input file:"+result.length);
		log.info("Returning result");
		return result;
	}
	
	
	@AfterTest
	public void closeBrowser() throws InterruptedException{
		extent.flush();
		Thread.sleep(3000);
		extent.close();
		if(driver != null)
			driver.quit();
		driver = null;
	}
}	