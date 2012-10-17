package kbd.handlers;

import kbd.rsc.RSC;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class KBDHandler extends Node {
	NPC KBD;
	
	private int KBDId = 50;

	@Override
	public boolean activate() {
		//Only activate if we are in the KBD cave
		return Players.getLocal().getLocation().getY() > 4600;
	}

	@Override
	public void execute() {
		//Instantiate the KBD as an NPC
		KBD = NPCs.getNearest(KBDId);
		
		//If we have less than 600 Hp, let's eat
		if (RSC.getHp() < 600 && Inventory.getItem(RSC.FOOD_IDS) != null) {
			Inventory.getItem(RSC.FOOD_IDS).getWidgetChild().click(true);
			Task.sleep(1500,2001);
		}
		
		//If we have any vials in our inventory, drop them
		if (Inventory.contains(new int[]{229})) {
			Inventory.getItem(229).getWidgetChild().interact("Drop");
		}
		
		//If the KBD is dead, let's move the mouse a bit to keep us logged in
		if (KBD == null) {
			if (Random.nextInt(1, 101) == 6) {
				Mouse.move(Random.nextInt(50, 765), Random.nextInt(100, 765));
			}
			if (Random.nextInt(1, 101) == 7) {
				Camera.setAngle(Random.nextInt(100, 360));
			}
		} else {
			//If the KBD is alive and we are too close, walk away from it
			if (Calculations.distance(KBD.getLocation(), Players.getLocal().getLocation()) < 4) {
				int select = Random.nextInt(0, 3);
				//We don't want to walk to the same spot every time, so let's randomize it a bit
				if (!Players.getLocal().isMoving()) {
					switch(select) {
					case 0:
						Walking.walk(KBD.getLocation().derive(0, -4));
						Task.sleep(1000,1501);
						break;
					case 1:
						Walking.walk(KBD.getLocation().derive(-4, 0));
						Task.sleep(1000,1501);
						break;
					case 2:
						Walking.walk(KBD.getLocation().derive(4, 0));
						Task.sleep(1000,1501);
					}
				}
			} else {
				//If the KBD is on screen, attack it
				if (KBD.isOnScreen()) {
					if (Players.getLocal().getInteracting() == null && KBD.getAnimation() != 17780) {
						KBD.interact("Attack");
						Task.sleep(1500,2001);
					}
				} else {
					//If it is not on screen, lets move the camera so we can see it
					Camera.setPitch(Random.nextInt(30,50));
					Camera.turnTo(KBD);
				}
			}
		}
	}
}
