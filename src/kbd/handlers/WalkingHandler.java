package kbd.handlers;


import kbd.rsc.RSC;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;

public class WalkingHandler extends Node {
	private int foodId;
	
	private final Tile[] toArtifact = {
			new Tile(3092, 3496, 0), 
			new Tile(3092, 3502, 0), 
			new Tile(3086, 3502, 0), 
			new Tile(3077, 3502, 0), 
			new Tile(3072, 3502, 0), 
			new Tile(3070, 3507, 0), 
			new Tile(3065, 3511, 0), 
			new Tile(3056, 3513, 0), 
			new Tile(3052, 3517, 0)};
			
			/*toBank = {
			new Tile(3052, 3517, 0),
			new Tile(3056, 3513, 0), 
			new Tile(3065, 3511, 0), 
			new Tile(3070, 3507, 0), 
			new Tile(3072, 3502, 0), 
			new Tile(3077, 3502, 0), 
			new Tile(3086, 3502, 0), 
			new Tile(3092, 3502, 0),
			new Tile(3092, 3496, 0)};*/
	
	public WalkingHandler(final int foodId) {
		this.foodId = foodId;
	}
	
	@Override
	public boolean activate() {
		if (Inventory.contains(new int[]{this.foodId}) && Players.getLocal().getLocation().getY() < 4000) {
			return true;
		}
		if (Inventory.getItem(this.foodId) == null 
				&& !RSC.bankArea.contains(Players.getLocal().getLocation())
				&& Players.getLocal().getLocation().getY() < 4000) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		if (Inventory.getItem(this.foodId) != null && Players.getLocal().getLocation().getY() < 4000) {
		    Walking.newTilePath(toArtifact).traverse();
		}
		if (Inventory.getItem(this.foodId) == null 
				&& !RSC.bankArea.contains(Players.getLocal().getLocation())
				&& Players.getLocal().getLocation().getY() < 4000) {
			Walking.newTilePath(toArtifact).reverse().traverse();
		}
	}
}
