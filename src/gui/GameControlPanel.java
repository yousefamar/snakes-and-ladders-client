package gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import core.GameClient;

@SuppressWarnings("serial")
public class GameControlPanel extends JPanel implements ActionListener {

	private GameClient client;
	private JButton rollDiceButton;
	private JLabel stateText;
	
	/**
	 * Creates a JPanel with a roll button and game status box.
	 * @param client
	 */
	public GameControlPanel(GameClient client) {
		this.client = client;
		
		/* Create and add roll dice button. */
		rollDiceButton = new JButton("Roll Dice", new ImageIcon(this.getClass().getResource("die.png")));
		rollDiceButton.setEnabled(false);
		add(rollDiceButton);
		rollDiceButton.addActionListener(this);
		
		add(new JPanel()); //Small gap.
		
		/* Create and add game status box. */
		JPanel stateTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		stateTextPanel.setPreferredSize(new Dimension(320, 60));
		stateText = new JLabel("Waiting for game to begin.");
		stateText.setFont(new Font("Serif", Font.PLAIN, 15));
		stateTextPanel.setBorder(BorderFactory.createTitledBorder("Game Status"));
		stateTextPanel.add(stateText);
		add(stateTextPanel);
		
		add(new JPanel()); //Small gap.
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==rollDiceButton)
			/* If button is pressed, send dice roll request to server. */
			client.sendMessage("r");
	}
	
	public void setGameStatus(String text) {
		stateText.setText(text);
		validate();
	}
	
	public void setRollEnabled(boolean enabled) {
		rollDiceButton.setEnabled(enabled);
	}
}