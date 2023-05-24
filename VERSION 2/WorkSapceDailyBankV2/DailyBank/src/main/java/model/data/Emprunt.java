package model.data;

import java.util.Date;

/**
 * Classe Emprunt
 * 
 * @author Julie BAELEN
 */
public class Emprunt {

	public int id;
	public int tauxApp;
	public int capital;
	public int duree;
	public int idClient;
	public Date date;

	public Emprunt(int idEmprunt, int tauxApp, int capital, int duree, Date date, int idClient) {
		this.id = idEmprunt;
		this.tauxApp = tauxApp;
		this.capital = capital;
		this.duree = duree;
		this.date = date;
		this.idClient = idClient;
	}

	@Override
	public String toString() {
		return "[" + this.id + "] Capital = " + this.capital + "€ (" + this.date.toString() + ")";
	}
}