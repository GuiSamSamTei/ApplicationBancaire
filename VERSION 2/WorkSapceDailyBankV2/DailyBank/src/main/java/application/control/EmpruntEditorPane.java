package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.EmpruntEditorPaneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;

public class EmpruntEditorPane {

	private Stage primaryStage;
	private EmpruntEditorPaneController eepViewController;
	private Client clientDesEmprunts;

	/**
	 * Constructeur de la classe EmpruntEditorPane
	 *
	 * @param _parentStage IN : Stage parent
	 * @param _dbstate     IN : Etat de l'application
	 */
	public EmpruntEditorPane(Stage _parentStage, DailyBankState _dbstate, Client _client) {
		this.clientDesEmprunts = _client;
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
			this.primaryStage.setTitle("Edition emprunt");
			this.primaryStage.setResizable(false);

			this.eepViewController = loader.getController();
			this.eepViewController.initContext(this.primaryStage, this, _dbstate, this.clientDesEmprunts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de création d'un emprunt et son assurance
	 */
	public void doEmpruntEditorPaneDialog() {
		this.eepViewController.displayDialog();
	}
}