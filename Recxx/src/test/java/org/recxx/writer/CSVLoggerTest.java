package org.recxx.writer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.meanbean.test.BeanTester;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CSVLoggerTest {

	private static final String TEST_FILE_NAME = "csvLoggerTestFile.csv";

	@Mock
	private BufferedWriterManager writerManagerMock;

	@Mock
	private BufferedWriter writerMock;

	private CSVLogger csvLogger;

	@After
	public void tearDown() throws Exception {
		csvLogger = null;
		deleteTestFile();
	}

	@Test
	public void gettersAndSettersShouldFunctionCorrectly() throws Exception {
		new BeanTester().testBean(CSVLogger.class);
	}

	private void givenWriterManagerReturnsWriterMock() throws Exception {
		when(writerManagerMock.open(new File(TEST_FILE_NAME))).thenReturn(writerMock);
	}

	private void givenCSVLoggerIsCreated() throws Exception {
		csvLogger = new CSVLogger();
		csvLogger.setFilename(TEST_FILE_NAME);
		csvLogger.setWriterManager(writerManagerMock);
	}

	private void givenCSVLoggerIsOpen() throws Exception {
		csvLogger.open();
	}

	private void givenFileExists() throws IOException {
		getTestFile().createNewFile();
	}

	private File getTestFile() {
		return new File(TEST_FILE_NAME);
	}

	private long getLastModifiedTimestampOfTestFile() throws IOException {
		return getTestFile().lastModified();
	}

	private long getLengthOfTestFile() throws IOException {
		return getTestFile().length();
	}

	private void deleteTestFile() {
		getTestFile().delete();
	}

	private String getLastModifiedTimestampOfTestFileAsString() throws IOException {
		long lastModifiedTimestampOfTestFile = getLastModifiedTimestampOfTestFile();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date lastModifiedDateOfTestFile = new Date(lastModifiedTimestampOfTestFile);
		String lastModifiedDateStringOfTestFile = dateFormat.format(lastModifiedDateOfTestFile);
		return lastModifiedDateStringOfTestFile;
	}

	private void thenWriterShouldWrite(String string) throws IOException {
		verify(writerMock).write(string);
		verifyNoMoreInteractions(writerMock);
	}

	private void thenWriterShouldWriteLine(String string) throws IOException {
		verify(writerMock).write(string);
		verify(writerMock).newLine();
		verifyNoMoreInteractions(writerMock);
	}

	private void thenWriterShouldWriteOnlyDelimiter() throws IOException {
		thenWriterShouldWrite(csvLogger.getDelimiter());
	}

	private void thenWriterShouldNotBeWrittenTo() throws IOException {
		verifyNoMoreInteractions(writerMock);
	}

	@Test
	public void closeShouldDelegateToBufferedWriterManager() throws Exception {
		CSVLogger csvLogger = new CSVLogger();
		csvLogger.setWriterManager(writerManagerMock);
		csvLogger.close();
		verify(writerManagerMock).close();
		verifyNoMoreInteractions(writerManagerMock);
	}

	@Test
	public void openShouldDelegateToBufferedWriterManager() throws Exception {
		givenCSVLoggerIsCreated();
		csvLogger.open();
		verify(writerManagerMock).open(new File(TEST_FILE_NAME));
		verifyNoMoreInteractions(writerManagerMock);
	}

	@Test
	public void writeWithDateShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		Date inputDate = new Date();
		csvLogger.write(inputDate);
		thenWriterShouldWrite("" + inputDate + csvLogger.getDelimiter());
	}

	@Test
	public void writeWithNullDateShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		Date nullInputDate = null;
		csvLogger.write(nullInputDate);
		thenWriterShouldWriteOnlyDelimiter();
	}

	@Test
	public void writeLineWithDateShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		Date inputDate = new Date();
		csvLogger.writeLine(inputDate);
		thenWriterShouldWriteLine("" + inputDate);
	}

	@Test
	public void writeLineWithNullDateShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		Date nullInputDate = null;
		csvLogger.writeLine(nullInputDate);
		thenWriterShouldWriteLine(csvLogger.getNullString());
	}

	@Test
	public void writeWithStringShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		String inputString = "TestString";
		csvLogger.write(inputString);
		thenWriterShouldWrite("" + inputString + csvLogger.getDelimiter());
	}

	@Test
	public void writeWithNullStringShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		String nullString = null;
		csvLogger.write(nullString);
		thenWriterShouldWriteOnlyDelimiter();
	}

	@Test
	public void writeLineWithStringShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		String inputString = "TestString";
		csvLogger.writeLine(inputString);
		thenWriterShouldWriteLine("" + inputString);
	}

	@Test
	public void writeLineWithNullStringShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		String nullString = null;
		csvLogger.writeLine(nullString);
		thenWriterShouldWriteLine(csvLogger.getNullString());
	}

	@Test
	public void writeWithIntShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		int inputInt = 15;
		csvLogger.write(inputInt);
		thenWriterShouldWrite("" + inputInt + csvLogger.getDelimiter());
	}

	@Test
	public void writeLineWithIntShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		int inputInt = 15;
		csvLogger.writeLine(inputInt);
		thenWriterShouldWriteLine("" + inputInt);
	}

	@Test
	public void writeWithFloatShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		float inputFloat = 16.0f;
		csvLogger.write(inputFloat);
		thenWriterShouldWrite("" + inputFloat + csvLogger.getDelimiter());
	}

	@Test
	public void writeLineWithFloatShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		float inputFloat = 16.0f;
		csvLogger.writeLine(inputFloat);
		thenWriterShouldWriteLine("" + inputFloat);
	}

	@Test
	public void writeWithDoubleShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		double inputDouble = 17.0;
		csvLogger.write(inputDouble);
		thenWriterShouldWrite("" + inputDouble + csvLogger.getDelimiter());
	}

	@Test
	public void writeLineWithDoubleShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		double inputDouble = 17.0;
		csvLogger.writeLine(inputDouble);
		thenWriterShouldWriteLine("" + inputDouble);
	}

	@Test
	public void writeLineWithEmptyStringArrayShouldNotDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		String[] emptyStringArray = {};
		csvLogger.writeLine(emptyStringArray);
		thenWriterShouldNotBeWrittenTo();
	}

	@Test
	public void writeLineWithStringArrayShouldDelegateToWriter() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		String[] stringArray = { "1", "2", "3" };
		csvLogger.writeLine(stringArray);
		verify(writerMock).write(stringArray[0] + csvLogger.getDelimiter());
		verify(writerMock).write(stringArray[1] + csvLogger.getDelimiter());
		verify(writerMock).write(stringArray[2]);
		verify(writerMock).newLine();
		verifyNoMoreInteractions(writerMock);
	}

	@Test
	public void toStringOnOpenedLoggerShouldReturnStringContainingFileInformation() throws Exception {
		givenCSVLoggerIsCreated();
		givenWriterManagerReturnsWriterMock();
		givenCSVLoggerIsOpen();
		givenFileExists();
		String expectedToString =
		        "CSV File [" + TEST_FILE_NAME + "], last modfied at [" + getLastModifiedTimestampOfTestFileAsString()
		                + "], size [" + getLengthOfTestFile() + " bytes].";
		assertThat(csvLogger.toString(), is(expectedToString));
	}

	@Test
	public void toStringOnUnopenedLoggerShouldNotThrowNullPointerException() throws Exception {
		String expectedToString = "CSV File not opened yet.";
		String actualToString = new CSVLogger().toString();
		assertThat(actualToString, is(expectedToString));
	}
}