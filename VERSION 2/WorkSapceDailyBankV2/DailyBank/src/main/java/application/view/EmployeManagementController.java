// CRUD employé : Guilherme SAMPAIO

package application.view;

import java.util.ArrayList;

import application.DailyBankState;
import application.control.EmployeManagement;
import application.tools.AlertUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Employe;

public class EmployeManagementController {

	// Contrôleur de Dialogue associé à ClientsManagementController
	private EmployeManagement cmDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private ObservableList<Employe> oListEmployes;

	// Manipulation de la fenêtre
	public void initContext(Stage _containingStage, EmployeManagement employeManagement, DailyBankState _dbstate) {
		this.cmDialogController = employeManagement;
		this.primaryStage = _containingStage;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.oListEmployes = FXCollections.observableArrayList();
		this.lvEmployes.setItems(this.oListEmployes);
		this.lvEmployes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvEmployes.getFocusModel().focus(-1);
		this.lvEmployes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.validateComponentState();
	}

	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private TextField txtIdEmploye;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private ListView<Employe> lvEmployes;
	@FXML
	private Button btnSupprEmploye;
	@FXML
	private Button btnModifEmploye;
	@FXML
	private Button btnComptesEmploye;

	/**
	 * Action sur le bouton annuler
	 *
	 * @author Guilherme SAMPAIO
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Action sur le bouton rechercher
	 *
	 * @author Guilherme SAMPAIO
	 */
	@FXML
	private void doRechercher() {
		int idEmploye;
		try {
			String nc = this.txtIdEmploye.getText();
			if (nc.equals("")) {
				idEmploye = -1;
			} else {
				idEmploye = Integer.parseInt(nc);
				if (idEmploye < 0) {
					this.txtIdEmploye.setText("");
					idEmploye = -1;
				}
			}
		} catch (NumberFormatException nfe) {
			this.txtIdEmploye.setText("");
			idEmploye = -1;
		}

		String debutNom = this.txtNom.getText();
		String debutPrenom = this.txtPrenom.getText();

		if (idEmploye != -1) {
			this.txtNom.setText("");
			this.txtPrenom.setText("");
		}

		// Recherche des employes en BD. cf. AccessClient > getClients(.)
		// numCompte != -1 => recherche sur numCompte
		// numCompte != -1 et debutNom non vide => recherche nom/prenom
		// numCompte != -1 et debutNom vide => recherche tous les employes

		ArrayList<Employe> listeEmployes;
		listeEmployes = this.cmDialogController.getlisteEmployes(idEmploye, debutNom, debutPrenom);

		this.oListEmployes.clear();
		this.oListEmployes.addAll(listeEmployes);
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Action sur le bouton modifier
	 *
	 * @author Guilherme SAMPAIO
	 */
	@FXML
	private void doModifierEmploye() {

		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Employe empMod = this.oListEmployes.get(selectedIndice);
			Employe result = this.cmDialogController.modifierEmploye(empMod);
			if (result != null) {
				this.oListEmployes.set(selectedIndice, result);
			}
		}
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Action sur le bouton supprimer
	 *
	 * @author Guilherme SAMPAIO
	 */
	@FXML
	private void doDesactiverEmploye() {
		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			if (AlertUtilities.confirmYesCancel(this.primaryStage, "Supprimer l'employé",
					"Etes vous sur de vouloir supprimer cet employé ?", null, AlertType.CONFIRMATION)) {
				Employe employe = this.oListEmployes.get(selectedIndice);
				this.cmDialogController.supprimerEmploye(employe.idEmploye);
			}

		}
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Action sur le bouton créer employé
	 *
	 * @author Guilherme SAMPAIO
	 */
	@FXML
	private void doNouveauEmploye() {
		Employe employe;
		employe = this.cmDialogController.nouveauEmploye();
		if (employe != null) {
			this.oListEmployes.add(employe);
		}
		this.loadList();
		this.validateComponentState();
	}

	/**
	 * Actualisation de la liste des clients
	 *
	 * @author Guilherme SAMPAIO
	 */
	private void loadList() {
		String debutNom = this.txtNom.getText();
		String debutPrenom = this.txtPrenom.getText();
		int idEmploye;
		try {
			idEmploye = Integer.parseInt(this.txtIdEmploye.getText());
		} catch (Exception e) {
			idEmploye = -1;
		}

		ArrayList<Employe> listeCpt;
		listeCpt = this.cmDialogController.getlisteEmployes(idEmploye, debutNom, debutPrenom);
		this.oListEmployes.clear();
		this.oListEmployes.addAll(listeCpt);
	}

	/**
	 * Active et désactive les boutons selon différents états
	 *
	 * @author Guilherme SAMPAIO
	 */
	private void validateComponentState() {
		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifEmploye.setDisable(false);
			this.btnSupprEmploye.setDisable(false);
		} else {
			this.btnModifEmploye.setDisable(true);
			this.btnSupprEmploye.setDisable(true);
		}
	}
}