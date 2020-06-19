package model.cards.spells;

import java.io.IOException;
import java.util.ArrayList;

import model.cards.Rarity;
import model.cards.minions.Minion;
import model.heroes.Mage;

public class HolyNova extends Spell implements AOESpell {

	public HolyNova() {
		super("Holy Nova", 5, Rarity.BASIC);
	
	}

	@Override
	public void performAction(ArrayList<Minion> oppField, ArrayList<Minion> curField)
	{
		ArrayList<Minion> toremove= new ArrayList<Minion>();
		int size = oppField.size();
		for(int i =0;i<size;i++)
		{
			if(oppField.get(i).isDivine()==false)
			{
				int x =oppField.get(i).getCurrentHP();
				if(x>2)
					oppField.get(i).setCurrentHP(x-2);
				else
					toremove.add(oppField.get(i));
			}
			else
				oppField.get(i).setDivine(false);
		}
		oppField.removeAll(toremove);
		for(int i=0;i<curField.size();i++)
		{
			curField.get(i).setCurrentHP(curField.get(i).getCurrentHP()+2);
		}
	}
}
