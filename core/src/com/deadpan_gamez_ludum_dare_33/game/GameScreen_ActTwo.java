package com.deadpan_gamez_ludum_dare_33.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
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
import com.deadpan_gamez_ludum_dare_33.game.dialog.ActTwoTriggers;
import com.deadpan_gamez_ludum_dare_33.game.dialog.DialogHandler;
import com.deadpan_gamez_ludum_dare_33.game.environment.Door1Point0;
import com.deadpan_gamez_ludum_dare_33.game.environment.Door1Point0.DOOR_STATE;
import com.deadpan_gamez_ludum_dare_33.game.environment.Knife;
import com.deadpan_gamez_ludum_dare_33.game.environment.Wall;

public class GameScreen_ActTwo extends ApplicationResource implements Screen {

	private static boolean STAGE_TWO = false;
	private static final String TILED_MAP_SET = "SpriteSheet";
	private TiledMap tiledMap;
	private OrthoCachedTiledMapRenderer mapRenderer;
	private TiledMapTileLayer backgroundLayer;
	private TiledMapTileLayer backgroundWall;
	private TiledMapTileLayer playerLayer;
	private TiledMapTileLayer enemiesLayer;
	private int[] layers_background = new int[3];
	
	private final float WORLD_WIDTH = 800;
	private final float WORLD_HEIGHT = 600;
	
	private Stage stage;
	private ShapeRenderer renderer;
	
	private DialogHandler dialogHandler;
	private ActTwoTriggers actTwoTriggers;
	private Player player;
	private Knife knife;
	private Door1Point0 door2Point0;
	
	private Sound doorSound;
	private ApplicationTimer walkTimer;
	private float speed = 32;
	
	private GlyphLayout layout = new GlyphLayout();
	private BitmapFont bitmapFont;
	private String act_two = "Act Two Complete!";
	private ApplicationTimer endLevelTimer;
	
	public GameScreen_ActTwo(ScreenIds screenId, DisplayManager displayManager, AssetManager asset) {
		super(screenId, displayManager, asset);
		
		this.stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
		this.renderer = new ShapeRenderer();
		this.tiledMap = asset.get(Display.TILED_MAP_ACT_TWO);
		
		this.mapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);
		this.mapRenderer.setBlending(true);
		this.backgroundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Floor");
		this.backgroundWall = (TiledMapTileLayer) tiledMap.getLayers().get("Wall");
		this.playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
		this.enemiesLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Enemies");
		this.layers_background[0] = 0;
		this.layers_background[1] = 1;
		this.layers_background[2] = 3;
		
		List<TextureRegion> idleSprites = new ArrayList<>();
		List<TextureRegion> closedSprites = new ArrayList<>();
		List<TextureRegion> openSprites = new ArrayList<>();
		Iterator<TiledMapTile> iterator2 = tiledMap.getTileSets().getTileSet(TILED_MAP_SET).iterator();
		for (Iterator<TiledMapTile> iterator = iterator2; iterator2.hasNext();) {
			TiledMapTile next = iterator.next();
			if ("IdlePlayer".equals(next.getProperties().get("Player"))) {
				idleSprites.add(next.getTextureRegion());
			}
			if ("closed".equals(next.getProperties().get("Door"))) {
				closedSprites.add(next.getTextureRegion());
			}
			if ("open".equals(next.getProperties().get("Door"))) {
				openSprites.add(next.getTextureRegion());
			}
		}
		
		float playerXPos = 0;
		float playerYPos = 0;
		float knifeXPos = 0;
		float knifeYPos = 0;
		float doorXPos = 0;
		float doorYPos = 0;
		for (int y = 0; y < backgroundLayer.getHeight(); y++) {
			for (int x = 0; x < backgroundLayer.getWidth(); x++) {
				Cell targetPlayerCell = playerLayer.getCell(x , y);
				Cell targetEnemyCell = enemiesLayer.getCell(x , y);
				Cell targetWallCell = backgroundWall.getCell(x , y);
				if (targetPlayerCell != null && "IdlePlayer".equals(targetPlayerCell.getTile().getProperties().get("Player"))) {
					playerXPos = x * playerLayer.getTileWidth();
					playerYPos = y * playerLayer.getTileHeight();
				}
				if (enemiesLayer.getCell(x , y) != null && "knife".equals(targetEnemyCell.getTile().getProperties().get("Weapon"))) {
					knifeXPos = x * enemiesLayer.getTileWidth();
					knifeYPos = y * enemiesLayer.getTileHeight();
				}
				if (backgroundWall.getCell(x , y) != null && "wall".equals(targetWallCell.getTile().getProperties().get("Wall"))) {
					this.stage.addActor(new Wall(x * enemiesLayer.getTileWidth(), y * enemiesLayer.getTileHeight()));
				} else if (backgroundWall.getCell(x , y) != null && "position".equals(targetWallCell.getTile().getProperties().get("Door"))) {
					doorXPos = x * enemiesLayer.getTileWidth();
					doorYPos = y * enemiesLayer.getTileHeight();
				}
			}
		}

