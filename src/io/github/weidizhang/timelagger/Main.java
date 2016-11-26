package io.github.weidizhang.timelagger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	private static Options options = new Options();
	private static Logger logger;
	
	
	private static int delay = 15;
	private static String path = "";
	private static String date = "";
	
	private static boolean helpShown = false;
	private static Calendar calendar = Calendar.getInstance();
	
	public static void main(String[] args) {
		boolean isAdmin = Util.hasElevatedPrivleges();
		logger = Logger.getLogger(Main.class.getName()); // workaround to initialize after previous admin check call to fix output issue
		
		if (!isAdmin) {			
			logger.severe("Must be run as administrator or root");
			return;
		}
		
		addOptions();
		
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			handleArgs(cmd);
			
			if (!helpShown) {
				if (path.equals("") || date.equals("")) {
					logger.severe("Required arguments not supplied");
				}
				else {
					timeLag();
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static void timeLag() {
		logger.info("Date changed");
		Date.setDate(date);
		
		try {
			logger.info("Launched " + path);
			Runtime.getRuntime().exec(path);
			
			logger.info("Sleeping for " + delay + " seconds");
			Thread.sleep(delay * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Fixing date");
		Date.correctDate(calendar, delay);
		logger.info("Date fixed");
	}

	private static void handleArgs(CommandLine cmd) {
		if (cmd.hasOption("h")) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp("timelagger", null, options, "Created by Weidi Zhang");
			
			helpShown = true;
		}
		
		if (cmd.hasOption("d")) {
			try {
				delay = Integer.parseInt(cmd.getOptionValue("d"));
			}
			catch (NumberFormatException e) {
				logger.warning("Using default delay, supplied delay not valid int");
			}
		}
		
		if (cmd.hasOption("p")) {
			path = cmd.getOptionValue("p");
		}
		
		if (cmd.hasOption("t")) {
			date = cmd.getOptionValue("t");
		}		
	}
	
	private static void addOptions() {
		options.addOption("h", "help", false, "Shows help information");
		options.addOption("d", "delay", true, "Delay in seconds before reverting date to normal, default: 15");
		options.addOption("p", "path", true, "Path to executable file to run");
		options.addOption("t", "time", true, "Temporary date to set before running: Use YYYYmmdd on linux, varies on windows (detected: " + Date.getDateFormat() + ")");
	}
}
