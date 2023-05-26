package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.ExceptionDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.orm.exception.ApplicationException;

public class ExceptionDialog {

	private Stage primaryStage;
	private ExceptionDialogController edcViewController;

	/**
	 * Constructeur de la classe ExceptionDialog
	 *
	 * @param _parentStage IN : Stage parent
	 * @param _dbstate     IN : Etat de l'application
	 * @param ae           IN : Exception à afficher
	 */
	public ExceptionDialog(Stage _parentStage, DailyBankState _dbstate, ApplicationException ae) {

		try {
			FXMLLoader loader = new FXMLLoader(ExceptionDialogController.class.getResource("exceptiondialog.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, 700 + 20, 400 + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Opération impossible");
			this.primaryStage.setResizable(false);

			this.edcViewController = loader.getController();
			this.edcViewController.initContext(this.primaryStage, _dbstate, ae);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des clients
	 */
	public void doExceptionDialog() {
		this.edcViewController.displayDialog();
	}
}
