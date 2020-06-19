package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import engine.Game;
import exceptions.FullHandException;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;

public class MainGameRunner extends JFrame implements ActionListener
{
	private Controller controller;
	private Hero one;
	private Hero two;
	private int firstPos;
	private int secondPos;
	private ArrayList<JButton> heros;
	private ArrayList<JButton> heros2;
	public MainGameRunner(){
	
	firstPos=-1;
	secondPos=-1;
	this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	this.setUndecorated(false);
	this.setVisible(true);
	this.setLayout(new GridLayout(0,6));
	heros = new ArrayList<JButton>();
	heros2 = new ArrayList<JButton>();
	JButton hunter = new JButton("Hunter");
	hunter.setIcon(new ImageIcon("images/rexxar.png"));
	hunter.addActionListener(this);
	hunter.setFont(new Font("Consolas", Font.BOLD, 18));
	hunter.setBackground(Color.black);
	hunter.setForeground(Color.BLUE);
	JButton mage = new JButton("Mage");
	mage.setIcon(new ImageIcon("images/j.png"));
	mage.addActionListener(this);
	mage.setFont(new Font("Consolas",Font.BOLD,18));
	mage.setBackground(Color.black);
	mage.setForeground(Color.BLUE);
	JButton paladin = new JButton("Paladin");
	paladin.setIcon(new ImageIcon("images/u.png"));
	paladin.addActionListener(this);
	paladin.setFont(new Font("Consolas",Font.BOLD,18));
	paladin.setBackground(Color.black);
	paladin.setForeground(Color.BLUE);
	JButton priest = new JButton("Priest");
	priest.addActionListener(this);
	priest.setIcon(new ImageIcon("images/a.png"));
	priest.setFont(new Font("Consolas",Font.BOLD,18));
	priest.setBackground(Color.black);
	priest.setForeground(Color.BLUE);
	JButton warlock = new JButton("Warlock");
	warlock.setIcon(new ImageIcon("images/g.png"));
	warlock.addActionListener(this);
	warlock.setFont(new Font("Consolas",Font.BOLD,18));
	warlock.setBackground(Color.black);
	warlock.setForeground(Color.blue);
	JButton hunter1 = new JButton("Hunter");
	hunter1.setIcon(new ImageIcon("images/rexxar.png"));
	hunter1.addActionListener(this);
	hunter1.setFont(new Font("Consolas",Font.BOLD,18));
	hunter1.setForeground(Color.red);
	hunter1.setBackground(Color.black);
	JButton mage1 = new JButton("Mage");
	mage1.setIcon(new ImageIcon("images/j.png"));
	mage1.addActionListener(this);
	mage1.setFont(new Font("Consolas",Font.BOLD,18));
	mage1.setForeground(Color.red);
	mage1.setBackground(Color.black);
	JButton paladin1 = new JButton("Paladin");
	paladin1.setIcon(new ImageIcon("images/u.png"));
	paladin1.addActionListener(this);
	paladin1.setFont(new Font("Consolas",Font.BOLD,18));
	paladin1.setForeground(Color.red);
	paladin1.setBackground(Color.black);
	JButton priest1 = new JButton("Priest");
	priest1.setIcon(new ImageIcon("images/a.png"));
	priest1.addActionListener(this);
	priest1.setFont(new Font("Consolas",Font.BOLD,18));
	priest1.setForeground(Color.red);
	priest1.setBackground(Color.black);
	JButton warlock1 = new JButton("Warlock");
	warlock1.setIcon(new ImageIcon("images/g.png"));
	warlock1.addActionListener(this);
	warlock1.setFont(new Font("Consolas",Font.BOLD,18));
	warlock1.setForeground(Color.red);
	warlock1.setBackground(Color.black);
	heros.add(hunter);
	heros.add(mage);
	heros.add(paladin);
	heros.add(priest);
	heros.add(warlock);
	heros2.add(hunter1);
	heros2.add(mage1);
	heros2.add(paladin1);
	heros2.add(priest1);
	heros2.add(warlock1);
	JLabel first = new JLabel("Choose 1st hero: ");
	first.setFont(new Font("Consolas",Font.ITALIC,17));
	first.setForeground(Color.BLUE);
	first.setBackground(Color.gray);
	first.setOpaque(true);
	first.setHorizontalAlignment(first.CENTER);
	first.setVerticalAlignment(first.CENTER);
	JLabel second = new JLabel("Choose 2nd hero: ");
	second.setFont(new Font("Consolas",Font.ITALIC,17));
	second.setBackground(Color.GRAY);
	second.setForeground(Color.red);
	second.setOpaque(true);
	second.setHorizontalAlignment(second.CENTER);
    second.setVerticalAlignment(second.CENTER);
	this.add(first);
	this.add(hunter);
	this.add(mage);
	this.add(paladin);
	this.add(priest);
	this.add(warlock);
	
	this.add(second);
	this.add(hunter1);
	this.add(mage1);
	this.add(paladin1);
	this.add(priest1);
	this.add(warlock1);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.revalidate();
	this.repaint();
}
public int getFirstPos()
{
	return firstPos;
}
public void setFirstPos(int firstPos)
{
	this.firstPos = firstPos;
}
public int getSecondPos()
{
	return secondPos;
}
public void setSecondPos(int secondPos)
{
	this.secondPos = secondPos;
}
public ArrayList<JButton> getHeros()
{
	return heros;
}
public void setHeros(ArrayList<JButton> heros)
{
	this.heros = heros;
}
public void actionPerformed(ActionEvent e)
{
	JButton b = (JButton)e.getSource();
	if(firstPos==-1 && secondPos==-1)
	{
		firstPos = this.heros.indexOf(b);
	}
	else if(secondPos==-1 && firstPos!=-1)
	{
		secondPos = this.heros2.indexOf(b);
		Hero one =null;
		Hero two =null;
		if(heros.get(firstPos).getText().equals("Hunter"))
			try
			{
				one = new Hunter();
			} catch (IOException | CloneNotSupportedException e5)
			{
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
		else if (heros.get(firstPos).getText().equals("Mage"))
			try
			{
				one = new Mage();
			} catch (IOException | CloneNotSupportedException e5)
			{
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
		else if (heros.get(firstPos).getText().equals("Paladin"))
			try
			{
				one = new Paladin();
			} catch (IOException | CloneNotSupportedException e6)
			{
				// TODO Auto-generated catch block
				e6.printStackTrace();
			}
		else if (heros.get(firstPos).getText().equals("Priest"))
			try
			{
				one = new Priest();
			} catch (IOException | CloneNotSupportedException e6)
			{
				// TODO Auto-generated catch block
				e6.printStackTrace();
			}
		else if (heros.get(firstPos).getText().equals("Warlock"))
			try
			{
				one = new Warlock();
			} catch (IOException | CloneNotSupportedException e5)
			{
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
		
		if(heros2.get(secondPos).getText().equals("Hunter"))
			try
			{
				two = new Hunter();
			} catch (IOException | CloneNotSupportedException e4)
			{
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
		else if (heros2.get(secondPos).getText().equals("Mage"))
			try
			{
				two = new Mage();
			} catch (IOException | CloneNotSupportedException e4)
			{
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
		else if (heros2.get(secondPos).getText().equals("Paladin"))
			try
			{
				two = new Paladin();
			} catch (IOException | CloneNotSupportedException e4)
			{
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
		else if (heros2.get(secondPos).getText().equals("Priest"))
			try
			{
				two = new Priest();
			} catch (IOException | CloneNotSupportedException e3)
			{
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		else if (heros2.get(secondPos).getText().equals("Warlock"))
			try
			{
				two = new Warlock();
			} catch (IOException | CloneNotSupportedException e2)
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			this.setVisible(false);
			this.dispose();
			controller = new Controller(one,two);
	}
}
public static void main(String[] args)
{
	new MainGameRunner();
}
}