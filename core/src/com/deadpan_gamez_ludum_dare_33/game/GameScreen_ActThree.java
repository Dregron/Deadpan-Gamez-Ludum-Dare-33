package com.deadpan_gamez_ludum_dare_33.game;

import static com.deadpan_gamez_ludum_dare_33.Display.IMAGE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.deadpan_gamez_ludum_dare_33.Display;
import com.deadpan_gamez_ludum_dare_33.DisplayManager;
import com.deadpan_gamez_ludum_dare_33.ScreenIds;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationResource;
import com.deadpan_gamez_ludum_dare_33.application_resources.ApplicationTimer;
import com.deadpan_gamez_ludum_dare_33.game.dialog.DialogHandler;
import com.deadpan_gamez_ludum_dare_33.game.enemy.ActThreeEnemy;

public class GameScreen_ActThree extends ApplicationResource implements Screen{

	private final float WORLD_WIDTH = 800;
	private final float WORLD_HEIGHT = 600;
	
	private static final String TILED_MAP_SET = "SpriteSheet";
	private TiledMap tiledMap;
	private OrthoCachedTiledMapRenderer mapRenderer;
	private TiledMapTileLayer backgroundLayer;
	private TiledMapTileLayer backgroundWall;
	private TiledMapTileLayer playerLayer;
	private TiledMapTileLayer enemiesLayer;
	private int[] layers_background = new int[2];
	
	private Stage stage;
	private ShapeRenderer renderer;
	
	private Player player;
	private ApplicationTimer walkTimer;
	private float speed = 32;
	
	private DialogHandler dialogHandler;
	private ApplicationTimer timer;
	
	private boolean played = false;
	
