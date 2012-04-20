package gui;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class PlayerRosterPanel extends JPanel {

	private JLabel[] playerNames = new JLabel[5];
	
	/**
	 * Creates a JPanel that accommodates all player names.
	 */
	public PlayerRosterPanel() {
		super(new GridBagLayout());
		
		/* Create and add title. */
		JLabel title = new JLabel("Players:");
		title.setFont(new Font("Helvetica", Font.BOLD, 19));
		GridBagConstraints cons0 = new GridBagConstraints();
		cons0.anchor = GridBagConstraints.WEST;
		cons0.ipady = 40;
		add(title, cons0);
		
		/* Create and add name panel. */
		JPanel namePanel = new JPanel(new GridLayout(0, 1));
		namePanel.setPreferredSize(new Dimension(185, 220));
		namePanel.setBackground(Color.LIGHT_GRAY);
		namePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		for (int i = 0; i < playerNames.length; i++) {
			/* Initialse all names to "<None>". */
			playerNames[i] = new JLabel(" P"+(i+1)+" - <None>  ");
			playerNames[i].setFont(new Font("Helvetica", Font.ITALIC, 18));
			playerNames[i].setForeground(SnakesAndLaddersGUI.playerCols[i]);
			namePanel.add(playerNames[i]);
			if (i != playerNames.length-1)
				namePanel.add(new JLabel()); //Empty gap.
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