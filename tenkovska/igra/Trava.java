package tenkovska.igra;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class Trava extends Polje {
	
	public Trava(Integer[] pozicija, Mreza kontroler) {
		super(pozicija, true);
		this.setBackground(Color.green);
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
		figure.add(figura);
		repaint();
	}

	@Override
	public void ukloniFiguru(Figura figura) {
		figure.remove(figura);
		repaint();
	}
	
}
