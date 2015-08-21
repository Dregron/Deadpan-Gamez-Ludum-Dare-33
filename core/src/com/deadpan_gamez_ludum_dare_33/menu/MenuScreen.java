package com.deadpan_gamez_ludum_dare_33.menu;

import com.badlogic.gdx.Screen;
import com.deadpan_gamez_ludum_dare_33.DisplayManager;
import com.deadpan_gamez_ludum_dare_33.ScreenIds;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;

public class MenuScreen extends ApplicationResource implements Screen {

	public MenuScreen(ScreenIds screenId, DisplayManager displayManager) {
		super(screenId, displayManager);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
//		System.err.println(getScreenId().name());
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
}
