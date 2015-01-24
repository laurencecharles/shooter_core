package com.mygdx.shooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;

public class ResourceManager {

	private static ResourceManager instance=null;
	private Game game;
	
	/********Barrier Textures*********/
	private static Texture highBarrier1;
	private static Texture highBarrier2;
	private static Texture midBarrier1;
	private static Texture midBarrier2;
	
	
	/********Target Textures*********/
	private static Texture standingSoldier;
	private static Texture crouchingSoldier;
	private static Texture lyingSoldier;
	
	/********Background Texture*********/
	private static Texture background;
	private static Image backImg;
	
	/********Font Generator*********/
	private  static FreeTypeFontGenerator generator;
	
	/***Skins & Associated Atlas***/
	private static Skin defaultSkin;
	private static Skin customSkin;
	private static TextureAtlas atlas;
	

	private ResourceManager(){}
	
	
	public static ResourceManager getInstance(){
		if (instance==null){
			instance =new ResourceManager();
		}
		return instance;
	}
	
	
	public void initialize(Game game) {
        this.game = game;
    }
	
	//Loads all barrier image files
	public static void createBarrierImages(){
//		highBarrier1 = new Texture(Gdx.files.internal("sprites/standingBarrier250_1.png"));
//		highBarrier2 = new Texture(Gdx.files.internal("sprites/standingBarrier250_2.png"));
//		midBarrier1 = new Texture(Gdx.files.internal("sprites/crouchingBarrier175_1.png"));
//		midBarrier2 = new Texture(Gdx.files.internal("sprites/crouchingBarrier175_2.png"));	
		highBarrier1 = new Texture(Gdx.files.internal("sprites/sBarrier_1.png"));
		highBarrier2 = new Texture(Gdx.files.internal("sprites/sBarrier_2.png"));
		midBarrier1 = new Texture(Gdx.files.internal("sprites/cBarrier_1.png"));
		midBarrier2 = new Texture(Gdx.files.internal("sprites/cBarrier_2.png"));	
	}
	
	public static void cancelBarrierImages(){
		highBarrier1 = null;
		highBarrier2 = null;
		midBarrier1 = null;
		midBarrier2 = null;
	}
	
	
	//Loads all soldier image files
	public static void createSoldierImages(){
//		standingSoldier = new Texture(Gdx.files.internal("sprites/standingSoldier275.png"));
//		crouchingSoldier = new Texture(Gdx.files.internal("sprites/crouchingSoldier200.png"));
//		lyingSoldier = new Texture(Gdx.files.internal("sprites/layingSoldier83.png"));	
		standingSoldier = new Texture(Gdx.files.internal("sprites/standingSoldier.png"));
		crouchingSoldier = new Texture(Gdx.files.internal("sprites/crouchingSoldier.png"));
		lyingSoldier = new Texture(Gdx.files.internal("sprites/layingSoldier.png"));	
	}
	
	public static void cancelSoldierImages(){
		standingSoldier = null;
		crouchingSoldier = null;
		lyingSoldier = null;
	}
	
	//Loads background image file
	public static void loadBackground(){
		background = new Texture(Gdx.files.internal("ui/landscape2.png"));	
	}
	
	//Configures the size of the background image
	public static void configureBackground(){
		setBackImg(new Image(background));
        getBackImg().setScaling(Scaling.fit);
       // getBackImg().setScaling(Scaling.fill);
	}
	
	public static void unloadBackground(){
		background = null;
	}
	
	public static void loadFontGenerator(){
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Top Secret Stamp.ttf"));
	}
	
	public static void loadSkinAtlas(){
		defaultSkin = new Skin(Gdx.files.internal("data/uiskin.json")); 
        atlas = new TextureAtlas("ui/newButtons.pack");
        customSkin = new Skin(atlas);
	}
	
	public static void unloadSkinAtlas(){
		defaultSkin = null; 
        atlas = null;
        customSkin = null;
	}
	
	
	
	/**
	 * @return the highBarrier1
	 */
	public static Texture getHighBarrier1() {
		return highBarrier1;
	}


