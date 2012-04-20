package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.text.NumberFormat;
import javax.swing.*;

import core.GameClient;

@SuppressWarnings("serial")
public class ClientGUI extends JPanel implements ActionListener {

	public JFrame frame;
	public SnakesAndLaddersGUI board;
	public GameControlPanel gcPanel;
	public PlayerRosterPanel prPanel;
	
	private JTextField hostField;
	private JFormattedTextField portField;
	private JTextField nameField;
	private JButton connectButton;

	/**
	 * Creates a JPanel that allows input for information to connect to a game server
	 * and, on connecting, will build the game GUI. 
	 * @param frame
	 */
	public ClientGUI(JFrame frame) {
		super(new GridBagLayout());
		this.frame = frame;
		
		/* Create and add title. */
		JLabel title = new JLabel(" Snakes And Ladders Client ", JLabel.CENTER);
		title.setFont(new Font("MV Boli", Font.BOLD, 25));
		GridBagConstraints cons0 = new GridBagConstraints();
		cons0.gridy = 0;
		cons0.insets = new Insets(6, 0, 0, 0);
		add(title, cons0);
		
		/* Create and add subtitle. */
		JLabel subtitle = new JLabel("(C) Yousef Amar 2012");
		subtitle.setFont(new Font("MV Boli", Font.PLAIN, 10));
		GridBagConstraints cons1 = new GridBagConstraints();
		cons1.gridy = 1;
		cons1.anchor = GridBagConstraints.NORTHEAST;
		cons1.insets = new Insets(0, 0, 20, 40);
		add(subtitle, cons1);
		
		/* Create and add host input. */
		JPanel hostPanel = new JPanel(new GridLayout());
  		hostPanel.add(new JLabel("Host:", SwingConstants.LEFT));
  		hostField = new JTextField("localhost");
  		hostField.setPreferredSize(new Dimension(80, 25));
  		hostPanel.add(hostField);
  		GridBagConstraints cons2 = new GridBagConstraints();
		cons2.gridy = 2;
  		add(hostPanel, cons2);
		
  		/* Create and add port input. */
		JPanel portPanel = new JPanel(new GridLayout());
  		portPanel.add(new JLabel("Port:", SwingConstants.LEFT));
  		NumberFormat format = NumberFormat.getIntegerInstance();
  		format.setGroupingUsed(false);
  		portField = new JFormattedTextField(format);
  		portField.setPreferredSize(new Dimension(80, 25));
  		portField.setValue(8250);
  		portPanel.add(portField);
  		GridBagConstraints cons3 = new GridBagConstraints();
		cons3.gridy = 3;
  		add(portPanel, cons3);
  		
  		/* Create and add name input (can be left empty for default). */
  		JPanel namePanel = new JPanel(new GridLayout());
  		namePanel.add(new JLabel("Name:", SwingConstants.LEFT));
  		nameField = new JTextField();
  		nameField.setPreferredSize(new Dimension(80, 25));
  		namePanel.add(nameField);
  		GridBagConstraints cons4 = new GridBagConstraints();
		cons4.gridy = 4;
  		add(namePanel, cons4);
  		
  		/* Create and add conenct button. */
  		connectButton = new JButton("Connect");
  		connectButton.addActionListener(this);
  		/* Create and add a sub-panel that uses FlowLayout to fix size issues. */
  		JPanel buttonPanel = new JPanel();
  		buttonPanel.add(connectButton);
  		GridBagConstraints cons5 = new GridBagConstraints();
		cons5.gridy = 5;
		cons5.insets = new Insets(15, 0, 5, 0);
  		add(buttonPanel, cons5);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == connectButton) {
			/* Get host from host field. */
			String host = hostField.getText();
			/* Get port from port field. */
			int port = (Integer) portField.getValue();
			/* Get name from name field and remove whitespace. */
			String name = nameField.getText().replaceAll("\\s+", "");
			//if (name.length()>10)
				//name = name.substring(0, 10);

			GameClient client = null;
			try {
				/* Attempt to open socket and create client object. */
				client = new GameClient(new Socket(host, port), this, name);
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(frame, "Could not resolve host.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (ConnectException e) {
				JOptionPane.showMessageDialog(frame, "Could not connect to host.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/* Clear GUI. */
			removeAll();
			
			/* Create and add game board. */
			board = new SnakesAndLaddersGUI();
			board.setPreferredSize(new Dimension(300, 300));
			board.setMinimumSize(new Dimension(300, 300));
	  		add(board);
	  		
	  		/* Create and add player roster. */
	  		GridBagConstraints cons1 = new GridBagConstraints();
	  		cons1.gridx = 1;
	  		cons1.anchor = GridBagConstraints.NORTH;
	  		add(prPanel = new PlayerRosterPanel(), cons1);
	  		
	  		/* Create and add control panel. */
	  		GridBagConstraints cons2 = new GridBagConstraints();
	  		cons2.gridy = 1;
	  		cons2.gridwidth = 2;
	  		cons2.insets = new Insets(10, 10, 10, 0);
	  		cons2.anchor = GridBagConstraints.WEST;
	  		add(gcPanel = new GameControlPanel(client), cons2);
	  		
	  		frame.pack();
	  		
	  		/* Start a thread that will listen to the server.
	  		 * All subsequent code is called from this thread. */
	  		System.out.println("Starting Snakes and Ladders Client.");
	  		new Thread(client, "Server Listener Thread").start();
		}
	}
}