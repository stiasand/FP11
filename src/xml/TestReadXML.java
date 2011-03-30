package xml;

public class TestReadXML {
	public static void main(String[] args) {
		/*
		String xmlstring = "<AddAppointment>" +
		"<Appointment>" +
		"<id>1</id>" +
		"<addedBy>Testbruker</addedBy>" +
		"<startDate>30.03.2011</startDate>" +
		"<endDate>31.03.2011</endDate>" +
		"<description>Test desc</description>" +
		"<location>Trondheim</location>" +
		"<room>Room 355</room>" +
		"</Appointment>" +
		"</AddAppointment>";
		*/
		String xmlstring = "<hello><name>john</name></hello>";
		ReadXML xml = new ReadXML(xmlstring);
		//System.out.println(xml.getNodeValue("hello", 1, "id"));
	}
}
