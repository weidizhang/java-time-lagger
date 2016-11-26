package io.github.weidizhang.timelagger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.prefs.Preferences;

public class Util {	
	
	public static boolean isWindows() {
		return System.getProperty("os.name").contains("Windows");
	}
	
	// hasElevatedPrivleges source: http://stackoverflow.com/questions/4350356/detect-if-java-application-was-run-as-a-windows-admin
	public static boolean hasElevatedPrivleges() {
		Preferences prefs = Preferences.systemRoot();
		PrintStream systemErr = System.err;
		
		synchronized(systemErr) {
			System.setErr(new PrintStream(
				new OutputStream() {
					@Override public void write(int i) throws IOException {}
				}
			));
			
			try {
				prefs.put("foo", "bar");
				prefs.remove("foo");
				prefs.flush();
				return true;
			}
			catch (Exception e){
				return false;
			}
			finally {
				System.setErr(systemErr);
			}
		}
	}
}