	public GameScreen_ActThree(ScreenIds screenId, DisplayManager displayManager, AssetManager asset) {
		super(screenId, displayManager, asset);
		
		this.stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
		this.renderer = new ShapeRenderer();
		this.tiledMap = asset.get(Display.TILED_MAP_ACT_THREE);
		
		this.mapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
		this.mapRenderer.setBlending(true);
		this.backgroundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Floor");
		this.backgroundWall = (TiledMapTileLayer) tiledMap.getLayers().get("Wall");
		this.playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
		this.enemiesLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Enemies");
		this.layers_background[0] = 0;
		this.layers_background[1] = 1;
		
		List<TextureRegion> idleSprites = new ArrayList<>();
		List<TextureRegion> enemySprites = new ArrayList<>();
		Iterator<TiledMapTile> iterator2 = tiledMap.getTileSets().getTileSet(TILED_MAP_SET).iterator();
		for (Iterator<TiledMapTile> iterator = iterator2; iterator2.hasNext();) {
			TiledMapTile next = iterator.next();
			if ("IdlePlayer".equals(next.getProperties().get("Player"))) {
				idleSprites.add(next.getTextureRegion());
			}
			if ("actThree".equals(next.getProperties().get("Enemy"))) {
				enemySprites.add(next.getTextureRegion());
			}
		}
		
		float playerXPos = 0;
		float playerYPos = 0;
		for (int y = 0; y < backgroundLayer.getHeight(); y++) {
			for (int x = 0; x < backgroundLayer.getWidth(); x++) {
				Cell targetPlayerCell = playerLayer.getCell(x , y);
				if (targetPlayerCell != null && "IdlePlayer".equals(targetPlayerCell.getTile().getProperties().get("Player"))) {
					playerXPos = x * playerLayer.getTileWidth();
					playerYPos = y * playerLayer.getTileHeight();
				}
			}
		}
		
		this.player = new Player(playerXPos, playerYPos, playerLayer.getTileWidth(), playerLayer.getTileHeight(), idleSprites);
		this.stage.addActor(player);
		
		dialogHandler = new DialogHandler(stage, asset);
		
		for (int y = 0; y < backgroundLayer.getHeight(); y++) {
			for (int x = 0; x < backgroundLayer.getWidth(); x++) {
				Cell targetEnemyCell = enemiesLayer.getCell(x , y);
				if (targetEnemyCell != null && "actThree".equals(targetEnemyCell.getTile().getProperties().get("Enemy"))) {
					this.stage.addActor(new ActThreeEnemy( x * enemiesLayer.getTileWidth(), y * enemiesLayer.getTileHeight(), enemySprites, player, backgroundLayer, dialogHandler, this));
				}
			}
		}
		
		this.walkTimer = new ApplicationTimer(0.1f);
		this.addTimer(walkTimer);
		this.timer = new ApplicationTimer(3);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		dialogHandler.update(delta);
		stage.getCamera().update();

		stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
		renderer.setProjectionMatrix(stage.getCamera().combined);
		
		mapRenderer.render(layers_background);
		mapRenderer.setView((OrthographicCamera) stage.getCamera());
		
		stage.act(delta);
		stage.draw();
		
		input(Gdx.input);
		renderDarkness();
		
		if (getEnemies() == 0) {
			timer.tick(delta);
			if (timer.isTimerPassedTarget()) {
				stage.getBatch().begin();
				stage.getBatch().draw(getManager().get(IMAGE, Texture.class), 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
				stage.getBatch().end();
				
				if (!played) {
					getManager().get(Display.SOUND_SCARE, Sound.class).play();
					played = true;
				}
			}
		}
	}
	
	public int getEnemies() {
		int i = 0;
		for (Actor actor : stage.getActors()) {
			if(actor instanceof ActThreeEnemy) {
				i++;
			}
		}
		return i;
	}

	@Override
	public void resize(int width, int height) {
		
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
	
	private void input(Input input) {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			if (walkTimer.isTimerEventReady()) {
				if (player.getX() > 0 && isSpaceAvailable(player.getX() - speed, player.getY()))
					player.moveBy(-(speed), 0);
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			if (walkTimer.isTimerEventReady()) {
				if (player.getX() < (backgroundLayer.getWidth() * backgroundLayer.getTileWidth()) - player.getWidth() && isSpaceAvailable(player.getX() + speed, player.getY()))
					player.moveBy((speed), 0);
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (walkTimer.isTimerEventReady()) {
				if (player.getY() < (backgroundLayer.getHeight() * backgroundLayer.getTileHeight()) - player.getHeight() && isSpaceAvailable(player.getX(), player.getY() + speed))
					player.moveBy(0, (speed));
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
			if (walkTimer.isTimerEventReady()) {
				if (player.getY() > 0 && isSpaceAvailable(player.getX(), player.getY() - speed))
					player.moveBy(0, -(speed));
			}
		}
	}
	
	private boolean isSpaceAvailable(float xPos, float yPos) {
		for (Actor actor : stage.getActors()) {
			if (MathUtils.isEqual(actor.getX(), xPos) && MathUtils.isEqual(actor.getY(), yPos)) {
				return false;
			}
		}
		return true;
	}
	
	private void renderDarkness() {
		Gdx.gl.glEnable(GL30.GL_BLEND);
	    renderer.begin(ShapeType.Filled);
	    
		for (int y = 0; y < backgroundLayer.getHeight(); y++) {
			for (int x = 0; x < backgroundLayer.getWidth(); x++) {
				if (isPlayerTwoTilesAway(y, x)) {
					renderer.setColor(0, 0, 0, .6f);	
				} else if (x * backgroundLayer.getTileWidth() > player.getX()-(3*backgroundLayer.getTileWidth()) 
						&& x * backgroundLayer.getTileWidth() < player.getX()+(3*backgroundLayer.getTileWidth())
						&& y * backgroundLayer.getTileHeight() > player.getY()-(3*backgroundLayer.getTileHeight()) 
						&& y * backgroundLayer.getTileHeight() < player.getY()+(3*backgroundLayer.getTileHeight())) {
					renderer.setColor(0, 0, 0, .8f);	
				}  else 
					renderer.setColor(0, 0, 0, 1f);
				
				renderer.rect(x * backgroundLayer.getTileWidth(), y * backgroundLayer.getTileHeight(), backgroundLayer.getTileWidth(), backgroundLayer.getTileHeight());
			}
		}
		renderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
	}
	
	private boolean isPlayerTwoTilesAway(int y, int x) {
		return x * backgroundLayer.getTileWidth() > player.getX()-(2*backgroundLayer.getTileWidth()) 
				&& x * backgroundLayer.getTileWidth() < player.getX()+(2*backgroundLayer.getTileWidth())
				&& y * backgroundLayer.getTileHeight() > player.getY()-(2*backgroundLayer.getTileHeight()) 
				&& y * backgroundLayer.getTileHeight() < player.getY()+(2*backgroundLayer.getTileHeight());
	}
}
