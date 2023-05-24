package model.orm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
				int taux = rs.getInt("tauxEmp");
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
	public void insertEmprunt(Emprunt emp) throws RowNotFoundOrTooManyRowsException, DataAccessException,
			DatabaseConnexionException, ManagementRuleViolation {
		try {

			Connection con = LogToDatabase.getConnexion();

			String query = "INSERT INTO Emprunt(idEmprunt, tauxEmp, capitalEmp, dureeEmp, dateDebEmp, idNumCli) VALUES ("
					+ "seq_id_emprunt.NEXTVAL" + ", " + "?" + ", " + "?" + ", " + "?" + ", " + "?" + ", " + "?" + ")";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, emp.tauxApp);
			pst.setInt(2, emp.capital);
			pst.setInt(3, emp.duree);
			pst.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
			pst.setInt(5, emp.idClient);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();

			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.Client, Order.INSERT,
						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
			} else {
				con.commit();
			}

		} catch (SQLException e) {
			throw new DataAccessException(Table.Client, Order.INSERT, "Erreur accès", e);
		}
	}
}
