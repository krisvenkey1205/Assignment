package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DisplayListPage {
	
	WebDriver driver = null;
	
	By screenSize = By.xpath("//nav[@class='srp-carousel__body']");
	By listPanel = By.id("ResultSetItems");
	By filter = By.xpath("//*[@id='smuys']/span/b/span");
	By rightClick = By.xpath("//button[@class='srp-carousel__next-btn']");
	
	public DisplayListPage(WebDriver driver){
		this.driver = driver;
	}
	
	public WebElement getListPanel(){
		return driver.findElement(listPanel);
	}	
	
	public WebElement getScreenSize(){
		return driver.findElement(screenSize);
	}	
	
	public WebElement getFilter(){
		return driver.findElement(filter);
	}
	
	public WebElement getClick(){
		return driver.findElement(rightClick);
	}
}
