package model.orm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.text.DecimalFormat;
import model.data.AssuranceEmprunt;
import model.data.CompteCourant;
import model.data.Emprunt;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 *
 * Classe d'accès aux Emprunt en BD Oracle.
 * 
 * @author Bastien RECORD
 */
public class Access_BD_Emprunt {

	public Access_BD_Emprunt() {
	}

	/**
	 * Recherche des Emprunt d'un client à partir de son id.
	 *
	 * @param idNumCli IN : id du client dont on cherche les emprunts
	 * @return tous les emprunts de idNumCli (ou liste vide)
	 * @throws DataAccessException        Erreur d'accès aux données (requête mal
	 *                                    formée ou autre)
	 * @throws DatabaseConnexionException Erreur de connexion
	 */
	public ArrayList<Emprunt> getEmprunts(int idNumCli) throws DataAccessException, DatabaseConnexionException {
		DecimalFormat f = new DecimalFormat();
		f.setMaximumFractionDigits(2);

		ArrayList<Emprunt> alResult = new ArrayList<>();

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM Emprunt where idNumCli = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idNumCli);
			System.err.println(query);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idEmp = rs.getInt("idEmprunt");
				double taux = rs.getDouble("tauxEmp");
				int capital = rs.getInt("capitalEmp");
				int duree = rs.getInt("dureeEmp");
				int idClient = rs.getInt("idNumCli");
				Date date = rs.getDate("dateDebEmp");

				alResult.add(new Emprunt(idEmp, taux, capital, duree, date, idClient));
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Emprunt, Order.SELECT, "Erreur accès", e);
		}

		return alResult;
	}

	/**
	 * Création d'un CompteCourant.
	 * 
	 * @author Guilherme SAMPAIO
	 * @param compte IN compte.idNumCli doit exister
	 * @throws RowNotFoundOrTooManyRowsException La requête modifie 0 ou plus de 1
	 *                                           ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 * @throws ManagementRuleViolation           Erreur sur le solde courant par
	 *                                           rapport au débitAutorisé (solde <
	 *                                           débitAutorisé)
	 */
	public void insertEmprunt(Emprunt emp, AssuranceEmprunt assEmp) throws RowNotFoundOrTooManyRowsException,
			DataAccessException, DatabaseConnexionException, ManagementRuleViolation {

		DecimalFormat formatDouble = new DecimalFormat("#0.00");

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "INSERT INTO Emprunt(idEmprunt, tauxEmp, capitalEmp, dureeEmp, dateDebEmp, idNumCli) VALUES ("
					+ "seq_id_emprunt.NEXTVAL" + ", " + "?" + ", " + "?" + ", " + "?" + ", " + "?" + ", " + "?" + ")";
			PreparedStatement pst = con.prepareStatement(query);

			double test = Double.valueOf(formatDouble.format(emp.tauxApplicable).replaceAll(",", "."));

			pst.setDouble(1, test);
			pst.setInt(2, emp.capitalEmprunt);
			pst.setInt(3, emp.dureeEmprunt);
			pst.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
			pst.setInt(5, emp.idClient);

			System.err.println(query);

			int result = pst.executeUpdate();

			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.Emprunt, Order.INSERT,
						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
			} else {
				con.commit();
			}

			if (assEmp != null) {

				ArrayList<Emprunt> listTempEmp = this.getEmprunts(emp.idClient);

				int idEmp = 0;

				while (listTempEmp.size() > 0) {
					if (listTempEmp.get(0).capitalEmprunt == emp.capitalEmprunt
							&& listTempEmp.get(0).dateDebEmprunt.equals(emp.dateDebEmprunt)
							&& listTempEmp.get(0).dureeEmprunt == emp.dureeEmprunt) {
						idEmp = listTempEmp.get(0).idEmprunt;
						listTempEmp.clear();
					} else {
						listTempEmp.remove(0);
					}
				}

				query = "INSERT INTO AssuranceEmprunt (idAss, tauxAss, tauxCouv, idEmprunt) VALUES ("
						+ "seq_id_assurance.NEXTVAL" + ", " + "?" + ", " + "?" + ", " + "?" + ")";
				PreparedStatement pst2 = con.prepareStatement(query);
				pst2.setDouble(1, Double.valueOf(formatDouble.format(assEmp.tauxAssurance).replaceAll(",", ".")));
				pst2.setDouble(2, Double.valueOf(formatDouble.format(assEmp.tauxCouv).replaceAll(",", ".")));
				pst2.setInt(3, idEmp);

				System.err.println(query);

				int result1 = pst2.executeUpdate();

				if (result1 != 1) {
					con.rollback();
					throw new RowNotFoundOrTooManyRowsException(Table.AssuranceEmprunt, Order.INSERT,
							"Insert anormal (insert de moins ou plus d'une ligne)", null, result1);
				} else {
					con.commit();
				}

//				rs.close();
				pst.close();
//				pst1.close();
				pst2.close();
			}

		} catch (SQLException e) {
			throw new DataAccessException(Table.Emprunt, Order.INSERT, "Erreur accès", e);
		}
	}

	public AssuranceEmprunt getAssuranceEmprunt(int idEmprunt) throws DataAccessException, DatabaseConnexionException {
		AssuranceEmprunt result = null;

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM AssuranceEmprunt where idEmprunt = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idEmprunt);

			System.err.println(query);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				int idAssurance = rs.getInt("idAss");
				double tauxAssurance = rs.getDouble("tauxAss");
				double tauxCouverture = rs.getDouble("tauxCouv");

				result = new AssuranceEmprunt(idAssurance, tauxAssurance, tauxCouverture, idEmprunt);

			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.AssuranceEmprunt, Order.SELECT, "Erreur accès", e);
		}

		return result;
	}

}
