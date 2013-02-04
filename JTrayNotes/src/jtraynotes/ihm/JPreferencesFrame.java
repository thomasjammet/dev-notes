package jtraynotes.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JPreferencesFrame extends JFrame
{
  private static final long serialVersionUID = 1L;
  public JPreferencesFrame()
  {
    super("Preferences");

    JPanel panelContent = new JPanel(new GridLayout(3, 2));

    setContentPane(panelContent);
    setSize(new Dimension(500, 320));
    setResizable(false);
    setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
    setDefaultCloseOperation(2);
    setVisible(true);
  }
}