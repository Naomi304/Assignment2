package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
	//Instance variables **********************************************
	ChatIF serverUI;

	
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  String loginKey = "loginID";
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) throws IOException
  {
    super(port);
    this.serverUI = serverUI;
    listen();
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client.getInfo(loginKey));
    
    	String message = (String) msg;
    	
		if (message.startsWith("#login")) {
    		if(client.getInfo(loginKey)!= null){
    			try {
	    			client.sendToClient("Error already logged in. Terminating Connection.");
	    			client.close();
    			}catch(IOException e){
    				System.out.println("Error sending message to client: " + e.getMessage());
    			}
    		}
    		String loginid = msg.toString().split(" ")[1];
    		//System.out.println("A new client has connected to the server. ");
    		//System.out.println(msg+"has logged on");
    		//this.sendToAllClients(loginid+" has logged on!");
  	      
  	         client.setInfo("loginID", loginid);
  	         System.out.println(loginid + " has logged on.");
             this.sendToAllClients(loginid + " has logged on!");
    	}
    	else {
    		String loginid = (String) client.getInfo(loginKey);
    		this.sendToAllClients(loginid+">"+message);
    		}
    	}
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handlemessagefromServerUI (String message) {
	  ChatClient com = null;
	  try {
		  if (message.startsWith("#")) {
			  handleCommand(message);
		  } else {
			  serverUI.display(message);
			  sendToAllClients("SERVER MSG> " + message);
		  }
	} catch (IOException e) {
	      serverUI.display
	        ("Could not send message to server.  Terminating server.");
//	      quit();
	}
  }
  
  /**
 	 * Handles commands
  * @throws IOException 
 	 */
  public void handleCommand (String command) throws IOException {
	  
	  String[] commandParts = command.split(" ");
      String mainCommand = commandParts[0];
    		  
	  if (command.startsWith("#quit")) {
		  close();
		  System.exit(0);
	  }
	  
	  else if (command.startsWith("#stop")){
		  stopListening();
	  }
	  else if (command.startsWith("#close")) {
		  close();
		  }
	  else if (command.startsWith("#setport")) {
		 int port = Integer.parseInt(commandParts[1]);
         setPort(port);
         
         if (isListening() == true){
				serverUI.display("Cannot set port while connected!");
			}
         else {

			setPort(port);
			serverUI.display("Port now set to: "+port+"!");}
	  }
	  else if (command.startsWith("#start")) {
		 listen();
		 serverUI.display("listening");
		 
	  }else if (command.startsWith("#getport")) {
		  serverUI.display(String.valueOf(getPort()));//displays
	  }
	  
  }


  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  

  
  /**
	 * Implementation of hook method called each time a new client connection is
	 * accepted. The default implementation does nothing.
	 * @param client the connection connected to the client.
	 */
  @Override
	protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client has connected to the server.");
  }

	/**
	 * Implementation of hook method called each time a client disconnects.
	 * The default implementation does nothing. The method
	 * may be overridden by subclasses but should remains synchronized.
	 *
	 * @param client the connection with the client.
	 */
  @Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("The client "+ client.getInfo(loginKey)+" is disconnected");
	}
	/**
	 * Overrides hook method called each time an exception is thrown in a
	 * ConnectionToClient thread.
	 * The method may be overridden by subclasses but should remains
	 * synchronized.
	 *
	 * @param client the client that raised the exception.
	 * @param Throwable the exception thrown.
	 */
	@Override
	  protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
	    System.out.println("A client has disconnected!");
	  }

	/**
	 * Overrides hook method called when the server is clased.
	 * The default implementation does nothing. This method may be
	 * overriden by subclasses. When the server is closed while still
	 * listening, serverStopped() will also be called.
	 */
	  @Override
	  protected void serverClosed() {
	    System.out.println("Server has closed!");
	  }

}
//End of EchoServer class
