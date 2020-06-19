package controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import model.cards.Card;
import model.cards.minions.Minion;

public class ExceptionWindow extends JFrame implements ActionListener
{
	private JTextField message;
	private JButton button;
	private JTextArea m;
	
	public ExceptionWindow(String s)
	{
		this.setBounds(750,500,300,200);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		message = new JTextField(s);
		message.setFont(new Font("Consolas",Font.BOLD,13));
		message.setForeground(Color.blue);
		message.setBackground(Color.WHITE);
		message.setHorizontalAlignment(message.CENTER);
		message.setEditable(false);
		button = new JButton("Ok");
		button.setFont(new Font("Consolas",Font.BOLD,17));
		button.setBackground(Color.white);
		button.setForeground(Color.GRAY);

		button.addActionListener(this);
		this.add(message,BorderLayout.CENTER);
		this.add(button,BorderLayout.SOUTH);
		this.revalidate();
		this.repaint();
	}
	public ExceptionWindow(String s, Card x)
	{
		this.setBounds(750,500,300,200);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		m = new JTextArea();
		m.setBackground(Color.black);
		m.setForeground(Color.red);
		m.setOpaque(true);
		m.setEditable(false);
		if(x instanceof Minion)
		{
			m.setText( s + "\n" +"Card Burned:" + "\n"+"Name: " + x.getName() + "\n" + "Mana Cost: " + x.getManaCost() + "\n" + "Rarity: " + x.getRarity().toString()  + " | " + "Attack: " + ((Minion)x).getAttack()  + "\n" + "Current HP: " +((Minion)x).getCurrentHP() + " | " + "Taunt: " + ((Minion)x).isTaunt() +  "\n" + "Divine: " + ((Minion)x).isDivine() +  " | "+ "Charge: " + !(((Minion)x).isSleeping()));
		}
		else
		{
			m.setText(s + "\n" +"Card Burned:" + "\n"+"Name: " + x.getName() + "\n" + "Mana Cost: " + x.getManaCost() + "\n" + "Rarity: " + x.getRarity().toString());
		}
		button = new JButton("Ok");
		button.addActionListener(this);
		this.add(m,BorderLayout.CENTER);
		this.add(button,BorderLayout.SOUTH);
		this.revalidate();
		this.repaint();
	}
	public void actionPerformed(ActionEvent e)
	{
		this.dispose();
	}
}