package core;

import gui.ClientGUI;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class GameClient implements Runnable {

	private ClientGUI clientGUI;
	
	private int playerID;
	private String playerName;
	private Socket clientSoc;
	private BufferedReader in;
	private PrintWriter out;
	
	/**
	 * Constructs a GameClient object that manages network IO and updating the GUI.
	 * @param clientSoc
	 * @param gamePanel
	 * @param playerName
	 * @throws IOException
	 */
	public GameClient(Socket clientSoc, ClientGUI gamePanel, String playerName) throws IOException {
		this.clientSoc = clientSoc;
		this.clientGUI = gamePanel;
		this.playerName = playerName;
		in = new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
		out = new PrintWriter(clientSoc.getOutputStream(), true);
	}
	
	@Override
	public void run() {
		try {
			/* Enter a loop that continuously listens to incoming messages. */
			while (handleMessage(in.readLine()));
		} catch (SocketException e) {
			JOptionPane.showMessageDialog(clientGUI.frame, "The Server has disconnected. The game will now shut down.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			/* End thread quietly if the main thread has ended (e.g. Exit from GUI). */
		} finally {
			try {
				out.close();
				in.close();
				clientSoc.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
	}

	public synchronized void sendMessage(String msg) {
		out.println(msg);
	}
	
	/**
	 * Parses and handles incoming messages.
	 * This method is snychronised to block potential simultaneous access to the PrintWriter
	 * that is used by both the client listening thread and the main thread. 
	 * @param msg
	 * @return Returns false if and only if the client should stop listening to the server. 
	 */	
	private synchronized boolean handleMessage(String msg) {
		if(msg == null) { // End of stream has been reached.
			JOptionPane.showMessageDialog(clientGUI.frame, "The Server has disconnected. The game will now shut down.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		String[] data = msg.split("\\s+"); // \s+ == regex for whitespace chars 1 or more times.
		/* Check prefix and act accordingly (see report). */
		if (data[0].equals("m")) {
			clientGUI.board.setPosition(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
		} else if(data[0].equals("d")) {
			int rollingPlayerID = Integer.parseInt(data[1]);
			clientGUI.gcPanel.setGameStatus(((rollingPlayerID==playerID)?"You have":clientGUI.prPanel.getPlayerName(rollingPlayerID)+" has")+" rolled a "+Integer.parseInt(data[2])+".");
		} else if(data[0].equals("p")) {
			int currentPlayerID = Integer.parseInt(data[1]);
			boolean isThisPlayersTurn = currentPlayerID==playerID;
			if (currentPlayerID != -1)
				clientGUI.gcPanel.setGameStatus("It is "+(isThisPlayersTurn?"your":clientGUI.prPanel.getPlayerName(currentPlayerID)+"'s")+" turn (P"+(currentPlayerID+1)+").");
			clientGUI.gcPanel.setRollEnabled(isThisPlayersTurn);
		} else if(data[0].equals("l")) {
			clientGUI.board.setNumberOfPlayers(data.length-1);
			for (int i = 1; i < data.length; i++)
				clientGUI.prPanel.setPlayerName(i-1, data[i]);
			clientGUI.frame.pack();
		} else if(data[0].equals("i")) {
			playerID = Integer.parseInt(data[1]);
			if (playerName.length()<1)
				playerName = "Player"+(playerID+1);
			clientGUI.frame.setTitle("Snakes and Ladders Client - "+playerName+" (P"+(playerID+1)+")");
			sendMessage("n "+playerName);
		} else if(data[0].equals("w")) {
			int winningPlayerID = Integer.parseInt(data[1]);
			JOptionPane.showMessageDialog(clientGUI.frame, ((winningPlayerID==playerID)?"You have":clientGUI.prPanel.getPlayerName(winningPlayerID)+" has")+" won the game!", "Game Over - P"+(winningPlayerID+1)+" Win", JOptionPane.INFORMATION_MESSAGE);
			return false;
		} else
			clientGUI.gcPanel.setGameStatus("Server: "+msg);
		return true;
	}
}