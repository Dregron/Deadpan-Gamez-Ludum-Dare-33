package com.deadpan_gamez_ludum_dare_33.game.player_bullet;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deadpan_gamez_ludum_dare_33.game.GameScreen;
import com.deadpan_gamez_ludum_dare_33.game.Player;
import com.deadpan_gamez_ludum_dare_33.game.pixels.PixelHandler;

public class PlayerBulletHandler {

	private Stage stage;
	private PixelHandler handler;
	private List<PlayerBullet> bullets = new ArrayList<>();
	
	public PlayerBulletHandler(Stage stage, PixelHandler handler) {
		this.stage = stage;
		this.handler = handler;
	}
	
	public void draw(ShapeRenderer renderer) {
		for (PlayerBullet bullet : bullets) {
			bullet.draw(renderer);
		}
	}
	
	public void update() {
		for (int i = 0; i < bullets.size(); i++) {
			if (bullets.get(i).destroy()) {
				GameScreen.STAGE_ONE = true;
				handler.addBloodPixel(bullets.get(i).getX(), bullets.get(i).getY());
				bullets.remove(i);
			}
		}
	}
	
	public void addBullet(Player player) {
		PlayerBullet playerBullet = new PlayerBullet(player.getX() + (player.getWidth()/2), player.getY());
		bullets.add(playerBullet);
		stage.addActor(playerBullet);
	}
}
