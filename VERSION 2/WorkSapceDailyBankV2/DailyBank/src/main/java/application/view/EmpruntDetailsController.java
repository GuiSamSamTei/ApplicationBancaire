package application.view;

import java.text.DecimalFormat;
import java.util.ArrayList;

import application.DailyBankState;
import application.control.EmpruntDetails;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.AssuranceEmprunt;
import model.data.Emprunt;

public class EmpruntDetailsController {

	// Contrôleur de Dialogue associé à ClientsManagementController
	private EmpruntDetails edDialogController;

	// Fenêtre physique ou est la scène contenant le fichier xml contrôlé par this
	private Stage primaryStage;

	// Données de la fenêtre
	private Emprunt empruntConcerne;

	/**
	 * Manipulation de la fenêtre
	 *
	 * @param _containingStage IN : Fenêtre physique ou est la scène contenant le
	 *                         fichier xml contrôlé par this
	 * @param _ed              IN : Contrôleur de Dialogue associé à
	 *                         EmpruntEditorPaneController
	 * @param _dbstate         IN : Etat courant de l'application
	 * @param _emprunt         IN : emprunt concerné
	 */
	public void initContext(Stage _containingStage, EmpruntDetails _ed, DailyBankState _dbstate, Emprunt _emprunt) {
		this.edDialogController = _ed;
		this.primaryStage = _containingStage;
		this.empruntConcerne = _emprunt;

		this.vbAssurance.setVisible(false);

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
	private ListView<String> lvEmprunt;
	@FXML
	private Button btnRefreshAss;
	@FXML
	private VBox vbAssurance;
	@FXML
	private Label lbTauxAssurance;
	@FXML
	private Label lbTauxCouverture;
	@FXML
	private Label lbMensualite;
	@FXML
	private Label lbCoutCredit;
	@FXML
	private Label lbCoutAssurance;
	@FXML
	private Label lbTotal;

	/**
	 * Fermeture de la fenêtre
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Actualise la liste détaillée de l'emprunt
	 *
	 * @author Bastien RECORD
	 */
	@FXML
	private void doRefreshEmprunt() {
		ArrayList<String> alDetails = this.edDialogController.genererListDetails();

		this.lvEmprunt.getItems().clear();
		this.lvEmprunt.getItems().addAll(alDetails);

		if (this.edDialogController.genererAssurance() != null) {
			this.btnRefreshAss.setDisable(false);
		}
	}

	/**
	 * Actualise la liste détaillée de l'assurance
	 *
	 * @author Bastien RECORD
	 */
	@FXML
	private void doRefreshAssurance() {
		AssuranceEmprunt assuranceConcerne = this.edDialogController.genererAssurance();

		DecimalFormat formatDouble = new DecimalFormat();
		formatDouble.setMaximumFractionDigits(2);

		if (assuranceConcerne != null) {
			double txAss, txCouv, mensualite, coutCredit, coutAss, total;

			txAss = (assuranceConcerne.tauxAssurance * 100);
			txCouv = assuranceConcerne.tauxCouv;
			mensualite = assuranceConcerne.tauxAssurance * this.empruntConcerne.capitalEmprunt / 12;
			coutCredit = this.edDialogController.getSommeInteret();
			coutAss = (assuranceConcerne.tauxAssurance * this.empruntConcerne.capitalEmprunt / 12)
					* this.empruntConcerne.dureeEmprunt;
			total = coutCredit + coutAss;

			this.lbTauxAssurance.setText("" + formatDouble.format(txAss));
			this.lbTauxCouverture.setText("" + formatDouble.format(txCouv));
			this.lbMensualite.setText("" + formatDouble.format(mensualite));
			this.lbCoutCredit.setText("" + formatDouble.format(coutCredit));
			this.lbCoutAssurance.setText("" + formatDouble.format(coutAss));
			this.lbTotal.setText("" + formatDouble.format(total));

			this.vbAssurance.setVisible(true);
		}
	}
}