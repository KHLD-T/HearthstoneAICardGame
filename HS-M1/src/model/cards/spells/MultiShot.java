package model.cards.spells;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import model.cards.Rarity;
import model.cards.minions.Minion;
import model.heroes.Mage;

public class MultiShot extends Spell implements AOESpell{

	public MultiShot() {
		super("Multi-Shot", 4,Rarity.BASIC);
		
	}

	@Override
	public void performAction(ArrayList<Minion> oppField, ArrayList<Minion> curField)
	{
		if(oppField.size()==1)
		{
			if(oppField.get(0).isDivine()==false)
				oppField.get(0).setCurrentHP(oppField.get(0).getCurrentHP()-3);
			else
				oppField.get(0).setDivine(false);
		}
		else if(oppField.size()>=2)
		{
			Random r = new Random();
			int rand1 = r.nextInt(oppField.size());
			if(oppField.get(rand1).isDivine()==false)
				{
				oppField.get(rand1).setCurrentHP(oppField.get(rand1).getCurrentHP()-3);
				}
			else
				oppField.get(rand1).setDivine(false);
			int rand2 = r.nextInt(oppField.size());
			while(rand1==rand2)
			{
				rand2 = r.nextInt(oppField.size());
			}
			if(oppField.get(rand2).isDivine()==false)
				oppField.get(rand2).setCurrentHP(oppField.get(rand2).getCurrentHP()-3);
			else
				oppField.get(rand2).setDivine(false);
		}
	}
}
