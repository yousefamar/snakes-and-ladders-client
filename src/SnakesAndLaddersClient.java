import java.io.*;
import java.net.*;
import javax.swing.*;

public class SnakesAndLaddersClient {
	
	private static final String[] playerCols = { "blue", "green", "red", "orange", "yellow" };
	
	private int playerID;
	private String playerName;
	private Socket clientSoc;
	private BufferedReader in;
	private PrintWriter out;
	
	public GamePanel gamePanel;
	
	public SnakesAndLaddersClient(String playerName, GamePanel gamePanel, Socket clientSoc) throws IOException {
		this.playerName = playerName;
		this.gamePanel = gamePanel;
		this.clientSoc = clientSoc;
		in = new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
		out = new PrintWriter(clientSoc.getOutputStream(), true);
	}

	public void startListeningToServer() {
		/* Start a thread that listens to incoming messages. */
		new Thread(new Runnable() {
			public void run() {
				try {
					while (handleMessage(in.readLine()));
					clientSoc.close();
					System.exit(0);
				} catch (SocketException e) {
					JOptionPane.showMessageDialog(gamePanel.frame, "The Server has disconnected. The game will now shut down.", "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				} catch (Exception e) {
					/* Shut down quietly if the main thread has ended. */
				}
			}
		}, "Incoming Message Handling Thread").start();
	}
	
	public void sendMessage(String msg) {
		out.println(msg);
	}
	
	private boolean handleMessage(String msg) throws IOException {
		if(msg == null) {
			JOptionPane.showMessageDialog(gamePanel.frame, "The Server has disconnected. The game will now shut down.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		String[] data = msg.split("\\s+");
		if (data[0].equals("m")) {
			gamePanel.board.setPosition(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
		} else if(data[0].equals("d")) {
			int rollingPlayerID = Integer.parseInt(data[1]);
			gamePanel.gcPanel.setGameStatus(((rollingPlayerID==playerID)?"You have":gamePanel.prPanel.getPlayerName(rollingPlayerID)+" has")+" rolled a "+Integer.parseInt(data[2])+".");
		} else if(data[0].equals("p")) {
			int currentPlayerID = Integer.parseInt(data[1]);
			boolean isThisPlayersTurn = currentPlayerID==playerID;
			if (currentPlayerID != -1)
				gamePanel.gcPanel.setGameStatus("It is "+(isThisPlayersTurn?"your":gamePanel.prPanel.getPlayerName(currentPlayerID)+"'s")+" turn (P"+(currentPlayerID+1)+").");
			gamePanel.gcPanel.setRollEnabled(isThisPlayersTurn);
		} else if(data[0].equals("l")) {
			gamePanel.board.setNumberOfPlayers(data.length-1);
			for (int i = 1; i < data.length; i++)
				gamePanel.prPanel.setPlayerName(i-1, data[i]);
			gamePanel.frame.pack();
		} else if(data[0].equals("i")) {
			playerID = Integer.parseInt(data[1]);
			gamePanel.frame.setTitle("Snakes and Ladders Client - "+playerName+" (P"+(playerID+1)+", "+playerCols[playerID]+")");
			sendMessage("n "+playerName);
		} else if(data[0].equals("w")) {
			int winningPlayerID = Integer.parseInt(data[1]);
			JOptionPane.showMessageDialog(gamePanel.frame, ((winningPlayerID==playerID)?"You have":gamePanel.prPanel.getPlayerName(winningPlayerID)+" has")+" won the game!", "Game Over - P"+(winningPlayerID+1)+" Win", JOptionPane.INFORMATION_MESSAGE);
			return false;
		} else
			gamePanel.gcPanel.setGameStatus("Server: "+msg);
		return true;
	}
}
