package com.lfdsyben.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.lfdsyben.main.Game;
import com.lfdsyben.world.Camera;

public class BulletShoot extends Entity {

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite,double dx,double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	private double dx;
	private double dy;
	private double spd = 4;
	
	private int life = 30,curLife = 0;
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
	
	public void tick() {
		x += dx*spd;
		y += dy*spd;
		curLife++;
		if(curLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}

}
