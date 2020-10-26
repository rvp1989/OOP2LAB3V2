package tenkovska.igra;

import java.awt.Graphics;

public abstract class Figura {

	private Polje trenutnoPolje;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Figura other = (Figura) obj;
		if (trenutnoPolje != other.dohvatiTrenutnoPolje())
			return false;
		return true;
	}
	
	//svaka figura drugacije implementira crtanje
	public abstract void nacrtajFiguru(Polje polje, Graphics g);
	
	public Polje dohvatiTrenutnoPolje() {
		return trenutnoPolje;
	}
	
	void postaviTrenutnoPolje(Polje trenutnoPolje) {
		this.trenutnoPolje = trenutnoPolje;
	}
	
}
