package ua.shamrai.xml.model;

import lombok.Builder;
import lombok.Data;
import org.w3c.dom.Node;

@Data
@Builder
public class TestSuite {

    private Long tests;
    private Long failures;
    private Long errors;
    private Long skipped;
    private String name;
    private Double time;
    private Node xml;
}