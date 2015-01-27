package com.mygdx.shooter;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class MainMenuScreen extends CustomScreen {
	
	private  FreeTypeFontGenerator generator = null;
	
	private BitmapFont button = null;
    private BitmapFont tLabel = null;
    private BitmapFont dialg = null;
	
	private Skin skin = null;
	private Skin skin2 = null;
    private TextureAtlas atlas = null;
	
	private Stage temp= null;
	private Stage stage= null;
	
	private VerticalGroup vGroup= null;
	private Table table= null;
	
	private TextButton admin= null;
	private TextButton player= null;
	private TextButton instruct= null;
	
	private Label titleLabel = null;
	
    private OrthographicCamera camera;
    
    
    @SuppressWarnings("deprecation")	//suppresses deprecation warnings generated by "generateFont()"
	public MainMenuScreen() {
    	
    	setName("MAIN_MENU");	//Sets the current activity name
    	
    	//Implements the device's Back button functionality on current activity
    	temp = new Stage(){
    		@Override
	        public boolean keyDown(int keyCode) {
	            if (keyCode == Keys.BACK) {
	            	Gdx.app.exit();
	            }
	            return super.keyDown(keyCode);
    		}
    	};
    	setStage(temp);
    	stage = getStage();
    	
    	//Loads shared resources
    	ResourceManager.loadBackground();		//loads background
    	ResourceManager.configureBackground();
    	ResourceManager.loadFontGenerator();	//loads font generator
    	ResourceManager.loadSkinAtlas();

    	//Configures camera settings
    	camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        System.out.println("Width=" +  Gdx.graphics.getWidth() + "   Height=" + Gdx.graphics.getHeight());
        
        generator = ResourceManager.getGenerator();	//fetches font generator
        skin = ResourceManager.getDefaultSkin(); 	//fetches default skin
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

    	Gdx.input.setInputProcessor(stage);	//attaches input process to current activity

    	//Instantiation and configuration of "Administrator Profile" button
    	admin = new TextButton("Administrator", textButtonStyle);
    	admin.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			ScreenManager.getInstance().show("ADMIN_HOME", Screens.ADMIN_HOME);	//switch to "Administrator Home" activity
    		}
    	});

    	//Instantiation and configuration of "Player Profile" button
    	player = new TextButton("Player", textButtonStyle);
    	player.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			
    			if (DatabaseManager.getLastSessionID().equals(" ")){
    				new Dialog("", skin, "dialog") { 					
    					protected void result (Object object) {
    					}
    				}.text("No player profiles currently exist").button("Ok", true).show(stage);
    			}
    			else{
    				ScreenManager.getInstance().show("PLAYER_HOME", Screens.PLAYER_HOME);	//switch to "Player Home" activity
    			}
    		}
    	});
    	
    	//Instantiation and configuration of "Instructions" button
    	instruct = new TextButton("Instructions", textButtonStyle);
    	instruct.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
//    			ScreenManager.getInstance().show("INSTRUCTIONS", Screens.INSTRUCTIONS); //switch to "Instructions" activity
    			ScreenManager.getInstance().show("SCROLL", Screens.SCROLL); //switch to "Instructions" activity
    		}
    	});
    	
    	//Instantiates vertical group and attaches labels and text buttons
    	vGroup = new VerticalGroup().space(Gdx.graphics.getWidth()/35).pad(5).fill();
    	titleLabel = new Label("Main Menu", titleSkin); 
    	vGroup.addActor(titleLabel);
    	
    	vGroup.addActor(admin);
    	vGroup.addActor(player);
    	vGroup.addActor(instruct);
    	
    	table = new Table();
    	table.add(vGroup);
    	table.pack();
    	
    	//Centers table in screen
    	int xPos = (int) (Gdx.graphics.getWidth() - table.getPrefWidth()) / 2;
    	int yPos = (int) (Gdx.graphics.getHeight() - table.getPrefHeight()) / 2;
    	table.setPosition(xPos, yPos);

    	stage.addActor(ResourceManager.getBackImg());	//attaches background image to stage
    	stage.addActor(table);							//attaches main table to the activity
    	
    }
    
    
    public String declare(){
		return "Main menu screen";
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
	public void show() {}

	
	@Override
	//Disposes resources to mitigate memory leaks
	public void hide() {
		button.dispose();
		tLabel.dispose();
		stage.dispose();
	}

	
	@Override
	public void pause() {}

	
	@Override
	public void resume() {}

	
	@Override
	public void dispose() {}
	
}