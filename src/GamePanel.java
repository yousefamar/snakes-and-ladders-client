import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.text.NumberFormat;
import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {

	public JFrame frame;
	public SnakesAndLaddersGUI board;
	public GameControlPanel gcPanel;
	public PlayerRosterPanel prPanel;
	
	private JTextField hostField;
	private JFormattedTextField portField;
	private JTextField nameField;
	private JButton startButton;

	public GamePanel(JFrame frame) {
		super(new GridBagLayout());
		this.frame = frame;
		
		JLabel title = new JLabel(" Snakes And Ladders Client ", JLabel.CENTER);
		title.setFont(new Font("MV Boli", Font.BOLD, 25));
		GridBagConstraints cons0 = new GridBagConstraints();
		cons0.gridy = 0;
		cons0.insets = new Insets(6, 0, 0, 0);
		add(title, cons0);
		
		JLabel subtitle = new JLabel("(C) Yousef Amar 2012");
		subtitle.setFont(new Font("MV Boli", Font.PLAIN, 10));
		GridBagConstraints cons1 = new GridBagConstraints();
		cons1.gridy = 1;
		cons1.anchor = GridBagConstraints.NORTHEAST;
		cons1.insets = new Insets(0, 0, 20, 40);
		add(subtitle, cons1);
		
		JPanel hostPanel = new JPanel(new GridLayout());
  		hostPanel.add(new JLabel("Host:", SwingConstants.LEFT));
  		hostField = new JTextField("localhost");
  		hostField.setPreferredSize(new Dimension(80, 25));
  		hostPanel.add(hostField);
  		GridBagConstraints cons2 = new GridBagConstraints();
		cons2.gridy = 2;
  		add(hostPanel, cons2);
		
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
  		
  		JPanel namePanel = new JPanel(new GridLayout());
  		namePanel.add(new JLabel("Name:", SwingConstants.LEFT));
  		nameField = new JTextField();
  		nameField.setPreferredSize(new Dimension(80, 25));
  		namePanel.add(nameField);
  		GridBagConstraints cons4 = new GridBagConstraints();
		cons4.gridy = 4;
  		add(namePanel, cons4);
  		
  		startButton = new JButton("Start");
  		startButton.addActionListener(this);
  		/* Create a sub-panel that uses FlowLayout to fix size issues. */
  		JPanel buttonPanel = new JPanel();
  		buttonPanel.add(startButton);
  		GridBagConstraints cons5 = new GridBagConstraints();
		cons5.gridy = 5;
		cons5.insets = new Insets(15, 0, 5, 0);
  		add(buttonPanel, cons5);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == startButton) {
			String host = hostField.getText();
			int port = (Integer) portField.getValue();
			String name = nameField.getText();
			if (name.length()>12)
				name = name.substring(0, 12);

			SnakesAndLaddersClient client = null;
			try {
				client = new SnakesAndLaddersClient(name, this, new Socket(host, port));
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(frame, "Could not resolve host.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (ConnectException e) {
				JOptionPane.showMessageDialog(frame, "Could not connect to host.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			removeAll();
			
			board = new SnakesAndLaddersGUI();
			board.setPreferredSize(new Dimension(300, 300));
			board.setMinimumSize(new Dimension(300, 300));
	  		add(board);
	  		
	  		GridBagConstraints cons1 = new GridBagConstraints();
	  		cons1.gridx = 1;
	  		cons1.anchor = GridBagConstraints.NORTH;
	  		add(prPanel = new PlayerRosterPanel(), cons1);
	  		
	  		GridBagConstraints cons2 = new GridBagConstraints();
	  		cons2.gridy = 1;
	  		cons2.gridwidth = 2;
	  		cons2.insets = new Insets(10, 10, 10, 0);
	  		cons2.anchor = GridBagConstraints.WEST;
	  		add(gcPanel = new GameControlPanel(client), cons2);
	  		
	  		frame.pack();
	  		
	  		client.startListeningToServer();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Starting Snakes and Ladders Client.");

		JFrame frame = new JFrame("Snakes And Ladders Client");
      	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      	GamePanel gamePanel = new GamePanel(frame);
  		frame.add(gamePanel);
  		frame.pack();
  		frame.setResizable(false);
  		frame.setLocationRelativeTo(null);
      	frame.setVisible(true);
	}
}
