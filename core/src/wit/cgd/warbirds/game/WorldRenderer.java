package wit.cgd.warbirds.game;
/**
 * @file        WorldRenderer
 * @author      Oleksandr Kononov 20071032
 * @assignment  WarBirds
 * @brief       Renders everything in the game
 *
 * @notes       
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;
import wit.cgd.warbirds.game.util.Constants;

public class WorldRenderer implements Disposable {

	@SuppressWarnings("unused")
	private static final String	TAG	= WorldRenderer.class.getName();

	public OrthographicCamera	camera;
	public OrthographicCamera	cameraGUI;
	
	private SpriteBatch			batch;
	private WorldController		worldController;
	
	private ShapeRenderer		shapeRenderer;

	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
		
		shapeRenderer = new ShapeRenderer();
	}

	public void resize(int width, int height) {
		
		float scale = (float)height/(float)width;
		camera.viewportHeight = scale * Constants.VIEWPORT_HEIGHT;
		camera.update();
		worldController.viewportWidth = camera.viewportWidth;
		worldController.width = width;
		worldController.height = height;
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = scale*Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
		
		// update level decoration
		worldController.level.levelDecoration.scale.y =  scale;
	}
	
	public void render() {
		
		// Game rendering
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		
		// GUI + HUD rendering 
		renderGui(batch);
	}
	
	/**
	 * Render GUI on screen such as Texts, Health and Shield bars
	 */
	private void renderGui(SpriteBatch batch){

		//Render green (red when low) health bar
		renderHealthBar();
		
		//Render player's shield bar
		renderShieldBar();
		
		
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		
		//Render player score at the top left corner
		renderGuiScore(batch);
		
		//Render health text in the top right corner
		renderGuiTextNormal(batch, "Health", 50, 22);
		
		//If player has a shield, render shield text
		if(worldController.level.player.shield > 0) renderGuiTextNormal(batch, "Shield", 50, 60);
		
		if(worldController.level.levelStartTimer > 0)
			renderGuiTextBig(batch, "LEVEL START\n           "+(int)(worldController.level.levelStartTimer+1),
					Constants.VIEWPORT_GUI_WIDTH,
					Constants.VIEWPORT_GUI_HEIGHT/3);
		
		if(worldController.level.levelBossTimer > 0 && worldController.level.startBoss)
			renderGuiTextBig(batch, "BOSS START\n           "+(int)(worldController.level.levelBossTimer+1),
					Constants.VIEWPORT_GUI_WIDTH,
					Constants.VIEWPORT_GUI_HEIGHT/3);
		
		if(worldController.level.levelEndTimer != Constants.LEVEL_END_DELAY)
			renderGuiTextBig(batch, (worldController.level.player.isDead())?"        YOU DIED":"LEVEL COMPLETE",
					Constants.VIEWPORT_GUI_WIDTH-100, Constants.VIEWPORT_GUI_HEIGHT/3);
			
		//Render level number in the bottom left corner
		renderGuiLevelNumber(batch);
		
		batch.end();
	}
	
	private void renderGuiScore(SpriteBatch batch) {
		Assets.instance.fonts.defaultNormal.draw(batch, "Score: ", cameraGUI.viewportWidth-350, 20);
        Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.level.player.score, cameraGUI.viewportWidth-175, 20);
    }
	
	private void renderGuiTextNormal(SpriteBatch batch, String text, float x, float y){
		Assets.instance.fonts.defaultNormal.draw(batch, text, x, y);
	}
	
	private void renderGuiTextBig(SpriteBatch batch, String text, float x, float y){
		Assets.instance.fonts.defaultBig.draw(batch, text, x, y);
	}
	
	private void renderGuiLevelNumber(SpriteBatch batch){
		TextureRegion region = Assets.instance.levelNumbers.regions.get(worldController.level.levelNumber-1);
		batch.draw(region.getTexture(), 25, cameraGUI.viewportHeight-100, 200, 100, region.getRegionX(), region.getRegionY(),
				region.getRegionWidth(), region.getRegionHeight(), false, true);
	}
	
	private void renderHealthBar(){
		shapeRenderer.setProjectionMatrix(cameraGUI.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor((worldController.level.player.health > 5)? Color.GREEN : Color.RED);
		shapeRenderer.rect(50, 22, (worldController.level.player.health)*20, 25);
		shapeRenderer.end();
	}
	
	private void renderShieldBar(){
		shapeRenderer.setProjectionMatrix(cameraGUI.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.CYAN);
		shapeRenderer.rect(50, 60, (worldController.level.player.shield)*20, 25);
		shapeRenderer.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
