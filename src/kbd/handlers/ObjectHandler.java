package kbd.handlers;

import kbd.rsc.Condition;
import kbd.rsc.RSC;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 * @author Nolan
 * Svetty, this is powerbot bro.
 */
public class ObjectHandler extends Node {

	private final int entranceId = 77834,
					  ditchId = 65084,
					  exitId = 1817;
	
	private SceneObject entrance, ditch, exit;

	@Override
	public boolean activate() {
		entrance = SceneEntities.getNearest(entranceId);
		ditch = SceneEntities.getNearest(ditchId);
		exit = SceneEntities.getNearest(exitId);
		if (entrance != null
			&& Inventory.contains(RSC.FOOD_IDS)) {
			return true;
		} else if (exit != null
				   && !Inventory.contains(RSC.FOOD_IDS)) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		final WidgetChild warningScreen = Widgets.get(1361, 13);
		
		//Below we handle exiting the cave
		if (exit != null && !Inventory.contains(RSC.FOOD_IDS)) {
			if (exit.isOnScreen() 
				&& Players.getLocal().getAnimation() != 8939) {
				if (exit.click(true)) {
					Task.sleep(2000,3001);
				}
			} else {
				Walking.walk(exit);
			}
		}
		
		//Here, we handle entering the cave
		if (entrance != null) {
			if (entrance.isOnScreen() 
				&& !warningScreen.validate()
				&& Players.getLocal().getAnimation() == -1) {
				if (!Players.getLocal().isMoving() && entrance.click(true)) {
					Task.sleep(2000,2201);
				}
			} else {
				//if we are not in the teleport, 
				//animation then we can walk to the object
				if (Players.getLocal().getAnimation() != 827
					&& Players.getLocal().getAnimation() != 8939
					&& !Players.getLocal().isMoving()) {
					Walking.walk(entrance);
				}
			}
		}
		
		//If we accidentally cross the ditch, get back over
		if (Players.getLocal().getLocation().getY() > 3520) {
			if (ditch != null
				&& ditch.click(true)) {
				RSC.waitFor(new Condition() {
					@Override
					public boolean validate() {
						return Players.getLocal().getLocation().getY() < 3520;
					}
					
				}, 4000);
			}
		}
		
		//if the warning screen is active, click continue
		if (warningScreen.validate()) {
			if (warningScreen.click(true)) {
				Task.sleep(1500,2001);
			}
		}
	}

}
