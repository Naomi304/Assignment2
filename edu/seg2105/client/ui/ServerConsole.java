package edu.seg2105.client.ui;

import java.io.BufferedReader;
import java.net.BindException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;
import ocsf.server.AbstractServer;

public class ServerConsole implements ChatIF {
	  final public static int DEFAULT_PORT = 5555;
	  Scanner fromConsole;
	EchoServer server;

	  
	/**
	   * Constructs an instance of the ServerConsole UI.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port){
			try 
			{
				server = new EchoServer(port, this);
			} 
			catch(Exception exception) 
			{
				System.out.println("ERROR - Could not listen for clients!");
			}
			
			fromConsole = new Scanner(System.in);
	}
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it 5 s it to the client's message handler.
	   */
	public void accept(){
		try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handlemessagefromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
		
	}
			
	
	  /**
	   * This method is responsible for the creation of 
	   * the server instance (there is no UI in this phase).
	   *
	   * @param args[0] The port number to listen on.  Defaults to 5555 
	   *          if no argument is entered.
	   */
	public static void main(String[] args) 
	  {
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
		
	    ServerConsole chat = new ServerConsole(port);
	    chat.accept();
	  }
	
	  /**
	   * This method overrides the method in the ChatIF interface.  It
	   * displays a message onto the screen.
	   *
	   * @param message The string to be displayed.
	   */
	@Override
	public void display(String message) {
	    System.out.println("SERVER MSG> " + message);

	}
	

}
