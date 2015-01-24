package com.mygdx.shooter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;


public class DatabaseManager {
	
	private static DatabaseManager instance;
	public static Database dbHandler;

	private Game game;

	//Administrator Table Strings
	public static final String ADMINISTRATOR_TABLE = "administrator";		
	public static final String ADMIN_ID = "admin_id";				
	public static final String LAST_NAME = "last_name";
	public static final String PASSWORD = "password";

	private static final String DATABASE_CREATE = "create table if not exists " + ADMINISTRATOR_TABLE 
			+ "(" + ADMIN_ID + " text primary key, "
			+ LAST_NAME + " text not null,"
			+ PASSWORD + " text not null"
			+ ");";

	//Session Table Strings
	public static final String SESSION_TABLE = "session";		
	public static final String SESSION_ID = "session_id";				
	public static final String ADMIN_ID2 = "admin_id";
	public static final String DATE = "date";
	public static final String TEST_DURATION = "test_duration";
	public static final String CYCLE_DURATION = "cycle_duration";
	public static final String VISIBITILY_DURATION = "visibility_duration";
	public static final String TIME_BETWEEN_TARGETS = "interval";
	public static final String TARGET_COUNT = "target_count";
	public static final String PLAYER_COUNT = "player_count";
	public static final String STANDING_TARGETS = "standing_targets";
	public static final String CROUCHING_TARGET = "crouching_targets";
	public static final String LAYING_TARGET = "laying_targets";
	public static final String STANDING_TALL_BARRIER = "standing_tall_barrier";
	public static final String STANDING_MID_BARRIER = "standing_mid_barrier";
	public static final String STANDING_UNHIDDEN = "standing_unhidden";
	public static final String CROUCHING_MID_BARRIER = "crouching_mid_barrier";
	public static final String CROUCHING_UNHIDDEN = "crouching_unhidden";


	private static final String ADD_SESSION = "create table if not exists " + SESSION_TABLE 
			+ "(" + SESSION_ID + " text primary key, "
			+ ADMIN_ID2 + " text not null,"
			+ DATE + " text not null,"
			+ TEST_DURATION + " integer not null,"
			+ CYCLE_DURATION + " integer not null,"
			+ VISIBITILY_DURATION + " integer not null,"
			+ TIME_BETWEEN_TARGETS + " integer not null,"
			+ TARGET_COUNT + " integer not null,"
			+ PLAYER_COUNT + " integer not null,"
			+ STANDING_TARGETS + " integer not null,"
			+ CROUCHING_TARGET + " integer not null,"
			+ LAYING_TARGET + " integer not null,"
			+ STANDING_TALL_BARRIER + " integer not null,"
			+ STANDING_MID_BARRIER + " integer not null,"
			+ STANDING_UNHIDDEN + " integer not null,"
			+ CROUCHING_MID_BARRIER + " integer not null,"
			+ CROUCHING_UNHIDDEN + " integer not null,"
			+ "foreign key(" +ADMIN_ID2 + ") references " + ADMINISTRATOR_TABLE + "(" + ADMIN_ID + ")"
			+ ");";


	//Player table strings
	public static final String PLAYER_TABLE = "player";		
	public static final String PLAYER_ID = "player_id";			
	public static final String SESSION_ID3 = "session_id";	

	private static final String ADD_PLAYER = "create table if not exists " + PLAYER_TABLE 
			+ "(" + PLAYER_ID + " text not null, "
			+ SESSION_ID3 + " text not null,"
			+ "foreign key(" +SESSION_ID3 + ") references " + SESSION_TABLE + "(" + SESSION_ID + "),"
			+ "primary key(" + PLAYER_ID + "," +  SESSION_ID3 + ")"		
			+ ");";


	//Results Table Strings
	public static final String RESULTS_TABLE = "results";		
	public static final String PLAYER_ID2 = "player_id";				
	public static final String SESSION_ID2 = "session_id";
	public static final String TARGET_ID = "target_id";	
	public static final String HIT = "hit";
	public static final String MISS = "miss";
	public static final String REACTION_TIME = "reaction_time";

