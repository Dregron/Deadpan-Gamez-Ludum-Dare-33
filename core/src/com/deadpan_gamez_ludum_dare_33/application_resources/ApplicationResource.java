package com.deadpan_gamez_ludum_dare_33.application_resources;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.deadpan_gamez_ludum_dare_33.DisplayManager;
import com.deadpan_gamez_ludum_dare_33.ScreenIds;

public abstract class ApplicationResource {

	private ScreenIds screenId;
	private DisplayManager displayManager;
	private List<ApplicationTimer> timers = new ArrayList<>();
	private AssetManager manager;
	
	public ApplicationResource(ScreenIds screenId, DisplayManager displayManager, AssetManager asset) {
		this.screenId = screenId;
		this.displayManager = displayManager;
		this.manager = asset;
	}
	
	public void update(float delta) {
		for (int i = 0; i < timers.size(); i++) {
			if (timers.get(i).tick(delta)) {
				timers.remove(i);
			}
		}
	}
	
	public void setDisplayManager(DisplayManager displayManager) {
		this.displayManager = displayManager;
	}
	
	public void setScreenId(ScreenIds screenId) {
		this.screenId = screenId;
	}
	
	public ScreenIds getScreenId() {
		return screenId;
	}
	
	public void setScreen(ScreenIds screenId) {
		displayManager.setScreenWithId(screenId);
	}
	
	public void addTimer(ApplicationTimer applicationTimer) {
		timers.add(applicationTimer);
	}
	
	public AssetManager getManager() {
		return manager;
	}
}
