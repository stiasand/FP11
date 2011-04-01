package client.network;
import java.io.*;
import java.net.*;

public class Client {
	private static Socket socket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    private static BufferedReader read = null;

    public static void main(String[] args) throws IOException {



        try {
            socket = new Socket("127.0.0.1", 123);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection");
            System.exit(1);
        }

        read = new BufferedReader(new InputStreamReader(System.in));
        //String num1,num2;

		//System.out.println(in.readLine()); //Uncomment to debug
        boolean run = true;
        while (run) {
        	String msg = read.readLine();
        	if (msg != null && msg != "" && msg.equals("test")) {
				String addAppointment = "<AddAppointment>\n" +
					"<Appointment>\n" +
					"<id>1</id>\n" +
					"<addedBy>Testbruker</addedBy>\n" +
					"<startDate>30.03.2011</startDate>\n" +
					"<endDate>31.03.2011</endDate>\n" +
					"<description>Test desc</description>\n" +
					"<location>Trondheim</location>\n" +
					"<room>Room 355</room>\n" +
					"</Appointment>\n" +
					"</AddAppointment>";
				out.println(addAppointment + "\n");

			} else if (msg != "" && msg != null) {
        		out.println(msg + "\n");
        	} else {
        		run = false;
        	}
        	/*
        	String reply = in.readLine();
        	if (reply != null) {
        		System.out.println(reply);
        	}
        	*/
        }

        /*
		System.out.print("This int-->");
		num1=read.readLine();
		out.println(num1);
		System.out.print("Times this int-->");
		num2=read.readLine();
		out.println(num2);
		System.out.println("Equals");
		*/

		//System.out.println(in.readLine());

        out.close();
        in.close();
        read.close();
        socket.close();
    }

	protected void finalize() {
		try {
	        out.close();
	        in.close();
	        read.close();
	        socket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}