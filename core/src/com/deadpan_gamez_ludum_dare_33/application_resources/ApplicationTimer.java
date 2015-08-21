package com.deadpan_gamez_ludum_dare_33.application_resources;

public class ApplicationTimer {

	private float tickTimer, targetTime;
	private long startTime, endTime;
	
	public ApplicationTimer(long targetTime) {
		this.targetTime = targetTime;
		this.tickTimer = 0;
		this.startTime = System.currentTimeMillis();
	}
	
	protected void tick(float delta) {
		tickTimer = tickTimer + (1 * delta);
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
	
	public void resetTick() {
		endTime = ((System.currentTimeMillis() - startTime) / 1000) % 60;
		System.err.println(endTime);
		tickTimer = 0;
	}
}
