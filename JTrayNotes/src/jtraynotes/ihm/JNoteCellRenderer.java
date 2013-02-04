package jtraynotes.ihm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DateFormat;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

import jtraynotes.modele.StructNote;

/***
 * Cette classe gère le rendu d'une note
 * dans la liste des notes du JNoteExplorer 
 *
 */
public class JNoteCellRenderer
  implements ListCellRenderer
{
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    StructNote eltNote = (StructNote)value;

    if (eltNote == null) return null;

    JPanel panelNote = new JPanel(new BorderLayout());

    JTextArea areaNote = new JTextArea();
    areaNote.setEditable(false);
    areaNote.setLineWrap(true);
    areaNote.setText(eltNote.textNote);
    panelNote.add(areaNote, "Center");

    JPanel panelDroit = new JPanel(new GridLayout(2, 1));
    if (eltNote.dateModif != null) {
      JLabel labDateModif = new JLabel("Modifs : " + DateFormat.getInstance().format(eltNote.dateModif));
      panelDroit.add(labDateModif);
    } else {
      JLabel labDateModif = new JLabel("Modifs : ");
      panelDroit.add(labDateModif);
    }
    if (eltNote.dateRappel != null) {
      JLabel labDateRappel = new JLabel("Rappel : " + DateFormat.getInstance().format(eltNote.dateRappel));
      panelDroit.add(labDateRappel);
    } else {
      JLabel labDateRappel = new JLabel("Rappel : ");
      panelDroit.add(labDateRappel);
    }
    panelNote.add(panelDroit, "East");

    if (isSelected) {
      areaNote.setBackground(Color.BLUE);
      panelDroit.setBackground(Color.BLUE);
    }

    panelNote.setBorder(new LineBorder(Color.black, 1));
    panelNote.setPreferredSize(new Dimension(150, 100));

    return panelNote;
  }
}