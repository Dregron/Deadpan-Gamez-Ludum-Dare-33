package com.deadpan_gamez_ludum_dare_33.game;

import static com.deadpan_gamez_ludum_dare_33.Display.TILED_MAP_ACT_ONE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import com.deadpan_gamez_ludum_dare_33.game.dialog.ActOneTriggers;
import com.deadpan_gamez_ludum_dare_33.game.dialog.DialogHandler;
import com.deadpan_gamez_ludum_dare_33.game.enemy.ActOneEnemy;
import com.deadpan_gamez_ludum_dare_33.game.enemy.ActOneEnemy.ANIMATION_STATE;
import com.deadpan_gamez_ludum_dare_33.game.environment.Wall;
import com.deadpan_gamez_ludum_dare_33.game.pixels.PixelHandler;
import com.deadpan_gamez_ludum_dare_33.game.player_bullet.PlayerBulletHandler;

public class GameScreen extends ApplicationResource implements Screen {

	public static boolean STAGE_ONE = false;
	private static final String TILED_MAP_SET = "SpriteSheet";
	private final float WORLD_WIDTH = 800;
	private final float WORLD_HEIGHT = 600;
	
	private OrthoCachedTiledMapRenderer mapRenderer;
	private TiledMapTileLayer backgroundLayer;
	private TiledMapTileLayer backgroundWall;
	private TiledMapTileLayer playerLayer;
	private TiledMapTileLayer enemiesLayer;
	private int[] layers_background = new int[2];
	
	private Stage stage;
	private DialogHandler dialogHandler;
	private PlayerBulletHandler playerBulletHandler;
	private PixelHandler pixelHandler;
	private ActOneTriggers actOneTriggers;
	private ShapeRenderer renderer;
	private TiledMap tiledMap;
	private Player player;
	
	private ActOneEnemy actOneEnemy;
	
	private float speed = 32;
	private ApplicationTimer walkTimer;
	
	private GlyphLayout layout = new GlyphLayout();
	private BitmapFont bitmapFont;
	private String act_one = "Act One Complete!";
	private ApplicationTimer endLevelTimer;
	private Music actOneSound;
	private boolean shoot = false;
	
