// Emprunt et assurance d'emprunt : Julie BAELEN et Bastien RECORD

package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.EmpruntsManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.Emprunt;

public class EmpruntsManagementController {

	// Contrôleur de Dialogue associé à ComptesManagementController
	private EmpruntsManagement emDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDesEmprunts;
	private ObservableList<Emprunt> oListEmprunts;

	/**
	 * Manipulation de la fenêtre
	 *
	 * @param _containingStage IN : Fenêtre physique ou est la scène contenant le
	 *                         fichier xml contrôlé par this
	 * @param _em              IN : Contrôleur de Dialogue associé à
	 *                         EmpruntsManagementController
	 * @param _dbstate         IN : Etat courant de l'application
	 * @param _client          IN : client concerné
	 */
	public void initContext(Stage _containingStage, EmpruntsManagement _em, DailyBankState _dbstate, Client client) {
		this.emDialogController = _em;
		this.primaryStage = _containingStage;
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
	private ListView<Emprunt> lvEmprunts;
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
	 * Actions sur le bouton "Détails emprunt"
	 *
	 * @author Bastien RECORD
	 */
	@FXML
	private void doDetailsEmprunts() {
		int selectedIndice = this.lvEmprunts.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Emprunt emp = this.oListEmprunts.get(selectedIndice);
			this.emDialogController.detailsEmprunt(emp);
		}

		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Actions sur le bouton "Nouvel emprunt"
	 *
	 * @author Bastien RECORD
	 */
	@FXML
	private void doNouvelEmprunt() {
		this.emDialogController.creerEmprunt();

		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Chargement de la liste des emprunts
	 *
	 * @author Bastien RECORD
	 */
	private void loadList() {
		ArrayList<Emprunt> listeCpt;
		listeCpt = this.emDialogController.getEmpruntDunClient();
		this.oListEmprunts.clear();
		this.oListEmprunts.addAll(listeCpt);
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
