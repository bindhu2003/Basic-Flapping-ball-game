import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.awt.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.JPanel;

class Renderer extends JPanel
{
private static final long serialVersionUID= 1L;
protected void paintComponent(Graphics g)
{
super.paintComponent(g);
FlappyBall.flappyBall.repaint(g);
}
}
public class FlappyBall implements ActionListener, MouseListener, KeyListener
{
public static FlappyBall flappyBall;
public final int WIDTH = 1600, HEIGHT =800;
public Renderer renderer;
public Rectangle ball;
public ArrayList<Rectangle>columns;
public int ticks, yMotion, score;
public boolean gameOver, started;
public Random rand;
public FlappyBall()
{
JFrame jframe = new JFrame();
Timer timer = new Timer(20,this);
renderer = new Renderer();
rand = new Random();
jframe.add(renderer);
jframe.setTitle("Flappy Ball");
jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
jframe.setSize(WIDTH,HEIGHT);
jframe.addMouseListener(this);
jframe.addKeyListener(this);
jframe.setResizable(false);
jframe.setVisible(true);
ball = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10,20, 20);
columns = new ArrayList<Rectangle>();
addColumn(true);
addColumn(true);
addColumn(true);
addColumn(true);
timer.start();
}
public void addColumn(boolean start)
{
int space =300;
int width= 100;
int height = 50 +rand.nextInt(300);
if(start)
{
columns.add(new Rectangle(WIDTH + width + columns.size() * 300,HEIGHT- height - 120, width, height));
columns.add(new Rectangle(WIDTH + width +(columns.size()-1)*300, 0, width, HEIGHT - height - space));
}
else
{
columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600,HEIGHT- height - 120, width, height));
columns.add(new Rectangle(columns.get(columns.size() - 1).x , 0,width, HEIGHT - height - space));
}
}
public void paintColumn(Graphics g, Rectangle column)
{
g.setColor(Color.green.darker());
g.fillRect(column.x, column.y, column.width,column.height);
}
public void jump()
{
if (gameOver)
{
ball = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10,20, 20);
columns.clear();
yMotion =0;
score = 0;
addColumn(true);
addColumn(true);
addColumn(true);
addColumn(true);
gameOver =false;
}
if(!started)
{
started =true;
}
else if(!gameOver)
{
if(yMotion >0)
{
yMotion =0;
}
yMotion -=10;
}
}
public void actionPerformed(ActionEvent e)
{
int speed =10;
ticks++;
if(started )
{
for( int i = 0; i < columns.size(); i++)
{
Rectangle column =columns.get(i);
column.x -=speed;
}
if(ticks % 2 ==0 && yMotion <15)
{
yMotion += 2;
}
for (int i = 0; i < columns.size(); i++)
{
Rectangle column =columns.get(i);
if (column.x + column.width <0)
{
columns.remove(column);
if(column.y ==0)
{
addColumn(false);
}
}
}
ball.y +=yMotion;
for(Rectangle column : columns)
{
if(ball.x + ball.width / 2 > column.x + column.width / 2 -5 && ball.x + ball.width / 2 < column.x + column.width / 2+ 5 && column.y == 0)
{
score++;
}
if(column.intersects(ball))
{
gameOver =true;
if(ball.x <= column.x)
{
ball.x = column.x - ball.width;
}
else
{
if(column.y != 0)
{
ball.y = column.y -ball.height;
}
else if(ball.y < column.height)
{
ball.y = column.height;
}
}
}
}
if(ball.y > HEIGHT - 120 || ball.y <0 )
{
gameOver = true;
}
if(ball.y + yMotion >= HEIGHT -120)//(gameOver)
{
ball.y = HEIGHT -120 -ball.height;
}
}
renderer.repaint();
}
public void repaint(Graphics g)
{

 g.setColor(Color.cyan);
g.fillRect(0,0,WIDTH,HEIGHT);
g.setColor(Color.orange);
g.fillRect(0, HEIGHT - 120, WIDTH,150);
g.setColor(Color.green);
g.fillRect(0, HEIGHT - 120,WIDTH, 20);
g.setColor(Color.red);
g.fillOval(ball.x, ball.y, ball.width,ball.height);
for ( Rectangle column :columns )
{
paintColumn(g,column);
}
g.setColor(Color.blue);
g.setFont(new Font("Georgia",1,50));
if(!started)
{
g.drawString(" You can use mouse/spacebar",40,HEIGHT/4-100);

g.drawString("Click to start game",90,360);
}
if(gameOver)
{
g.drawString("Game Over!!",40,400);
g.drawString("Lost Game!!",40,500);
g.drawString("To exit the game click x",16,100);
g.drawString("To restart game click anywhere",16,200);

}
if(!gameOver && started)
{
g.drawString(String.valueOf(score), WIDTH / 2,100);
}
}
public static void main(String[]args)
{
flappyBall = new FlappyBall();
}
public void mouseClicked(MouseEvent e)
{
jump();
}
public void mousePressed(MouseEvent e){}
public void mouseReleased(MouseEvent e){}
public void mouseEntered(MouseEvent e){}
public void mouseExited(MouseEvent e){}
public void keyPressed(KeyEvent e){}
public void keyTyped(KeyEvent e){}
public void keyReleased(KeyEvent e)
{
if(e.getKeyCode() ==KeyEvent.VK_SPACE)
{
jump();
}
}
}
