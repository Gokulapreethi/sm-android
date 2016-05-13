package com.cg.callservices;

import java.util.TimerTask;

public class AnimationThread extends TimerTask {

	AnimationListener animationListener = null;

	public AnimationThread(AnimationListener animationListener) {
		this.animationListener = animationListener;
	}

	public void run() {
		animationListener.onTimerElapsed();
	}
}
