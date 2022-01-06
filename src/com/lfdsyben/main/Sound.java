package com.lfdsyben.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip;
	
	public static final Sound musicBackground = new Sound("/music.wav");
	public static final Sound hurtPlayer = new Sound("/hurtPlayer.wav");
	public static final Sound hurtEnemy = new Sound("/hurtEnemy.wav");
	public static final Sound shoot = new Sound("/shoot.wav");

	public Sound(String name) {
		 try {
			 clip = Applet.newAudioClip(Sound.class.getResource(name));
		 }catch(Throwable e) {}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		 }catch(Throwable e) {}
	}

	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		 }catch(Throwable e) {}


}
}

