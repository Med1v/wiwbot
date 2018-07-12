package bot.wiw;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Reddit_webdriver {
	
	private WebDriver driver;
	
	public Reddit_webdriver() {
		String path_to_driver = "D:\\Develop\\Java\\chromedriver_win32\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", path_to_driver);
	}
	
	public Reddit_webdriver connect(String subreddit) {
		driver = new ChromeDriver();
		driver.get("https://www.reddit.com/r/" + subreddit + "/");
		System.out.println(driver.getTitle());
		return this;
	}
	
	public Reddit_webdriver printAllTextPosts() {
		try {
			List<WebElement> elems = driver.findElements(By.className("expando-button"));
			System.out.println("am clicking");
			elems.forEach((e) -> {
				String elemClass = e.getAttribute("class").split(" ")[3];
				if (elemClass.compareTo("selftext") == 0) {
					System.out.println("clicking on: " + e.getAttribute("class"));
					e.click();
				}
			});
			
			System.out.println("\nPrinting all text posts on page 1!\n\n");
			elems = driver.findElements(By.className("usertext-body"));
			String ender = "\n\n----------------\n";
			elems.forEach((e) -> {System.out.println(e.getText()+ender);});
		} catch(NoSuchElementException nse) {
			System.out.println("nothing... Element is not found");
		}
		return this;
	}
	
	public void maximize() {
		driver.manage().window().maximize();
	}
	
	public void quit() {
		driver.quit();
	}
	
	protected void finalyze() {
		System.out.println("I'm done");
		driver.quit();		
	}
}
