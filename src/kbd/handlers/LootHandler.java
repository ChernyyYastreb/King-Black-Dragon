package kbd.handlers;

import java.io.IOException;

import kbd.main.KingBlackDragon;
import kbd.rsc.RSC;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class LootHandler extends Node {
	private final int[] loot = {
		25316,25318,1149,1127,9144,1303,563,560,565,454,18778,1185,
		1319,1373,1215,1216,892,533,2999,258,3001,270,450,7937,1443,
		372,384,1615,1631,1392,574,570,2364,452,2362,5315,5316,5289,
		5304,5300,1516,9342,20667,6686,443,445,1514,1516,7980,11286,
		25312,25314,12163,12160,12159,12158};
	
	@Override
	public boolean activate() {
		return Players.getLocal().getLocation().getY() > 4000
			&& GroundItems.getNearest(loot) != null;
	}

	@Override
	public void execute() {
		GroundItem loot = GroundItems.getNearest(this.loot);
		if (loot != null) {
			if (loot.isOnScreen()) {
				try {
					RSC.lootItem(loot, KingBlackDragon.profit);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if (!Players.getLocal().isMoving()) {
					Walking.walk(loot);
				}
			}
		}
	}
}
