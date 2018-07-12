package bot.wiw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.google.common.io.Files;

public class WiwBot {
	
	public WebDriver driver;
	
	public WiwBot(String path_to_driver) {
		//Chrome
//		System.setProperty("webdriver.chrome.driver", path_to_driver);
//		driver = new ChromeDriver();
		
		//Headless firefox
		FirefoxBinary firefoxBinary = new FirefoxBinary();
	    firefoxBinary.addCommandLineOptions("--headless");
		System.setProperty("webdriver.gecko.driver", path_to_driver);
		FirefoxOptions firefoxOptions = new FirefoxOptions();
	    firefoxOptions.setBinary(firefoxBinary);
	    firefoxOptions.merge(firefoxOptions);
		driver = new FirefoxDriver(firefoxOptions);
		driver.get("https://login.wheniwork.com/");
	}
	
	public void loginToAlign(String email, String password) {
		//init
		String loginInputXPath = "//form[@class=\"login-form\"]";
		List<WebElement> elems = driver.findElements(By.xpath(loginInputXPath + "//input"));
		WebElement emailEl = elems.get(0);
		WebElement passEl = elems.get(1);
		WebElement submit = driver.findElement(By.xpath(loginInputXPath + "//button"));
		
		//enter
		emailEl.sendKeys(email);
		passEl.sendKeys(password);
		submit.click();
		// TODO make a flexible wait
		waitSec(3);
		//go to align accaunt
		String alignAcc = "//div[@class=\"all-accounts\"]//strong[contains(text(),\"Align Communications\")]";
		driver.findElement(By.xpath(alignAcc)).click();		
	}
	
	public void takeShift() {
		String takeBut = "//a[contains(text(), \"Take\")]";
//		String takeBut = "//*[@class=\"open-item\"]//a[contains(text(), \"Take\")]";
		try {
			List<WebElement> takeButs = driver.findElements(By.xpath(takeBut));
			if (takeButs.size() < 1) {
				System.out.println("No shifts buttons found (shifts not available)");
				saveScreenshot("archive", "laststate");
				return;
			}
			
			takeButs.get(0).click();
			System.out.println("SHIFT IS TAKEN!");
			saveScreenshot("archive", "success");
			
		} catch (NoSuchElementException e) {
			System.out.println("No shifts available");
			return;
		}
		
		waitSec(1);
		
		String confirmBut = "//a[contains(text(), \"Take\")]";
		List<WebElement> takeButs = driver.findElements(By.xpath(confirmBut));
		takeButs.get(takeButs.size() - 1).click();
	}
	
	public void saveHTML(String folder, String name) {
		String html = driver.getPageSource();
		
		File file = new File(folder, name + ".html");
		
		try {
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(html);
			fileWriter.flush();
			fileWriter.close();
			System.out.println("succesfully wrote file: " + name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void saveScreenshot(String folder, String name) {
		waitSec(1);
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			Files.copy(scrFile, new File(folder, name + ".png"));
		} catch (IOException e) {
			System.out.println("couldn't save the screenshot");
			return;
		}
		System.out.println("screenshot \"" + name + "\" was saved to " + folder);
	}
	
	public void refresh() {
		driver.navigate().refresh();
	}
	
	public void waitSec(int seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}
	
	public void quit() {
		driver.quit();
		System.out.println("I'm done");
	}
	
	protected void finalyze() {
		System.out.println("I'm done");
		driver.quit();		
	}
}
