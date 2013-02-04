package jtraynotes.ctrl;
import java.io.File;
import java.util.Iterator;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class AnalyseConfXML
{
  public static String chargeFichierConfXML(String stConfXML)
    throws Exception
  {
    SAXBuilder sb = new SAXBuilder();
    XMLOutputter xo = new XMLOutputter();
    try
    {
      String stCheminNotes = "";

      File f = new File(stConfXML);
      Document document = sb.build(f);

      ElementFilter efAlbum = new ElementFilter("fichier");
      try
      {
        Iterator i = document.getDescendants(efAlbum);
        Element elt = null;

        if (i.hasNext()) {
          elt = (Element)i.next();
          Attribute attrCheminFichier = elt.getAttribute("chemin");
          stCheminNotes = attrCheminFichier.getValue();
        } else {
          throw new Exception("Impossible de trouver la balise 'fichier' dans le fichier de configuration.");
        }
      }
      catch (Exception e)
      {
        throw new Exception(e);
      }

      return stCheminNotes;
    }
    catch (Exception e)
    {
      throw e;
    }
  }
}