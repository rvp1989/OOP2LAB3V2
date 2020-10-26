package tenkovska.igra;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class Zid extends Polje {
	public Zid(Integer[] pozicija, Mreza kontroler) {
		super(pozicija, false);
		this.setBackground(Color.GRAY);
		Polje polje = this;
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				kontroler.promeniPolje(polje);
			}
			
		});
	}

	@Override
	public void dodajFiguru(Figura figura) {
		// zid ne moze da ima figuru
	}

	@Override
	public void ukloniFiguru(Figura figura) {
		// zid ne moze da ima figuru
	}
	
}
