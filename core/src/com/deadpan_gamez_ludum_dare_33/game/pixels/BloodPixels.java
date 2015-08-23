package com.deadpan_gamez_ludum_dare_33.game.pixels;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class BloodPixels implements Pixel{

	private float[] xPos, yPos, xPosVol, yPosVol, angle;
	
	public BloodPixels(float xPos, float yPos) {
		
		int i = 50;
		this.xPos = new float[i];
		this.yPos = new float[i];
		this.xPosVol = new float[i];
		this.yPosVol = new float[i];
		this.angle = new float[i];
		
		for (int j = 0; j < i; j++) {
			
			this.xPos[j] = xPos;
			this.yPos[j] = yPos;
			
			this.xPosVol[j] = MathUtils.random() * .5f;
			this.yPosVol[j] = MathUtils.random() * .5f;
			
			Random r = new Random();
			angle[j] = r.nextInt(6) + 1;
		}
	}
	
	@Override
	public void draw(ShapeRenderer renderer) {
		
		renderer.setColor(Color.RED);
		for (int i = 0; i < angle.length; i++) {
			
			renderer.rect(xPos[i], yPos[i], 3, 3);
		}
	}

	@Override
	public void update(float delta) {
		
		for (int i = 0; i < this.xPos.length; i++) {
			
			xPos[i] += xPosVol[i] * MathUtils.cos(angle[i]);
			yPos[i] += yPosVol[i] * MathUtils.sin(angle[i]);
		}
	}

}
