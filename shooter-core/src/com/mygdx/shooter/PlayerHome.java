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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;


public class PlayerHome extends CustomScreen {

	private  FreeTypeFontGenerator generator = null;
	
	private Skin skin2 = null;
	private Skin skin;
	private Skin textboxskin = null;
    private TextureAtlas atlas = null;
	
	private Stage stage;
	private Stage temp;
	
	private Table table;
	private Table mainTable;
	
	private Label titleLabel;
	private Label nameLabel;
	
	private TextField nameField;
	
	private TextButton enter;
	private TextButton back;
	
	private BitmapFont button = null;
	private BitmapFont tLabel = null;
	private BitmapFont fLabel = null;
	private BitmapFont field = null;
       
    private	Texture uiButton =null;
    private	Texture uiCursor =null;

    private OrthographicCamera camera;

    
    @SuppressWarnings("deprecation")	//suppresses deprecation warnings generated by "generateFont()"
	public PlayerHome() {
    	
    	setName("Player_HOME");			//Sets the current activity name
    	
    	//Implements the device's Back button functionality on this activity
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
        atlas = ResourceManager.getAtlas();     	//fetches textureAtlas 
        skin = ResourceManager.getDefaultSkin();  	//fetches default skin
        skin2 = ResourceManager.getCustomSkin();	//fetches customized skin

        //Generates font size for objects containing text
		button = generator.generateFont(Gdx.graphics.getWidth()/24);
        tLabel = generator.generateFont(Gdx.graphics.getWidth()/10);
        fLabel = generator.generateFont(Gdx.graphics.getWidth()/25);
        field = generator.generateFont(Gdx.graphics.getWidth()/40);
       
        //Label style instantiations
        LabelStyle titleSkin = new LabelStyle();
        LabelStyle formLabelSkin = new LabelStyle();
        
        //Sets font size of various label styles   
        titleSkin.font = tLabel;   
        formLabelSkin.font = fLabel;
        
        //Configurations of text field style
        TextFieldStyle textfieldstyle = new TextFieldStyle();
        textboxskin = new Skin();
        uiButton = new Texture(Gdx.files.internal("ui/button.down.png"));
        uiCursor = new Texture(Gdx.files.internal("ui/cursor.png"));
        textboxskin.add("textfieldback", uiButton); 
        textboxskin.add("cursor", uiCursor); 
        textboxskin.add("font", field);
        textfieldstyle.background= textboxskin.getDrawable("textfieldback");
        textfieldstyle.font=textboxskin.getFont("font");
        textfieldstyle.fontColor= com.badlogic.gdx.graphics.Color.WHITE;
        textfieldstyle.cursor= textboxskin.getDrawable("cursor");
        
        //Configurations of text button style
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin2.getDrawable("button.up");
        textButtonStyle.down = skin2.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 3;
        textButtonStyle.pressedOffsetX = -3;
        textButtonStyle.font= button;  
        
        titleLabel = new Label("Player Login", titleSkin);		//title label instantiation

        nameLabel = new Label("Username", formLabelSkin); 
        nameField = new TextField ("", textfieldstyle);
        
    	Gdx.input.setInputProcessor(stage);		//attaches input process to current activity

    	//Instantiation and configuration of "Enter" button
    	enter = new TextButton("OK", textButtonStyle);
    	enter.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			
    			DataManager.setUsername(nameField.getText());	//sets the current player name
    			String sID = DatabaseManager.getLastSessionID();	//fetches the current active session
    			
    			if (fieldsCompleted()==true){	//if all field have data	
    				
    				//if player is scheduled to participate in current active session
	    			if (DatabaseManager.validPlayer(nameField.getText(), sID) == true  &&
	    				DatabaseManager.alreadyPlayed(nameField.getText(), sID) == false){
	    					ScreenManager.getInstance().show("PLAYER_PROFILE", Screens.PLAYER_PROFILE);	//switch to "Player Profile" activity
	    			}
	    			else if (DatabaseManager.validPlayer(nameField.getText(), sID) == false){	//if player is not on schedule, a dialog box provides the appropriate feedback
	    				new Dialog("", skin, "dialog") {
	    					protected void result (Object object) {
	    					}
	    				}.text("PlayerID invalid").button("Ok", true).show(stage);
	    			}
	    			else if (DatabaseManager.alreadyPlayed(nameField.getText(), sID) == true){	//if players has already particpated, a dialog box provides the appropriate feedback
	    				new Dialog("", skin, "dialog") {
	    					protected void result (Object object) {
	    					}
	    				}.text("Player assessment already completed").button("Ok", true).show(stage);
	    			}
    			}
    			else{
    				new Dialog("", skin, "dialog") {	//if all fields are not completed, a dialog box provides the appropriate feedback
    					protected void result (Object object) {
    					}
    				}.text("Please complete blank fields").button("Ok", true).show(stage);
    			}
    		}
    	});
    	
    	//Instantiation and configuration of "Back" button
    	back = new TextButton("Back", textButtonStyle);
    	back.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			ScreenManager.getInstance().show("MAIN_MENU", Screens.MAIN_MENU);		//switch to "Main Menu" activity
    		}
    	});
    	   	
    	//Instantiates tables and attaches labels, text fields and buttons
    	mainTable = new Table();
        mainTable.add(titleLabel);
        mainTable.row();
        
        table = new Table();
        table.add(nameLabel).align(Align.left).spaceRight(Gdx.graphics.getWidth()/75);
        table.add(nameField).width(nameLabel.getWidth()*(float)1.5);
        table.row();
         
        table.add(enter).spaceTop(Gdx.graphics.getWidth()/50).spaceRight(Gdx.graphics.getWidth()/25);
    	table.add(back).spaceTop(Gdx.graphics.getWidth()/50);
    	
    	mainTable.add(table);
    	mainTable.pack();
    	
    	
    	//Centers the main table within the screen
    	int xPos = (int) (   (Gdx.graphics.getWidth()/2) - (mainTable.getPrefWidth() / 2 ) );
    	int yPos = (int) (   (Gdx.graphics.getHeight()/2) - (mainTable.getPrefHeight() / 2 ) );
    	mainTable.setPosition(xPos, yPos);	

    	stage.addActor(ResourceManager.getBackImg());	//attaches background image to stage
    	stage.addActor(mainTable);   	//attaches main table to the activity	
    }

    //Returns false if any field is left empty
  	public boolean fieldsCompleted(){
  		if (nameField.getText().equals("") ){
  				return false;
  		}
  		return true;
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
		fLabel.dispose();
		textboxskin.dispose();
		stage.dispose();
	}

	
	@Override
	public void pause() {}

	
	@Override
	public void resume() {}

	
	@Override
	public void dispose() {}

}
