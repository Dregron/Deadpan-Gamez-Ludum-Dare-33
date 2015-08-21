package com.deadpan_gamez_ludum_dare_33.application_resources;

import java.util.ArrayList;
import java.util.List;

import com.deadpan_gamez_ludum_dare_33.DisplayManager;
import com.deadpan_gamez_ludum_dare_33.ScreenIds;

public abstract class ApplicationResource {

	private ScreenIds screenId;
	private DisplayManager displayManager;
	private List<ApplicationTimer> timers = new ArrayList<>();
	
	public ApplicationResource(ScreenIds screenId, DisplayManager displayManager) {
		this.screenId = screenId;
		this.displayManager = displayManager;
	}
	
	public void update(float delta) {
		for (ApplicationTimer timer : timers) {
			timer.tick(delta);
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
}
