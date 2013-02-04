package jtraynotes.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import jtraynotes.modele.JListNotesModel;
import jtraynotes.modele.StructNote;

/**
 * C'est la classe qui représente la fenêtre
 * d'une note
 */
public class JNoteFrame extends JFrame
{
  private static final long serialVersionUID = 1L;
  public boolean bRappel = false;
  public StructNote noteEnCours;
  public JListNotesModel modelNotes;
  public JTextArea areaNote;
  public JCalendarPanel calendar;
  public JPanel panelHeure;
  public JSpinner sHeure;
  public JSpinner sMinutes;
  public JCheckBox checkBoxRappel;

  public JNoteFrame(String nameFrame, StructNote eltNote, JListNotesModel listNotesModel)
  {
    super(nameFrame);

    this.modelNotes = listNotesModel;
    this.noteEnCours = eltNote;

    int iAnnee = -1;
    int iMois = -1;
    int iJour = -1;
    int iHeure = 0;
    int iMinutes = 0;

    Calendar calendrier = Calendar.getInstance();
    if (eltNote.dateRappel != null) {
      calendrier.setTime(eltNote.dateRappel);
    }

    iAnnee = calendrier.get(1);

    iMois = calendrier.get(2);

    iJour = calendrier.get(5);

    iHeure = calendrier.get(11);

    iMinutes = calendrier.get(12);

    JPanel panelContent = new JPanel(new BorderLayout());

    this.areaNote = new JTextArea();
    this.areaNote.setText(this.noteEnCours.textNote);
    this.areaNote.setLineWrap(true);
    JScrollPane scrollPaneEditor = new JScrollPane(this.areaNote);
    panelContent.add(scrollPaneEditor, "Center");

    /*************************************
		 * Création du panel de gestion des rappels
		 */
    JPanel panelRappel = new JPanel(new BorderLayout());
    JPanel panelHaut = new JPanel();
    JLabel labRappel = new JLabel("Ajouter un rappel");
    panelHaut.add(labRappel);
    this.checkBoxRappel = new JCheckBox();
    CheckRappelListener checkBoxListener = new CheckRappelListener(this);
    this.checkBoxRappel.addActionListener(checkBoxListener);
    panelHaut.add(this.checkBoxRappel);
    panelRappel.add(panelHaut, "North");

    this.calendar = new JCalendarPanel(iAnnee, iMois, iJour);
    this.calendar.setVisible(false);
    panelRappel.add(this.calendar, "Center");

    this.panelHeure = new JPanel();
    JLabel labHeure = new JLabel("Heure : ");
    this.panelHeure.add(labHeure);
    this.sHeure = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
    this.sHeure.setValue(Integer.valueOf(iHeure));
    this.panelHeure.add(this.sHeure);
    this.sMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
    this.sMinutes.setValue(Integer.valueOf(iMinutes));
    this.panelHeure.add(this.sMinutes);
    this.panelHeure.setVisible(false);
    panelRappel.add(this.panelHeure, "South");

    panelContent.add(panelRappel, "East");

    // Si un rappel a été défini
		if(eltNote.dateRappel != null) {
			
			// Si la date du rappel est supérieure à la date du jour
			if(eltNote.dateRappel.after(new Date())) {
			
				// Affichage du mode rappel
				this.setRappel(true);
			}
		}

    setContentPane(panelContent);
    addWindowListener(new WindowJNoteListener());
    setMinimumSize(new Dimension(500, 320));
    setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
    setVisible(true);
  }

  public void setRappel(boolean bModeRappel)
  {
    this.bRappel = bModeRappel;
    this.checkBoxRappel.setSelected(bModeRappel);
    this.calendar.setVisible(bModeRappel);
    this.panelHeure.setVisible(bModeRappel);
  }
}