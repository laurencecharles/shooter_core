package com.mygdx.shooter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;


public class GameScreen extends CustomScreen {

	private Stage stage;

	private Texture background = null;
	private Image backImg=null;
	
	//Stores target and barrier objects
	private ArrayList<Target> targets;	
	private ArrayList<Target> scriptedTargets = null; 
	private ArrayList<Barrier> barriers;

    private OrthographicCamera camera;
    
    private boolean sessionStarted;
    
    private long creationTime;		//stores the time of initialization of the current activity
    private long timeStamp;			//stores the current time
  
    private float xOrigin;			//stores the x coordinate of the activity's screen 
    private float yOrigin;			//stores the y coordinate of the activity's screen 
    private float quadrantWidth;
    private float quadrantHeight;
    
    private int[][] coordinates;	//stores the current positions of each barrier
    private int targetCount=0;		//stores counter of initialized targets
    
    private long targetCreationTime=0;
    private long visibilityDuration;
    private long maxTimeBetweenTargets;
    private int randInterval;
    private long removalTimeStamp;
   
    //Variables to facilitate standardized session configurations
    private long gameDuration;
    private long cycleTime;
    private int noTargets;
    private int standingTargets;
    private int crouchingTargets;
    private int layingTargets;
    private int standingBehindHighBarrier;
    private int standingBehindMidBarrier;
    private int standingUnhidden;
    private int crouchingBehindMidBarrier;
    private int crouchingUnhidden;
        
    //Stores target/barrier occurrences
    private int standingHighCount=0;
	private int standingMidCount=0;
	private int standingUnhiddenCount=0;
	private int crouchingMidCount=0;
	private int crouchingUnhiddenCount=0;
	
