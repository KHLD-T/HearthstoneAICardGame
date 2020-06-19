package view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class FieldHandPanel extends JPanel
{
	private JLabel field;
	private JPanel hand;
	private ArrayList<JButton> buttonField;
	private ArrayList<JButton> buttonHand;
	
	public FieldHandPanel(int n)
	{
		this.setPreferredSize(new Dimension(1000,500));
		this.setLayout(new BorderLayout());
		field = new JLabel();
		field.setPreferredSize(new Dimension(1000,250));
		field.setIcon(new ImageIcon("images/battle.png"));
		hand = new JPanel();
		field.setLayout(new FlowLayout());
		hand.setLayout(new FlowLayout());
		hand.setBackground(Color.BLACK);
		field.setBackground(new Color(40,40,40));
		buttonField = new ArrayList<JButton>();
		buttonHand = new ArrayList<JButton>();
		if(n==0)
		{
			this.add(field,BorderLayout.CENTER);
			this.add(hand,BorderLayout.SOUTH);
		}
		else
		{
			this.add(field,BorderLayout.CENTER);
			this.add(hand,BorderLayout.NORTH);
		}
		this.revalidate();
		this.repaint();
	}

	public JLabel getField()
	{
		return field;
	}

	public void setField(JLabel field)
	{
		this.field = field;
	}

	public JPanel getHand()
	{
		return hand;
	}

	public void setHand(JPanel hand)
	{
		this.hand = hand;
	}

	public ArrayList<JButton> getButtonField()
	{
		return buttonField;
	}

	public void setButtonField(ArrayList<JButton> buttonField)
	{
		this.buttonField = buttonField;
	}

	public ArrayList<JButton> getButtonHand()
	{
		return buttonHand;
	}

	public void setButtonHand(ArrayList<JButton> buttonHand)
	{
		this.buttonHand = buttonHand;
	}

}