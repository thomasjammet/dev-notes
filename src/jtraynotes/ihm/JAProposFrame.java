package jtraynotes.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JAProposFrame extends JFrame
{
  private static final long serialVersionUID = 1L;

  public JAProposFrame()
  {
    super("A propos");

    JPanel panelContent = new JPanel(new GridLayout(3, 2));

    panelContent.add(new JLabel("Nom : "));
    panelContent.add(new JLabel("Carnet de notes"));

    panelContent.add(new JLabel("Auteur : "));
    panelContent.add(new JLabel("Jammet Thomas"));

    panelContent.add(new JLabel("Date de création : "));
    panelContent.add(new JLabel("Janvier 2009"));

    setContentPane(panelContent);
    setDefaultCloseOperation(2);
    setSize(new Dimension(500, 320));
    setResizable(false);
    setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
    setVisible(true);
  }
}