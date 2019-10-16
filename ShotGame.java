import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.Random;
import java.util.Arrays;

// 繼承JFrame類別
public class ShotGame extends JFrame{

	//BufferedImage img;
	
	private Image bg,cur;
	private static AudioClip shot_sound, reload, bgm, empty, hurt, start;
	private static int bullet, health, time, killed;
	private JLabel enemy[] = new JLabel[4], bang_pic, bgLabel, bulletL, healthL, killedL, timeL;
	private ImageIcon enemy_pic[] = new ImageIcon[4], bang, bg2, icon;
	private Timer enemy_attack[] = new Timer[4], enemy_rand, clock, bangpicShow;
	private boolean gaming = false;
	
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	public ShotGame() {    // 建構子
		super("shot game");
      
		loadimg();//載入圖片
		loadmusic();//載入音樂
	  
		Cursor starCursor = toolkit.createCustomCursor( cur, new Point(18,18), "star");//滑鼠游標改為準心
		setCursor(starCursor);
		
		this.setLayout(null);
		((JPanel)this.getContentPane()).setOpaque(false);
		
		//init data
		bullet = 7;
		health = 100;
		killed = 0;
		time = 30;
		
		Font f = new Font("Serief", Font.PLAIN, 70);
		
		//bullet label
		bulletL = new JLabel();
		bulletL.setBounds(94,590,80,80);
		bulletL.setForeground(new Color(204, 153, 0));
		bulletL.setText(Integer.toString(bullet));
		bulletL.setFont(f);
		this.add(bulletL);
		
		//health label
		healthL = new JLabel();
		healthL.setBounds(94,665,900,80);
		healthL.setForeground(new Color(204, 0, 0));
		healthL.setText(Integer.toString(health));
		healthL.setFont(f);
		this.add(healthL);
		
		//killed label
		killedL = new JLabel();
		killedL.setBounds(1180,60,80,80);
		killedL.setForeground(new Color(40, 40, 40));
		killedL.setText(Integer.toString(killed));
		killedL.setFont(f);
		this.add(killedL);
		
		//time label
		timeL = new JLabel();
		timeL.setBounds(600,665,80,80);
		timeL.setForeground(new Color(204, 204, 0));
		timeL.setText(Integer.toString(time));
		timeL.setFont(f);
		this.add(timeL);
		
		
		this.setResizable(false);//視窗放大按鈕無效

		bangpicShow = new Timer(500,new  ActionListener(){
			public  void  actionPerformed(ActionEvent  e)
			{
				bang_pic.setVisible(false);
				bangpicShow.stop();
			}
											}
								);
		
	
		game_start();

	}
	
	private void enemy_show(){
		int enemy_id[] = new int[4];
		//生敵間隔
		int showdelay = 3000;
		//停留時間
		int staydelay = 2000;
		enemy_rand = new  Timer(showdelay,new  ActionListener(){
			public  void  actionPerformed(ActionEvent  e)
			{
				//random 出現的 enemy
				int x = new Random().nextInt(4);
				//如果該enemy目前不在
				if(!enemy[x].isVisible()){
					enemy[x].setVisible(true);
					enemy_id[x] = x;
					enemy_attack[x] = new Timer(staydelay,new  ActionListener(){
						private int ex = x;
						public  void  actionPerformed(ActionEvent  e)
						{
							if(health>0&&enemy[ex].isVisible()){
								healthL.setText(Integer.toString(health-=10));
								hurt.play();
								//攻擊完消失
								enemy[ex].setVisible(false);					
								if(health<10)
									game_over();
							}
							enemy_attack[ex].stop();
						}
					}
					);
					//enemy_attack[x].setInitialDelay(2000);
					enemy_attack[x].start();					
				}
			}
			}
			);
			//enemy_rand.setInitialDelay(5000);
			enemy_rand.start();
	}
	
	private void enemy_build(){
		for(int i=0;i<4;i++){
			enemy[i] = new JLabel(enemy_pic[i]);
			enemy[i].setVisible(false);
			this.add(enemy[i]);
		}
		enemy[0].setBounds(200,340,60,76);
		enemy[1].setBounds(500,295,71,155);
		enemy[2].setBounds(970,437,147,256);
		enemy[3].setBounds(250,470,46,76);
	}
	
	private void eventListen_add(){
		KeyLis kl = new KeyLis();
		MouseLis ml = new MouseLis();
		this.addMouseListener(ml);
		this.addKeyListener(kl);
		for(int i=0;i<4;i++)
			enemy[i].addMouseListener(ml);
	}
	
