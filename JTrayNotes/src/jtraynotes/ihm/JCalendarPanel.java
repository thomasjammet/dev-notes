package jtraynotes.ihm;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JCalendarPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Locale _locale = getDefaultLocale();

	private GregorianCalendar _calendar = new GregorianCalendar();

	private DateFormatSymbols _dateSymbols = new DateFormatSymbols();

	private SimpleDateFormat _formatMY = new SimpleDateFormat("MMMM yyyy");

	private int _firstDayOfWeek = this._calendar.getFirstDayOfWeek();

	private final JLabel _monthYear = new JLabel("", 0);

	private final JLabel[] _daysOfWeek = new JLabel[7];

	private final JButton[] _daysNumber = new JButton[42];
	private MngDayButtons listener;
	public int iNumDaySelect;
	public int iMonthSelect;
	public int iYearSelect;
	public JButton btDaySelect;
	public Color colorDefaut;

	//Création du listener sur les changements de mois
	private final ActionListener _changeMonth = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			int nb = "next".equals(ae.getActionCommand()) ? 1 : -1;
			JCalendarPanel.this._calendar.add(2, nb);
			JCalendarPanel.this.updateMonthYear();
			JCalendarPanel.this.updateDaysNumber(false);

			//Si l'on avait un bouton sélectionné on réinitialise
			if ((JCalendarPanel.this.btDaySelect != null) && (JCalendarPanel.this.colorDefaut != null)) {
				JCalendarPanel.this.btDaySelect.setBackground(JCalendarPanel.this.colorDefaut);
				JCalendarPanel.this.btDaySelect = null;
				JCalendarPanel.this.iNumDaySelect = -1;
				JCalendarPanel.this.iMonthSelect = -1;
				JCalendarPanel.this.iYearSelect = -1;
			}
		}
	};

	public JCalendarPanel(int annee, int mois, int jour)
	{
		this.listener = new MngDayButtons();

		this.iNumDaySelect = jour;
		this.iMonthSelect = mois;
		this.iYearSelect = annee;

		setLayout(new FlowLayout());

		JPanel panelMain = new JPanel(new BorderLayout());

		JPanel monthPanel = new JPanel();
		JButton previous = new JButton("<");
		previous.addActionListener(this._changeMonth);
		JButton next = new JButton(">");
		next.addActionListener(this._changeMonth);
		next.setActionCommand("next");
		this._monthYear.setPreferredSize(new Dimension(120, 20));
		monthPanel.add(previous);
		monthPanel.add(this._monthYear);
		monthPanel.add(next);
		panelMain.add(monthPanel, "North");

		JPanel dayPanel = new JPanel();
		dayPanel.setLayout(new GridLayout(7, 7));
		for (int i = 0; i < 7; i++)
			dayPanel.add(this._daysOfWeek[i] = new JLabel("", SwingUtilities.CENTER));
		for (int i = 0; i < this._daysNumber.length; i++) {
			this._daysNumber[i] = new JButton();
			this._daysNumber[i].setMargin(new Insets(1, 1, 1, 1));
			this._daysNumber[i].addMouseListener(this.listener);
			dayPanel.add(this._daysNumber[i]);
		}

		//Chargement de la couleur par défaut
		this.colorDefaut = this._daysNumber[0].getBackground();

		panelMain.add(dayPanel, "Center");

		add(panelMain);

		//Recherche de la date
		boolean bInit = false;
		if ((annee >= 0) && (mois >= 0) && (jour > 0)) {
			this._calendar = new GregorianCalendar(annee, mois, jour);
			bInit = true;
		}

		// Remplissage des composants
		updateMonthYear();
		updateDaysOfWeek();
		updateDaysNumber(bInit);

		// Mise à jour du jour courant
		if (bInit)
			setJour(jour, mois, annee);
	}

	// Affiche le mois et l'année en cours
	private void updateMonthYear()
	{
		this._monthYear.setText(this._formatMY.format(this._calendar.getTime()));
	}

	// Affiche les jours de la semaine
	private void updateDaysOfWeek()
	{
		String[] weekDays = this._dateSymbols.getShortWeekdays();
		for (int i = 1; i < weekDays.length; i++) {
			int index = (i - 2 + this._firstDayOfWeek) % 7 + 1;
			this._daysOfWeek[(i - 1)].setText(weekDays[index]);
		}
	}

	private void updateDaysNumber(boolean bInit)
	{
		Date tmp = this._calendar.getTime();
		this._calendar.set(5, 1);
		int firstDay = this._calendar.get(7);
		int LocalFirstDay = (firstDay - this._firstDayOfWeek + 7) % 7 + 1;
		boolean full = false;
		for (int i = 0; i < this._daysNumber.length; i++)
		{
			boolean isNotEmpty = (i < LocalFirstDay - 1) || (full);
			this._daysNumber[i].setVisible(!isNotEmpty);

			if (!isNotEmpty) {
				int dayOfMonth = this._calendar.get(5);
				this._daysNumber[i].setText(String.valueOf(dayOfMonth));
				if ((bInit) && (dayOfMonth == this.iNumDaySelect)) {
					this.btDaySelect = this._daysNumber[i];
				}
				this._calendar.add(5, 1);
				full = 1 == this._calendar.get(5);
			}
		}
		this._calendar.setTime(tmp);
	}

	/**
	 * Met à jour les attributs du calendrier pour sélectionner
	 * le jour voulu
	 * 
	 * @param numDay
	 * @param monthSelected
	 * @param yearSelected
	 */
	public void setJour(int numDay, int monthSelected, int yearSelected)
	{
		this.iNumDaySelect = numDay;
		this.iMonthSelect = monthSelected;
		this.iYearSelect = yearSelected;
		this.btDaySelect.setBackground(Color.RED);
	}
	public class MngDayButtons extends MouseAdapter {
		public MngDayButtons() {
		}
		public void mouseReleased(MouseEvent evt) {
			JButton btSelected = (JButton)evt.getSource();

			// Si le bouton n'était pas vide
			if (JCalendarPanel.this.btDaySelect != null) {
				JCalendarPanel.this.btDaySelect.setBackground(JCalendarPanel.this.colorDefaut);
			}

			//Si ce n'est pas le même bouton que l'on a sélectionné
			if (JCalendarPanel.this.btDaySelect != btSelected) {
				JCalendarPanel.this.btDaySelect = btSelected;

				// Récupération du numéro de jour
				String stNumDay = JCalendarPanel.this.btDaySelect.getText();

				//Mise à jour des attributs
				int numDay = Integer.parseInt(stNumDay);
				int monthSelected = JCalendarPanel.this._calendar.get(2);
				int yearSelected = JCalendarPanel.this._calendar.get(1);
				JCalendarPanel.this.setJour(numDay, monthSelected, yearSelected);
			}
		}
	}
}