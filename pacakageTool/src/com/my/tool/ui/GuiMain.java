package com.my.tool.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.my.tool.excute.ChannelPacakatService;
import com.my.tool.excute.Config;

import utils.SignUtils;

public class GuiMain {
	int width = 550;
	int w;
	int hight = 500;
	int h;
	JFrame jframe;
	String savepathstr = "";
	String baseName = "";
	JPanel mcontent;
	JTextField keypath;
	JTextField pwd;
	JTextField name;
	JTextField apkpath;
	JTextField savepath;
	JTextField allSavePath;
	JTextField exellFilepath;
	JTextField xmlpath;
	JTextField sourseaplpath;
	JTextArea area;
	public static JScrollPane jScrollPane;
	volatile boolean isstart = false;

	private void initJfram() {
		this.jframe = new JFrame();
		this.jframe.setLayout(null);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		this.w = ((int) toolkit.getScreenSize().getWidth());
		this.h = ((int) toolkit.getScreenSize().getHeight());
		this.jframe.setSize(this.width, this.hight);
		this.jframe.setLocation((this.w - this.width) / 2, (this.h - this.hight) / 2);
		this.jframe.setDefaultCloseOperation(3);
		this.jframe.setTitle("批量打包apk");
		this.jframe.setResizable(false);
		this.mcontent = new JPanel();
		this.mcontent.setLayout(null);
		this.mcontent.setBorder(BorderFactory.createLoweredSoftBevelBorder());
	}

