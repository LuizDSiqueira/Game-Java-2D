package com.lfdsyben.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.lfdsyben.entities.BulletShoot;
import com.lfdsyben.entities.Enemy;
import com.lfdsyben.entities.Entity;
import com.lfdsyben.entities.Npc;
import com.lfdsyben.entities.Player;
import com.lfdsyben.graficos.Spritesheet;
import com.lfdsyben.graficos.UI;
//import com.lfdsyben.world.Camera;
import com.lfdsyben.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private int CUR_LEVEL = 1,MAX_LEVEL = 2;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("comicsans.ttf");
	public Font newfont;
	
	public static InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("grafa.ttf");
	public static Font grafa;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
		
	//Cutsene
	
	public static int entrada = 1;
	public static int comecar = 2;
	public static int jogando = 3;
	public static int estado_cena = entrada;
	public int timeCena = 0 , maxTimeCena = (60*3) + 30;
	
	//Cutsene
	
	public Menu menu;
	public BufferedImage lightmap;
	public int[] pixels;
	public static int[] minimapaPixels;
	public int[] lightMapPixels;
	
	public int xx,yy;
	
	public boolean saveGame = false;
	public int mx,my;
	
	public static BufferedImage minimapa;
	
	public Npc npc;	
	
	public Game() {
		//Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//inicializando objetos 
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet =  new Spritesheet("/spritesheet.png");
		player =  new Player(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		
		minimapa = new BufferedImage(World.WIDTH,World.HEIGHT,BufferedImage.TYPE_INT_RGB);
		minimapaPixels = ((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
		
		try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			grafa = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(50f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		npc = new Npc(32,32,16,16, spritesheet.getSprite(16, 16, 16, 16));
		entities.add(npc);
		menu = new Menu();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void initFrame() {
		frame = new JFrame("Jogo Foda");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		Game game = new Game();
		game.start();
		
	}
	public void tick() {
	if (gameState == "NORMAL") {
				if(this.saveGame) {
					this.saveGame = false;
					String[] opt1 = {"level","vida", "bala"};
					int[] opt2 = {this.CUR_LEVEL,(int) player.life, player.ammo};
					Menu.saveGame(opt1,opt2,10);
					//Desenhar string toda bonitinha num retangulo
					System.out.println("Joso salvo");
				}
			this.restartGame = false;
			if(Game.estado_cena == jogando) {
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}
			}else {
				if(Game.estado_cena == entrada) {
					if(Game.player.getX() < 120) {
						player.x++;
					}else {
						System.out.println("Entrada concluída");
						Game.estado_cena = Game.comecar;
					}
				}else if(Game.estado_cena == Game.comecar ) {
					timeCena++;
					if(timeCena == maxTimeCena) {
						Game.estado_cena = Game.jogando;
					}
				}
			}

			if (enemies.size() == 0) {
				// avançar p outro lvl
				CUR_LEVEL++;
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		} else if (gameState == "GAME_OVER") {
			this.framesGameOver++;
			if (this.framesGameOver == 15) {
				this.framesGameOver = 0;
				if (this.showMessageGameOver)
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}
			if (restartGame) {
				Game.gameState = "NORMAL";
				this.restartGame = false;
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}

		} else if (gameState == "MENU") {
			player.updateCamera();
			menu.tick();
		}
	}
	/*
	public void drawRectangleExample(int xoff,int yoff){
		for(int xx = 0; xx < 32; xx++) {
			for(int yy = 0; yy < 32; yy++) {
				int xOff = xx + xoff;
				int yOff = yy + yoff;
				if(xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT) {
					continue;
				}
				pixels[xOff + (yOff*WIDTH)] = 0Xff0000;
			}
		}
	}
	*/
	
	
	public void applyLight() {
		
		for(int xx = 0; xx < Game.WIDTH; xx++) {
			for(int yy = 0; yy < Game.HEIGHT; yy++) {
				if(lightMapPixels[xx + (yy * WIDTH)] == 0xffffffff) {
					pixels[xx + (yy * WIDTH)] = 0;
				}
			}
		}
			}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		/*renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities, Entity.nodeSorter);
		for(int i = 0; i <entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		//applyLight();
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		//drawRectangleExample(xx,yy);
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE,null);
		g.setColor(Color.white);
		g.setFont(newfont);
		g.drawString("Munição: "+ player.ammo, 550, 30);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(grafa);
			g.setColor(Color.WHITE);
			g.drawString("MUITO FRACO, NEM TENTE DE NOVO", 70, 250);
			g.setFont(new Font("arial",Font.BOLD,30));
			if(this.showMessageGameOver)
			g.drawString(">Pressione ENTER para reiniciar<", 110, 300);
	}else if(gameState == "MENU") {
		menu.render(g);
	}
		
		
		World.renderMiniMap();
		g.drawImage(minimapa,615,80,World.WIDTH*5,World.HEIGHT*5,null);
		
		if(Game.estado_cena == Game.comecar) {
			g.setColor(Color.WHITE);
			g.drawString("Se prepare, o jogo vai começar no 3!", 70, 250);
			g.setColor(Color.RED);
			g.drawString(""+timeCena/60, 350, 300);
		}
		
		bs.show();
	}
	@Override
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
				}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS =" +frames);
				frames = 0;
				timer+=1000;
			}
				
			}
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			Npc.showMessage = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			player.jump = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//Executar tal ação se estiver com a tecla da direit
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W ) {
			player.up = true;
			
			if(gameState == "MENU") {
				menu.up = true;
			}
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			player.speed = +2;
		}
		
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
		//	Sound.shoot.play();
			player.shoot = true;
		}
		if(e.getKeyCode() ==  KeyEvent.VK_ENTER) {
			this.restartGame = true;
			
		if(gameState == "MENU") {
			menu.enter = true;
		}
		}if(e.getKeyCode() ==  KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			Menu.pause = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(gameState == "NORMAL")
			this.saveGame = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//Executar tal ação se estiver com a tecla da direita/d
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W ) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
			player.speed = 1.4;
		

		
	}

	@Override
	public void keyTyped(KeyEvent e) {
	
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() /3);
		player.my = (e.getY() /3);
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
	}

}
