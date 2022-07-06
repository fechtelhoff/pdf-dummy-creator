package de.fechtelhoff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PdfDummyCreatorTest {

	private static final Logger LOG = LoggerFactory.getLogger(PdfDummyCreatorTest.class);

	private static final int AMOUNT = 5;

	private static final String FILE_EXTENSION = "pdf";
	private static final String FILE_NAME = "Output";

	private static final Path TESTDATA_DIRECTORY = Path.of("target/test-classes").toAbsolutePath();

	@BeforeAll
	static void beforeAll() {
		Assertions.assertTrue(checkPath(TESTDATA_DIRECTORY));
		System.out.println();
	}

	@Test
	void test() {
		final String[] args = new String[]{String.valueOf(AMOUNT)};
		PdfDummyCreator.main(args);

		final Path workDir = Path.of("");
		List<Path> pdfFiles = listPdfFiles(workDir);
		Assertions.assertEquals(AMOUNT, pdfFiles.size());
		pdfFiles.forEach(PdfDummyCreatorTest::deleteFile);
		pdfFiles = listPdfFiles(workDir);
		Assertions.assertEquals(0, pdfFiles.size());
	}

	@SuppressWarnings("SameParameterValue")
	private static boolean checkPath(final Path path) {
		if (Files.exists(path)) {
			if (Files.isDirectory(path)) {
				System.out.printf("Das Verzeichnis: \"%s\" existiert.%n", path);
			} else if (Files.isRegularFile(path)) {
				System.out.printf("Die Datei: \"%s\" existiert.%n", path);
			}
			return true;
		} else {
			System.out.printf("Der Pfad: \"%s\" existiert NICHT.%n", path);
			return false;
		}
	}

	private static List<Path> listPdfFiles(final Path contentPath) {
		try (Stream<Path> stream = Files.walk(contentPath)) {
			return stream
				.filter(entry -> !Files.isDirectory(entry))
				.filter(entry -> entry.toString().toLowerCase().endsWith(FILE_EXTENSION))
				.filter(entry -> entry.toString().startsWith(PdfDummyCreator.FILE_NAME_PREFIX))
				.sorted()
				.toList();
		} catch (final IOException exception) {
			LOG.error("Fehler beim lesen der Dateien.", exception);
		}
		return Collections.emptyList();
	}

	private static void deleteFile(final Path path) {
		try {
			Files.delete(path);
		} catch (final IOException exception) {
			final String message = String.format("Kann die Datei \"%s\" nicht löschen.", path);
			throw new IllegalStateException(message, exception);
		}
	}
}
