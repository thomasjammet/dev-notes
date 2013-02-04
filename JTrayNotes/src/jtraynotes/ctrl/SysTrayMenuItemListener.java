package jtraynotes.ctrl;

import java.util.Map;
import java.util.Vector;

import jtraynotes.ihm.JNoteExplorer;
import jtraynotes.modele.JListNotesModel;
import jtraynotes.modele.RappelManagerThread;
import jtraynotes.modele.StructNote;

import snoozesoft.systray4j.SysTrayMenu;
import snoozesoft.systray4j.SysTrayMenuAdapter;
import snoozesoft.systray4j.SysTrayMenuEvent;
import snoozesoft.systray4j.SysTrayMenuItem;

public class SysTrayMenuItemListener extends SysTrayMenuAdapter
{
  JNoteExplorer explorerInstance = null;

  public SysTrayMenuItemListener(String stCheminFichierNotes, Map<String, Vector<StructNote>> hashNotes) {
    this.explorerInstance = new JNoteExplorer(stCheminFichierNotes, hashNotes);
  }

  public void menuItemSelected(SysTrayMenuEvent evt)
  {
    SysTrayMenuItem itemMenu = (SysTrayMenuItem)evt.getSource();
    if (itemMenu.getLabel().equals("Quitter"))
    {
      if (this.explorerInstance != null) {
        JListNotesModel notesModel = this.explorerInstance.getDataModel();
        if (notesModel != null) notesModel.vFermeFenetres();
        this.explorerInstance.threadRappels.interrupt();
        this.explorerInstance.dispose();
      }

      SysTrayMenu.dispose();
    } else if ((itemMenu.getLabel().equals("Ouvrir")) && 
      (!this.explorerInstance.isVisible())) {
      this.explorerInstance.setVisible(true);
    }
  }

  public void iconLeftDoubleClicked(SysTrayMenuEvent e)
  {
    if (!this.explorerInstance.isVisible())
      this.explorerInstance.setVisible(true);
  }
}