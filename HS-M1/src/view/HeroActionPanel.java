package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

public class HeroActionPanel extends JPanel
{
	private JTextArea heroDetails;
	private JButton hero;
	private JButton useHeroPower;
	private JButton endTurn;
	
	public HeroActionPanel()
	{
		this.setPreferredSize(new Dimension(250,500));
		this.setLayout(new GridLayout(0,1));
		heroDetails = new JTextArea();
		heroDetails.setBackground(Color.black);
		heroDetails.setForeground(Color.white);
		heroDetails.setEditable(false);
		hero = new JButton("Hero (if want to target)");
		hero.setFont(new Font("Consolas", Font.BOLD, 17));
		hero.setBackground(Color.BLACK);
		hero.setForeground(Color.red);
		useHeroPower = new JButton("Use HeroPower (on Selected)");
		useHeroPower.setFont(new Font("Consolas", Font.BOLD, 15));
		useHeroPower.setBackground(Color.BLACK);
		useHeroPower.setForeground(Color.red);
		endTurn = new JButton("End Turn");
		endTurn.setFont(new Font("Consolas", Font.BOLD, 30));
		endTurn.setBackground(Color.BLACK);
		endTurn.setForeground(Color.red);
		this.add(hero);
		this.add(heroDetails);
		this.add(useHeroPower);
		this.add(endTurn);
		this.revalidate();
		this.repaint();
	}
	public JTextArea getHeroDetails()
	{
		return heroDetails;
	}
	public void setHeroDetails(String s)
	{
		this.heroDetails.setText(s);
	}
	public JButton getHero()
	{
		return hero;
	}
	public void setHero(JButton hero)
	{
		this.hero = hero;
	}
	public JButton getUseHeroPower()
	{
		return useHeroPower;
	}
	public void setUseHeroPower(JButton useHeroPower)
	{
		this.useHeroPower = useHeroPower;
	}
	public JButton getEndTurn()
	{
		return endTurn;
	}
	public void setEndTurn(JButton endTurn)
	{
		this.endTurn = endTurn;
	}
}