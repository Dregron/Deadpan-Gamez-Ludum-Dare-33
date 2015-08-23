package com.deadpan_gamez_ludum_dare_33;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Logger;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.game.GameScreen;
import com.deadpan_gamez_ludum_dare_33.game.GameScreen_ActThree;
import com.deadpan_gamez_ludum_dare_33.game.GameScreen_ActTwo;
import com.deadpan_gamez_ludum_dare_33.menu.MenuScreen;

public class Display extends Game implements DisplayManager {
	
	public static final String TILED_MAP_ACT_ONE = "ActOne.tmx";
	public static final String TILED_MAP_ACT_TWO = "ActTwo.tmx";
	public static final String TILED_MAP_ACT_THREE = "ActThree.tmx";
	public static final String BITMAP_FONT_DIALOG_FNT = "dialogFont.fnt";
	public static final String BITMAP_FONT_DIALOG_PNG = "dialogFont_0.png";
	public static final String IMAGE = "scarePicture.jpg";
	public static final String SOUND_TYPING = "typingSound.wav";
	public static final String SOUND_BLADE = "bladeSound.wav";
	public static final String SOUND_ACT_ONE = "gameSound.wav";
	public static final String SOUND_DOOR = "doorOpening.wav";
	public static final String SOUND_ENEMY_TAKE = "enemySpace.wav";
	public static final String SOUND_SCARE = "scare.wav";
	
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
		
		this.manager = new AssetManager();
		this.manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		this.manager.load(TILED_MAP_ACT_ONE, TiledMap.class);
		this.manager.load(TILED_MAP_ACT_TWO, TiledMap.class);
		this.manager.load(TILED_MAP_ACT_THREE, TiledMap.class);
		this.manager.load(BITMAP_FONT_DIALOG_FNT, BitmapFont.class);
		this.manager.load(BITMAP_FONT_DIALOG_PNG, Texture.class);
		this.manager.load(IMAGE, Texture.class);
		this.manager.load(SOUND_TYPING, Music.class);
		this.manager.load(SOUND_BLADE, Sound.class);
		this.manager.load(SOUND_DOOR, Sound.class);
		this.manager.load(SOUND_ACT_ONE, Music.class);
		this.manager.load(SOUND_ENEMY_TAKE, Sound.class);
		this.manager.load(SOUND_SCARE, Sound.class);
		this.manager.finishLoading();
		
		this.screens.add(new GameScreen(ScreenIds.GAME, this, manager));
		this.screens.add(new MenuScreen(ScreenIds.MENU, this, manager));
		this.screens.add(new GameScreen_ActTwo(ScreenIds.GAME_ACT_TWO, this, manager));
		this.screens.add(new GameScreen_ActThree(ScreenIds.GAME_ACT_THREE, this, manager));
		this.setScreenWithId(ScreenIds.GAME);
		
		
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
	public void dispose() {
		super.dispose();
		manager.dispose();
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