	private static final String ADD_RESULTS = "create table if not exists " + RESULTS_TABLE 
			+ "(" + PLAYER_ID2 + " text not null, "
			+ SESSION_ID2 + " text not null,"
			+ TARGET_ID + " text not null,"
			+ HIT + " integer not null,"
			+ MISS + " integer not null,"
			+ REACTION_TIME + " real not null,"
			+ "foreign key(" +PLAYER_ID2 + ") references " + PLAYER_TABLE + "(" + PLAYER_ID + "),"
			+ "foreign key(" +SESSION_ID2 + ") references " + SESSION_TABLE + "(" + SESSION_ID + "),"
			+ "primary key(" + PLAYER_ID2 + "," +  SESSION_ID2 + "," + TARGET_ID +  ")"		
			+ ");";

	private static final String DATABASE_NAME = "sbsDatabase.db";	
	private static final int DATABASE_VERSION = 1;		


	private DatabaseManager(){

		dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME,
				DATABASE_VERSION, DATABASE_CREATE, null);

		dbHandler.setupDatabase();

				 //DROPS ALL DATABASES [FOR TESTING PURPOSES]
//				 try {
//					 dbHandler.openOrCreateDatabase();
//					 dbHandler.execSQL("drop table if exists results");
//					 dbHandler.execSQL("drop table if exists player");
//					 dbHandler.execSQL("drop table if exists session");
//					 dbHandler.execSQL("drop table if exists administrator");
//					 System.out.println("All tables have been deleted");
//				 } catch (SQLiteGdxException e) {
//		
//					 System.err.println("Error executing database creation");
//				 }


		//Adds initial table (Administrator)
		try {
			dbHandler.openOrCreateDatabase();
			dbHandler.execSQL(DATABASE_CREATE);
		} catch (SQLiteGdxException e) {
			System.err.println("Error executing database creation");
		}


		//Adds Session table
		try {
			dbHandler.execSQL(ADD_SESSION);
		} catch (SQLiteGdxException e1) {
			System.err.println("Error executing session table creation");
		}


		//Adds Player table
		try {
			dbHandler.execSQL(ADD_PLAYER);
		} catch (SQLiteGdxException e2) {
			System.err.println("Error executing player table creation");
		}