	public GameScreen(ScreenIds screenId, DisplayManager displayManager, AssetManager manager) {
		super(screenId, displayManager, manager);
		
		this.stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
		this.renderer = new ShapeRenderer();
		this.tiledMap = manager.get(TILED_MAP_ACT_ONE);
		this.dialogHandler = new DialogHandler(stage, manager);
		this.pixelHandler = new PixelHandler();
		this.playerBulletHandler = new PlayerBulletHandler(stage, pixelHandler);
		
		this.mapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
		this.mapRenderer.setBlending(true);
		this.backgroundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Floor");
		this.backgroundWall = (TiledMapTileLayer) tiledMap.getLayers().get("Wall");
		this.playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
		this.enemiesLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Enemies");
		this.layers_background[0] = 0;
		this.layers_background[1] = 1;
		this.bitmapFont = manager.get(Display.BITMAP_FONT_DIALOG_FNT, BitmapFont.class);
		this.actOneSound = manager.get(Display.SOUND_ACT_ONE, Music.class);
		
		List<TextureRegion> idleSprites = new ArrayList<>();
		List<TextureRegion> actOneEnemySprites = new ArrayList<>();
		List<TextureRegion> actOneEnemyWalkingSprites = new ArrayList<>();
		List<TextureRegion> actOneEnemyPreparingSprites = new ArrayList<>();
		Iterator<TiledMapTile> iterator2 = tiledMap.getTileSets().getTileSet(TILED_MAP_SET).iterator();
		for (Iterator<TiledMapTile> iterator = iterator2; iterator2.hasNext();) {
			TiledMapTile next = iterator.next();
			if ("IdlePlayer".equals(next.getProperties().get("Player"))) {
				idleSprites.add(next.getTextureRegion());
			}
			if ("ActOne".equals(next.getProperties().get("Enemy"))) {
				actOneEnemySprites.add(next.getTextureRegion());
			}
			if ("walking".equals(next.getProperties().get("Enemy"))) {
				actOneEnemyWalkingSprites.add(next.getTextureRegion());
			}
			if ("prepare".equals(next.getProperties().get("Enemy"))) {
				actOneEnemyPreparingSprites.add(next.getTextureRegion());
			}
		}
		float playerXPos = 0;
		float playerYPos = 0;
		for (int y = 0; y < backgroundLayer.getHeight(); y++) {
			for (int x = 0; x < backgroundLayer.getWidth(); x++) {
				Cell targetCell = playerLayer.getCell(x , y);
				if (targetCell != null && "IdlePlayer".equals(targetCell.getTile().getProperties().get("Player"))) {
					playerXPos = x * playerLayer.getTileWidth();
					playerYPos = y * playerLayer.getTileHeight();
				}
			}
		}
		
		float actOneXPos = 0;
		float actOneYPos = 0;
		for (int y = 0; y < backgroundLayer.getHeight(); y++) {
			for (int x = 0; x < backgroundLayer.getWidth(); x++) {
				Cell targetCell = enemiesLayer.getCell(x , y);
				if (targetCell != null && "ActOne".equals(targetCell.getTile().getProperties().get("Enemy"))) {
					actOneXPos = x * enemiesLayer.getTileWidth();
					actOneYPos = y * enemiesLayer.getTileHeight();
				}
			}
		}
		
		for (int y = 0; y < backgroundLayer.getHeight(); y++) {
			for (int x = 0; x < backgroundLayer.getWidth(); x++) {
				Cell targetCell = backgroundWall.getCell(x , y);
				if (targetCell != null && "wall".equals(targetCell.getTile().getProperties().get("Wall"))) {
					stage.addActor(new Wall(x * backgroundWall.getTileWidth(), y * backgroundWall.getTileHeight()));
				}
			}
		}
		
		this.player = new Player(playerXPos, playerYPos, playerLayer.getTileWidth(), playerLayer.getTileHeight(), idleSprites);
		this.actOneEnemy = new ActOneEnemy(this, actOneXPos, actOneYPos, enemiesLayer.getTileWidth(), enemiesLayer.getTileHeight(), actOneEnemySprites, actOneEnemyWalkingSprites, actOneEnemyPreparingSprites);
		this.stage.addActor(player);
		this.stage.addActor(actOneEnemy);
		walkTimer = new ApplicationTimer(0.1f);
		endLevelTimer = new ApplicationTimer(5f);
		addTimer(walkTimer);
		
		actOneTriggers = new ActOneTriggers(this, dialogHandler, player, actOneEnemy, manager.get(Display.BITMAP_FONT_DIALOG_FNT, BitmapFont.class));
		actOneSound.play();
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		stage.getCamera().update();

		renderer.setProjectionMatrix(stage.getCamera().combined);
		stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
		
		mapRenderer.render(layers_background);
		mapRenderer.setView((OrthographicCamera) stage.getCamera());
		
		actOneTriggers.update();
		dialogHandler.update(delta);
		pixelHandler.update(delta);
		playerBulletHandler.update();
		
		Gdx.gl.glEnable(GL30.GL_BLEND);
	    renderer.begin(ShapeType.Filled);
	    
	    dialogHandler.draw(renderer);
	    playerBulletHandler.draw(renderer);
	    pixelHandler.draw(renderer);
	    
	    renderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
		
		stage.act(delta);
		stage.draw();
		
		input();
		renderDarkness();
		
		if (STAGE_ONE) {
			stage.getBatch().begin();
			layout.setText(bitmapFont, act_one);
			bitmapFont.setColor(Color.WHITE);
			bitmapFont.draw(stage.getBatch(), act_one, (WORLD_WIDTH/2) - layout.width/2, (WORLD_HEIGHT/2) - layout.width/2);
			stage.getBatch().end();
			
			endLevelTimer.tick(Gdx.graphics.getDeltaTime());
			
			if (actOneSound.isPlaying()) {
				actOneSound.stop();
			}
			
			if (endLevelTimer.isTimerEventReady()) {
				super.setScreen(ScreenIds.GAME_ACT_TWO);
			}
		}
	}

