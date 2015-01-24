package com.mygdx.shooter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CustomScreen implements Screen{
	
	private String screenName;
	private Stage st;
	
	public CustomScreen(){
	}

	@Override
	public void render(float delta) {}

	
	@Override
	public void resize(int width, int height) {}

	
	@Override
	public void show() {}

	
	@Override
	public void hide() {}

	
	@Override
	public void pause() {}

	
	@Override
	public void resume() {}

	
	@Override
	public void dispose() {}
	
	
	public void setName(String name){
		screenName = name;
	}
	
	
	public String getName(){
		return screenName;
	}
	
	
	public Stage getStage(){
		return st;
	}
	
	
	public void setStage(Stage s){
		st = s;
	}
	
}
