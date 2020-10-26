package tenkovska.igra;

import java.awt.GridLayout;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class Mreza extends Panel implements Runnable {
	private Thread t;
	private volatile boolean igraSe = false;
	private int ukupnoPoena = 0;

	private boolean igraRezim;
	private boolean izborTrava;

	private Igra igraKontroler;
	private Map<Integer[], Polje> mapaPolja;
	private int brojKolona;
	private int brojPolja;
	private int brojTrava;
	private int brojZidova;
	private Random random = new Random();

	private List<Figura> novcici;
	private List<Figura> tenkovi;
	private Figura igrac;
	private static int c = 5;

	public Mreza(int brojKolona, Igra igraKontroler) {
		this.igraKontroler = igraKontroler;
		this.brojKolona = brojKolona;
		brojPolja = brojKolona * brojKolona;
		brojTrava = brojPolja * 80 / 100;
		brojZidova = brojPolja - brojTrava;
		mapaPolja = new LinkedHashMap<Integer[], Polje>();
		initMreza();
		c++;
	}

	// poslovi koje mreza radi kao nit
	// ------------------------------------------------------------------------------------------------------
	@Override
	public void run() {
		while (igraSe) {
			proveri();
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// ne radi nista
			}
		}
	}

	public void pokreniIgru(int brojNovcica) {
		if (brojNovcica > 0) {
			ocistiTablu();
			postaviTablu(brojNovcica);
			igraSe = true;
			this.t = new Thread(this);
			t.start();
		}
	}

	public void zavrsiIgru() {
		igraKontroler.postaviBrojPoena(ukupnoPoena);
		for (Figura tenk : tenkovi) {
			Tenk t = (Tenk) tenk;
			t.ugasi();
		}
		igraSe = false;
		t.interrupt();
		ukupnoPoena = 0;
		igraKontroler.igraZavrsena();
	}

	private void proveri() {
		Polje trenutnoPolje = igrac.dohvatiTrenutnoPolje();
		List<Figura> trenutneFigureNaPolju = trenutnoPolje.dohvatiTrenutneFigure();
		for (Figura figura : trenutneFigureNaPolju) {
			if (figura instanceof Tenk) {
				trenutnoPolje.ukloniFiguru(igrac);
				zavrsiIgru();
				igraKontroler.postaviBrojPoena(ukupnoPoena);
				break;
			} else if (figura instanceof Novcic) {
				ukupnoPoena++;
				trenutnoPolje.ukloniFiguru(figura);
				novcici.remove(figura);
				igraKontroler.postaviBrojPoena(ukupnoPoena);
				if (novcici.size() == 0) {
					trenutnoPolje.ukloniFiguru(igrac);
					zavrsiIgru();
					break;
				}
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------

	// inicijalizacija mreze
	// ------------------------------------------------------------------------------------------------------
	private void initMreza() {
		setLayout(new GridLayout(brojKolona, brojKolona));
		int ukupnoTrave = 0;
		int ukupnoZidova = 0;
		for (int i = 0; i < brojKolona; i++) {
			for (int j = 0; j < brojKolona; j++) {
				Integer[] pozicija = new Integer[] { i, j };
				int rand = random.nextInt(100);
				Polje polje = null;
				if (rand < 80) {
					if (ukupnoTrave <= brojTrava) {
						ukupnoTrave++;
						polje = new Trava(pozicija, this);
					} else {
						ukupnoZidova++;
						polje = new Zid(pozicija, this);
					}
				} else {
					if (ukupnoZidova <= brojZidova) {
						ukupnoZidova++;
						polje = new Zid(pozicija, this);
					} else {
						ukupnoTrave++;
						polje = new Trava(pozicija, this);
					}
				}
				mapaPolja.put(pozicija, polje);
			}
		}

		postaviMrezu();
	}

	private void postaviMrezu() {
		this.removeAll();
		this.setLayout(new GridLayout(brojKolona, brojKolona));
		Set<Integer[]> kljucevi = mapaPolja.keySet();
		for (Integer[] kljuc : kljucevi) {
			Polje p = mapaPolja.get(kljuc);
			add(p);
			p.revalidate();
		}
	}

	public void postaviTablu(int brojNovcica) {
		postaviNovcice(brojNovcica);
		postaviTenkove(brojNovcica / 3);
		postaviIgraca();
	}

	public void ocistiTablu() {
		if (igraSe)
			zavrsiIgru();
		Polje p = null;
		if (novcici != null) {
			for (Figura novcic : novcici) {
				p = novcic.dohvatiTrenutnoPolje();
				p.ukloniFiguru(novcic);
			}

			novcici.clear();
		} else {
			novcici = new ArrayList<Figura>();
		}

		if (tenkovi != null) {
			for (Figura tenk : tenkovi) {
				p = tenk.dohvatiTrenutnoPolje();
				p.ukloniFiguru(tenk);
			}
			tenkovi.clear();
		} else {
			tenkovi = new ArrayList<Figura>();
		}

		if (igrac != null) {
			p = igrac.dohvatiTrenutnoPolje();
			if (p != null)
				p.ukloniFiguru(igrac);
		} else {
			igrac = new Igrac();
		}
	}

	// ------------------------------------------------------------------------------------------------------

	public void promeniPolje(Polje staro) {
		if (!igraRezim) {
			if (staro == null)
				return;
			Polje polje;

			if (izborTrava) {
				polje = new Trava(staro.dohvatiPoziciju(), this);
			} else {
				polje = new Zid(staro.dohvatiPoziciju(), this);
			}
			mapaPolja.replace(staro.dohvatiPoziciju(), polje);
			postaviMrezu();
		}
	}

	public void pomeriIgraca(String tipka) {
		int x = igrac.dohvatiTrenutnoPolje().dohvatiPoziciju()[0];
		int y = igrac.dohvatiTrenutnoPolje().dohvatiPoziciju()[1];

		if (tipka.equalsIgnoreCase("w"))
			x -= 1;
		else if (tipka.equalsIgnoreCase("a"))
			y -= 1;
		else if (tipka.equalsIgnoreCase("s"))
			x += 1;
		else if (tipka.equalsIgnoreCase("d"))
			y += 1;

		Polje novoPolje = null;
		List<Polje> polja = dohvatiSveTrave();
		for (Polje p : polja) {
			if (x == p.dohvatiPoziciju()[0] && y == p.dohvatiPoziciju()[1]) {
				novoPolje = p;
				break;
			}
		}

		if (novoPolje != null) {
			Polje staroPolje = igrac.dohvatiTrenutnoPolje();
			staroPolje.ukloniFiguru(igrac);
			novoPolje.dodajFiguru(igrac);
		}
	}

	private void postaviNovcice(int brojNovcica) {
		for (int i = 0; i < brojNovcica; i++) {
			Polje polje = slucajnoPolje(dohvatiSveTrave());
			if (polje != null) {
				Figura novcic = new Novcic();
				polje.dodajFiguru(novcic);
				novcici.add(novcic);
			}
		}
	}

	private void postaviTenkove(int brojTenkova) {
		for (int i = 0; i < brojTenkova; i++) {
			postaviTenk();
		}
	}

	private void postaviTenk() {
		Polje polje = slucajnoPolje(dohvatiSveTrave());
		if (polje != null) {
			Figura tenk = new Tenk(dohvatiSveTrave());
			polje.dodajFiguru(tenk);
			tenkovi.add(tenk);
		}
	}

	private boolean postaviIgraca() {
		Polje polje = slucajnoPolje(dohvatiSveSlobodneTrave());
		if (polje != null) {
			polje.dodajFiguru(igrac);
			igrac.postaviTrenutnoPolje(polje);
			return true;
		}
		return false;
	}

	private List<Polje> dohvatiSveTrave() {
		return mapaPolja.values().stream().filter(x -> x.trava() == true ? true : false).collect(Collectors.toList());
	}

	private List<Polje> dohvatiSveSlobodneTrave() {
		return mapaPolja.values().stream().filter(x -> x.trava() == true ? true : false).filter(p -> {
			List<Figura> figureNaPolju = p.dohvatiTrenutneFigure();
			for (Figura f : figureNaPolju)
				if (f instanceof Tenk || f instanceof Novcic)
					return false;
			return true;
		}).collect(Collectors.toList());
	}

	private Polje slucajnoPolje(List<Polje> polja) {
		int slucajno = slucajanBroj(polja.size() - 1);
		if (slucajno == 0)
			return null;
		return polja.get(slucajno);
	}

	private int slucajanBroj(int ogranicenje) {
		return random.nextInt(ogranicenje);
	}

	public void postaviRezimIgre(boolean igraRezim) {
		this.igraRezim = igraRezim;
	}

	public void postaviIzborPolja(boolean izborTrava) {
		this.izborTrava = izborTrava;
	}

	public String toString() {
		return c + "";
	}
}
