package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SignUtils {

	static StringBuilder builder = new StringBuilder();

	
	public static void sign(String keyPath, String pwd, String apkreleasePath, String unsignapk, String name,
			javax.swing.JTextArea area) {
		builder.setLength(0);
		builder.append("jarsigner -digestalg SHA1 -sigalg MD5withRSA -verbose -keystore ").append(keyPath)//
		.append(" -storepass ").append(pwd)//
		.append(" -signedjar ").append(apkreleasePath)//
		.append(" ").append(unsignapk).append(" ").append(name);//
		String command = builder.toString();
		builder.setLength(0);
		Process p = null;
		BufferedReader br;
		try {
			p = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			area.setText(e.getMessage()+"\n");
			
		}
		br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				area.append(line + "\n");
				area.setCaretPosition(area.getDocument().getLength());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			area.append(e1.getMessage() + "\n");
		}
	}

}
