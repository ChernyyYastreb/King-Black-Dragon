package kbd.handlers;

import kbd.rsc.RSC;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.tab.Inventory;

public class WalkingHandler extends Node {
	private int foodId;
	
	public WalkingHandler(final int foodId) {
		this.foodId = foodId;
	}
	
	@Override
	public boolean activate() {
		return Inventory.contains(this.foodId);
	}

	@Override
	public void execute() {
		Walking.newTilePath(RSC.toArtifact).traverse();
	}
}
