package jtraynotes.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BtCarnetListener
  implements ActionListener
{
  private JNoteExplorer carnetNote;

  public BtCarnetListener(JNoteExplorer noteExplorer)
  {
    this.carnetNote = noteExplorer;
  }

  public void actionPerformed(ActionEvent evt)
  {
    if (evt.getSource() == this.carnetNote.btAjouter)
    {
      this.carnetNote.vAjouterNote();
    }
    else if (evt.getSource() == this.carnetNote.btSupprimer)
    {
      int iIndex = this.carnetNote.getSelectedNoteIndex();
      if (iIndex != -1) this.carnetNote.vSupprimerNote(iIndex);
    }
  }
}