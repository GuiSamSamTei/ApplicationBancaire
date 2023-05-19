// CRUD employé : Guilherme SAMPAIO

package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.EmployeManagementController;
import application.view.ClientEditorPaneController;
import application.view.EmployeEditorPaneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.Employe;

public class EmployeEditorPane {

	private Stage primaryStage;
	private EmployeEditorPaneController eepcViewController;
	private DailyBankState dailyBankState;

	/**
	 * Constructeur de la classe EmployeEditorPane
	 * 
	 * @param _parentStage IN : stage parent
	 * @param _dbstate     IN : Etat de l'application
	 */
	public EmployeEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmployeManagementController.class.getResource("employeeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion d'un client");
			this.primaryStage.setResizable(false);

			this.eepcViewController = loader.getController();
			this.eepcViewController.initContext(this.primaryStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion d'un employé
	 * 
	 * @author Guilherme SAMPAIO
	 * 
	 * @param employe IN : Employé à gérer
	 * @param em      IN : Mode d'édition
	 * @return
	 */
	public Employe doEmployeEditorDialog(Employe employe, EditionMode em) {
		return this.eepcViewController.displayDialog(employe, em);
	}
}
