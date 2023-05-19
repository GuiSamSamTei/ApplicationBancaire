// CRUD emplyé : Guilherme SAMPAIO

package application.view;

import java.util.regex.Pattern;

import application.DailyBankState;
import application.control.ExceptionDialog;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.Order;
import model.orm.exception.Table;

public class EmployeEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private EditionMode editionMode;

	private Employe employeEdite;

	private Employe employeResultat;

	/**
	 * Manipulation de la fenêtre
	 * 
	 * @param _containingStage IN : fenêtre physique où est la scène contenant le
	 *                         xml contrôler par this
	 * @param _dbstate         IN : état courant de l'application
	 */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.configure();
	}

	/**
	 * Configuration des actions par défaut voulus
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	/**
	 * Manipulation de la fenêtre
	 * 
	 * @param employe IN : Employé à afficher
	 * @param mode    IN :mode d'édition
	 * @return employé résultat
	 */
	public Employe displayDialog(Employe employe, EditionMode mode) {

		this.editionMode = mode;
		if (employe == null) {
			this.employeEdite = new Employe(0, "", "", "", "", "", this.dailyBankState.getEmployeActuel().idAg);
		} else {
			this.employeEdite = new Employe(employe);
		}
		this.employeResultat = null;
		switch (mode) {
		case CREATION:
			this.txtIdEmploye.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			this.txtDroits.setDisable(false);
			this.txtLogin.setDisable(false);
			this.txtIDAG.setDisable(false);
			this.lblMessage.setText("Informations sur le nouveau employé");
			this.butOk.setText("Ajouter");
			this.butCancel.setText("Annuler");
			break;
		case MODIFICATION:
			this.txtIdEmploye.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			this.txtDroits.setDisable(false);
			this.txtLogin.setDisable(false);
			this.txtMDP.setDisable(false);
			this.txtIDAG.setDisable(false);
			this.lblMessage.setText("Informations employé");
			this.butOk.setText("Modifier");
			this.butCancel.setText("Annuler");
			break;
		case SUPPRESSION:
			// ce mode n'est pas utilisé pour les Clients :
			// la suppression d'un client n'existe pas il faut que le chef d'agence
			// bascule son état "Actif" à "Inactif"
			ApplicationException ae = new ApplicationException(Table.NONE, Order.OTHER, "SUPPRESSION CLIENT NON PREVUE",
					null);
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();

			break;
		}
		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dailyBankState.getEmployeActuel())) {
			// rien pour l'instant
		}
		// initialisation du contenu des champs
		this.txtIdEmploye.setText("" + this.employeEdite.idEmploye);
		this.txtNom.setText(this.employeEdite.nom);
		this.txtPrenom.setText(this.employeEdite.prenom);
		this.txtDroits.setText(this.employeEdite.droitsAccess);
		this.txtLogin.setText(this.employeEdite.login);
		this.txtMDP.setText(this.employeEdite.motPasse);
		this.txtIDAG.setText(String.valueOf(this.employeEdite.idAg));

		this.employeResultat = null;

		this.primaryStage.showAndWait();
		return this.employeResultat;
	}

	/**
	 * Fermeture de la fenêtre
	 * 
	 * @param e IN : événement de fermeture
	 * @return Object : null
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions

	@FXML
	private Label lblMessage;
	@FXML
	private TextField txtIdEmploye;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private TextField txtDroits;
	@FXML
	private TextField txtLogin;
	@FXML
	private TextField txtMDP;
	@FXML
	private TextField txtIDAG;
	@FXML
	private Button butOk;
	@FXML
	private Button butCancel;

	/**
	 * Action sur le bouton annuler
	 * 
	 * @author Guilherme SAMPAIO
	 */
	@FXML
	private void doCancel() {
		this.employeResultat = null;
		this.primaryStage.close();
	}

	/**
	 * Action sur le bouton ajouter
	 * 
	 * @author Guilherme SAMPAIO
	 */
	@FXML
	private void doAjouter() {
		switch (this.editionMode) {
		case CREATION:
			if (this.isSaisieValide()) {
				this.employeResultat = this.employeEdite;
				this.primaryStage.close();
			}
			break;
		case MODIFICATION:
			if (this.isSaisieValide()) {
				this.employeResultat = this.employeEdite;
				this.primaryStage.close();
			}
			break;
		case SUPPRESSION:
			this.employeResultat = this.employeEdite;
			this.primaryStage.close();
			break;
		}

	}

	/**
	 * Vérifie si les données saisies sont valides
	 * 
	 * @author Guilherme SAMPAIO
	 * 
	 * @return true si tout est bien saisi et false sinon
	 */
	private boolean isSaisieValide() {
		this.employeEdite.nom = this.txtNom.getText().trim();
		this.employeEdite.prenom = this.txtPrenom.getText().trim();
		this.employeEdite.droitsAccess = this.txtDroits.getText().trim();
		this.employeEdite.login = this.txtLogin.getText().trim();
		this.employeEdite.motPasse = this.txtMDP.getText().trim();
		if (this.employeEdite.nom.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le nom ne doit pas être vide",
					AlertType.WARNING);
			this.txtNom.requestFocus();
			return false;
		}
		if (this.employeEdite.prenom.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le prénom ne doit pas être vide",
					AlertType.WARNING);
			this.txtPrenom.requestFocus();
			return false;
		}

		if (this.employeEdite.droitsAccess.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
					"Les droits d'accès ne doivent pas être vides", AlertType.WARNING);
			this.txtPrenom.requestFocus();
			return false;
		}

		if (this.employeEdite.login.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le login ne doit pas être vide",
					AlertType.WARNING);
			this.txtPrenom.requestFocus();
			return false;
		}

		if (this.employeEdite.motPasse.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
					"Le mot de passe ne doit pas être vide", AlertType.WARNING);
			this.txtPrenom.requestFocus();
			return false;
		}

		return true;
	}
}
