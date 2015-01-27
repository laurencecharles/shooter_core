package com.mygdx.shooter;

import com.badlogic.gdx.Screen;

//Activity initialization calls for each activity
public enum Screens {
	
	INTRO {
        @Override
        protected Screen getScreenInstance() {
            return new IntroScreen();
        }
    },
  
    SCROLL {
        @Override
        protected Screen getScreenInstance() {
            return new Scroll();
        }
    },
    
    MAIN_MENU {
        @Override
        protected Screen getScreenInstance() {
             return new MainMenuScreen();
        }
    },
    
	    ADMIN_HOME {
	        @Override
	        protected Screen getScreenInstance() {
	             return new AdminHome();
	        }
	    },
	    
		    ADMIN_REG_0 {
		        @Override
		        protected Screen getScreenInstance() {
		             return new AdminRegister0();
		        }
		    },
	    
		    ADMIN_REG_1 {
		        @Override
		        protected Screen getScreenInstance() {
		             return new AdminRegister1();
		        }
		    },
		    
		    ADMIN_LOGIN {
		        @Override
		        protected Screen getScreenInstance() {
		             return new AdminLogin();
		        }
		    },
		    
			    CONTROL_PANEL {
			        @Override
			        protected Screen getScreenInstance() {
			             return new ControlPanel();
			        }
			    },
			    	
				    CREATE_SESSION_ONE {
				        @Override
				        protected Screen getScreenInstance() {
				             return new Session1();
				        }
				    },
				    
					    CREATE_SESSION_TWO {
					        @Override
					        protected Screen getScreenInstance() {
					             return new Session2();
					        }
					    },
				    
						    CREATE_SESSION_THREE {
						        @Override
						        protected Screen getScreenInstance() {
						             return new Session3(DataManager.getSessionParticipantCount());
						        }
						    },
			    	
		    
			    VIEW_SESSION {
			        @Override
			        protected Screen getScreenInstance() {
			             return new ViewSession();
			        }
			    },
		    
			    ADD_PARTICIPANT {
			        @Override
			        protected Screen getScreenInstance() {
			             return new AddParticipant();
			        }
			    },
			    
			    ADMIN_INSTRUCTIONS {
			        @Override
			        protected Screen getScreenInstance() {
			             return new AdminInstructions();
			        }
			    },
			    
	    
	    
	    PLAYER_HOME {
	        @Override
	        protected Screen getScreenInstance() {
	             return new PlayerHome();
	        }
	    },
	    
		    PLAYER_PROFILE {
		        @Override
		        protected Screen getScreenInstance() {
		             return new PlayerProfile();
		        }
		    },
		    
			    MAIN_GAME_SCREEN {
			        @Override
			        protected Screen getScreenInstance() {
//			             return new MainGameScreen();		temporarily changed
			        	return new GameScreen();
			        }
			    },
			    
			    PLAYER_INSTRUCTIONS{
			        @Override
			        protected Screen getScreenInstance() {
			             return new PlayerInstructions();
			        }
			    },
		
		    
		    
	    INSTRUCTIONS {
	        @Override
	        protected Screen getScreenInstance() {
	             return new Instructions();
	        }
	    },
    	
    	
    	
    	GAME_OVER {
            @Override
            protected Screen getScreenInstance() {
                return new GameOverScreen();
            }
        };
	 
 
	    protected abstract Screen getScreenInstance();
	 	
	
	}
