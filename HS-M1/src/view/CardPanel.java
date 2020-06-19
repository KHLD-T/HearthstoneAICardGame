package view;

import java.awt.*;

import javax.swing.*;

public class CardPanel extends JPanel
{
	private JTextArea cardDetails;
	private JButton playCard;
	private JButton attack;
	private JButton reset;
	private JTextField select;
	private JTextField target;
	
	public CardPanel()
	{
		this.setPreferredSize(new Dimension(250,500));
		this.setLayout(new GridLayout(0,1));
		cardDetails = new JTextArea();
		cardDetails.setEditable(false);
		cardDetails.setBackground(Color.BLACK);
		playCard = new JButton("Play selected Card");
		playCard.setForeground(Color.red);
		playCard.setBackground(Color.BLACK);
		playCard.setFont(new Font("Consolas", Font.BOLD, 20));
		attack = new JButton("Attack with selected");
		attack.setForeground(Color.red);
		attack.setBackground(Color.black);
		attack.setFont(new Font("Consolas", Font.BOLD, 19));
		reset = new JButton("Reset selected & target");
		reset.setForeground(Color.red);
		reset.setBackground(Color.BLACK);
		reset.setFont(new Font("Consolas", Font.BOLD, 17));
		reset.setActionCommand("reset");
		target = new JTextField("Target: ");
		target.setEditable(false);
		target.setFont(new Font("Consolas", Font.BOLD, 17));
		target.setBackground(Color.BLACK);
		target.setForeground(Color.white);
		select = new JTextField("Selected:");
		select.setFont((new Font("Consolas", Font.BOLD, 17)));
		select.setEditable(false);
		select.setBackground(Color.BLACK);
		select.setForeground(Color.white);
		this.add(cardDetails);
		this.add(select);
		this.add(target);
		this.add(attack);
		this.add(playCard);
		this.add(reset);
		this.revalidate();
		this.repaint();
	}

	public JButton getReset()
	{
		return reset;
	}

	public void setReset(JButton reset)
	{
		this.reset = reset;
	}

	public void setCardDetails(String s)
	{
		this.cardDetails.setText(s);
		this.cardDetails.setForeground(Color.white);
	}

	public void setPlayCard(JButton playCard)
	{
		this.playCard = playCard;
	}

	public void setAttack(JButton attack)
	{
		this.attack = attack;
	}

	public void setSelect(String s)
	{
		this.select.setText(s);
	}

	public JTextArea getCardDetails()
	{
		return cardDetails;
	}

	public JButton getPlayCard()
	{
		return playCard;
	}

	public JButton getAttack()
	{
		return attack;
	}

	public JTextField getSelect()
	{
		return select;
	}

	public JTextField getTarget()
	{
		return target;
	}

	public void setTarget(String s)
	{
		this.target.setText(s);
	}
	
	/*
	 * public static void main(String[] args) { JFrame x = new JFrame();
	 * x.setBounds(500, 500, 1000, 1000); x.setVisible(true); x.add(new
	 * CardPanel()); }
	 */
	 
}
