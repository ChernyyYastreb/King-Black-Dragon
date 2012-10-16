package kbd.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import kbd.handlers.*;
import kbd.rsc.RSC;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.input.Mouse.Speed;
import org.powerbot.game.api.util.Random;

@Manifest(authors= {"TaylorSwift","Jdog653"},
		  name = "Auto KBD",
		  description = "Kills the King Black Dragon",
		  version = 0.01D)
public class KingBlackDragon extends ActiveScript implements PaintListener, MessageListener {
	public static boolean drinkAntiFire = true;
	
	private Tree scriptTree = new Tree(new Node[] {
			new PotionHandler(),
			new BankingHandler(7946),
			new ObjectHandler(),
			new KBDHandler(),
			new WalkingHandler(7946)
	});
	
	public void onStart() {
		Mouse.setSpeed(Speed.FAST);
		KingBlackDragon.this.getContainer().submit(new LoopTask() {
			@Override
			public int loop() {
				
				return 100;
			}
		});
	}

	@Override
	public int loop() {
		final Node stateNode = scriptTree.state();
		if (stateNode != null) {
			scriptTree.set(stateNode);
			final Node setNode = scriptTree.get();
			if (setNode != null) {
				getContainer().submit(setNode);
				setNode.join();
			}
		}
		return Random.nextInt(80, 101);
	}

	@Override
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		RSC.drawMouse(g, new Color(0,0,0,175));
	}

	@Override
	public void messageReceived(MessageEvent e) {
		if (e.getMessage().equalsIgnoreCase("Your resistance to dragonfire is about to run out.")) {
			drinkAntiFire = true;
		}
	}
}
