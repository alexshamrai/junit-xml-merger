package ua.shamrai.xml;


import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ua.shamrai.xml.model.TestSuite;
import ua.shamrai.xml.model.TestSuites;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static ua.shamrai.xml.utils.XmlUtils.getAttributeValueAsDouble;
import static ua.shamrai.xml.utils.XmlUtils.getAttributeValueAsLong;
import static ua.shamrai.xml.utils.XmlUtils.getAttributeValueAsString;

@Slf4j
public class JunitXmlParser {

    private static final String INPUT_DIR = "build/test-results/test/";
    private static final String OUTPUT_FILE = "build/test-results/merged.xml";
    private static final String SUITE_NAME = "My Suite";

    protected void run() throws Exception {
        var outputFile = new File(OUTPUT_FILE);
        var inputFileDir = new File(INPUT_DIR);

        if (!inputFileDir.isDirectory()) {
            log.error("Cannot read input directory {}", inputFileDir.getAbsolutePath());
            return;
        }

        if (!initializeOutputFile(outputFile)) {
            return;
        }

        log.info("Files and folders are ok");

        var suites = new TestSuites();
        suites.setName(SUITE_NAME);
        var filesList = Arrays.stream(Objects.requireNonNull(inputFileDir.listFiles())).toList();
        for (File file : filesList) {
            if (file.getAbsoluteFile().toString().endsWith(".xml")) {
                log.info("Adding {} to TestSuites", file.getName());
                suites.getTestSuites().add(parseTestSuite(file));
            }
        }

        writeXmlToFile(suites.toXml(), outputFile);
    }

    private boolean initializeOutputFile(File outputFile) {
        try (var ignored = new FileOutputStream(outputFile)) {
            return true;
        } catch (IOException e) {
            log.error("Cannot write output to: {}", outputFile.getAbsolutePath());
            return false;
        }
    }

    private void writeXmlToFile(org.w3c.dom.Document xmlDocument, File outputFile) throws Exception {
        var transformer = TransformerFactory.newInstance().newTransformer();
        var outputResult = new StreamResult(outputFile);
        var inputSource = new DOMSource(xmlDocument);
        transformer.transform(inputSource, outputResult);
    }

    private TestSuite parseTestSuite(File filename) throws ParserConfigurationException, SAXException, IOException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var document = builder.parse(filename);
        return transform(document.getFirstChild());
    }

    private TestSuite transform(Node testSuite) {
        NamedNodeMap attributes = testSuite.getAttributes();
        return TestSuite.builder()
            .tests(getAttributeValueAsLong(attributes, "tests"))
            .errors(getAttributeValueAsLong(attributes, "errors"))
            .failures(getAttributeValueAsLong(attributes, "failures"))
            .skipped(getAttributeValueAsLong(attributes, "skipped"))
            .name(getAttributeValueAsString(attributes, "name"))
            .time(getAttributeValueAsDouble(attributes, "time"))
            .xml(testSuite)
            .build();
    }
}