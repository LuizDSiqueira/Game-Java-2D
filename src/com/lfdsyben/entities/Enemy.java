package com.lfdsyben.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.lfdsyben.main.Game;
import com.lfdsyben.world.AStar;
//import com.lfdsyben.main.Sound;
import com.lfdsyben.world.Camera;
import com.lfdsyben.world.Vector2i;
//import com.lfdsyben.world.Camera;
//import com.lfdsyben.world.World;

public class Enemy extends Entity {

		
	private int frames = 0,maxFrames= 20,index = 0,maxIndex = 1; //3

	private BufferedImage[] sprites;
	
	private int life = 10;
	private boolean isDamaged = false;
	private int damageFrames= 10, damageCurrent = 0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
	sprites = new BufferedImage[2]; //add [4] e add resto das animações
	sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
	sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	/*
	sprites[2] = Game.spritesheet.getSprite(112, 16+16, 16, 16);
	sprites[3] = Game.spritesheet.getSprite(128, 16+16, 16, 16);
	*/

	}

	public void tick() { 
		
		depth = 0;
		//mwidth = 16;
		//mheight = 16;
		//masky = 3;
		//maskw = sla;
		
		/*
		if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 90) {
		if(isColiddingWithPlayer() == false) {
		//if(Game.rand.nextInt(100) < 30) {
		if(((int)x < Game.player.getX()) && World.isFree((int)(x+speed), this.getY())
				&& !isColidding((int)(x+speed), this.getY())) {
			x+=speed;
		}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
				&& !isColidding((int)(x-speed), this.getY())) {
			x-=speed;
		}
		if(y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
				&& !isColidding(this.getX(), (int)(y+speed))) {
			y+=speed;
		}else if(y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
				&& !isColidding(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
		}else {
			//estamos colidindo
			if(Game.rand.nextInt(100) < 10){
			Game.player.life-=Game.rand.nextInt(4);
			Game.player.isDamaged = true;
			}
			
		}
		frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
			if(index > maxIndex) 
				index = 0;
		
			}
		}
		
		else {}
		*/
		
		if(!isColiddingWithPlayer()) {
		if(path == null || path.size() == 0) {
			Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
			// onde ela vai?
			Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
			path = AStar.findPath(Game.world, start, end);
		}
		}else {
			if(new Random().nextInt(100) < 5) {
			//efeito dano
			Game.player.life-=Game.rand.nextInt(3);
			Game.player.isDamaged = true;
			}
		}
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
		if(index > maxIndex) 
			index = 0;
	
		}
		
		if(!isColiddingWithPlayer()) {
			   if (new Random().nextInt(100) < 60)
			      followPath(path);
			}

			if (new Random().nextInt(100) < 10) {
			   Vector2i start = new Vector2i((int) (x / 16), (int) (y / 16));
			   Vector2i end = new Vector2i((int) (Game.player.x / 16) , (int) (Game.player.y / 16));
			   path = AStar.findPath(Game.world, start, end);
			}
			
		collidingBullet();
			
		
	if(isDamaged) {
		this.damageCurrent++;
		if(this.damageCurrent == damageFrames) {
			this.damageCurrent = 0;
			this.isDamaged = false;
		}
		
	}
	
	if(life <= 0) {
		destroySelf();
		return;
	}
	
	
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
				if(Entity.isColidding(this, e)) {
					isDamaged = true;
					life--;
					//Sound.hurtEnemy.play();
					Game.bullets.remove(i);
					return;
				}
			}
		
		
		
	}
		
		//}
		
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx,this.getY() + masky,mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		
		
		
		return enemyCurrent.intersects(player);
	}
	
	
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y,null);
		}else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y,null);
		}
		//g.setColor(Color.GREEN);
		//g.fillRect(this.getX() + maskx - Camera.x, getY() + masky - Camera.y, mwidth, mheight);
	}
	
}
