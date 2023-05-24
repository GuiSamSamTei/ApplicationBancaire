// Emprunt et assurance d'emprunt : Julie BAELEN et Bastien RECORD

package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.EmpruntsManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Emprunt;
import model.orm.Access_BD_Emprunt;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.Order;
import model.orm.exception.Table;

public class EmpruntsManagement {

	private Stage primaryStage;
	private EmpruntsManagementController emcViewController;
	private DailyBankState dailyBankState;
	private Client clientDesEmprunt;

	/**
	 * Constructeur de la classe EmpruntsManagement
	 * 
	 * @param _parentStage IN : Stage parent
	 * @param _dbstate     IN : Etat de l'application
	 */
	public EmpruntsManagement(Stage _parentStage, DailyBankState _dbstate, Client client) {

		this.clientDesEmprunt = client;
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					EmpruntsManagementController.class.getResource("empruntsmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des emprunts");
			this.primaryStage.setResizable(false);

			this.emcViewController = loader.getController();
			this.emcViewController.initContext(this.primaryStage, this, _dbstate, client);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des comptes
	 */
	public void doEmpruntsManagementDialog() {
		this.emcViewController.displayDialog();
	}

	/**
	 * Affiche la fenêtre de gestion des comptes
	 * 
	 * @param cpt IN : Compte à gérer
	 */
	public void gererOperationsDUnCompte(CompteCourant cpt) {
		OperationsManagement om = new OperationsManagement(this.primaryStage, this.dailyBankState,
				this.clientDesEmprunt, cpt);
		om.doOperationsManagementDialog();
	}

	/**
	 * Affiche la fenêtre de gestion des comptes
	 * 
	 * @author Guilherme SAMPAIO
	 * 
	 * @param cpt IN : Compte à gérer
	 * 
	 * @return le nouveau compte
	 */
//	public CompteCourant creerNouveauCompte() {
//		CompteCourant compte;
//		CompteEditorPane cep = new CompteEditorPane(this.primaryStage, this.dailyBankState);
//		compte = cep.doCompteEditorDialog(this.clientDesEmprunt, null, EditionMode.CREATION);
//		if (compte != null) {
//			try {
//				Access_BD_CompteCourant acc = new Access_BD_CompteCourant();
//				acc.createCompteCourant(compte);
//			} catch (DatabaseConnexionException e) {
//				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
//				ed.doExceptionDialog();
//				this.primaryStage.close();
//			} catch (ApplicationException ae) {
//				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
//				ed.doExceptionDialog();
//			}
//		}
//		return compte;
//	}

	/**
	 * Affiche la fenêtre de gestion des emprunt
	 * 
	 * @return une liste d'emprunts d'un client
	 */
	public ArrayList<Emprunt> getEmpruntDunClient() {
		ArrayList<Emprunt> listeCpt = new ArrayList<>();

		try {
			Access_BD_Emprunt acc = new Access_BD_Emprunt();
			listeCpt = acc.getEmprunts(this.clientDesEmprunt.idNumCli);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeCpt = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listeCpt = new ArrayList<>();
		}
		return listeCpt;
	}
}
