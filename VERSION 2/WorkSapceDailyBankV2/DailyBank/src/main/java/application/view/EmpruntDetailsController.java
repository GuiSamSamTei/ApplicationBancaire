package application.view;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import application.DailyBankState;
import application.control.EmpruntDetails;
import application.control.EmpruntEditorPane;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.Emprunt;
import model.data.AssuranceEmprunt;
import model.orm.Access_BD_Emprunt;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.RowNotFoundOrTooManyRowsException;

public class EmpruntDetailsController {

	// Etat courant de l'application
	private DailyBankState dailyBankState;

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
	 * @param _eep             IN : Contrôleur de Dialogue associé à
	 *                         EmpruntEditorPaneController
	 * @param _dbstate         IN : Etat courant de l'application
	 */
	public void initContext(Stage _containingStage, EmpruntDetails _ed, DailyBankState _dbstate, Emprunt _emprunt) {
		this.edDialogController = _ed;
		this.primaryStage = _containingStage;
		this.dailyBankState = _dbstate;
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
	 * Validation de l'état des composants
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	@FXML
	private void doRefreshEmprunt() {
		ArrayList<String> alDetails = this.edDialogController.genererListDetails();

		this.lvEmprunt.getItems().clear();
		this.lvEmprunt.getItems().addAll(alDetails);

		if (this.edDialogController.genererAssurance() != null) {
			this.btnRefreshAss.setDisable(false);
		}
	}

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