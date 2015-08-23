package com.deadpan_gamez_ludum_dare_33.game.enemy;

import java.util.List;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.deadpan_gamez_ludum_dare_33.Display;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationTimer;

public class ActOneEnemy extends Actor {

	public enum ANIMATION_STATE {IDLE, WALKING, PREPARE_ATTACK, ATTACKING};
	
	private Animation enemyIdleAnimation;
	private Animation walkingAnimation;
	private Animation preparingAnimation;
	private TextureRegion currentFrame;
	private ANIMATION_STATE currentState;
	private ApplicationTimer enemyWalkingTimer;
	
	private float stateTime; 
	private boolean prepared = false;
	private Sound blade;
	
	public ActOneEnemy(ApplicationResource applicationResource, float xPos, float yPos, float width, float height
			, List<TextureRegion> enemySprites, List<TextureRegion> walkingAnimation, List<TextureRegion> prepareAnimation) {
		setX(xPos);
		setY(yPos);
		setWidth(width);
		setHeight(height);
		
		TextureRegion[] idle = new TextureRegion[enemySprites.size()];
		for (int i = 0; i < idle.length; i++) {
			idle[i] = enemySprites.get(i);
		}
		
		TextureRegion[] walking = new TextureRegion[walkingAnimation.size()];
		for (int i = 0; i < walking.length; i++) {
			walking[i] = walkingAnimation.get(i);
		}
		
		TextureRegion[] preparing = new TextureRegion[prepareAnimation.size()];
		for (int i = 0; i < preparing.length; i++) {
			preparing[i] = prepareAnimation.get(i);
		}
		
		this.currentState = ANIMATION_STATE.IDLE;
		this.enemyIdleAnimation = new Animation(.2f, idle);
		this.enemyIdleAnimation.setPlayMode(PlayMode.LOOP);
		
		this.walkingAnimation = new Animation(.5f, walking);
		this.walkingAnimation.setPlayMode(PlayMode.LOOP);
		
		this.preparingAnimation = new Animation(2f, preparing);
		this.preparingAnimation.setPlayMode(PlayMode.NORMAL);
		
		enemyWalkingTimer = new ApplicationTimer(.5f);
		applicationResource.addTimer(enemyWalkingTimer);
		
		blade = applicationResource.getManager().get(Display.SOUND_BLADE, Sound.class);
	}
	
	private boolean isSpaceAvailable(float xPos, float yPos) {
		for (Actor actor : getStage().getActors()) {
			if (MathUtils.isEqual(actor.getX(), xPos) && MathUtils.isEqual(actor.getY(), yPos)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void act(float delta) {
		stateTime += delta;           
		
		if (currentState.equals(ANIMATION_STATE.IDLE)) {
			currentFrame = enemyIdleAnimation.getKeyFrame(stateTime, true);
		} else if (currentState.equals(ANIMATION_STATE.WALKING)) {
			currentFrame = walkingAnimation.getKeyFrame(stateTime, true);
		} else if (currentState.equals(ANIMATION_STATE.PREPARE_ATTACK)) {
			currentFrame = preparingAnimation.getKeyFrame(stateTime, true);
		}
		
		if (getCurrentState().equals(ANIMATION_STATE.WALKING) && isSpaceAvailable(getX(), getY() + 16) && enemyWalkingTimer.isTimerEventReady()) {
			moveBy(0, 4);
		} else if (!prepared && !getCurrentState().equals(ANIMATION_STATE.PREPARE_ATTACK) && !isSpaceAvailable(getX(), getY() + 16) && enemyWalkingTimer.isTimerEventReady()) {
			setCurrentState(ANIMATION_STATE.PREPARE_ATTACK);
			prepared = true;
			blade.play(.5f);
		} else if (prepared && !getCurrentState().equals(ANIMATION_STATE.ATTACKING) && preparingAnimation.isAnimationFinished(stateTime)) {
			setCurrentState(ANIMATION_STATE.ATTACKING);
		}
	}
	
	public void setCurrentState(ANIMATION_STATE currentState) {
		stateTime = 0;
		this.currentState = currentState;
	}
	
	public ANIMATION_STATE getCurrentState() {
		return currentState;
	}
}
