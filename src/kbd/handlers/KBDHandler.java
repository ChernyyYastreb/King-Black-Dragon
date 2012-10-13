package kbd.handlers;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
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
		
		//If the KBD is dead, let's move the mouse a bit to keep us logged in
		if (KBD == null) {
			if (Random.nextInt(1, 101) == 6) {
				Mouse.move(Random.nextInt(50, 765), Random.nextInt(100, 765));
				if (Random.nextInt(1, 50) == 7) {
					Camera.setAngle(Random.nextInt(100, 360));
				}
			}
		} else {
			//If the KBD is alive and we are too close, walk away from it
			if (Calculations.distance(KBD.getLocation(), Players.getLocal().getLocation()) < 3) {
				Walking.walk(KBD.getLocation().derive(0, -Random.nextInt(5, 7)));
			} else {
				//If the KBD is on screen, attack it
				if (KBD.isOnScreen()) {
					//If we are far enough away, let's attack it
					if (Players.getLocal().getInteracting() == null) {
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
