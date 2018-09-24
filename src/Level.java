/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeremiah Stillings
 * jstillings1@nc.rr.com
 */
// get plug ins imported
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Level extends JPanel implements ActionListener {
    // declare variables
    private final int levelWidth = 1200;// set board size
    private final int levelHeight = 800; // set board size
    private final int allEscapers = 900; // set max escapers length
    private final int allMonsters = 900; //set max monster length
    private final int x[] = new int[allEscapers]; 
    private final int y[] = new int[allEscapers];
    private final int mx1[] = new int[allMonsters];
    private final int my1[] = new int[allMonsters];
    private final int mx2[] = new int[allMonsters];
    private final int my2[] = new int[allMonsters];
    private final int mx3[] = new int[allMonsters];
    private final int my3[] = new int[allMonsters];
    private final int chasedistance[] = new int[3];
    private int escapers;
    private Timer timer;
    private Image player;// leader
    private Image monstera;
    private Image monsterb;
    private Image monsterc;
    private Image helpers;// followers
    private Image out;// thing to reach to get a follower
    private final int delay = 140;
    private boolean inGame = true;
    private int door_x;
    private int door_y;
    private final int playersize = 50  ;
    private final int movespeed = 50  ;
    private final int chasespeed = 50;
    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private final int RAND_POS = 18;
    private final int RAND_POS2 = 18;
    private boolean win = false;
    private boolean levelup = true;
    private boolean failedtolead = false;
    public Level(){
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(levelWidth, levelHeight));
        loadImages();
        initGame();
        
    }
    private void loadImages() {
        // assign jar safe file paths with java.net.URL
        java.net.URL imageURL = Escape.class.getResource("Kid.png");
        ImageIcon escapee = new ImageIcon(imageURL);
        player = escapee.getImage();
        
        
        java.net.URL imageURL1 = Escape.class.getResource("Monster1.png");
        ImageIcon monster1 = new ImageIcon(imageURL1);
        monstera = monster1.getImage(); // assign variable to the image
        
        java.net.URL imageURL2 = Escape.class.getResource("Monster2.png");
        ImageIcon monster2 = new ImageIcon(imageURL2);
        monsterb = monster2.getImage();// assign variable to the image
        
        java.net.URL imageURL3 = Escape.class.getResource("Monster3.png");
        ImageIcon monster3= new ImageIcon(imageURL3);
        monsterc = monster3.getImage();// assign variable to the image
        
        java.net.URL imageURL4 = Escape.class.getResource("Helper.png");
        ImageIcon helper = new ImageIcon(imageURL4);
        helpers = helper.getImage();
        
        java.net.URL imageURL5 = Escape.class.getResource("Door.png");
        ImageIcon doorout= new ImageIcon(imageURL5);
        out = doorout.getImage();
    }
    private void initGame() {
        //start with 2 escapers
        escapers = 2;
        
        for (int c = 0; c < escapers; c++){
            x[c]= 50 - c * 10;
            y[c]= 50;
        }

        locateMonsters();
        locateDoor();
       
        timer = new Timer(delay, this);
        timer.start();
        JOptionPane.showMessageDialog(null, "Use your keyboards arrow keys to guide the leader to the door.");
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    private void doDrawing(Graphics g) {
        
        if (inGame) {
            
                
            g.drawImage(monstera, mx1[0], my1[0], this); // add monster a to the screen
            g.drawImage(monsterb, mx2[0], my2[0], this); // add monster b to the screen
            g.drawImage(monsterc, mx3[0], my3[0], this); // add monster c to the screen
                     
            
            g.drawImage(out, door_x, door_y, this); // add door a to the screen 
            
            for (int z = 0; z < escapers; z++) { // set up loop to draw all the escapers
                if (z == 0) {
                    g.drawImage(player, x[z], y[z], this); // draw leader
                } else {
                    g.drawImage(helpers, x[z] , y[z] , this); // draw followers
            }
            }
            Toolkit.getDefaultToolkit().sync();
            
        } else {

            gameOver(g);
        }        
    }
    
    private void gameOver(Graphics g) {
         if (win == true){
            String msg = "You Escaped with 10 people - good job leader";
        Font small = new Font("Helvetica", Font.BOLD, 26);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.yellow);
        g.setFont(small);
        g.drawString(msg, (levelWidth - metr.stringWidth(msg)) / 2, levelHeight / 2); 
         }
         else{
             if (failedtolead ==true){
                 String msg3 = "You failed to Escape by running into your own follower";
        Font small = new Font("Helvetica", Font.BOLD, 26);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.yellow);
        g.setFont(small);
        g.drawString(msg3, (levelWidth - metr.stringWidth(msg3)) / 2, levelHeight / 2);
             }
             else
             {
                 
                
             
        String msg2 = "You failed to Escape by running into a monster or a wall";
        Font small = new Font("Helvetica", Font.BOLD, 26);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.yellow);
        g.setFont(small);
        g.drawString(msg2, (levelWidth - metr.stringWidth(msg2)) / 2, levelHeight / 2);
         }
    }}
    private void chase(){
        
    chasedistance[0]= 50;
    chasedistance[1]= 100;
    chasedistance[2]= 150;
    int w = 0;
     for (int z = 0; z < escapers; z++) { // loop thru the line of followers
        
        //check right
         // check if monster 1 is 1 spot to the right of the player or followers
         if (x[z] == mx1[0]+chasedistance[w]){
             if (leftDirection) {
            mx1[0] = mx1[0] - chasespeed;
        }

        if (rightDirection) { 
            mx1[0] = mx1[0] +chasespeed;
        }

        if (upDirection) {
            my1[0] = my1[0] -chasespeed;
        }

        if (downDirection) {
            my1[0] = my1[0] +chasespeed; 
        }
             
        
     }
         //check if monster 2 is 1 spot to the right of the player or followers
         if (x[z] == mx2[0]+chasedistance[w]){
             if (leftDirection) {
            mx2[0] = mx2[0] -chasespeed;
        }

        if (rightDirection) { 
            mx2[0] = mx2[0] +chasespeed;
        }

        if (upDirection) {
            my2[0] = my2[0] -chasespeed;
        }

        if (downDirection) {
            my2[0] = my2[0] + chasespeed; 
        }
             
         
     }
         //check if monster 3 is 1 spot to the right of the player or followers
         if (x[z] == mx3[0]+chasedistance[w]){
             if (leftDirection) {
            mx3[0] = mx3[0]  -chasespeed;
        }

        if (rightDirection) { 
            mx3[0] = mx3[0] +chasespeed;
        }

        if (upDirection) {
            my3[0] = my3[0] -chasespeed;
        }

        if (downDirection) {
            my3[0] = my3[0] + chasespeed; 
        }
             
         
     }
         //check left
         // //check if monster 1 is 1 spot to the left of the player or followers
         if (x[z] == mx1[0]-chasedistance[w]){
             if (leftDirection) {
            mx1[0] = mx1[0] -chasespeed;
        }

        if (rightDirection) { 
            mx1[0] = mx1[0] +chasespeed;
        }

        if (upDirection) {
            my1[0] = my1[0] -chasespeed;
        }

        if (downDirection) {
            my1[0] = my1[0] + chasespeed; 
        }
             
        
     }
         //check if monster 2 is 1 spot to the left of the player or followers
         if (x[z] == mx2[0]-chasedistance[w]){
             if (leftDirection) {
            mx2[0] = mx2[0] -chasespeed;
        }

        if (rightDirection) { 
            mx2[0] = mx2[0] + chasespeed;
        }

        if (upDirection) {
            my2[0] = my2[0] -chasespeed;
        }

        if (downDirection) {
            my2[0] = my2[0] + chasespeed; 
        }
             
         
     }
         //check if monster 3 is 1 spot to the left of the player or followers
         if (x[z] == mx3[0]-chasedistance[w]){
             if (leftDirection) {
            mx3[0] = mx3[0] - chasespeed;
        }

        if (rightDirection) { 
            mx3[0] = mx3[0] + chasespeed;
        }

        if (upDirection) {
            my3[0] = my3[0] - chasespeed;
        }

        if (downDirection) {
            my3[0] = my3[0]  + chasespeed; 
        }
             
         
     }
     
        //check Y
         // check if monster 1 is 1 spot  above of the player or followers
         if (y[z] == my1[0]+chasedistance[w]){
             if (leftDirection) {
            mx1[0] = mx1[0] -  chasespeed;
        }

        if (rightDirection) { 
            mx1[0] = mx1[0] + chasespeed;
        }

        if (upDirection) {
            my1[0] = my1[0] - chasespeed;
        }

        if (downDirection) {
            my1[0] = my1[0] + chasespeed; 
        }
             
        
     }
         //check if monster 2 is 1 spot above of the player or followers
         if (y[z] == my2[0]+chasedistance[w]){
             if (leftDirection) {
            mx2[0] = mx2[0] - chasespeed;
        }

        if (rightDirection) { 
            mx2[0] = mx2[0] + chasespeed;
        }

        if (upDirection) {
            my2[0] = my2[0] - chasespeed;
        }

        if (downDirection) {
            my2[0] = my2[0]  +chasespeed; 
        }
             
         
     }
         //check if monster 3 is 1 spot above of the player or followers
         if (y[z] == my3[0]+chasedistance[w]){
             if (leftDirection) {
            mx3[0] = mx3[0] -chasespeed;
        } 

        if (rightDirection) { 
            mx3[0] = mx3[0] + chasespeed;
        }

        if (upDirection) {
            my3[0] = my3[0] - chasespeed;
        }

        if (downDirection) {
            my3[0] = my3[0]  + chasespeed; 
        }
             
         
     }
         //check below
         // //check if monster 1 is 1 spot below of the player or followers
         if (y[z] == my1[0]-chasedistance[w]){
             if (leftDirection) {
            mx1[0] = mx1[0] - chasespeed;
        }

        if (rightDirection) { 
            mx1[0] = mx1[0] + chasespeed;
        }

        if (upDirection) {
            my1[0] = my1[0] -chasespeed;
        }

        if (downDirection) {
            my1[0] = my1[0] + chasespeed; 
        }
             
        
     }
         //check if monster 2 is 1 spot below of the player or followers
         if (y[z] == my2[0]-chasedistance[w]){
             if (leftDirection) {
            mx2[0] = mx2[0]- chasespeed;
        }

        if (rightDirection) { 
            mx2[0] = mx2[0] + chasespeed;
        }

        if (upDirection) {
            my2[0] = my2[0] -chasespeed;
        }

        if (downDirection) {
            my2[0] = my2[0] + chasespeed; 
        }
             
         
     }
         //check if monster 3 is 1 spot below of the player or followers
         if (y[z] == my1[0]-chasedistance[w]){
             if (leftDirection) {
            mx3[0] = mx3[0] - chasespeed;
        }

        if (rightDirection) { 
            mx3[0] = mx3[0] +chasespeed;
        }

        if (upDirection) {
            my3[0] = my3[0] - chasespeed;
        }

        if (downDirection) {
            my3[0] = my3[0]  + chasespeed; 
        }
             
         
     }
     }
     
     }
    private void checkCaught() {// check caught my monster
        for (int z = 0; z < escapers; z++) {
        if ((x[z] == mx1[0]) && (y[0] == my1[0])) {

            inGame = false;
        }
        if ((x[z] == mx2[0]) && (y[0] == my2[0])) {

            inGame = false;
        }
        if ((x[z] == mx3[0]) && (y[0] == my3[0])) {

            inGame = false;
        }
        }
    }
    private void checkDoor() {

        if ((x[0] == door_x) && (y[0] == door_y)) {
           
            
            escapers++;
            if (escapers == 11){
                win = true;
            }
           JOptionPane.showMessageDialog(null, "You Leveled up and gained a follower click OK to lead them to the door");
            
            locateMonsters(); // load new monsters
            locateDoor();// load new door
        }
    }
    private void playermove() { // make the characters move 

        for (int z = escapers; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] = x[0]- movespeed;// if left make players x loc - the move speed
        }

        if (rightDirection) { 
            x[0] = x[0] +movespeed;// if right make players x loc + move speed
        }

        if (upDirection) {
            y[0] = y[0]- movespeed;// if up make players x - move speed
        }

        if (downDirection) {
            y[0] = y[0] + movespeed; // if down make players y + move speed
        }
        // since we are moving 50 pixels at a time the speed is too fast 
        // adding a wait to the move to slow it down
        try {
            TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
            //Handle exception
            }
    }
    private void monstermove(){ // move + monster wall bounce
        
        if (my1[0] >= levelHeight) { // top collison
         locateMonstersa();   
        }

        if (my1[0] < 0) { // bottom collision
           locateMonstersa();  
        }

        if (mx1[0] >= levelWidth) { //right collision
            locateMonstersa(); 
        }

        if (mx1[0] < 0) { // left collision
           locateMonstersa(); 
       // monster 2 collision check
        }
        if (my2[0] >= levelHeight) { // top collison
            locateMonstersb(); 
        }

        if (my2[0] < 0) { // bottom collision
            locateMonstersb(); ;
        }

        if (mx2[0] >= levelWidth) { //right collision
           locateMonstersb(); 
        }

        if (mx2[0] < 0) { // left collision
          locateMonstersb(); 
       
        }
        // monster 3 collison check
        if (my3[0] >= levelHeight) { // top collison
            locateMonstersc(); 
        }

        if (my3[0] < 0) { // bottom collision
            locateMonstersc(); 
        }

        if (mx3[0] >= levelWidth) { //right collision
            locateMonstersc(); 
        }

        if (mx3[0] < 0) { // left collision
           locateMonstersc(); 
       
        }
        
      
       
        // make monsters move when player moves
        //monster 1
        if (leftDirection) {
            mx1[0] = mx1[0]- movespeed;
        }

        if (rightDirection) { 
            mx1[0] = mx1[0] +movespeed;
        }

        if (upDirection) {
            my1[0] = my1[0]-movespeed;
        }

        if (downDirection) {
            my1[0] = my1[0] + movespeed; 
        }
        //monster 2
        if (leftDirection) {
            mx2[0] = mx2[0]+ movespeed;
        }

        if (rightDirection) { 
            mx2[0] = mx2[0] -movespeed;
        }

        if (upDirection) {
            my2[0] = my2[0]+movespeed;
        }

        if (downDirection) {
            my2[0] = my2[0] - movespeed; 
        }
      //monster 3
        if (leftDirection) {
            mx3[0] = mx3[0]+ movespeed;
        }

        if (rightDirection) { 
            mx3[0] = mx3[0] -movespeed;
        }

        if (upDirection) {
            my3[0] = my3[0]+movespeed;
        }

        if (downDirection) {
            my3[0] = my3[0] - movespeed; 
        }
        
        
       
        
    }
    private void checkCollision() { //checks for hitting the walls

        for (int z = escapers; z > 0; z--) { // checks to see if you ran into your followers

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
             inGame = false; 
             failedtolead = true;
             
            }
        }

        if (y[0] >= levelHeight) { // top collison
            inGame = false;
        }

        if (y[0] < 0) { // bottom collision
            inGame = false;
        }

        if (x[0] >= levelWidth) { //right collision
            inGame = false;
        }

        if (x[0] < 0) { // left collision
            inGame = false;
        }
        
        if(!inGame) {
            timer.stop();
        }
    }
    private void locateMonsters() { // randomizer for monster locations
        //monstera
        int r = (int) (Math.random() * RAND_POS);
        mx1[0] = ((r * playersize));
        r = (int) (Math.random() * RAND_POS);
        my1[0] = ((r * playersize));
        //monsterb
        r = (int) (Math.random() * RAND_POS);
        mx2[0]= ((r * playersize));
        r = (int) (Math.random() * RAND_POS);
        my2[0] = ((r * playersize));
        //monsterc
        r = (int) (Math.random() * RAND_POS);
        mx3[0] = ((r * playersize));
        r = (int) (Math.random() * RAND_POS);
        my3[0] = ((r * playersize));
    }
    private void locateMonstersa() { // randomizer for monster locations
        //monstera
        int r = (int) (Math.random() * RAND_POS);
        mx1[0] = ((r * playersize));
        r = (int) (Math.random() * RAND_POS);
        my1[0] = ((r * playersize));
        
    }
    private void locateMonstersb() { // randomizer for monster locations
        //monstera
        int r = (int) (Math.random() * RAND_POS);
        mx2[0] = ((r * playersize));
        r = (int) (Math.random() * RAND_POS);
        my2[0] = ((r * playersize));
        
    }
    private void locateMonstersc() { // randomizer for monster locations
        //monstera
        int r = (int) (Math.random() * RAND_POS);
        mx3[0] = ((r * playersize));
        r = (int) (Math.random() * RAND_POS);
        my3[0] = ((r * playersize));
        
    }
    private void locateDoor() { // randomizer for door locations
        //Door
        int r2 = (int) (Math.random() * RAND_POS2);
        door_x = ((r2 * playersize));
        r2 = (int) (Math.random() * RAND_POS2);
        door_y = ((r2 * playersize));
        
        if (door_x == mx1[0]){// if RNG puts door over a monster move door
            door_x = door_x + 50;}
            else
            {if (door_x == mx2[0]){
            door_x = door_x + 50;}
            else
            {if (door_x == mx3[0]){
            door_x = door_x + 50;
            }
            }       
            }
        if ( door_x >levelWidth-50 || door_x < 0){ // if rng puts door outside of walls reset inside.
            door_x = 400;}
        
        if (door_y>levelHeight-50 || door_y <0){
            door_y = 400;
            }
        
            }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkCaught();
            checkDoor();
            checkCollision();
            playermove();
            chase();
            monstermove();     
            
            
        }

        repaint();
    }
    private class TAdapter extends KeyAdapter { // generic keypress listener

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
}
}