	/**
	 * @param highBarrier1 the highBarrier1 to set
	 */
	public static void setHighBarrier1(Texture highBarrier1) {
		ResourceManager.highBarrier1 = highBarrier1;
	}


	/**
	 * @return the highBarrier2
	 */
	public static Texture getHighBarrier2() {
		return highBarrier2;
	}


	/**
	 * @param highBarrier2 the highBarrier2 to set
	 */
	public static void setHighBarrier2(Texture highBarrier2) {
		ResourceManager.highBarrier2 = highBarrier2;
	}


	/**
	 * @return the midBarrier1
	 */
	public static Texture getMidBarrier1() {
		return midBarrier1;
	}


	/**
	 * @param midBarrier1 the midBarrier1 to set
	 */
	public static void setMidBarrier1(Texture midBarrier1) {
		ResourceManager.midBarrier1 = midBarrier1;
	}


	/**
	 * @return the midBarrier2
	 */
	public static Texture getMidBarrier2() {
		return midBarrier2;
	}


	/**
	 * @param midBarrier2 the midBarrier2 to set
	 */
	public static void setMidBarrier2(Texture midBarrier2) {
		ResourceManager.midBarrier2 = midBarrier2;
	}


	/**
	 * @return the standingSoldier
	 */
	public static Texture getStandingSoldier() {
		return standingSoldier;
	}


	/**
	 * @param standingSoldier the standingSoldier to set
	 */
	public static void setStandingSoldier(Texture standingSoldier) {
		ResourceManager.standingSoldier = standingSoldier;
	}


	/**
	 * @return the crouchingSoldier
	 */
	public static Texture getCrouchingSoldier() {
		return crouchingSoldier;
	}


	/**
	 * @param crouchingSoldier the crouchingSoldier to set
	 */
	public static void setCrouchingSoldier(Texture crouchingSoldier) {
		ResourceManager.crouchingSoldier = crouchingSoldier;
	}


	/**
	 * @return the lyingSoldier
	 */
	public static Texture getLyingSoldier() {
		return lyingSoldier;
	}


	/**
	 * @param lyingSoldier the lyingSoldier to set
	 */
	public static void setLyingSoldier(Texture lyingSoldier) {
		ResourceManager.lyingSoldier = lyingSoldier;
	}


	/**
	 * @return the background
	 */
	public static Texture getBackground() {
		return background;
	}


	/**
	 * @param background the background to set
	 */
	public static void setBackground(Texture background) {
		ResourceManager.background = background;
	}


	/**
	 * @return the backImg
	 */
	public static Image getBackImg() {
		return backImg;
	}


	/**
	 * @param backImg the backImg to set
	 */
	public static void setBackImg(Image backImg) {
		ResourceManager.backImg = backImg;
	}


	/**
	 * @return the generator
	 */
	public static FreeTypeFontGenerator getGenerator() {
		return generator;
	}


	/**
	 * @param generator the generator to set
	 */
	public static void setGenerator(FreeTypeFontGenerator generator) {
		generator = generator;
	}


	/**
	 * @return the defaultSkin
	 */
	public static Skin getDefaultSkin() {
		return defaultSkin;
	}


	/**
	 * @param defaultSkin the defaultSkin to set
	 */
	public static void setDefaultSkin(Skin defaultSkin) {
		ResourceManager.defaultSkin = defaultSkin;
	}


	/**
	 * @return the customSkin
	 */
	public static Skin getCustomSkin() {
		return customSkin;
	}


	/**
	 * @param customSkin the customSkin to set
	 */
	public static void setCustomSkin(Skin customSkin) {
		ResourceManager.customSkin = customSkin;
	}


	/**
	 * @return the atlas
	 */
	public static TextureAtlas getAtlas() {
		return atlas;
	}


	/**
	 * @param atlas the atlas to set
	 */
	public static void setAtlas(TextureAtlas atlas) {
		ResourceManager.atlas = atlas;
	}
	
	
	

}
