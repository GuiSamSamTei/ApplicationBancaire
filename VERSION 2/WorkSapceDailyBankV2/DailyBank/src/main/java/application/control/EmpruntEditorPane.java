package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.ClientsManagementController;
import application.view.EmpruntEditorPaneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EmpruntEditorPane {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private EmpruntEditorPaneController eepViewController;

	/**
	 * Constructeur de la classe EmpruntEditorPane
	 * 
	 * @param _parentStage IN : Stage parent
	 * @param _dbstate     IN : Etat de l'application
	 */
	public EmpruntEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(EmpruntEditorPaneController.class.getResource("emprunteditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Emprunt editor");
			this.primaryStage.setResizable(false);

			this.eepViewController = loader.getController();
			this.eepViewController.initContext(this.primaryStage, this, _dbstate);

			System.out.println(this.eepViewController);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des clients
	 */
	public void doEmpruntEditorPaneDialog() {
		this.eepViewController.displayDialog();
	}
}