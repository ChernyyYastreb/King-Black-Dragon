package kbd.rsc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;

/**
 * @author Nolan, Jordan
 * This class contains static fields, methods, and/or classes that are referenced multiple times in other classes.
 * Similar to the final Math class, RSC provides methods useful to the writer and other programmers.
 */
public class RSC {
	public static final int[] FOOD_IDS = { 7946 },
							  ANTI_FIRE_IDS = {2458,2456,2454,2452};
	
	public static final Area bankArea = new Area(
			new Tile(3089, 3500, 0), 
			new Tile(3098, 3500, 0), 
			new Tile(3098, 3487, 0), 
			new Tile(3089, 3487, 0));
	
	public static final void drawMouse(Graphics g, Color c) {
		Graphics2D g1 = (Graphics2D) g;
		g1.setColor(c);
		g1.setStroke(new BasicStroke(2));
        final Point p = Mouse.getLocation();
        g1.drawLine(0, p.y, Game.getDimensions().width, p.y);
        g1.drawLine(p.x, 0, p.x, Game.getDimensions().height);
	}
	
	public static boolean waitFor(Condition c, long timeout) {
		Timer t = new Timer(timeout);
		while (!c.validate() && t.isRunning()) {
			if (c.validate()) {
				return true;
			}
			Task.sleep(10);
		}
		return c.validate();
	}
	
	public static int getHp() {
		int hp = Integer.parseInt(Widgets.get(748, 8).getText());
		return hp;
	}
	
	public static void lootItem(GroundItem loot, int profit) throws IOException {
		String[] parts;
		Mouse.move(loot.getCentralPoint().x,loot.getCentralPoint().y);
		Task.sleep(5, 11);
		parts = loot.getGroundItem().getName().split(" ");
		if (Menu.select("Take", parts.length == 0 ? parts[0]
				: parts[0] + " " + parts[1])) {
			Task.sleep(500,1001);
			if (getPrice(loot.getId()) > 0) {
				profit += (getPrice(loot.getId()) * loot.getGroundItem().getStackSize());
			}
			final Timer timer = new Timer(1500);
			while (timer.isRunning() && loot.validate()) {
				Task.sleep(120);
				if (Players.getLocal().isMoving()) {
					timer.reset();
				}
			}
		}
	}
	
	public static int getPrice(final int id) {
		try {
			final URL url = new URL(
					"http://open.tip.it/json/ge_single_item?item="
							.concat(Integer.toString(id)));
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(url.openStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("mark_price")) {
					reader.close();
					return Integer.parseInt(line.substring(
							line.indexOf("mark_price") + 13,
							line.indexOf(",\"daily_gp") - 1)
							.replaceAll(",", ""));
				}
			}
		} catch (final Exception e) {
			return -1;
		}
		return -1;
	}
}
