 package com.dvoragames.world;

import com.dvoragames.main.Game;

public class ScoreTubo {
	public int time = 0;
	public int targetTime = 80;

	public void tick() {
		time++;
		if(time == targetTime) {			
			Score tuboS = new Score((int)Game.WIDTH+20,TuboGen.alturaS, 1,50,1,null);
			
			Game.entities.add(tuboS);
			time = 0;
		}
	}
}
