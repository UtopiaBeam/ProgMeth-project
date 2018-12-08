package model.item;

import constants.Images;
import controller.GameManager;
import model.player.Player;

public class HealItem extends Item {
	
	private int healHp;
	
	public HealItem() {
		super("Heal Potion", 5, Images.heal);
		this.healHp = 50;
	}
	
	public HealItem(double posX, double posY) {
		super("Heal Potion", 5, Images.heal, posX, posY);
		this.healHp = 50;
	}
	
	@Override
	public boolean activate() {
		Player player = GameManager.getInstance().getPlayer();
		return player.heal(healHp);
	}
	
	// Getters & Setters
	
	public int getHealHp() {
		return healHp;
	}

}
