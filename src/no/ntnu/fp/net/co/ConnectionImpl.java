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
    		System.out.println("Port taken!!!");
    	} // TODO find a way to free the port afterwards
    	else usedPorts.put(myPort, true);
    	
		this.myPort=myPort;
		myAddress=getIPv4Address();
		nextSequenceNo=(int)(Math.random()*50); // TODO replace 50 with something meaningful
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
    	
    	//send synpacket until synack is received
    	boolean synackOk=false;
    	KtnDatagram ackPacket=null;
    	for(int i=0; i<3; i++){//TODO counter i.e. try to connect three times, then throw exception
    		try {
				simplySendPacket(syn);
			} catch (ClException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO wait here?
    		ackPacket=receiveAck();
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
    			break;
    		}
    	}
    	if(!synackOk){
    		System.out.println("Timeoutx3..");
    		//throw exception
    	}
    	
    	
    	
    	System.out.println("Sending ACK");
    	//TODO error handling
    	sendAck(ackPacket, false); //send ack for the synack
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
        
        //TODO: check if packet is valid
        
        //send synack
        remoteAddress=synPacket.getSrc_addr();
        remotePort=synPacket.getSrc_port();
        myAddress=synPacket.getDest_addr();
        sendAck(synPacket, true);
        
        //wait for ack
        while(true){
	        KtnDatagram datagram=receivePacket(true);
	        if(datagram==null){
	        	System.out.println("No datagram received, retry:");
	        	continue;
	        }
	        else if(datagram.getAck()!=nextSequenceNo-1){
	        	System.out.println("Wrong ack, retry:");
	        	continue;
	        }
	        else if(!datagram.getSrc_addr().equals(remoteAddress)){
	        	System.out.println("Wrong source address, retry:");
	            continue;
        	}
	        else if(datagram.getSrc_port()!=remotePort){
	        	System.out.println("Wrong source port, retry:");
	        	continue;
	        }
	        //more, check destination address?
	        break;
        }
        System.out.println("\nConnection Accepted!!!\n");
        
//        //free resources
//        state=State.CLOSED;
//        usedPorts.put(myPort, false);
        
        //create a new connection with correct attributes
//        ConnectionImpl connection = new ConnectionImpl(myPort); //port collisions?
//        connection.remoteAddress=synPacket.getSrc_addr();
//        connection.remotePort=synPacket.getSrc_port();
//        connection.myAddress=myAddress;
//        connection.state=State.ESTABLISHED;
        state=State.ESTABLISHED;
        
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
        //throw new NotImplementedException();
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
        //throw new NotImplementedException();
    	return null;
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
        // TODO implement
    	return true;
    }
}
