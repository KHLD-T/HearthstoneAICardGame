package model.cards.spells;

public class SpellPriority
{
	private Spell spell;
	private int priority;
	
	public SpellPriority(Spell spell,int priority)
	{
		this.spell=spell;
		this.priority=priority;
	}

	public Spell getSpell()
	{
		return spell;
	}

	public void setSpell(Spell spell)
	{
		this.spell = spell;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}
}
