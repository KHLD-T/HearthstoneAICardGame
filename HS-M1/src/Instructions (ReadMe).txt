1. How to select the two heroes.
->			 a. run MainGameRunner in src->controller->MainGameRunner.java
   			 b. select one hero from upper row and second hero from lower row

2. How the current hero plays a minion.
-> 			a. press on card (selected card on left will change)
 			b. press on Play selected card button on left
   			c.if need to change selected card to play press on Reset selected&target button on left then select desired card again

3. How the current hero casts all types of spells ( hero target, minion target etc.).
-> Hero Target spell:   a. select spell first (selected card on left will change)
		        b. press on opponent hero icon (target card on left will change)
		        c. press Play Selected card button on left

-> Minion Target spell: a. select spell first (selected card on left will change)
		        b. press on opponent minion on field (target card on left will change)
		        c. press Play Selected card button on left

-> AOE spell: 		a. select spell first (selected card on left will change)
	      		b. press Play Selected card button on left

-> Field spell: 	a. select spell first (selected card on left will change)
	        	b. press Play Selected card button on left

-> Leeching spell: 	a. select spell first (selected card on left will change)
		   	b. press on opponent minion on field (target card on left will change)
	           	c. press Play Selected card button on left

4. How the current hero uses his minions to attack the opponent’s minion.
->		        a. press on minion to set attacker(selected card on left will change)
   			b. press on any minion set as target (target card on left will change)
   			c. to change target minion simply press on it (target card on left will change)
   			d. to change attacker press on Reset selected&target button on left then select desired card again

5. How to end the turn.
-> 			a. press on end turn on lower right of current hero (who is at the bottom of screen)

6. Specify the screen orientation (whether the current hero hand and field is at a fixed half i.e top/button or each hero has a fixed half).
-> Screen is divided, upper half is always for opponent and lower half is always for current hero (changes when ending turn)
-> left panel: controls all card actions and details
-> middle panel: divided into hand and field cards
-> right panel: controls all hero actions and details

7. Any other details that might be specific to your own implementation.
-> if game didn't start when selecting heros, choose them again in order: 1. player one (upper half) 2. player two (lower half)
-> hovering over any card will reveal it's details: if card belongs to current hero -> lower left
						    if card belongs to opponent -> upper left