		//Adds Results table
		try {
			dbHandler.execSQL(ADD_RESULTS);
		} catch (SQLiteGdxException e3) {
			System.err.println("Error executing results table  creation");
		}
	}

	//Returns instance of object
	public static DatabaseManager getInstance() {
		if (null == instance) {
			instance = new DatabaseManager();
		}
		return instance;
	}

	//Initializes the game object
	public void initialize(Game game) {
		this.game = game;
	}


	//Returns the size of a result set
	public static int calCount(DatabaseCursor c){
		int count = c.getCount();
		if (count<=0){
			//			 System.out.println("No of matching records= " + count);
			return 0;
		}
		//		 System.out.println("No of matching records= " + count);
		return count;
	}


	//Closes the database
	public static void closeDatabase(){
		//Close database
		try {
			dbHandler.closeDatabase();
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		dbHandler = null;
	}


	//Inserts administrator record
	public static boolean insertAdministrator(String adminID, String lastName, String pass){
		try {
			dbHandler
			.execSQL("insert into administrator (admin_id, last_name, password)"
					+ "values ('" + adminID + "', '" + lastName + "', '" + pass + "');");
			return true;
		} catch (SQLiteGdxException e) {
			System.out.println("Administrator table insertion failed");
			return false;
		}
	}


	//Outputs administrator records
	public static void outputAdministrator(){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("SELECT * FROM administrator");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		System.out.println("\n\nAdministrator Table");
		while (cursor.next()) {
			System.out.println("AdminID - " + String.valueOf(cursor.getString(0)));
			System.out.println("LastName - " + String.valueOf(cursor.getString(1)));
			System.out.println("Password - " + String.valueOf(cursor.getString(2)));
			System.out.println("\n\n");
		}
	}


	//Inserts session record
	public static boolean insertSession(String sessionID, String adminID, String date, int tDuration, int cDuration, int vDuration, int interval, int tCount, int count, int sTargets, int cTargets, int lTargets, int standingBehindTall, int standingBehindMid, int standingUnhidden, int crouchingBehindMid, int crouchingUnhidden){
		try {
			dbHandler
			.execSQL("insert into session (session_id, admin_id, date, test_duration, cycle_duration, visibility_duration, interval, target_count, player_count, standing_targets, crouching_targets, laying_targets, standing_tall_barrier, standing_mid_barrier, standing_unhidden, crouching_mid_barrier, crouching_unhidden)"
					+ "values ('" + sessionID + "', '" + adminID + "', '" + date + "', " + tDuration + ", " + cDuration + ", " + vDuration + ", " + interval + ", " + tCount + ", " + count + ", " + sTargets + ", " +   cTargets + ", " + lTargets + ", " + standingBehindTall + ", " + standingBehindMid + ", " + standingUnhidden + ", " + crouchingBehindMid + ", " + crouchingUnhidden + ");");
			return true;
			//			 System.out.println("Session table insertion successful");
		} catch (SQLiteGdxException e) {
			System.out.println ("Session table insertion failed.");
			return false;
		}
	}


	//Update session record
	public static boolean updateSession(String sessionID, int sTargets, int cTargets, int lTargets, int standingBehindTall, int standingBehindMid, int standingUnhidden, int crouchingBehindMid, int crouchingUnhidden){
		try {
			dbHandler
			.execSQL("update session"
					+ " set standing_targets=" + sTargets + ", crouching_targets=" + cTargets + ", laying_targets=" + lTargets + ", standing_tall_barrier=" + standingBehindTall + ", standing_mid_barrier=" + standingBehindMid + ", standing_unhidden=" + standingUnhidden + ", crouching_mid_barrier=" + crouchingBehindMid + ", crouching_unhidden=" + crouchingUnhidden
					+ " where session_id='" + sessionID + "';");
			return true;
			//			 System.out.println("Session update successful");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
			System.out.println ("Session table update failed.");
			return false;
		}
	}


	//Returns a array of session configurations.  String array is for convenience
	public static String[] loadSessionConfigurations(String sessionID){
		String[] configs = new String[17];
		DatabaseCursor cursor=null;
		boolean found;
		try {
			cursor = dbHandler.rawQuery("select * from session where session_id='" + sessionID + "'");
			found= cursor.next();
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		configs[0] = String.valueOf(cursor.getString(0));
		configs[1] = String.valueOf(cursor.getString(1));
		configs[2] = String.valueOf(cursor.getString(2));
		configs[3] = String.valueOf(cursor.getString(3));
		configs[4] = String.valueOf(cursor.getString(4));
		configs[5] = String.valueOf(cursor.getString(5));
		configs[6] = String.valueOf(cursor.getString(6));
		configs[7] = String.valueOf(cursor.getString(7));
		configs[8] = String.valueOf(cursor.getString(8));
		configs[9] = String.valueOf(cursor.getString(9));
		configs[10] = String.valueOf(cursor.getString(10));
		configs[11] = String.valueOf(cursor.getString(11));
		configs[12] = String.valueOf(cursor.getString(12));
		configs[13] = String.valueOf(cursor.getString(13));
		configs[14] = String.valueOf(cursor.getString(14));
		configs[15] = String.valueOf(cursor.getString(15));
		configs[16] = String.valueOf(cursor.getString(16));
		return configs;
	}

	//Outputs the contents of the Session table
	public static void outputSessions(){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from session");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		System.out.println("\n\nSession Table");
		while (cursor.next()) {
			System.out.println("SessionID - " + String.valueOf(cursor.getString(0)));
			System.out.println("AdminID - " + String.valueOf(cursor.getString(1)));
			System.out.println("Date - " + String.valueOf(cursor.getString(2)));
			System.out.println("Test_Duration - " + String.valueOf(cursor.getString(3)));
			System.out.println("Landscape_Cycle_Duration - " + String.valueOf(cursor.getString(4)));

			System.out.println("Visibility_Duration - " + String.valueOf(cursor.getString(5)));
			System.out.println("Interval - " + String.valueOf(cursor.getString(6)));
			System.out.println("Target_Count - " + String.valueOf(cursor.getString(7)));
			System.out.println("Player_Count - " + String.valueOf(cursor.getString(8)));
			System.out.println("Standing_Targets - " + String.valueOf(cursor.getString(9)));
			System.out.println("Crouching_Targets - " + String.valueOf(cursor.getString(10)));
			System.out.println("Laying_Targets - " + String.valueOf(cursor.getString(11)));
			System.out.println("Standing_Behind_Tall_Barrier - " + String.valueOf(cursor.getString(12)));
			System.out.println("Standing_Behind_Mid_Barrier - " + String.valueOf(cursor.getString(13)));
			System.out.println("Standing_Behind_Unhidden - " + String.valueOf(cursor.getString(14)));
			System.out.println("Crouching_Behind_Mid_Barrier - " + String.valueOf(cursor.getString(15)));
			System.out.println("Crouching_Unhidden - " + String.valueOf(cursor.getString(16)));
			System.out.println("\n\n");
		}
	}


	//Insert player record
	public static boolean insertPlayer(String playerID, String sessionID){
		try {
			dbHandler
			.execSQL("insert into player (player_id, session_id)"
					+ "values ('" + playerID + "', '" + sessionID + "');");
			return true;
			//			 System.out.println("Player table insertion successful");
		} catch (SQLiteGdxException e) {
			System.out.println ("Player table insertion failed.");
			return false;
		}
	}

	
	//Deletes a player record
	public static boolean deletePlayer(String playerID, String sessionID){
		try {
			dbHandler
			.execSQL("delete from player where player_id='" + playerID + "' and session_id='" + sessionID + "';");
			return true;
			//			 System.out.println("Player deletion successful");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
			System.out.println ("Player deletion failed.");
			return false;
		}
	}


	//Outputs Player table
	public static void outputPlayers(){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("SELECT * FROM player");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		System.out.println("\n\nPlayer Table");
		while (cursor.next()) {
			System.out.println("PlayerID - " + String.valueOf(cursor.getString(0)));
			System.out.println("SessionID - " + String.valueOf(cursor.getString(1)));
			System.out.println("\n\n");
		}
	}


	//Insert result record
	public static boolean insertResults(String playerID, String sessionID, String targetID , int hit, int miss, float time){
		try {
			dbHandler
			.execSQL("insert into results (player_id, session_id, target_id, hit, miss, reaction_time)"
					+ "values ('" + playerID  + "', '" + sessionID + "', '"  + targetID + "', '" + hit + "', '" + miss + "', '" + time + "');");
			return true;
			//			 System.out.println("Results table insertion successful");
		} catch (SQLiteGdxException e) {
			System.out.println ("Results table insertion failed.");
			return false;
		}	 
	}


	//Outputs results records
	public static void outputResults(){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from results");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		System.out.println("\n\nResults Table");
		while (cursor.next()) {
			System.out.println("PlayerID - " + String.valueOf(cursor.getString(0)));
			System.out.println("SessionID - " + String.valueOf(cursor.getString(1)));
			System.out.println("TargetID - " + String.valueOf(cursor.getString(2)));
			System.out.println("Hit - " + String.valueOf(cursor.getString(3)));
			System.out.println("Miss - " + String.valueOf(cursor.getString(4)));
			System.out.println("Reaction Time - " + String.valueOf(cursor.getString(5)));
			System.out.println("\n\n");
		}
	}


	//Returns true if initial session assessment has been completed.
	public static boolean sessionExists(String sessionID){
		if (sessionID.equals(" ")){
			return false;
		}
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from results where session_id='" + sessionID + "'" );
			if (calCount(cursor)>0){
				return true;
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return false; 
	}


	//Returns true if no session records exists
	public static boolean isInitialSession(){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("SELECT * FROM session");
			if (cursor!=null  && calCount(cursor)>0){
				return false; 
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return true; 
	}


	//Returns false if no root administrator exists
	public static boolean rootAdministratorExists(){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("SELECT * FROM administrator");
			if (cursor!=null  && calCount(cursor)>0){
				return true; 
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return false; 
	}

	
	//Returns true if administratorID already exists
	public static boolean administratorExists(String id){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from administrator where admin_id='" + id + "';");
			if (cursor!=null  && resultsetEmpty(cursor)==false){
				return true; 
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return false; 
	}


	//Return true if administrator credentials are correct
	public static boolean confirmAdminCredentials(String id, String pass){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from administrator where admin_id='" + id +"' and password='" + pass + "';");
			if (resultsetEmpty(cursor)==true){	
				return false; 
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return true; 
	}

	
	//Returns false is authorizer credentials are invalid
	public static boolean authorizerConfirmed(String id, String pass){
		DatabaseCursor cursor=null;
		boolean found;
		try {
			cursor = dbHandler.rawQuery("select * from administrator limit 1;");
			found = cursor.next();
			if (cursor!=null && String.valueOf(cursor.getString(0)).equals(id) &&  String.valueOf(cursor.getString(2)).equals(pass) ){
				return true; 
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return false;
	}


	//Returns last session of null if no session exists most recent session
	public static String getLastSessionID(){
		DatabaseCursor curr=null;
		boolean hasNext=false;

		String last = null;
		try {
			curr = dbHandler.rawQuery("select * from session");
			int count = calCount(curr);

			if (count == 0){
				return " ";
			}
			else{
				while(curr.next()){
					last = String.valueOf(curr.getString(0));
				}
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return last;	
	}

	
	//Returns true if player is authorized for session
	public static boolean validPlayer(String playerID, String sessionID){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from player where player_id='" + playerID + "'" + " and session_id='" + sessionID + "';");
			if (cursor!=null && calCount(cursor)>0){
				return true; 
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return false;
	}


	//Returns true if player has already completed their assessment
	public static boolean alreadyPlayed(String playerID, String sessionID){
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from results where player_id='" + playerID + "'" + " and session_id='" + sessionID + "';");
			if (cursor!=null && calCount(cursor)>0){
				return true; 
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		return false;
	}


	//Return session configurations
	public static int[] sessionConfig(String sessionID){
		int []results = new int[4];
		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("SELECT * FROM session where session_id='" + sessionID + "';");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		results[0] = Integer.parseInt(cursor.getString(3));
		results[1] = Integer.parseInt(cursor.getString(4));
		results[2] = Integer.parseInt(cursor.getString(5));
		results[3] = Integer.parseInt(cursor.getString(6));
		return results;
	}

	
	//Returns true if result set is empty
	public static boolean resultsetEmpty(DatabaseCursor cursor){
		int count=0;
		while (cursor.next()){
			count++;
		}
		if (count==0){
			return true;
		}
		return false;
	}


	//Exports data to csv file
	public static void exportResults(String sessionID){

		List<String[]> database = new ArrayList<String[]>();
		String[] array = new String[6];

		database.add(new String[] {"Player ID", "Session ID", "Target ID", "Hit", "Miss", "Reaction Time"});

		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from results");
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		while (cursor.next()) {
			if (cursor.getString(1).equals(sessionID)){
				//				 System.out.println("Match found");
				array[0] = cursor.getString(0);
				array[1] = cursor.getString(1);
				array[2] = cursor.getString(2);
				array[3] = cursor.getString(3);
				array[4] = cursor.getString(4);
				array[5] = cursor.getString(5);
				database.add(new String[]{array[0], array[1], array[2], array[3], array[4], array[5]});
			}
		}

		String externalRoot = Gdx.files.getExternalStoragePath();
		String filename =  "/storage/emulated/0/Session_Results/" + sessionID + "_results.csv";

		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(filename));
			writer.writeAll(database); 
			writer.close();
		} catch (IOException e) {
			System.out.println("Error creating file");
			e.printStackTrace();
		}	 
	}


	//Returns true if all scheduled players have completed assessment.
	public static boolean sessionComplete(String sessionID){

		ArrayList<String> players = new ArrayList<String>();

		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from player where session_id='" + sessionID + "'");

			while (cursor.next()) {
				players.add(String.valueOf(cursor.getString(0)));
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		ArrayList<String> playersFromResults = new ArrayList<String>();

		DatabaseCursor cursor2=null;
		try {
			cursor2 = dbHandler.rawQuery("select * from results where session_id='" + sessionID + "'");
			while (cursor2.next()) {
				playersFromResults.add(cursor2.getString(0));
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		int count =0;
		if (playersFromResults.size() == 0){;
			return false;
		}

		String temp=null;
		int i,j;
		for(i= 0 ; i<players.size() ; i++){
			temp = players.get(i);
			for(j=0 ; j<playersFromResults.size() ; j++){
				if (temp.equals(playersFromResults.get(j))){
					count++;
					j=playersFromResults.size();
				}
			}
		}

		if (count == players.size()){
			return true;
		}
		return false;
	} 


	//Returns a list of the current session's pending players
	public static String[] getPendingPlayers(String sessionID){

		ArrayList<String> players = new ArrayList<String>();

		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from player where session_id='" + sessionID + "'");
			System.out.println("Players from player table:");
			while (cursor.next()) {
				players.add(String.valueOf(cursor.getString(0)));
				System.out.println(String.valueOf(cursor.getString(0)) + "\t");
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}


		ArrayList<String> playersFromResults = new ArrayList<String>();
		DatabaseCursor cursor2=null;
		System.out.println("\nDistinct players from results table:");
		try {
			cursor2 = dbHandler.rawQuery("select distinct player_id  from results where session_id='" + sessionID + "'");
			while (cursor2.next()) {
				playersFromResults.add(cursor2.getString(0));
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		//Extract relevant pending records
		ArrayList<String> pending = new ArrayList<String>();
		String temp=null;
		boolean found=false;
		for(int i= 0 ; i<players.size() ; i++){
			temp = players.get(i);
			for(int j=0 ; j<playersFromResults.size() ; j++){
				if (temp.equals(playersFromResults.get(j))){
					found=true;
				}
			}
			if (found==false){
				pending.add(temp);

			}
			found=false;
		}

		if (pending.size()==0){
			return null;
		}
		else{
			//Transfer records from arraylist to array
			String[] result = new String[pending.size()];
			for(int i=0 ; i<pending.size(); i++){
				result[i] = pending.get(i);
			}
			return result;
		} 
	}


	//Returns an array of completed players.
	public static Object[] getCompletedPlayers(String sessionID){

		ArrayList<String> players = new ArrayList<String>();

		DatabaseCursor cursor=null;
		try {
			cursor = dbHandler.rawQuery("select * from player where session_id='" + sessionID + "'");	//Returns result set of all players in the current session
			while (cursor.next()) {
				players.add(String.valueOf(cursor.getString(0)));	//add player names to arraylist
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		ArrayList<String> completed = new ArrayList<String>();
		DatabaseCursor cursor2=null;
		try {
			cursor2 = dbHandler.rawQuery("select distinct player_id  from results where session_id='" + sessionID + "'");
			while (cursor2.next()) {
				completed.add(cursor2.getString(0));
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		
		if (completed.size()==0){
			return null;
		}
		else{
			Object[] result = new Object[completed.size()];
			for(int i=0 ; i<completed.size(); i++){
				result[i] = completed.get(i);
			}
			return result;
		} 
	}

}


