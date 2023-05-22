// Créditer/Débiter : Julie BAELEN
// Virement compte à compte : Bastien RECORD
// Relevé de compte : Bastien RECORD

package application.view;

import java.util.ArrayList;
import java.util.Locale;

import application.DailyBankState;
import application.control.OperationsManagement;
import application.tools.AlertUtilities;
import application.tools.NoSelectionModel;
import application.tools.PairsOfValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;

public class OperationsManagementController {

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
		this.updateInfoCompteClient();
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
	private Label lblInfosClient;
	@FXML
	private Label lblInfosCompte;
	@FXML
	private ListView<Operation> lvOperations;
	@FXML
	private Button btnDebit;
	@FXML
	private Button btnCredit;
	@FXML
	private Button btnVirement;
	@FXML
	private Button btnReleve;

	/**
	 * Action sur le bouton "Annuler"
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Action sur le bouton "Effectuer un débit"
	 * 
	 * @author Julie BAELEN
	 */
	@FXML
	private void doDebit() {

		Operation op = this.omDialogController.enregistrerDebit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Action sur le bouton "Effectuer un crédit"
	 * 
	 * @author Julie BAELEN
	 */
	@FXML
	private void doCredit() {
		Operation op = this.omDialogController.enregistrerCredit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Action sur le bouton "Virement"
	 * 
	 * @author Bastien RECORD
	 */
	@FXML
	private void doAutre() {
		Operation op = this.omDialogController.enregistrerVirement();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Action sur le bouton "Relevé mensuel"
	 * 
	 * @author Bastien RECORD
	 */
	@FXML
	private void doReleveCompte() {
		boolean op = this.omDialogController.genererReleveMensuel(this.oListOperations);
		if (op) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		} else {
			AlertUtilities.showAlert(primaryStage, "Erreur de génération du relevé",
					"Impossible de générer le relevé de compte", "Le relevé de compte n'a pas été généré !!",
					AlertType.ERROR);
		}
	}

	/**
	 * Validation de l'état des composants
	 * 
	 * @author Bastien RECORD
	 */
	private void validateComponentState() {
		if (this.compteConcerne.estCloture == null || this.compteConcerne.estCloture.equals("O")) {
			this.btnCredit.setDisable(true);
			this.btnDebit.setDisable(true);
			this.btnVirement.setDisable(true);
		} else {
			this.btnCredit.setDisable(false);
			this.btnDebit.setDisable(false);
			this.btnVirement.setDisable(false);
		}
		this.btnReleve.setDisable(false);
	}

	/**
	 * Mise à jour des informations du compte client
	 */
	private void updateInfoCompteClient() {

		PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte;

		opesEtCompte = this.omDialogController.operationsEtSoldeDunCompte();

		ArrayList<Operation> listeOP;
		this.compteConcerne = opesEtCompte.getLeft();
		listeOP = opesEtCompte.getRight();

		String info;
		info = this.clientDuCompte.nom + "  " + this.clientDuCompte.prenom + "  (id : " + this.clientDuCompte.idNumCli
				+ ")";
		this.lblInfosClient.setText(info);

		info = "Cpt. : " + this.compteConcerne.idNumCompte + "  "
				+ String.format(Locale.ENGLISH, "%12.02f", this.compteConcerne.solde) + "  /  "
				+ String.format(Locale.ENGLISH, "%8d", this.compteConcerne.debitAutorise);
		this.lblInfosCompte.setText(info);

		this.oListOperations.clear();
		this.oListOperations.addAll(listeOP);

		this.validateComponentState();
	}
}
