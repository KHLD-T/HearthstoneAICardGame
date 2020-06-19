package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import engine.AttackMove;
import engine.Game;
import engine.GameListener;
import engine.Position;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import view.CardPanel;
import view.GameView;

public class Controller extends JFrame implements GameListener,MouseListener,ActionListener
{
	private GameView view;
	private Game game;
	private JButton select;
	private JButton target;
	public Controller(Hero one, Hero two)
	{
		view = new GameView();
		try
		{
			game = new Game(one,two);
		} catch (FullHandException e)
		{
			ExceptionWindow f = new ExceptionWindow(e.getMessage(),e.getBurned());
		}
		catch (CloneNotSupportedException e){}
		game.setListener(this);
		this.updateHand();
		this.updateField();
		this.updateHeros();
		this.view.getHeroOnePanel().getCardPanel().getReset().addActionListener(this);
		this.view.getHeroTwoPanel().getCardPanel().getReset().addActionListener(this);
		
		this.view.getHeroOnePanel().getCardPanel().getAttack().addActionListener(this);
		this.view.getHeroTwoPanel().getCardPanel().getAttack().addActionListener(this);
		
		this.view.getHeroOnePanel().getHeroActionPanel().getEndTurn().addActionListener(this);
		this.view.getHeroTwoPanel().getHeroActionPanel().getEndTurn().addActionListener(this);
		
		this.view.getHeroOnePanel().getHeroActionPanel().getHero().addActionListener(this);
		this.view.getHeroTwoPanel().getHeroActionPanel().getHero().addActionListener(this);
		
		this.view.getHeroOnePanel().getCardPanel().getPlayCard().addActionListener(this);
		this.view.getHeroTwoPanel().getCardPanel().getPlayCard().addActionListener(this);
		
		this.view.getHeroOnePanel().getHeroActionPanel().getUseHeroPower().addActionListener(this);
		this.view.getHeroTwoPanel().getHeroActionPanel().getUseHeroPower().addActionListener(this);
		view.revalidate();
		view.repaint();
	}
	public void updateHand()
	{
		view.getHeroOnePanel().getFieldHandPanel().getButtonHand().clear();
		view.getHeroOnePanel().getFieldHandPanel().getHand().removeAll();
		view.getHeroTwoPanel().getFieldHandPanel().getButtonHand().clear();
		view.getHeroTwoPanel().getFieldHandPanel().getHand().removeAll();
		for(int i=0;i<this.game.getCurrentHero().getHand().size();i++)
		{
			String s = this.game.getCurrentHero().getHand().get(i).getName();
			JButton b = new JButton(s);
			b.setPreferredSize(new Dimension(145,200));
			b.setBackground(Color.BLACK);
			if(contains(s))
				b.setForeground(new Color(102,0,153));
			else
				b.setForeground(new Color(0,153,0));
			b.addMouseListener(this);
			b.setActionCommand("card");
			view.getHeroOnePanel().getFieldHandPanel().getButtonHand().add(b);
			view.getHeroOnePanel().getFieldHandPanel().getHand().add(b);
			view.getHeroOnePanel().getFieldHandPanel().getHand().revalidate();
			view.getHeroOnePanel().getFieldHandPanel().getHand().repaint();
		}
		for(int i=0;i<this.game.getOpponent().getHand().size();i++)
		{
			JButton b = new JButton();
			b.setIcon(new ImageIcon("images/card.png"));
			b.setPreferredSize(new Dimension(145,200));
			b.setBackground(Color.BLACK);
			b.setForeground(Color.ORANGE);
			b.setActionCommand("card");
			view.getHeroTwoPanel().getFieldHandPanel().getButtonHand().add(b);
			view.getHeroTwoPanel().getFieldHandPanel().getHand().add(b);
			view.getHeroTwoPanel().getFieldHandPanel().getHand().revalidate();
			view.getHeroTwoPanel().getFieldHandPanel().getHand().repaint();
		}
		view.getHeroTwoPanel().getFieldHandPanel().getHand().revalidate();
		view.getHeroTwoPanel().getFieldHandPanel().getHand().repaint();
		view.getHeroOnePanel().getFieldHandPanel().getHand().revalidate();
		view.getHeroOnePanel().getFieldHandPanel().getHand().repaint();
	}
	public void updateField()
	{
		view.getHeroOnePanel().getFieldHandPanel().getButtonField().clear();
		view.getHeroOnePanel().getFieldHandPanel().getField().removeAll();
		view.getHeroTwoPanel().getFieldHandPanel().getButtonField().clear();
		view.getHeroTwoPanel().getFieldHandPanel().getField().removeAll();
		for(int i=0;i<this.game.getCurrentHero().getField().size();i++)
		{
			String s = this.game.getCurrentHero().getField().get(i).getName();
			JButton b = new JButton(s);
			b.setPreferredSize(new Dimension(145,200));
			b.setBackground(Color.BLACK);
			if(contains(s))
				b.setForeground(new Color(102,0,153));
			else
				b.setForeground(new Color(0,153,0));
			b.addMouseListener(this);
			b.setActionCommand("card");
			view.getHeroOnePanel().getFieldHandPanel().getButtonField().add(b);
			view.getHeroOnePanel().getFieldHandPanel().getField().add(b);
			view.getHeroOnePanel().getFieldHandPanel().getField().revalidate();
			view.getHeroOnePanel().getFieldHandPanel().getField().repaint();
		}
		for(int i=0;i<this.game.getOpponent().getField().size();i++)
		{
			String s = this.game.getOpponent().getField().get(i).getName();
			JButton b = new JButton(s);
			b.setPreferredSize(new Dimension(145,200));
			b.setBackground(Color.BLACK);
			if(contains(s))
				b.setForeground(new Color(102,0,153));
			else
				b.setForeground(new Color(0,153,0));
			b.addMouseListener(this);
			b.setActionCommand("card");
			view.getHeroTwoPanel().getFieldHandPanel().getButtonField().add(b);
			view.getHeroTwoPanel().getFieldHandPanel().getField().add(b);
			view.getHeroTwoPanel().getFieldHandPanel().getField().revalidate();
			view.getHeroTwoPanel().getFieldHandPanel().getField().repaint();
		}
		view.getHeroOnePanel().getFieldHandPanel().getField().revalidate();
		view.getHeroOnePanel().getFieldHandPanel().getField().repaint();
		view.getHeroTwoPanel().getFieldHandPanel().getField().revalidate();
		view.getHeroTwoPanel().getFieldHandPanel().getField().repaint();
	}
	public void updateHeros()
	{
		Hero p = this.game.getCurrentHero();
		Hero p1 = this.game.getOpponent();
		
		
		view.getHeroOnePanel().getHeroActionPanel().setHeroDetails("Current Hero" + "\n"+"Name: " + p.getName()+"\n" + "Current Health: " + p.getCurrentHP()+"\n"+ "Current mana: " + p.getCurrentManaCrystals()+"\n"+"Total Mana: " + p.getTotalManaCrystals()+"\n"+"Cards in deck left: "+p.getDeck().size());
		view.getHeroOnePanel().getHeroActionPanel().getHeroDetails().setFont(new Font("Consolas", Font.BOLD, 17));
		if(p instanceof Hunter)
			view.getHeroOnePanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/r2.png"));
		else if (p instanceof Warlock)
			view.getHeroOnePanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/g2.png"));
		else if (p instanceof Priest)
			view.getHeroOnePanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/a2.png"));
		else if (p instanceof Mage)
			view.getHeroOnePanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/j2.png"));
		else if (p instanceof Paladin)
			view.getHeroOnePanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/u2.png"));
		view.getHeroTwoPanel().getHeroActionPanel().setHeroDetails("Opponent" + " | " + "Hand size: " +p1.getHand().size()+ "\n"+"Name: " + p1.getName()+"\n" + "Current Health: " + p1.getCurrentHP()+"\n"+ "Current mana: " + p1.getCurrentManaCrystals()+"\n"+"Total Mana: " + p1.getTotalManaCrystals()+"\n"+"Cards in deck left: "+p1.getDeck().size());
		view.getHeroTwoPanel().getHeroActionPanel().getHeroDetails().setFont(new Font("Consolas", Font.BOLD, 17));
		if(p1 instanceof Hunter)
			view.getHeroTwoPanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/r2.png"));
		else if (p1 instanceof Warlock)
			view.getHeroTwoPanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/g2.png"));
		else if (p1 instanceof Priest)
			view.getHeroTwoPanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/a2.png"));
		else if (p1 instanceof Mage)
			view.getHeroTwoPanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/j2.png"));
		else if (p1 instanceof Paladin)
			view.getHeroTwoPanel().getHeroActionPanel().getHero().setIcon(new ImageIcon("images/u2.png"));
		
		view.getHeroOnePanel().getHeroActionPanel().getHeroDetails().revalidate();
		view.getHeroOnePanel().getHeroActionPanel().getHeroDetails().repaint();
		
		view.getHeroTwoPanel().getHeroActionPanel().getHeroDetails().revalidate();
		view.getHeroTwoPanel().getHeroActionPanel().getHeroDetails().repaint();
	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
		JButton b = (JButton)e.getSource();
		int r;
		Card x = null;
		if(view.getHeroOnePanel().getFieldHandPanel().getButtonField().contains(b))
		{
			r=view.getHeroOnePanel().getFieldHandPanel().getButtonField().indexOf(b);
			x=this.game.getCurrentHero().getField().get(r);
		}
		else if(view.getHeroOnePanel().getFieldHandPanel().getButtonHand().contains(b))
		{
			r=view.getHeroOnePanel().getFieldHandPanel().getButtonHand().indexOf(b);
			x=this.game.getCurrentHero().getHand().get(r);
		}
		else if(view.getHeroTwoPanel().getFieldHandPanel().getButtonField().contains(b))
		{
			r=view.getHeroTwoPanel().getFieldHandPanel().getButtonField().indexOf(b);
			x=this.game.getOpponent().getField().get(r);
		}
		if(select == null)
		{
			select =b;
			this.view.getHeroOnePanel().getCardPanel().setSelect("Selected:" + b.getText());
			this.view.getHeroOnePanel().getCardPanel().revalidate();
			this.view.getHeroOnePanel().getCardPanel().repaint();
		}
		else
		{
			if(x instanceof Minion)
			{
				target=b;
				this.view.getHeroOnePanel().getCardPanel().setTarget("Target:" + b.getText());
				this.view.getHeroOnePanel().getCardPanel().revalidate();
				this.view.getHeroOnePanel().getCardPanel().repaint();
			}
		}
		
	}
	@Override
	public void mouseEntered(MouseEvent e)
	{
		JButton b = (JButton)e.getSource();
		int r;
		Card x = null;
		CardPanel panel = new CardPanel();
		if(view.getHeroOnePanel().getFieldHandPanel().getButtonField().contains(b))
		{
			panel = view.getHeroOnePanel().getCardPanel();
			r=view.getHeroOnePanel().getFieldHandPanel().getButtonField().indexOf(b);
			x=this.game.getCurrentHero().getField().get(r);
		}
		else if(view.getHeroOnePanel().getFieldHandPanel().getButtonHand().contains(b))
		{
			panel = view.getHeroOnePanel().getCardPanel();
			r=view.getHeroOnePanel().getFieldHandPanel().getButtonHand().indexOf(b);
			x=this.game.getCurrentHero().getHand().get(r);
		}
		else if(view.getHeroTwoPanel().getFieldHandPanel().getButtonField().contains(b))
		{
			panel = view.getHeroTwoPanel().getCardPanel();
			r=view.getHeroTwoPanel().getFieldHandPanel().getButtonField().indexOf(b);
			x=this.game.getOpponent().getField().get(r);
		}
		if(x instanceof Minion)
		{
			panel.setCardDetails("Name: " + x.getName() + "\n" + "Mana Cost: " + x.getManaCost() + "\n" + "Rarity: " + x.getRarity().toString()  + " | " + "Attack: " + ((Minion)x).getAttack()  + "\n" + "Current HP: " +((Minion)x).getCurrentHP() + " | " + "Taunt: " + ((Minion)x).isTaunt() +  "\n" + "Divine: " + ((Minion)x).isDivine() +  " | "+ "Charge: " + !(((Minion)x).isSleeping()));
			panel.getCardDetails().setFont(new Font("Consolas", Font.BOLD, 14));
			panel.setBackground(Color.BLACK);
			panel.getCardDetails().setForeground(new Color(0,153,0));
			panel.revalidate();
			panel.repaint();
		}
		else
		{
			panel.setCardDetails("Name: " + x.getName() + "\n" + "Mana Cost: " + x.getManaCost() + "\n" + "Rarity: " + x.getRarity().toString());
			panel.getCardDetails().setFont(new Font("Consolas", Font.BOLD, 14));
			panel.setBackground(Color.BLACK);
			panel.getCardDetails().setForeground(new Color(102,0,153));
			panel.revalidate();
			panel.repaint();
		}
		
	}
	@Override
	public void mouseExited(MouseEvent arg0)
	{
		    view.getHeroOnePanel().getCardPanel().setCardDetails("Selected: Attacker/Card to play" + "\n" + "Target:could be Minion or Hero" + "\n" + "Target hero: click on Image");
		    view.getHeroOnePanel().getCardPanel().getCardDetails().setFont(new Font("Consolas", Font.BOLD, 14));
			view.getHeroOnePanel().getCardPanel().revalidate();
			view.getHeroOnePanel().getCardPanel().repaint();
			
			view.getHeroTwoPanel().getCardPanel().setCardDetails("");
			view.getHeroTwoPanel().getCardPanel().revalidate();
			view.getHeroTwoPanel().getCardPanel().repaint();
	}
	public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}
	
	public void playCard() throws NotYourTurnException, NotEnoughManaException, InvalidTargetException, FullFieldException
	{
		try
		{
		int r = this.view.getHeroOnePanel().getFieldHandPanel().getButtonHand().indexOf(select);
		int t = this.view.getHeroTwoPanel().getFieldHandPanel().getButtonField().indexOf(target);
		if(contains(select.getText()))
		{
			if(select.getText().contains("Curse of Weakness") || select.getText().contains("Flamestrike") || select.getText().contains("Holy Nova") || select.getText().contains("Multi-Shot") || select.getText().contains("Twisting Nether"))
				this.game.getCurrentHero().castSpell((AOESpell) this.game.getCurrentHero().getHand().get(r),this.game.getOpponent().getField());
			else if ((select.getText().contains("Divine Spirit") || select.getText().contains("Kill Command") || select.getText().contains("Polymorph") || select.getText().contains("Pyroblast") || select.getText().contains("Seal of Champions") || select.getText().contains("Shadow Word: Death")) && this.view.getHeroTwoPanel().getFieldHandPanel().getButtonField().contains(target)==true)
				this.game.getCurrentHero().castSpell((MinionTargetSpell)this.game.getCurrentHero().getHand().get(r), this.game.getOpponent().getField().get(t));
			else if (select.getText().contains("Kill Command") || select.getText().contains("Pyroblast") && this.view.getHeroTwoPanel().getHeroActionPanel().getHero().equals(target))
				this.game.getCurrentHero().castSpell((HeroTargetSpell)this.game.getCurrentHero().getHand().get(r), this.game.getOpponent());
			else if (select.getText().contains("Level Up!"))
				this.game.getCurrentHero().castSpell((FieldSpell)this.game.getCurrentHero().getHand().get(r));
			else if(select.getText().contains("Siphon Soul"))
				this.game.getCurrentHero().castSpell((LeechingSpell)this.game.getCurrentHero().getHand().get(r), this.game.getOpponent().getField().get(t));
		}
		else
		{
			this.game.getCurrentHero().playMinion((Minion) this.game.getCurrentHero().getHand().get(r));
		}
		this.updateField();
		this.updateHand();
		this.updateHeros();
		select = null;
		target = null;
		this.view.getHeroOnePanel().getCardPanel().setSelect("Selected Card: ");
		this.view.getHeroOnePanel().getCardPanel().setTarget("Attack target: ");
		this.view.getHeroOnePanel().getCardPanel().revalidate();
		this.view.getHeroOnePanel().getCardPanel().repaint();
		}
		catch(NotEnoughManaException| InvalidTargetException| FullFieldException e)
		{
			ExceptionWindow ex = new ExceptionWindow(e.getMessage());
		}
	}
	public static boolean contains(String s)
	{
		String [] spells = {"Curse of Weakness","Divine Spirit","Flamestrike","Holy Nova","Kill Command","Level Up!","Multi-Shot","Polymorph","Pyroblast","Seal of Champions","Shadow Word: Death","Siphon Soul","Twisting Nether"};
		for(int i=0;i<spells.length;i++)
		{
			if (s.equals(spells[i]))
				return true;
		}
		return false;
	}
	public void useHeroPower() throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	{
		try
		{
		if (this.game.getCurrentHero() instanceof Mage)
		{
			if(this.view.getHeroOnePanel().getCardPanel().getTarget().getText().contains(this.game.getOpponent().getName()))
				this.game.getCurrentHero().useHeroPower(this.game.getOpponent());
			else
			{
				int t = this.view.getHeroTwoPanel().getFieldHandPanel().getButtonField().indexOf(select);
				this.game.getCurrentHero().useHeroPower(this.game.getOpponent().getField().get(t));
			}
		}
		else if (this.game.getCurrentHero() instanceof Hunter)
			this.game.getCurrentHero().useHeroPower();
		else if (this.game.getCurrentHero() instanceof Priest)
		{
			if(this.view.getHeroOnePanel().getCardPanel().getTarget().getText().contains(this.game.getCurrentHero().getName()))
				this.game.getCurrentHero().useHeroPower(this.game.getCurrentHero());
			else
			{
				int t = this.view.getHeroOnePanel().getFieldHandPanel().getButtonField().indexOf(select);
				this.game.getCurrentHero().useHeroPower(this.game.getCurrentHero().getField().get(t));
			}
		}
		else if(this.game.getCurrentHero() instanceof Warlock)
			this.game.getCurrentHero().useHeroPower();
		else if(this.game.getCurrentHero() instanceof Paladin)
			this.game.getCurrentHero().useHeroPower();
		this.updateField();
		this.updateHand();
		this.updateHeros();
		}
		catch(FullHandException e)
		{
			ExceptionWindow ex = new ExceptionWindow(e.getMessage(),e.getBurned());
		}
		catch(Exception e)
		{
			ExceptionWindow ex = new ExceptionWindow(e.getMessage());
		}
	}
	public void endTurn() throws FullHandException, CloneNotSupportedException
	{
		try
		{
		this.game.endTurn();
		this.updateField();
		this.updateHand();
		this.updateHeros();
		select = null;
		target = null;
		this.view.getHeroOnePanel().getCardPanel().setSelect("Selected Card: ");
		this.view.getHeroOnePanel().getCardPanel().setTarget("Attack target: ");
		this.view.getHeroOnePanel().getCardPanel().revalidate();
		this.view.getHeroOnePanel().getCardPanel().repaint();
		}
		catch(FullHandException e)
		{
			ExceptionWindow ex = new ExceptionWindow(e.getMessage(),e.getBurned());
		}
		catch(Exception e)
		{
			ExceptionWindow ex = new ExceptionWindow(e.getMessage());
		}
	}
	public void attack() throws CannotAttackException, NotYourTurnException, TauntBypassException, InvalidTargetException, NotSummonedException
	{
		try
		{
		int a = this.view.getHeroOnePanel().getFieldHandPanel().getButtonField().indexOf(select);
		if(this.view.getHeroTwoPanel().getFieldHandPanel().getButtonField().contains(select))
			throw new NotYourTurnException("Not your turn!");
		if(a==-1)
			throw new NotSummonedException("Minion is not summoned");
		int t;
		if(target.equals(this.view.getHeroOnePanel().getHeroActionPanel().getHero()))
			this.game.getCurrentHero().attackWithMinion(this.game.getCurrentHero().getField().get(a), this.game.getCurrentHero());
		
		else if (target.equals(this.view.getHeroTwoPanel().getHeroActionPanel().getHero()))
			this.game.getCurrentHero().attackWithMinion(this.game.getCurrentHero().getField().get(a), this.game.getOpponent());
		else
		{
			if(this.view.getHeroOnePanel().getFieldHandPanel().getButtonField().contains(target))
			{
				t = this.view.getHeroOnePanel().getFieldHandPanel().getButtonField().indexOf(target);
				this.game.getCurrentHero().attackWithMinion(this.game.getCurrentHero().getField().get(a),this.game.getCurrentHero().getField().get(t));
			}
			else if (this.view.getHeroTwoPanel().getFieldHandPanel().getButtonField().contains(target))
			{
				t = this.view.getHeroTwoPanel().getFieldHandPanel().getButtonField().indexOf(target);
				this.game.getCurrentHero().attackWithMinion(this.game.getCurrentHero().getField().get(a),this.game.getOpponent().getField().get(t));
			}
		}
		this.updateField();
		this.updateHand();
		this.updateHeros();
		this.view.revalidate();
		this.view.repaint();
		}
		catch(CannotAttackException | TauntBypassException | InvalidTargetException | NotSummonedException | NotYourTurnException e)
		{
			ExceptionWindow ex = new ExceptionWindow(e.getMessage());
		}
		finally
		{
			select = null;
			target = null;
			this.view.getHeroOnePanel().getCardPanel().setSelect("Selected Card: ");
			this.view.getHeroOnePanel().getCardPanel().setTarget("Attack target: ");
			this.view.getHeroOnePanel().getCardPanel().revalidate();
			this.view.getHeroOnePanel().getCardPanel().repaint();
		}
	}

	public void onGameOver()
	{
		this.setVisible(false);
		JFrame end = new JFrame();
		end.setBounds(750, 500, 500, 300);
		end.setVisible(true);
		end.setBackground(Color.black);
		end.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JLabel winner = new JLabel();
		if(this.game.getCurrentHero().getCurrentHP()==0)
		{
			winner.setText("Winner:" + this.game.getOpponent().getName());
			if(this.game.getOpponent() instanceof Hunter)
				winner.setIcon(new ImageIcon("images/r2.png"));
			else if (this.game.getOpponent() instanceof Warlock)
				winner.setIcon(new ImageIcon("images/g2.png"));
			else if (this.game.getOpponent() instanceof Priest)
				winner.setIcon(new ImageIcon("images/a2.png"));
			else if (this.game.getOpponent() instanceof Mage)
				winner.setIcon(new ImageIcon("images/j2.png"));
			else if (this.game.getOpponent() instanceof Paladin)
				winner.setIcon(new ImageIcon("images/u2.png"));
			end.add(winner);
		}
		else
		{
			winner.setText("Winner:" + this.game.getCurrentHero().getName());
			if(this.game.getCurrentHero() instanceof Hunter)
				winner.setIcon(new ImageIcon("images/r2.png"));
			else if (this.game.getCurrentHero() instanceof Warlock)
				winner.setIcon(new ImageIcon("images/g2.png"));
			else if (this.game.getCurrentHero() instanceof Priest)
				winner.setIcon(new ImageIcon("images/a2.png"));
			else if (this.game.getCurrentHero() instanceof Mage)
				winner.setIcon(new ImageIcon("images/j2.png"));
			else if (this.game.getCurrentHero() instanceof Paladin)
				winner.setIcon(new ImageIcon("images/u2.png"));
			end.add(winner);
		}
		winner.setFont(new Font("Consolas",Font.BOLD,15));
		winner.setBackground(Color.BLACK);
		winner.setOpaque(true);
		winner.setForeground(Color.red);
		winner.setHorizontalAlignment(winner.CENTER);
		winner.setVerticalAlignment(winner.CENTER);
		this.dispose();
	}
	private void aiStartTurn() throws CloneNotSupportedException, InvalidTargetException
	{
			for(int k=0;k<4;k++)
			{
				ArrayList<Minion> aiField = new ArrayList<Minion>();
				ArrayList<Minion> humanField =new ArrayList<Minion>();
				for(int i =0;i<this.game.getCurrentHero().getField().size();i++)
					humanField.add(this.game.getCurrentHero().getField().get(i));
				for(int i =0;i<this.game.getOpponent().getField().size();i++)
					aiField.add(this.game.getOpponent().getField().get(i));
				
				Position p = new Position(this.game.getOpponent().getCurrentHP(),this.game.getCurrentHero().getCurrentHP() ,aiField, humanField);
				if(this.game.getOpponent().getField().size()==0)
				{
					Hero t = this.game.getOpponent();
					
					for(int i=0;i<t.getHand().size();i++)
					{
						if(t.getHand().get(i) instanceof Minion && t.getHand().get(i).getManaCost()<=t.getCurrentManaCrystals())
							{
							this.game.aiPlayMinion((Minion) t.getHand().get(i));
							this.updateField();
							this.updateHand();
							this.updateHeros();
							}
					}
					
					if(this.game.getOpponent().getCurrentManaCrystals()>=2)
						{
						this.game.playHeroPower();
						this.updateField();
						this.updateHand();
						this.updateHeros();
						}
					this.game.playSpell();
					aiField.clear();
					humanField.clear();
					
					for(int i =0;i<this.game.getCurrentHero().getField().size();i++)
						humanField.add(this.game.getCurrentHero().getField().get(i));
					for(int i =0;i<this.game.getOpponent().getField().size();i++)
						aiField.add(this.game.getOpponent().getField().get(i));
				}
				else
				{
					AttackMove attack = game.getAttacker(5,p,true);
					if(attack.val==2000 && this.game.getOpponent().getField().get(attack.i).isAttacked()==false)
					{
						this.game.aiAttackWithMinion(this.game.getOpponent().getField().get(attack.i), this.game.getCurrentHero());
						this.updateField();
						this.updateHand();
						this.updateHeros();
					}
					else if (attack.val!=-10000 && this.game.getCurrentHero().getField().size()>0 && this.game.getOpponent().getField().get(attack.i).isAttacked()==false)
					{
						this.game.aiAttackWithMinion(this.game.getOpponent().getField().get(attack.i),this.game.getCurrentHero().getField().get(attack.j));
						this.updateField();
						this.updateHand();
						this.updateHeros();
					}
					else if (attack.val!=-10000 && this.game.getCurrentHero().getField().size()==0 && this.game.getOpponent().getField().get(attack.i).isAttacked()==false)
					{
						this.game.aiAttackWithMinion(this.game.getOpponent().getField().get(attack.i), this.game.getCurrentHero());
						this.updateField();
						this.updateHand();
						this.updateHeros();
					}
					
					for(int i =0;i<this.game.getCurrentHero().getField().size();i++)
						humanField.add(this.game.getCurrentHero().getField().get(i));
					for(int i =0;i<this.game.getOpponent().getField().size();i++)
						aiField.add(this.game.getOpponent().getField().get(i));
					
					p = new Position(this.game.getOpponent().getCurrentHP(),this.game.getCurrentHero().getCurrentHP() ,aiField, humanField);					
					
					AttackMove playcard = game.getCard(5,p,true);
			
					if(playcard.val!=-10000 && this.game.getOpponent().getHand().get(playcard.i).getManaCost()<=game.getOpponent().getCurrentManaCrystals())// -10000 is either no minion or not enough mana
						{
						this.game.aiPlayMinion((Minion) this.game.getOpponent().getHand().get(playcard.i));
						this.updateField();
						this.updateHand();
						this.updateHeros();
						}
			
					if(this.game.getOpponent().getCurrentManaCrystals()>=2)
						{
						this.game.playHeroPower();
						this.updateField();
						this.updateHand();
						this.updateHeros();
						}
					this.game.playSpell();
					}
				}
			}

