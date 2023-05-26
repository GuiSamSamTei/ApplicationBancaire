// CRUD Prélèvements : Guilherme SAMPAIO

package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.PrelEditorPaneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Prelevement;

public class PrelEditorPane {

	private Stage primaryStage;
	private PrelEditorPaneController pepcViewController;
	private DailyBankState dailyBankState;

	/**
	 * Constructeur de la classe PrelEditorPane
	 *
	 * @author Guilherme SAMPAIO
	 *
	 * @param _parentStage IN : stage parent
	 * @param _dbstate     IN : Etat de l'application
	 */
	public PrelEditorPane(Stage _parentStage, DailyBankState _dbstate) {
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(PrelEditorPaneController.class.getResource("preleditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 20, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion d'un prélèvement");
			this.primaryStage.setResizable(false);

			this.pepcViewController = loader.getController();
			this.pepcViewController.initContext(this.primaryStage, this.dailyBankState);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion d'un prélèvement
	 *
	 * @author Guilherme SAMPAIO
	 *
	 * @param prelevement IN : Prélèvement à gérer
	 * @param em          IN : Mode d'édition
	 * @return
	 */
	public Prelevement doPrelevementEditorDialog(Prelevement prelevement, EditionMode em) {
		return this.pepcViewController.displayDialog(prelevement, em);
	}
}
