package ua.shamrai.xml.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestSuites {

    private String name;
    @Builder.Default
    private List<TestSuite> testSuites = new ArrayList<>();

    public Document toXml() throws ParserConfigurationException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var document = builder.newDocument();
        var rootElement = document.createElement("testsuites");
        document.appendChild(rootElement);
        rootElement.setAttribute("time", getTotalTimeInSeconds().toString());
        rootElement.setAttribute("tests", getTotalTestsAmount().toString());
        rootElement.setAttribute("failures", getTotalFailuresAmount().toString());
        rootElement.setAttribute("name", this.name);
        for (TestSuite t : this.testSuites) {
            rootElement.appendChild(document.importNode(t.getXml(), true));
        }
        return document;
    }

    private Long getTotalTestsAmount() {
        return testSuites.stream().mapToLong(TestSuite::getTests).sum();
    }

    private Long getTotalFailuresAmount() {
        return testSuites.stream().mapToLong(TestSuite::getFailures).sum();
    }

    private Double getTotalTimeInSeconds() {
        return testSuites.stream().mapToDouble(TestSuite::getTime).sum();
    }
}