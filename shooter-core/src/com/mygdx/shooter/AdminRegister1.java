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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class AdminRegister1 extends CustomScreen{

	private  FreeTypeFontGenerator generator = null;
	
	private Skin skin2 = null;
	private Skin skin = null;
	private Skin textboxskin = null;
    private TextureAtlas atlas = null;
    
    private BitmapFont button = null;
    private BitmapFont tLabel = null;
    private BitmapFont fLabel = null;
    private BitmapFont field = null;
	
    private	Texture uiButton =null;
    private	Texture uiCursor =null;

	private Stage stage;
	private Stage temp;
	
	private TextButton register;
	private TextButton cancel;
	
	private Label titleLabel;
	private Label adminidLabel;
	private Label lastnameLabel;
	private Label password0Label;
	private Label password1Label;
	private Label authorizerIDLabel;
	private Label authorizerPassLabel;
	
	private TextField adminidField;
	private TextField lastnameField;
	private TextField password0Field;
	private TextField password1Field;
	private TextField authorizerIDField;
	private TextField authorizerPassField;
	
	private Table table0;
	private Table mainTable;
	
	private OrthographicCamera camera;
	
	
	@SuppressWarnings("deprecation")	//suppresses deprecation warnings generated by "generateFont()"
	public AdminRegister1(){
		
		setName("ADMIN_REG_1");			//Sets the current activity name
		
		//Implements the device's Back button functionality on current activity
    	temp = new Stage(){
    		@Override
	        public boolean keyDown(int keyCode) {
	            if (keyCode == Keys.BACK) {
	            	ScreenManager.getInstance().show("ADMIN_HOME", Screens.ADMIN_HOME);
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
        skin = ResourceManager.getDefaultSkin(); 	//fetches default skin
        atlas = ResourceManager.getAtlas();			//fetches textureAtlas
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
        
        titleLabel = new Label("Create Profile", titleSkin);	//title label instantiation
        
        Gdx.input.setInputProcessor(stage);		//attaches input process to current activity
        
        //Label instantiations for form
        adminidLabel = new Label("AdminID", formLabelSkin); 
        lastnameLabel = new Label("Lastname", formLabelSkin); 
        password0Label = new Label("Password", formLabelSkin); 
        password1Label = new Label("Verify Password", formLabelSkin); 
        authorizerIDLabel = new Label("Authorizer ID", formLabelSkin); 
        authorizerPassLabel = new Label("Authorizer Password", formLabelSkin); 
        
        //Text field instantiations and configuration for form 
        adminidField = new TextField ("", textfieldstyle);
        lastnameField = new TextField ("", textfieldstyle);
        password0Field = new TextField ("", textfieldstyle);
        password0Field.setPasswordMode(true);
        password0Field.setPasswordCharacter('*');
        password1Field = new TextField ("", textfieldstyle);
        password1Field.setPasswordMode(true);
        password1Field.setPasswordCharacter('*');
        authorizerIDField = new TextField ("", textfieldstyle);
        authorizerPassField = new TextField ("", textfieldstyle);
            
        //Instantiates tables and attaches labels, text fields and buttons
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(titleLabel);
        mainTable.row();

        table0 = new Table();
        table0.add(adminidLabel).align(Align.left).spaceRight(Gdx.graphics.getWidth()/75).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.add(adminidField).width(adminidLabel.getWidth()*(float)1.5).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.row();
        
        table0.add(lastnameLabel).align(Align.left).spaceRight(Gdx.graphics.getWidth()/75).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.add(lastnameField).width(adminidLabel.getWidth()*(float)1.5).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.row();
        
        table0.add(password0Label).align(Align.left).spaceRight(Gdx.graphics.getWidth()/75).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.add(password0Field).width(adminidLabel.getWidth()*(float)1.5).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.row();
        
        table0.add(password1Label).align(Align.left).spaceRight(Gdx.graphics.getWidth()/75).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.add(password1Field).width(adminidLabel.getWidth()*(float)1.5).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.row();
        
        table0.add(authorizerIDLabel).align(Align.left).spaceRight(Gdx.graphics.getWidth()/75).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.add(authorizerIDField).width(adminidLabel.getWidth()*(float)1.5).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.row();
      
        table0.add(authorizerPassLabel).align(Align.left).spaceRight(Gdx.graphics.getWidth()/75).spaceBottom((Gdx.graphics.getWidth()/50)*8);
        table0.add(authorizerPassField).width(adminidLabel.getWidth()*(float)1.5).spaceBottom((Gdx.graphics.getWidth()/50)*8);
    	table0.row();
    	
    	
		//Additional Widgets
        Label blankLabel = new Label("Blank", formLabelSkin); 
        blankLabel.setVisible(false);
        TextField blankField = new TextField ("", textfieldstyle);
        blankField.setVisible(false);
        table0.add(blankLabel).align(Align.left).spaceRight(Gdx.graphics.getWidth()/75).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.add(blankField).width(password1Label.getWidth()).spaceBottom(Gdx.graphics.getWidth()/50);
        table0.row();
        
        ScrollPane scroller = new ScrollPane(table0);
        mainTable.add(scroller).fill().expand();
        mainTable.row();
    	
        Table table1 = new Table();
    	
    	//Instantiation and configuration of "Register" button
        register = new TextButton("Confirm", textButtonStyle);
        register.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			boolean status = false;
    			if (fieldsCompleted()==true){	//if all fields have data
	    			if (DatabaseManager.administratorExists(adminidField.getText())==false){	//if administrator id is unique
	    				if(DatabaseManager.authorizerConfirmed(authorizerIDField.getText(), authorizerPassField.getText())==true){	//if root administrator credentials are correct
			    			if (password0Field.getText().equals(password1Field.getText())){		//if both password entries are consistent
				    			status = DatabaseManager.insertAdministrator(adminidField.getText(), lastnameField.getText(), password0Field.getText());	//insert administrator record in database
				    			ScreenManager.getInstance().show("ADMIN_HOME", Screens.ADMIN_HOME);		//switch to "Administrator Home" activity
				    		}
				    		else {	//if both password entries are not consistent, a dialog box provides the appropriate feedback
				    			new Dialog("", skin, "dialog") {
			    					protected void result (Object object) {
			    					}
			    				}.text("Passwords don't match").button("Ok", true).show(stage);
				    		}
	    				}
	    				else {
	    					new Dialog("", skin, "dialog") {	//if root administrator credentials are incorrect, a dialog box provides the appropriate feedback
		    					protected void result (Object object) {
		    					}
		    				}.text("Invalid authorizer credentials").button("Ok", true).show(stage);	
	    				}
	    			}
	    			else {	//if administrator id already exists, a dialog box provides the appropriate feedback
		    			new Dialog("", skin, "dialog") {
	    					protected void result (Object object) {
	    					}
	    				}.text("Administrator ID already exists").button("Ok", true).show(stage);
		    		}
    			}
    			else{	//if all fields are not completed, a dialog box provides the appropriate feedback
    				new Dialog("", skin, "dialog") {
    					protected void result (Object object) {
    					}
    				}.text("Please complete blank fields").button("Ok", true).show(stage);
    			}
    		}
    	});
        
        //Instantiation and configuration of "Cancel" button
    	cancel = new TextButton("Cancel", textButtonStyle);
    	cancel.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			ScreenManager.getInstance().show("ADMIN_HOME", Screens.ADMIN_HOME);
    		}
    	});
    	
    	table1.add(register).spaceTop(Gdx.graphics.getWidth()/50).spaceRight(Gdx.graphics.getWidth()/25);
    	table1.add(cancel).spaceTop(Gdx.graphics.getWidth()/50);;
    	
    	mainTable.add(table1);

    	stage.addActor(ResourceManager.getBackImg());	//attaches background image to stage
    	stage.addActor(mainTable);						//attaches main table to the activity
	}
	    
	
	//Returns false if any field is left empty
	public boolean fieldsCompleted(){
		if (adminidField.getText().equals("") || lastnameField.getText().equals("") || 
			password0Field.getText().equals("") || password1Field.getText().equals("") || 
			authorizerIDField.getText().equals("") ||  authorizerPassField.getText().equals("")){
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
	public void show() {
	}

	
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
	public void pause() {
	}

	
	@Override
	public void resume() {}

	
	@Override
	public void dispose() {}
	
}


