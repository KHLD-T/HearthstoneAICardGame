package model.cards.spells;
import java.util.Comparator;
import java.util.PriorityQueue;
class The_Comparator implements Comparator<SpellPriority> { 
    public int compare(SpellPriority one , SpellPriority two) 
    { 
        return Integer.compare(two.getPriority(),one.getPriority()); 
    } 
} 

public class PriorityQueueSpells extends PriorityQueue<SpellPriority>
{
	public PriorityQueueSpells(boolean bool)
	{
		super(13,new The_Comparator());
		SpellPriority one = new SpellPriority(new CurseOfWeakness(),3);
		SpellPriority two = new SpellPriority(new Flamestrike(),4);
		SpellPriority three = new SpellPriority(new HolyNova(),4);
		SpellPriority four = new SpellPriority(new MultiShot(),5);
		SpellPriority five = new SpellPriority(new TwistingNether(),8);
		SpellPriority six = new SpellPriority(new DivineSpirit(),4);
		SpellPriority seven = new SpellPriority(new KillCommand(),4);
		SpellPriority eight = new SpellPriority(new Polymorph(),6);
		SpellPriority nine = new SpellPriority(new Pyroblast(),10);
		SpellPriority ten = new SpellPriority(new ShadowWordDeath(),5);
		SpellPriority eleven = new SpellPriority(new SealOfChampions(),2);
		SpellPriority twelve =new SpellPriority(new SiphonSoul(),10);
		SpellPriority thirteen;
		if(bool == true)
		{
			thirteen = new SpellPriority(new LevelUp(),6);		
		}
		else
			thirteen = new SpellPriority(new LevelUp(),1);
		this.add(one);
		this.add(two);
		this.add(three);
		this.add(four);
		this.add(five);
		this.add(six);
		this.add(seven);
		this.add(eight);
		this.add(nine);
		this.add(ten);
		this.add(eleven);
		this.add(twelve);
		this.add(thirteen);
	}
	public boolean add(SpellPriority e)
	{
		return super.add(e);
	}
	public static void main(String[] args)
	{
		PriorityQueueSpells s= new PriorityQueueSpells(true);
		while(s.iterator().hasNext())
			System.out.println(s.remove().getPriority());
	}
}
