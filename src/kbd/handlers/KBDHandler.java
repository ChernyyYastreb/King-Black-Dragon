package kbd.handlers;

import kbd.main.KingBlackDragon;
import kbd.rsc.RSC;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class KBDHandler extends Node {
	private NPC KBD;
	
	private int[] potionIds, antiPoisonIds;
	
	private boolean count = true, check = true;
	
	private Tile teleTile = new Tile(2273, 4681, 0),
				 middleTile = new Tile(2273, 4694, 0);
	
	private void chosePotions() {
		if (Inventory.contains(RSC.EXTREME_RANGING_FLASK_IDS)) {
			potionIds = RSC.EXTREME_RANGING_FLASK_IDS;
		} else if (Inventory.contains(RSC.EXTREME_RANGING_POTION_IDS)) {
			potionIds = RSC.EXTREME_RANGING_POTION_IDS;
		} else if (Inventory.contains(RSC.RANGING_FLASK_IDS)) {
			potionIds = RSC.RANGING_FLASK_IDS;
		} else if (Inventory.contains(RSC.RANGING_POTION_IDS)) {
			potionIds = RSC.RANGING_POTION_IDS;
		} else {
			potionIds = null;
		}
		if (Inventory.getItem(RSC.ANTI_POISON_PP_IDS) != null) {
			antiPoisonIds = RSC.ANTI_POISON_PP_IDS;
		} else if (Inventory.getItem(RSC.ANTI_POISON_P_IDS) != null) {
			antiPoisonIds = RSC.ANTI_POISON_P_IDS;
		} else if (Inventory.getItem(RSC.SUPER_ANTI_POISON_IDS) != null) {
			antiPoisonIds = RSC.SUPER_ANTI_POISON_IDS;
		} else if (Inventory.getItem(RSC.ANTI_POISON_IDS) != null) {
			antiPoisonIds = RSC.ANTI_POISON_IDS;
		} else {
			antiPoisonIds = null;
		}
	}

	@Override
	public boolean activate() {
		//Only activate if we are in the KBD cave
		return Players.getLocal().getLocation().getY() > 4600
				&& Inventory.contains(RSC.FOOD_IDS);
	}

	@Override
	public void execute() {
		//Instantiate the KBD as an NPC
		KBD = NPCs.getNearest(50); //50 - the king black dragon's ID
		
		//If we have less than 600 Hp, let's eat
		if (RSC.getHp() < 600 && Inventory.getItem(RSC.FOOD_IDS) != null) {
			Inventory.getItem(RSC.FOOD_IDS).getWidgetChild().click(true);
			Task.sleep(1500,2001);
		}
		
		if (KingBlackDragon.friendsChat && check) {
			Tabs.FRIENDS_CHAT.open();
			Task.sleep(400);
			if (Widgets.get(1109, 19).getTextureId() != 1070) {
				check = false;
			}
			if (check) {
				if (Widgets.get(1109, 19).validate() && Widgets.get(1109, 19).getTextureId() == 1070
						&& Widgets.get(1109, 20).visible()) {
					if (Widgets.get(1109, 19).click(true)) {
						check = false;
						Task.sleep(1000);
					}
				}
			}
		}
		
		//If we can count
		if (count) {
			//If the KBD is dying
			if (KBD != null && KBD.getHpRatio() == 0) {
				KingBlackDragon.killCount++;
				System.out.println(KingBlackDragon.killCount);
				count = false;
				check = true;
			}
		}
		
		//If we KBD is null, we should be able to count again
		if (KBD == null) {
			count = true;
		}
		
		//If we are less than 8 levels boosted 
		//and we have potions in our inventory, let's drink a potion
		chosePotions();
		if (potionIds != null && RSC.getBoosted(Skills.RANGE) < 6) {
			if (Inventory.getItem(potionIds) != null) {
				if (Inventory.getItem(potionIds).getWidgetChild().click(true)) {
					Task.sleep(1500,2001);
				}
			}
		}
		
		//If we are poisoned and we have some antipoison, drink some
		if (antiPoisonIds != null && Widgets.get(748, 4).validate() 
				&& Widgets.get(748, 4).getTextureId() == 1801) {
			if (Inventory.contains(antiPoisonIds)) {
				if (Widgets.get(748, 4).click(true)) {
					Task.sleep(800, 1001);
				}
			}
		}
		
		if (Players.getLocal().getLocation().equals(teleTile)) {
			int derive = Random.nextInt(0, 3);
			switch (derive) {
			case 0:
				Walking.walk(middleTile.derive(1, 0));
				break;
			case 1:
				Walking.walk(middleTile.derive(-1, 0));
				break;
			case 2:
				Walking.walk(middleTile.derive(0, 1));
			}
		}
		
		//If we have any vials in our inventory, drop them
		if (Inventory.contains(new int[]{229})) {
			Inventory.getItem(229).getWidgetChild().interact("Drop");
		}
		
		//If the KBD is dead, let's move the mouse a bit to keep us logged in
		if (KBD == null) {
			if (Random.nextInt(1, 301) == 6) {
				Mouse.move(Random.nextInt(50, 765), Random.nextInt(100, 765));
			}
			if (Random.nextInt(1, 301) == 7) {
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
