package com.deadpan_gamez_ludum_dare_33;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Logger;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.game.GameScreen;
import com.deadpan_gamez_ludum_dare_33.menu.MenuScreen;

public class Display extends Game implements DisplayManager {
	
	private long diff, start = System.currentTimeMillis();
	
	private List<ApplicationResource> screens = new ArrayList<ApplicationResource>();
	
	private AssetManager manager;
	private FPSLogger fpsLogger;
	
	public void sleep(int fps) {
	    if(fps>0){
	      diff = System.currentTimeMillis() - start;
	      long targetDelay = 1000/fps;
	      if (diff < targetDelay) {
	        try{
	            Thread.sleep(targetDelay - diff);
	          } catch (InterruptedException e) {}
	        }   
	      start = System.currentTimeMillis();
	    }
	}
	
	@Override
	public void create () {
		
		Gdx.app.setLogLevel(Logger.DEBUG);
		
		this.screens.add(new GameScreen(ScreenIds.GAME, this));
		this.screens.add(new MenuScreen(ScreenIds.MENU, this));
		this.setScreenWithId(ScreenIds.GAME);
		
		this.manager = new AssetManager();
		this.manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		
		this.fpsLogger = new FPSLogger();
	}

	@Override
	public void render () {
		
		fpsLogger.log();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (getScreen() instanceof ApplicationResource) {
			((ApplicationResource) getScreen()).update(Gdx.graphics.getDeltaTime());
		}
		super.render();
		
		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			sleep(30);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	@Override
	public void setScreenWithId(ScreenIds screenId) {
		
		Screen screenChoice = null;
		for (ApplicationResource applicationResource : screens) {
			if (applicationResource.getScreenId().equals(screenId)) {
				if (applicationResource instanceof Screen) {
					Gdx.app.debug(GameLogger.DEBUG, "State Changed to: " + applicationResource.getScreenId().name());
					screenChoice = (Screen) applicationResource;
				} else {
					throw new RuntimeException("Application Resource is not isntance of screen!");
				}
			}
		}
		setScreen(screenChoice);
	}
}
