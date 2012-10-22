package kbd.main;

import java.awt.Color;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import kbd.handlers.*;
import kbd.rsc.*;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.util.Random;

@Manifest(authors= {"TaylorSwift","Jdog653"},
		  name = "Auto KBD",
		  description = "Kills the King Black Dragon",
		  version = 0.02,
		  website = "http://www.powerbot.org/community/topic/829037-auto-king-black-dragon-sdn/")
public class KingBlackDragon extends ActiveScript implements PaintListener, MessageListener {
	public static boolean drinkAntiFire = true, 
			stop = false, 
			friendsChat = false,
			wait = true;
	
	public static int profit, killCount, visageCount;
	
	public static String information;
	
	private long startTime; //hour = 3600000;
	
	//private long lastSent;
	
	//PAINT URL - http://i.imgur.com/cnzNW.png, Text font = Arial, RGB palate = 136 128 224
	//Text effects = drop shadow, 2px distance/size @ 90degrees
	
	private Image paint = RSC.getImage("http://i.imgur.com/cnzNW.png");
	
	//private Font paintFont = new Font("Arial", 4, 11);
	
	private Tree scriptTree = new Tree(new Node[] {
			new PotionHandler(),
			new BankingHandler(),
			new ObjectHandler(),
			new LootHandler(),
			new KBDHandler(),
			new WalkingHandler()
	});
	
	//http://tswiftkbdsignatures.net76.net/signature.php?user=
	
	private boolean validate() {
		return Game.getClientState() == 11;
	}
	
	public void onStart() {
		profit = 0;
		killCount = 0;
		visageCount = 0;
		information = "";
		startTime = System.currentTimeMillis();
		Tabs.FRIENDS_CHAT.open();
		Task.sleep(400);
		if (Widgets.get(1109, 20).visible()) {
			friendsChat = true;
		}
		if (friendsChat) {
			if (Widgets.get(1109, 19).validate() && Widgets.get(1109, 19).getTextureId() == 1070) {
				if (Widgets.get(1109, 19).click(true)) {
					Task.sleep(1000);
					wait = false;
				}
			}
		}
		wait = false;
		/*getContainer().submit(new LoopTask() {
			@Override
			public int loop() {
				if (lastSent >= hour) {
					try {
						final URL url = new URL(
							"http://tswiftkbdsignatures.net76.net/submitdata.php?user="
								+ Environment.getDisplayName()
								+ "&timeran="
								+ (System.currentTimeMillis() - startTime)
								+ "&profit=" + profit + "&killed="
								+ killCount + "&visages=" + visageCount);
						URLConnection con = url.openConnection();
						con.setDoInput(true);
						con.setDoOutput(true); 
						con.setUseCaches(false);
						final BufferedReader rd = new BufferedReader(
								new InputStreamReader(con.getInputStream()));
						String line;
						while ((line = rd.readLine()) != null) {
							if (line.toLowerCase().contains("success")) {
								log.info("Successfully updated signature.");
							} else if (line.toLowerCase().contains("fuck off")) {
								log.info("Something fucked up, couldn't update.");
							}
						}
						rd.close();
						//lastSent = System.currentTimeMillis();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return (int) hour;
			}
		});*/
		JOptionPane.showMessageDialog(null, "If you want to use a friendschat, " +
				"\njoin the friendschat before you start the script!");
	}
	
	@Override
	public void onStop() {
		try {
			String user = Base64.encode(Environment.getDisplayName());
			String timerun = Base64.encode(Long.toString((System.currentTimeMillis() - startTime)));
			String profits = Base64.encode(Integer.toString(profit));
			String killed = Base64.encode(Integer.toString(killCount)); //ready
			String visagesc = Base64.encode(Integer.toString(visageCount));
			final URL url = new URL(
				"http://tswiftkbdsignatures.net76.net/submitdata.php?user="
					+ user
					+ "&timerun="
					+ timerun
					+ "&profit=" + profits + "&killed="
					+ killed + "&visages=" + visagesc);
			URLConnection con = url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true); 
			con.setUseCaches(false);
			final BufferedReader rd = new BufferedReader( //can you have it print URL ?
					new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if (line.toLowerCase().contains("success")) {
					log.info("Successfully updated signature.");
				} else if (line.toLowerCase().contains("missing") || line.toLowerCase().contains("fak'd")) {
					log.info("Something fucked up, couldn't update.");
				}
			}
			rd.close();
			//lastSent = System.currentTimeMillis();
			System.out.println(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int loop() {
		if (stop) {
			shutdown();
		}
		if (validate() && !wait) {
			final Node stateNode = scriptTree.state();
			if (stateNode != null) {
				scriptTree.set(stateNode);
				final Node setNode = scriptTree.get();
				if (setNode != null) {
					getContainer().submit(setNode);
					setNode.join();
				}
			}
		}
		return Random.nextInt(80, 101);
	}

	//142,515
	@Override
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		
		long millis = System.currentTimeMillis() - startTime;
		long hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		long minutes = millis / (1000 * 60);
		millis -= minutes * (1000 * 60);
		long seconds = millis / 1000;
		
		RSC.drawMouse(g, new Color(0,0,0,175));
		g.drawImage(paint, -1, 317, null);
		g.setColor(new Color(136, 128, 224));
		g.drawString(hours + ":" + minutes + ":" + seconds, 142, 451);
		g.drawString("" + killCount, 92, 492);
		g.drawString("" + profit, 374, 450);
		g.drawString("" + visageCount, 398, 492);
	}
	
	//
	@Override
	public void messageReceived(MessageEvent e) {
		String message = e.getMessage().toString();
		//information = message + "[sent: " + e.getSender() + "]";
		log.info(message + ", sent by: " + e.getSender().toString());
		if (message.contains("Your resistance to dragonfire is about to run out.")
				&& e.getSender().equals("")) {
			drinkAntiFire = true;
		}
	}
	
}
