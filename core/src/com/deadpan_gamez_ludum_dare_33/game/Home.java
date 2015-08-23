package com.deadpan_gamez_ludum_dare_33.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Home extends Actor {
	
	private OrthoCachedTiledMapRenderer mapRenderer;
	private TiledMapTileLayer  tiledMapTileLayer;
	private OrthographicCamera camera;
	
	private int[] layers = new int[1];
	
	public Home(AssetManager assetManager, Camera camera, TiledMap tiledMap) {
		
		this.mapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
		this.mapRenderer.setBlending(true);
		this.tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Background_Floor");
		this.layers[0] = tiledMapTileLayer.getObjects().getCount();
		this.camera = (OrthographicCamera) camera;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		mapRenderer.render(layers);
	}
	
	@Override
	public void act(float delta) {
		mapRenderer.setView(camera);
	}
}
