package pageobjects;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LandingPage {

	WebDriver driver=null;
	
	By searchText = By.xpath("//*[@id='gh-ac']");
	By searchButton = By.xpath("//*[@id='gh-btn']");
	
	public LandingPage(WebDriver driver){
		this.driver = driver;
	}
	
	public WebElement getSearchText(){
		return driver.findElement(searchText);
	}
	
	public WebElement getSearchButton(){
		return driver.findElement(searchButton);
	}
	
}
