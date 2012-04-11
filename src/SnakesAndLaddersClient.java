import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SnakesAndLaddersClient {
	
	private static final String[] playerCols = { "blue", "green", "red", "orange", "yellow" };
	
	private JFrame frame;
	private Socket clientSoc;
	private BufferedReader in;
	private PrintWriter out;
	private SnakesAndLaddersGUI board;
	
	public SnakesAndLaddersClient(Socket clientSoc) throws IOException {
		this.clientSoc = clientSoc;
		in = new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
		out = new PrintWriter(clientSoc.getOutputStream(), true);
		
		frame = new JFrame();
      	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  		frame.setSize(400,400);
  		frame.add(board = new SnakesAndLaddersGUI());
      	frame.setLocationRelativeTo(null);
      	frame.setResizable(false);
      	frame.setVisible(true);
	}

	private void startListeningToServer() {
		/* Start a thread that listens to incoming messages. */
		new Thread(new Runnable() {
			public void run() {
				try {
					while (handleMessage(in.readLine()));
					clientSoc.close();
					System.exit(0);
				} catch (SocketException e) {
					System.out.println("Server has disconnected.");
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		/* NB: Although there is no reason why the above should run in its own thread
		 * if all the client does is listen to the server, it is implemented that way
		 * to allow for expanding the code to include two-way communication. */
		
		//TODO: Consider letting the clients roll die. 
		//out.println("r");
	}
	
	private boolean handleMessage(String msg) throws IOException {
		if(msg == null) {
			System.out.println("Server has disconnected.");
			return false;
		}
		String[] data = msg.split("\\s+");
		if (data[0].equals("m"))
			board.setPosition(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
		else if(data[0].equals("n"))
			board.setNumberOfPlayers(Integer.parseInt(data[1]));
		else if(data[0].equals("i")) {
			frame.setTitle("Player "+(Integer.parseInt(data[1])+1)+" ("+playerCols[Integer.parseInt(data[1])]+")");
		} else if(data[0].equals("w")) {
			JOptionPane.showMessageDialog(frame, "Player "+(Integer.parseInt(data[1])+1)+" has won the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
			
		//System.out.println(">"+msg);
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println("Starting Snakes and Ladders Client.");
		
		String host = "localhost";
		int port = 8250;
		
		if (args.length<1)
			System.out.println("To change default host and port ("+host+":"+port+"), input the host and port as command-line arguments respectively.");
		if (args.length>0)
			host = args[0];
		if (args.length>1)
			port = Integer.parseInt(args[1]);
		
		System.out.println("Connecting to port "+port+" on "+host+".");
		
		try {
			new SnakesAndLaddersClient(new Socket(host, port)).startListeningToServer();
		} catch (UnknownHostException e) {
			System.err.println("Error: Could not resolve host.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
