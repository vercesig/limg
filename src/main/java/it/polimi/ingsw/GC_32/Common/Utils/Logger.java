package it.polimi.ingsw.GC_32.Common.Utils;

import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Logger extends java.util.logging.Logger{

	protected Logger(String name, String resourceBundleName) {
		super(name, resourceBundleName);
		Handler handler = new StreamHandler(new NullOutputStream(), new SimpleFormatter());
		this.addHandler(handler);
	}
}