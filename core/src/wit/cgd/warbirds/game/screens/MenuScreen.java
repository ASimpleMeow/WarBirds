package wit.cgd.warbirds.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.util.AudioManager;
import wit.cgd.warbirds.game.util.Constants;
import wit.cgd.warbirds.game.util.GamePreferences;

public class MenuScreen extends AbstractGameScreen{
	
	private static final String TAG	= MenuScreen.class.getName();
	
	private Stage			stage;
	
	
	//MenuScreen widgets
	private Button			startButton;
	private Button			continueButton;
	private Button			optionsButton;
	
	//Options widgets
	private Table			optionsWindowLayer;
	private Window			optionsWindow;
	
	private Button			optionsOkButton;
	private Button			optionsCancelButton;
	
	private CheckBox        soundCheckBox;
	private Slider          soundSlider;
	private CheckBox        musicCheckBox;
	private Slider          musicSlider;
	private Slider			enemySpawnSlider;
	
	
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
    	GamePreferences.instance.load();
    	
    	//assemble stage for menu screen
    	stage.clear();
    	Stack stack = new Stack();
    	stage.addActor(stack);
    	stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
    	
    	stack.add(buildBackgroundLayer());
    	stack.add(buildControlsLayer());
    	
    	optionsWindowLayer = buildOptionsWindowLayer();
    	stage.addActor(optionsWindowLayer);
    	
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
    	table.add(startButton).pad(Constants.BUTTON_PADDING);
    	startButton.addListener(new ChangeListener(){
    		@Override
    		public void changed(ChangeEvent event, Actor actor){
    			onPlayerClicked();
    		}
    	});
    	table.row();
    	
    	continueButton = new Button(skin, "continue");
    	table.add(continueButton).pad(Constants.BUTTON_PADDING);
    	continueButton.addListener(new ChangeListener(){
    		@Override
    		public void changed(ChangeEvent event, Actor actor){
    			onContinueClicked();
    		}
    	});
    	table.row();
    	
    	optionsButton = new Button(skin, "options");
    	table.add(optionsButton).pad(Constants.BUTTON_PADDING);
    	optionsButton.addListener(new ChangeListener(){
    		@Override
    		public void changed(ChangeEvent event, Actor actor){
    			onOptionsClicked();
    		}
    	});
    	
