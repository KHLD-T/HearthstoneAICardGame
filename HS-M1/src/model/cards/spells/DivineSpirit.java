package model.cards.spells;

import exceptions.InvalidTargetException;
import model.cards.Rarity;
import model.cards.minions.Minion;

public class DivineSpirit extends Spell implements MinionTargetSpell {

	public DivineSpirit() {
		super("Divine Spirit", 3, Rarity.BASIC);
		
	}

	@Override
	public void performAction(Minion m) throws InvalidTargetException
	{
		//invalid target throw
		int cur = m.getCurrentHP() * 2;
		m.setMaxHP(m.getMaxHP()*2);
		m.setCurrentHP(cur);
	}
}
