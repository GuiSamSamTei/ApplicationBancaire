package application.view;

import java.time.LocalDate;
import java.util.Date;

import application.DailyBankState;
import application.control.EmpruntEditorPane;
import application.tools.AlertUtilities;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.AssuranceEmprunt;
import model.data.Client;
import model.data.Emprunt;
import model.orm.Access_BD_Emprunt;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.RowNotFoundOrTooManyRowsException;

public class EmpruntEditorPaneController {

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
	 * @param _client          IN : client concerné
	 */
	public void initContext(Stage _containingStage, EmpruntEditorPane _eep, DailyBankState _dbstate, Client _client) {
		this.primaryStage = _containingStage;
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
	 * Action sur le bouton "Annuler"
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Action sur le bouton "Valider"
	 *
	 * @author Bastien RECORD
	 */
	@FXML
	private void doValidate() {
		double tauxApp;
		int capitalEmp, dureeEmp;
		Date dateDebEmp;

		if (this.isSaisieValide()) {
			tauxApp = Double.valueOf(this.tauxApp.getText());
			capitalEmp = Integer.valueOf(this.capital.getText());
			dureeEmp = Integer.valueOf(this.duree.getText());
			dateDebEmp = java.sql.Date.valueOf(LocalDate.now());

			Emprunt newEmprunt = new Emprunt(0, tauxApp, capitalEmp, dureeEmp, dateDebEmp,
					this.clientDesEmprunts.idNumCli);

			if (this.assuranceOui.isSelected()) {
				double tauxAss, tauxCouv;

				tauxAss = Double.valueOf(this.tauxAssurance.getText());
				tauxCouv = Double.valueOf(this.tauxCouv.getText());

				AssuranceEmprunt newAssurance = new AssuranceEmprunt(0, tauxAss, tauxCouv, 0);

				Access_BD_Emprunt ae = new Access_BD_Emprunt();
				try {
					ae.insertEmprunt(newEmprunt, newAssurance);
				} catch (RowNotFoundOrTooManyRowsException | DataAccessException | DatabaseConnexionException
						| ManagementRuleViolation e) {
					e.printStackTrace();
				}
			} else {

				Access_BD_Emprunt ae = new Access_BD_Emprunt();
				try {
					ae.insertEmprunt(newEmprunt, null);
				} catch (RowNotFoundOrTooManyRowsException | DataAccessException | DatabaseConnexionException
						| ManagementRuleViolation e) {
					e.printStackTrace();
				}
			}

			this.doCancel();
		}

	}

	/**
	 * Vérifie que les champs sont correctements saisies
	 *
	 * @author Bastien RECORD
	 *
	 * @return true si la saisie est valide (false sinon et affiche une alerte avec
	 *         l'erreur associée)
	 */
	private boolean isSaisieValide() {

		if (this.capital.getText().trim().isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le capital ne doit pas être vide",
					AlertType.WARNING);

			return false;
		}

		if (this.duree.getText().trim().isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "La durée ne doit pas être vide",
					AlertType.WARNING);

			return false;
		}

		if (this.tauxApp.getText().trim().isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
					"Le taux applicable ne doit pas être vide", AlertType.WARNING);

			return false;
		}

		if (this.assuranceOui.isSelected()) {
			if (this.tauxAssurance.getText().trim().isEmpty()) {
				AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
						"Le taux assurance ne doit pas être vide", AlertType.WARNING);

				return false;
			}

			if (this.tauxCouv.getText().trim().isEmpty()) {
				AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
						"Le taux couverture ne doit pas être vide", AlertType.WARNING);

				return false;
			}
		}

		return true;
	}
}