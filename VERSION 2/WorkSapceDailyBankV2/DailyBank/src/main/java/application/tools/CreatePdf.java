// CreatePdf : Bastien RECORD

package application.tools;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Utilitaire pour générer un pdf
 * 
 * @author Bastien RECORD
 */
public class CreatePdf {

	private Document doc;
	private String nomPdf;

	/**
	 * Constructeur paramètré de la classe CreatePdf
	 * 
	 * @author Bastien RECORD
	 * 
	 * @param _nom IN : nom du fichier
	 * 
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	public CreatePdf(String _nom) throws FileNotFoundException, DocumentException {
		this.doc = new Document(PageSize.A4);
		this.nomPdf = _nom;
		PdfWriter.getInstance(this.doc, new FileOutputStream(this.nomPdf));
	}

	/**
	 * Permet d'ajouter un paragraphe au document
	 * 
	 * @author Bastien RECORD
	 * 
	 * @param _message IN : message à insérer
	 * @param _etat    IN : true si le pdf doit être fermé à la fin de la méthode
	 * 
	 * @throws DocumentException
	 */
	public void ajoutParagraphe(String _message, boolean _etat) throws DocumentException {
		if (!this.doc.isOpen()) {
			this.doc.open();
		}

		this.doc.add(new Paragraph(_message));

		if (_etat == true) {
			this.doc.close();
		}
	}

	/**
	 * Permet d'ajouter un tableau au document
	 * 
	 * @author Bastien RECORD
	 * 
	 * @param _table IN : tableau à insérer
	 * @param _etat  IN : true si le pdf doit être fermé à la fin de la méthode
	 * 
	 * @throws DocumentException
	 */
	public void ajoutTableau(PdfPTable _table, boolean _etat) throws DocumentException {
		if (!this.doc.isOpen()) {
			this.doc.open();
		}

		this.doc.add(_table);

		if (_etat == true) {
			this.doc.close();
		}
	}
}