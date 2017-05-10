package wit.cgd.warbirds.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import wit.cgd.warbirds.game.util.Constants;

public class MenuScreen extends AbstractGameScreen{
	
	private static final String TAG	= MenuScreen.class.getName();
	
	private Stage			stage;
	
	
	//MenuScreen widgets
	private Button			startButton;
	private Button			optionsButton;
	
	
	public MenuScreen(Game game){
		super(game);
	}
	
	@Override
	public void render(float deltaTime){
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    stage.act(deltaTime);
	    stage.draw();
	}
	
	@Override 
    public void show() {
    	stage = new Stage();
    	Gdx.input.setInputProcessor(stage);
    	rebuildStage();
    }
	
	@Override
	public void resize(int width, int height) {}
    
    @Override 
    public void hide() {
    	stage.dispose();
    }
    
    @Override public void pause() {}
    
    private void rebuildStage(){
    	skin = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
    	
    	//assemble stage for menu screen
    	stage.clear();
    	Stack stack = new Stack();
    	stage.addActor(stack);
    	stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
    	
    	stack.add(buildBackgroundLayer());
    	stack.add(buildControlsLayer());
    }
    
    private Table buildBackgroundLayer(){
    	Table table = new Table();
    	table.add(new Image(skin, "background"));
    	return table;
    }
    
    private Table buildControlsLayer(){
    	Table table = new Table();
    	table.center().bottom();
    	
    	startButton = new Button(skin, "start");
    	table.add(startButton).pad(5);
    	startButton.addListener(new ChangeListener(){
    		@Override
    		public void changed(ChangeEvent event, Actor actor){
    			onPlayerClicked();
    		}
    	});
    	table.row();
    	
    	optionsButton = new Button(skin, "options");
    	table.add(optionsButton).pad(5);
    	optionsButton.addListener(new ChangeListener(){
    		@Override
    		public void changed(ChangeEvent event, Actor actor){
    			onOptionsClicked();
    		}
    	});
    	
    	return table;
    }
    
    private void onPlayerClicked(){
    	game.setScreen(new GameScreen(game));
    }
    
    private void onOptionsClicked(){
    	
    }
}
