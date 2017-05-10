package wit.cgd.warbirds.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import wit.cgd.warbirds.game.util.Constants;

public class WorldRenderer implements Disposable {

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
	
	private void renderGui(SpriteBatch batch){
		
		renderHealthBar();
		
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		
		//Render player score at the top left corner
		renderGuiScore(batch);
		
		//Render health text in the top right corner
		renderGuiHealth(batch);
		
		//Render level number in the bottom left corner
		renderGuiLevelNumber(batch);
		
		batch.end();
	}
	
	private void renderGuiScore(SpriteBatch batch) {
        Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.level.player.score, cameraGUI.viewportWidth-100, 22);
    }
	
	private void renderGuiHealth(SpriteBatch batch){
		Assets.instance.fonts.defaultNormal.draw(batch, "Health", 50, 22);
	}
	
	private void renderGuiLevelNumber(SpriteBatch batch){
		TextureRegion region = Assets.instance.levelNumber.get(worldController.level.levelNumber-1).region;
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

	@Override
	public void dispose() {
		batch.dispose();
	}
}
