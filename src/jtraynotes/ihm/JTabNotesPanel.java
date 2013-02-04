package jtraynotes.ihm;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.TransferHandler;
import jtraynotes.ctrl.MouseListenerAreaNotes;
import jtraynotes.modele.JListNotesModel;
import jtraynotes.modele.StructNote;

/**
 * Classe d'implémentation des onglets du carnet de notes
 *
 */
public class JTabNotesPanel extends JTabbedPane
{
	private Map<String, Vector<StructNote>> hashNotes;
	private String m_stPathNotes;
	public static final DataFlavor PANEL_FLAVOR = new DataFlavor(JPanel.class, "JPanel");

	public JTabNotesPanel(String stCheminFichierNotes, Map<String, Vector<StructNote>> hashNotes)
	{
		this.hashNotes = hashNotes;
		this.m_stPathNotes = stCheminFichierNotes;

		int i = 0;
		for (Map.Entry coupleOnglet : hashNotes.entrySet()) {
			String stOnglet = (String)coupleOnglet.getKey();
			Vector listNotes = (Vector)coupleOnglet.getValue();

			JListNotesModel dataModel = new JListNotesModel(stCheminFichierNotes, listNotes, this);
			JList jlistNotes = new JList(dataModel);

			jlistNotes.setCellRenderer(new JNoteCellRenderer());
			jlistNotes.setTransferHandler(new PanelHandler());
			jlistNotes.setDragEnabled(true);

			MouseListenerAreaNotes listenerNotes = new MouseListenerAreaNotes(dataModel);
			jlistNotes.addMouseListener(listenerNotes);
			jlistNotes.addKeyListener(new KeyListenerNotes(dataModel));
			JScrollPane scrollList = new JScrollPane(jlistNotes);

			insertTab(stOnglet, null, scrollList, "Onglet " + stOnglet, i);
			i++;
		}

		insertTab("+", null, new JScrollPane(), "Ajouter un onglet", i);

		addMouseListener(new JTabMouseListener(this));
	}

	/**
	 * Fonction de création d'un onglet
	 * @param stOngletName nom de l'onglet
	 */
	void vCreateOnglet(String stOngletName)
	{
		Vector listNotes = new Vector();
		this.hashNotes.put(stOngletName, listNotes);

		JListNotesModel dataModel = new JListNotesModel(this.m_stPathNotes, listNotes, this);
		JList jlistNotes = new JList(dataModel);

		jlistNotes.setCellRenderer(new JNoteCellRenderer());
		jlistNotes.setTransferHandler(new PanelHandler());
		jlistNotes.setDragEnabled(true);

		MouseListenerAreaNotes listenerNotes = new MouseListenerAreaNotes(dataModel);
		jlistNotes.addMouseListener(listenerNotes);
		jlistNotes.addKeyListener(new KeyListenerNotes(dataModel));
		JScrollPane scrollList = new JScrollPane(jlistNotes);

		insertTab(stOngletName, null, scrollList, "Onglet " + stOngletName, getTabCount() - 1);
	}

	public int getSelectedNoteIndex()
	{
		JScrollPane scroll = (JScrollPane)getSelectedComponent();
		if (scroll == null) return -1;

		JList jlistNotes = (JList)scroll.getViewport().getComponent(0);
		if (jlistNotes == null) return -1;

		return jlistNotes.getSelectedIndex();
	}

	public Map<String, Vector<StructNote>> getMapNotes()
	{
		return this.hashNotes;
	}

	public JListNotesModel getDataModel()
	{
		JScrollPane scroll = (JScrollPane)getSelectedComponent();
		if (scroll == null) return null;

		JList jlistNotes = (JList)scroll.getViewport().getComponent(0);
		if (jlistNotes == null) return null;

		return (JListNotesModel)jlistNotes.getModel();
	}

