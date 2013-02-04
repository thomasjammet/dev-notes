package jtraynotes.ctrl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import jtraynotes.modele.JListNotesModel;
import jtraynotes.modele.StructNote;

public class MouseListenerAreaNotes extends MouseAdapter
{
  private JListNotesModel modelNotes;

  public MouseListenerAreaNotes(JListNotesModel notesModel)
  {
    this.modelNotes = notesModel;
  }

  /*
	 * Double-click sur une note
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
  public void mouseClicked(MouseEvent evt)
  {
    if (evt.getClickCount() == 2)
    {
      JList jlistNotes = (JList)evt.getSource();
      StructNote eltNote = (StructNote)jlistNotes.getSelectedValue();

      if (eltNote != null)
      {

		// Ouverture de la fenêtre d'édition d'une note
        this.modelNotes.vOuvrirNote(eltNote);
      }
    }
  }

  public void mouseDragged(MouseEvent evt)
  {
  }
}