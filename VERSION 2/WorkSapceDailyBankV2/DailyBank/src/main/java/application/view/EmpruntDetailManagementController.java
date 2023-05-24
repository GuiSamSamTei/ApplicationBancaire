// Créditer/Débiter : Julie BAELEN
// Virement compte à compte : Bastien RECORD

package application.view;

import java.util.ArrayList;
import java.util.Locale;

import application.DailyBankState;
import application.control.OperationsManagement;
import application.tools.NoSelectionModel;
import application.tools.PairsOfValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;

public class EmpruntDetailManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à OperationsManagementController
	private OperationsManagement omDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDuCompte;
	private CompteCourant compteConcerne;
	private ObservableList<Operation> oListOperations;

	/**
	 * Manipulation de la fenêtre
	 * 
	 * @param _containingStage IN : Fenêtre physique ou est la scène contenant le
	 *                         fichier xml contrôlé par this
	 * @param _om              IN : Contrôleur de Dialogue associé à
	 *                         OperationsManagementController
	 * @param _dbstate         IN : Etat courant de l'application
	 * @param client           IN : Client du compte
	 * @param compte           IN : Compte concerné
	 */
	public void initContext(Stage _containingStage, OperationsManagement _om, DailyBankState _dbstate, Client client,
			CompteCourant compte) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.omDialogController = _om;
		this.clientDuCompte = client;
		this.compteConcerne = compte;
		this.configure();
	}

	/**
	 * Configuration de la fenêtre
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListOperations = FXCollections.observableArrayList();
		this.lvOperations.setItems(this.oListOperations);
		this.lvOperations.setSelectionModel(new NoSelectionModel<Operation>());
		this.validateComponentState();
	}

	/**
	 * Mise à jour des informations du compte client
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	/**
	 * Gestion du stage
	 * 
	 * @param e IN : Evènement de fermeture de la fenêtre
	 * @return null
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private TextField capital;
	@FXML
	private TextField tauxApp;
	@FXML
	private TextField duree;
	@FXML
	private RadioButton assuranceOui;
	@FXML
	private RadioButton assuranceNon;
	@FXML
	private TextField tauxAssurance;
	@FXML
	private TextField tauxCouv;
	@FXML
	private Label lblInfosCompte;
	@FXML
	private ListView<Operation> lvOperations;
	

	/**
	 * Action sur le bouton "Annuler"
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Action sur le bouton "Effectuer un
	 * Validation de l'état des composants
	 * 
	 * @author Bastien RECORD
	 */
	private void validateComponentState() {
		if (assuranceNon.isSelected()) {
			tauxAssurance.setDisable(true);
			tauxCouv.setDisable(true);
			
		}
	}
}

