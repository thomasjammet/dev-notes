package jtraynotes.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import jtraynotes.modele.JListNotesModel;
import jtraynotes.modele.RappelManagerThread;
import jtraynotes.modele.StructNote;

public class JNoteExplorer extends JFrame
{
  private static final long serialVersionUID = 1L;
  private Map<String, Vector<StructNote>> hashNotes;
  public RappelManagerThread threadRappels;
  public JButton btSupprimer;
  public JButton btAjouter;
  public JTabNotesPanel ongletPanel;
  public JMenuItem ssmenuFermer;
  public JMenuItem ssmenuQuitter;
  public JMenuItem ssmenuPreferences;
  public JMenuItem ssmenuAPropos;

  public JNoteExplorer(String stCheminFichierNotes, Map<String, Vector<StructNote>> hashNotes)
  {
    super("Carnet de notes");

    this.hashNotes = hashNotes;

    /********************************
		 * Création des menus
		 */
    MenuExplorerListener menuListener = new MenuExplorerListener(this);
    JMenuBar menuBar = new JMenuBar();

    JMenu menuFichier = new JMenu("Fichier");
    this.ssmenuFermer = new JMenuItem("Fermer");
    this.ssmenuFermer.addActionListener(menuListener);
    menuFichier.add(this.ssmenuFermer);
    menuFichier.addSeparator();
    this.ssmenuQuitter = new JMenuItem("Quitter");
    this.ssmenuQuitter.addActionListener(menuListener);
    menuFichier.add(this.ssmenuQuitter);
    menuBar.add(menuFichier);

    JMenu menuEdition = new JMenu("Edition");
    this.ssmenuPreferences = new JMenuItem("Preferences");
    this.ssmenuPreferences.addActionListener(menuListener);
    menuEdition.add(this.ssmenuPreferences);
    menuBar.add(menuEdition);

    JMenu menuInfos = new JMenu("?");
    JMenuItem ssmenuAide = new JMenuItem("Aide");
    menuInfos.add(ssmenuAide);
    menuInfos.addSeparator();
    this.ssmenuAPropos = new JMenuItem("A Propos");
    this.ssmenuAPropos.addActionListener(menuListener);
    menuInfos.add(this.ssmenuAPropos);
    menuBar.add(menuInfos);

    setJMenuBar(menuBar);

    /****************************
		 * Création de l'explorateur
		 */
    JPanel contentPanel = new JPanel(new BorderLayout());

    this.ongletPanel = new JTabNotesPanel(stCheminFichierNotes, this.hashNotes);
    contentPanel.add(this.ongletPanel, "Center");

    JPanel panelBoutons = new JPanel(new FlowLayout(2));
    BtCarnetListener boutonsListener = new BtCarnetListener(this);

    this.btSupprimer = new JButton();
    this.btSupprimer.addActionListener(boutonsListener);
    ImageIcon iconSupprimer = new ImageIcon("./img/btSupprimer_petit.png");
    this.btSupprimer.setIcon(iconSupprimer);
    panelBoutons.add(this.btSupprimer);
    this.btAjouter = new JButton();
    this.btAjouter.addActionListener(boutonsListener);
    ImageIcon iconAjouter = new ImageIcon("./img/btAJouter_petit.png");
    this.btAjouter.setIcon(iconAjouter);
    panelBoutons.add(this.btAjouter);
    contentPanel.add(panelBoutons, "South");

    /************************************
		 * Création du thread de gestion
		 * des rappels
		 */
    this.threadRappels = new RappelManagerThread(this.hashNotes, 60000, this);
    this.threadRappels.start();

    // Finalisation de la fenêtre
    setContentPane(contentPanel);
    setIconImage(new ImageIcon("./img/edit.png").getImage());
    setSize(new Dimension(300, Toolkit.getDefaultToolkit().getScreenSize().height - 30));
    setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - getWidth(), 0);
    setDefaultCloseOperation(1);
    setVisible(true);
  }

  public boolean bMajFichierXML()
  {
    return getDataModel().bMajFichierXML();
  }

  public void vAjouterNote()
  {
    getDataModel().vAjouterNote();
  }

  public void vSupprimerNote(int iIndNote)
  {
    getDataModel().vRemoveNoteAt(iIndNote);
  }

  public int getSelectedNoteIndex()
  {
    return this.ongletPanel.getSelectedNoteIndex();
  }

  public JListNotesModel getDataModel()
  {
    return this.ongletPanel.getDataModel();
  }
}