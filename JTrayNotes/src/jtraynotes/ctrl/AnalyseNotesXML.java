package jtraynotes.ctrl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import jtraynotes.modele.StructNote;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class AnalyseNotesXML
{

	/******************************************
	 * Charge la liste des notes depuis le
	 * fichier XML
	 * @param stCheminFichierNotes chemin du fichier XML des notes
	 * @param listNotes liste des notes
	 * @throws Exception
	 */
	public static void chargeListeNotes(String stCheminFichierNotes, Map<String, Vector<StructNote>> hashNotes)
	throws Exception
	{
		SAXBuilder sb = new SAXBuilder();

		String stCheminNotes = "";

		File f = new File(stCheminFichierNotes);

		if (f.length() > 10L) {
			Document document = sb.build(f);

			ElementFilter efAlbum = new ElementFilter("Onglet");

			Iterator i = document.getDescendants(efAlbum);
			Element elt = null;

			while (i.hasNext()) {
				elt = (Element)i.next();

				String stNomOnglet = elt.getAttributeValue("name");
				Vector listNotes = new Vector();

				for (Iterator i$ = elt.getChildren().iterator(); i$.hasNext(); ) { Object obj = i$.next();
				Element eltNote = (Element)obj;
				traiteBaliseNote(eltNote, listNotes);
				}

				hashNotes.put(stNomOnglet, listNotes);
			}
		}
	}

	private static void traiteBaliseNote(Element elt, Vector<StructNote> listNotes)
	{
		StructNote eltNote = new StructNote();

		Attribute attrDateModif = elt.getAttribute("dateModif");
		String stDateModif = attrDateModif.getValue();

		DateFormat formatter = DateFormat.getDateTimeInstance(3, 3);
		Date dateModif = null;
		try {
			dateModif = formatter.parse(stDateModif);
		} catch (Exception e) {
			dateModif = null;
		}
		if (dateModif != null)
			eltNote.dateModif = dateModif;
		else {
			return;
		}

		Attribute attrIndice = elt.getAttribute("indiceNote");
		int iIndiceNote = -1;
		try {
			iIndiceNote = attrIndice.getIntValue();
		} catch (DataConversionException e) {
			iIndiceNote = -1;
		}
		if (iIndiceNote > 0)
			eltNote.indiceNote = iIndiceNote;
		else {
			return;
		}

		Attribute attrDateRappel = elt.getAttribute("dateRappel");

		Date dateRappel = null;
		if (attrDateRappel != null) {
			String stDateRappel = attrDateRappel.getValue();

			DateFormat formatterRappel = DateFormat.getDateTimeInstance(3, 3);
			try {
				dateRappel = formatterRappel.parse(stDateRappel);
			} catch (Exception e) {
				dateRappel = null;
			}
			if (dateRappel != null) {
				eltNote.dateRappel = dateRappel;
			}

		}

		/********************************************
		 * Recherche de la marque de rappel confirmé
		 */
		if (dateRappel != null) {
			boolean bConfirm = false;
			Attribute attrBConfirm = elt.getAttribute("confirm");
			try {
				bConfirm = attrBConfirm.getBooleanValue();
			} catch (DataConversionException e) {
				return;
			}

			eltNote.bConfirm = bConfirm;
		}

		eltNote.textNote = elt.getText();

		listNotes.add(eltNote);
	}

	/**
	 * Ecriture du fichier des notes
	 * 
	 * @param stCheminNotesXML
	 * @param listNotes
	 * @throws Exception
	 */
	public static void extractListNotes(String stCheminNotesXML, Map<String, Vector<StructNote>> hashNotes)
	throws Exception
	{
		XMLOutputter xo = new XMLOutputter();
		try
		{
			Document docNotes = new Document();
			Element eltRacineNotes = new Element("Notes");

			for (Map.Entry coupleOnglet : hashNotes.entrySet()) {
				Element eltOnglet = new Element("Onglet");
				String stOnglet = (String)coupleOnglet.getKey();
				eltOnglet.setAttribute("name", stOnglet);

				Vector<StructNote> listNotes = (Vector<StructNote>)coupleOnglet.getValue();

				for (StructNote structNote : listNotes) {
					Element eltNote = new Element("note");
					if (structNote.textNote != null) {
						CDATA noteText = new CDATA(structNote.textNote);
						eltNote.setContent(noteText);
					} else {
						return;
					}

					if (structNote.indiceNote > 0)
						eltNote.setAttribute("indiceNote", "" + structNote.indiceNote);
					else {
						return;
					}

					if (structNote.dateModif != null) {
						String stDateModif = DateFormat.getInstance().format(structNote.dateModif);
						if (stDateModif != null)
							eltNote.setAttribute("dateModif", stDateModif);
						else
							return;
					}
					else {
						return;
					}

					if (structNote.dateRappel != null) {
						String stDateRappel = DateFormat.getInstance().format(structNote.dateRappel);
						if (stDateRappel != null) {
							eltNote.setAttribute("dateRappel", stDateRappel);
						}
					}

					if (structNote.bConfirm)
						eltNote.setAttribute("confirm", "true");
					else {
						eltNote.setAttribute("confirm", "false");
					}

					eltOnglet.addContent(eltNote);
				}

				eltRacineNotes.addContent(eltOnglet);
			}

			docNotes.addContent(eltRacineNotes);

			//Sérialisation
			File fNotes = new File(stCheminNotesXML);
			FileOutputStream fic = new FileOutputStream(fNotes);
			xo.setFormat(Format.getPrettyFormat());
			xo.output(docNotes, fic);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
}