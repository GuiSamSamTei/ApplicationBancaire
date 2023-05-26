// Clôturer compte : Bastien RECORD
// Nouveau compte : Guilherme SAMPAIO

package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.ComptesManagement;
import application.tools.AlertUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;

public class ComptesManagementController {

	// Contrôleur de Dialogue associé à ComptesManagementController
	private ComptesManagement cmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDesComptes;
	private ObservableList<CompteCourant> oListCompteCourant;

	/**
	 * Manipulation de la fenêtre
	 *
	 * @param _containingStage IN : Fenêtre physique ou est la scène contenant le
	 *                         fichier xml contrôlé par this
	 * @param _cm              IN : Contrôleur de Dialogue associé à
	 *                         ComptesManagementController
	 * @param _dbstate         IN : Etat courant de l'application
	 */
	public void initContext(Stage _containingStage, ComptesManagement _cm, DailyBankState _dbstate, Client client) {
		this.cmDialogController = _cm;
		this.primaryStage = _containingStage;
		this.clientDesComptes = client;
		this.configure();
	}

	/**
	 * Validation de l'état des composants
	 */
	private void configure() {
		String info;

		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListCompteCourant = FXCollections.observableArrayList();
		this.lvComptes.setItems(this.oListCompteCourant);
		this.lvComptes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvComptes.getFocusModel().focus(-1);
		this.lvComptes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());

		info = this.clientDesComptes.nom + "  " + this.clientDesComptes.prenom + "  (id : "
				+ this.clientDesComptes.idNumCli + ")";
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
	private ListView<CompteCourant> lvComptes;
	@FXML
	private Button btnVoirOpes;
	@FXML
	private Button btnCloturerCompte;
	@FXML
	private Button btnModifierCompte;
	@FXML
	private Button btnSupprCompte;
	@FXML
	private Button btnPrel;

	/**
	 * Actions sur le bouton "Annuler"
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Actions sur le bouton "Voir les opérations"
	 */
	@FXML
	private void doVoirOperations() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			this.cmDialogController.gererOperationsDUnCompte(cpt);
		}
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Action sur le bouton "Prélèvement"
	 *
	 * @author Gulherme SAMPAIO
	 */
	@FXML
	private void doVoirPrelevements() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			CompteCourant cpt = this.oListCompteCourant.get(selectedIndice);
			this.cmDialogController.gererPrelevementsDUnCompte(cpt);
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
	private void doCloturerCompte() {
		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			if (AlertUtilities.confirmYesCancel(this.primaryStage, "Clôturer le compte",
					"Etes vous sur de vouloir clôturer ce compte ?", null, AlertType.CONFIRMATION)) {
				this.cmDialogController.cloturerCompte(this.oListCompteCourant.get(selectedIndice));
			}
		}
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Actions sur le bouton "Modifier le compte"
	 */
	@FXML
	private void doModifierCompte() {
	}

	/**
	 * Actions sur le bouton "Supprimer le compte"
	 */
	@FXML
	private void doSupprimerCompte() {
	}

	/**
	 * Actions sur le bouton "Nouveau compte"
	 *
	 * @author Guilherme SAMPAIO
	 */
	@FXML
	private void doNouveauCompte() {
		CompteCourant compte;
		compte = this.cmDialogController.creerNouveauCompte();
		if (compte != null) {
			this.oListCompteCourant.add(compte);
		}
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Chargement de la liste des comptes
	 */
	private void loadList() {
		ArrayList<CompteCourant> listeCpt;
		listeCpt = this.cmDialogController.getComptesDunClient();
		this.oListCompteCourant.clear();
		this.oListCompteCourant.addAll(listeCpt);
	}

	/**
	 * Validation de l'état des composants
	 *
	 * @author Bastien RECORD
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnModifierCompte.setDisable(true);
		this.btnSupprCompte.setDisable(true);
		this.btnPrel.setDisable(true);

		int selectedIndice = this.lvComptes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnVoirOpes.setDisable(false);
			if (this.oListCompteCourant.get(selectedIndice).estCloture.equals("O")) {
				this.btnCloturerCompte.setDisable(true);
			} else {
				this.btnCloturerCompte.setDisable(false);
				this.btnPrel.setDisable(false);
			}
		} else {
			this.btnVoirOpes.setDisable(true);
			this.btnCloturerCompte.setDisable(true);
		}
	}
}
