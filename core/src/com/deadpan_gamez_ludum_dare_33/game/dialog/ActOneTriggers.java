package com.deadpan_gamez_ludum_dare_33.game.dialog;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.game.Player;
import com.deadpan_gamez_ludum_dare_33.game.enemy.ActOneEnemy;
import com.deadpan_gamez_ludum_dare_33.game.enemy.ActOneEnemy.ANIMATION_STATE;

public class ActOneTriggers {

	private DialogHandler dialogHandler;
	private ApplicationResource applicationResource;
	private Player player;
	private ActOneEnemy actOneEnemy;
	private BitmapFont bitmapFont;
	private int i = 0;
	private String[] dialogList = new String[4];
	private boolean trigger = false;
	private boolean attack = false;
	
	public ActOneTriggers(ApplicationResource applicationResource, DialogHandler dialogHandler, Player player, ActOneEnemy actOneEnemy, BitmapFont bitmapFont) {
		this.applicationResource = applicationResource;
		this.dialogHandler = dialogHandler;
		this.player = player;
		this.actOneEnemy = actOneEnemy;
		this.actOneEnemy = actOneEnemy;
		this.bitmapFont = bitmapFont;
		
		this.dialogList[0] = "Wh... where am i ?";
		this.dialogList[1] = "Wh... who are you !?";
		this.dialogList[2] = "YOUR AWAKE! ARGGGH!!!";
		this.dialogList[3] = "PRESS SPACE BAR!!!!!";
	}
	
	public void update() {
		if (isTrigger()) {
			if (i <= 1) {
				dialogHandler.addDialogBox(applicationResource, player, 150, 25, dialogList[i], bitmapFont);
				i++;
				setTrigger(false);
			} else if (i == 2) {
				dialogHandler.addDialogBox(applicationResource, actOneEnemy, 150, 25, dialogList[i], bitmapFont);
				i++;
				setTrigger(false);
				actOneEnemy.setCurrentState(ANIMATION_STATE.WALKING);
			} else if (i == 3) {
				dialogHandler.addDialogBox(applicationResource, player, 150, 25, dialogList[i], bitmapFont);
				i++;
			}
		}
		
		if(dialogHandler.getSize() == 0 && i != dialogList.length) {
			setTrigger(true);
		}
	}
	
	public void setAttack(boolean attack) {
		this.attack = attack;
	}
	
	public boolean isAttack() {
		return attack;
	}
	
	public void setTrigger(boolean trigger) {
		this.trigger = trigger;
	}
	
	public boolean isTrigger() {
		return trigger;
	}
	
}
