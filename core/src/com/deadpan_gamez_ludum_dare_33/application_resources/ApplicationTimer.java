package com.deadpan_gamez_ludum_dare_33.application_resources;

public class ApplicationTimer {

	private float tickTimer, targetTime;
	private boolean remove = false;
	
	public ApplicationTimer(float targetTime) {
		this.targetTime = targetTime;
		this.tickTimer = 0;
	}
	
	public boolean tick(float delta) {
		tickTimer = tickTimer + (1 * delta);
		return remove;
	}
	
	public boolean isTimerEventReady() {
		if ((targetTime - tickTimer) <= 0) {
			resetTick();
			return true;
		}
		return false;
	}
	
	public boolean isTimerPassedTarget() {
		if ((targetTime - tickTimer) <= 0) {
			return true;
		}
		return false;
	}
	
	public void setTargetTime(float targetTime) {
		this.targetTime = targetTime;
	}
	
	public void resetTick() {
		tickTimer = 0;
	}
	
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
}