	private void input() {
//		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
//			((OrthographicCamera) stage.getCamera()).zoom -= 0.01;
//			Gdx.app.debug(GameLogger.DEBUG, "Zooming Out: " + ((OrthographicCamera) stage.getCamera()).zoom);
//		} else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
//			((OrthographicCamera) stage.getCamera()).zoom += 0.01;
//			Gdx.app.debug(GameLogger.DEBUG, "Zooming In: " + ((OrthographicCamera) stage.getCamera()).zoom);
//		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//			if (walkTimer.isTimerEventReady()) {
//				if (player.getX() > 0 && isSpaceAvailable(player.getX() - speed, player.getY()))
//					player.moveBy(-(speed), 0);
//			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//			if (walkTimer.isTimerEventReady()) {
//				if (player.getX() < (backgroundLayer.getWidth() * backgroundLayer.getTileWidth()) - player.getWidth() && isSpaceAvailable(player.getX() + speed, player.getY()))
//					player.moveBy((speed), 0);
//			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (walkTimer.isTimerEventReady()) {
				if (player.getY() < (backgroundLayer.getHeight() * backgroundLayer.getTileHeight()) - player.getHeight() && isSpaceAvailable(player.getX(), player.getY() + speed))
					player.moveBy(0, (speed));
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
//			if (walkTimer.isTimerEventReady()) {
//				if (player.getY() > 0 && isSpaceAvailable(player.getX(), player.getY() - speed))
//					player.moveBy(0, -(speed));
//			}
		}
		
		if (!shoot && Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && (actOneEnemy.getCurrentState().equals(ANIMATION_STATE.PREPARE_ATTACK) || actOneEnemy.getCurrentState().equals(ANIMATION_STATE.ATTACKING))) {
			playerBulletHandler.addBullet(player);
			shoot = true;
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
				if (x * backgroundLayer.getTileWidth() > player.getX()-(2*backgroundLayer.getTileWidth()) 
						&& x * backgroundLayer.getTileWidth() < player.getX()+(2*backgroundLayer.getTileWidth())
						&& y * backgroundLayer.getTileHeight() > player.getY()-(2*backgroundLayer.getTileHeight()) 
						&& y * backgroundLayer.getTileHeight() < player.getY()+(2*backgroundLayer.getTileHeight())) {
					renderer.setColor(0, 0, 0, .2f);	
				} else if (x * backgroundLayer.getTileWidth() > player.getX()-(3*backgroundLayer.getTileWidth()) 
						&& x * backgroundLayer.getTileWidth() < player.getX()+(3*backgroundLayer.getTileWidth())
						&& y * backgroundLayer.getTileHeight() > player.getY()-(3*backgroundLayer.getTileHeight()) 
						&& y * backgroundLayer.getTileHeight() < player.getY()+(3*backgroundLayer.getTileHeight())) {
					renderer.setColor(0, 0, 0, .4f);	
				} else if (x * backgroundLayer.getTileWidth() > player.getX()-(4*backgroundLayer.getTileWidth()) 
						&& x * backgroundLayer.getTileWidth() < player.getX()+(4*backgroundLayer.getTileWidth())
						&& y * backgroundLayer.getTileHeight() > player.getY()-(4*backgroundLayer.getTileHeight()) 
						&& y * backgroundLayer.getTileHeight() < player.getY()+(4*backgroundLayer.getTileHeight())) {
					renderer.setColor(0, 0, 0, .6f);	
				} else if (x * backgroundLayer.getTileWidth() > player.getX()-(5*backgroundLayer.getTileWidth()) 
						&& x * backgroundLayer.getTileWidth() < player.getX()+(5*backgroundLayer.getTileWidth())
						&& y * backgroundLayer.getTileHeight() > player.getY()-(5*backgroundLayer.getTileHeight()) 
						&& y * backgroundLayer.getTileHeight() < player.getY()+(5*backgroundLayer.getTileHeight())) {
					renderer.setColor(0, 0, 0, .8f);	
				} else if (x * backgroundLayer.getTileWidth() > player.getX()-(6*backgroundLayer.getTileWidth()) 
						&& x * backgroundLayer.getTileWidth() < player.getX()+(6*backgroundLayer.getTileWidth())
						&& y * backgroundLayer.getTileHeight() > player.getY()-(6*backgroundLayer.getTileHeight()) 
						&& y * backgroundLayer.getTileHeight() < player.getY()+(6*backgroundLayer.getTileHeight())) {
					renderer.setColor(0, 0, 0, .9f);	
				} else 
					renderer.setColor(0, 0, 0, 1f);
				renderer.rect(x * backgroundLayer.getTileWidth(), y * backgroundLayer.getTileHeight(), backgroundLayer.getTileWidth(), backgroundLayer.getTileHeight());
			}
		}
		renderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		stage.getCamera().position.set(stage.getViewport().getWorldWidth()/2, stage.getViewport().getWorldHeight()/2, 0);
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
		stage.dispose();
	}
}
