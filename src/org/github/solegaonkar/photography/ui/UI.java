package org.github.solegaonkar.photography.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.github.solegaonkar.photography.framework.Main;
import org.github.solegaonkar.photography.framework.Photograph;

public class UI extends JFrame {
	private static final long	serialVersionUID	= 8399597753813992046L;

	private JPanel				contentPane;
	private ImagePanel			imagePanel			= new ImagePanel();
	private JPanel				panelCenter			= new JPanel();

	private Photograph			photograph			= null;
	private final JLabel		labelPrev			= new JLabel("  <<  ");
	private final JLabel		labelNext			= new JLabel("  >>  ");
	private final JMenuBar		menuBar				= new JMenuBar();
	private final JMenu			mnFile				= new JMenu("File");
	private final JMenuItem		mntmExit			= new JMenuItem("Exit");
	private final JMenu			mnImage				= new JMenu("Image");
	private final JMenuItem		mntmRating			= new JMenuItem("Rating");
	private final JMenuItem		mntmMetadata		= new JMenuItem("Metadata");
	private final JMenuItem		mntmInformation	= new JMenuItem("Information");
	private final JMenu			mnFlickr			= new JMenu("Flickr");
	private final JMenuItem		mntmUpload			= new JMenuItem("Upload");
	private final JMenuItem		mntmBrowse			= new JMenuItem("Browse");

	/**
	 * Create the frame.
	 */
	public UI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
		}
		setTitle("Photo Album");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createMenubar();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		panelCenter.add(imagePanel, BorderLayout.CENTER);

		contentPane.add(labelPrev, BorderLayout.WEST);

		contentPane.add(labelNext, BorderLayout.EAST);

		labelNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Main.next();
			}
		});
		labelPrev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Main.prev();
			}
		});
		setMinimumSize(new Dimension(512, 512));
		// setExtendedState(MAXIMIZED_BOTH);
	}

	private void createMenubar() {
		setJMenuBar(menuBar);
		menuBar.add(mnFile);
		mnFile.add(mntmExit);
		menuBar.add(mnImage);
		mnImage.add(mntmRating);
		mnImage.add(mntmMetadata);
		mnImage.add(mntmInformation);
		menuBar.add(mnFlickr);
		mnFlickr.add(mntmUpload);
		mnFlickr.add(mntmBrowse);
		
		mntmInformation.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, String.format("Title: %s%nDescription: %s", "", ""));
			}});
	}

	public void setPhotograph(Photograph photograph) {
		this.photograph = photograph;
		loadPhotograph();
	}

	private void loadPhotograph() {
		imagePanel.setImage(photograph.getImage());
	}

}
