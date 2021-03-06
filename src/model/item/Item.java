package model.item;

import controller.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Entity;
import model.IUpdatable;
import model.IUseable;

public abstract class Item extends Entity implements IUpdatable, IUseable {
	
	private int count = 0;
	private int maxCount;
	private final int expireTime = 300;
	private int expireTick = 0;
	
	public Item(String name, int maxCount, Image image) {
		super(0, 0, image.getWidth(), image.getHeight(), name, image);
		this.maxCount = maxCount;
	}
	
	public Item(String name, int maxCount, Image image, double posX, double posY) {
		super(posX, posY, image.getWidth(), image.getHeight(), name, image);
		this.maxCount = maxCount;
	}
	
	public boolean isExpired() {
		return expireTick >= expireTime;
	}
	
	public boolean addCount(int count) {
		if (this.count + count > this.maxCount) {
			return false;
		}
		this.count += count;
		return true;
	}
	
	public void addExpireTick() {
		if (expireTick < expireTime) {
			expireTick++;
		}
	}
	
	@Override
	public void update() {
		addExpireTick();
	}
	
	@Override
	public void use() {
		if (count > 0) {
			if (activate()) {				
				count--;
			};
		}
	}
	
	public abstract boolean activate();
	
	public void render(GraphicsContext gc){
		gc.drawImage(getImage(), posX-GameManager.getInstance().getCurrentMap().getPosX(), posY-GameManager.getInstance().getCurrentMap().getPosY());
	}
	
	// Getters & Setters

	public int getCount() {
		return count;
	}
	
}
