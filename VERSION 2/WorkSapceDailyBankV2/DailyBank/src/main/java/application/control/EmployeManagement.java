// CRUD employé : Guilherme SAMPAIO

package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientsManagementController;
import application.view.EmployeManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.Employe;
import model.orm.Access_BD_Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

public class EmployeManagement {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private EmployeManagementController emcViewController;

	/**
	 * Constructeur de la classe EmployeManagement
	 * 
	 * @param _parentStage IN : Stage parent
	 * @param _dbstate     IN : Etat de l'application
	 */
	public EmployeManagement(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployeManagementController.class.getResource("employemanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des employés");
			this.primaryStage.setResizable(false);

			this.emcViewController = loader.getController();
			System.out.println(emcViewController.getI());
			this.emcViewController.initContext(this.primaryStage, this, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des clients
	 * 
	 * @author Guilherme SAMPAIO
	 */
	public void doEmployeManagementDialog() {
		this.emcViewController.displayDialog();
	}

	/**
	 * Permet de supprimer un employé
	 * 
	 * @author Guilherme SAMPAIO
	 * 
	 * @param idEmploye IN : Employé à supprimer
	 */
	public void supprimerEmploye(int idEmploye) {

		try {
			Access_BD_Employe ac = new Access_BD_Employe();
			ac.supprEmploye(idEmploye);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();

		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
		}
	}

	/**
	 * Permet de créer un nouvel employé
	 * 
	 * @author Guilherme SAMPAIO
	 * 
	 * @return l'employé à créer
	 */
	public Employe nouveauEmploye() {
		Employe employe;
		EmployeEditorPane cep = new EmployeEditorPane(this.primaryStage, this.dailyBankState);
		employe = cep.doEmployeEditorDialog(null, EditionMode.CREATION);
		if (employe != null) {
			try {
				Access_BD_Employe ac = new Access_BD_Employe();

				ac.insertEmploye(employe);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				employe = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				employe = null;
			}
		}
		return employe;
	}

	/**
	 * Affiche la fenêtre de gestion des employé
	 * 
	 * @author Guilherme SAMPAIO
	 * 
	 * @param _idEmploye   IN : id de l'employé
	 * @param _debutNom    IN : le prénom de l'employé
	 * @param _debutPrenom IN : le nom de l'employé
	 * @return une liste d'employé
	 */
	public ArrayList<Employe> getlisteEmployes(int _idEmploye, String _debutNom, String _debutPrenom) {
		ArrayList<Employe> listeCli = new ArrayList<>();
		try {
			// Recherche des employes en BD. cf. AccessClient > getClients(.)
			// numCompte != -1 => recherche sur numCompte
			// numCompte == -1 et debutNom non vide => recherche nom/prenom
			// numCompte == -1 et debutNom vide => recherche tous les employes

			Access_BD_Employe ac = new Access_BD_Employe();
			listeCli = ac.getEmployes(_idEmploye, _debutNom, _debutPrenom);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeCli = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeCli = new ArrayList<>();
		}
		return listeCli;
	}

	/**
	 * Permet de modifier un employé
	 * 
	 * @author Guilherme SAMPAIO
	 * 
	 * @param empMod IN : l'employé à modifier
	 * @return l'employé modifié
	 */
	public Employe modifierEmploye(Employe empMod) {
		EmployeEditorPane cep = new EmployeEditorPane(this.primaryStage, this.dailyBankState);
		Employe result = cep.doEmployeEditorDialog(empMod, EditionMode.MODIFICATION);
		if (result != null) {
			try {
				Access_BD_Employe ac = new Access_BD_Employe();
				ac.updateEmploye(result);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				result = null;
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				result = null;
			}
		}
		return result;
	}
}