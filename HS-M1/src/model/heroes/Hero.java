package model.heroes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import engine.ActionValidator;
import engine.Game;
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
import model.cards.minions.MinionListener;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Polymorph;

public abstract class Hero implements MinionListener {
	private String name;
	private int currentHP;
	private boolean heroPowerUsed;
	private int totalManaCrystals;
	private int currentManaCrystals;
	private ArrayList<Card> deck;
	private ArrayList<Minion> field;
	private ArrayList<Card> hand;
	@SuppressWarnings("unused")
	private int fatigueDamage;
	private ActionValidator validator;
	private HeroListener listener;
	
	public Hero(String name) throws IOException, CloneNotSupportedException {
		this.name = name;
		currentHP = 30;
		deck = new ArrayList<Card>();
		field = new ArrayList<Minion>();
		hand = new ArrayList<Card>();
		buildDeck();
		heroPowerUsed=false;
	}
	public abstract void buildDeck() throws IOException,CloneNotSupportedException;
	public int total()
	{
		int res = this.getCurrentHP();
		for(int i =0;i<this.getField().size();i++)
		{
			res+=this.getField().get(i).getCurrentHP();
		}
		return res;
	}
	public HeroListener getListener()
	{
		return listener;
	}

	public void setListener(HeroListener listener)
	{
		this.listener = listener;
	}

	public void setValidator(ActionValidator validator)
	{
		this.validator = validator;
	}

	public static final ArrayList<Minion> getAllNeutralMinions(String filePath) throws IOException
	{
		ArrayList<Minion> neutralMinions = new ArrayList<Minion>();
		String current ="";
		boolean taunt=false;
		boolean divine=false;
		boolean charge=false;
		Rarity rarity=null;
		FileReader filereader = new FileReader(filePath);
		BufferedReader br = new BufferedReader(filereader);
		while((current=br.readLine())!=null)
		{
			String [] minions = current.split(",");
			
			String name = minions[0];
			if(name.equals("Icehowl"))
			{
				Icehowl ice = new Icehowl();
				neutralMinions.add(ice);
			}
			else
			{
			int manaCost = Integer.parseInt(minions[1]);
			int attack = Integer.parseInt(minions[3]);
			int maxHP = Integer.parseInt(minions[4]);
			
			if(minions[5].equals("FALSE"))
				taunt = false;
			else if(minions[5].equals("TRUE"))
				taunt = true;
			if(minions[6].equals("FALSE"))
				divine = false;
			else if (minions[6].equals("TRUE"))
				divine = true;
			if(minions[7].equals("FALSE"))
				charge = false;
			else if(minions[7].equals("TRUE"))
				charge = true;
			
			if(minions[2].equals("b"))
				rarity = Rarity.BASIC;
			else if(minions[2].equals("c"))
				rarity = Rarity.COMMON;
			else if(minions[2].equals("r"))
				rarity = Rarity.RARE;
			else if(minions[2].equals("e"))
				rarity = Rarity.EPIC;
			else if (minions[2].equals("l"))
				rarity = Rarity.LEGENDARY;
			
			Minion minion = new Minion(name,manaCost,rarity,attack,maxHP,taunt,divine,charge);
			
			neutralMinions.add(minion);
			}
		}
		return neutralMinions;
	}
	
