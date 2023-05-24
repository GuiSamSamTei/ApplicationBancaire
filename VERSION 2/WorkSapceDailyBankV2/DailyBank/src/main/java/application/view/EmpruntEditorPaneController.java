package application.view;

import application.DailyBankState;
import application.control.EmpruntEditorPane;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EmpruntEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ClientsManagementController
	private EmpruntEditorPane eepDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre

	/**
	 * Manipulation de la fenêtre
	 * 
	 * @param _containingStage IN : Fenêtre physique ou est la scène contenant le
	 *                         fichier xml contrôlé par this
	 * @param _eep             IN : Contrôleur de Dialogue associé à
	 *                         EmpruntEditorPaneController
	 * @param _dbstate         IN : Etat courant de l'application
	 */
	public void initContext(Stage _containingStage, EmpruntEditorPane _eep, DailyBankState _dbstate) {
		this.eepDialogController = _eep;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Validation de l'état des composants
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	/**
	 * Validation de l'état des composants
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	/**
	 * Validation de l'état des composants
	 * 
	 * @param e IN : Event
	 * @return Object : null
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	/**
	 * Validation de l'état des composants
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}
}