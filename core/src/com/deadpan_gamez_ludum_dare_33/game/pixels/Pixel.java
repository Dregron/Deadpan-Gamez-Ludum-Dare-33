package com.deadpan_gamez_ludum_dare_33.game.pixels;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Pixel {

	public void draw(ShapeRenderer renderer);
	public void update(float delta);
}
