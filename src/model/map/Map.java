package model.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.MoveableEntity;
import model.item.Item;
import model.npc.NPC;
import model.Frame;
import model.IUpdatable;
import model.player.Player;
import ui.GameScene;
import ui.StatusBar;

public class Map extends Frame implements IUpdatable {
	
	public static final int X_PADDING = 150;
	public static final int Y_PADDING = 66;
	
	private Image img;
	private double moveSpeed = 4;
	
	private MediaPlayer bgm;
	
	private List<NPC> listNPC= new ArrayList<>();
	private List<Item> listItem = new ArrayList<>();
	
	private StatusBar statusBar = new StatusBar();

	
	public Map(Image img, AudioClip bgm) {
		super(0, 0, img.getWidth(), img.getHeight());
		this.img = img;
		
		this.bgm = new MediaPlayer(new Media(bgm.getSource()));
		this.bgm.setCycleCount(MediaPlayer.INDEFINITE);
	}
	
	public List<NPC> collideCharacter(Frame f) {
		List<NPC> npcs = new ArrayList<NPC>();
		for (NPC e: listNPC) {
			if (e instanceof NPC && f.isCollideWith(e)) {
				npcs.add((NPC) e);
			}
		}
		return npcs;
	}
	
	public List<Item> collideItems(Frame f) {
		List<Item> collideItems = new ArrayList<Item>();
		for (Item e: listItem) {
			if (e instanceof Item && f.isCollideWith(e)) {
				collideItems.add((Item) e);
			}
		}
		return collideItems;
	}
	
	public void removeItem(Item item) {
		listItem.remove(item);
	}
	
	private void moveMap() {
		// Move map by object inside
		Player player = GameManager.getInstance().getPlayer();
		if (player.getPosX() > GameScene.WINDOW_WIDTH + posX - player.getWidth() - X_PADDING) {			
			posX += moveSpeed;
		} else if (player.getPosX() < posX + X_PADDING) {			
			posX -= moveSpeed;
		}
		if (player.getPosY() > GameScene.WINDOW_HEIGHT + posY - player.getHeight() - Y_PADDING) {			
			posY += moveSpeed;
		} else if (player.getPosY() < posY + 333 + Y_PADDING) {			
			posY -= moveSpeed;
		}

		// Map boundary
		if (posX < 0) {			
			posX = 0;
		} else if (posX > width - GameScene.WINDOW_WIDTH) {			
			posX = width - GameScene.WINDOW_WIDTH;
		}
		if (posY < 0) {			
			posY = 0;
		} else if (posY > height - GameScene.WINDOW_HEIGHT) {			
			posY = height - GameScene.WINDOW_HEIGHT;
		}
	}
	
	private void motion(MoveableEntity e) {
		e.move();
		if (e.getPosX() < 0)
			e.setPosX(0);
		else if (e.getPosX() + e.getWidth() > width)
			e.setPosX(width - e.getWidth());
		if (e.getPosY() < 333)
			e.setPosY(333);
		else if (e.getPosY() + e.getHeight() > height)
			e.setPosY(height - e.getHeight());
	}
	
	public void motionPlayer(Player p) {
		motion(p);
		moveMap();
	}
	
	public void motionAll() {
		for (NPC i: listNPC) {
			motion(i);
		}
	}
	
	public void spawnRandom() {
		double x = 333 + (Math.random() * (height/2));
		double y = 333 + (Math.random() * (width/2));
		NPC monster = new NPC(x, y);
		listNPC.add(monster);
	}
	
	public void render(GraphicsContext gc) {
		gc.drawImage(img, -posX, -posY);
		GameManager.getInstance().getPlayer().render(gc);
		for (NPC e: listNPC) {			
			e.render(gc);
		}
		for (Item i:listItem) {
			i.render(gc);
		}
		statusBar.render(gc);
		
	}

	@Override
	public void update() {
		Iterator<NPC> npcIt = listNPC.listIterator();
		while (npcIt.hasNext()) {
			if (npcIt.next().isDead()) {
				npcIt.remove();
			}
		}
		
		for (NPC e: listNPC) {
			e.update();
		}
		
		for (int i = listItem.size()-1; i >= 0; i--) {
			listItem.get(i).addExpireTick();
			if (listItem.get(i).isExpired()) {
				listItem.remove(i);
			}
		}
	}
	
	public void playBgm() {
		this.bgm.play();
	}
	
	// Getters & Setters

	public List<NPC> getListNPC() {
		return listNPC;
	}

	public List<Item> getListItem() {
		return listItem;
	}
	

}
