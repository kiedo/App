package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class FileDom4j {

	private Document document;
	private String filename;

	public FileDom4j(String filename) throws ParserConfigurationException {
		this.filename = filename;
		this.document = getDocument();
	}

	public Document getDocument() throws ParserConfigurationException {
		return loadXml();
	}

	public Document loadXml() throws ParserConfigurationException {
		Document document = null;
		SAXReader saxReader = new SAXReader();
		try {
			document = (Document) saxReader.read(new File(filename));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public void ChangNode(String nodename, String appName) {
		ArrayList list = (ArrayList) document.selectNodes(nodename);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Element e1 = (Element) iter.next();
			e1.setText(appName);
			e1.setData(appName);
		}
		xml2File();

	}

	public void xml2File() {
		try {
			XMLWriter writer = new XMLWriter(new FileOutputStream(filename));
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String xmlToString(File file) throws Exception {
		if (file == null) {
			return "";
		}
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		String documentStr = document.asXML();
		System.out.println("document 字符串" + documentStr);
		return documentStr;
	}
}