		this.knife = new Knife(knifeXPos, knifeYPos);
		this.stage.addActor(knife);
		
		this.player = new Player(playerXPos, playerYPos, playerLayer.getTileWidth(), playerLayer.getTileHeight(), idleSprites);
		this.stage.addActor(player);
		
		door2Point0 = new Door1Point0(doorXPos, doorYPos, closedSprites, openSprites);
		this.stage.addActor(door2Point0);
		
		this.walkTimer = new ApplicationTimer(0.1f);
		this.addTimer(walkTimer);
		
		this.dialogHandler = new DialogHandler(stage, getManager());
		this.actTwoTriggers = new ActTwoTriggers(this, dialogHandler, player, knife);
		this.doorSound = asset.get(Display.SOUND_DOOR, Sound.class);
		
		this.endLevelTimer = new ApplicationTimer(5f);
		this.bitmapFont = asset.get(Display.BITMAP_FONT_DIALOG_FNT, BitmapFont.class);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		stage.getCamera().update();
		actTwoTriggers.update();
		dialogHandler.update(delta);

		stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
		renderer.setProjectionMatrix(stage.getCamera().combined);
		
		mapRenderer.render(layers_background);
		mapRenderer.setView((OrthographicCamera) stage.getCamera());
		
		stage.act(delta);
		stage.draw();
		
		renderDarkness();
		input(Gdx.input);
		
		if (STAGE_TWO) {
			stage.getBatch().begin();
			layout.setText(bitmapFont, act_two);
			bitmapFont.setColor(Color.WHITE);
			bitmapFont.draw(stage.getBatch(), act_two, (WORLD_WIDTH/2) - layout.width/2, (WORLD_HEIGHT/2) - layout.width/2);
			stage.getBatch().end();
			
			endLevelTimer.tick(Gdx.graphics.getDeltaTime());
			if (endLevelTimer.isTimerEventReady()) {
				super.setScreen(ScreenIds.GAME_ACT_THREE);
			}
		}
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
		renderer.dispose();
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
		
