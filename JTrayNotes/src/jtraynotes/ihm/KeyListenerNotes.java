package jtraynotes.ihm;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TreeMap;
import javax.swing.JList;

import jtraynotes.modele.JListNotesModel;
import jtraynotes.modele.StructNote;

public class KeyListenerNotes
  implements KeyListener
{
  private JListNotesModel dataModel;

  public KeyListenerNotes(JListNotesModel model)
  {
    this.dataModel = model;
  }

  public void keyPressed(KeyEvent arg0)
  {
  }

  public void keyReleased(KeyEvent evt) {
    if (evt.getKeyChar() == '\n') {
      JList jlistNotes = (JList)evt.getSource();
      StructNote eltNote = (StructNote)jlistNotes.getSelectedValue();
      if ((eltNote != null) && 
        (this.dataModel.hashNotesFrames.get(eltNote) == null)) {
        this.dataModel.hashNotesFrames.put(eltNote, new JNoteFrame("Edition d'une note", eltNote, this.dataModel));
      }
    }
    else if (evt.getKeyCode() == 127)
    {
      JList listNotes = (JList)evt.getSource();

      int iIndex = listNotes.getSelectedIndex();
      if (iIndex > -1) this.dataModel.vRemoveNoteAt(iIndex);
    }
  }

  public void keyTyped(KeyEvent arg0)
  {
  }
}