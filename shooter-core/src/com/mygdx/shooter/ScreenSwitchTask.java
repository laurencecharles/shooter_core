package com.mygdx.shooter;

import com.badlogic.gdx.utils.Timer.Task;

public class ScreenSwitchTask extends Task {
	
	private Screens screen = null;
	
	public ScreenSwitchTask(Screens screen) {
		this.screen = screen;
	}

	@Override
	public void run() {
		ScreenManager.getInstance().show("MAIN_MENU",screen);	//switch to "Main Menu" activity
	}

}
