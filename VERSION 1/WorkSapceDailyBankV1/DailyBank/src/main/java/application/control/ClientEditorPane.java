package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ClientEditorPaneController;
import application.view.ClientsManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;

public class ClientEditorPane {

	private Stage primaryStage;
	private ClientEditorPaneController cepcViewController;
	private DailyBankState dailyBankState;

	/**
	 * Constructeur de la classe ClientEditorPane
	 * 
	 * @param _parentStage IN : Stage parent
	 * @param _dbstate     IN : Etat de l'application
	 * 
	 * @return void
	 */
	public ClientEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(ClientsManagementController.class.getResource("clienteditorpane.fxml"));
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

			this.cepcViewController = loader.getController();
			this.cepcViewController.initContext(this.primaryStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion d'un client
	 * 
	 * @param client IN : Client à gérer
	 * @param em     IN : Mode d'édition
	 * 
	 * @return Client
	 */
	public Client doClientEditorDialog(Client client, EditionMode em) {
		return this.cepcViewController.displayDialog(client, em);
	}
}
