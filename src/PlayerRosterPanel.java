import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class PlayerRosterPanel extends JPanel {

	private JLabel[] playerNames = new JLabel[5];
	
	public PlayerRosterPanel() {
		super(new GridBagLayout());
		
		JLabel title = new JLabel("Players:");
		title.setFont(new Font("Helvetica", Font.BOLD, 19));
		GridBagConstraints cons0 = new GridBagConstraints();
		cons0.anchor = GridBagConstraints.WEST;
		cons0.ipady = 40;
		add(title, cons0);
		
		JPanel namePanel = new JPanel(new GridLayout(0, 1));
		namePanel.setBackground(Color.LIGHT_GRAY);
		for (int i = 0; i < playerNames.length; i++) {
			playerNames[i] = new JLabel(" P"+(i+1)+" - ....................  ");
			playerNames[i].setFont(new Font("Helvetica", Font.ITALIC, 18));
			playerNames[i].setForeground(SnakesAndLaddersGUI.playerCols[i]);
			namePanel.add(playerNames[i]);
			if (i!=playerNames.length-1)
				namePanel.add(new JLabel());
		}
		GridBagConstraints cons1 = new GridBagConstraints();
		cons1.gridy = 1;
		add(namePanel, cons1);
	}
	
	public void setPlayerName(int id, String name) {
		playerNames[id].setText(" P"+(id+1)+" - "+name+"  ");
	}

	public String getPlayerName(int id) {
		return playerNames[id].getText().substring(6).trim();
	}
}