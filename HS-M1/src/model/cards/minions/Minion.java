package model.cards.minions;

import engine.Game;
import engine.GameListener;
import exceptions.InvalidTargetException;
import model.cards.Card;
import model.cards.Rarity;
import model.heroes.Hero;

public class Minion extends Card implements Cloneable {
	private int attack;
	private int maxHP;	
	private int currentHP;
	private boolean taunt;
	private boolean divine;
	private boolean sleeping;
	private boolean attacked;
	private MinionListener listener;

	public void set(MinionListener listener)
	{
		this.listener=listener;
	}
	public Minion(String name, int manaCost, Rarity rarity, int attack, int maxHP, boolean taunt, boolean divine,
			boolean charge) {
		super(name,manaCost,rarity);
		 setAttack(attack);
		 this.maxHP=maxHP;
		 this.currentHP=maxHP; //It starts with the value of the maxHP
		 this.taunt=taunt;
		 this.divine=divine;
		 this.sleeping=!charge;
		 this.attacked=false;
	}
	public void setListener(MinionListener listener)
	{
		this.listener=listener;
	}
	public boolean isTaunt() {
		return taunt;
	}
	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHp) {
		this.maxHP = maxHp;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
		if (this.currentHP > maxHP)
			this.currentHP = maxHP;
		else if (this.currentHP <= 0) {
			this.currentHP = 0;
			this.listener.onMinionDeath(this);
		}
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
		if (this.attack <= 0)
			this.attack = 0;
	}

	public void setTaunt(boolean isTaunt) {
		this.taunt = isTaunt;
	}

	public void setDivine(boolean divine) {
		this.divine = divine;
	}

	public boolean isAttacked() {
		return attacked;
	}

	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}

	public boolean isSleeping() {
		return sleeping;
	}

	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}

	public boolean isDivine() {
		return divine;
	}
	public Minion clone() throws CloneNotSupportedException
	{
		Minion shallow = (Minion)super.clone();
		Minion deep = new Minion(shallow.getName(),shallow.getManaCost(),shallow.getRarity(),shallow.getAttack(),shallow.getMaxHP(),shallow.isTaunt(),shallow.isDivine(),!(shallow.isSleeping()));
		return deep;
	}
	public void attack(Minion target)
	{
		int attackerPoints = this.getAttack();
		int targetAttackPoints = target.getAttack();
		int attackerHP = this.getCurrentHP();
		int targetHP = target.getCurrentHP();
		if(this.divine==false && target.divine==false)
		{
			target.setCurrentHP(targetHP - attackerPoints);
			this.setCurrentHP(attackerHP - targetAttackPoints);
		}
		else if (this.divine==false && target.divine==true)
		{
			if(this.getAttack()!=0)
				target.setDivine(false);
			this.setCurrentHP(attackerHP-targetAttackPoints);
		}
		else if (this.divine==true && target.divine==false)
		{
			
			target.setCurrentHP(targetHP - attackerPoints);
			if(target.getAttack()!=0)
				this.setDivine(false);
		}
		else
		{
			if(this.getAttack()!=0)
				target.setDivine(false);
			if(target.getAttack()!=0)
				this.setDivine(false);
		}
		this.setAttacked(true);
	}
	public void attack(Hero target) throws InvalidTargetException
	{
		if(this.getName().equals("Icehowl"))
			throw new InvalidTargetException("Icehowl attacks only minions");
		else
		{
			int attackerPoints = this.getAttack();
			int targetHP = target.getCurrentHP();
			target.setCurrentHP(targetHP - attackerPoints);
			this.setAttacked(true);
		}
		
	}
}
