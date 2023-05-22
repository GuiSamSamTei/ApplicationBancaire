package application.tools;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CreatePdf {

	private Document doc;
	private String nomPdf;

	public CreatePdf(String _nom) throws FileNotFoundException, DocumentException {
		this.doc = new Document(PageSize.A4);
		this.nomPdf = _nom;
		PdfWriter.getInstance(this.doc, new FileOutputStream(this.nomPdf));
	}

	public void ajoutParagraphe(String _message, boolean _etat) throws DocumentException {
		if (!this.doc.isOpen()) {
			this.doc.open();
		}

		this.doc.add(new Paragraph(_message));

		if (_etat == true) {
			this.doc.close();
		}
	}
}