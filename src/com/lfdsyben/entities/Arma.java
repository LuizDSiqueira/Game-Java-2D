package com.lfdsyben.entities;

import java.awt.image.BufferedImage;

public class Arma extends Entity{

	public Arma(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		depth = 0;
	}

}
