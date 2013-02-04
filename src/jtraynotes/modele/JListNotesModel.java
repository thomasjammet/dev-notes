package jtraynotes.modele;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import jtraynotes.ctrl.AnalyseNotesXML;
import jtraynotes.ihm.JNoteFrame;
import jtraynotes.ihm.JTabNotesPanel;

public class JListNotesModel
  implements ListModel
{
  JTabNotesPanel ongletNotes;
  private Vector<StructNote> listNotes;
  private Vector<ListDataListener> listListener;
  public TreeMap<StructNote, JNoteFrame> hashNotesFrames;
  private String stCheminNotesXML;

  public JListNotesModel(String stCheminFichierNotes, Vector<StructNote> tabNotes, JTabNotesPanel ongletNotes)
  {
    this.listNotes = tabNotes;
    this.stCheminNotesXML = stCheminFichierNotes;
    this.ongletNotes = ongletNotes;

    this.listListener = new Vector();

    this.hashNotesFrames = new TreeMap();
  }

  public void addListDataListener(ListDataListener listener)
  {
    this.listListener.add(listener);
  }

  public Object getElementAt(int iElt)
  {
    return this.listNotes.elementAt(iElt);
  }

  public int getSize()
  {
    return this.listNotes.size();
  }

  public void removeListDataListener(ListDataListener listener)
  {
    this.listListener.remove(listener);
  }

  /**
	 * Ferme toutes les fenêtres de notes
	 */
  public void vFermeFenetres()
  {
    for (JNoteFrame frame : this.hashNotesFrames.values())
      frame.dispose();
  }

  /**
	 * Evenement d'ajout d'une note au modele
	 */
  public void vAjouterNote()
  {
    StructNote eltNote = new StructNote();
    eltNote.indiceNote = (this.listNotes.size() + 1);
    eltNote.dateModif = new Date();
    this.listNotes.add(eltNote);

    if (!bMajFichierXML())
    {
      this.listNotes.remove(eltNote);

      return;
    }

    for (ListDataListener listener : this.listListener) {
      listener.contentsChanged(new ListDataEvent(this, 1, this.listNotes.size() - 1, this.listNotes.size() - 1));
    }

    // Ouverture de la fenetre d'édition
    vOuvrirNote(eltNote);
  }

  public void vModifierNote(StructNote eltNote) {
    int iIndNote = this.listNotes.indexOf(eltNote);

    if (bMajFichierXML())
    {
      this.hashNotesFrames.remove(eltNote);

      for (ListDataListener listener : this.listListener)
        listener.contentsChanged(new ListDataEvent(this, 0, iIndNote, iIndNote));
    }
  }

  public boolean bMajFichierXML()
  {
    try
    {
      AnalyseNotesXML.extractListNotes(this.stCheminNotesXML, this.ongletNotes.getMapNotes());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Impossible d'enregistrer le fichier '" + this.stCheminNotesXML + "' : " + e.getMessage(), "Erreur de sauvegarde", 0);
      return false;
    }

    return true;
  }

  /**
	 * MAJ du fichier xml
	 * @return VRAI si le fichier a été mis à jour
	 */
  public void vOuvrirNote(StructNote eltNote)
  {
    if (this.hashNotesFrames.get(eltNote) != null) return;

    JNoteFrame fenetreNote = new JNoteFrame("Edition d'une note", eltNote, this);
    this.hashNotesFrames.put(eltNote, fenetreNote);
  }

  public void vInsertNoteAt(StructNote eltNote, int insertRow)
  {
    this.listNotes.insertElementAt(eltNote, insertRow);

    if (!bMajFichierXML())
    {
      this.listNotes.remove(eltNote);
      return;
    }

    for (ListDataListener listener : this.listListener)
      listener.contentsChanged(new ListDataEvent(this, 1, insertRow, insertRow));
  }

  public void vRemoveNoteAt(int insertRow)
  {
    StructNote eltNote = (StructNote)this.listNotes.remove(insertRow);

    if (!bMajFichierXML())
    {
      this.listNotes.insertElementAt(eltNote, insertRow);
      return;
    }

    for (ListDataListener listener : this.listListener)
      listener.contentsChanged(new ListDataEvent(this, 2, insertRow, insertRow));
  }
}