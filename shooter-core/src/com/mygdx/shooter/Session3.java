package com.mygdx.shooter;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Scaling;

public class Session3 extends CustomScreen{
	
	private  FreeTypeFontGenerator generator = null;
	
	private Skin skin2 = null;
	private Skin skin;
	private Skin textboxskin = null;
    private TextureAtlas atlas = null;
	
	private Stage stage;
	private Stage temp;
	
	private TextButton next;
	private TextButton back;
	
	private BitmapFont button = null;
    private BitmapFont tLabel = null;
    private BitmapFont fLabel = null;
    private BitmapFont field = null;
     
    private	Texture uiButton =null;
    private	Texture uiCursor =null;
	
	private Label titleLabel;
	
	private Table buttonTable;
	private Table mainTable;
	private Table nameTable;
	private VerticalGroup vGroup;
	
	private OrthographicCamera camera;
	
	private ArrayList<TextField> textFields;
	
	private int count=1;
	

	@SuppressWarnings("deprecation")		//suppresses deprecation warnings generated by "generateFont()"
	public Session3(int participants){
		
		setName("CREATE_SESSION_THREE");	//Sets the current activity name
    	
		
		//Implements the device's Back button functionality on current activity
		temp = new Stage(){
    		@Override
	        public boolean keyDown(int keyCode) {
	            if (keyCode == Keys.BACK) {
	            	ScreenManager.getInstance().show("CREATE_SESSION_TWO", Screens.CREATE_SESSION_TWO);
	            }
	            return super.keyDown(keyCode);
    		}
    	};
    	setStage(temp);
    	stage = getStage();	
    	
    	Gdx.input.setInputProcessor(stage);	//attaches input process to current activity

		textFields = new ArrayList<TextField>(participants);
		
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
        
        LabelStyle titleSkin = new LabelStyle();
        titleSkin.font = tLabel;
        
        LabelStyle formLabelSkin = new LabelStyle();
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
        
        titleLabel = new Label("Enter Participants", titleSkin);	//title label instantiation
        
        vGroup = new VerticalGroup().space(Gdx.graphics.getWidth()/40).pad(5).fill();
        nameTable = new Table().padTop(Gdx.graphics.getWidth()/50).padBottom(Gdx.graphics.getWidth()/50);
        
        //Creates a number of text fields for the specified number of players
        for(int i=0 ; i< participants ; i++){
        	textFields.add(new TextField("participant " + i, textfieldstyle));
        	nameTable.add((TextField)textFields.get(i)).width(Gdx.graphics.getWidth()/5).spaceBottom(Gdx.graphics.getWidth()/50);
        	if (i<participants-1){
        		nameTable.row();
        	}
        }

        //Instantiates tables and attaches labels, text fields and buttons
        mainTable = new Table();
        mainTable.add(titleLabel);
        mainTable.row();
        mainTable.add(nameTable);
        mainTable.row();
        
        //Instantiation and configuration of "Next" button
        next = new TextButton("Next", textButtonStyle);
        next.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			if (validatePlayerEntries()==true){	//if player names have been entered appropriately
	    			if (DataManager.getOptionSelected()==1){	//if custom setting have been selected
	    				DatabaseManager.insertSession(	DataManager.getSessionID(), DataManager.getActiveAdminID(), 
	    												DataManager.getSessionDate(), DataManager.getSessionDuration(),
	    												DataManager.getCycleTime(),
	    												DataManager.getVisibilityDuration(), DataManager.getIntervalDuration(), 
	    												DataManager.getSessionTargetCount(), DataManager.getSessionParticipantCount(),
	    												0,0,0,0,0,0,0,0
	    											);	//insert data into session table
	    			}
	    			else if (DataManager.getOptionSelected()==2){	//if easy settings have been selected
	    				DatabaseManager.insertSession(	DataManager.getSessionID(), DataManager.getActiveAdminID(), 
														DataManager.getSessionDate(), DataManager.getOptimalGameDuration(),
														DataManager.getCycleTime(),
														DataManager.getOptimalVisibilityDuration(), DataManager.getOptimalIntervalDuration(), 
														DataManager.getOptimalTargetCount(), DataManager.getSessionParticipantCount(),
														0,0,0,0,0,0,0,0
	    											);	//insert data into session table
	    			}
	    			else if (DataManager.getOptionSelected()==3){	//if difficult setting has been selected
	    				DatabaseManager.insertSession(	DataManager.getSessionID(), DataManager.getActiveAdminID(), 
														DataManager.getSessionDate(), DataManager.getDifficultGameDuration(),
														DataManager.getCycleTime(),
														DataManager.getDifficultVisibilityDuration(), DataManager.getDifficultIntervalDuration(), 
														DataManager.getDifficultTargetCount(), DataManager.getSessionParticipantCount(),
														0,0,0,0,0,0,0,0
	    											);	//insert data into session table
	    			}
	    			populatePlayerTable();	//inserts data into player database
    			}
	    		else{	//if there is an error in input data, a dialog box providing appropriate feedback is generated
	    			new Dialog("", skin, "dialog") {
    					protected void result (Object object) {
    					}
    				}.text("Please enter all participants").button("Ok", true).show(stage);
	    		}    		
    			ScreenManager.getInstance().show("CONTROL_PANEL", Screens.CONTROL_PANEL);	//switch to "Control Panel" activity
    		}
    	});
        
        //Instantiation and configuration of "Back" button
    	back = new TextButton("Back", textButtonStyle);
    	back.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			ScreenManager.getInstance().show("CREATE_SESSION_TWO", Screens.CREATE_SESSION_TWO);	//switch to "Session Configuration 2" activity
    		}
    	});
    	
    	buttonTable = new Table();
    	buttonTable.add(next).spaceRight(Gdx.graphics.getWidth()/40);
    	buttonTable.add(back);
    	
    	mainTable.add(buttonTable);
    	mainTable.pack();
    	
		//Centers table in screen 	
    	int xPos = (int) (Gdx.graphics.getWidth() - mainTable.getPrefWidth()) / 2;
    	int yPos = (int) (Gdx.graphics.getHeight() - mainTable.getPrefHeight()) / 2;
    	mainTable.setPosition(xPos, yPos);
    	
    	stage.addActor(ResourceManager.getBackImg());	//attaches background image to stage
    	stage.addActor(mainTable);						//attaches main table to the activity
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
		uiButton.dispose();
		uiCursor.dispose();
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
	
	//Returns false if player name is invalid
	public boolean validatePlayerEntries(){
		TextField t=null;
		for (int i=0 ; i<textFields.size() ; i++){
			t = (TextField) textFields.get(i);
			//if player name is the default entry or empty
			if (t.getText().equals("participant " + Integer.toString(i+1)) || t.getText().equals(" ")){
				return false;
			}	
		}
		return true;
	}
	
	//Populates player table
	public void populatePlayerTable(){
		TextField t=null;
		for (int i=0 ; i<textFields.size() ; i++){
			t = (TextField) textFields.get(i);
			DatabaseManager.insertPlayer(t.getText(), DataManager.getSessionID());
		}
	}

	
}
