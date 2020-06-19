package view;

import java.awt.*;

import javax.swing.*;

public class HeroPanel extends JPanel
{
	private CardPanel cardPanel;
	private FieldHandPanel fieldHandPanel;
	private HeroActionPanel heroActionPanel;
	
	public HeroPanel(int n)
	{
		this.setPreferredSize(new Dimension(1000,500));
		this.setLayout(new BorderLayout());
		cardPanel = new CardPanel();
		fieldHandPanel = new FieldHandPanel(n);
		heroActionPanel = new HeroActionPanel();
		this.add(cardPanel,BorderLayout.WEST);
		this.add(fieldHandPanel,BorderLayout.CENTER);
		this.add(heroActionPanel,BorderLayout.EAST);
		this.revalidate();
		this.repaint();
	}

	public CardPanel getCardPanel()
	{
		return cardPanel;
	}

	public void setCardPanel(CardPanel cardPanel)
	{
		this.cardPanel = cardPanel;
	}

	public FieldHandPanel getFieldHandPanel()
	{
		return fieldHandPanel;
	}

	public void setFieldHandPanel(FieldHandPanel fieldHandPanel)
	{
		this.fieldHandPanel = fieldHandPanel;
	}

	public HeroActionPanel getHeroActionPanel()
	{
		return heroActionPanel;
	}

	public void setHeroActionPanel(HeroActionPanel heroActionPanel)
	{
		this.heroActionPanel = heroActionPanel;
	}
}