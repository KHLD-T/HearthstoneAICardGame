package model.cards.spells;

import java.io.IOException;
import java.util.ArrayList;

import model.cards.Rarity;
import model.cards.minions.Minion;
import model.heroes.Mage;

public class Flamestrike extends Spell implements AOESpell {

	
	public Flamestrike()
	{
		super("Flamestrike",7,Rarity.BASIC);
	}

	@Override
	public void performAction(ArrayList<Minion> oppField, ArrayList<Minion> curField)
	{
		ArrayList<Minion> toremove= new ArrayList<Minion>();
		int size = oppField.size();
		for(int i =0;i<size;i++)
		{
			int x =oppField.get(i).getCurrentHP();
			if(oppField.get(i).isDivine())
				oppField.get(i).setDivine(false);
			else
				{
				if(x>4)
					oppField.get(i).setCurrentHP(x-4);
				else
					toremove.add(oppField.get(i));
				}
		}
		oppField.removeAll(toremove);
	}
}
