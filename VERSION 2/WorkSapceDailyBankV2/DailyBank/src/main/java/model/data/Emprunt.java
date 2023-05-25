package model.data;

import java.util.Date;

/**
 * Classe Emprunt
 * 
 * @author Julie BAELEN
 */
public class Emprunt {

	public int idEmprunt;
	public double tauxApplicable;
	public int capitalEmprunt;
	public int dureeEmprunt;
	public int idClient;
	public Date dateDebEmprunt;

	public Emprunt(int idEmprunt, double tauxApp, int capital, int duree, Date date, int idClient) {
		this.idEmprunt = idEmprunt;
		this.tauxApplicable = tauxApp / 100;
		this.capitalEmprunt = capital;
		this.dureeEmprunt = duree;
		this.dateDebEmprunt = date;
		this.idClient = idClient;
	}

	@Override
	public String toString() {
		return "[" + this.idEmprunt + "] Capital = " + this.capitalEmprunt + "€ (" + this.dateDebEmprunt.toString()
				+ ")";
	}
}
