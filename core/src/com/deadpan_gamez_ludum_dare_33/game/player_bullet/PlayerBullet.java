package com.deadpan_gamez_ludum_dare_33.game.player_bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.deadpan_gamez_ludum_dare_33.game.enemy.ActOneEnemy;

public class PlayerBullet extends Actor {

	private Rectangle rectangle = new Rectangle();
	private boolean destroy = false;
	
	public PlayerBullet(float xPos, float yPos) {
		setX(xPos);
		setY(yPos);
		setWidth(5);
		setHeight(5);
	}
	
	public void draw(ShapeRenderer renderer) {
		
		renderer.setColor(Color.RED);
		renderer.rect(getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void act(float delta) {

		setY(getY() - (32 * delta));
		rectangle.set(getX(), getY(), getWidth(), getHeight());
		hit();
	}
	
	public boolean hit() {
		for (Actor actor : getStage().getActors()) {
			if (actor instanceof ActOneEnemy) {
				Rectangle targetRect = new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
				if (targetRect.overlaps(rectangle)) {
					destroy =true;
					remove();
					return true;
				}
			}
		}
		return false;
	}

	public boolean destroy() {
		return destroy;
	}
	
}
