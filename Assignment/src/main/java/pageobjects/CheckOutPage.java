package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckOutPage {

	WebDriver driver = null;
	
	By prodName = By.xpath("//div[@class='mr10']/div[1]/span/a");
	By price = By.xpath("//div[@class='fw-b']");
	By price1 = By.xpath("//div[@id='secondipity-itemGroup1-item1']/div/div/div/div[1]/div[2]/div[1]/span/div[1]");	
	By sellerName = By.xpath("//a[@class='mbg-id']");
	By totalPrice = By.xpath("//div[@id='syncTotal']/span[2]");
	By proceedButtton = By.linkText("Proceed to checkout");
	By prodNameCheckout = By.xpath("//div[@class='d-table']/div[2]/div[1]/div[1]");
	By orderTotal = By.xpath("//*[@id='cart-total']/table/tbody/tr[2]/td[2]/span");
	
	public CheckOutPage(WebDriver driver){
		this.driver = driver;
	}
	
	public WebElement getProdname(){
		return driver.findElement(prodName);
	}
	
	public WebElement getPrice(){
		return driver.findElement(price);
	}
	
	public WebElement getSellerName(){
		return driver.findElement(sellerName);
	}
	
	public WebElement getTotalPrice(){
		return driver.findElement(totalPrice);
	}	
	
	public WebElement getProceedButton(){
		return driver.findElement(proceedButtton);
	}
	
	public WebElement getProdNameCheckout(){
		return driver.findElement(prodNameCheckout);
	}
	
	public WebElement getOrderTotal(){
		return driver.findElement(orderTotal);
	}
	
	public WebElement getPrice1(){
		return driver.findElement(price1);
	}
}
