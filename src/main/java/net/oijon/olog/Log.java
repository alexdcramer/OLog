package net.oijon.olog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Properties;

//last edit: 8/3/2026 -N3



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
	private static String RESET = "\u001B[0m";
	private static String F_DEBUG = "\u001B[97m";
	private static String F_INFO = "\u001B[96m";
	private static String F_WARN = "\u001B[30m\u001B[103m";
	private static String F_ERROR = "\u001B[97m\u001B[101m";
	private static String F_CRITICAL = "\u001B[1m\u001B[91m\u001B[103m";
	
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
			log("DEBUG", input, F_DEBUG);
		}
	}
	
	/**
	 * Prints an info line
	 * @param input What is to be printed
	 */
	public void info(String input) {
		log("INFO", input, F_INFO);
	}
	
	/**
	 * Prints a warning
	 * @param input What is to be printed
	 */
	public void warn(String input) {
		log("WARN", input, F_WARN);
	}
	
	/**
	 * Prints an error.
	 * @param input What is to be printed
	 */
	public void err(String input) {
		log("ERROR", input, F_ERROR);
	    
	}
	/**
	 * Prints a critical error.
	 * @param input What is to be printed
	 */
	public void critical(String input) {
		log("CRITICAL", input, F_CRITICAL);
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
	
	/**
	 * Checks if a log is printing debug messages
	 * @return If a log is printing debug messages
	 */
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * Sets if debug messages should be printed
	 * @param debug true to print debug messages, false to not print them
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * Prints out to a file and to console given a prefix and color
	 * @param prefix The prefix of the line to print, for example [INFO]
	 * @param input The message to print
	 * @param color The ANSI escape codes to print in the console
	 */
	private void log(String prefix, String input, String color) {
		LocalDateTime now = LocalDateTime.now();
		this.now = dtf.format(now);
		String output = String.format("%-10s", "[" + prefix + "]") + 
				" [" + this.now + "] - " + input;
		System.out.println(color + output + RESET);
		write(output);
	}
	
	/**
	 * Writes a line to the log file
	 * @param input The text to write to the file
	 */
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
