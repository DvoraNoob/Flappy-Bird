package com.dvoragames.world;

import com.dvoragames.entities.Entity;
import com.dvoragames.main.Game;

public class TuboGen{
	
	public static int alturaS;
	public static int alturaT;
	
	public static Tubo tuboU;
	public static Tubo tuboD;

	public int time = 0;
	public int targetTime = 80;

	public void tick() {
		time++;
		if(time == targetTime) {	
			Tubo tubor = new Tubo(240,0,0,0,1,null);
			Game.entities.add(tubor);
			
			int altura1 = Entity.rand.nextInt((80-40)+40);
			Tubo tubo1 = new Tubo((int)Game.WIDTH,-160+altura1,26,160,1,Tubo.TUBO_U);
			alturaT = altura1;
			tuboU = tubo1;
			Game.entities.add(tubo1);
			
			int altura2 = Game.HEIGHT-altura1-50;
			Tubo tubo2 = new Tubo((int)Game.WIDTH,(int)Game.HEIGHT - altura2, 26,altura2,1,Tubo.TUBO_D);
			alturaS = altura1;
			tuboD = tubo2;
			Game.entities.add(tubo2);

			time = 0;
		}
	}

}
