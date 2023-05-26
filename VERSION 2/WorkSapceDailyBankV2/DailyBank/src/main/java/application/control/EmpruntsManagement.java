// Emprunt et assurance d'emprunt : Julie BAELEN et Bastien RECORD

package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.EmpruntsManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.Emprunt;
import model.orm.Access_BD_Emprunt;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

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
	 * Affiche la fenêtre de gestion des emprunts
	 */
	public void doEmpruntsManagementDialog() {
		this.emcViewController.displayDialog();
	}

	/**
	 * Affiche la fenêtre de création d'un emprunt
	 *
	 * @author Julie BAELEN
	 */
	public void creerEmprunt() {
		EmpruntEditorPane eep = new EmpruntEditorPane(this.primaryStage, this.dailyBankState, this.clientDesEmprunt);
		eep.doEmpruntEditorPaneDialog();
	}

	/**
	 * Affiche la fenêtre de détails des emprunts
	 *
	 * @author Bastien RECORD
	 *
	 * @param emprunt IN : emprunt à détailler
	 */
	public void detailsEmprunt(Emprunt emprunt) {
		EmpruntDetails ed = new EmpruntDetails(this.primaryStage, this.dailyBankState, emprunt);
		ed.doEmpruntDetailsDialog();
	}

	/**
	 * Permet d'obtenir une liste d'emprunts d'un client
	 *
	 * @author Bastien RECORD
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
