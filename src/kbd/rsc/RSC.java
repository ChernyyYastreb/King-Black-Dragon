package kbd.rsc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

import kbd.main.KingBlackDragon;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Skills;
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
	public static final int[] FOOD_IDS = { 15266, 385, 7946, 6705, 373, 365, 379, 361 },
							  ANTI_FIRE_IDS = {2458,2456,2454,2452},
							  RANGING_POTION_IDS = { 173, 171, 169, 2444 }, //1,2,3,4
							  RANGING_FLASK_IDS = { 23313, 23311, 23309, 23307, 23305, 23303 }, //1,2,3,4,5,6
							  EXTREME_RANGING_FLASK_IDS = { 23524, 23523, 23522, 23521, 23520, 23519 },
							  EXTREME_RANGING_POTION_IDS = { 15327, 15326, 15325, 15324 }, //1,2,3,4
							  ANTI_POISON_IDS = { 179, 177, 175, 2446 },
							  SUPER_ANTI_POISON_IDS = { 185, 183, 181, 2448 },
							  ANTI_POISON_P_IDS = { 5949, 5947, 5945, 5943 },
							  ANTI_POISON_PP_IDS = { 5958, 5956, 5954, 5952 };
	
	private final static String[] notes = { "Gold ore", "Silver ore", "Yew logs", "Runite ore",
									 "Rune bar", "Adamant bar", "Magic logs"};
	
	public static final Area bankArea = new Area(
			new Tile(3089, 3500, 0), 
			new Tile(3098, 3500, 0), 
			new Tile(3098, 3487, 0), 
			new Tile(3089, 3487, 0));
	
	/**
	 * Retrieves an image from the url given
	 * @param url The url that contains the image
	 * @return The image
	 */
	public static Image getImage(String url) {
		Image im = null;
		int i = 0;
		while (im == null && i < 50) {
			try {
				im = ImageIO.read(new URL(url));
			} catch (IOException e) {
				System.out.println("Try #" + (i + 1));
			}
			i++;
		}
		return im;
	}
	
	/**
	 * Get's the per hour of the value passed in using the start time passed in
	 * @param value Value to get the per hour value of
	 * @param startTime The time to start counting per hour
	 * @return The per hour of the value
	 */
	public static long getPerHour(final long value, final long startTime) {
		return (long)(value * 3600000D / (System.currentTimeMillis() - startTime));
    }
	
	/**
	 * Get's the difference between the boosted level and the real level
	 * @param skill The skill to get the boosted difference of
	 * @return The difference in real level and boosted level
	 */
	public static int getBoosted(int skill) {
		return Skills.getLevel(skill) - Skills.getRealLevel(skill);
	}
	
	/**
	 * Draws a painted mouse
	 * @param g Graphics object to draw from
	 * @param c The color of the object
	 */
	public static void drawMouse(Graphics g, Color c) {
		Graphics2D g1 = (Graphics2D) g;
		g1.setColor(c);
		g1.setStroke(new BasicStroke(2));
        final Point p = Mouse.getLocation();
        g1.drawLine(0, p.y, Game.getDimensions().width, p.y);
        g1.drawLine(p.x, 0, p.x, Game.getDimensions().height);
	}
	
	/**
	 * Waits for a condition to be true and returns the condition after
	 * it has either been validated, or the method has timed out.
	 * @param c The Condition you want to wait for
	 * @param timeout How long you want to wait for the Condition
	 * @return The Condtion
	 */
	public static boolean waitFor(Condition c, long timeout) {
		Timer t = new Timer(timeout);
		while (!c.validate() && t.isRunning()) {
			Task.sleep(10);
		}
		return c.validate();
	}
	
	/**
	 * Gets the local players HP in real time
	 * @return The local players HP
	 */
	public static int getHp() {
		int hp = Integer.parseInt(Widgets.get(748, 8).getText());
		return hp;
	}
	
	/**
	 * Loots the GroundItem passed in and adds its price to the passed in profit
	 * @param loot The GroundItem to loot
	 * @param profit The profit counter you want to add to
	 * @throws IOException If the getPrice method is interrupted
	 */
	public static void lootItem(GroundItem loot, int profit) throws IOException {
		String[] parts;
		String note = "";
		boolean noted = false;
		int id = 0;
		Mouse.move(loot.getCentralPoint().x,loot.getCentralPoint().y);
		Task.sleep(5, 11);
		parts = loot.getGroundItem().getName().split(" ");
		if (Menu.select("Take", parts.length == 0 ? parts[0]
				: parts[0] + " " + parts[1])) {
			Task.sleep(500,1001);
			if (loot.getGroundItem().getName().equals("Draconic visage")) {
				KingBlackDragon.visageCount++;
			}
			for (String s : notes) {
				if (s.equals(loot.getGroundItem().getName())) {
					note = loot.getGroundItem().getName();
					noted = true;
					break;
				}
			}
			switch (note) {
			case "Gold ore":
				id = 444;
				break;
			case "Silver ore":
				id = 442;
				break;
			case "Yew logs":
				id = 1515;
				break;
			case "Runite ore":
				id = 451;
				break;
			case "Rune bar":
				id = 2363;
				break;
			case "Adamant bar":
				id = 2361;
				break;
			case "Magic logs":
				id = 1513;
				break;
			default:
				id = 0;
				break;
			}
			if (!noted && getPrice(loot.getId()) > 0) {
				profit += (getPrice(loot.getId()) * loot.getGroundItem().getStackSize());
			} else {
				profit += (getPrice(id) * loot.getGroundItem().getStackSize());
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
	
	/**
	 * Gets the price of the item via it's ID
	 * @param id The id of the item 
	 * @return The price of the item
	 */
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
