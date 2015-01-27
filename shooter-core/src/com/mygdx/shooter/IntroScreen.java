package com.mygdx.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;


public class IntroScreen extends CustomScreen {
	
	private Texture texture1;
	private Image pic;
	
	private Stage stage;
	private Stage temp;
	
	private Table table;
	
	private long creationTime;
    
	private OrthographicCamera camera;

	
    public IntroScreen() {

    	setName("INTRO");	//Sets the current activity name 	
    	
    	temp = new Stage();
    	setStage(temp);
    	stage = getStage();	
    	
    	creationTime = TimeUtils.nanoTime();	//set initialization time of current activity

    	//Configures camera settings
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
   
        texture1 = new Texture(Gdx.files.internal("ui/spash.png"));		//loads display image file
        pic = new Image(texture1);

    	Gdx.input.setInputProcessor(stage);	//Sets the current activity name

    	//Instantiates tables and attaches images
    	table = new Table();
    	table.add(pic);
    	table.pack();
    	
    	stage.addActor(table);	//attaches main table to the activity
    }

    
    @Override
    public void render(float delta) {	
    	Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    	stage.draw();
    	Table.drawDebug(stage);
    }

    
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		table.setSize(width, height);
	}

	
	@Override
	public void show() {
		Timer.schedule(new ScreenSwitchTask(Screens.MAIN_MENU), 2f);	//schedule to show main menu screen after 3 seconds	}
	}
	
	@Override
	//Disposes resources to mitigate memory leaks
	public void hide() {
		texture1.dispose();
		stage.dispose();
	}

	
	@Override
	public void pause() {}

	
	@Override
	public void resume() {}

	
	@Override
	public void dispose() {}
	
}




