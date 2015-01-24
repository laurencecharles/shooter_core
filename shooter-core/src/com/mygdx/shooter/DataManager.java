package com.mygdx.shooter;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public final class DataManager {
	 
    private static DataManager instance;
 
    private Game game;
    
    //Needed for transition from 'PlayerHome' -> 'PlayerProfile'
    private static String activeAdminID;
    
    
    //Needed for transition from 'PlayerHome' -> 'PlayerProfile'
    public static String username;
    
    //Needed to keep track of active session and needed for 'Session1' -> 'Session2' transition
    private static String sessionID=null;
    
    //Needed for 'Session1' -> 'Session2' transition
    private static String sessionDate=null;
    private static int sessionParticipantCount=0;
    private static int cycleTime = 0;
    
    //Needed for 'Session2' -> 'Session3' transition
    private static int sessionDuration=0;
    private static int visibilityDuration=0;
    private static int intervalDuration=0;
    private static int sessionTargetCount=0;

    private static int optionSelected=0;
    
    //Stores optimum settings
    private static int optimalGameDuration=0;
    private static int optimalTargetCount=0;
    private static int optimalVisibilityDuration=0;
    private static int optimalIntervalDuration=0;

    //Stores most difficult settings
    private static int difficultGameDuration=0;
    private static int difficultTargetCount=0;
    private static int difficultVisibilityDuration=0;
    private static int difficultIntervalDuration=0;
   
    private static Texture background = null;
    private static Image backImg = null;
    
    
    private DataManager() {
    	background = new Texture(Gdx.files.internal("ui/desert_landscape.png"));
        backImg = new Image(background);
        backImg.setScaling(Scaling.fit);
    }
    
    public static Image getBackground(){
    	return backImg;
    }
 
    
    public static DataManager getInstance() {
        if (null == instance) {
            instance = new DataManager();
        }
        return instance;
    }
 
    public void initialize(Game game) {
        this.game = game;
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

	public static String getSessionID() {
		return sessionID;
	}
	
	public static void resetSessionID(){
    	sessionID = "";
    }

	public static void setSessionID(String id) {
		sessionID = id;
	}

	public static String getSessionDate() {
		return sessionDate;
	}

	public static void setSessionDate(String date) {
		sessionDate = date;
	}
	
	public static void resetSessionDate(){
    	sessionID = "";
    }

	public static int getSessionDuration() {
		return sessionDuration;
	}

	public static void setSessionDuration(int dur) {
		sessionDuration = dur;
	}
	
	public static void resetSessionDuration(){
		sessionDuration=0;
	}
	
	public static int getSessionTargetCount() {
		return sessionTargetCount;
	}

	public static void setSessionTargetCount(int count) {
		sessionTargetCount = count;
	}
	
	public static void resetSessionTargetCount(){
		sessionTargetCount=0;
	}

	public static int getSessionParticipantCount() {
		return sessionParticipantCount;
	}

	public static void setSessionParticipantCount(int sessionPrticipantCount) {
		sessionParticipantCount = sessionPrticipantCount;
	}
	
	public static void resetSessionParticipantCount(){
		sessionParticipantCount=0;
	}

	public static int getIntervalDuration() {
		return intervalDuration;
	}
	
	public static void setIntervalDuration(int dur) {
		intervalDuration = dur;
	}
	
	public static void resetIntervalDuration() {
		intervalDuration = 0;
	}
	
	public static int getVisibilityDuration() {
		return visibilityDuration;
	}

	public static  void setVisibilityDuration(int val) {
		visibilityDuration = val;
	}
	
	public static  void resetVisibilityDuration() {
		visibilityDuration = 0;
	}


	public static String getActiveAdminID() {
		return activeAdminID;
	}

	public static void setActiveAdminID(String id) {
		activeAdminID = id;
	}
	
	 public static void resetActiveAdminID(){
		 activeAdminID = "";
	 }

	public static int getOptionSelected() {
		return optionSelected;
	}

	public static void setOptionSelected(int val) {
		optionSelected = val;
	}

	public static int getOptimalGameDuration() {
		return optimalGameDuration;
	}

	public static void setOptimalGameDuration(int optimalGameDuration) {
		DataManager.optimalGameDuration = optimalGameDuration;
	}

	public static int getOptimalTargetCount() {
		return optimalTargetCount;
	}

	public static void setOptimalTargetCount(int optimalTargetCount) {
		DataManager.optimalTargetCount = optimalTargetCount;
	}

	public static int getOptimalVisibilityDuration() {
		return optimalVisibilityDuration;
	}

	public static void setOptimalVisibilityDuration(
			int optimalVisibilityDuration) {
		DataManager.optimalVisibilityDuration = optimalVisibilityDuration;
	}

	public static int getOptimalIntervalDuration() {
		return optimalIntervalDuration;
	}

	public static void setOptimalIntervalDuration(int optimalIntervalDuration) {
		DataManager.optimalIntervalDuration = optimalIntervalDuration;
	}
	
	public static int getDifficultGameDuration() {
		return difficultGameDuration;
	}

	public static void setDifficultGameDuration(int difficultGameDuration) {
		DataManager.difficultGameDuration = difficultGameDuration;
	}

	public static int getDifficultTargetCount() {
		return difficultTargetCount;
	}

	public static void setDifficultTargetCount(int difficultTargetCount) {
		DataManager.difficultTargetCount = difficultTargetCount;
	}

	public static int getDifficultVisibilityDuration() {
		return difficultVisibilityDuration;
	}

	public static void setDifficultVisibilityDuration(
			int difficultVisibilityDuration) {
		DataManager.difficultVisibilityDuration = difficultVisibilityDuration;
	}

	public static int getDifficultIntervalDuration() {
		return difficultIntervalDuration;
	}

	public static void setDifficultIntervalDuration(
			int difficultIntervalDuration) {
		DataManager.difficultIntervalDuration = difficultIntervalDuration;
	}

	public static int getCycleTime() {
		return cycleTime;
	}

	public static void setCycleTime(int time) {
		cycleTime = time;
	}
	
	public static void resetCycleTime() {
		cycleTime = 0;
	}
	
	public static void resetSessionOne(){
		resetSessionID();
		resetSessionDuration();
		resetSessionDate();
		resetSessionTargetCount();
		resetSessionParticipantCount();
		resetIntervalDuration();
		resetVisibilityDuration();
	}

}
