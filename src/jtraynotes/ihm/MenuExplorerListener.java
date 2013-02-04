package jtraynotes.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import jtraynotes.modele.JListNotesModel;
import jtraynotes.modele.RappelManagerThread;
import snoozesoft.systray4j.SysTrayMenu;

class MenuExplorerListener
  implements ActionListener
{
  private JNoteExplorer frame;

  public MenuExplorerListener(JNoteExplorer fenetre)
  {
    this.frame = fenetre;
  }

  public void actionPerformed(ActionEvent evt)
  {
    JMenuItem itemMenu = (JMenuItem)evt.getSource();

    if (this.frame.ssmenuFermer == itemMenu) {
      this.frame.setVisible(false);
    } else if (this.frame.ssmenuQuitter == itemMenu) {
      this.frame.getDataModel().vFermeFenetres();
      this.frame.threadRappels.interrupt();
      this.frame.dispose();
      SysTrayMenu.dispose();
    } else if (this.frame.ssmenuPreferences == itemMenu) {
      new JPreferencesFrame();
    } else if (this.frame.ssmenuAPropos == itemMenu) {
      new JAProposFrame();
    }
  }
}