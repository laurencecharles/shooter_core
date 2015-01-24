package com.mygdx.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Barrier extends Actor{
	
	private Texture texture;
	
	private int id;
	private int type;
	private int actorX, actorY, actorHeight, actorWidth;
	
	private long creationTime;
	private long hitTime;
	private long reactionTime;
	
	private float originalHeight=0;
	private float originalWidth=0;
	
	private boolean hit = false;
	private boolean miss = false;
	private boolean hasCollided = false;
	
	
	public Barrier(int type, int id, int x, int y){
		
		this.type = type;
		this.setId(id);
		actorX = x;
		actorY = y;
		
		if (type==0){		//if object is a high barrier
			texture = ResourceManager.getHighBarrier1();
		}
		else if (type==1){	//if object is a high barrier
			texture = ResourceManager.getHighBarrier2();
		}
		else if (type==2){	//if object is a medium height barrier
			texture = ResourceManager.getMidBarrier1();
		}
		else if (type==3){	//if object is a medium height barrier
			texture = ResourceManager.getMidBarrier2();
		}
		
		//Sets manipulatable of barrier
		actorHeight = texture.getHeight();
		actorWidth = texture.getWidth();
		
		//Set original dimensions of barriers
		setOriginalHeight(texture.getHeight());
		setOriginalWidth(texture.getWidth());
		
		setCreationTime(TimeUtils.nanoTime());		//sets creation time of the barrier

		setBounds(getActorX(),getActorY(),getActorWidth(), getActorHeight());
		
		//Adds touch functionality to object
		this.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
		
		setTouchable(Touchable.disabled);	//disables barrier's touch detection functionality
	}
	
	
	//Returns Actor object if a touch event if detected
    public Actor hit(float x, float y, boolean touchable){
    	// If this Actor is hidden or untouchable, it cant be hit
    	if(!this.isVisible() || this.getTouchable() == Touchable.disabled)
    		return null;
    	
    	//if detected touched coordinates lie within boundaries of the barrier
    	if (x>=getActorX() && x<=getActorX()+getActorWidth() && 
    			y<=Gdx.graphics.getHeight()-getActorY()  && 
    			y>=Gdx.graphics.getHeight()-getActorY()-getActorHeight()){	
    		hasCollided=true;
    		return this;
    	}

        return null;
    }
   
    //Returns the row and column of quadrant
  	public int[] determineRowCol(int index){
  		
  		//No. of rows = 4
  		//No. of columns = 5
  
  		int row, column;
  		int[] result = new int[2];
  		
  		row = index / 5;
  		column = index % 5;
  		result[0]= row;
  		result[1] = column;
  		return result;
  	}
    
  	
  	//Returns scaled width and height of barrier
  	public float[] scaleBarrier(int index, float w, float h){
  		
  		//Quadrants are numbered from left->right and top->bottom
  		float[] dimensions = new float[2];
  		
  		int[] quadLocation = new int[2];
  		quadLocation = determineRowCol(index);
  		int row = quadLocation[0];
  		
  		float width=0;
  		float height=0;
  		
  		//Barrier reduction is inversely proportional to row number
  		if(row==0){
  			height = (float) (h * (3/6.0));
  			width = (float) (height*1.6);
  		}
  		else if(row==1){
  			height = (float) (h *(4/6.0));
  			width = (float) (height*1.6);
  		}
  		else if(row==2){
  			height = (float) (h * (5/6.0));
  			width = (float) (height*1.6);
  		}
  		else if(row==3){	//barriers in the highest numbered rows are not scaled
  			height = h;
  			width = w;
  		}
  		
  		dimensions[0] = width; 
  		dimensions[1] = height;
  		
  		return dimensions;
  	}
    
	

	@Override
	public void draw (Batch batch, float parentAlpha) {
		batch.draw(texture, getActorX(), getActorY(), getActorWidth(), getActorHeight());
	}

	

	//Calculates the difference between object creation and a 'hit'
	public void calculateReactionTime(){
		reactionTime = ( hitTime - creationTime) /1000000000;	
	}
	
	
	public long getCreationTime() {
		return creationTime;
	}

	
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	
	public long getHitTime() {
		return hitTime;
	}

	public void setHitTime(long hitTime) {
		this.hitTime = hitTime;
	}

	
	public long getReactionTime() {
		return reactionTime;
	}

	
	public void setReactionTime(long reactionTime) {
		this.reactionTime = reactionTime;
	}
	
	
	public int getType() {
		return type;
	}

	
	public void setType(int type) {
		this.type = type;
	}

	
	public boolean isHit() {
		return hit;
	}

	
	public void setHit(boolean hit) {
		this.hit = hit;
	}

	
	public boolean isMiss() {
		return miss;
	}

	
	public void setMiss(boolean miss) {
		this.miss = miss;
	}

	
	public int getActorX() {
		return actorX;
	}


	public void setActorX(int actorX) {
		this.actorX = actorX;
	}


	public int getActorY() {
		return actorY;
	}


	public void setActorY(int actorY) {
		this.actorY = actorY;
	}


	public int getActorHeight() {
		return actorHeight;
	}


	public void setActorHeight(int actorHeight) {
		this.actorHeight = actorHeight;
	}


	public int getActorWidth() {
		return actorWidth;
	}


	public void setActorWidth(int actorWidth) {
		this.actorWidth = actorWidth;
	}


	public float getOriginalHeight() {
		return originalHeight;
	}


	public void setOriginalHeight(float originalHeight) {
		this.originalHeight = originalHeight;
	}


	public float getOriginalWidth() {
		return originalWidth;
	}


	public void setOriginalWidth(float originalWidth) {
		this.originalWidth = originalWidth;
	}
	
	
	public void setTexture(Texture tex){
		this.texture = tex;
	}
	
	
	public Texture getTexture(){
		return this.texture;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


}
