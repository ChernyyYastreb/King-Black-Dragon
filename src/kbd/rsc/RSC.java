package kbd.rsc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

/**
 * @author Nolan, Jordan
 * This class contains static fields, methods, and/or classes that are referenced multiple times in other classes.
 * In comparison to the abstract static Math class, RSC provides methods useful to the writer and other programmers.
 */
public class RSC {
	public static final int[] FOOD_IDS = { 7946 };
	
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
}
