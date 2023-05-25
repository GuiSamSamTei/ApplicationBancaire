package model.data;

/**
 * Classe AssuranceEmprunt
 * 
 * @author Bastien RECORD
 */
public class AssuranceEmprunt {

	public int idAssurance;
	public double tauxAssurance;
	public double tauxCouv;
	public int idEmprunt;

	public AssuranceEmprunt(int idAss, double tauxAss, double tauxCouv, int idEmp) {
		this.idAssurance = idAss;
		this.tauxAssurance = tauxAss / 100;
		this.tauxCouv = tauxCouv;
		this.idEmprunt = idEmp;
	}

	@Override
	public String toString() {
		return "[" + this.idAssurance + "] Taux assurance = " + this.tauxAssurance + " Taux Couv" + this.tauxCouv;
	}
}