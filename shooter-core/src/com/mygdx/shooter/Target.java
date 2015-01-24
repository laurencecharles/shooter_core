package com.mygdx.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Target extends Actor{
	
	private Texture texture;
	
	private long creationTime;
	private long hitTime;
	private float reactionTime;
	
	private int type;
	private int targetID;
	private int hit = 0;
	private int miss = 0;
	private int actorX, actorY, actorHeight, actorWidth;
	private int barrierHeight = 0;
	private int originalHeight=0;
	private int originalWidth=0;
	
	private boolean hasCollided = false;
	
	
	public Target(int type, int id, int x, int y){
		
		this.type = type;
		targetID = id;
		actorX = x;
		actorY = y;
		
		if (type==0){			//if soldier is standing
			texture = ResourceManager.getStandingSoldier();	//fetch standing soldier
		}
		else if (type==1){		//if soldier is crouching
			texture = ResourceManager.getCrouchingSoldier();	//fetch crouching soldier
		}
		else if (type==2){		//if soldier is lying down
			texture = ResourceManager.getLyingSoldier();	//fetch lying soldier
		}
		
		//Sets manipulatable of barrier
		actorHeight = texture.getHeight();
		actorWidth = texture.getWidth();
		
		//Set original dimensions of barriers
		setOriginalHeight(texture.getHeight());
		setOriginalWidth(texture.getWidth());
		
		setBounds(getActorX(),getActorY(),getActorWidth(), getActorHeight());
 	
		//Adds touch functionality to object
		this.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
	}
	
	//Returns Actor object if a touch event if detected
    public Actor hit(float x, float y, boolean touchable){
    	// If this Actor is hidden or untouchable, it cant be hit
    	if(!this.isVisible() || this.getTouchable() == Touchable.disabled)
    		return null;
    	
    	//If detected touched coordinates lie within boundaries of the barrier
    	if (x>=getActorX() && x<=getActorX()+getActorWidth() && 
    		y<=Gdx.graphics.getHeight()- actorY - barrierHeight  && 
    		y>=Gdx.graphics.getHeight()- actorY - actorHeight){
    			return this;
    	}
        return null;
    }
	

	@Override
	public void draw (Batch batch, float parentAlpha) {
		batch.draw(texture, getActorX(), getActorY(), getActorWidth(), getActorHeight());
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

	public float getReactionTime() {
		return reactionTime;
	}

	public void setReactionTime(long reactionTime) {
		this.reactionTime = reactionTime;
	}
	
	public void calculateReactionTime(){
		reactionTime = (float) (( hitTime - creationTime) /1000.0);	
	}
	
	public int getID() {
		return targetID;
	}

	public void setID(int targetID) {
		this.targetID = targetID;
	}

	public int isHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int isMiss() {
		return miss;
	}

	public void setMiss(int miss) {
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

	public boolean isHasCollided() {
		return hasCollided;
	}

	public void setHasCollided(boolean hasCollided) {
		this.hasCollided = hasCollided;
	}

	public int getBarrierHeight() {
		return barrierHeight;
	}

	public void setBarrierHeight(int barrierHeight) {
		this.barrierHeight = barrierHeight;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String toString(){
		String temp;
		temp = "Type:" + getType() + "  ID:" + getID() + "  x:" + getActorX() + "  y:" + getActorY() + "\n";
		return temp;
	}
	
	public float getOriginalHeight() {
		return originalHeight;
	}

	public void setOriginalHeight(int originalHeight) {
		this.originalHeight = originalHeight;
	}

	public float getOriginalWidth() {
		return originalWidth;
	}

	public void setOriginalWidth(int originalWidth) {
		this.originalWidth = originalWidth;
	}


	//Returns scaled width and height of barrier based on its row
	public float[] scaleTarget(float quadHeight){
		float[] dimensions = new float[2];

		int row = getRow(actorY, quadHeight);	//fetches the row of the target

		if (type==2){	//if current target is lying flat
			actorHeight = originalHeight;
			actorWidth = originalWidth;
		}
		else{	//if current target is standing or crouching
			if(row==0){
				actorHeight = (int) (originalHeight * (3/6.0));
				actorWidth = (int) (actorHeight*0.62);
			}
			else if(row==1){
				actorHeight = (int) (originalHeight *(4/6.0));
				actorWidth = (int) (actorHeight*0.62);
			}
			else if(row==2){
				actorHeight = (int) (originalHeight * (5/6.0));
				actorWidth = (int) (actorHeight*0.62);
			}
			else if(row==3){
				actorHeight = originalHeight;
				actorWidth = originalWidth;;
			}
		}

		dimensions[0] = actorWidth; 
		dimensions[1] = actorHeight;
		System.out.println("Target width=" + dimensions[0]);
		System.out.println("Target height=" + dimensions[1]);
		return dimensions;
	}
	
	
	//Returns the row the current object occupies
	public int getRow(float yValue, float quadrantHeight){
		int row=0;
		row =  (int) ( (Gdx.graphics.getHeight() - yValue) / quadrantHeight);
		return row;
	}

}
