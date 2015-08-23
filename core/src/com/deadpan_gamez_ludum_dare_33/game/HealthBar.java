package com.deadpan_gamez_ludum_dare_33.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HealthBar {

	private float maxWidth = 200;
	private float maxHeight = 50;
	private float maxHealth;
	
	public HealthBar(float maxHealth, float maxWidth) {
		this.maxHealth = maxHealth;
		this.maxWidth = maxWidth;
	}
	
	public HealthBar(float maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public void draw(ShapeRenderer renderer, float xPos, float yPos, float health, Color colour) {
		renderer.setColor(colour);
		renderer.rect(xPos, yPos, (health*getMaxWidth())/maxHealth, getMaxHeight());
	}

	public float getMaxWidth() {
		return maxWidth;
	}

	public float getMaxHeight() {
		return maxHeight;
	}
}
