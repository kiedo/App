package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class IOutils {

	public static void closeWriteIO(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeReadIO(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeAccessFile(RandomAccessFile accessFile) {
		if (accessFile != null) {
			try {
				if (accessFile != null) {
					accessFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
