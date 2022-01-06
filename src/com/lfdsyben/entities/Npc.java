package com.lfdsyben.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.lfdsyben.main.Game;

public class Npc extends Entity {
	
	public String frases[] = new String[2];
	public static boolean showMessage = false;
	public boolean show = false;
	
	public int curIndexMsg = 0;
	public int fraseIndex = 0;
	public int time = 0;
	public int maxTime = 30;
	
	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		frases[0] = "Monika não fez nada de errado";
		frases[1] = "Eae";
		}
	
	
	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		
		int xNpc = (int) x;
		int yNpc = (int) y;
		
		if(Math.abs(xPlayer- xNpc) < 50 && Math.abs(yPlayer - yNpc) < 50){
			if(show == false) {
				showMessage = true;
				show = true;
			}
		
			
			
			if(showMessage) {
				
				this.time++;
				if(this.time >= maxTime) {
					this.time = 0;
			if(curIndexMsg < frases[fraseIndex].length()) {
				curIndexMsg++;
		}else {
			if(fraseIndex < frases.length - 1) {
				fraseIndex++;
				curIndexMsg = 0;	
			}
		}
				}
			
			
			}
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		if(showMessage) {
			g.setColor(Color.white);
			g.fillRect(20, 130, 200, 25);
			g.setColor(Color.black);
			g.fillRect(19, 129, 199, 24);
			g.setFont(new Font("Arial", Font.BOLD, 9));
			g.setColor(Color.white);
			g.drawString(frases[fraseIndex].substring(0, curIndexMsg),30, 150);
			
		}
	}
	
}
