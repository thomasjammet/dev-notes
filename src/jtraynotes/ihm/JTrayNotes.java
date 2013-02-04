package jtraynotes.ihm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import jtraynotes.ctrl.AnalyseConfXML;
import jtraynotes.ctrl.AnalyseNotesXML;
import jtraynotes.ctrl.SysTrayMenuItemListener;
import jtraynotes.modele.StructNote;

import snoozesoft.systray4j.SysTrayMenu;
import snoozesoft.systray4j.SysTrayMenuIcon;
import snoozesoft.systray4j.SysTrayMenuItem;

public class JTrayNotes
{
	public static final String MENU_ITEM_OUVRIR = "Ouvrir";
	public static final String MENU_ITEM_QUITTER = "Quitter";

	//Instance Singleton
	private static JTrayNotes INSTANCE = null;
	private Map<String, Vector<StructNote>> hashNotes;
	private String stCheminFichierNotes = "";
	private boolean bStarted = false;

	/**
	 * Constructeur
	 */
	private JTrayNotes()
	{
		/****************************
		 * Chargement du fichier de configuration
		 */
		String stConfXML = "./confXML.xml";
		try
		{
			this.stCheminFichierNotes = AnalyseConfXML.chargeFichierConfXML(stConfXML);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erreur de chargement du fichier XML de configuration (" + stConfXML + ") : " + e.getMessage(), "Erreur de configuration", 0);
			return;
		}

		/*****************************************
		 * Chargement des notes
		 */
		this.hashNotes = new LinkedHashMap();
		if (!this.stCheminFichierNotes.equals(""))
			try
		{
				AnalyseNotesXML.chargeListeNotes(this.stCheminFichierNotes, this.hashNotes);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erreur de chargement du fichier  (" + this.stCheminFichierNotes + ") : " + e.getMessage(), "Erreur de configuration", 0);
			return;
		}
		else;
	}

	/**
	 * Lancement de JTrayNotes
	 * 
	 * @return Faux si JTrayNotes était déjà lancé, Vrai sinon
	 */
	public boolean start()
	{
		if (!this.bStarted) this.bStarted = true; else {
			return false;
		}

		/****************************
		 * Création du SysTray
		 */
		//Création de l'icone dans la barre de systray
		SysTrayMenu sysTrayNotes = new SysTrayMenu(new SysTrayMenuIcon("./img/edit.ico"), "Carnet de notes");

		SysTrayMenuItemListener listener = new SysTrayMenuItemListener(this.stCheminFichierNotes, this.hashNotes);

		SysTrayMenuItem sysTrayItemQuitter = new SysTrayMenuItem("Quitter");
		sysTrayItemQuitter.addSysTrayMenuListener(listener);
		sysTrayNotes.addItem(sysTrayItemQuitter);

		sysTrayNotes.addSeparator();

		SysTrayMenuItem sysTrayItemOuvrir = new SysTrayMenuItem("Ouvrir");
		sysTrayItemOuvrir.addSysTrayMenuListener(listener);
		sysTrayNotes.addItem(sysTrayItemOuvrir);

		// Ouverture sur dbl-click de l'icone
		SysTrayMenuIcon icon = sysTrayNotes.getIcon();
		icon.setActionCommand("Ouvrir");
		icon.addSysTrayMenuListener(listener);

		return true;
	}

	/**
	 * Pattern Singleton.
	 * Si une instance de JTrayNotes existe on la renvoie
	 * Sinon création de l'instance singleton
	 * 
	 * @return la tâche JTrayNotes
	 */
	public static JTrayNotes getInstance()
	{
		if (INSTANCE == null) {
			INSTANCE = new JTrayNotes();
			return INSTANCE;
		}
		return INSTANCE;
	}

	public static void main(String[] args)
	{
		JTrayNotes trayNotes = getInstance();

		trayNotes.start();
	}
}