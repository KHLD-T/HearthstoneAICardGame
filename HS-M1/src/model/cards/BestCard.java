package model.cards;

public class BestCard extends Card
{
	public int val;
	public int pos;

	public BestCard(String name, int manaCost, Rarity rarity,int val,int pos)
	{
		super(name, manaCost, rarity);
		this.val=val;
		this.pos=pos;
	}

}
