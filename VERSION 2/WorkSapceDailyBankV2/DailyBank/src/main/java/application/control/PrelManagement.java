// CRUD Prélèvements: Guilherme SAMPAIO

package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.PrelManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.CompteCourant;
import model.data.Prelevement;
import model.orm.Access_BD_Prelevement;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

public class PrelManagement {

	private Stage primaryStage;
	private DailyBankState dailyBankState;
	private PrelManagementController pmcViewController;
	private CompteCourant compteConcerne;

	/**
	 * Création des scenes javafx de la gestion des prélèvements
	 *
	 * @author Guilherme SAMPAIO
	 *
	 * @param _parentStage : Le stage parent
	 * @param _dbstate     : L'application DailyBankState
	 * @param compte       : Le compte du client séléctionné
	 *
	 * @throws Exception e
	 */
	public PrelManagement(Stage _parentStage, DailyBankState _dbstate, CompteCourant compte) {

		this.compteConcerne = compte;
		this.dailyBankState = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(PrelManagementController.class.getResource("prelmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, 900 + 20, 350 + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des prélèvements");
			this.primaryStage.setResizable(false);

			this.pmcViewController = loader.getController();
			this.pmcViewController.initContext(this.primaryStage, this, _dbstate, this.compteConcerne);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affichage de la fenêtre de gestion des prélèvements
	 *
	 * @author Guilherme SAMPAIO
	 *
	 */
	public void doPrelevementsManagementDialog() {
		this.pmcViewController.displayDialog();
	}

	/**
	 * Suppression d'un prélèvement
	 *
	 * @author Guilherme SAMPAIO
	 * @param idPrelev : L'id du prélèvement à supprimer
	 *
	 */
	public void supprimerPrelevement(int idPrelev) {
		try {
			Access_BD_Prelevement ac = new Access_BD_Prelevement();
			ac.supprPrelevement(idPrelev);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();

		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
		}
	}

	/**
	 * Modification d'un prélèvement
	 *
	 * @author Guilherme Sampaio
	 *
	 * @param prelMod : Le prélèvement à modifier
	 * @return Le prélèvement modifié
	 *
	 */
	public Prelevement modifierPrelevement(Prelevement prelMod) {
		PrelEditorPane cep = new PrelEditorPane(this.primaryStage, this.dailyBankState);
		Prelevement result = cep.doPrelevementEditorDialog(prelMod, EditionMode.MODIFICATION);
		if (result != null) {
			try {
				Access_BD_Prelevement ac = new Access_BD_Prelevement();
				ac.updatePrelevement(result);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				result = null;
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				result = null;
			}
		}
		return result;
	}

	/**
	 * Création d'un prélèvement
	 *
	 * @author Guilherme SAMPAIO
	 *
	 * @return Le prélèvement créé
	 */
	public Prelevement nouveauPrelevement() {
		Prelevement prel;
		prel = new Prelevement(0, 0.0, 0, "", this.compteConcerne.idNumCompte);
		PrelEditorPane pep = new PrelEditorPane(this.primaryStage, this.dailyBankState);
		prel = pep.doPrelevementEditorDialog(prel, EditionMode.CREATION);
		if (prel != null) {
			try {
				Access_BD_Prelevement ac = new Access_BD_Prelevement();

				ac.insertPrelevement(prel);
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				prel = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
				ed.doExceptionDialog();
				prel = null;
			}
		}
		return prel;
	}

	/**
	 * Recherche des prélèvements en BD
	 *
	 * @author Guilherme SAMPAIO
	 *
	 * @param idPrelev : L'id du prélèvement à rechercher(si = -1, recherche tous
	 *                 les prélèvements)
	 * @return La liste des prélèvements
	 *
	 */
	public ArrayList<Prelevement> getlistePrelevements(int idPrelev) {
		ArrayList<Prelevement> listePrels = new ArrayList<>();
		try {
			// Recherche des employes en BD. cf. AccessClient > getClients(.)
			// numCompte != -1 => recherche sur numCompte
			// numCompte == -1 et debutNom non vide => recherche nom/prenom
			// numCompte == -1 et debutNom vide => recherche tous les employes
			int idNumCompte = this.compteConcerne.idNumCompte;
			Access_BD_Prelevement ac = new Access_BD_Prelevement();
			listePrels = ac.getPrelevements(idPrelev, idNumCompte);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listePrels = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dailyBankState, ae);
			ed.doExceptionDialog();
			listePrels = new ArrayList<>();
		}
		return listePrels;
	}

}
