package de.fechtelhoff;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Erzeugt binär unterschiedliche PDF-Dummy-Dateien. Sinnvoll, wenn verschiedene Dokumente für das FRS benötigt.
 */
public class PdfDummyCreator {

	private static final Logger LOG = LoggerFactory.getLogger(PdfDummyCreator.class);

	public static final String FILE_NAME_PREFIX = "DummyFile";

	public static void main(String[] args) {
		if (args != null && args.length != 0 && args[0] != null) {
			try {
				final int amountOfGeneratedFiles = Integer.parseInt(args[0]);
				final Path path = Path.of("").toAbsolutePath();
				LOG.info("Aktueller Ordner {}", path);
				LOG.info("Anzahl zu erzeugende Dokumente {}", amountOfGeneratedFiles);
				for (int i = 0; i < amountOfGeneratedFiles; i++) {
					try (final PDDocument document = new PDDocument()) {
						final String uuid = UUID.randomUUID().toString();
						final PDPage page = new PDPage();
						document.addPage(page);

						// Create a new font object selecting one of the PDF base fonts
						PDFont font = PDType1Font.HELVETICA_BOLD;

						// Start a new content stream which will "hold" the to be created content
						PDPageContentStream contentStream = new PDPageContentStream(document, page);

						// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
						contentStream.beginText();
						contentStream.setFont(font, 12);
						contentStream.newLineAtOffset(amountOfGeneratedFiles, 700);
						contentStream.showText("Dummy File " + uuid);
						contentStream.endText();

						// Make sure that the content stream is closed:
						contentStream.close();

						final Path file = path.resolve(String.format("%s-%s.pdf", FILE_NAME_PREFIX, uuid));
						document.save(new File(file.toString()));
					}
				}
				LOG.info("{} PDFs in {} erzeugt.", amountOfGeneratedFiles, path);
			} catch (final IOException exception) {
				LOG.error("Kann die Datei(en) nicht erzeugen.");
			} catch (final NumberFormatException exception) {
				LOG.error("Argument \"{}\" kann nicht in eine Zahl gewandelt werden.", args[0]);
			}
		} else {
			LOG.warn("Bitte als Argument die Anzahl der zu erzeugenden Dokumente übergeben.");
		}
	}
}