	public static final class PanelTransferable
	implements Transferable
	{
		public static final DataFlavor[] FLAVORS = { JTabNotesPanel.PANEL_FLAVOR };
		private JPanel target;

		public PanelTransferable(JPanel target)
		{
			this.target = target;
		}

		public Object getTransferData(DataFlavor flavor)
		throws UnsupportedFlavorException
		{
			if (flavor.equals(JTabNotesPanel.PANEL_FLAVOR)) {
				return this.target;
			}
			throw new UnsupportedFlavorException(flavor);
		}

		public DataFlavor[] getTransferDataFlavors()
		{
			return FLAVORS;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			for (DataFlavor f : getTransferDataFlavors()) {
				if (f.equals(flavor)) {
					return true;
				}
			}
			return false;
		}
	}

	public static final class PanelHandler extends TransferHandler
	{
		private StructNote eltNote;
		private int iIndex;

		private JPanel exportPanel(JComponent c)
		{
			if ((c instanceof JList)) {
				JList list = (JList)c;
				this.eltNote = ((StructNote)list.getSelectedValue());
				this.iIndex = list.getSelectedIndex();

				Component objPanel = list.getCellRenderer().getListCellRendererComponent(list, this.eltNote, this.iIndex, true, true);

				if (objPanel == null) return null;

				return (JPanel)objPanel;
			}

			return null;
		}

		protected Transferable createTransferable(JComponent c) {
			JPanel panel = exportPanel(c);
			return panel == null ? null : new JTabNotesPanel.PanelTransferable(panel);
		}

		public int getSourceActions(JComponent c) {
			return 3;
		}

		public boolean importData(JComponent c, Transferable t) {
			if (canImport(c, t.getTransferDataFlavors())) {
				try {
					JPanel panel = (JPanel)t.getTransferData(JTabNotesPanel.PANEL_FLAVOR);
					importPanel(c, panel);
					return true;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}

		private void importPanel(JComponent c, JPanel panel)
		{
			if ((c instanceof JList)) {
				JList list = (JList)c;
				JListNotesModel listModel = (JListNotesModel)list.getModel();
				int insertRow = list.getSelectedIndex();

				if (insertRow > this.iIndex) insertRow++;
				else if (insertRow < this.iIndex) this.iIndex += 1;

				listModel.vInsertNoteAt(this.eltNote, insertRow);
			}
		}

		protected void exportDone(JComponent c, Transferable data, int action) {
			cleanup(c, action == 2);
		}

		private void cleanup(JComponent c, boolean remove) {
			if ((remove) && 
					((c instanceof JList))) {
				JList list = (JList)c;
				JListNotesModel listModel = (JListNotesModel)list.getModel();

				listModel.vRemoveNoteAt(this.iIndex);
			}
		}

		public boolean canImport(JComponent c, DataFlavor[] flavors)
		{
			for (int i = 0; i < flavors.length; i++) {
				if (JTabNotesPanel.PANEL_FLAVOR.equals(flavors[i])) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Listener sur le titre des onglets
	 *
	 */
	public class JTabMouseListener implements MouseListener
	{
		private JTabNotesPanel ongletPanel;

		public JTabMouseListener(JTabNotesPanel ongletPanel)
		{
			this.ongletPanel = ongletPanel;
		}

		public void mouseClicked(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {

			int iIndex = this.ongletPanel.getSelectedIndex();
			boolean bIsOngletPlus =  (iIndex == this.ongletPanel.getTabCount() - 1);
			
			// Si c'est le dernier onglet (onglet '+') : demande de création d'un nouvel onglet
			if (bIsOngletPlus) {
				String stOngletName = JOptionPane.showInputDialog(this.ongletPanel, "Saisissez le nom de l'onglet à créer", "Création d'un onglet");

				if (stOngletName != null)
					this.ongletPanel.vCreateOnglet(stOngletName);
			}
			// clic droit : modification du nom de l'onglet
			else if (e.getButton() == MouseEvent.BUTTON3) {
				String stOngletName = JOptionPane.showInputDialog(this.ongletPanel, 
						"Saisissez le nom de l'onglet à modifier", this.ongletPanel.getTitleAt(iIndex));

				if (stOngletName != null)
					this.ongletPanel.setTitleAt(iIndex, stOngletName);
			}
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}
	}
}