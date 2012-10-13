package kbd.handlers;

import kbd.rsc.RSC;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class ObjectHandler extends Node {

	private final int entranceId = 77834,
					  ditchId = 65084,
					  exitId = 1817;

	@Override
	public boolean activate() {
		if (SceneEntities.getNearest(entranceId) != null
			&& Inventory.contains(RSC.FOOD_IDS)) {
			return true;
		} else if (SceneEntities.getNearest(exitId) != null
				   && !Inventory.contains(RSC.FOOD_IDS)) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		final WidgetChild warningScreen = Widgets.get(1361, 13);
		
		//Below we handle exiting the cave
		if (SceneEntities.getNearest(exitId) != null) {
			if (SceneEntities.getNearest(exitId).isOnScreen() 
				&& Players.getLocal().getAnimation() != 8939) {
				if (SceneEntities.getNearest(exitId).click(true)) {
					Task.sleep(2000,3001);
				}
			} else {
				Walking.walk(SceneEntities.getNearest(exitId));
			}
		}
		
		//Here, we handle enter the cave
		if (SceneEntities.getNearest(entranceId) != null) {
			if (SceneEntities.getNearest(entranceId).isOnScreen() 
				&& !warningScreen.validate()
				&& Players.getLocal().getAnimation() == -1) {
				if (!Players.getLocal().isMoving() && SceneEntities.getNearest(entranceId).click(true)) {
					Task.sleep(2000,2201);
				}
			} else {
				if (Players.getLocal().getAnimation() != 827
					&& Players.getLocal().getAnimation() != 8939) {
					Walking.walk(SceneEntities.getNearest(entranceId));
				}
			}
		}
		
		//If we accidentally cross the ditch, get back over
		if (Players.getLocal().getLocation().getY() > 3520) {
			if (SceneEntities.getNearest(ditchId) != null
				&& SceneEntities.getNearest(ditchId).click(true)) {
				Task.sleep(2500,3001);
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