	public void showWind() {
		initJfram();

		this.keypath = new JTextField(10);
		this.keypath.setEnabled(false);
		this.keypath.setBounds(20, 5, 180, 30);
		this.mcontent.add(this.keypath);
		JButton jButton = new JButton("选择签名key文件");
		jButton.setBounds(200, 5, 150, 30);
		this.mcontent.add(jButton);

		JButton deletekeyStore = new JButton("删除签名信息");
		deletekeyStore.setBounds(350, 30, 150, 30);

		deletekeyStore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("删除签名信息");
			}
		});
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiMain.this.keypath.setText(GuiMain.this.showDialog(null));
			}
		});
		selectApp();
		signName();
		save();
		passw();
		addText();
		saveMore();
		moreStart();
		addExcell();
		selectXml();
		sourseApk();
		show();
	}

	private void sourseApk() {
		this.sourseaplpath = new JTextField();
		this.sourseaplpath.setEnabled(false);
		this.sourseaplpath.setBounds(20, 228, 180, 30);
		JButton app = new JButton("选择签名后apk");
		app.setBounds(200, 228, 140, 30);
		this.mcontent.add(this.sourseaplpath);
		this.mcontent.add(app);
		app.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiMain.this.sourseaplpath.setText(GuiMain.this.showDialog(".apk"));
			}
		});
	}

	private void selectXml() {
		this.xmlpath = new JTextField();
		this.xmlpath.setEnabled(false);
		this.xmlpath.setBounds(20, 198, 180, 30);
		JButton app = new JButton("选择渠道模版xml");
		app.setBounds(200, 198, 140, 30);
		this.mcontent.add(this.xmlpath);
		this.mcontent.add(app);
		app.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiMain.this.xmlpath.setText(GuiMain.this.showDialog(".xml"));
			}
		});
	}

	private void addExcell() {
		this.exellFilepath = new JTextField();
		this.exellFilepath.setEnabled(false);
		this.exellFilepath.setBounds(20, 170, 180, 30);
		JButton app = new JButton("选择excel批量信息文件");
		app.setBounds(200, 170, 180, 30);

		app.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiMain.this.exellFilepath.setText(GuiMain.this.showDialog(".xls"));
			}
		});
		this.mcontent.add(this.exellFilepath);
		this.mcontent.add(app);
	}

	private void excute() {
		String allapk = this.allSavePath.getText().toString();
		if (allapk.length() == 0) {
			DialogUtil.show(this.mcontent, "选择保存apks文件夹");
			return;
		}
		String excelPath = this.exellFilepath.getText().toString();
		if (excelPath.length() == 0) {
			DialogUtil.show(this.mcontent, "选择excel批量信息文件");
			return;
		}
		String xmlstr = this.xmlpath.getText().toString();
		if (xmlstr.length() == 0) {
			DialogUtil.show(this.mcontent, "选择xml渠道模版");
			return;
		}
		if ((this.sourseaplpath.getText().toString().length() == 0)
				&& (this.savepath.getText().toString().length() == 0)) {
			DialogUtil.show(this.mcontent, "选择签名后apk");
			return;
		}
		Config.BASEPATH = allapk;
		Config.RELEASEPACKAGE = this.sourseaplpath.getText().toString();
		Config.APPCHANNELINFO = xmlstr;
		Config.EXCELPATH = excelPath;
		if (this.baseName.length() == 0) {
			String filename = new File(this.apkpath.getText().toString()).getName();
			if ((filename.length() > 0) && (filename.contains("."))) {
				String[] names = filename.split("\\.");
				if (names.length > 0) {
					this.baseName = names[0];
				}
			}
		}
		Config.APKNAME = this.baseName;
		Config.CHANNELPACKAGE = Config.BASEPATH + "/channel/";
		File file = new File(Config.CHANNELPACKAGE);
		if (!file.exists()) {
			file.mkdirs();
		}
		this.area.append("打包开始\r\n");
		new ChannelPacakatService().startPacket(this.area);
		this.area.append("打包结束\r\n");
	}

	private void saveMore() {
		this.allSavePath = new JTextField(15);
		this.allSavePath.setEditable(false);

		this.allSavePath.setBounds(20, 142, 180, 30);
		JButton app = new JButton("保存apks文件夹");
		app.setBounds(200, 142, 140, 30);
		app.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(1);
				chooser.showOpenDialog(GuiMain.this.mcontent);
				if (chooser.getSelectedFile() != null && chooser.getSelectedFile().getAbsolutePath() != null) {
					String temp = chooser.getSelectedFile().getAbsolutePath();
					temp = temp + "/apps";
					GuiMain.this.allSavePath.setText(temp);
				}

			}
		});
		this.mcontent.add(this.allSavePath);
		this.mcontent.add(app);
	}

	private void addText() {
		this.area = new JTextArea();
		this.area.setEnabled(false);
		this.area.setLineWrap(true);
		this.area.setWrapStyleWord(true);
		jScrollPane = new JScrollPane(this.area);
		jScrollPane.setHorizontalScrollBarPolicy(32);
		jScrollPane.setVerticalScrollBarPolicy(22);
		jScrollPane.setBounds(20, 260, 500, 220);
		this.mcontent.add(jScrollPane);
	}

	private void signName() {
		JLabel comp = new JLabel("输入签名别名");
		comp.setBounds(20, 112, 200, 25);
		comp.setHorizontalAlignment(0);
		this.name = new JTextField();
		this.name.setBounds(200, 112, 140, 30);
		this.mcontent.add(comp);
		this.mcontent.add(this.name);
	}

	private void passw() {
		JLabel comppwd = new JLabel("输入签名密码");
		comppwd.setBounds(20, 90, 200, 25);
		comppwd.setHorizontalAlignment(0);
		this.pwd = new JTextField();

		this.pwd.setBounds(200, 85, 140, 30);
		this.mcontent.add(comppwd);
		this.mcontent.add(this.pwd);
	}

	private void save() {
		this.savepath = new JTextField();
		this.savepath.setBounds(20, 60, 180, 30);
		this.savepath.setEditable(false);
		JButton saveBt = new JButton("签名后apk文件夹");
		saveBt.setBounds(200, 60, 140, 30);
		this.mcontent.add(this.savepath);
		this.mcontent.add(saveBt);
		saveBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(1);
				chooser.showOpenDialog(GuiMain.this.mcontent);
				if (chooser.getSelectedFile() != null && chooser.getSelectedFile().getAbsolutePath() != null) {
					GuiMain.this.savepath.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		JButton buttonpwd = new JButton("签名");
		buttonpwd.setBounds(380, 60, 140, 50);
		this.mcontent.add(buttonpwd);
		buttonpwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiMain.this.startSign();
			}
		});
	}

	private void selectApp() {
		this.apkpath = new JTextField(15);
		this.apkpath.setEnabled(false);
		this.apkpath.setBounds(20, 32, 180, 30);
		JButton app = new JButton("选择未签名apk文件");
		app.setBounds(200, 32, 140, 30);
		app.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiMain.this.apkpath.setText(GuiMain.this.showDialog("apk"));
			}
		});
		this.mcontent.add(this.apkpath);
		this.mcontent.add(app);
	}

	private String showDialog(final String name) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter() {
			public String getDescription() {
				return null;
			}

			public boolean accept(File f) {
				if (name == null) {
					return true;
				}
				return f.isDirectory() ? true : f.getName().endsWith(name);
			}
		});
		chooser.showOpenDialog(this.mcontent);
		File mFile = chooser.getSelectedFile();
		return mFile == null ? "" : mFile.getAbsolutePath();
	}

	private void startSign() {
		final String keypathstr = this.keypath.getText().toString();
		if (keypathstr.length() == 0) {
			DialogUtil.show(this.mcontent, "选择签名key");
			return;
		}
		final String apKstr = this.apkpath.getText().toString();
		if (apKstr.length() == 0) {
			DialogUtil.show(this.mcontent, "选择签名apk");
			return;
		}

		final String savepathstr = this.savepath.getText().toString();
		if (savepathstr.length() == 0) {
			DialogUtil.show(this.mcontent, "选择签名后apk保存路径");
			return;
		}
		this.savepathstr = this.savepath.getText().toString();
		if (!this.savepathstr.endsWith(".apk")) {
			String filename = new File(apKstr).getName();
			if ((filename.length() > 0) && (filename.contains("."))) {
				String[] names = filename.split("\\.");
				if (names.length > 0) {
					this.baseName = names[0];
					Date mDate = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					String format = dateFormat.format(mDate);
					this.savepathstr = (this.savepathstr + File.separator + this.baseName + format + "_signed.apk");
				}
			}
		}
		if (this.savepathstr.length() == 0) {
			DialogUtil.show(this.mcontent, "选择保存签名apk路径");
			return;
		}

		final String keyPassword = this.pwd.getText().toString();
		if (keyPassword.length() == 0) {
			DialogUtil.show(this.mcontent, "密码不能为空");
			return;
		}
		final String nameAlias = this.name.getText().toString();
		if (nameAlias.length() == 0) {
			DialogUtil.show(this.mcontent, "签名别名不能为空");
			return;
		}
		if (isstart) {
			DialogUtil.show(this.mcontent, "正在工作中...");
			return;
		}
		this.area.append("签名开始\r\n");
		new Thread(new Runnable() {
			public void run() {
				isstart = true;
				SignUtils.sign(keypathstr, keyPassword, GuiMain.this.savepathstr, apKstr, nameAlias, GuiMain.this.area);
				isstart = false;
			}
		}).start();
	}

	private void moreStart() {
		JButton button = new JButton("开始批量打包");
		button.setBounds(380, 180, 140, 50);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuiMain.this.excute();
			}
		});
		this.mcontent.add(button);
	}

	private void show() {

		JPanel jPanelTop = new JPanel();
		jPanelTop.setBounds(0, 0, this.width, 142);
		mcontent.add(jPanelTop);

		JPanel jPanelBottom = new JPanel();
		jPanelBottom.setBounds(0, 143, this.width, 350);
		mcontent.add(jPanelBottom);
		this.mcontent.setBackground(Color.gray);
		this.jframe.setContentPane(this.mcontent);
		this.jframe.setVisible(true);
	}
}
