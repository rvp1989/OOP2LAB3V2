package tenkovska.igra;

import java.awt.Color;
import java.awt.Graphics;

public class Novcic extends Figura {
	
	@Override
	public void nacrtajFiguru(Polje polje, Graphics g) {
		postaviTrenutnoPolje(polje);
		
		int sirina = polje.getWidth();
		int visina = polje.getHeight();
		
		g.setColor(Color.YELLOW);
		g.drawOval(sirina/4, visina/4, sirina/2, visina/2);
		g.fillOval(sirina/4, visina/4, sirina/2, visina/2);
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
