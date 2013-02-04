package jtraynotes.modele;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import jtraynotes.ihm.JNoteExplorer;

/**
 * Classe de gestion des rappels en cours
 * 
 */
public class RappelManagerThread extends Thread
{
	public Map<String, Vector<StructNote>> hashNotes;
	public int iSleepTime = 0;
	private JNoteExplorer explorer;

	public RappelManagerThread(Map<String, Vector<StructNote>> hashNotes, int sleepTime, JNoteExplorer noteExplorer)
	{
		this.hashNotes = hashNotes;
		this.iSleepTime = sleepTime;
		this.explorer = noteExplorer;
	}

	public void run()
	{
		boolean bQuit = false;

		while (!bQuit)
		{
			for (Vector<StructNote> listNotes : this.hashNotes.values())
			{
				for (StructNote eltNote : listNotes) {
					if (eltNote.dateRappel != null)
					{
						if ((!eltNote.bConfirm) && (eltNote.dateRappel.compareTo(new Date()) <= 0)) {
							String stDateRappel = DateFormat.getInstance().format(eltNote.dateRappel);

							int iConfirmRes = JOptionPane.showConfirmDialog(null, stDateRappel + ": " + eltNote.textNote + "\nSouhaitez-vous arrêter le rappel?", "Rappel", 0);
							if (iConfirmRes == 0) {
								eltNote.bConfirm = true;

								this.explorer.bMajFichierXML();
							}
						}
					}
				}

			}

			try
			{
				Thread.sleep(this.iSleepTime);
			} catch (InterruptedException e) {
				bQuit = true;
			}
		}
	}
}