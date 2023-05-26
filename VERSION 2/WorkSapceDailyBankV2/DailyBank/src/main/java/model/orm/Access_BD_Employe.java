package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.data.Client;
import model.data.Employe;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 * Classe d'accès aux Employe en BD Oracle.
 */
public class Access_BD_Employe {

	public Access_BD_Employe() {
	}

	/**
	 * Recherche d'un employé par son login / mot de passe.
	 *
	 * @param login    login de l'employé recherché
	 * @param password mot de passe donné
	 * @return un Employe ou null si non trouvé
	 * @throws RowNotFoundOrTooManyRowsException La requête renvoie plus de 1 ligne
	 * @throws DataAccessException               Erreur d'accès aux données (requête
	 *                                           mal formée ou autre)
	 * @throws DatabaseConnexionException        Erreur de connexion
	 */
	public Employe getEmploye(String login, String password)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {

		Employe employeTrouve;

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM Employe WHERE" + " login = ?" + " AND motPasse = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, login);
			pst.setString(2, password);

			ResultSet rs = pst.executeQuery();

			System.err.println(query);

			if (rs.next()) {
				int idEmployeTrouve = rs.getInt("idEmploye");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String droitsAccess = rs.getString("droitsAccess");
				String loginTROUVE = rs.getString("login");
				String motPasseTROUVE = rs.getString("motPasse");
				int idAgEmploye = rs.getInt("idAg");

				employeTrouve = new Employe(idEmployeTrouve, nom, prenom, droitsAccess, loginTROUVE, motPasseTROUVE,
						idAgEmploye);
			} else {
				rs.close();
				pst.close();
				// Non trouvé
				return null;
			}

			if (rs.next()) {
				// Trouvé plus de 1 ... bizarre ...
				rs.close();
				pst.close();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.SELECT,
						"Recherche anormale (en trouve au moins 2)", null, 2);
			}
			rs.close();
			pst.close();
			return employeTrouve;
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.SELECT, "Erreur accès", e);
		}
	}
	
	public void insertEmploye(Employe employe)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {

			Connection con = LogToDatabase.getConnexion();

			String query = "INSERT INTO EMPLOYE VALUES (" + "seq_id_employe.NEXTVAL" + ", " + "?" + ", " + "?" + ", "
					+ "?" + ", " + "?" + ", " + "?" + ", " + "?"  + ")";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, employe.nom);
			pst.setString(2, employe.prenom);
			pst.setString(3, employe.droitsAccess);
			pst.setString(4, employe.login);
			pst.setString(5, employe.motPasse);
			pst.setInt(6, employe.idAg);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();

			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.INSERT,
						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
			}

			query = "SELECT seq_id_employe.CURRVAL from DUAL";

			System.err.println(query);
			PreparedStatement pst2 = con.prepareStatement(query);

			ResultSet rs = pst2.executeQuery();
			rs.next();
			int numEmpBase = rs.getInt(1);

			con.commit();
			rs.close();
			pst2.close();

			employe.idEmploye = numEmpBase;
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.INSERT, "Erreur accès", e);
		}
	}
	
	public void supprEmploye(int idEmploye)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {

			Connection con = LogToDatabase.getConnexion();

			String query = "DELETE FROM EMPLOYE WHERE idEmploye = ?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idEmploye);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();

			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.Employe, Order.INSERT,
						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
			
		} catch (SQLException e) {
			throw new DataAccessException(Table.Employe, Order.INSERT, "Erreur accès", e);
		}
	}

	public void updateEmploye(Employe employe)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			Connection con = LogToDatabase.getConnexion();

			String query = "UPDATE EMPLOYE SET " + "nom = " + "? , " + "prenom = " + "? , " + "droitsAccess = "
					+ "? , " + "login = " + "? , " + "motPasse = " + "? , " + "idAg = " + "? " + " "
					+ "WHERE idEmploye = ? ";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, employe.nom);
			pst.setString(2, employe.prenom);
			pst.setString(3, employe.droitsAccess);
			pst.setString(4, employe.login);
			pst.setString(5, employe.motPasse);
			pst.setInt(6, employe.idAg);
			pst.setInt(7, employe.idEmploye);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();
			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.Client, Order.UPDATE,
						"Update anormal (update de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Client, Order.UPDATE, "Erreur accès", e);
		}
	}

	public ArrayList<Employe> getEmployes(int idEmploye, String debutNom, String debutPrenom)
			throws DataAccessException, DatabaseConnexionException {

		ArrayList<Employe> alResult = new ArrayList<>();
		
		try {
			Connection con = LogToDatabase.getConnexion();

			PreparedStatement pst;

			String query;
			if (idEmploye != -1) {
				query = "SELECT * FROM EMPLOYE where idEmploye = ?";
				query += " ORDER BY nom";
				pst = con.prepareStatement(query);
				pst.setInt(1, idEmploye);

			} else if (!debutNom.equals("") || !debutPrenom.equals("")) {
				debutNom = debutNom.toUpperCase() + "%";
				debutPrenom = debutPrenom.toUpperCase() + "%";
				query = "SELECT * FROM EMPLOYE";
				query += " WHERE UPPER(nom) like ?" + " AND UPPER(prenom) like ?";
				query += " ORDER BY nom";
				pst = con.prepareStatement(query);
				pst.setString(1, debutNom);
				pst.setString(2, debutPrenom);
			} else {
				query = "SELECT * FROM EMPLOYE";
				query += " ORDER BY nom";
				pst = con.prepareStatement(query);
			}
			System.err.println(query + " nom : " + debutNom + " prenom : " + debutPrenom + "");

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idEmployeTR = rs.getInt("idEmploye");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String droitsAccess = rs.getString("droitsAccess");
				String login = rs.getString("login");
				login = (login == null ? "" : login);
				String motPasse = rs.getString("motPasse");
				motPasse = (motPasse == null ? "" : motPasse);
				int idAg = rs.getInt("idAg");

				alResult.add(
						new Employe(idEmployeTR, nom, prenom, droitsAccess, login, motPasse, idAg));
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.Client, Order.SELECT, "Erreur accès", e);
		}

		return alResult;
	}
}
