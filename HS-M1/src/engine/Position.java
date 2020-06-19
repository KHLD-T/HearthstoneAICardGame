package engine;

import java.util.ArrayList;

import model.cards.minions.Minion;
import model.heroes.Hero;

public class Position
{
	int ai;
	int human;
	ArrayList<Minion> aiField;
	ArrayList<Minion> humanField;
	
	public Position(int ai,int human,ArrayList<Minion> aiField,ArrayList<Minion> humanField)
	{
		this.ai=ai;
		this.human=human;
		this.humanField=humanField;
		this.aiField=aiField;
	}
	public int sum()
	{
		int aiSum=ai;
		int humanSum=human;
		for(int i=0;i<aiField.size();i++)
			aiSum+=aiField.get(i).getCurrentHP();
		for(int i=0;i<humanField.size();i++)
			humanSum+=humanField.get(i).getCurrentHP();
		
		return aiSum-humanSum;
	}
}
