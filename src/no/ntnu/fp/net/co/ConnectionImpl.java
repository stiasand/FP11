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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import com.sun.security.auth.UserPrincipal;

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
public class ConnectionImpl extends AbstractConnection{
	
	private void wait(int t){
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private static final int CONNECTRETRIES = 3;
	/** Keeps track of the used ports for each server port. */
    private static Map<Integer, Boolean> usedPorts = Collections.synchronizedMap(new HashMap<Integer, Boolean>());

    /**
     * Initialise initial sequence number and setup state machine.
     * 
     * @param myPort
     *            - the local port to associate with this connection
     */
    private void reservePort(int port){
    	// check if port is already used, and add to map
    	if(usedPorts.containsKey(port) && usedPorts.get(port)){
    		//port already taken, throw exception/return?
    		System.out.println("Port "+port+" already taken!!!");
    		throw new Error();
    	}
    	else{
    		System.out.println("Port "+ port+ " reserved.");
    		usedPorts.put(port, true);
    	}
    }
    
    public ConnectionImpl(int myPort) {
    	reservePort(myPort);
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
    	
    	Timer timer = new Timer();
		timer.scheduleAtFixedRate(sendTimer, 0, RETRANSMIT);
		state=State.SYN_SENT;
		long startTime=new Date().getTime();
		while(startTime+TIMEOUT > new Date().getTime() && !synackOk){//TODO counter i.e. try to connect three times, then throw exception
    		
    		ackPacket=receiveAck(); //wrap in a while/timer?
    		synackOk=isValid(ackPacket);
    		
    	}
		timer.cancel();
    	
    	if(!synackOk){
    		System.out.println("Connection timed out");
    		throw new Error();
    	}
    	
    	System.out.println("Sending ACK");
    	//TODO error handling
    	wait(1000);
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
    	while(true){
        	synPacket=receivePacket(true);
        	if(isValid(synPacket)){
        		System.out.println("Got valid synpacket");
        		break;
        	}
        }
    	
    	state=State.CLOSED;
    	
    	//connection accepted
        usedPorts.put(myPort, false);
        ConnectionImpl connection=new ConnectionImpl(myPort);
        connection.myAddress=synPacket.getDest_addr();
        connection.remoteAddress=synPacket.getSrc_addr();
        connection.remotePort=synPacket.getSrc_port();
        connection.state=State.SYN_RCVD;
        
        //send synack
        wait(1000);
    	connection.sendAck(synPacket, true);
    	
    	
    	//wait for ack
    	KtnDatagram datagram=null;
        do{
        	System.out.println("\nWaiting for ack.\n");
	        datagram=receiveAck(); //dette virker ikke av en eller annen grunn
        	//datagram=connection.receivePacket(true);
        }while(!connection.isValid(datagram));
        
        connection.state=State.ESTABLISHED;
       
        System.out.println("\nConnection Accepted!!!\n");
        state=State.CLOSED;
        
    	return connection;
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
    	System.out.println("\nTrying to send message: " + msg);
    	if(state!=State.ESTABLISHED)throw new IllegalStateException("Should only be used in ESTABLISHED state.");
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
    	if(!sendOk){
    		throw new ConnectException("Connection timed out");
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
    	System.out.println("\nReceiving...");
    	if(state!=State.ESTABLISHED)throw new ConnectException("Cannot receive: Connection not established.");
        while(true){
        	KtnDatagram datapacket = receivePacket(false);

            if(isValid(datapacket)){
            	sendAck(lastValidPacketReceived, false);
            	break;
            }
            else{
            	System.out.println("Invalid packet, continuing receive");
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
    	if(disconnectRequest==null){ //do client part of disconnection
        	KtnDatagram fin = constructInternalPacket(Flag.FIN);
        	try {
        		wait(1000);
    			simplySendPacket(fin);
    		} catch (ClException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		//fin sent
//    		state=State.FIN_WAIT_1;
//    		//wait for ack from server
//    		while(true){
//        		KtnDatagram finack = receivePacket(true);
//        		if(isValid(finack)){
//        			System.out.println("Received valid ack for the fin");
//        			break;
//        		}
//        		else{
//        			System.out.println("Ivalid finack");
//        		}
//    		}
    		state=State.FIN_WAIT_1;
    		while(true){
    			System.out.println("\nWaiting for ack\n");
    			KtnDatagram ack = receivePacket(true);
    			if(isValid(ack)){
    				break;
    			}
    		}
    		System.out.println("Valid ack received");
    		
    		
    		state=State.FIN_WAIT_2;
    		System.out.println("Waiting for fin");
    		//wait for fin
    		while(true){
    			try{
    				KtnDatagram internalPacket=receivePacket(true);
        			if(isValid(internalPacket)){
        				System.out.println("Valid fin received");
        				disconnectRequest=internalPacket;
        				break;
        			}
    			}
    			catch(EOFException e){
    				//shouldn't happen
    			}
    			System.out.println("Still waiting for fin...");
    		}
    		
    		//fin received, send ack
    		System.out.println("Fin received, sending ack");
    		wait(1000);
    		sendAck(disconnectRequest, false);
    		
    	}
    	else{ //do server part of disconnecting
    		//ack the fin
    		wait(1000);
    		sendAck(disconnectRequest, false);
    		
    		//send fin
    		KtnDatagram fin = constructInternalPacket(Flag.FIN);
    		try {
    			wait(1000);
				simplySendPacket(fin);
			} catch (ClException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			state=State.CLOSE_WAIT;
//			try {
//				Thread.sleep(2000); //wait 2 secs for the ack to arrive
//			} catch (InterruptedException e) {
//				//do nothing
//			} 
			
			while(true){
				KtnDatagram ack = receiveAck();
				if(isValid(ack)){
					break;
				}
			}
    	}
    	//Code for both server and client close
    	
    	//were finished :)
		state=State.CLOSED;
		
		//free port
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
    	System.out.println("Checking packet for state "+state);
    	if(packet==null){ //if the packet is null, it's definitely invalid
			System.out.println("packet is null");
			return false;
		}
    	
    	switch(state){
    	case ESTABLISHED:
    		//we're expecting a datapacket or an ack
    		if(packet.getFlag()==Flag.ACK){
    			//ack
    			if(packet.getAck()!=nextSequenceNo-1){
    				System.out.println("Wrong ack");
    				return false;
    			}
    		}
    		else if(packet.getFlag()==Flag.NONE){
    			//datapacket
    			if(packet.getChecksum()!=packet.calculateChecksum()){
    				System.out.println("Invalid checksum");
    				return false;
    			}
    		}
    		else{
    			System.out.println("Invalid flag");
    			return false;
    		}
    		if(!packet.getSrc_addr().equals(remoteAddress)){
            	System.out.println("Wrong source address");
    			return false;
    		}
            else if(packet.getSrc_port()!=remotePort){
            	System.out.println("Wrong source port");
            	return false;
            }
            else {		        //more, check destination address?
            	System.out.println("Packet is valid");
            	lastValidPacketReceived=packet;
            	return true; //passed all tests :)
            }
    	case LISTEN:
    		//waiting for syn
			if(packet.getFlag()!=Flag.SYN){
				System.out.println("Not a syn packet");
				return false;
			} 
			else return true;
    	case SYN_RCVD:
    		System.out.println("Expecting ack for the syn");
    		//only valid packet is an ack for the synack
    		if(packet.getFlag()!=Flag.ACK){
    			System.out.println("Not an ack");
    			return false;
    		}
    		else if (packet.getAck()!=nextSequenceNo-1){
    			System.out.println("Wrong sequence no");
    			return false;
    		}
    		else{
    			return true;
    		}
    		//TODO: more tests on rcvd?
    	case SYN_SENT:
    		//only valid packet in this state is a synack
    		//check synackpacket for errors
    		if(packet.getFlag()!=KtnDatagram.Flag.SYN_ACK){
    			System.out.println("Not a synack");
    			return false;
    		}
    		else if(packet.getAck()!=nextSequenceNo-1){
    			System.out.println("Wrong sequence no");
    			return false;
    		}
    		else if(!packet.getSrc_addr().equals(remoteAddress)){
    			System.out.println("Wrong source");
    			return false;
    		}
    		else {
    			System.out.println("Valid synack received.");
    			return true;
    		}
    	case FIN_WAIT_1:
    		//fin is sent and we are waiting for an ack for the fin
    		if(packet.getFlag()!=KtnDatagram.Flag.ACK){
    			System.out.println("Not an ack");
    			return false;
    		}
    		else if(packet.getAck()!=nextSequenceNo-1){
    			System.out.println("Wrong sequence no");
    			return false;
    		}
    		else if(!packet.getSrc_addr().equals(remoteAddress)){
    			System.out.println("Wrong source address");
    			return false;
    		}
    		else {
    			System.out.println("Valid ack received.");
    			return true;
    		}
    	case FIN_WAIT_2:
    		System.out.println("Checking finpacket");
    		if(packet.getFlag()!=Flag.FIN){
    			System.out.println("Not a fin");
    			return false;
    		}
    		else return true;
    	case CLOSE_WAIT:
    		System.out.println("Checking ack");
    		if(packet.getFlag()!=Flag.ACK){
    			System.out.println("Not an ack");
    			return false;
    		}
    		else return true;
    	default:
    			System.out.println("Unsupported state");
    			return false;
    		
    	}
    	//return false; //one of the tests failed
    }
}
