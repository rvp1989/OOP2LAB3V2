package tenkovska.igra;

import java.awt.Color;
import java.awt.Graphics;

public class Igrac extends Figura {
	
	@Override
	public void nacrtajFiguru(Polje polje, Graphics g) {
		postaviTrenutnoPolje(polje);
		
		int sirina = polje.getWidth();
		int visina = polje.getHeight();
		
		g.setColor(Color.RED);
		g.drawLine(sirina/2, 0, sirina/2, visina);
		g.drawLine(0, visina/2, sirina, visina/2);
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
	
}
