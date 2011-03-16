/*
 * Created on Oct 27, 2004
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.cl.ClException;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.cl.KtnDatagram;
import no.ntnu.fp.net.cl.KtnDatagram.Flag;

/**
 * Implementation of the Connection-interface. <br>
 * <br>
 * This class implements the behaviour in the methods specified in the interface
 * {@link Connection} over the unreliable, connectionless network realised in
 * {@link ClSocket}. The base class, {@link AbstractConnection} implements some
 * of the functionality, leaving message passing and error handling to this
 * implementation.
 * 
 * @author Sebj�rn Birkeland and Stein Jakob Nordb�
 * @see no.ntnu.fp.net.co.Connection
 * @see no.ntnu.fp.net.cl.ClSocket
 */
public class ConnectionImpl extends AbstractConnection {

    private static final int CONNECTRETRIES = 3;
	/** Keeps track of the used ports for each server port. */
    private static Map<Integer, Boolean> usedPorts = Collections.synchronizedMap(new HashMap<Integer, Boolean>());

    /**
     * Initialise initial sequence number and setup state machine.
     * 
     * @param myPort
     *            - the local port to associate with this connection
     */
    public ConnectionImpl(int myPort) {
    	// check if port is already used, and add to map
    	if(usedPorts.containsKey(myPort) && usedPorts.get(myPort)){
    		//port already taken, throw exception/return?
    		System.out.println("Port already taken!!!");
    		throw new Error();
    	} // TODO find a way to free the port afterwards
    	else usedPorts.put(myPort, true);
    	
		this.myPort=myPort;
		myAddress=getIPv4Address();
		nextSequenceNo=(int)(Math.random()*2147400000);
		state=State.CLOSED;
		System.out.println("Connection instantiated");
    }

    private String getIPv4Address() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    /**
     * Establish a connection to a remote location.
     * 
     * @param remoteAddress
     *            - the remote IP-address to connect to
     * @param remotePort
     *            - the remote portnumber to connect to
     * @throws IOException
     *             If there's an I/O error.
     * @throws java.net.SocketTimeoutException
     *             If timeout expires before connection is completed.
     * @see Connection#connect(InetAddress, int)
     */
    public void connect(InetAddress remoteAddress, int remotePort) throws IOException,
            SocketTimeoutException {
    	//copy arguments to object
    	this.remoteAddress=remoteAddress.getHostAddress();
    	this.remotePort=remotePort;
    	
    	//create synpacket
    	KtnDatagram syn = constructInternalPacket(KtnDatagram.Flag.SYN);
    	SendTimer sendTimer = new SendTimer(new ClSocket(), syn);
    	
    	//send synpacket until synack is received
    	boolean synackOk=false;
    	KtnDatagram ackPacket=null;
    	for(int i=0; i<CONNECTRETRIES && !synackOk; i++){//TODO counter i.e. try to connect three times, then throw exception
    		Timer timer = new Timer();
    		timer.scheduleAtFixedRate(sendTimer, 0, RETRANSMIT);
    		state=State.SYN_SENT;
    		
			//TODO wait here?
    		
    		
    		ackPacket=receiveAck(); //wrap in a while/timer?
    		//check ackpacket for errors
    		if(ackPacket==null){
    			System.out.println("No ack-packet received, retry:");
    		}
    		else if(ackPacket.getFlag()!=KtnDatagram.Flag.SYN_ACK){
    			System.out.println("Not a synack, retry:");
    		}
    		else if(ackPacket.getAck()!=nextSequenceNo-1){
    			System.out.println("Wrong sequence no, retry:");
    		}
    		else if(ackPacket.getDest_addr().equals(remoteAddress)){
    			System.out.println("Wrong host, retry:");
    		}
    		else {
    			System.out.println("Valid synack received.");
    			synackOk=true;
    		}
    		
    		timer.cancel();
    	}
    	
    	if(!synackOk){
    		System.out.println("Connection timed out");
    		throw new Error();
    	}
    	
    	
    	
    	System.out.println("Sending ACK");
    	//TODO error handling
    	sendAck(ackPacket, false); //send ack for the synack
    	state=State.ESTABLISHED;
    	System.out.println("\nConnected!!!\n");
    }

