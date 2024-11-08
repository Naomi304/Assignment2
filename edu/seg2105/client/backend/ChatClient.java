// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, String loginID, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#")){//special command???? {
    		handleCommand(message);
    	}
    	else 
      sendToServer(message);
    }
    catch(IOException e)
    
    {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
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
		  System.out.println("client is quitting");
		  quit();
	  }
	  
	  else if (command.startsWith("#logoff")){
		  closeConnection();
	  }
	  else if (command.startsWith("#sethost")) {
		  if (!isConnected()) {
			  setHost(commandParts[1]);
			  clientUI.display("Host set to: " + commandParts[1]);
		  }
		  else
			  clientUI.display("Cannot set host while connected, please log off and try again");
	  }
	  else if (command.startsWith("#setport")) {
		  if (!isConnected()) {
                  int port = Integer.parseInt(commandParts[1]);
                  setPort(port);
                  clientUI.display("Port set to: " + port);
              }
		  else
			  clientUI.display("Cannot set host while connected, please log off and try again");
	  }
	  else if (command.startsWith("#login")) {
		  if (isConnected()) {
			  openConnection();
		  }
		  else 
			  clientUI.display("Could not send message to server.  Terminating client.");
		  
		  }
		  
  		else if (command.startsWith("#gethost")) {
  			clientUI.display("Host is " + getHost());
		  
	  }else if (command.startsWith("#getport")) {
		  clientUI.display("Port is " + Integer.toString(getPort()));
	  }
	  
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  } 
  
  /**
	 * Implements hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override
  protected void connectionException (Exception exception0) {
	  clientUI.display("The server is shut down");
	  quit();
  }
  /**
	 * Implements hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
		
		clientUI.display("Connection closed");
	}
	@Override
    protected void connectionEstablished() {
        // Send the login message to the server
        try {
			sendToServer("#login " + loginID);
		} catch (IOException e) {
			System.out.println("Error closing establishing connection: " + e.getMessage());
		}
    }
}
//End of ChatClient class