	public static final ArrayList<Minion> getNeutralMinions(ArrayList<Minion> minions,int count) throws CloneNotSupportedException
	{
		Random r = new Random();
		ArrayList<Minion> neutralDeck = new ArrayList<Minion>();
		while(neutralDeck.size()<count)
		{
			int random = r.nextInt(14);
			Minion toadd = minions.get(random).clone();
			if((isInList(neutralDeck,toadd.getName())==false))
				neutralDeck.add(toadd);
			else if(numberOccurences(neutralDeck,toadd.getName())>=2 && toadd.getRarity()!=Rarity.LEGENDARY)
				continue;
			else if(numberOccurences(neutralDeck,toadd.getName())>=1 && toadd.getRarity()==Rarity.LEGENDARY)
				continue;
			else
				neutralDeck.add(toadd);
		}
		return neutralDeck;
	}
	public static int numberOccurences(ArrayList<Minion> minions,String name)
	{
		int count =0;
		for(int i =0;i<minions.size();i++)
		{
			if(minions.get(i).getName().equals(name))
				count++;
		}
		return count;
	}
	public static boolean isInList(ArrayList<Minion> minions,String name)
	{
		for(int i =0;i<minions.size();i++)
		{
			if(minions.get(i).getName().equals(name))
				return true;
		}
		return false;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int hp) {
		this.currentHP = hp;
		if (this.currentHP > 30)
			this.currentHP = 30;
		else if (this.currentHP <= 0) {
			this.currentHP = 0;
			listener.onHeroDeath();
			
		}
	}

	public int getTotalManaCrystals() {
		return totalManaCrystals;
	}

	public void setTotalManaCrystals(int totalManaCrystals) {
		this.totalManaCrystals = totalManaCrystals;
		if (this.totalManaCrystals > 10)
			this.totalManaCrystals = 10;
	}

	public int getCurrentManaCrystals() {
		return currentManaCrystals;
	}

	public void setCurrentManaCrystals(int currentManaCrystals) {
		this.currentManaCrystals = currentManaCrystals;
		if (this.currentManaCrystals > 10)
			this.currentManaCrystals = 10;
	}