    /**
     * Listen for, and accept, incoming connections.
     * 
     * @return A new ConnectionImpl-object representing the new connection.
     * @see Connection#accept()
     */
    public Connection accept() throws IOException, SocketTimeoutException {
    	
    	state=State.LISTEN;
    	
    	//receive internal packets until we get a syn
    	KtnDatagram synPacket=null;
    	while(synPacket==null || synPacket.getFlag()!=KtnDatagram.Flag.SYN){
        	synPacket=receivePacket(true);
        }
        
    	state=State.SYN_RCVD;
    	
        //TODO: check if packet is valid
        
        //send synack
        remoteAddress=synPacket.getSrc_addr();
        remotePort=synPacket.getSrc_port();
        myAddress=synPacket.getDest_addr();
        
        boolean connectOk = false;
        for(int i=0; i<CONNECTRETRIES && !connectOk; i++){
        	System.out.println("Sending synack: ");
        	sendAck(synPacket, true);
        	//wait for ack
	        while(!connectOk){
		        KtnDatagram datagram=receivePacket(true);
		        connectOk=isValid(datagram);
		        //more, check destination address?
	        }
        }
        if(!connectOk){
        	System.out.println("Never received ack for the synack");
        	state=State.CLOSED;
        	throw new ConnectException("Wait for ack timed out");
        }
        
        System.out.println("\nConnection Accepted!!!\n");
        state=State.ESTABLISHED;
//        //free resources
//        state=State.CLOSED;
//        usedPorts.put(myPort, false);
        
        //create a new connection with correct attributes
//        ConnectionImpl connection = new ConnectionImpl(myPort); //port collisions?
//        connection.remoteAddress=synPacket.getSrc_addr();
//        connection.remotePort=synPacket.getSrc_port();
//        connection.myAddress=myAddress;
//        connection.state=State.ESTABLISHED;
 
    	return this;
    }

    /**
     * Send a message from the application.
     * 
     * @param msg
     *            - the String to be sent.
     * @throws ConnectException
     *             If no connection exists.
     * @throws IOException
     *             If no ACK was received.
     * @see AbstractConnection#sendDataPacketWithRetransmit(KtnDatagram)
     * @see no.ntnu.fp.net.co.Connection#send(String)
     */
    public void send(String msg) throws ConnectException, IOException {
    	System.out.println("Trying to send message: " + msg);
    	if(state!=State.ESTABLISHED)throw new ConnectException("Cannot send: Connection not established.");
    	KtnDatagram packet = constructDataPacket(msg);
    	boolean sendOk=false;
    	for(int i=0; i<3 && !sendOk; i++){
    		KtnDatagram ack=sendDataPacketWithRetransmit(packet);
    		if(ack == null){
    			System.out.println("No packet received");
    		}
    		else if(ack.getFlag()!=Flag.ACK){
    			System.out.println("Not an ack!");
    		}
    		else if(isValid(ack)){
    			System.out.println("Corresponding ack received");
    			sendOk=true;
    		}
    		else {
    			System.out.println("Wrong ackpacket received");
    		}
    	}
    }

    /**
     * Wait for incoming data.
     * 
     * @return The received data's payload as a String.
     * @see Connection#receive()
     * @see AbstractConnection#receivePacket(boolean)
     * @see AbstractConnection#sendAck(KtnDatagram, boolean)
     */
    public String receive() throws ConnectException, IOException {
        while(true){
        	KtnDatagram datapacket = receivePacket(false);
            if(isValid(datapacket)){
            	lastValidPacketReceived=datapacket;
            	sendAck(lastValidPacketReceived, false);
            	break;
            }
        }
    	return lastValidPacketReceived.toString();
    }

    /**
     * Close the connection.
     * 
     * @see Connection#close()
     */
    public void close() throws IOException {
    	//send close message to other end
    	state=State.CLOSED;
        usedPorts.put(myPort, false);
    }

    /**
     * Test a packet for transmission errors. This function should only called
     * with data or ACK packets in the ESTABLISHED state.
     * 
     * @param packet
     *            Packet to test.
     * @return true if packet is free of errors, false otherwise.
     */
    protected boolean isValid(KtnDatagram packet) {
    	if(state!=State.ESTABLISHED && state!=State.SYN_RCVD)throw new Error();
    	else if(packet==null)
        	System.out.println("No datagram received");
        else if(packet.getFlag()==Flag.ACK && packet.getAck()!=nextSequenceNo-1)
        	System.out.println("Wrong ack, retry:");
        else if(!packet.getSrc_addr().equals(remoteAddress))
        	System.out.println("Wrong source address, retry:");
        else if(packet.getSrc_port()!=remotePort)
        	System.out.println("Wrong source port, retry:");
        else {		        //more, check destination address?
        	System.out.println("Packet is valid");
        	return true; //passed all tests :)
        }
    	
    	return false; //one of the tests failed
    }
}
