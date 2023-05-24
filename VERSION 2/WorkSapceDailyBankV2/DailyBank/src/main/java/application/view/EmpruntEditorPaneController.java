package application.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import application.DailyBankState;
import application.control.EmpruntEditorPane;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.Emprunt;
import model.orm.Access_BD_Emprunt;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.RowNotFoundOrTooManyRowsException;

public class EmpruntEditorPaneController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

	// Contrôleur de Dialogue associé à ClientsManagementController
	private EmpruntEditorPane eepDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDesEmprunts;

	/**
	 * Manipulation de la fenêtre
	 * 
	 * @param _containingStage IN : Fenêtre physique ou est la scène contenant le
	 *                         fichier xml contrôlé par this
	 * @param _eep             IN : Contrôleur de Dialogue associé à
	 *                         EmpruntEditorPaneController
	 * @param _dbstate         IN : Etat courant de l'application
	 */
	public void initContext(Stage _containingStage, EmpruntEditorPane _eep, DailyBankState _dbstate, Client _client) {
		this.eepDialogController = _eep;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
		this.clientDesEmprunts = _client;
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

	/**
	 * Validation de l'état des composants
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	@FXML
	private void doValidate() {
		if (this.isSaisieValide()) {
			Emprunt newEmprunt = new Emprunt(0, Integer.valueOf(this.tauxApp.getText().trim()),
					Integer.valueOf(this.capital.getText().trim()), Integer.valueOf(this.duree.getText().trim()), null,
					this.clientDesEmprunts.idNumCli);
			Access_BD_Emprunt ae = new Access_BD_Emprunt();
			try {
				ae.insertEmprunt(newEmprunt);
			} catch (RowNotFoundOrTooManyRowsException | DataAccessException | DatabaseConnexionException
					| ManagementRuleViolation e) {
				e.printStackTrace();
			}
			this.doCancel();
		}
	}

	private boolean isSaisieValide() {
//		this.clientEdite.nom = this.txtNom.getText().trim();
//		this.clientEdite.prenom = this.txtPrenom.getText().trim();
//		this.clientEdite.adressePostale = this.txtAdr.getText().trim();
//		this.clientEdite.telephone = this.txtTel.getText().trim();
//		this.clientEdite.email = this.txtMail.getText().trim();
//		if (this.rbActif.isSelected()) {
//			this.clientEdite.estInactif = ConstantesIHM.CLIENT_ACTIF;
//		} else {
//			this.clientEdite.estInactif = ConstantesIHM.CLIENT_INACTIF;
//		}
//
//		if (this.clientEdite.nom.isEmpty()) {
//			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le nom ne doit pas être vide",
//					AlertType.WARNING);
//			this.txtNom.requestFocus();
//			return false;
//		}
//		if (this.clientEdite.prenom.isEmpty()) {
//			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le prénom ne doit pas être vide",
//					AlertType.WARNING);
//			this.txtPrenom.requestFocus();
//			return false;
//		}
//
//		String regex = "(0)[1-9][0-9]{8}";
//		if (!Pattern.matches(regex, this.clientEdite.telephone) || this.clientEdite.telephone.length() > 10) {
//			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le téléphone n'est pas valable",
//					AlertType.WARNING);
//			this.txtTel.requestFocus();
//			return false;
//		}
//		regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
//				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
//		if (!Pattern.matches(regex, this.clientEdite.email) || this.clientEdite.email.length() > 20) {
//			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le mail n'est pas valable",
//					AlertType.WARNING);
//			this.txtMail.requestFocus();
//			return false;
//		}

		return true;
	}
}