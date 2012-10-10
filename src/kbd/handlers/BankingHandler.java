package kbd.handlers;

import kbd.rsc.Condition;
import kbd.rsc.RSC;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

public class BankingHandler extends Node {
	private int foodId;
	
	public BankingHandler(int foodId) {
		this.foodId = foodId;
	}

	@Override
	public boolean activate() {
		return RSC.bankArea.contains(Players.getLocal().getLocation())
				&& !Inventory.contains(foodId);
	}

	@Override
	public void execute() {
		if (RSC.waitFor(new Condition() {
			@Override
			public boolean validate() {
				return !Bank.isOpen();
			}
		}, 1500)) {
			Bank.open();
		} else if (RSC.waitFor(new Condition() {
			@Override
			public boolean validate() {
				return Bank.isOpen();
			}
		}, 2000)) {
			if (Bank.depositInventory() && Bank.withdraw(this.foodId, 0)) {
				Bank.close();
			}
		}
	}
}
