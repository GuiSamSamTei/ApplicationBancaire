package application.control;

import java.util.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.ClientsManagementController;
import application.view.EmpruntDetailsController;
import application.view.EmpruntEditorPaneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.AssuranceEmprunt;
import model.data.Client;
import model.data.Emprunt;
import model.orm.Access_BD_Emprunt;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;

public class EmpruntDetails {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private EmpruntDetailsController edViewController;
	private Emprunt empruntConcerne;

	private double sommeInteret;

	/**
	 * Constructeur de la classe EmpruntDetails
	 * 
	 * @param _parentStage IN : Stage parent
	 * @param _dbstate     IN : Etat de l'application
	 */
	public EmpruntDetails(Stage _parentStage, DailyBankState _dbstate, Emprunt _emprunt) {
		this.dailyBankState = _dbstate;
		this.empruntConcerne = _emprunt;
		try {
			FXMLLoader loader = new FXMLLoader(EmpruntDetailsController.class.getResource("empruntdetails.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth() + 50, root.getPrefHeight() + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Details emprunt");
			this.primaryStage.setResizable(false);

			this.edViewController = loader.getController();
			this.edViewController.initContext(this.primaryStage, this, _dbstate, this.empruntConcerne);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la fenêtre de gestion des clients
	 */
	public void doEmpruntDetailsDialog() {
		this.edViewController.displayDialog();
	}

	public ArrayList<String> genererListDetails() {
		ArrayList<String> listeDetail = new ArrayList<>();
		listeDetail.add("Date | Capital Deb | Montant intérêts | Montant du | Mensualité | Capital Fin");

		double capDeb, interet, montant, mensualite, capFin;
		DecimalFormat formatDouble = new DecimalFormat();
		formatDouble.setMaximumFractionDigits(2);

		capDeb = this.empruntConcerne.capitalEmprunt;
		interet = capDeb * this.empruntConcerne.tauxApplicable;
		mensualite = this.empruntConcerne.capitalEmprunt * (this.empruntConcerne.tauxApplicable
				/ (1 - Math.pow((1 + this.empruntConcerne.tauxApplicable), -this.empruntConcerne.dureeEmprunt)));
		montant = mensualite - interet;
		capFin = capDeb - montant;

		this.sommeInteret = interet;

		Calendar date = Calendar.getInstance();
		date.setTime(this.empruntConcerne.dateDebEmprunt);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		listeDetail.add(dateFormat.format(date.getTime()) + "    |    " + formatDouble.format(capDeb) + "    |    "
				+ formatDouble.format(interet) + "    |    " + formatDouble.format(montant) + "    |    "
				+ formatDouble.format(mensualite) + "    |    " + formatDouble.format(capFin));

		for (int i = 2; i <= this.empruntConcerne.dureeEmprunt; i++) {
			capDeb = capFin;
			interet = capDeb * this.empruntConcerne.tauxApplicable;
			montant = mensualite - interet;
			capFin = capDeb - montant;

			this.sommeInteret += interet;

			date.add(Calendar.MONTH, 1);

			listeDetail.add(dateFormat.format(date.getTime()) + "    |    " + formatDouble.format(capDeb) + "    |    "
					+ formatDouble.format(interet) + "    |    " + formatDouble.format(montant) + "    |    "
					+ formatDouble.format(mensualite) + "    |    " + formatDouble.format(capFin));
		}

		return listeDetail;
	}

	public AssuranceEmprunt genererAssurance() {
		Access_BD_Emprunt ae = new Access_BD_Emprunt();
		AssuranceEmprunt assurance = null;

		try {
			assurance = ae.getAssuranceEmprunt(this.empruntConcerne.idEmprunt);
		} catch (DataAccessException | DatabaseConnexionException e) {
			e.printStackTrace();
		}

		return assurance;
	}

	public double getSommeInteret() {
		return this.sommeInteret;
	}
}