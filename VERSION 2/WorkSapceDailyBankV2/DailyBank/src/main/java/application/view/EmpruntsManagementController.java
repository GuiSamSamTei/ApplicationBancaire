// Emprunt et assurance d'emprunt : Julie BAELEN et Bastien RECORD

package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.EmpruntsManagement;
import application.tools.AlertUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;

public class EmpruntsManagementController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ComptesManagementController
	private EmpruntsManagement emDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDesEmprunts;
	private ObservableList<String> oListEmprunts;

	/**
	 * Manipulation de la fenêtre
	 * 
	 * @param _containingStage IN : Fenêtre physique ou est la scène contenant le
	 *                         fichier xml contrôlé par this
	 * @param _cm              IN : Contrôleur de Dialogue associé à
	 *                         ComptesManagementController
	 * @param _dbstate         IN : Etat courant de l'application
	 */
	public void initContext(Stage _containingStage, EmpruntsManagement _em, DailyBankState _dbstate, Client client) {
		this.emDialogController = _em;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.clientDesEmprunts = client;
		this.configure();
	}

	/**
	 * Validation de l'état des composants
	 */
	private void configure() {
		String info;

		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListEmprunts = FXCollections.observableArrayList();
		this.lvEmprunts.setItems(this.oListEmprunts);
		this.lvEmprunts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvEmprunts.getFocusModel().focus(-1);
		this.lvEmprunts.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());

		info = this.clientDesEmprunts.nom + "  " + this.clientDesEmprunts.prenom + "  (id : "
				+ this.clientDesEmprunts.idNumCli + ")";
		this.lblInfosClient.setText(info);

		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Affichage de la fenêtre
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	/**
	 * Fermeture de la fenêtre
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
	private Label lblInfosClient;
	@FXML
	private ListView<String> lvEmprunts;
	@FXML
	private Button btnDetailsEmprunt;
	@FXML
	private Button btnNouvelEmprunt;

	/**
	 * Actions sur le bouton "Retour"
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Actions sur le bouton "Voir les opérations"
	 */
	@FXML
	private void doDetailsEmprunts() {
		System.out.println("Détails");
		int selectedIndice = this.lvEmprunts.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			System.out.println("Un emprunt est sélectionné");
		}
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Actions sur le bouton "Clôturer le compte"
	 * 
	 * @author Bastien RECORD
	 */
	@FXML
	private void doNouvelEmprunt() {
		System.out.println("Nouveau");

		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Chargement de la liste des emprunts
	 */
	private void loadList() {
		ArrayList<String> listeEmp = new ArrayList<>();
		listeEmp.add("Emp 1");
		listeEmp.add("Emp 2");
		this.oListEmprunts.clear();
		this.oListEmprunts.addAll(listeEmp);
	}

	/**
	 * Validation de l'état des composants
	 * 
	 * @author Bastien RECORD
	 */
	private void validateComponentState() {
		this.btnNouvelEmprunt.setDisable(false);
		int selectedIndice = this.lvEmprunts.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnDetailsEmprunt.setDisable(false);
		} else {
			this.btnDetailsEmprunt.setDisable(true);
		}
	}
}
