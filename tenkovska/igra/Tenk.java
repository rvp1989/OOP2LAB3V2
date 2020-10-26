package tenkovska.igra;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tenk extends Figura implements Runnable {
	
	private List<Polje> sveTrave;
	private boolean exit = true;
	private Thread t;
	
	public Tenk(List<Polje> sveTrave) {
		this.sveTrave = sveTrave;
		t = new Thread(this);
		exit = false;
		t.start();
	}
	
	@Override
	public void nacrtajFiguru(Polje polje, Graphics g) {
		postaviTrenutnoPolje(polje);
		
		int sirina = polje.getWidth();
		int visina = polje.getHeight();
		 
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, sirina, visina);
		g.drawLine(0, visina, sirina, 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public void run() {
		while(!exit) {
			try {
				Thread.sleep(500);
				promeniPoziciju();
			} catch (InterruptedException e) {
				//ne radi nista
			}
		}
	}
	
	public void ugasi() {
		exit = true;
		t.interrupt();
		t = null;
	}
	
	private void promeniPoziciju() {
		int x = dohvatiTrenutnoPolje().dohvatiPoziciju()[0];
		int y = dohvatiTrenutnoPolje().dohvatiPoziciju()[1];
		List<Polje> mogucaPolja = new ArrayList<Polje>();
		for (Polje p : sveTrave) {
			int moguceX = p.dohvatiPoziciju()[0];
			int moguceY = p.dohvatiPoziciju()[1];
			if (x == moguceX - 1 || x == moguceX + 1 || x == moguceX) {
				if (y == moguceY - 1 || y == moguceY + 1 || y == moguceY) {
					if(x == moguceX && x == moguceY)
						continue;
					mogucaPolja.add(p);
				}
			}
		}
		if (mogucaPolja.size() != 0) {
			int slucajno = new Random().nextInt(mogucaPolja.size());
			Polje novoPolje = mogucaPolja.get(slucajno);
			Polje staroPolje = this.dohvatiTrenutnoPolje();
			staroPolje.ukloniFiguru(this);
			novoPolje.dodajFiguru(this);
		}
	}
	
}
