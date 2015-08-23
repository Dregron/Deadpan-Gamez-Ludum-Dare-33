package com.deadpan_gamez_ludum_dare_33.game.pixels;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PixelHandler {

	private List<Pixel> pixels = new ArrayList<Pixel>();
	
	public void addBloodPixel(float xPos, float yPos) {
		BloodPixels bloodPixels = new BloodPixels(xPos, yPos);
		pixels.add(bloodPixels);
	}
	
	public void draw(ShapeRenderer renderer) {
		for (Pixel pixel : pixels) {
			pixel.draw(renderer);
		}
	}
	
	public void update(float delta) {
		for (Pixel pixel : pixels) {
			pixel.update(delta);
		}
	}
	
	
	
}