		if (input.isKeyJustPressed(Input.Keys.SPACE) && door2Point0.getCurrentState().equals(DOOR_STATE.CLOSED) && isPlayerTwoTilesAwayWithKnifePosition() ) {
			knife.remove();
			door2Point0.setCurrentState(DOOR_STATE.OPEN);
			layers_background[2] = 1;
			doorSound.play();
		} else if (door2Point0.getCurrentState().equals(DOOR_STATE.OPEN) &&
				(MathUtils.isEqual(player.getX(), door2Point0.getX()) && MathUtils.isEqual(player.getY(), door2Point0.getY()))) {
			STAGE_TWO = true;
		}
	}
	
	private void renderDarkness() {
		Gdx.gl.glEnable(GL30.GL_BLEND);
	    renderer.begin(ShapeType.Filled);
	    
		for (int y = 0; y < backgroundLayer.getHeight(); y++) {
			for (int x = 0; x < backgroundLayer.getWidth(); x++) {
				if (isPlayerTwoTilesAway(y, x)) {
					renderer.setColor(0, 0, 0, .4f);	
				} else if (x * backgroundLayer.getTileWidth() > player.getX()-(3*backgroundLayer.getTileWidth()) 
						&& x * backgroundLayer.getTileWidth() < player.getX()+(3*backgroundLayer.getTileWidth())
						&& y * backgroundLayer.getTileHeight() > player.getY()-(3*backgroundLayer.getTileHeight()) 
						&& y * backgroundLayer.getTileHeight() < player.getY()+(3*backgroundLayer.getTileHeight())) {
					renderer.setColor(0, 0, 0, .6f);	
				} else if (x * backgroundLayer.getTileWidth() > player.getX()-(4*backgroundLayer.getTileWidth()) 
						&& x * backgroundLayer.getTileWidth() < player.getX()+(4*backgroundLayer.getTileWidth())
						&& y * backgroundLayer.getTileHeight() > player.getY()-(4*backgroundLayer.getTileHeight()) 
						&& y * backgroundLayer.getTileHeight() < player.getY()+(4*backgroundLayer.getTileHeight())) {
					renderer.setColor(0, 0, 0, .8f);	
				} else 
					renderer.setColor(0, 0, 0, 1f);
				
				if (stage.getActors().contains(knife, true)) {
					if (isKnifeOneTileAway(y, x) && !isPlayerTwoTilesAway(y, x)) {
						renderer.setColor(0, 0, 0, .6f);	
					} else if (isKnifeTwoTileAway(y, x) && !isPlayerTwoTilesAway(y, x)) {
						renderer.setColor(0, 0, 0, .8f);	
					} else if (isKnifeTwoTileAway(y, x)) {
						actTwoTriggers.setTriggerDialog(true);
						renderer.setColor(0, 0, 0, .4f);
					} else if (isKnifeOneTileAway(y, x)) {
						renderer.setColor(0, 0, 0, .4f);
					}
				}
				
				renderer.rect(x * backgroundLayer.getTileWidth(), y * backgroundLayer.getTileHeight(), backgroundLayer.getTileWidth(), backgroundLayer.getTileHeight());
			}
		}
		renderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
	}

	private boolean isKnifeOneTileAway(int y, int x) {
		return x * backgroundLayer.getTileWidth() > knife.getX()-(1*backgroundLayer.getTileWidth()) 
				&& x * backgroundLayer.getTileWidth() < knife.getX()+(1*backgroundLayer.getTileWidth())
				&& y * backgroundLayer.getTileHeight() > knife.getY()-(1*backgroundLayer.getTileHeight()) 
				&& y * backgroundLayer.getTileHeight() < knife.getY()+(1*backgroundLayer.getTileHeight());
	}
	
	private boolean isKnifeTwoTileAway(int y, int x) {
		return x * backgroundLayer.getTileWidth() > knife.getX()-(2*backgroundLayer.getTileWidth()) 
				&& x * backgroundLayer.getTileWidth() < knife.getX()+(2*backgroundLayer.getTileWidth())
				&& y * backgroundLayer.getTileHeight() > knife.getY()-(2*backgroundLayer.getTileHeight()) 
				&& y * backgroundLayer.getTileHeight() < knife.getY()+(2*backgroundLayer.getTileHeight());
	}

	private boolean isPlayerTwoTilesAway(int y, int x) {
		return x * backgroundLayer.getTileWidth() > player.getX()-(2*backgroundLayer.getTileWidth()) 
				&& x * backgroundLayer.getTileWidth() < player.getX()+(2*backgroundLayer.getTileWidth())
				&& y * backgroundLayer.getTileHeight() > player.getY()-(2*backgroundLayer.getTileHeight()) 
				&& y * backgroundLayer.getTileHeight() < player.getY()+(2*backgroundLayer.getTileHeight());
	}
	
	private boolean isPlayerTwoTilesAwayWithKnifePosition() {
		return knife.getX()+1 > player.getX()-(2*backgroundLayer.getTileWidth()) 
				&& knife.getX()-1 * backgroundLayer.getTileWidth() < player.getX()+(2*backgroundLayer.getTileWidth())
				&& knife.getY()+1 * backgroundLayer.getTileHeight() > player.getY()-(2*backgroundLayer.getTileHeight()) 
				&& knife.getY()-1 * backgroundLayer.getTileHeight() < player.getY()+(2*backgroundLayer.getTileHeight());
	}
	
	private boolean isSpaceAvailable(float xPos, float yPos) {
		for (Actor actor : stage.getActors()) {
			if ((MathUtils.isEqual(actor.getX(), xPos) && MathUtils.isEqual(actor.getY(), yPos) && !(actor instanceof Door1Point0)) 
					|| (MathUtils.isEqual(actor.getX(), xPos) && MathUtils.isEqual(actor.getY(), yPos) && !door2Point0.getCurrentState().equals(DOOR_STATE.OPEN))) {
				return false;
			}
		}
		return true;
	}
}