    	return table;
    }
    
    private void onPlayerClicked(){
    	AudioManager.instance.play(Assets.instance.sounds.click);
    	GamePreferences.instance.levelNumber = 1;
    	game.setScreen(new GameScreen(game));
    }
    
    private void onContinueClicked(){
    	AudioManager.instance.play(Assets.instance.sounds.click);
    	game.setScreen(new GameScreen(game));
    }
    
    private void onOptionsClicked(){
    	AudioManager.instance.play(Assets.instance.sounds.click);
    	startButton.setVisible(false);
    	continueButton.setVisible(false);
    	optionsButton.setVisible(false);
    	optionsWindow.setVisible(true);
    	loadOptions();
    }
    
    private Table buildOptionsWindowLayer(){
    	
    	//Create instance of window
    	optionsWindow = new Window("Options", defaultSkin);
    	
    	optionsWindow.add(new Label("Sound Effects", defaultSkin)).colspan(3);
    	optionsWindow.row();
        soundCheckBox = new CheckBox("On",defaultSkin);
        optionsWindow.add(soundCheckBox);
        soundCheckBox.addListener(new ChangeListener(){
        	@Override
        	public void changed(ChangeEvent event, Actor actor){
        		CheckBox me = (CheckBox) actor;
        		disableSlider(soundSlider, me.isChecked());
        	}
        });
        soundSlider = new Slider(0.0f,1.0f,0.1f,false,defaultSkin);
        optionsWindow.add(soundSlider).prefWidth(Constants.VIEWPORT_GUI_WIDTH/2).colspan(2);
        optionsWindow.row().padBottom(10);
        
        // music settings
        optionsWindow.add(new Label("Music",defaultSkin)).colspan(3);
        optionsWindow.row();
        musicCheckBox = new CheckBox("On",defaultSkin);
        optionsWindow.add(musicCheckBox);
        musicCheckBox.addListener(new ChangeListener(){
        	@Override
        	public void changed(ChangeEvent event, Actor actor){
        		CheckBox me = (CheckBox) actor;
        		disableSlider(musicSlider, me.isChecked());
        	}
        });
        musicSlider = new Slider(0.0f,1.0f,0.1f,false,defaultSkin);
        optionsWindow.add(musicSlider).prefWidth(Constants.VIEWPORT_GUI_WIDTH/2).colspan(2);
        optionsWindow.row().padBottom(10);
        
        optionsWindow.add(new Label("Enemy Spawn (Difficulty)", defaultSkin)).colspan(3);
        optionsWindow.row();
        enemySpawnSlider = new Slider(1, 5, 1, false, defaultSkin);
        optionsWindow.add(enemySpawnSlider).prefWidth(Constants.VIEWPORT_GUI_WIDTH/2).colspan(2);
        optionsWindow.row().padBottom(10);
        
        // ok and cancel buttons 
        optionsOkButton = new Button(skin,"ok");
        optionsWindow.add(optionsOkButton).width(125).height(50).pad(Constants.BUTTON_PADDING);
        optionsOkButton.addListener(new ChangeListener(){
        	@Override
        	public void changed(ChangeEvent event,Actor actor){
        		onOkClicked();
        	}
        });
        
        optionsCancelButton = new Button(skin,"cancel");
        optionsWindow.add(optionsCancelButton).width(125).height(50).pad(Constants.BUTTON_PADDING);
        optionsCancelButton.addListener(new ChangeListener(){
        	@Override
        	public void changed(ChangeEvent event, Actor actor){
        		onCancelClicked();
        	}
        });
        
        // tidy up window = resize and center
        optionsWindow.setColor(0.7f,0.7f,0.7f,0.9f);
        optionsWindow.setVisible(false);
        optionsWindow.setWidth(Constants.VIEWPORT_GUI_WIDTH);
        optionsWindow.setHeight(Constants.VIEWPORT_GUI_HEIGHT/3);
        optionsWindow.setPosition((Constants.VIEWPORT_GUI_WIDTH - optionsWindow.getWidth())/2,
                (Constants.VIEWPORT_GUI_HEIGHT - optionsWindow.getHeight())/2);
        
        // return constructed window
        return optionsWindow;
    }
    
    private void onOkClicked(){
    	GamePreferences.instance.soundVolume = soundSlider.getValue();
    	GamePreferences.instance.sound = soundCheckBox.isChecked();
    	GamePreferences.instance.musicVolume = musicSlider.getValue();
    	GamePreferences.instance.music = musicCheckBox.isChecked();
    	GamePreferences.instance.enemySpawnLimit = (int) enemySpawnSlider.getValue();
    	AudioManager.instance.onSettingsUpdated();
    	GamePreferences.instance.save();
    	onCancelClicked();
    }
    
    private void onCancelClicked(){
    	AudioManager.instance.play(Assets.instance.sounds.click);
    	startButton.setVisible(true);
    	continueButton.setVisible(true);
        optionsButton.setVisible(true);
        optionsWindow.setVisible(false);
    }
    
    private void loadOptions(){
    	soundCheckBox.setChecked(GamePreferences.instance.sound);
    	soundSlider.setValue(GamePreferences.instance.soundVolume);
    	musicCheckBox.setChecked(GamePreferences.instance.music);
    	musicSlider.setValue(GamePreferences.instance.musicVolume);
    	enemySpawnSlider.setValue(GamePreferences.instance.enemySpawnLimit);
    	disableSlider(soundSlider, soundCheckBox.isChecked());
    	disableSlider(musicSlider, musicCheckBox.isChecked());
    }
    
    private void disableSlider(Slider slider, boolean enable){
    	if(enable){
    		slider.setDisabled(false);
    		slider.setColor(skin.getColor("white"));
		}else{
			slider.setDisabled(true);
			slider.setColor(skin.getColor("gray"));
		}
    }
}
