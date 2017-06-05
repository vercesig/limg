package it.polimi.ingsw.GC_32.Common.Utils;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream{
	public void write(int i) throws IOException {}
}