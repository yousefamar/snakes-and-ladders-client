import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameControlPanel extends JPanel implements ActionListener {

	private SnakesAndLaddersClient client;
	private JButton rollDiceButton;
	private JLabel stateText;
	
	public GameControlPanel(SnakesAndLaddersClient client) {
		this.client = client;
		
		rollDiceButton = new JButton("Roll Dice (Space)", new ImageIcon(this.getClass().getResource("die.png")));
		rollDiceButton.setEnabled(false);
		rollDiceButton.setMnemonic(KeyEvent.VK_SPACE);
		add(rollDiceButton);
		rollDiceButton.addActionListener(this);
		
		add(new JPanel());
		
		JPanel stateTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		stateTextPanel.setPreferredSize(new Dimension(250, 60));
		stateText = new JLabel("Waiting for game to begin.");
		stateText.setFont(new Font("Serif", Font.PLAIN, 15));
		stateTextPanel.setBorder(BorderFactory.createTitledBorder("Game Status"));
		stateTextPanel.add(stateText);
		add(stateTextPanel);
		
		add(new JPanel());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==rollDiceButton)
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