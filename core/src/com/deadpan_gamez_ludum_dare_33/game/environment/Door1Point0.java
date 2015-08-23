package com.deadpan_gamez_ludum_dare_33.game.environment;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Door1Point0 extends Actor {

	public enum DOOR_STATE {CLOSED, OPEN};
	
	private TextureRegion closed, open;
	private DOOR_STATE currentState;
	
	public Door1Point0(float xPos, float yPos, List<TextureRegion> closedSprites, List<TextureRegion> openSprites) {
		setX(xPos);
		setY(yPos);
		
		currentState = DOOR_STATE.CLOSED;
		closed = closedSprites.get(0);
		open = openSprites.get(0);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {

		if (currentState.equals(DOOR_STATE.OPEN)) {
			batch.draw(open, getX(), getY());
		} else if (currentState.equals(DOOR_STATE.CLOSED)) {
			batch.draw(closed, getX(), getY());
		}
		
	}
	
	public void setCurrentState(DOOR_STATE currentState) {
		this.currentState = currentState;
	}
	
	public DOOR_STATE getCurrentState() {
		return currentState;
	}
}
