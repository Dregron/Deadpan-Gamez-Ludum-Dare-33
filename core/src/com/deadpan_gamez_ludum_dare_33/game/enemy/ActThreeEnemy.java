package com.deadpan_gamez_ludum_dare_33.game.enemy;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.deadpan_gamez_ludum_dare_33.Display;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationTimer;
import com.deadpan_gamez_ludum_dare_33.game.Player;
import com.deadpan_gamez_ludum_dare_33.game.dialog.DialogHandler;

public class ActThreeEnemy extends Actor {

	public enum ACT_THREE_STATES {IDLE, WALKING, ATTACKING};
	
	private Animation enemyIdleAnimation;
	
	private TextureRegion currentFrame;
	private ACT_THREE_STATES currentState;
	
	private float stateTime; 
	
	private Player player;
	private TiledMapTileLayer backgroundLayer;
	private DialogHandler dialogHandler;
	private ApplicationResource applicationResource;
	private boolean dialog;
	
	
	
	public ActThreeEnemy(float xPos, float yPos, List<TextureRegion> idleSprites, Player player
			, TiledMapTileLayer backgroundLayer, DialogHandler dialogHandler, ApplicationResource applicationResource) {
		setX(xPos);
		setY(yPos);
		
		TextureRegion[] idle = new TextureRegion[idleSprites.size()];
		for (int i = 0; i < idle.length; i++) {
			idle[i] = idleSprites.get(i);
		}
		
		this.player = player;
		this.backgroundLayer = backgroundLayer;
		this.dialogHandler = dialogHandler;
		this.applicationResource = applicationResource;
		this.dialog = false;
		
		this.currentState = ACT_THREE_STATES.IDLE;
		this.enemyIdleAnimation = new Animation(.2f, idle);
		this.enemyIdleAnimation.setPlayMode(PlayMode.LOOP);
		this.currentFrame = enemyIdleAnimation.getKeyFrame(stateTime, true);
		currentState = ACT_THREE_STATES.IDLE;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.draw(currentFrame, getX(), getY());
	}
	
	@Override
	public void act(float delta) {
		
		stateTime += delta; 
		this.currentFrame = enemyIdleAnimation.getKeyFrame(stateTime, true);
		
		if (isPlayerTwoTilesAwayWithKnifePosition() && !dialog) {
			dialogHandler.addDialogBox(applicationResource, player, -64, 32, 150, 25, "Do not press space", applicationResource.getManager().get(Display.BITMAP_FONT_DIALOG_FNT, BitmapFont.class));
			dialog = true;
		} else if (isPlayerTwoTilesAwayWithKnifePosition() && dialog && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			remove();
			applicationResource.getManager().get(Display.SOUND_ENEMY_TAKE, Sound.class).play();
		}
		
	}
	
	public ACT_THREE_STATES getCurrentState() {
		return currentState;
	}
	
	private boolean isPlayerTwoTilesAwayWithKnifePosition() {
		return getX()+1 > player.getX()-(1*backgroundLayer.getTileWidth()) 
				&& getX()-1 * backgroundLayer.getTileWidth() < player.getX()+(1*backgroundLayer.getTileWidth())
				&& getY()+1 * backgroundLayer.getTileHeight() > player.getY()-(1*backgroundLayer.getTileHeight()) 
				&& getY()-1 * backgroundLayer.getTileHeight() < player.getY()+(1*backgroundLayer.getTileHeight());
	}
}
