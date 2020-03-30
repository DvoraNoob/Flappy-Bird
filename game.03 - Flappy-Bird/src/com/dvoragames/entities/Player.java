package com.dvoragames.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dvoragames.main.Game;
import com.dvoragames.world.Score;
import com.dvoragames.world.Tubo;
import com.dvoragames.world.TuboGen;
import com.dvoragames.world.World;


public class Player extends Entity{
	
	private BufferedImage playerU = Game.spritesheet.getSprite(23, 5, 17, 12);
	private BufferedImage playerD = Game.spritesheet.getSprite(44, 5, 17, 12);

	public static boolean isPressed = false;

	public int time = 0;
	
	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
	}
	
	public void tick(){
		depth = 3;
		if(!isPressed) {
			y+=1.5;
		}else {
			if(y > 0) {
				y-=1.5;
			}
		}
		
		if(y > (Game.HEIGHT-30)) {
			y-=1.5;
			World.restartGame();
		}
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);

			if(e != this) {
				if(Entity.isColidding(this, e)) {
					if(e instanceof Score) {
						Game.score++;
						Game.timeBack+=10;
						Game.entities.remove(e);
					}
					if(e instanceof Tubo) {
						World.restartGame();
					}
				}
				
			}
			
		}
		
	
	}
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(!isPressed) {
			g2.drawImage(playerD, getX(), getY(),null);
		}else {
			g2.drawImage(playerU, getX(), getY(),null);
		}
	}
}
