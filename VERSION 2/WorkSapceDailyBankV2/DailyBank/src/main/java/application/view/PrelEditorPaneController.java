// CRUD emplyé : Guilherme SAMPAIO

package application.view;

import application.DailyBankState;
import application.tools.AlertUtilities;
import application.tools.EditionMode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Prelevement;

public class PrelEditorPaneController {

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private EditionMode editionMode;

	private Prelevement prelEdite;

	private Prelevement prelResultat;

	/**
	 * Manipulation de la fenêtre
	 *
	 * @param _containingStage IN : fenêtre physique où est la scène contenant le
	 *                         xml contrôler par this
	 * @param _dbstate         IN : état courant de l'application
	 */
	public void initContext(Stage _containingStage, DailyBankState _dbstate) {
		this.primaryStage = _containingStage;
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
	public Prelevement displayDialog(Prelevement prelevement, EditionMode mode) {

		this.editionMode = mode;
		if (prelevement == null) {
			this.prelEdite = new Prelevement();
		} else {
			this.prelEdite = new Prelevement(prelevement);
		}
		this.prelResultat = null;
		switch (mode) {
		case CREATION:
			this.txtMontant.setDisable(false);
			this.txtDate.setDisable(false);
			this.txtBenef.setDisable(false);
			this.lblMessage.setText("Informations sur le nouveau prélèvement");
			this.butOk.setText("Ajouter");
			this.butCancel.setText("Annuler");
			break;
		case MODIFICATION:
			this.txtMontant.setDisable(false);
			this.txtDate.setDisable(false);
			this.txtBenef.setDisable(false);
			this.lblMessage.setText("Informations prélèvement");
			this.butOk.setText("Modifier");
			this.butCancel.setText("Annuler");
			break;
		case SUPPRESSION:
			break;
		}

		// initialisation du contenu des champs
		this.txtMontant.setText(String.valueOf(this.prelEdite.montant));
		this.txtDate.setText(String.valueOf(this.prelEdite.dateRecurrente));
		this.txtBenef.setText(this.prelEdite.beneficiaire);
		this.prelResultat = null;

		this.primaryStage.showAndWait();
		return this.prelResultat;
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
	private TextField txtMontant;
	@FXML
	private TextField txtDate;
	@FXML
	private TextField txtBenef;
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
		this.prelResultat = null;
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
				this.prelResultat = this.prelEdite;
				this.primaryStage.close();
			}
			break;
		case MODIFICATION:
			if (this.isSaisieValide()) {
				this.prelResultat = this.prelEdite;
				this.primaryStage.close();
			}
			break;
		case SUPPRESSION:
			this.prelResultat = this.prelEdite;
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
		try {
			this.prelEdite.montant = Double.parseDouble(this.txtMontant.getText().trim());
			if (this.prelEdite.montant <= 0) {
				AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
						"Saisissez un montant valide (supérieur à 0)", AlertType.WARNING);
				this.txtMontant.requestFocus();
				return false;
			}
		} catch (Exception e) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
					"Saisissez un montant valide (un nombre)", AlertType.WARNING);
			this.txtMontant.requestFocus();
			return false;
		}
		try {
			this.prelEdite.dateRecurrente = Integer.parseInt(this.txtDate.getText().trim());
			if (this.prelEdite.dateRecurrente < 1 || this.prelEdite.dateRecurrente > 28) {
				AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
						"Saisissez une date récurrente valide(de 1 à 28)", AlertType.WARNING);
				this.txtDate.requestFocus();
				return false;
			}
		} catch (Exception e) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
					"Saisissez une date valide (un nombre)", AlertType.WARNING);
			this.txtDate.requestFocus();
			return false;
		}
		this.prelEdite.beneficiaire = this.txtBenef.getText().trim();
		if (this.prelEdite.beneficiaire.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null,
					"Le bénéficiaire ne doit pas être vide", AlertType.WARNING);
			this.txtBenef.requestFocus();
			return false;
		}

		return true;
	}

}