	private String currentSession;
	private String[] configurations = new String[17];
	
	
    public GameScreen() {
  	
    	setName("MAIN_GAME_SCREEN");		//Sets the current activity name
    	
    	creationTime = System.currentTimeMillis(); 	//sets creation time of current activity  	
    	timeStamp = creationTime;
    	
    	xOrigin = 0;
    	yOrigin = 0;
    	
    	//Sets the height and width of each of the 20 quadrants
    	quadrantHeight = (float) (Gdx.graphics.getHeight()/4);
    	quadrantWidth = (float) (Gdx.graphics.getWidth()/5);
	
    	coordinates = new int[20][2];
    	
    	stage = new Stage();	//stage initialization
    	
        Gdx.input.setInputProcessor(stage);	//attaches input process to current activity
    	
    	currentSession= DatabaseManager.getLastSessionID();		//determines current session
   
    	//Load the session configuration from the database
    	configurations = DatabaseManager.loadSessionConfigurations(currentSession);
    	for(int i=0 ; i<17 ; i++){
    		System.out.println("configs[" + i + "] = " + configurations[i] + "\n");
    	}
    	
    	gameDuration = (Integer.parseInt(configurations[3])) * 1000;		//sets the session's duration
    	cycleTime = (Integer.parseInt(configurations[4])) * 1000;			//sets the time between barrier image repositioning
    	visibilityDuration = (Integer.parseInt(configurations[5])) * 1000;	//sets the mix visibility duration of each target
    	maxTimeBetweenTargets = (Integer.parseInt(configurations[6])*1000);	//sets the maximum duration between each target
    	noTargets = Integer.parseInt(configurations[7]);					//sets the number of targets in the current session
    	
    	//Loads the background image file and configures its size
    	background = new Texture(Gdx.files.internal("sprites/landscape3.png"));	
        backImg = new Image(background);
        backImg.setScaling(Scaling.fit);
         
        stage.addActor(backImg);	//attaches background image to the activity
              
    	targets = new ArrayList<Target>(noTargets);		//initializes target collection
    	
    	ResourceManager.createSoldierImages();			//loads images of soldiers
    	
    	barriers = new ArrayList<Barrier>(20);			//initializes barrier collection
    	ResourceManager.createBarrierImages();			//loads images of barriers
    	createBarriers(20);								//creates 20 barrier objects
    	coordinates = generateOrigins(20);				//generates random locations for each barrier within it's quadrant
    	updateBarrierLocations(coordinates, 20);		//positions barrier
    	addBarriers();									//add barriers to the display

    	sessionStarted =  DatabaseManager.sessionExists(currentSession);	//fetches the current active session
    	if (sessionStarted == true){	//if this is not the initial game in the session load saved target/barrier occurrences
    		scriptedTargets = createScriptedTargets(Integer.parseInt(configurations[9]), 
    												Integer.parseInt(configurations[10]), 
    												Integer.parseInt(configurations[11]));
    		standingBehindHighBarrier = Integer.parseInt(configurations[12]);
    		standingBehindMidBarrier = Integer.parseInt(configurations[13]);
    		standingUnhidden = Integer.parseInt(configurations[14]);
    		crouchingBehindMidBarrier = Integer.parseInt(configurations[15]);
    		crouchingUnhidden = Integer.parseInt(configurations[16]);
    		targetCreationTime = spawnScriptedTarget(targetCount);	//spawns first target of the game
    	}
    	else{	//if this is the initial game in the current session
    		createTargets(noTargets);						//creates targets
    		targetCreationTime = spawnTarget(targetCount);	//spawns first target of the game
    		
    	}
    	
    	//Configures camera settings
        camera = new OrthographicCamera();			
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

  
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();		 // tell the camera to update its matrices.

    	//Cycles barriers every x seconds
    	if (System.currentTimeMillis() - timeStamp > cycleTime){
    		coordinates = generateOrigins(barriers.size());
    		changeBarrierImages(barriers.size());
        	updateBarrierLocations(coordinates, barriers.size());
        	timeStamp = System.currentTimeMillis();		//resets timestamp
    	}
    	
    	//Removes target if target time on screen elapses
    	Target target=null;
    	if(targetCreationTime!=0  && System.currentTimeMillis() - targetCreationTime >= getVisibilityDuration()){
    		
    		if(sessionStarted==false)						//if this is the initial game in the session
    			target = targets.get(targetCount);			//retrieve next pending target
    		else if (sessionStarted==true)					//if this is not the initial game in the session
    			target = scriptedTargets.get(targetCount);	//retrieve next pending target
    		target.setMiss(1);		//indicates that target was missed
    		target.remove();		//removes target from screen
    		incrementTargetCount();
    		removalTimeStamp = System.currentTimeMillis();	//stores time of target removal
    		randInterval = (int) (MathUtils.random(2000, getMaxTimeBetweenTargets()));	//generates duration until next target spawning
    		targetCreationTime = 0;		//resets the target creation time
    		
    		//If all scheduled targets has been spawned or the game duration is exceeded
    		if ((sessionStarted==false && targetCount==targets.size())  || (sessionStarted==true && targetCount==scriptedTargets.size())   || System.currentTimeMillis()- creationTime> gameDuration){
    			if (sessionStarted == true){	
    				processResults(scriptedTargets);	//writes results out to the Result table
    				DatabaseManager.outputResults();
    			}
    			else{
    				processResults(targets);	//writes results out to the Result table
    				DatabaseManager.outputResults();
    				DatabaseManager.updateSession(currentSession, standingTargets, crouchingTargets, layingTargets, standingBehindHighBarrier, standingBehindMidBarrier, standingUnhidden, crouchingBehindMidBarrier, crouchingUnhidden);	//update the session table to facilitate uniformity between sessions
    			}
    			ScreenManager.getInstance().show("GAME_OVER", Screens.GAME_OVER);	//switch to "Game Over" screen
    		}
    	}
    	    	
    	//Spawns new target if current target was removed
    	if (targetCreationTime==0  && System.currentTimeMillis() -  removalTimeStamp > randInterval){	//if duration between targets is exceed
    		if (sessionStarted == true && getTargetCount()< scriptedTargets.size()){	//if pending targets still exist
    			targetCreationTime = spawnScriptedTarget(targetCount);
    		}
    		else if (sessionStarted==false  && getTargetCount() < targets.size()){		//if pending targets still exist
    			targetCreationTime = spawnTarget(targetCount);
        	}
    	}
    	
    	stage.act(Gdx.graphics.getDeltaTime());
    	stage.draw();
    	
    	if (Gdx.input.isTouched() && targetCount<noTargets) {	//if a touch event is detected and pending targets still exist
    		Target t=null;
    		if(sessionStarted==false)
    			t = targets.get(targetCount);
    		else if (sessionStarted==true)
    			t = scriptedTargets.get(targetCount);
    		
    		//Check if target was hit
			if (t.hit(Gdx.input.getX(), Gdx.input.getY(), true)!=null){
				Gdx.input.vibrate(500);					//vibrate device for .5 seconds
				if (t.isHit()==0)						//ensure only one printing of statement
					System.out.println("Target: " + t.getID() + "has been hit\n\n");

				t.setHit(1);
				t.setHitTime(System.currentTimeMillis());
				t.calculateReactionTime();		
    			t.remove();
    			incrementTargetCount();
					
				if ((sessionStarted==false && targetCount==targets.size())  || (sessionStarted==true && targetCount==scriptedTargets.size())){	//if there are no pending targets
	    			if (sessionStarted == true){
	    				processResults(scriptedTargets);
	    			}
	    			else{
	    				processResults(targets);
	    				DatabaseManager.updateSession(currentSession, standingTargets, crouchingTargets, layingTargets, standingBehindHighBarrier, standingBehindMidBarrier, standingUnhidden, crouchingBehindMidBarrier, crouchingUnhidden);
	    			}	   		
		    		ScreenManager.getInstance().show("GAME_OVER", Screens.GAME_OVER);	//switch to "Game Over" screen
		    	}
				else{	//if there are no pending targets
    				targetCreationTime=0;
    		    	removalTimeStamp = System.currentTimeMillis();
    		    	randInterval = (int) (MathUtils.random(2000, getMaxTimeBetweenTargets()));
				}
			}
		}
	}

    
    @Override
    public void resize(int width, int height) {
    }

    
    @Override
    public void show() {
    }

    
    @Override
    //Disposes resources to mitigate memory leaks
    public void hide() {
    	stage.dispose();
    }

    
    @Override
    public void pause() {}

    
    @Override
    public void resume() {}

    
    @Override
    public void dispose() {}
    
 
	public float getxOrigin() {
		return xOrigin;
	}

