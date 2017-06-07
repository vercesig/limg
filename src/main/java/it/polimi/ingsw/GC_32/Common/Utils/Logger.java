package it.polimi.ingsw.GC_32.Common.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Logger extends java.util.logging.Logger{

	protected Logger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
		Handler handler = new StreamHandler(new OutputStream(){
			public void write(int i) throws IOException {}
		}, new SimpleFormatter());
		this.addHandler(handler);
		this.setUseParentHandlers(false);
	}
	
	public static Logger getLogger(String name){
		return new Logger(name, null);
	}
}