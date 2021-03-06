package com.mygdx.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;


public class PlayerProfile extends CustomScreen {

	private  FreeTypeFontGenerator generator = null;
	
	private Skin skin2 = null;
    private TextureAtlas atlas = null;
	
    private Stage stage;
	private Stage temp;
	
	private VerticalGroup vGroup;
	private Table table;
	
	private Label label;
	
	private TextButton start;
	private TextButton instructions;
	
	private BitmapFont button = null;
    private BitmapFont tLabel = null;

    private OrthographicCamera camera;

    
    @SuppressWarnings("deprecation")	//suppresses deprecation warnings generated by "generateFont()"
	public PlayerProfile() {

    	setName("PLAYER_PROFILE");		//Sets the current activity name
    	
    	//Implements the device's Back button functionality on current activity
    	temp = new Stage(){
    		@Override
	        public boolean keyDown(int keyCode) {
	            if (keyCode == Keys.BACK) {
	            	ScreenManager.getInstance().show("MAIN_MENU", Screens.MAIN_MENU);
	            }
	            return super.keyDown(keyCode);
    		}
    	};
    	setStage(temp);
    	stage = getStage();	
    
    	//Configures camera settings
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        generator = ResourceManager.getGenerator();	//fetches font generator
        atlas = ResourceManager.getAtlas();			//fetches textureAtlas
        skin2 = ResourceManager.getCustomSkin();	//fetches customized skin

        //Generates font size for objects containing text
		button = generator.generateFont(Gdx.graphics.getWidth()/20);
        tLabel = generator.generateFont(Gdx.graphics.getWidth()/10);
              
        LabelStyle titleSkin = new LabelStyle();
        titleSkin.font = tLabel;
       
        //Configurations of text button style
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin2.getDrawable("button.up");
        textButtonStyle.down = skin2.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 3;
        textButtonStyle.pressedOffsetX = -3;
        textButtonStyle.font= button;  

        label = new Label("Welcome " + DataManager.getUsername(), titleSkin);	//title label instantiation
        
    	Gdx.input.setInputProcessor(stage);		//attaches input process to current activity

    	//Instantiation and configuration of "Start" button
    	start = new TextButton("Start Game", textButtonStyle);
    	start.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			ScreenManager.getInstance().show("MAIN_GAME_SCREEN", Screens.MAIN_GAME_SCREEN);		//switch to "Game Screen" activity
    		}
    	});
    	
    	//Instantiation and configuration of "Instructions" button
    	instructions = new TextButton("Instructions", textButtonStyle);
    	instructions.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			ScreenManager.getInstance().show("PLAYER_INSTRUCTIONS", Screens.PLAYER_INSTRUCTIONS);	//switch to "Player Instructions" activity
    		}
    	});
    	
    	//Instantiates vertical group and tables; and attaches labels, text fields and buttons
    	vGroup = new VerticalGroup().space(Gdx.graphics.getWidth()/50).pad(5).fill();
    	vGroup.addActor(label);
    	vGroup.addActor(start);
    	vGroup.addActor(instructions);
	
    	table = new Table();
    	table.add(vGroup);
    	table.pack();
    	
    	//Centers table in screen
    	int xPos = (int) (Gdx.graphics.getWidth() - table.getPrefWidth()) / 2;
    	int yPos = (int) (Gdx.graphics.getHeight() - table.getPrefHeight()) / 2;
    	table.setPosition(xPos, yPos);
    	
    	stage.addActor(ResourceManager.getBackImg());	//attaches background image to stage
    	stage.addActor(table);	//attaches main table to the activity
    	
    }

    
    @Override
    public void render(float delta) {     
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	stage.act(Gdx.graphics.getDeltaTime());
    	stage.draw();
    	Table.drawDebug(stage);
    }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		
	}

	@Override
	public void show() {	
	}

	@Override
	public void hide() {
		button.dispose();
		tLabel.dispose();
		stage.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}

