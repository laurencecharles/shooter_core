package com.mygdx.shooter;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class First extends Game {

	@Override
	//Application entry point
	public void create() {
		System.out.println("Application Started");
		ScreenManager.getInstance().initialize(this);
		ResourceManager.getInstance().initialize(this);
		DataManager.getInstance().initialize(this);
		DatabaseManager.getInstance().initialize(this);
		ScreenManager.getInstance().show("INTRO", Screens.INTRO);
		Gdx.input.setCatchBackKey(true);
		
	}
	
//	public void render() {
//       super.render(); //important!
//    }

	@Override
	public void dispose() {
		super.dispose();
	}
	
}







