package com.mygdx.shooter;

import java.util.LinkedList;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;


public final class ScreenManager {

    private static ScreenManager instance=null;
    private Game game;
    public static String username;
    private LinkedList<CustomScreen> screens;
 
    private ScreenManager() {
    	screens = new LinkedList<CustomScreen>();
    }
 
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }
 
    public void initialize(Game game) {
        this.game = game;
    }

    //Displays the requested activity
    public void show(String name, Screens screen) {
    	CustomScreen temp = null;
    	if (game == null) return;
    	boolean found=false;
    	int index=-1;
    	//Checks if screen exists
    	for(int i=0 ; i<screens.size() ; i++){
    		if (screens.get(i).getName().equals(screen)){
    			found=true;
    			index=i;
    			break;
    		}
    	}
    	//Creates screen and adds to collection if it doesn't previously exist
    	if (found == false){
    		temp = (CustomScreen) screen.getScreenInstance();
    		screens.add(temp);
    		index = screens.indexOf(temp); 
    	}
    	
    	Gdx.input.setInputProcessor(temp.getStage());	//attaches input process to current activity

    	game.setScreen(screens.get(index));
    }
    
    //Disposes resources of the current activity to mitigate memory leaks
    public void dispose(String screen) {
        boolean found=false;
    	int index=-1;
    	//Checks if screen exists
    	for(int i=0 ; i<screens.size() ; i++){
    		if (screens.get(i).getName().equals(screen)){
    			found=true;
    			index=i;
    			break;
    		}
    	}
    	if(found == false)
    		return;
    	screens.remove(index);
        
    }
  
    //Disposes resources of all activities to mitigate memory leaks
    public void dispose() {
        for (CustomScreen screen : screens) {
            screen.dispose();
        }
        screens.clear();
        instance = null;
    }
    
    public static void setUsername(String name){
    	username = name;
    }
    
    public static void resetUsername(){
    	username = "";
    }
    
    public static String getUsername(){
    	return username;
    }

}