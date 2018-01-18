package com.my.tool.ui;

 

import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class DialogUtil
{
  public static void show() {}
  
  public static void show(Container jPanel, String string)
  {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    int w = (int)toolkit.getScreenSize().getWidth();
    int h = (int)toolkit.getScreenSize().getHeight();
    JDialog dialog = new JDialog();
    dialog.setTitle("提示");
    dialog.setLocation((w - 200) / 2, (h - 150) / 2);
    dialog.setSize(200, 150);
    dialog.setModalityType(JDialog.ModalityType.TOOLKIT_MODAL);
    dialog.setResizable(false);
    dialog.setAlwaysOnTop(true);
    JLabel comp = new JLabel(string);
    comp.setHorizontalAlignment(0);
    dialog.add(comp);
    
    dialog.setVisible(true);
  }
}
