package kbd.handlers;

import kbd.main.KingBlackDragon;
import kbd.rsc.RSC;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;

public class PotionHandler extends Node {

	@Override
	public boolean activate() {
		if (Inventory.contains(RSC.ANTI_FIRE_IDS) && Players.getLocal().getLocation().getY() > 4000 
				&& KingBlackDragon.drinkAntiFire) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		System.out.println("Antifire");
		if (KingBlackDragon.drinkAntiFire && Inventory.getItem(RSC.ANTI_FIRE_IDS) != null) {
			if (Inventory.getItem(RSC.ANTI_FIRE_IDS).getWidgetChild().click(true)) {
				KingBlackDragon.drinkAntiFire = false;
				Task.sleep(2000,2501);
			}
		}
	}

}
