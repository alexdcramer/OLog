package olog;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import net.oijon.olog.info.Info;
import net.oijon.olog.Log;

public class UnitTests {

	File file = new File(System.getProperty("user.home") + "/.olog");
	Log log = new Log(System.getProperty("user.home") + "/.olog");

	@Test
	void testLog() {
		File logFile = new File(log.getLogFile());
		for (Object key : System.getProperties().keySet()) {
			log.info(key.toString() + ":" + System.getProperty(key.toString()));
		}
		log.info("This is a test of " + Info.getVersion());
		log.info("This will create a folder under the home directory, .olog. These will be deleted after the test.");
		log.info("Your home directory is " + System.getProperty("user.home"));
		log.info("=====BEGIN LOGGER TEST=====");
		log.info("Log file at " + log.getLogFile().toString());
		log.setDebug(true);
		log.debug("This is a test of a debug message. The debug marker has been set to true.");
		log.setDebug(false);
		log.debug("This is a test of a debug message. The debug marker has been set to false.");
		log.info("This is a test of an info message.");
		log.warn("This is a test of a warning message.");
		log.err("This is a test of an error message.");
		log.critical("This is a test of a critical error message.");
		log.info("======END LOGGER TEST======");
		
		try {
			Scanner sc = new Scanner(logFile);
			ArrayList<String> lines = new ArrayList<String>();
			while(sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();
			
			assertTrue(lines.size() > 0);
			
			for (int i = 0; i < lines.size(); i++) {
				assertNotEquals("This is a test of a debug message. The debug marker has been set to false.", lines.get(i));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(); // failing here as this means the getLogFile() method has failed.
		}
		log.info("Test successful. Deleting .olog directory...");
		deleteDirectory(file);
	}
	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
}