	public void setxOrigin(int xOrigin) {
		this.xOrigin = xOrigin;
	}

	public float getyOrigin() {
		return yOrigin;
	}

	public void setyOrigin(int yOrigin) {
		this.yOrigin = yOrigin;
	}

	public float getQuadrantHeight() {
		return quadrantHeight;
	}

	public void setQuadrantHeight(int quadrantHeight) {
		this.quadrantHeight = quadrantHeight;
	}

	public float getQuadrantWidth() {
		return quadrantWidth;
	}

	public void setQuadrantWidth(int quadrantWidth) {
		this.quadrantWidth = quadrantWidth;
	}
	
	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
	public long getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(long gameDuration) {
		this.gameDuration = gameDuration;
	}

	public long getVisibilityDuration() {
		return visibilityDuration;
	}

	public void setVisibilityDuration(long visibilityDuration) {
		this.visibilityDuration = visibilityDuration;
	}
	
	public long getMaxTimeBetweenTargets() {
		return maxTimeBetweenTargets;
	}

	public void setMaxTimeBetweenTargets(long maxTimeBetweenTargets) {
		this.maxTimeBetweenTargets = maxTimeBetweenTargets;
	}

	public int getNoTargets() {
		return noTargets;
	}

	public void setNoTargets(int noTargets) {
		this.noTargets = noTargets;
	}

	public int getStandingTargets() {
		return standingTargets;
	}

	public void setStandingTargets(int standingTargets) {
		this.standingTargets = standingTargets;
	}

	public int getCrouchingTargets() {
		return crouchingTargets;
	}

	public void setCrouchingTargets(int crouchingTargets) {
		this.crouchingTargets = crouchingTargets;
	}

	public int getLayingTargets() {
		return layingTargets;
	}

	public void setLayingTargets(int layingTargets) {
		this.layingTargets = layingTargets;
	}

	public int getStandingBehindHighBarrier() {
		return standingBehindHighBarrier;
	}

	public void setStandingBehindHighBarrier(int standingBehindHighBarrier) {
		this.standingBehindHighBarrier = standingBehindHighBarrier;
	}

	public int getStandingBehindMidBarrier() {
		return standingBehindMidBarrier;
	}

	public void setStandingBehindMidBarrier(int standingBehindMidBarrier) {
		this.standingBehindMidBarrier = standingBehindMidBarrier;
	}
	
	public int getStandingUnhidden() {
		return standingUnhidden;
	}

	public void setStandingUnhidden(int standingUnhidden) {
		this.standingUnhidden = standingUnhidden;
	}

	public int getCrouchingBehindMidBarrier() {
		return crouchingBehindMidBarrier;
	}

	public void setCrouchingBehindMidBarrier(int crouchingBehindMidBarrier) {
		this.crouchingBehindMidBarrier = crouchingBehindMidBarrier;
	}

	public int getCrouchingUnhidden() {
		return crouchingUnhidden;
	}

	public void setCrouchingUnhidden(int crouchingUnhidden) {
		this.crouchingUnhidden = crouchingUnhidden;
	}
    
    public int getTargetCount() {
		return targetCount;
	}

	public void setTargetCount(int targetCount) {
		this.targetCount = targetCount;
	}
	
