package net.oijon.olog;

import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Properties;
//import java.util.Scanner;
import com.diogonunes.jcolor.AnsiFormat;
import static com.diogonunes.jcolor.Attribute.*;

//last edit: 10/22/2023 -N3



/**
 * Simple log utility to help with getting console output to file
 * @author N3ther
 *
 */
public class Log {
	
	private boolean debug = true;
	private File file;
	private String today;
	private String now;	
	
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static AnsiFormat fDebug = new AnsiFormat(WHITE_TEXT());
	private static AnsiFormat fInfo = new AnsiFormat(CYAN_TEXT());
	private static AnsiFormat fWarn = new AnsiFormat(BLACK_TEXT(), YELLOW_BACK());
	private static AnsiFormat fError = new AnsiFormat(WHITE_TEXT(), RED_BACK());
	private static AnsiFormat fCritical = new AnsiFormat(BOLD(), RED_TEXT(), YELLOW_BACK());
	
	public Log(File logdir) {
		this(logdir.getAbsolutePath());
	}
	
	/**
	 * Creates the log object. This should only be used in the main class, unless you want multiple log files.
	 * Please note: This will create a directory under the directory specified called "/logs/".
	 * @param logdir The directory, in string format, of the log.
	 */
	public Log(String logdir) {
		File logFolder = new File(logdir + "/logs/");
		logFolder.mkdirs();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.today = LocalDate.now().format(formatter);
		File logFile = new File(logdir + "/logs/" + this.today + ".log");
		try {
			logFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int i = 0;
		while (logFile.exists()) {
			i++;
			logFile = new File(logdir + "/logs/" + this.today + "(" + i + ")" + ".log");
		}
		if (i > 0) {
			i--;
			if (i == 0) {
				logFile = new File(logdir + "/logs/" + this.today + ".log");
			} else {
				logFile = new File(logdir + "/logs/" + this.today + "(" + i + ")" + ".log");
			}
		}
		this.file = logFile;
	}

	/**
	 * Sets the definition of "now", as in what is the time the print was sent out
	 */
	private void setNow() {
		LocalDateTime now = LocalDateTime.now();
		this.now = dtf.format(now);
	}
	
	/**
	 * Logs console input to the log
	 * @param input The raw input
	 */
	public void input(String input) {
		write(">" + input);
	}
	
	/**
	 * Prints a debug line
	 * @param input What is to be printed
	 */
	public void debug(String input) {
		if (debug) {
			log("DEBUG", input, fDebug);
		}
	}
	
	/**
	 * Prints an info line
	 * @param input What is to be printed
	 */
	public void info(String input) {
		log("INFO", input, fInfo);
	}
	
	/**
	 * Prints a warning
	 * @param input What is to be printed
	 */
	public void warn(String input) {
		log("WARN", input, fWarn);
	}
	
	/**
	 * Prints an error.
	 * @param input What is to be printed
	 */
	public void err(String input) {
		log("ERROR", input, fError);
	    
	}
	/**
	 * Prints a critical error.
	 * @param input What is to be printed
	 */
	public void critical(String input) {
		log("CRITICAL", input, fCritical);
	}
	
	/**
	 * Prints system information to console and writes to file
	 */
	public void logSystemInfo() {
		Properties properties = System.getProperties();
		this.debug("=====================");
		this.debug("List of system properties:");
		Enumeration<Object> keyNames = properties.keys();
		while(keyNames.hasMoreElements()) {
			String key = keyNames.nextElement().toString();
			String value = properties.getProperty(key).toString();
			this.debug(key + " - " + value);
		}
		this.debug("=====================");
	}
	
	public void setDebug(boolean bool) {
		debug = bool;
	}
	
	/**
	 * Prints out to a file and to console given a prefix and color
	 * @param prefix The prefix of the line to print, for example [INFO]
	 * @param input The message to print
	 * @param color The color to print in the console
	 */
	private void log(String prefix, String input, AnsiFormat color) {
		setNow();
		String output = String.format("%-10s", "[" + prefix + "]") + 
				" [" + this.now + "] - " + input;
		System.out.println(color.format(output));
		write(output);
	}
	
	private void write(String input) {
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(input);
			bw.newLine();
		    bw.close();
		} catch (IOException e) {
			// if this catch is being hit, something has gone horribly wrong
			this.err(e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the current file a log is writing to.
	 * @return The path to the current file
	 */
	public String getLogFile() {
		return file.toString();
	}
}
