package util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author danwu
 * Utilization
 */
public class Util {
	/**
	 * Read file.
	 * @param path
	 * @param encoding
	 * @return String
	 * @throws IOException
	 */
    public static String readFile(String path, Charset encoding) throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
}
