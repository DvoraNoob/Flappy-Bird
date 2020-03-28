package com.dvoragames.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dvoragames.entities.Entity;
import com.dvoragames.main.Game;

public class Tubo extends Entity{
	
	public static BufferedImage TUBO_U = Game.spritesheet.getSprite(0, 21, 26, 160);
	public static BufferedImage TUBO_D = Game.spritesheet.getSprite(27, 21, 26, 160);
	
	public Tubo(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}

	public void tick() {
		x--;
		if(x+width <= 0) {
			Game.entities.remove(this);
			return; 
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		g.setColor(new Color(0,0,0,0));
		g.fillRect((int)x, (int)y, width, height);
	}
}
