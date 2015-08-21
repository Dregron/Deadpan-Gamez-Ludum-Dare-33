package com.deadpan_gamez_ludum_dare_33.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deadpan_gamez_ludum_dare_33.DisplayManager;
import com.deadpan_gamez_ludum_dare_33.ScreenIds;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationTimer;

public class GameScreen extends ApplicationResource implements Screen {

	private Stage stage;
	private ApplicationTimer changeStateTimer;
	
	public GameScreen(ScreenIds screenId, DisplayManager displayManager) {
		super(screenId, displayManager);
		
		this.stage = new Stage();
		changeStateTimer = new ApplicationTimer(10);
		addTimer(changeStateTimer);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		if (changeStateTimer.isTimerEventReady()) {
			setScreen(ScreenIds.MENU);
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
