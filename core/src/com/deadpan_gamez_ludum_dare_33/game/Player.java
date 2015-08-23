package com.deadpan_gamez_ludum_dare_33.game;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor{

	private Animation idleAnimation;
	private TextureRegion currentFrame;
	
	private float stateTime; 
	
	public Player(float xPos, float yPos, float width, float height, List<TextureRegion> idleSprites) {
		this.setX(xPos);
		this.setY(yPos);
		this.setWidth(width);
		this.setHeight(height);
		
		TextureRegion[] blah = new TextureRegion[idleSprites.size()];
		for (int i = 0; i < blah.length; i++) {
			blah[i] = idleSprites.get(i);
		}
		this.idleAnimation = new Animation(.5f, blah);
		this.idleAnimation.setPlayMode(PlayMode.LOOP);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void act(float delta) {
		stateTime += delta;           
        currentFrame = idleAnimation.getKeyFrame(stateTime, true);
	}
}
