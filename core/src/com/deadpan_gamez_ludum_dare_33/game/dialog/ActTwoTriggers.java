package com.deadpan_gamez_ludum_dare_33.game.dialog;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.deadpan_gamez_ludum_dare_33.Display;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.game.Player;
import com.deadpan_gamez_ludum_dare_33.game.environment.Knife;


public class ActTwoTriggers {

	private DialogHandler dialogHandler;
	private ApplicationResource applicationResource;
	private BitmapFont bitmapFont;
	private Player player;
	private Knife knife;
	
	private String[] dialogList = new String[2];
	private boolean triggerDialog = true;
	private int i = 0;
	
	public ActTwoTriggers(ApplicationResource applicationResource, DialogHandler dialogHandler, Player player, Knife knife) {
		this.applicationResource = applicationResource;
		this.dialogHandler = dialogHandler;
		this.player = player;
		this.knife = knife;
		this.bitmapFont = applicationResource.getManager().get(Display.BITMAP_FONT_DIALOG_FNT, BitmapFont.class);
		
		this.dialogList[0] = "Press the arrow keys to move around";
		this.dialogList[1] = "Press Space to pick me up!";
	}
	
	public void update() {
		if (isTriggerDialog()) {
			
			if (i == 0) {
				dialogHandler.addDialogBox(applicationResource, player, -64, 16, 150, 25, dialogList[i], bitmapFont);
				setTriggerDialog(false);
				i++;
			} else if (i == 1) {
				dialogHandler.addDialogBox(applicationResource, knife, 150, 25, dialogList[i], bitmapFont);
				setTriggerDialog(false);
				i++;
			} 
		}
	}
	
	public void setTriggerDialog(boolean triggerDialog) {
		this.triggerDialog = triggerDialog;
	}
	
	public boolean isTriggerDialog() {
		return triggerDialog;
	}
}
