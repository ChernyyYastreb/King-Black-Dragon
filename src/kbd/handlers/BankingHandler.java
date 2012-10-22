package kbd.handlers;

import kbd.main.KingBlackDragon;
import kbd.rsc.Condition;
import kbd.rsc.RSC;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

public class BankingHandler extends Node {
	private int foodId = -1, potionId, antiPoisonId;
	
	/**
	 * Searches the bank for the best food.
	 * If it does not find any food, set stop to true.
	 */
	private void choseFood() {
		for (int i : RSC.FOOD_IDS) {
			if (Bank.getItem(i) != null) {
				foodId = i;
				break;
			}
		}
		if (foodId == -1) {
			KingBlackDragon.stop = true;
		}
	}
	
	/**
	 * Searches the bank for the best ranging potion.
	 * If it does not find any ranging potions, set the id to -1.
	 */
	private void chosePotions() {
		if (Bank.getItem(RSC.EXTREME_RANGING_FLASK_IDS[5]) != null) {
			potionId = RSC.EXTREME_RANGING_FLASK_IDS[5];
		} else if (Bank.getItem(RSC.EXTREME_RANGING_POTION_IDS[3]) != null) {
			potionId = RSC.EXTREME_RANGING_POTION_IDS[3];
		} else if (Bank.getItem(RSC.RANGING_FLASK_IDS[5]) != null) {
			potionId = RSC.RANGING_FLASK_IDS[5];
		} else if (Bank.getItem(RSC.RANGING_POTION_IDS[3]) != null) {
			potionId = RSC.RANGING_POTION_IDS[3];
		} else {
			potionId = -1;
		}
		if (Bank.getItem(RSC.ANTI_POISON_PP_IDS) != null) {
			antiPoisonId = RSC.ANTI_POISON_PP_IDS[3];
		} else if (Bank.getItem(RSC.ANTI_POISON_P_IDS[3]) != null) {
			antiPoisonId = RSC.ANTI_POISON_P_IDS[3];
		} else if (Bank.getItem(RSC.SUPER_ANTI_POISON_IDS[3]) != null) {
			antiPoisonId = RSC.SUPER_ANTI_POISON_IDS[3];
		} else if (Bank.getItem(RSC.ANTI_POISON_IDS[3]) != null) {
			antiPoisonId = RSC.ANTI_POISON_IDS[3];
		} else {
			antiPoisonId = -1;
		}
	}

	@Override
	public boolean activate() {
		return RSC.bankArea.contains(Players.getLocal().getLocation())
				&& Inventory.getItem(foodId) == null;
	}

	@Override
	public void execute() {
		System.out.println("Banking");
		if (RSC.waitFor(new Condition() {
			@Override
			public boolean validate() {
				//If the bank isn't open
				return !Bank.isOpen();
			}
		}, 1500)) {
			//Open the bank
			Bank.open();
		} else if (RSC.waitFor(new Condition() {
			@Override
			public boolean validate() {
				//If the bank is open
				return Bank.isOpen();
			}
		}, 2000)) {
			//If depositing the inventory was successful 
			//and withdrawing an antifire potion was successful
			if (Bank.depositInventory() 
				&& Bank.withdraw(RSC.ANTI_FIRE_IDS[3], 1)) {
				//Find the best suitable potion
				chosePotions();
				if (antiPoisonId != -1) {
					Bank.withdraw(antiPoisonId, 1);
				}
				choseFood();
				//If we found a potion, withdraw one
				if (potionId != -1) {
					//If the withdraw was successful, close the bank
					if (Bank.withdraw(potionId, 1) && Bank.withdraw(foodId, 0)) {
						Bank.close();
					}
				} else {
					//If we didn't find a potion, close the bank
					Bank.close();
				}
			}
		}
	}
}
