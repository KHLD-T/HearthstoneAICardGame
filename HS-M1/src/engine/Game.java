package engine;

import java.io.IOException;
import java.util.ArrayList;

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
import model.cards.Rarity;
import model.cards.minions.Icehowl;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.CurseOfWeakness;
import model.cards.spells.FieldSpell;
import model.cards.spells.Flamestrike;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.PriorityQueueSpells;
import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.HeroListener;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;

public class Game implements ActionValidator,HeroListener {
	private Hero human;
	private Hero ai;
	private Hero currentHero;
	private Hero opponent;
	private GameListener listener;
	
	public Game(Hero p1, Hero p2) throws FullHandException, CloneNotSupportedException
	{
		human=p2;
		ai=p1;
		
		currentHero=p2;
		opponent=p1;
		
		currentHero.setTotalManaCrystals(1);
		currentHero.setCurrentManaCrystals(1);
		opponent.setTotalManaCrystals(1);
		opponent.setCurrentManaCrystals(1);
		currentHero.setListener(this);
		currentHero.setValidator(this);
		
		opponent.setValidator(this);
		opponent.setListener(this);
		
		currentHero.drawCard();
		currentHero.drawCard();
		currentHero.drawCard();
		
		opponent.drawCard();
		opponent.drawCard();
		opponent.drawCard();
		opponent.drawCard();
	}

	public Hero getCurrentHero() {
		return currentHero;
	}

	public Hero getOpponent() {
		return opponent;
	}

