package ua.shamrai.xml.utils;

import org.w3c.dom.NamedNodeMap;

public class XmlUtils {

    public static Long getAttributeValueAsLong(NamedNodeMap attributes, String attributeName) {
        return attributes.getNamedItem(attributeName) != null ? Long.parseLong(attributes.getNamedItem(attributeName).getNodeValue()) : 0L;
    }

    public static String getAttributeValueAsString(NamedNodeMap attributes, String attributeName) {
        return attributes.getNamedItem(attributeName) != null ? attributes.getNamedItem(attributeName).getNodeValue() : "";
    }

    public static Double getAttributeValueAsDouble(NamedNodeMap attributes, String attributeName) {
        return attributes.getNamedItem(attributeName) != null ? Double.parseDouble(attributes.getNamedItem(attributeName).getNodeValue())
            : 0.0;
    }
}