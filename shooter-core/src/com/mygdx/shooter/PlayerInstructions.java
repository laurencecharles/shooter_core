package com.mygdx.shooter;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class PlayerInstructions extends CustomScreen {

	private  FreeTypeFontGenerator generator = null;
	
	private Skin skin2 = null;
	private TextureAtlas atlas = null;
	
	private BitmapFont header1 = null;
	private BitmapFont header2 = null;
	private BitmapFont header3 = null;
	private BitmapFont txt = null;
	private BitmapFont button = null;
	
	private Stage stage;
	private Stage temp;
	
	private TextButton back;
	
    private OrthographicCamera camera;

    
    @SuppressWarnings("deprecation")		//suppresses deprecation warnings generated by "generateFont()"
	public PlayerInstructions() {
    	
    	setName("PLAYER_INSTRUCTIONS");		//Sets the current activity name	
    	
    	//Implements the device's Back button functionality on this activity
    	temp = new Stage(){
    		@Override
	        public boolean keyDown(int keyCode) {
	            if (keyCode == Keys.BACK) {
	            	ScreenManager.getInstance().show("PLAYER_PROFILE", Screens.PLAYER_PROFILE);
	            }
	            return super.keyDown(keyCode);
    		}
	    };
    	setStage(temp);
    	stage = getStage();
    	
    	Gdx.input.setInputProcessor(stage);		//Implements the device's Back button functionality on this activity
      
    	//Configures camera settings
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        generator = ResourceManager.getGenerator();	//fetches font generator
        atlas = ResourceManager.getAtlas();			//fetches textureAtlas
        skin2 = ResourceManager.getCustomSkin();	//fetches customized skin
        
        //Generates font size for objects containing text
        header1 = generator.generateFont(Gdx.graphics.getWidth()/10);
        header2 = generator.generateFont(Gdx.graphics.getWidth()/25);
        header3 = generator.generateFont(Gdx.graphics.getWidth()/35);
        txt = generator.generateFont(Gdx.graphics.getWidth()/45);
        button = generator.generateFont(Gdx.graphics.getWidth()/20);
       
        //Label style instantiations
        LabelStyle header1Skin = new LabelStyle();
        LabelStyle header2Skin = new LabelStyle();
        LabelStyle textSkin = new LabelStyle();
        LabelStyle header3Skin = new LabelStyle();
        
        //Sets font size of various label styles
        header1Skin.font = header1;  
        header2Skin.font = header2;     
        textSkin.font = txt;
        header3Skin.font = header3;      
        
        Label titleLabel = new Label("Instructions", header1Skin);	//title label instantiation
        
        //Instantiations and configurations associated with game play instructions
        Label playGame = new Label("Play Game",  header3Skin);
        String playInstructions =  	 	"Player is first require to log into the game using the credentials     \n"
										+"provided to them by the medical asessment personel.                     ";
        final Label play = new Label(playInstructions, textSkin);
        play.setAlignment(Align.center);
        play.setWrap(true);
        
        //Configurations of text button style
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin2.getDrawable("button.up");
        textButtonStyle.down = skin2.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 3;
        textButtonStyle.pressedOffsetX = -3;
        textButtonStyle.font= button;
       
      //Instantiation and configuration of "Back" button
        back = new TextButton("Back", textButtonStyle);
    	back.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			ScreenManager.getInstance().show("PLAYER_PROFILE", Screens.PLAYER_PROFILE);	//switch to "Player Profile" activity
    		}
    	});
    	
    	//Instantiates scroll table and attaches labels, text fields and buttons
        final Table scrollTable = new Table();
        scrollTable.add(titleLabel);
        scrollTable.row();
        
        scrollTable.add(playGame);
        scrollTable.row();
        scrollTable.add(play).spaceBottom(Gdx.graphics.getWidth()/50);
        scrollTable.row();
       
        scrollTable.add(back);

        final ScrollPane scroller = new ScrollPane(scrollTable);

        final Table table = new Table();
        table.setFillParent(true);
        table.add(scroller).fill().expand();
        
        stage.addActor(DataManager.getBackground());	//attaches background image to stage
        stage.addActor(table);							//attaches main table to the activity
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
		header1.dispose();
        header2.dispose();
        header3.dispose();
        txt.dispose();
        button.dispose();
        stage.dispose();
	}

	
	@Override
	public void pause() {}

	
	@Override
	public void resume() {}

	
	@Override
	public void dispose() {}

}

