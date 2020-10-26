package tenkovska.igra;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public abstract class Polje extends Canvas {

	protected Integer[] pozicija;
	private boolean daLiJeTrava;
	protected CopyOnWriteArrayList<Figura> figure;

	public Polje(Integer[] pozicija, boolean daLiJeTrava) {
		setName("[" + pozicija[0] + "-" + pozicija[1] + "]");
		this.pozicija = pozicija;
		this.daLiJeTrava = daLiJeTrava;
		figure = new CopyOnWriteArrayList<Figura>();
	}

	public Integer[] dohvatiPoziciju() {
		return pozicija;
	}

	public boolean dozvoljenaFigura(Figura figura) {
		if (daLiJeTrava)
			return true;
		return false;
	}

	public boolean trava() {
		return daLiJeTrava;
	}

	@Override
	public void paint(Graphics g) {
		for (Figura f : figure)
			f.nacrtajFiguru(this, g);
	}

	public abstract void dodajFiguru(Figura figura);

	public abstract void ukloniFiguru(Figura figura);

	public List<Figura> dohvatiTrenutneFigure() {
		return figure;
	}
	
}
