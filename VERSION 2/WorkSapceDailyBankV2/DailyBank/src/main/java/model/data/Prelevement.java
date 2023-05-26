package model.data;

/**
 * Attributs mis en public car cette classe ne fait que "véhiculer" des données.
 *
 * @author Guilherme SAMPAIO
 */
public class Prelevement {

	public int idPrelev;
	public double montant;
	public int dateRecurrente;
	public String beneficiaire;
	public int idNumCompte;

	public Prelevement(int idPrelev, double montant, int dateRecurrente, String beneficiaire, int idNumCompte) {
		super();
		this.idPrelev = idPrelev;
		this.montant = montant;
		this.dateRecurrente = dateRecurrente;
		this.beneficiaire = beneficiaire;
		this.idNumCompte = idNumCompte;

	}

	public Prelevement(Prelevement p) {
		this(p.idPrelev, p.montant, p.dateRecurrente, p.beneficiaire, p.idNumCompte);
	}

	public Prelevement() {
		this(-1000, 0, 1, "-1000", -1);
	}

	@Override
	public String toString() {
		return "[ " + this.idPrelev + " ] " + "Compte : " + String.valueOf(this.idNumCompte) + " Montant : "
				+ String.format("%10.02f", this.montant) + " E Bénéficiaire : " + this.beneficiaire
				+ " Date récurrente : " + String.valueOf(dateRecurrente);
	}
}
