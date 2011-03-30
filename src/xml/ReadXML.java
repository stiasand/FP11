package xml;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
 
public class ReadXML {
	DocumentBuilderFactory documentBuilderFactory;
	DocumentBuilder documentBuilder;
	Document document;
	
	
	public ReadXML(String xml) {
		try {
			DocumentBuilderFactory dbf =
	            DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(xml));

	        Document doc = db.parse(is);
            System.out.println(doc);
        } catch (Exception e) { System.out.println(e.getMessage());} // TODO: Before release remove sysout
	}
	
	  public static String getCharacterDataFromElement(Element e) {
		    Node child = e.getFirstChild();
		    if (child instanceof CharacterData) {
		       CharacterData cd = (CharacterData) child;
		       return cd.getData();
		    }
		    return "?";
	  }
	
	public int getNodeSize(String nodeName) {
		return document.getElementsByTagName(nodeName).getLength();
	}
	
	public Node getNode(String nodeName, int nodeIndex) {
		NodeList nodeList = document.getElementsByTagName(nodeName);
		return nodeList.item(nodeIndex);
	}
	
	public String getNodeValue(String nodeName, int nodeIndex, String tag) {
		NodeList nodeList = document.getElementsByTagName(nodeName);
		Node node = nodeList.item(nodeIndex);
		return getValue(node, tag);
	}
	
    private String getValue(Node node, String tag) {
        String value = "";
        try {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                NodeList nodeList = element.getElementsByTagName(tag);
                Element item = (Element)nodeList.item(0);
                NodeList list = item.getChildNodes();
                value = ((Node)list.item(0)).getNodeValue().trim();
            }
        } catch (Exception e) { System.out.println(e.getMessage());} // TODO: Before release remove sysout
        return value;
    }
}