public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("reset"))
		{
			try
			{
				if(((JButton)e.getSource()).equals(this.view.getHeroTwoPanel().getCardPanel().getReset()))
					throw new NotYourTurnException("Not you Turn!");
			select = null;
			target = null;
			this.view.getHeroOnePanel().getCardPanel().setSelect("Selected:");
			this.view.getHeroOnePanel().getCardPanel().setTarget("Target:");
			this.view.getHeroOnePanel().getCardPanel().revalidate();
			this.view.getHeroOnePanel().getCardPanel().repaint();
			return;
			}
			catch(Exception e1)
			{
				ExceptionWindow ex = new ExceptionWindow(e1.getMessage());
			}
		}
		 if(((JButton)e.getSource()).getText().equals("Play selected Card"))
			try
			{
				if(((JButton)e.getSource()).equals(this.view.getHeroTwoPanel().getCardPanel().getPlayCard()))
					throw new NotYourTurnException("Not you Turn!");
				else
					this.playCard();
			} catch (NotYourTurnException | NotEnoughManaException | InvalidTargetException | FullFieldException e1)
			{
				ExceptionWindow ex = new ExceptionWindow(e1.getMessage());
			}
		else if(((JButton)e.getSource()).getText().equals("Attack with selected"))
			try
			{
				if(((JButton)e.getSource()).equals(this.view.getHeroTwoPanel().getCardPanel().getAttack()))
					throw new NotYourTurnException("Not you Turn!");
				this.attack();
			} catch (CannotAttackException | NotYourTurnException | TauntBypassException | InvalidTargetException
					| NotSummonedException e1)
			{
				// TODO Auto-generated catch block
				ExceptionWindow ex = new ExceptionWindow(e1.getMessage());
			}
		else if(((JButton)e.getSource()).getText().equals("Hero (if want to target)")) 
			{
			if(((JButton)e.getSource()).equals(view.getHeroOnePanel().getHeroActionPanel().getHero()))
			{
				target=view.getHeroOnePanel().getHeroActionPanel().getHero();
				this.view.getHeroOnePanel().getCardPanel().setTarget("Attack target: " + this.game.getCurrentHero().getName());
			}
			else if(((JButton)e.getSource()).equals(view.getHeroTwoPanel().getHeroActionPanel().getHero()))
			{
				target=view.getHeroTwoPanel().getHeroActionPanel().getHero();
				this.view.getHeroOnePanel().getCardPanel().setTarget("Attack target: " + this.game.getOpponent().getName());
			}
			this.view.getHeroOnePanel().getCardPanel().revalidate();
			this.view.getHeroOnePanel().getCardPanel().repaint();
			return;
			}
		 else if(((JButton)e.getSource()).getText().equals("End Turn"))
			try
			{
				if(((JButton)e.getSource()).equals(this.view.getHeroTwoPanel().getHeroActionPanel().getEndTurn()))
					throw new NotYourTurnException("Not you Turn!");
				this.endTurn();
			} catch (FullHandException | CloneNotSupportedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NotYourTurnException e1)
			{
				// TODO Auto-generated catch block
				ExceptionWindow ex = new ExceptionWindow(e1.getMessage());
			}
		 finally
		 {
				try
				{
					
					this.aiStartTurn();
					this.game.aiEndTurn();
				} catch (Exception e1)
				{
					try
					{
						this.game.aiEndTurn();
					} catch (CloneNotSupportedException e2)
					{}
				}
		 }
		else if(((JButton)e.getSource()).getText().equals("Use HeroPower (on Selected)"))
		{	
			try
			{
				if(((JButton)e.getSource()).equals(this.view.getHeroTwoPanel().getHeroActionPanel().getUseHeroPower()))
					throw new NotYourTurnException("Not you Turn!");
				else
					this.useHeroPower();
			}
			catch(NotYourTurnException | NotEnoughManaException | HeroPowerAlreadyUsedException | FullHandException | FullFieldException | CloneNotSupportedException e2)
			{
				ExceptionWindow ex = new ExceptionWindow(e2.getMessage());
			}
		}
		    this.updateField();
			this.updateHand();
			this.updateHeros();
			select = null;
			target = null;
			this.view.getHeroOnePanel().getCardPanel().setSelect("Selected Card: ");
			this.view.getHeroOnePanel().getCardPanel().setTarget("Attack target: ");
			this.view.getHeroOnePanel().getCardPanel().revalidate();
			this.view.getHeroOnePanel().getCardPanel().repaint();
		 
	}
}