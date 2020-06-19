package view;

import java.awt.*;
import javax.swing.*;

import model.heroes.Hero;

public class GameView extends JFrame
{
	private HeroPanel heroOnePanel;
	private HeroPanel heroTwoPanel;
	public GameView()
	{
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setUndecorated(false);
		this.setLayout(new BorderLayout());
		heroOnePanel = new HeroPanel(0);
		heroTwoPanel = new HeroPanel(1);
		this.add(heroOnePanel,BorderLayout.SOUTH);
		this.add(heroTwoPanel,BorderLayout.NORTH);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.revalidate();
		this.repaint();
	}
	public HeroPanel getHeroOnePanel()
	{
		return heroOnePanel;
	}
	public void setHeroOnePanel(HeroPanel heroOnePanel)
	{
		this.heroOnePanel = heroOnePanel;
	}
	public HeroPanel getHeroTwoPanel()
	{
		return heroTwoPanel;
	}
	public void setHeroTwoPanel(HeroPanel heroTwoPanel)
	{
		this.heroTwoPanel = heroTwoPanel;
	}
}