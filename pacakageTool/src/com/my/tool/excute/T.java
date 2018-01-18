package com.my.tool.excute;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class T {

	public static void main(String[] args) {
		 String content = readApk(new File("/Users/apple/Desktop/apps/channel/app_release_baidu.apk"));
		 System.out.println(content);
	}

	public static String readApk(File file) {
		byte[] bytes = null;
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(file, "r");
			long index = accessFile.length();
			bytes = new byte[2];
			index = index - bytes.length;
			accessFile.seek(index);
			accessFile.readFully(bytes);
			int contentLength = stream2Short(bytes, 0);
			bytes = new byte[contentLength];
			index = index - bytes.length;
			accessFile.seek(index);
			accessFile.readFully(bytes);
			return new String(bytes, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (Exception e) {
					accessFile = null;
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private static short stream2Short(byte[] stream, int offset) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(stream[offset]);
		buffer.put(stream[offset + 1]);
		return buffer.getShort(0);
	}
}
