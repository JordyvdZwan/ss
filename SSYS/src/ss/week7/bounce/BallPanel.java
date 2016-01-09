package ss.week7.bounce;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * BallPanel a special JPanel for drawing balls on.
 * Used with TimedBouncer.
 * @version 2005.02.22
 */
public class BallPanel extends JPanel implements ActionListener {
	private List<Ball> balls; // @invariant balls != null
	
	public BallPanel() {
		balls = new java.util.ArrayList<Ball>();
		//AnimateThread animateThread = new AnimateThread();
		//animateThread.start();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() { 
			public void run() { 
				actionPerformed(new ActionEvent(this, 1, "Test"));
			} 
		}, 0, 1);
		
	}

	/**
	 * Implements the method from the interface ActionListener
	 * Move and repaint the balls
	 */
	public void actionPerformed(ActionEvent evt) {
		moveBalls();
		repaint();
	}

	public void animate() {
		try {
			while (true) {
				Thread.sleep(5);
				moveBalls();
				repaint();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** Add a new ball to the ball list and start the timer if not yet running. */
	public synchronized void addNewBall() {
		balls.add(new Ball(this));
	}

	/**
	 * Move all balls 
	 * BEWARE: collision effects are not respecting Snellius' law. 
	 */
	public synchronized void moveBalls() {
		for (Ball b : balls) {
			b.move();
		}

		// collision detection
		ListIterator<Ball> ix = balls.listIterator();
		while (ix.hasNext()) {
			Ball b = ix.next();
			ListIterator<Ball> jx = balls.listIterator(ix.nextIndex());
			while (jx.hasNext()) {
				Ball other = jx.next();
				b.collide(other);
			}
		}
	}
   
	public class AnimateThread extends Thread {
		
		public void run(){
			animate();
		}
	}
	
	
	
	/**
	 * Overrides paintComponent in JPanel.
	 * Is called if repaint is called.
	 * Paints all elements of balls.
	 */
	public synchronized void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Ball b : balls) {
			b.draw(g);
		}
	}
}