	public void setListener(GameListener listener)
	{
		this.listener = listener;
	}
	public void onHeroDeath()
	{
			listener.onGameOver();
	}
	public void damageOpponent(int amount)
	{
		int health = opponent.getCurrentHP();
		opponent.setCurrentHP(health-amount);
	}
	public void validateTurn(Hero user) throws NotYourTurnException
	{
		if(user!=human)
			throw new NotYourTurnException("Not your turn!");
	}
	public void validateAttack(Minion attacker,Minion target) throws CannotAttackException, NotSummonedException, TauntBypassException, InvalidTargetException
	{
		if(attacker.isSleeping() || attacker.isAttacked())
			throw new CannotAttackException("Minion cannot attack this turn!");
		if(attacker.getAttack()==0)
			throw new CannotAttackException("Minion has zero attack points");
		if(currentHero.getField().contains(attacker) && currentHero.getField().contains(target))
			throw new InvalidTargetException("Cannot attack friendly minion");
		if(currentHero.getField().contains(attacker)==false || opponent.getField().contains(target)==false)
			throw new NotSummonedException("Minion not in field");
		if(hasTauntMinion(opponent.getField())==true && !(target.isTaunt()))
			throw new TauntBypassException("Opponent has taunt minion,kill first");
		
	}
	public void validateAttack(Minion attacker,Hero target) throws CannotAttackException, NotSummonedException, TauntBypassException, InvalidTargetException
	{
		if(attacker.isSleeping() || attacker.isAttacked())
			throw new CannotAttackException("Minion cannot attack this turn!");
		if(attacker.getAttack()==0)
			throw new CannotAttackException("Minion has zero attack points");
		if(currentHero.getField().contains(attacker)==false)
			throw new NotSummonedException("Minion not in field");
		if(hasTauntMinion(opponent.getField())==true)
			throw new TauntBypassException("Opponent has taunt minion,kill first");
		if(attacker.getName().equals("Icehowl"))
			throw new InvalidTargetException("Icehowl attacks only minion");
		if(currentHero.getField().contains(attacker) && target.equals(currentHero))
			throw new InvalidTargetException("Cannot attack your own Hero");	
	}
	public void validateManaCost(Card card) throws NotEnoughManaException
	{
		int hero = currentHero.getCurrentManaCrystals();
		int c = card.getManaCost();
		if(c>hero)
			throw new NotEnoughManaException("Not enough mana!");
	}
	public void validatePlayingMinion(Minion minion) throws FullFieldException
	{
		if(currentHero.getField().size()==7)
			throw new FullFieldException("You have no space in your field");
	}
	 public void validateUsingHeroPower(Hero hero) throws NotEnoughManaException, HeroPowerAlreadyUsedException
	 {
		 if(hero.getCurrentManaCrystals()<2)
			 throw new NotEnoughManaException("Not enough mana!");
		 if(hero.isHeroPowerUsed()==true)
			 throw new HeroPowerAlreadyUsedException("Already used power in this turn");
	 }
	public static boolean hasTauntMinion(ArrayList<Minion> field)
	{
		for(int i =0;i<field.size();i++)
		{
			if(field.get(i).isTaunt()==true)
				return true;
		}
		return false;
	}
	public void endTurn() throws FullHandException, CloneNotSupportedException
	{
		int number = currentHero.getTotalManaCrystals();
		currentHero.setCurrentManaCrystals(number+1);
		currentHero.setTotalManaCrystals(number+1);
		currentHero.setHeroPowerUsed(false);
		for(int i =0;i<currentHero.getField().size();i++)
		{
			currentHero.getField().get(i).setAttacked(false);
			currentHero.getField().get(i).setSleeping(false);
		}
		currentHero.drawCard();
	}
	public void playHeroPower() throws CloneNotSupportedException
	{
		if(this.ai.getCurrentManaCrystals()>=2)
		{
		if(this.ai instanceof Mage)
		{
			if(this.currentHero.getCurrentHP()==1 || this.currentHero.getField().size()==0)
				this.aiUseHeroPower(currentHero);
			else
				this.aiUseHeroPower(getStrongest(this.currentHero.getField()));
		}
		else if(this.ai instanceof Hunter)
			this.aiUseHeroPower();
		else if(this.ai instanceof Priest)
		{
			if(this.ai.getCurrentHP()<=10)
				this.aiUseHeroPower(ai);
			else
				this.aiUseHeroPower(getStrongest(this.ai.getField()));
		}
		else if (this.ai.getHand().size()<10 && this.ai instanceof Warlock && this.ai.getCurrentHP()>2)
		{
			this.aiUseHeroPower();
		}
		else if (this.ai instanceof Paladin && this.ai.getField().size()<7 && this.hasLevelUp())
		{
			this.aiUseHeroPower();
		}
		}
	}
	public boolean hasLevelUp()
	{
		for(int i =0;i<this.ai.getHand().size();i++)
		{
			if(this.ai.getHand().get(i).getName().equals("Level Up!"))
				return true;
		}
		return false;
	}
	public AttackMove getAttacker(int depth,Position p,boolean isAI)
	{
		// gets the best minion to attack with and best minion to targets
		AttackMove res = new AttackMove();
		int maxEval = -10000;
		//similar idea to minimax func
		for(int i =0;i<p.aiField.size();i++)
		{
			if(p.aiField.get(i).isSleeping() || p.aiField.get(i).isAttacked() || p.aiField.get(i).getAttack()==0||hasTauntMinion(p.humanField))
				continue;
			if(p.aiField.get(i).getName().equals("Icehowl"))
				continue;
			int org = p.human;
			
			p.human-=p.aiField.get(i).getAttack();
			Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
			int val = minimax(x,depth-1,!isAI,-100000,100000);
			p.human=org;
			if(val>maxEval)
			{
				maxEval=val;
				res.val=val;
				res.i=i;
			}
		}
		for(int i =0;i<p.aiField.size();i++)
		{
			if(p.aiField.get(i).isSleeping() || p.aiField.get(i).isAttacked() || p.aiField.get(i).getAttack()==0)
				continue;
			Minion m = p.aiField.get(i);
			int org = m.getCurrentHP();
			for(int j =0;j<p.humanField.size();j++)
			{				
				Minion n = p.humanField.get(j);
				if(n.isTaunt()==false && hasTauntMinion(p.humanField))
					continue;
				int org2 = n.getCurrentHP();
				if(m.getAttack()>=org2 && org>n.getAttack())//case1
				{
					p.humanField.remove(n);
					m.setCurrentHP(m.getCurrentHP()-n.getAttack());
					Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
					int val = minimax(x,depth-1,!isAI,-100000,100000);
					//now if that position of board is better get the attacker -> i and traget -> j
					if(val>maxEval)
					{
						maxEval=val;
						res.i=i;
						res.j=j;
						res.val=val;
					}
					m.setCurrentHP(org);
					p.humanField.add(j, n);
				}
				else if(m.getAttack()>=n.getCurrentHP() && m.getCurrentHP()<=n.getAttack())
				{
					p.humanField.remove(n);
					p.aiField.remove(m);
					Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
					int val = minimax(x,depth-1,!isAI,-100000,100000);
					if(val>maxEval)
					{
						maxEval=val;
						res.i=i;
						res.j=j;
						res.val=val;
					}
					p.aiField.add(i, m);
					p.humanField.add(j, n);
				}
				else if (n.getAttack()>=org && org2>m.getAttack())
				{
					p.aiField.remove(m);
					n.setCurrentHP(n.getCurrentHP()-m.getAttack());
					Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
					int val = minimax(x,depth-1,!isAI,-100000,100000);
					if(val>maxEval)
					{
						maxEval=val;
						res.i=i;
						res.j=j;
						res.val=val;
					}
					n.setCurrentHP(org2);
					p.aiField.add(i, m);
				}
				else 
				{
					n.setCurrentHP(n.getCurrentHP()-m.getAttack());
					m.setCurrentHP(m.getCurrentHP()-n.getAttack());
					Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
					int val = minimax(x,depth-1,!isAI,-100000,100000);
					if(val>maxEval)
					{
						maxEval=val;
						res.i=i;
						res.j=j;
						res.val=val;
					}
					n.setCurrentHP(org2);
					m.setCurrentHP(org);
				}
			}
		}
		return res;
	}
	public int minimax(Position p, int depth, boolean isAI,int alpha,int beta)
	{
		if(p.ai<=0)
			return -2000;
		if(p.human<=0)
			return 2000;
		//if in final state or any field is empty
		if(depth==0 || p.aiField.size()==0 || p.humanField.size()==0)
			return p.sum();
		if(isAI)
		{
			//value never will be reached
			int maxEval = -10000;
			// every minion on field will attack every minion on human field
			// creating  new postion/possiblity to get it's total
			for(int i =0;i<p.aiField.size();i++)
			{
				if(p.aiField.get(i).isSleeping() || p.aiField.get(i).isAttacked() || p.aiField.get(i).getAttack()==0||hasTauntMinion(p.humanField))
					continue;
				if(p.aiField.get(i).getName().equals("Icehowl"))
					continue;
				int org = p.human;
				
				p.human-=p.aiField.get(i).getAttack();
				Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
				int val = minimax(x,depth-1,!isAI,alpha,beta);
				maxEval = Math.max(val, maxEval);
				p.human=org;
				alpha=Math.max(alpha, val);
				if(beta<=alpha)
					break;
			}
			for(int i =0;i<p.aiField.size();i++)
			{
				if(p.aiField.get(i).isSleeping() || p.aiField.get(i).isAttacked() || p.aiField.get(i).getAttack()==0)
					continue;
				Minion m = p.aiField.get(i);
				int org = m.getCurrentHP();
				for(int j =0;j<p.humanField.size();j++)
				{
					Minion n = p.humanField.get(j);
					if(n.isTaunt()==false && hasTauntMinion(p.humanField))
						continue;
					int org2 = n.getCurrentHP();
					//attack case 1
					if(m.getAttack()>=org2 && org>n.getAttack())
					{
						p.humanField.remove(n);
						m.setCurrentHP(m.getCurrentHP()-n.getAttack());
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimax(x,depth-1,!isAI,alpha,beta);
						maxEval = Math.max(val, maxEval);
						m.setCurrentHP(org);
						p.humanField.add(j, n);
						alpha=Math.max(alpha, val);
						if(beta<=alpha)
							break;
						continue;
					}
					//attack case 1
					else if(m.getAttack()>=n.getCurrentHP() && m.getCurrentHP()<=n.getAttack())
					{
						p.humanField.remove(n);
						p.aiField.remove(m);
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimax(x,depth-1,!isAI,alpha,beta);
						maxEval = Math.max(val, maxEval);
						p.aiField.add(i, m);
						p.humanField.add(j, n);
						alpha=Math.max(alpha, val);
						if(beta<=alpha)
							break;
						continue;
					}
					//attack case 1
					else if (n.getAttack()>=org && org2>m.getAttack())
					{
						p.aiField.remove(m);
						n.setCurrentHP(n.getCurrentHP()-m.getAttack());
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimax(x,depth-1,!isAI,alpha,beta);
						maxEval = Math.max(val, maxEval);
						n.setCurrentHP(org2);
						p.aiField.add(i, m);
						alpha=Math.max(alpha, val);
						if(beta<=alpha)
							break;
						continue;
					}
					//attack case 1
					else 
					{
						//minion will inflict damage on each other
						n.setCurrentHP(n.getCurrentHP()-m.getAttack());
						m.setCurrentHP(m.getCurrentHP()-n.getAttack());
						//new position or shape of board
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						//get value of that shape of board, by predicting next turn
						int val = minimax(x,depth-1,!isAI,alpha,beta);
						//if better than current position, replace it
						maxEval = Math.max(val, maxEval);
						//undo any change of the board(just predicting)
						n.setCurrentHP(org2);
						m.setCurrentHP(org);
						alpha=Math.max(alpha, val);
						if(beta<=alpha)
							break;
						continue;
					}
				}
			}
			return maxEval;
		}
		else
		{
			int minEval = 10000;
			// predicts what the human will do
			//every minion on human field will attack every minion on ai field
			// creating  new postion/possiblity to get it's total
			if(this.human.getHand().size()>0)
			{
			for(int i =0;i<this.human.getHand().size();i++)
			{
				if(this.human.getHand().get(i) instanceof Spell || this.human.getHand().get(i).getManaCost()>this.human.getCurrentManaCrystals())
					continue;
				Minion toadd = (Minion) this.human.getHand().get(i);
				p.humanField.add(toadd);
				Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
				int val = minimaxCard(x,depth-1,!isAI,alpha,beta);
				minEval = Math.min(val, minEval);
				p.humanField.remove(toadd);//not sure
				beta=Math.min(beta, val); 
				  if(beta<=alpha) 
					  break;
			}
			}
			for(int i =0;i<p.humanField.size();i++)
			{
				if(p.humanField.get(i).isSleeping() || p.humanField.get(i).isAttacked() || p.humanField.get(i).getAttack()==0 || hasTauntMinion(p.aiField))
					continue;
				if(p.humanField.get(i).getName().equals("Icehowl"))
					continue;
				int org = p.ai;
				
				p.ai-=p.humanField.get(i).getAttack();
				Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
				int val = minimax(x,depth-1,!isAI,alpha,beta);
				minEval = Math.min(val, minEval);
				p.ai=org;
				beta=Math.min(beta, val);
				if(beta<=alpha)
					break;
			}
			for(int i =0;i<p.humanField.size();i++)
			{
				if(p.humanField.get(i).isSleeping() || p.humanField.get(i).isAttacked() || p.humanField.get(i).getAttack()==0)
					continue;
				Minion m = p.humanField.get(i);
				int org = m.getCurrentHP();
				for(int j =0;j<p.aiField.size();j++)
				{
					Minion n = p.aiField.get(j);
					if(n.isTaunt()==false && hasTauntMinion(p.aiField))
						continue;
					int org2 = n.getCurrentHP();
					if(m.getAttack()>=org2 && org>n.getAttack())//case1
					{
						p.aiField.remove(n);
						m.setCurrentHP(m.getCurrentHP()-n.getAttack());
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimax(x,depth-1,!isAI,alpha,beta);
						minEval = Math.min(val, minEval);
						m.setCurrentHP(org);
						p.aiField.add(j, n);
						beta=Math.min(beta, val);
						if(beta<=alpha)
							break;
						continue;
					}
					else if(m.getAttack()>=n.getCurrentHP() && m.getCurrentHP()<=n.getAttack())
					{
						p.humanField.remove(m);
						p.aiField.remove(n);
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimax(x,depth-1,!isAI,alpha,beta);
						minEval = Math.min(val, minEval);
						p.humanField.add(i, m);
						p.aiField.add(j, n);
						beta=Math.min(beta, val);
						if(beta<=alpha)
							break;
						continue;
					}
					else if (n.getAttack()>=org && org2>m.getAttack())
					{
						p.humanField.remove(m);
						n.setCurrentHP(n.getCurrentHP()-m.getAttack());
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimax(x,depth-1,!isAI,alpha,beta);
						minEval = Math.min(val, minEval);
						n.setCurrentHP(org2);
						p.humanField.add(i, m);
						beta=Math.min(beta, val);
						if(beta<=alpha)
							break;
						continue;
					}
					else 
					{
						n.setCurrentHP(n.getCurrentHP()-m.getAttack());
						m.setCurrentHP(m.getCurrentHP()-n.getAttack());
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimax(x,depth-1,!isAI,alpha,beta);
						minEval = Math.min(val, minEval);
						n.setCurrentHP(org2);
						m.setCurrentHP(org);
						beta=Math.min(beta, val);
						if(beta<=alpha)
							break;
						continue;
					}
				}
		 }
			return minEval;
		}
	}	
	public int minimaxCard(Position p,int depth,boolean isAI,int alpha,int beta)
	{
						
		if(depth==0 || p.humanField.size()==0 || this.ai.getHand().size()==0)
			return p.sum();
		if(isAI)
		{
			int maxEval = -10000;
			for(int i =0;i<this.ai.getHand().size();i++)
			{
				if(this.ai.getHand().get(i) instanceof Spell || this.ai.getHand().get(i).getManaCost()>this.ai.getCurrentManaCrystals())
					continue;
				Minion toadd = (Minion) this.ai.getHand().get(i);
				p.aiField.add(toadd);
				Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
				int val = minimaxCard(x,depth-1,!isAI,alpha,beta);
				maxEval = Math.max(val, maxEval);
				p.aiField.remove(toadd);//not sure
				alpha=Math.max(alpha, val);
				if(beta<=alpha)
					break;
			}
			return maxEval;
		}
		else
		{
			int minEval = 10000;
			// predicts what the human will do
			//every minion on human field will attack every minion on ai field
			// creating  new postion/possiblity to get it's total
			if(this.human.getHand().size()>0)
			{
			for(int i =0;i<this.human.getHand().size();i++)
			{
				if(this.human.getHand().get(i) instanceof Spell || this.human.getHand().get(i).getManaCost()>this.human.getCurrentManaCrystals())
					continue;
				Minion toadd = (Minion) this.human.getHand().get(i);
				p.humanField.add(toadd);
				Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
				int val = minimaxCard(x,depth-1,!isAI,alpha,beta);
				minEval = Math.min(val, minEval);
				p.humanField.remove(toadd);//not sure
				beta=Math.min(beta, val); 
				  if(beta<=alpha) 
					  break;
			}
			}
			  for(int i =0;i<p.humanField.size();i++) 
			  {
				  if(p.humanField.get(i).isSleeping() || p.humanField.get(i).isAttacked() || p.humanField.get(i).getAttack()==0 ||hasTauntMinion(p.aiField))
					  continue;
				  if(p.humanField.get(i).getName().equals("Icehowl")) 
					  continue; 
				  int org = p.ai;
				  p.ai-=p.humanField.get(i).getAttack(); 
				  Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
				  int val =minimax(x,depth-1,!isAI,alpha,beta); 
				  minEval = Math.min(val, minEval);
				  p.ai=org; 
				  beta=Math.min(beta, val); 
				  if(beta<=alpha) 
					  break; 
			  }
			for(int i =0;i<p.humanField.size();i++)
			{
				if(p.humanField.get(i).isSleeping() || p.humanField.get(i).isAttacked() || p.humanField.get(i).getAttack()==0)
					continue;
				Minion m = p.humanField.get(i);
				int org = m.getCurrentHP();
				for(int j =0;j<p.aiField.size();j++)
				{
					Minion n = p.aiField.get(j);
					if(n.isTaunt()==false && hasTauntMinion(p.aiField))
						continue;
					int org2 = n.getCurrentHP();
					if(m.getAttack()>=org2 && org>n.getAttack())//case1
					{
						p.aiField.remove(n);
						m.setCurrentHP(m.getCurrentHP()-n.getAttack());
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimaxCard(x,depth-1,!isAI,alpha,beta);
						minEval = Math.min(val, minEval);
						m.setCurrentHP(org);
						p.aiField.add(j, n);
						beta=Math.min(beta, val);
						if(beta<=alpha)
							break;
						continue;
					}
					else if(m.getAttack()>=n.getCurrentHP() && m.getCurrentHP()<=n.getAttack())
					{
						p.humanField.remove(m);
						p.aiField.remove(n);
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimaxCard(x,depth-1,!isAI,alpha,beta);
						minEval = Math.min(val, minEval);
						p.humanField.add(i, m);
						p.aiField.add(j, n);
						beta=Math.min(beta, val);
						if(beta<=alpha)
							break;
						continue;
					}
					else if (n.getAttack()>=org && org2>m.getAttack())
					{
						p.humanField.remove(m);
						n.setCurrentHP(n.getCurrentHP()-m.getAttack());
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimaxCard(x,depth-1,!isAI,alpha,beta);
						minEval = Math.min(val, minEval);
						n.setCurrentHP(org2);
						p.humanField.add(i, m);
						beta=Math.min(beta, val);
						if(beta<=alpha)
							break;
						continue;
					}
					else 
					{
						n.setCurrentHP(n.getCurrentHP()-m.getAttack());
						m.setCurrentHP(m.getCurrentHP()-n.getAttack());
						Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
						int val = minimaxCard(x,depth-1,!isAI,alpha,beta);
						minEval = Math.min(val, minEval);
						n.setCurrentHP(org2);
						m.setCurrentHP(org);
						beta=Math.min(beta, val);
						if(beta<=alpha)
							break;
						continue;
					}
				}
		 }
			return minEval;
		}
	}
	public AttackMove getCard(int depth,Position p, boolean isAI)
	{
		AttackMove res = new AttackMove();
		int maxEval = -10000;
		for(int i =0;i<this.ai.getHand().size();i++)
		{
			if(this.ai.getHand().get(i) instanceof Spell || this.ai.getHand().get(i).getManaCost()>ai.getCurrentManaCrystals())
				continue;
			Minion toadd = (Minion) this.ai.getHand().get(i);
			p.aiField.add(toadd);
			Position x = new Position(p.ai,p.human,p.aiField,p.humanField);
			int val = minimaxCard(x,depth-1,!isAI,100000,-100000);
			p.aiField.remove(toadd);//not sure
			if(val>maxEval)
			{
				maxEval=val;
				res.i=i;
				res.val=val;
			}	
		}
		return res;
	}
	public void playSpell() throws InvalidTargetException
	{
		int mana = this.ai.getCurrentManaCrystals();
		PriorityQueueSpells all = new PriorityQueueSpells(this.ai instanceof Mage);
		while(all.iterator().hasNext())
		{
			if(all.peek().getSpell().getManaCost()<=mana && hasSpell(all.peek().getSpell().getName()))
			{
				Spell select = all.remove().getSpell();
				if (select.getName().equals("Curse of Weakness") || select.getName().equals("Flamestrike") || select.getName().equals("Holy Nova") || select.getName().equals("Multi-Shot") || select.getName().equals("Twisting Nether"))
					this.aicastSpell((AOESpell) select, this.human.getField());
				else if ((select.getName().equals("Divine Spirit") || select.getName().equals("Kill Command") || select.getName().equals("Polymorph") || select.getName().equals("Pyroblast") || select.getName().equals("Seal of Champions") || select.getName().equals("Shadow Word: Death")))
				{
					if((select.getName().equals("Divine Spirit") || select.getName().equals("Seal of Champions")) && this.ai.getField().size()>0)
					{
						Minion s = getStrongest(this.ai.getField());
						this.aicastSpell((MinionTargetSpell)select, s);
					}
					else
					{
						Minion s = getStrongest(this.human.getField());
						if(select.getName().equals("Shadow Word: Death") && s.getMaxHP()>4 && this.human.getField().size()>0)
						{
							this.aicastSpell((MinionTargetSpell)select, s);
						}
						else if ((select.getName().equals("Kill Command") || select.getName().equals("Polymorph") || select.getName().equals("Pyroblast")) && this.human.getField().size()>0)
							this.aicastSpell((MinionTargetSpell)select, s);
					}
				}
				else if ((select.getName().equals("Kill Command") || select.getName().equals("Pyroblast"))&& this.human.getField().size()==0)
				{
					this.aicastSpell((HeroTargetSpell)select, this.human);
				}
				else if (select.getName().equals("Level Up!") && hasSilver())
					this.aicastSpell((FieldSpell) select);
				else if(select.getName().equals("Siphon Soul")&&this.human.getField().size()>0)
				{
					Minion s = getStrongest(this.human.getField());
					this.aicastSpell((LeechingSpell)select, s);
				}
			}
			else
				all.remove();
		}
	}
	public boolean hasSilver()
	{
		for(int i =0;i<this.ai.getField().size();i++)
		{
			if(this.ai.getField().get(i).getName().equals("Silver Hand Recruit"))
				return true;
		}
		return false;
	}
	public boolean hasSpell(String s)
	{
		for(int i =0;i<this.ai.getHand().size();i++)
		{
			if(this.ai.getHand().get(i).getName().equals(s))
				return true;
		}
		return false;
	}
	public static Minion getStrongest(ArrayList<Minion> field)
	{
		int max=0;
		int index=0;
		for(int i =0;i<field.size();i++)
		{
			if(field.get(i).getMaxHP()>max)
			{
				max=field.get(i).getMaxHP();
				index =i;
			}
		}
		return field.get(index);
	}
	public void damageHuman(int amount)
	{
		int health = currentHero.getCurrentHP();
		currentHero.setCurrentHP(health-amount);
	}
	public void aiEndTurn() throws CloneNotSupportedException
	{
		int number = opponent.getTotalManaCrystals();
		opponent.setCurrentManaCrystals(number+1);
		opponent.setTotalManaCrystals(number+1);
		opponent.setHeroPowerUsed(false);
		for(int i =0;i<opponent.getField().size();i++)
		{
			opponent.getField().get(i).setAttacked(false);
			opponent.getField().get(i).setSleeping(false);
		}
		Hero t = this.ai;
		if(t.getHand().size()==10)
		{
			for(int i=0;i<t.getHand().size();i++)
			{
				if(t.getHand().get(i) instanceof Minion && t.getHand().get(i).getManaCost()<=t.getCurrentManaCrystals())
					{
					this.aiPlayMinion((Minion) t.getHand().get(i));
					return;
					}
			}
		}
		else
			this.aiDrawCard();
	}
	public Card aiDrawCard() throws CloneNotSupportedException //max 10
	{
		Hero t = opponent;
		 if(t.getDeck().size()>1)
			 {
				 Card toadd = t.getDeck().remove(0);
				 if(t instanceof Warlock && aihasLegend(t.getField(),"Wilfred Fizzlebang") && toadd instanceof Minion&& t.isHeroPowerUsed()==true)
					 toadd.setManaCost(0);
				 t.getHand().add(toadd);
				 if(aihasLegend(t.getField(),"Chromaggus") && t.getHand().size()<10)
					 ailegendEffect(t.getHand(),toadd);
				 return toadd;
			 }
			 else if(t.getDeck().size()==1)
			 {
				 Card toadd = t.getDeck().remove(0);
				 if(t instanceof Warlock && aihasLegend(t.getField(),"Wilfred Fizzlebang")&&toadd instanceof Minion && t.isHeroPowerUsed()==true)
					 toadd.setManaCost(0);
				 t.getHand().add(toadd);
				 if(aihasLegend(t.getField(),"Chromaggus") && t.getHand().size()<10)
					 ailegendEffect(t.getHand(),toadd);
				 t.setFatigueDamage(1);
				 return toadd;
			 }
			 else //size of deck = 0
			 {
				 t.setCurrentHP(t.getCurrentHP()-t.getFatigueDamage());
				 t.setFatigueDamage(t.getFatigueDamage() + 1);
				 return null;
			 }
	}
	public static void ailegendEffect(ArrayList<Card> hand,Card toadd) throws CloneNotSupportedException
	 {
		 Card x = toadd.clone();
		 hand.add(x);
	 }
	public static boolean aihasLegend(ArrayList<Minion> field,String s)
	 {
		 for(int i =0;i<field.size();i++)
		 {
			 if(field.get(i).getName().equals(s))
				 return true;
		 }
		 return false;
	 }
	public void aiAttackWithMinion(Minion attacker,Minion target)
	{
		attacker.attack(target);
	}
	public void aiAttackWithMinion(Minion attacker,Hero target)
	{
		int attackerPoints = attacker.getAttack();
		int targetHP = target.getCurrentHP();
		target.setCurrentHP(targetHP - attackerPoints);
		attacker.setAttacked(true);
	}
	public void aiPlayMinion(Minion m)
	{
		opponent.getHand().remove(m);
		opponent.getField().add(m);
		opponent.setCurrentManaCrystals(opponent.getCurrentManaCrystals()-m.getManaCost());
	}
	public void aiUseHeroPower(Object target)
	{
		opponent.setCurrentManaCrystals(opponent.getCurrentManaCrystals()-2);
		opponent.setHeroPowerUsed(true);
		
		if (opponent instanceof Mage)
		{
			if(target instanceof Hero)
				((Hero) target).setCurrentHP(((Hero) target).getCurrentHP()-1);
			else
			{
				if(((Minion) target).isDivine()==true)
					((Minion) target).setDivine(false);
				else
					((Minion) target).setCurrentHP(((Minion) target).getCurrentHP()-1);
			}
		}
		else if (opponent instanceof Priest)
		{
			if(target instanceof Hero)
			{
				if(aihasLegend(opponent.getField(),"Prophet Velen"))
					opponent.setCurrentHP(opponent.getCurrentHP()+8);
				else
					opponent.setCurrentHP(opponent.getCurrentHP()+2);
			}
			else
			{
				if(aihasLegend(opponent.getField(),"Prophet Velen"))
					((Minion) target).setCurrentHP(((Minion) target).getCurrentHP()+8);
				else
					((Minion) target).setCurrentHP(((Minion) target).getCurrentHP()+2);
			}
		}
	}
	public void aiUseHeroPower() throws CloneNotSupportedException
	{
		opponent.setCurrentManaCrystals(opponent.getCurrentManaCrystals()-2);
		opponent.setHeroPowerUsed(true);
		
		if(opponent instanceof Hunter)
		{
			this.damageHuman(2);
		}
		else if (opponent instanceof Mage) // fullfield
		{
			Minion toadd = new Minion("Silver Hand Recruit",1,Rarity.BASIC,1,1,false,false,false);
			opponent.getField().add(toadd);
		}
		else if (opponent instanceof Warlock)
		{
			this.aiDrawCard();
			opponent.setCurrentHP(opponent.getCurrentHP()-2);
		}
	}
	public void aicastSpell(FieldSpell s)
	 {
		 Card spell=(Card)s;
		 if(opponent instanceof Mage && aihasLegend(opponent.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 opponent.setCurrentManaCrystals(opponent.getCurrentManaCrystals()-spell.getManaCost());
		 s.performAction(opponent.getField());
		 opponent.getHand().remove((Card)s);
	 }
	public void aicastSpell(AOESpell s, ArrayList<Minion >oppField)
	 {
		 Card spell=(Card)s;
		 if(opponent instanceof Mage && aihasLegend(opponent.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 opponent.setCurrentManaCrystals(opponent.getCurrentManaCrystals()-spell.getManaCost());
		 s.performAction(human.getField(),opponent.getField());
		 opponent.getHand().remove((Card)s);
	 }
	public void aicastSpell(MinionTargetSpell s, Minion m) throws InvalidTargetException
	 {
		 Card spell=(Card)s;
		 if(opponent instanceof Mage && aihasLegend(opponent.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 opponent.setCurrentManaCrystals(opponent.getCurrentManaCrystals()-spell.getManaCost());
		 s.performAction(m);//throws invalid
		 opponent.getHand().remove((Card)s);
	 }
	public void aicastSpell(HeroTargetSpell s, Hero h)
	 {
		 Card spell=(Card)s;
		 if(opponent instanceof Mage && aihasLegend(opponent.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 opponent.setCurrentManaCrystals(opponent.getCurrentManaCrystals()-spell.getManaCost());
		 s.performAction(h);
		 opponent.getHand().remove((Card)s);
	 }
	public void aicastSpell(LeechingSpell s, Minion m)
	 {
		 Card spell=(Card)s;
		 if(opponent instanceof Mage && aihasLegend(opponent.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 int health=s.performAction(m);
		 opponent.setCurrentManaCrystals(opponent.getCurrentManaCrystals()-spell.getManaCost());
		 opponent.getHand().remove((Card)s);
		 opponent.setCurrentHP(opponent.getCurrentHP()+health);
	 }

}