	private void loadmusic(){
	    try {  
		    URL musicURL = new URL("file:" +
		    System.getProperty("user.dir") + "/sound_effect/shot.aiff");
		    shot_sound = Applet.newAudioClip(musicURL);
			
			musicURL = new URL("file:" +
		    System.getProperty("user.dir") + "/sound_effect/bgm.wav");
		    bgm = Applet.newAudioClip(musicURL);
			
			musicURL = new URL("file:" +
		    System.getProperty("user.dir") + "/sound_effect/reload.aiff");
		    reload = Applet.newAudioClip(musicURL);
			
			musicURL = new URL("file:" +
		    System.getProperty("user.dir") + "/sound_effect/empty.wav");
		    empty = Applet.newAudioClip(musicURL);
			
			musicURL = new URL("file:" +
		    System.getProperty("user.dir") + "/sound_effect/hurt.aiff");
		    hurt = Applet.newAudioClip(musicURL);
			
			musicURL = new URL("file:" +
		    System.getProperty("user.dir") + "/sound_effect/start.aiff");
		    start = Applet.newAudioClip(musicURL);
		}
		catch ( MalformedURLException e ) { }

		bgm.play();
	}
	
	private void loadimg(){
	    //bg = toolkit.getImage("pic/bg.jpg");//背景
		cur = toolkit.getImage("pic/aim.png");//準心
		bang = new ImageIcon("pic/bang.png");//射擊特效
	    for(int i=0;i<4;i++)
	        enemy_pic[i] = new ImageIcon("pic/t"+(i+1)+".png");	    
		//射擊特效
		bang_pic = new JLabel(bang);
		this.add(bang_pic);
		
		bg2 = new ImageIcon("pic/bg2.jpg");     // 背景圖片
        bgLabel = new JLabel(bg2);      // 把背景圖顯示在Label中
        bgLabel.setBounds(0, 0, 1280, 800);    // 把含有背景圖之Label位置設置為圖片剛好填充整個版面
        // 把内容視窗轉為JPanel，否則不能使用setOpaque()來使視窗變成透明
        JPanel imagePanel = (JPanel) this.getContentPane();
        imagePanel.setOpaque(false);
        this.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));// 把背景圖添加到分層窗格的最底層以作為背景
				
		
		//設定icon
		icon = new ImageIcon("pic/icon.png"); 
		this.setIconImage(icon.getImage());
	}
	
	
	
	private void game_start(){
		gaming = true;
		clock = new  Timer(1000,new  ActionListener(){
			public  void  actionPerformed(ActionEvent  e)
			{
				if(time>0)
					timeL.setText(Integer.toString(--time));
				else
					game_over();
			}
			}
			);
		clock.start();
		
		enemy_build();
		enemy_show();
		eventListen_add();
		
	}
	
	private void game_over(){
		gaming = false;
		enemy_rand.stop();
		clock.stop();
		
		//取消顯示介面
		healthL.setVisible(false);
		timeL.setVisible(false);
		bulletL.setVisible(false);
		bang_pic.setVisible(false);
		//隱藏所有敵人
		for(int i=0; i<4; i++)
			enemy[i].setVisible(false);
		

		killedL.setFont(new Font("Serief", Font.PLAIN, 150));
		killedL.setBounds(970,440,250,200);
		killedL.setForeground(new Color(153, 0, 0));
		
		ImageIcon gobg = new ImageIcon("pic/go.jpg");
        JLabel gobgL = new JLabel(gobg);
		this.setLayout(new BorderLayout());
		this.add(gobgL, BorderLayout.CENTER);
	}
	
	// 主程式
	public static void main(String[] args) {
      // 建立Swing應用程式
		ShotGame app = new ShotGame();
		app.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		app.setSize(1280,800);  // 設定視窗尺寸
		app.setVisible(true);  // 顯示視窗
		app.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
		});
	}
		
	// 射擊事件
	private class MouseLis extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if(gaming){
			//Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
				Point point = e.getLocationOnScreen();
				if(bullet>0){
					shot_sound.stop();
					shot_sound.play();			
			
					if(e.getSource() instanceof JLabel){
						//取得點擊目標
						JLabel o_clicked = (JLabel)e.getSource();				

						//確認是否射擊到敵人
						if(Arrays.stream(enemy).anyMatch(o_clicked::equals)){
							o_clicked.setVisible(false);
							killedL.setText(Integer.toString(++killed));
						}
					}
						
					//b.setBounds(point.x-29,point.y-54,50,50);
					//顯示射擊特效
					
					 
					bangpicShow.stop(); //若前一個射擊特效還沒消失 讓其消失
					bang_pic.setBounds(e.getXOnScreen()-29,e.getYOnScreen()-54,50,50);
					bang_pic.setVisible(true);
					bangpicShow.start(); //倒數500ms自動消失
					bulletL.setText(Integer.toString(--bullet));		
				}
				else{
					empty.stop();
					empty.play();
				}
			}
		}
	}
	
	// 定義 KeyLis為static類別，並繼承自KeyAdapter類別	
	// reload事件
	private class KeyLis extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{			
			if(e.getKeyCode()==82){  // 如果是按R
				reload.stop();
				reload.play();
				bullet = 7;
				bulletL.setText(Integer.toString(bullet));
			}
		}
		
	}
   
} 