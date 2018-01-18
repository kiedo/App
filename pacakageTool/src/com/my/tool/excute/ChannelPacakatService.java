package com.my.tool.excute;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.swing.JTextArea;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import utils.FileDom4j;
import utils.FileUtils;
import utils.IOutils;

public class ChannelPacakatService {

	JTextArea area;
	private String apkName = "";
	private String strApkBaseName = "app_release_";
	static final int BUFFER = 8192;

	public boolean startPacket(JTextArea area) {
		this.area = area;
		return readExcel(Config.EXCELPATH);
	}

	private boolean isEmpty(CharSequence str) {
		if ((str == null) || (str.length() == 0)) {
			return true;
		}
		return false;
	}

	private boolean readExcel(String strExcelPath) {
		try {
			InputStream stream = new FileInputStream(strExcelPath);
			POIFSFileSystem fs = new POIFSFileSystem(stream);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			stream.close();
			HSSFRow row = null;
			int size = sheet.getLastRowNum();
			for (int i = 1; i <= size; i++) {
				row = sheet.getRow(i);
				row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				String c1 = row.getCell(1).getStringCellValue();
				if (c1 == null || c1.length() == 0) {
					continue;
				}
				if (packeting(c1)) {
					this.area.append(" 打包成功 " + this.apkName + "\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.area.append("  操作出错 " + e.getMessage() + "\r\n");
			return false;
		}
		return true;
	}

	private synchronized void writeApk(File file, String comment) {
		ByteArrayOutputStream outputStream = null;
		RandomAccessFile accessFile = null;
		try {
			byte[] byteComment = comment.getBytes();
			outputStream = new ByteArrayOutputStream();
			outputStream.write(byteComment);
			outputStream.write(short2Stream((short) byteComment.length));
			byte[] data = outputStream.toByteArray();
			accessFile = new RandomAccessFile(file, "rw");
			accessFile.seek(file.length() - 2);
			accessFile.write(short2Stream((short) data.length));
			accessFile.write(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOutils.closeWriteIO(outputStream);
			IOutils.closeAccessFile(accessFile);
		}
	}

	private boolean packeting(String subChannel) {
		FileDom4j fileDom4j = null;
		this.area.append("subChannel---->" + subChannel + "\r\n");
		if ((isEmpty(subChannel))) {
			this.area.append("Error: 输入参数有误!  isEmpty(strChannel)  \r\n");
			return false;
		}
		apkName = strApkBaseName + subChannel;
		try {
			String chennelApkPath = Config.CHANNELPACKAGE + this.apkName + ".apk";
			FileUtils.copyFile(Config.RELEASEPACKAGE, chennelApkPath);
			File file = new File(chennelApkPath);
			if (!file.exists()) {
				this.area.append("channelinfo.xml not exists   \r\n");
				return false;
			}
			fileDom4j = new FileDom4j(Config.APPCHANNELINFO);
			fileDom4j.ChangNode("/appchannel/sub_channel", subChannel);
			File xmlConfigFile = new File(Config.APPCHANNELINFO);
			String xmlConfigString = FileDom4j.xmlToString(xmlConfigFile);
			if (xmlConfigString == null || xmlConfigString.length() == 0) {
				this.area.append(" xmlConfigString == null \r\n ");
				return false;
			}
			this.area.append(xmlConfigString + "\r\n");
			writeApk(file, xmlConfigString);
		} catch (Exception e) {
			e.printStackTrace();
			this.area.append(" 失败   \r\n");
			return false;
		}
		return true;
	}

	private static byte[] short2Stream(short data) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(data);
		buffer.flip();
		return buffer.array();
	}

}
