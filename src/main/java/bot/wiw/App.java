package bot.wiw;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;

public class App {
    public static void main( String[] args ) {
    	if (args.length < 2) {
    		System.out.println("not enough params. should have 1: <systype> <hours to run>");
    		return;
    	}
    	String systype = args[0];
    	int hoursToRun = Integer.parseInt(args[1]);
    	String driverpath = "";
    	if (systype.equals("home")) {
    		driverpath = "geckodriver\\geckodriver.exe";
    	} else if (systype.equals("aws")) {
    		driverpath = "geckodriver/geckodriver";
    	} else {
    		System.out.println("systype param is wrong. <home|aws>");
    		return;
    	}
    	
    	WiwBot bot = new WiwBot(driverpath);
    	
    	//testing driver
    	if (args.length > 2) {
    		System.out.println("TESTING");
	    	bot.saveScreenshot("archive", "testimg");
	    	bot.quit();
	    	return;
    	}
    	
    	bot.loginToAlign("yfedorovskyi@gmail.com", "Fox_52218_");
    	
    	
    	LocalDateTime stopTime = LocalDateTime.now().plusHours(hoursToRun);
    	System.out.println("Bot will stop at: " + stopTime);
    	
    	while(LocalDateTime.now().compareTo(stopTime) < 0) {
    		
    		bot.refresh();
    		bot.waitSec(3);
	    	System.out.println(LocalDateTime.now());
//        	bot.saveHTML("archive\\", LocalDateTime.now().toString().replaceAll(":", "."));
    		try {
    			bot.takeShift();
    		} catch (NoSuchElementException e) {
    			System.out.println("Cannot take shift... :(");
    			String datetimern = LocalDateTime.now().toString().replaceAll(":", ".");
    			bot.saveHTML("archive", datetimern);
    			bot.saveScreenshot("archive", datetimern);
    		}
        	
	    	try {
				TimeUnit.SECONDS.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	bot.quit();
    }
    
}
