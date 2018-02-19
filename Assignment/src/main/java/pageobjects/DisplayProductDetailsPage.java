package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DisplayProductDetailsPage {

	WebDriver driver = null;
	private String prodName,prodPrice,sellerName;

	By itemCondition = By.xpath("//*[@id='vi-itm-cond']");
	By price = By.xpath("//*[@id='prcIsum']");
	By timeLeft = By.xpath("//*[@id='vi-cdown_timeLeft']");
	By seller = By.xpath("//*[@id='mbgLink']/span");
	By addToCart = By.linkText("Add to cart");
	By cart = By.xpath("//*[@id='PageTitle']/h1");
	
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdPrice() {
		return prodPrice;
	}

	public void setProdPrice(String prodPrice) {
		this.prodPrice = prodPrice;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	
	public DisplayProductDetailsPage(WebDriver driver){
		this.driver = driver;
	}
	
	public WebElement getItemCondition(){
		return driver.findElement(itemCondition);
	}
	
	public WebElement getPrice(){
		return driver.findElement(price);
	}
	
	public WebElement getTimeLeft(){
		return driver.findElement(timeLeft);
	}
	
	public WebElement getSeller(){
		return driver.findElement(seller);
	}
	
	public WebElement getAddToCart(){
		return driver.findElement(addToCart);
	}
	
	public WebElement getCart(){
		return driver.findElement(cart);
	}
}
