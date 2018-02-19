package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EbayLoginPage {

	WebDriver driver = null;
	
	By username = By.id("userid");
	By password = By.id("pass");
	By staySign = By.id("csi");
	By submit = By.xpath("//*[@id='sgnBt']");
	
	public EbayLoginPage(WebDriver driver){
		this.driver = driver;
	}
	
	public WebElement getUsername(){
		return driver.findElement(username);
	}
	
	public WebElement getPassword(){
		return driver.findElement(password);
	}
	
	public WebElement getStaySign(){
		return driver.findElement(staySign);
	}
	
	public WebElement getSubmit(){
		return driver.findElement(submit);
	}
	
}
