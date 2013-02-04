package jtraynotes.ihm;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import jtraynotes.modele.JListNotesModel;
import jtraynotes.modele.StructNote;

class WindowJNoteListener extends WindowAdapter
{
  public void windowClosing(WindowEvent e)
  {
    JNoteFrame frame = (JNoteFrame)e.getWindow();

    String areaText = frame.areaNote.getText();

    if (!frame.noteEnCours.equals(areaText)) {
      frame.noteEnCours.textNote = areaText;

      if (frame.checkBoxRappel.isSelected()) {
        int iNumJour = frame.calendar.iNumDaySelect;
        int iNumMois = frame.calendar.iMonthSelect;
        int iYear = frame.calendar.iYearSelect;

        int iHeure = 0;
        try {
          iHeure = ((Integer)frame.sHeure.getValue()).intValue();
        }
        catch (Exception ex) {
        }
        int iMinute = 0;
        try {
          iMinute = ((Integer)frame.sMinutes.getValue()).intValue();
        }
        catch (Exception ex) {
        }
        if ((iNumJour > 0) && (iNumMois >= 0) && (iYear >= 0)) {
          Calendar calendrier = Calendar.getInstance();
          calendrier.set(iYear, iNumMois, iNumJour, iHeure, iMinute);

          Date dateRappel = calendrier.getTime();

          if ((frame.noteEnCours.dateRappel != null) || (frame.noteEnCours.dateRappel != dateRappel)) {
            frame.noteEnCours.dateRappel = dateRappel;
            frame.noteEnCours.bConfirm = false;
          }
        }
      }
      else
      {
        frame.noteEnCours.dateRappel = null;
      }

      frame.modelNotes.vModifierNote(frame.noteEnCours);
    }
    else {
      frame.modelNotes.hashNotesFrames.remove(frame.noteEnCours);
    }

    frame.dispose();
  }
}