package com.deadpan_gamez_ludum_dare_33.game.dialog;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.game.Player;
import com.deadpan_gamez_ludum_dare_33.game.enemy.ActOneEnemy;
import com.deadpan_gamez_ludum_dare_33.game.environment.Knife;

public class DialogHandler {

	private List<DialogBox> boxes = new ArrayList<>();
	private Stage stage;
	private AssetManager manager;
	
	public DialogHandler(Stage stage, AssetManager manager) {
		this.stage = stage;
		this.manager = manager;
	}
	
	public void draw(ShapeRenderer renderer) {
		for (DialogBox box : boxes) {
			box.draw(renderer);
		}
	}
	
	public void update(float delta) {
		
		for (int i = 0; i < boxes.size(); i++) {
			if (boxes.get(i).isDestroyed()) {
				boxes.get(i).dispose();
				boxes.get(i).remove();
				boxes.remove(i);
			}
		}
	}
	
	public void addDialogBox(ApplicationResource applicationResource, Player player, float width, float height, String message, BitmapFont bitmapFont) {
		DialogBox dialogBox = new DialogBox(applicationResource, player, width, height, message, bitmapFont);
		boxes.add(dialogBox);
		stage.addActor(dialogBox);
	}
	
	public void addDialogBox(ApplicationResource applicationResource, Player player, float offSetX, float offSetY, float width, float height, String message, BitmapFont bitmapFont) {
		DialogBox dialogBox = new DialogBox(applicationResource, player, offSetX, offSetY, width, height, message, bitmapFont);
		boxes.add(dialogBox);
		stage.addActor(dialogBox);
	}
	
	public void addDialogBox(ApplicationResource applicationResource, ActOneEnemy player, float width, float height, String message, BitmapFont bitmapFont) {
		DialogBox dialogBox = new DialogBox(applicationResource, player, width, height, message, bitmapFont);
		boxes.add(dialogBox);
		stage.addActor(dialogBox);
	}
	
	public void addDialogBox(ApplicationResource applicationResource, Knife actor, float width, float height, String message, BitmapFont bitmapFont) {
		DialogBox dialogBox = new DialogBox(applicationResource, actor, -64, 16, width, height, message, bitmapFont);
		boxes.add(dialogBox);
		stage.addActor(dialogBox);
	}
	
	public int getSize() {
		return boxes.size();
	}
	
	
}
