package com.dvoragames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import com.dvoragames.entities.Entity;
import com.dvoragames.entities.Player;
import com.dvoragames.graficos.Spritesheet;
import com.dvoragames.graficos.UI;
import com.dvoragames.world.ScoreTubo;
import com.dvoragames.world.TuboGen;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 120;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private BufferedImage image;
	
	public BufferedImage Floor;
	public BufferedImage Floor2;
	public BufferedImage Floor3;
	
	public BufferedImage Back;
	public BufferedImage Back2;
	public BufferedImage Back3;
	public BufferedImage BackA;
	public BufferedImage BackA2;
	public BufferedImage BackA3;

	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static Player player;
	
	public static TuboGen tubo;
	public static ScoreTubo scoreTubo;
	
	public UI ui;
	
	public static int score = 0;
	public static int timeBack = 0;
	public int floorX = 0;
	public int floorX2 = 0;
	public double backX = 0;
	public double backX2 = 0;

	public int spd = 1;
	public double spdBack = 0.4;

	
	public Game(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		player = new Player(WIDTH/2-12,HEIGHT/2,17,12,2,spritesheet.getSprite(2,5,17,12));
		tubo = new TuboGen();
		scoreTubo = new ScoreTubo();
		ui = new UI();
		
		Floor = Game.spritesheet.getSprite(0, 193, 168, 55);
		Floor2 = Game.spritesheet.getSprite(0, 193, 168, 55);
		Floor3 = Game.spritesheet.getSprite(0, 193, 168, 55);
		
		Back = Game.spritesheet.getSprite(0, 252, 144, 248);
		Back2 = Game.spritesheet.getSprite(0, 252, 144, 248);
		Back3 = Game.spritesheet.getSprite(0, 252, 144, 248);
		
		BackA = Game.spritesheet.getSprite(145, 252, 144, 248);
		BackA2 = Game.spritesheet.getSprite(145, 252, 144, 248);
		BackA3 = Game.spritesheet.getSprite(145, 252, 144, 248);

		entities.add(player);
		
	}
	
	public void initFrame(){
		frame = new JFrame("Flappy Bird");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		tubo.tick();
		scoreTubo.tick();
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		
		backX-=spdBack;
		if(backX + 120 <= 0) {
			backX = 120;
		}
		backX2-=spdBack;
		if(backX2 + 120 <= 0) {
			backX2 = 120;
		}
		
		floorX-=spd;
		if(floorX + 120 <= 0) {
			floorX = 120;
		}
		floorX2-=spd;
		if(floorX2 + 120 <= 0) {
			floorX2 = 120;
		}

	}
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(122,102,255));
		g.fillRect(0, 0,WIDTH,HEIGHT);

		if(timeBack < 100) {
			g.drawImage(Back, (int) backX, 0,120,160, null);
			g.drawImage(Back2, (int) (backX2-120), 0,120,160, null);
			g.drawImage(Back3, (int) (backX2+120), 0,120,160, null);
		}
		if(timeBack >= 100){
			g.drawImage(BackA, (int) backX, 0,120,160, null);
			g.drawImage(BackA2, (int) (backX2-120), 0,120,160, null);
			g.drawImage(BackA3, (int) (backX2+120), 0,120,160, null);
			if(timeBack >= 200)
				timeBack = 0;
		}
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		g.drawImage(Floor, floorX, HEIGHT - 20,120,55, null);
		g.drawImage(Floor2, floorX2-120, HEIGHT - 20,120,55, null);
		g.drawImage(Floor3, floorX2+120, HEIGHT - 20,120,55, null);

		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			Player.isPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			Player.isPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {	
		Player.isPressed = true;

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Player.isPressed = false;

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	
}