	public ArrayList<Minion> getField() {
		return field;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public boolean isHeroPowerUsed() {
		return heroPowerUsed;
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public void setHeroPowerUsed(boolean powerUsed) {
		this.heroPowerUsed = powerUsed;
	}

	public String getName() {
		return name;
	}
	public void onMinionDeath(Minion m)
	{
			this.getField().remove(m);
	}
	 public void playMinion(Minion m) throws NotYourTurnException, NotEnoughManaException, FullFieldException
	 {
		 validator.validateTurn(this);
		 validator.validateManaCost(m);
		 validator.validatePlayingMinion(m);
		 this.getHand().remove(m);
		 this.getField().add(m);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-m.getManaCost());
	 }
	 public void attackWithMinion(Minion attacker, Minion target) throws CannotAttackException, NotYourTurnException, TauntBypassException, InvalidTargetException, NotSummonedException
	 {
		 validator.validateTurn(this);
		 validator.validateAttack(attacker, target);
		 attacker.attack(target);
	 }
	 public void attackWithMinion(Minion attacker, Hero target) throws CannotAttackException, NotYourTurnException, TauntBypassException, NotSummonedException, InvalidTargetException
	 {
		 validator.validateTurn(this);
		 validator.validateAttack(attacker, target);
		 attacker.attack(target);
	 }
	 public void castSpell(FieldSpell s) throws NotYourTurnException, NotEnoughManaException
	 {
		 validator.validateTurn(this);
		 Card spell=(Card)s;
		 if(this instanceof Mage && hasLegend(this.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 validator.validateManaCost(spell);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-spell.getManaCost());
		 s.performAction(this.getField());
		 this.getHand().remove((Card)s);
	 }
	 public void castSpell(AOESpell s, ArrayList<Minion >oppField) throws NotYourTurnException, NotEnoughManaException
	 {
		 validator.validateTurn(this);
		 Card spell=(Card)s;
		 if(this instanceof Mage && hasLegend(this.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 validator.validateManaCost(spell);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-spell.getManaCost());
		 s.performAction(oppField,this.getField());
		 this.getHand().remove((Card)s);
	 }
	 public void castSpell(MinionTargetSpell s, Minion m) throws NotYourTurnException, NotEnoughManaException, InvalidTargetException
	 {
		 validator.validateTurn(this);
		 Card spell=(Card)s;
		 if(this instanceof Mage && hasLegend(this.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 validator.validateManaCost(spell);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-spell.getManaCost());
		 s.performAction(m);//throws invalid
		 this.getHand().remove((Card)s);
	 }
	 public void castSpell(HeroTargetSpell s, Hero h) throws NotYourTurnException, NotEnoughManaException
	 {
		 validator.validateTurn(this);
		 Card spell=(Card)s;
		 if(this instanceof Mage && hasLegend(this.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 validator.validateManaCost(spell);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-spell.getManaCost());
		 s.performAction(h);
		 this.getHand().remove((Card)s);
	 }
	 public void castSpell(LeechingSpell s, Minion m) throws NotYourTurnException, NotEnoughManaException
	 {
		 validator.validateTurn(this);
		 Card spell=(Card)s;
		 if(this instanceof Mage && hasLegend(this.getField(),"Kalycgos"))
		 	{
			 	int old = spell.getManaCost() - 4;
			 	spell.setManaCost(old);
			}
		 validator.validateManaCost(spell);
		 int health=s.performAction(m);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-spell.getManaCost());
		 this.getHand().remove((Card)s);
		 this.setCurrentHP(this.getCurrentHP()+health);
	 }
	 public void endTurn() throws FullHandException, CloneNotSupportedException
	 {
		 listener.endTurn();
	 }
	 public Card drawCard() throws FullHandException, CloneNotSupportedException
	 {
		 if(this.getHand().size()==10)
			 throw new FullHandException("Cannot have more than 10 cards",this.getDeck().remove(0));
		 if(this.getDeck().size()>1)
			 {
				 Card toadd = this.getDeck().remove(0);
				 if(this instanceof Warlock && hasLegend(this.getField(),"Wilfred Fizzlebang") && toadd instanceof Minion&& this.heroPowerUsed==true)
					 toadd.setManaCost(0);
				 this.getHand().add(toadd);
				 if(hasLegend(this.getField(),"Chromaggus") && this.getHand().size()<10)
					 legendEffect(this.getHand(),toadd);
				 return toadd;
			 }
			 else if(this.getDeck().size()==1)
			 {
				 Card toadd = this.getDeck().remove(0);
				 if(this instanceof Warlock && hasLegend(this.getField(),"Wilfred Fizzlebang")&&toadd instanceof Minion && this.heroPowerUsed==true)
					 toadd.setManaCost(0);
				 this.getHand().add(toadd);
				 if(hasLegend(this.getField(),"Chromaggus") && this.getHand().size()<10)
					 legendEffect(this.getHand(),toadd);
				 this.fatigueDamage=1;
				 return toadd;
			 }
			 else //size of deck = 0
			 {
				 this.setCurrentHP(this.getCurrentHP()-this.fatigueDamage);
				 this.fatigueDamage++;
				 return null;
			 }
	 }
	 public int getFatigueDamage()
	{
		return fatigueDamage;
	}
	public void setFatigueDamage(int fatigueDamage)
	{
		this.fatigueDamage = fatigueDamage;
	}
	public static void legendEffect(ArrayList<Card> hand,Card toadd) throws CloneNotSupportedException
	 {
		 Card x = toadd.clone();
		 hand.add(x);
	 }
	 public static boolean hasLegend(ArrayList<Minion> field,String s)
	 {
		 for(int i =0;i<field.size();i++)
		 {
			 if(field.get(i).getName().equals(s))
				 return true;
		 }
		 return false;
	 }
	 public void useHeroPower() throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
	 {
		 validator.validateUsingHeroPower(this);
		 validator.validateTurn(this);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-2);
		 this.setHeroPowerUsed(true);
	 }
	 public void useHeroPower(Minion h) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
		{
		 validator.validateUsingHeroPower(this);
		 validator.validateTurn(this);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-2);
		 this.setHeroPowerUsed(true);
		}
	 public void useHeroPower(Hero h) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException, FullHandException, FullFieldException, CloneNotSupportedException
		{
		 validator.validateUsingHeroPower(this);
		 validator.validateTurn(this);
		 this.setCurrentManaCrystals(this.getCurrentManaCrystals()-2);
		 this.setHeroPowerUsed(true);
		}
}