	public void incrementTargetCount(){
		targetCount++;
	}
   
	
	//Adds targets to stage
    public void addTargets(){
    	for (Target t: targets){
    		stage.addActor((Actor)t);
    	}
    }
    
    
	//Creates barrier objects
	public void createBarriers(int count){
		Barrier b = null;
		Random rand = new Random(System.currentTimeMillis());
		int barrierType;
		
		for(int i=0 ; i<count ; i++){
			if (i==0){	//ensures at least one medium height barrier exists
				barrierType = 2;
			}
			else{
				barrierType = rand.nextInt(4);	//randomly choice from 4 types of barrier
			}
			b = new Barrier (barrierType,i, 0, 0 );
			barriers.add(b);
		}
	}
	
	
	//Creates barrier objects
	public void changeBarrierImages(int count){
		Barrier b = null;
		Random rand = new Random(System.currentTimeMillis());
		int barrierType;
		
		for(int i=0 ; i<count ; i++){
			barrierType = rand.nextInt(4);	//randomly choice from 4 types of barrier
			b = barriers.get(i);
			b.setType(barrierType);
			if (barrierType==0){
				b.setTexture(new Texture(Gdx.files.internal("sprites/sBarrier_1.png")));	//set graphical representation of barrier
			}
			else if (barrierType==1){
				b.setTexture(new Texture(Gdx.files.internal("sprites/sBarrier_2.png")));	//set graphical representation of barrier
			}
			else if (barrierType==2){
				b.setTexture(new Texture(Gdx.files.internal("sprites/cBarrier_1.png")));	//set graphical representation of barrier
			}
			else if (barrierType==3){
				b.setTexture(new Texture(Gdx.files.internal("sprites/cBarrier_2.png")));	//set graphical representation of barrier
			}
			//Set barriers current dimensions
			b.setActorHeight(b.getTexture().getHeight());
			b.setActorWidth(b.getTexture().getWidth());
			
			//Sets the original dimensions of the barriers
			b.setOriginalHeight(b.getTexture().getHeight());
			b.setOriginalWidth(b.getTexture().getWidth());
//			setCreationTime(TimeUtils.nanoTime());	//set the creation time of the objects
		}
	}
	
	
	//Given an index; returns the row and column of that quadrant
	public int[] determineRowCol(int index){
		int row, column;
		int[] result = new int[2];
		row = index / 5;
		column = index % 5;
		result[0]= row;
		result[1] = column;
		return result;
	}
	
	
	//Given an index it returns the boundaries of that quadrant
	public int[] calculateBoundaries(int index){
		int[] boundaries = new int[4];
		
		int[] quadLocation = new int[2];
		quadLocation = determineRowCol(index);
		
		int row = quadLocation[0];
		int column = quadLocation[1];
		
		boundaries[0] = (int) (xOrigin + (column * quadrantWidth));	//calculates left boundary of quadrant
		boundaries[1] = (int) (boundaries[0] + quadrantWidth);		//calculates right boundary of quadrant
		if (row==0){
			boundaries[2] = (int)  (Gdx.graphics.getHeight() - (quadrantHeight/2));	//calculates lower boundary of quadrant
		}
		else {
			boundaries[2] = (int) (Gdx.graphics.getHeight() - (row * quadrantHeight) - (quadrantHeight/2));	//calculates upper boundary of quadrant
		}	
		boundaries[3] = (int) (Gdx.graphics.getHeight() - (row * quadrantHeight) - quadrantHeight);	//calculates lower  boundary of quadrant
		
		return boundaries;
	}
	
	
	//Returns an array of x and y coordinates for each Barrier
	public int[][] generateOrigins(int count){
		
		int origins[][] = new int[20][2];
		int width;

		Barrier b = null;
		int[] bounds = new int[4];
		
		for (int i=0 ; i<count ; i++){
			b = barriers.get(i);
			width = (int) b.getOriginalWidth();
			bounds =calculateBoundaries(i);	//stores the boundaries of the barrier

			origins[i][0] = (int) MathUtils.random(bounds[0], bounds[1] - width);	//generates a random x coordinate
			origins[i][1] = (int) MathUtils.random(bounds[3], bounds[2]);			//generates a random y coordinate
		}
		return origins;
	}
	
	
	//Updates the barrier locations
	public void updateBarrierLocations(int[][] locs, int count){
		Barrier b = null;
		float[] dimensions = new float[2];
		
		for (int i=0; i<count ; i++){
			b = (Barrier) barriers.get(i);

			dimensions= b.scaleBarrier(i, b.getOriginalWidth(), b.getOriginalHeight());
				
			b.setActorWidth((int) dimensions[0]);
			b.setActorHeight((int) dimensions[1]);
			
			b.setActorX(locs[i][0]);
			b.setActorY(locs[i][1]);
			b.setBounds(b.getActorX(),b.getActorY(),b.getActorWidth(), b.getActorHeight());
		}
	}
	
