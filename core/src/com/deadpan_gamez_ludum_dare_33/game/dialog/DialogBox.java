package com.deadpan_gamez_ludum_dare_33.game.dialog;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.deadpan_gamez_ludum_dare_33.Display;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationTimer;

public class DialogBox extends Actor{

	private String message;
	private String currentMsg;
	private int i = 0;
	private BitmapFont bitmapFont;
	private GlyphLayout layout;
	private boolean destory = false;
	private Actor actor;
	private float offSetXPos, offSetYPos;
	

	
	private ApplicationTimer applicationTimer;
	private Music typing;
	
	public DialogBox(ApplicationResource applicationResource, Actor player, float width, float height, String message, BitmapFont bitmapFont) {
		this.actor = player;
		setWidth(width);
		setHeight(height);
		this.message = message;
		this.currentMsg = "";
		this.bitmapFont = bitmapFont;
		this.layout = new GlyphLayout(bitmapFont, message);
		this.applicationTimer = new ApplicationTimer(.1f);
		applicationResource.addTimer(applicationTimer);
		this.typing = applicationResource.getManager().get(Display.SOUND_TYPING, Music.class);
	}
	
	public DialogBox(ApplicationResource applicationResource, Actor player, float offSetXPos, float offSetYPos, float width, float height, String message, BitmapFont bitmapFont) {
		this.actor = player;
		this.offSetXPos = offSetXPos;
		this.offSetYPos = offSetYPos;
		setWidth(width);
		setHeight(height);
		this.message = message;
		this.currentMsg = "";
		this.bitmapFont = bitmapFont;
		this.layout = new GlyphLayout(bitmapFont, message);
		this.applicationTimer = new ApplicationTimer(.1f);
		applicationResource.addTimer(applicationTimer);
		this.typing = applicationResource.getManager().get(Display.SOUND_TYPING, Music.class);
	}
	
	public void draw(ShapeRenderer shapes) {

//		shapes.setColor(Color.DARK_GRAY);
//		shapes.rect(actor.getX(), actor.getY()+actor.getHeight(), getWidth(), getHeight());
//		
//		shapes.setColor(Color.GRAY);
//		shapes.rect(actor.getX(), actor.getY()+actor.getHeight(), getWidth(), 5);
//		shapes.rect(actor.getX(), actor.getY()+actor.getHeight()+getHeight(), getWidth(), 5);
//		shapes.rect(actor.getX(), actor.getY()+actor.getHeight(), 5, getHeight());
//		shapes.rect(actor.getX()+getWidth(), actor.getY()+actor.getHeight(), 5, getHeight()+5);
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
	
		bitmapFont.setColor(Color.WHITE);
		layout.setText(bitmapFont, currentMsg);
		bitmapFont.draw(batch, currentMsg, actor.getX()+ 5+offSetXPos, offSetYPos+actor.getY()+actor.getHeight() + (getHeight()/1.5f), getWidth()-5, Align.center, true);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (i < message.length() && applicationTimer.isTimerEventReady()) {
			if (!typing.isPlaying()) {
				typing.play();
			}
			currentMsg += message.charAt(i);
			i++;
		} else if (currentMsg.length() == message.length() && !destory){
			applicationTimer.setTargetTime(5f);
			applicationTimer.resetTick();
			destory = true;
			if (typing.isPlaying()) {
				typing.stop();
			}
		}
	}

	public boolean isDestroyed() {
		return currentMsg.length() == message.length() && applicationTimer.isTimerEventReady() && destory;
//		return false;
	}

	public void dispose() {
		applicationTimer.setRemove(true);
	}
	
	
	
}
