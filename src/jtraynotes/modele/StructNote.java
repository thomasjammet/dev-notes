package jtraynotes.modele;
import java.util.Date;

public class StructNote
  implements Comparable
{
  public int indiceNote = -1;
  public String textNote = "";
  public Date dateModif = null;
  public Date dateRappel = null;
  public boolean bConfirm = false;

  public int compareTo(Object eltNote)
  {
    StructNote structNote2 = (StructNote)eltNote;

    return this.indiceNote - structNote2.indiceNote;
  }
}