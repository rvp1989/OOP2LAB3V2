package tenkovska.igra;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class Igra extends Frame {
	boolean igraRezim = true;
	private Panel meniPanel;
	private Mreza mrezaPanel;
	private Panel podlogaTekstPanel;
	private Panel izborPodlogePanel;
	private Panel meniDonjiPanel;

	private MenuBar meniBar;
	private Menu meni;
	private MenuItem rezimIzmene;
	private MenuItem rezimIgranja;
	
	private CheckboxGroup izborGrupa;
	private Checkbox travaIzbor;
	private Checkbox zidIzbor;
	
	private TextField brojNovcica;
	private Label novcica = new Label("Novcica: ");
	private Label labelP = new Label("Poena: ");
	private Button dugmePocni;
	private Label podlogaTekstLabel;
	private int ukupnoNovcica = 0;
	private boolean igraPokrenuta = false;
	
	public Igra(String name) {
		super(name);
		setSize(580, 500);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(NORMAL);
			}

		});
		postaviKomponente();
		setVisible(true);
	}
	
	//Postavljanje komponenti na ekranu
	//------------------------------------------------------------------------------------------------------

	private void postaviKomponente() {
		//kreiranje menadzera koji ce pratiti svaki klik na tastaturi
		KeyboardFocusManager menadzer = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		menadzer.addKeyEventDispatcher(new MojDispecer());
		
		setResizable(false);

		dodajMeni();
		dodajPodloguTeksta();
		dodajIzborPodloge();
		dodajMrezu();
		dodajMeniDonji();

		mrezaPanel.postaviRezimIgre(true);
		mrezaPanel.postaviIzborPolja(true);
	}
	
	private void dodajMeni() {
		meniPanel = new Panel();
		meniPanel.setPreferredSize(new Dimension(getWidth(), 20));

		meniBar = new MenuBar();
		meni = new Menu("Rezim");
		rezimIzmene = new MenuItem("Rezim izmene");
		rezimIzmene.addActionListener(e -> {
			if(igraPokrenuta)
				mrezaPanel.zavrsiIgru();
			mrezaPanel.postaviRezimIgre(false);
			mrezaPanel.ocistiTablu();
			dugmePocni.setEnabled(false);
		});

		rezimIgranja = new MenuItem("Rezim igranja");
		rezimIgranja.addActionListener(e -> {
			mrezaPanel.postaviRezimIgre(true);
			dugmePocni.setEnabled(true);
		});

		meni.add(rezimIzmene);
		meni.add(rezimIgranja);
		meniBar.add(meni);
		this.setMenuBar(meniBar);
	}

	private void dodajMrezu() {
		mrezaPanel = new Mreza(17, this);
		mrezaPanel.setPreferredSize(new Dimension(400, 500));
		this.add(mrezaPanel, BorderLayout.LINE_START);
	}

	private void dodajPodloguTeksta() {
		podlogaTekstPanel = new Panel();
		podlogaTekstLabel = new Label("Podloga: ");
		podlogaTekstPanel.setPreferredSize(new Dimension(70, 500));
		podlogaTekstPanel.setLayout(new BorderLayout());
		podlogaTekstPanel.add(podlogaTekstLabel, BorderLayout.CENTER);

		this.add(podlogaTekstPanel, BorderLayout.CENTER);
	}

	private void dodajIzborPodloge() {
		izborPodlogePanel = new Panel();
		izborGrupa = new CheckboxGroup();

		travaIzbor = new Checkbox("Trava", izborGrupa, true);
		travaIzbor.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (travaIzbor.getState())
					mrezaPanel.postaviIzborPolja(true);
			}

		});
		Panel travaPanel = new Panel();
		travaPanel.setBackground(Color.GREEN);
		travaPanel.setLayout(new BorderLayout());
		travaPanel.add(travaIzbor, BorderLayout.CENTER);

		zidIzbor = new Checkbox("Zid", izborGrupa, false);
		zidIzbor.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (zidIzbor.getState())
					mrezaPanel.postaviIzborPolja(false);
			}

		});
		Panel zidPanel = new Panel();
		zidPanel.setBackground(Color.LIGHT_GRAY);
		zidPanel.setLayout(new BorderLayout());
		zidPanel.add(zidIzbor, BorderLayout.CENTER);

		izborPodlogePanel.setLayout(new GridLayout(2, 1));
		izborPodlogePanel.add(travaPanel);
		izborPodlogePanel.add(zidPanel);
		izborPodlogePanel.setPreferredSize(new Dimension(92, 500));

		this.add(izborPodlogePanel, BorderLayout.LINE_END);

	}

	private void dodajMeniDonji() {
		meniDonjiPanel = new Panel();
		meniDonjiPanel.setLayout(new FlowLayout());

		brojNovcica = new TextField();
		brojNovcica.setPreferredSize(new Dimension(30, 20));
		brojNovcica.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				String unos = brojNovcica.getText();
				if (unos.matches("\\d+")) {
					ukupnoNovcica = Integer.parseInt(unos);
				} else {
					brojNovcica.setText("");
				}
			}
		});

		brojNovcica.setVisible(true);
		dugmePocni = new Button("Pocni");
		dugmePocni.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mrezaPanel.pokreniIgru(ukupnoNovcica);
				igraPokrenuta = true;
				labelP.setText("Poena: ");
			}

		});

		labelP.setPreferredSize(new Dimension(80, 20));
		meniDonjiPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		meniDonjiPanel.add(novcica);
		meniDonjiPanel.add(brojNovcica);
		meniDonjiPanel.add(labelP);
		meniDonjiPanel.add(dugmePocni);
		this.add(meniDonjiPanel, BorderLayout.PAGE_END);
	}
	//------------------------------------------------------------------------------------------------------

	public void igraZavrsena() {
		igraPokrenuta = false;
	}
	
	public int dohvatiUkupnoNovcica() {
		return ukupnoNovcica;
	}

	public void postaviBrojPoena(int brojPoena) {
		labelP.setText("Poena: " + brojPoena);
	}

	private void dugmeStisnuto(KeyEvent e) {
		String unos = e.getKeyChar() + "";
		if (unos.matches("[wasdWASD]")) {
			mrezaPanel.pomeriIgraca(unos);
		}
	}

	//Da bi 'frame' detektovao svaki klik na tastaturi, mora da se implementira KeyEventDispatcher
	private class MojDispecer implements KeyEventDispatcher {

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED && igraPokrenuta) {
				dugmeStisnuto(e);
			}
			return false;
		}

	}
	
	public static void main(String[] args) {
		new Igra("Tenkovska igra").setVisible(true);
	}
	
	
}