	//Returns the row of an object
	public int getRow(float yValue){
		int row=0;
		row =  (int) ( (Gdx.graphics.getHeight() - yValue) / quadrantHeight);
		return row;
	}
	
	
    //Adds barriers to stage
    public void addBarriers(){
    	for (Barrier b: barriers){
    		stage.addActor(b);
    	}
    }

    
	//Creates targets of randomly assigned types (for first game of each session)
	public void createTargets(int count){
		
		Target t = null;
		Random rand = new Random(System.currentTimeMillis());
		int targetType;
		
		for(int i=0 ; i<count ; i++){
			targetType = rand.nextInt(3);	
			if (targetType==0){			//if target type is standing
				standingTargets++;
			}
			else if (targetType==1){	//if target type is crouching
				crouchingTargets++;
			}
			else if (targetType==2){	//if target type is lying
				layingTargets++;
			}
			t = new Target (targetType, i, 2000, 2000 );	//instantiate target 
			targets.add(t);		//add target to arraylist
		}
	}
	
	
	//Returns a collection of targets with the same target/barrier combinations as the initial game of the session
	public ArrayList<Target> createScriptedTargets(int standing, int crouching, int laying){
		
		int count = standing +crouching + laying;
		ArrayList<Target> scriptedTargets = new ArrayList<Target>(count);	//collection of all targets
		ArrayList<Target> stand = new ArrayList<Target>(standing);			//collection of standing targets
		ArrayList<Target> crouch = new ArrayList<Target>(crouching);		//collection of crouching targets
		ArrayList<Target> lay = new ArrayList<Target>(laying);				//collection of laying targets
		Target t = null;
		
		//Instantiate and store standing targets
		for (int i=0; i<standing ; i++){		
			t = new Target (0, -1, 2000, 2000 );
			stand.add(t);
		}
		
		//Instantiate and store crouching targets
		for (int i=0; i<crouching ; i++){		
			t = new Target (1, -1, 2000, 2000 );
			crouch.add(t);
		}
		
		//Instantiate and store laying targets
		for (int i=0; i<laying ; i++){
			t = new Target (2, -1, 2000, 2000 );
			lay.add(t);
		}
	
		int stCount=0;	//counter for standing targets
		int crCount=0;	//counter for crouching targets
		int laCount=0;	//counter for laying targets
		Random r = new Random(System.currentTimeMillis());
		int rand;
		Target target = null;
		int index=0;
		
		while (index<count){
			rand = r.nextInt(3);	//generate random target type
			if(rand==0  && stCount<standing){	//if type is standing and all targets of that type haven't been created
				target = (Target) stand.get(stCount);	//fetches standing target
				target.setID(index);
				stCount++;
				index++;
				scriptedTargets.add(target);			//adds standing target to targets collection
			}
			else if(rand==1  && crCount<crouching){		//if type is crouching and all targets of that type haven't been created
				target = (Target) crouch.get(crCount);	//fetches crouching target
				target.setID(index);
				crCount++;
				index++;
				scriptedTargets.add(target);			//adds crouching target to targets collection
			}
			else if(rand==2  && laCount<laying){		//if type is laying and all targets of that type haven't been created
				target = (Target) lay.get(laCount);		//adds laying target to targets collection
				target.setID(index);
				laCount++;
				index++;
				scriptedTargets.add(target);			//adds laying target to targets collection
			}
		}	
		return scriptedTargets;
	}

	
	//Generates a scripted target 
	public long spawnScriptedTarget(int index){
				
		Barrier b =null;
		Target t= scriptedTargets.get(index);	//fetches a target
		
		int targetType = t.getType();
		int[] origin = new int[2];			//stores coordinates of target
		float[] dimensions = new float[2];	//stores dimensions of target
		boolean targetObsecured=false;
		
		//Laying target must be placed in open space
		if (targetType==2){		//if target is laying 
			origin = generateRandomLocation(t, Gdx.graphics.getHeight()/2);
			t.setActorX(origin[0]);
			t.setActorY(origin[1]);
			targetObsecured = isHiding(t);
			while( targetObsecured== true){		//keep looking until clear location for laying target is found
				origin = generateRandomLocation(t, Gdx.graphics.getHeight()/2);		//target is generated within lower two quadrants
				t.setActorX(origin[0]);
				t.setActorY(origin[1]);
				targetObsecured = isHiding(t);
			}
			t.setCreationTime(System.currentTimeMillis());
			stage.addActor(t);		//adds target to stage
		}
	
		else if (targetType==0){		//if target is standing
			boolean foundStance=false;
			boolean foundBarrier =false;
			int randStance;
			Random rand = new Random(System.currentTimeMillis());
			int randBarrier;
			int randX;
			
			while (foundStance==false){				//while appropriate target/barrier combination hasn't been found
				randStance = rand.nextInt(3);		//randomly chooses stance
				if (randStance==0 && standingHighCount<standingBehindHighBarrier){	//choose random high barrier
					while(foundBarrier==false){		//while appropriate barrier hasn't been found
						randBarrier = MathUtils.random((barriers.size()-1));
						b = barriers.get(randBarrier);
						if (b.getType()==0  || b.getType()==1){	//if barrier is tall
							foundBarrier=true;
						}
					}
					standingHighCount++;
					t.setActorY(b.getActorY());
					dimensions= t.scaleTarget(quadrantHeight);
					t.setActorWidth((int) dimensions[0]);
					t.setActorHeight((int) dimensions[1]);
					randX =  MathUtils.random(b.getActorX(), (b.getActorX()+b.getActorWidth()-t.getActorWidth()) );		//generates random x coordinate within barrier boundaries
					t.setActorX(randX);
					t.setCreationTime(System.currentTimeMillis());
					b.remove();				//removes barrier from stage
					stage.addActor(t);		//adds target to stage
					stage.addActor(b);		//asks target with barrier
					foundStance=true;
				}
				
				else if (randStance==1 && standingMidCount<standingBehindMidBarrier){	//choose random mid barrier
					while(foundBarrier==false){		//while appropriate barrier hasn't been found
						randBarrier = MathUtils.random((barriers.size()-1));
						b = barriers.get(randBarrier);
						if (b.getType()==2  || b.getType()==3){		//if barrier is of medium height
							foundBarrier=true;
						}
					}
					standingMidCount++;
					t.setActorY(b.getActorY());
					dimensions= t.scaleTarget(quadrantHeight);
					t.setActorWidth((int) dimensions[0]);
					t.setActorHeight((int) dimensions[1]);
					randX =  MathUtils.random(b.getActorX(), (b.getActorX()+b.getActorWidth()-t.getActorWidth()) );		//generates random x coordinate within barrier boundaries
					t.setActorX(randX);
					t.setCreationTime(System.currentTimeMillis());
					b.remove();				//removes barrier from stage
					stage.addActor(t);		//adds target to stage
					stage.addActor(b);		//asks target with barrier
					foundStance=true;
				}
				
				else if (randStance==2 && standingUnhiddenCount<standingUnhidden){	//choose random high barrier	
					standingUnhiddenCount++;				
					origin = generateRandomLocation(t, Gdx.graphics.getHeight()/2);
					t.setActorX(origin[0]);
					t.setActorY(origin[1]);
					targetObsecured = isHiding(t);
					while( targetObsecured== true){		//keep looking until clear location for standing target is found
						origin = generateRandomLocation(t, Gdx.graphics.getHeight()/2);
						t.setActorX(origin[0]);
						t.setActorY(origin[1]);
						targetObsecured = isHiding(t);
					}
					t.setCreationTime(System.currentTimeMillis());
					stage.addActor(t);		//adds target to stage
					foundStance=true;
				}	
			}
		}
		
		else if (targetType==1){		//target is crouching
			boolean foundStance=false;
			boolean foundBarrier =false;
			int randStance;
			Random rand = new Random(System.currentTimeMillis());
			int randBarrier;
			int randX;
			
			while (foundStance==false){		//while appropriate target/barrier combination hasn't been found
				randStance = rand.nextInt(2);		//randomly chooses stance
				
				if (randStance==0 && crouchingMidCount<crouchingBehindMidBarrier){	//choose random medium height barrier	
					while(foundBarrier==false){		//while appropriate barrier hasn't been found
						randBarrier = MathUtils.random((barriers.size()-1));
						b = barriers.get(randBarrier);
						
						if (b.getType()==2  || b.getType()==3){
							foundBarrier=true;
						}
					}
					crouchingMidCount++;
					t.setActorY(b.getActorY());
					dimensions= t.scaleTarget(quadrantHeight);
					t.setActorWidth((int) dimensions[0]);
					t.setActorHeight((int) dimensions[1]);
					randX =  (int) MathUtils.random(b.getActorX(), (b.getActorX()+b.getOriginalWidth()-t.getActorWidth()) );		//generates random x coordinate within barrier boundaries
					t.setActorX(randX);
					t.setCreationTime(System.currentTimeMillis());
					b.remove();				//removes barrier from stage
					stage.addActor(t);		//adds target to stage
					stage.addActor(b);		//asks target with barrier
					foundStance=true;
				}
				
				else if (randStance==1  && crouchingUnhiddenCount<crouchingUnhidden){	
					crouchingUnhiddenCount++;				
					origin = generateRandomLocation(t, Gdx.graphics.getHeight()/2);
					t.setActorX(origin[0]);
					t.setActorY(origin[1]);
					targetObsecured = isHiding(t);
					while( targetObsecured== true){		//keep looking until clear location for crouching target is found
						origin = generateRandomLocation(t, Gdx.graphics.getHeight()/2);
						t.setActorX(origin[0]);
						t.setActorY(origin[1]);
						t.setBounds(t.getActorX(),t.getActorY(),t.getActorWidth(), t.getActorHeight());
						targetObsecured = isHiding(t);
					}
					t.setCreationTime(System.currentTimeMillis());
					stage.addActor(t);		//adds target to stage
					foundStance=true;
				}	
			}
		}
		return t.getCreationTime();
	}
			
		
	//Randomly generate target; returns creation time of target (used for the first game within a session)
	public long spawnTarget(int index){
		
		float[] dimensions = new float[2];
		Barrier b =null;
		Target t= targets.get(index);
		t.setID(index);
		int targetType = t.getType();
		int[] origin = new int[2];
		boolean targetObsecured;
		int randBarrier;
		int randX;
		
		//Laying target must be placed in open space
		if (targetType==2){		//if target is laying then it must be generated within lower two quadrants
			origin = generateRandomLocation(t, Gdx.graphics.getHeight()/2);
			t.setActorX(origin[0]);
			t.setActorY(origin[1]);
			targetObsecured = isHiding(t);
			
			while( targetObsecured== true){		//keep looking until clear location for laying target is found
				origin = generateRandomLocation(t, Gdx.graphics.getHeight()/2);
				t.setActorX(origin[0]);
				t.setActorY(origin[1]);
				t.setBounds(t.getActorX(),t.getActorY(),t.getActorWidth(), t.getActorHeight());
				targetObsecured = isHiding(t);
			}
			t.setCreationTime(System.currentTimeMillis());
			stage.addActor(t);		//adds target to stage
		}
		
		else{	//if target is not laying down
			//Determine whether generating target behind barrier or at totally random position
			int randomType = MathUtils.random(9);
			if (randomType<7){		//generate target behind barrier		
				if (targetType==0){		//standing target can appear behind any type of barrier
					randBarrier = MathUtils.random((barriers.size()-1));
					b = barriers.get(randBarrier);
					
					if (b.getType()==0  || b.getType()==1){	//if barrier is tall
						standingBehindHighBarrier++;
					}
					else if (b.getType()==2  || b.getType()==3){	//if barrier is of medium height
						standingBehindMidBarrier++;
					}
					t.setActorY(b.getActorY());
					dimensions= t.scaleTarget(quadrantHeight);
					t.setActorWidth((int) dimensions[0]);
					t.setActorHeight((int) dimensions[1]);
					randX =  MathUtils.random(b.getActorX(), (b.getActorX()+b.getActorWidth()-t.getActorWidth()) );		//generates random x coordinate within barrier boundaries
					t.setActorX(randX);
					t.setBounds(t.getActorX(),t.getActorY(),t.getActorWidth(), t.getActorHeight());
					t.setCreationTime(System.currentTimeMillis());
					b.remove();				//removes barrier from stage
					stage.addActor(t);		//adds target to stage
					stage.addActor(b);		//asks target with barrier
				}

				else if (targetType==1){	//crouching target must appear behind mid-height barrier
					b = canHide(t, barriers.size());
					crouchingBehindMidBarrier++;
					t.setActorY(b.getActorY());
					dimensions= t.scaleTarget(quadrantHeight);
					t.setActorWidth((int) dimensions[0]);
					t.setActorHeight((int) dimensions[1]);
					randX =  MathUtils.random(b.getActorX(), (b.getActorX()+b.getActorWidth()-t.getActorWidth()) );		//generates random x coordinate within barrier boundaries
					t.setActorX(randX);
					t.setBounds(t.getActorX(),t.getActorY(),t.getActorWidth(), t.getActorHeight());
					b.remove();				//removes barrier from stage
					t.setCreationTime(System.currentTimeMillis());
					stage.addActor(t);		//adds target to stage
					stage.addActor(b);		//asks target with barrier		
				}
			}
	
			else{	//generate target in open space
				if (targetType==0){ //if target is standing then it can be generated in any quadrant
					origin = generateRandomLocation(t, Gdx.graphics.getHeight());
					t.setActorX(origin[0]);
					t.setActorY(origin[1]);				
					targetObsecured = isHiding(t);	
					
					while( targetObsecured== true){		//keep looking until clear location for laying target is found
						origin = generateRandomLocation(t, Gdx.graphics.getHeight());
						t.setActorX(origin[0]);
						t.setActorY(origin[1]);
						targetObsecured = isHiding(t);
					}
					t.setCreationTime(System.currentTimeMillis());
					dimensions= t.scaleTarget(quadrantHeight);
					t.setActorWidth((int) dimensions[0]);
					t.setActorHeight((int) dimensions[1]);
					t.setBounds(t.getActorX(),t.getActorY(),t.getActorWidth(), t.getActorHeight());
					stage.addActor(t);		//adds target to stage
					standingUnhidden++;
				}
				
				else if (targetType==1){ //if target is crouching then it can be generated within lower 3 quadrants
					origin = generateRandomLocation(t, (int)(Gdx.graphics.getHeight()* (3.0/4)));
					t.setActorX(origin[0]);
					t.setActorY(origin[1]);
					targetObsecured = isHiding(t);
					
					while( targetObsecured== true){		//keep looking until clear location for laying target is found
						origin = generateRandomLocation(t, (int)(Gdx.graphics.getHeight()* (3.0/4)));
						t.setActorX(origin[0]);
						t.setActorY(origin[1]);
						targetObsecured = isHiding(t);
					}
					t.setCreationTime(System.currentTimeMillis());
					dimensions= t.scaleTarget(quadrantHeight);
					t.setActorWidth((int) dimensions[0]);
					t.setActorHeight((int) dimensions[1]);
					t.setBounds(t.getActorX(),t.getActorY(),t.getActorWidth(), t.getActorHeight());
					stage.addActor(t);		//adds target to stage
					crouchingUnhidden++;
				}
			}
		}
		return t.getCreationTime();
	}
	
	
	//Returns a reference to a barrier that a target can hide behind
	public Barrier canHide(Target t, int count){
		
		Barrier b = null;
		Barrier tester=null;
		int range = count -1;
		int barrierType;
		int bIndex;
		
		while (b==null){
			bIndex = MathUtils.random(range);
			tester = barriers.get(bIndex);
			barrierType = tester.getType();
			if (barrierType==2  || barrierType==3){		//if barrier is suitable for crouching target	
				b= tester;
				return b;
			}
		}
		return null;
	}
			
		
	//Returns true if target "t" is behind barrier "b"
	public boolean overlap(Target t, Barrier b){

		Rectangle tRectangle = new Rectangle(t.getActorX(), t.getActorY(), t.getActorWidth(), t.getHeight());
		Rectangle bRectangle = new Rectangle(b.getActorX(), b.getActorY(), b.getActorWidth(), b.getHeight());
		Rectangle overlap = new Rectangle();
		
		if(Intersector.intersectRectangles(tRectangle, bRectangle, overlap)==true){	//if objects intersect
			return true;
		}
		return false;	
	}
	
	
	//Returns true if target is hiding behind any barrier
	public boolean isHiding(Target t){	
		Iterator<Barrier> iter = barriers.iterator();
		Rectangle tRectangle = new Rectangle(t.getActorX(), t.getActorY(), t.getActorWidth(), t.getHeight());
		Rectangle bRectangle;
		Rectangle overlap = new Rectangle();
		boolean intersect;
		
		while (iter.hasNext()==true){
			Barrier b = iter.next();
			bRectangle = new Rectangle(b.getActorX(), b.getActorY(), b.getActorWidth(), b.getHeight());
			intersect = Intersector.intersectRectangles(tRectangle, bRectangle, overlap);

			if (intersect==true){	//if target and barrier intersects
				return true;
			}
		}
		return false;
	}
	
	
	//Returns reference of barrier that target is hiding behind
	public Barrier barrierReference(Target t){	
		Iterator<Barrier> iter = barriers.iterator();
		Rectangle tRectangle = new Rectangle(t.getActorX(), t.getActorY(), t.getActorWidth(), t.getHeight());
		Rectangle bRectangle;
		Rectangle overlap = new Rectangle();
	
		
		while (iter.hasNext()==true){
			Barrier b = iter.next();
			bRectangle = new Rectangle(b.getActorX(), b.getActorY(), b.getActorWidth(), b.getHeight());

			boolean intersect = Intersector.intersectRectangles(tRectangle, bRectangle, overlap);

			if (intersect==true){
				t.setBarrierHeight(b.getActorHeight());
				return b;
			}
		}
		return null;
	}

	
	//Returns an array containing a randomly generates x and y coordinate for a target
	public int[] generateRandomLocation(Target target, int upperBound){
		
		float[] dimensions = new float[2];
		int []location = new int[2];
	
		location[1] = MathUtils.random(0, upperBound - target.getActorHeight());	//stores y-coordinate
		
		if (target.getType()!=2){	//ensures only standing and crouching targets are scaled
			dimensions= target.scaleTarget(quadrantHeight);		//calculates new dimensions of target
			target.setActorWidth((int) dimensions[0]);			//sets target width
			target.setActorHeight((int) dimensions[1]);			//sets actor height
		}
		location[0] = MathUtils.random(0, Gdx.graphics.getWidth() - target.getActorWidth());	//stores x-coordinate
		return location;
	}

	
	//Transfers of game's results to database
	public void processResults(ArrayList<Target> targets){
		
		String playerID = DataManager.getUsername();
		String sessionID = currentSession;
		String targetID="";
		int hit=0;
		int miss=0;
		float time= 0;
		Target target = null;
		
		for (int i=0 ; i<targets.size(); i++){
			target = (Target) targets.get(i);
			targetID = String.valueOf(target.getID());
			hit = target.isHit();
			miss = target.isMiss();
			time = target.getReactionTime();
			DatabaseManager.insertResults(playerID, sessionID, targetID , hit, miss, time);	//writes data to Results table
		}
	}
		
}