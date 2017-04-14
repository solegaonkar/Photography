package fun.photoutil.ui;

// SplashScreen.java
// A simple application to show a title screen in the center of the screen
// for the amount of time given in the constructor. This class includes
// a sample main() method to test the splash screen, but it's meant for use
// with other applications.
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 6352294292617426314L;

	private static volatile boolean	stop				= false;

	// A simple little method to show a title screen in the center
	// of the screen for the amount of time given in the constructor
	public void showSplash() {
		JPanel content = (JPanel) getContentPane();
		content.setBackground(Color.white);

		// Set the window's bounds, centering the window
		int width = 450;
		int height = 115;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);

		// Build the splash screen
		//JLabel label = new JLabel(new ImageIcon("oreilly.gif"));
		JLabel copyrt = new JLabel("Photography!!", JLabel.CENTER);
		copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
		//content.add(label, BorderLayout.CENTER);
		content.add(copyrt, BorderLayout.SOUTH);
		Color oraRed = new Color(156, 20, 20, 255);
		content.setBorder(BorderFactory.createLineBorder(oraRed, 10));

		// Display it
		setVisible(true);

		// Wait a little while, maybe while loading resources
		while (!stop) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
		setVisible(false);
	}

	public static void splash() {
		new SplashScreen().showSplash();
	}
}
