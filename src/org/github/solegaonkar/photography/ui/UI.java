package org.github.solegaonkar.photography.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.github.solegaonkar.photography.framework.Main;
import org.github.solegaonkar.photography.framework.Photograph;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class UI extends JFrame {
	private static final long		serialVersionUID	= 8399597753813992046L;

	private Photograph				photograph			= null;
	private JPanel					contentPane;
	private ImagePanel				imagePanel			= new ImagePanel();
	private JPanel					panelCenter			= new JPanel();

	private final JMenuBar			menuBar				= new JMenuBar();
	private final JMenu				mnFile				= new JMenu("File");
	private final JMenuItem			mntmExit			= new JMenuItem("Exit");
	private final JMenuItem			mntmMetadata		= new JMenuItem("Metadata");
	private final JMenu				mnFlickr			= new JMenu("Flickr");
	private final JMenuItem			mntmUpload			= new JMenuItem("Upload");
	private final JPanel			panelSouth			= new JPanel();
	private final JSeparator		separator			= new JSeparator();
	private final JTextArea			txtDescription		= new JTextArea();
	private final JMenu				mnAlbum				= new JMenu("Album");
	private final JCheckBoxMenuItem	chckbxmntmNoAlbums	= new JCheckBoxMenuItem("No Album");
	private final JCheckBoxMenuItem	checkBoxMenuItem	= new JCheckBoxMenuItem("New check item");
	private final JMenu				mnFilter			= new JMenu("Filter");
	private final JSpinner			spinner				= new JSpinner();
	private final JCheckBoxMenuItem	chckbxmntmNoAlbum	= new JCheckBoxMenuItem("No Album");
	private final JSlider			slider				= new JSlider();
	private final JPanel			panelButtons		= new JPanel();
	private final JButton			buttonNext			= new JButton(">>");
	private final JButton			buttonPrev			= new JButton("<<");
	private final JPanel			panel_2				= new JPanel();
	private final JPanel			panelInformation	= new JPanel();
	private final AlbumPanel		albumPanel			= new AlbumPanel();
	private final JTextField		txtTitle			= new JTextField();
	private final JButton			btnFlickr			= new JButton("Upload");
	private final JPanel			panelButtons1		= new JPanel();
	private final JPanel			panelButtons2		= new JPanel();

	/**
	 * Create the frame.
	 */
	public UI() {
		txtTitle.setBackground(UIManager.getColor("Viewport.background"));
		txtTitle.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtTitle.setText("Title");
		txtTitle.setColumns(0);
		txtTitle.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				photograph.setTitle(txtTitle.getText());
			}});
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
		imagePanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCenter.add(imagePanel, BorderLayout.CENTER);

		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new BorderLayout(0, 0));
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (photograph != null)
					photograph.setRating(slider.getValue());
			}
		});
		slider.setMajorTickSpacing(1);
		slider.setBorder(new TitledBorder(null, "Rating", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		slider.setMinorTickSpacing(1);
		slider.setMinimum(1);
		slider.setMaximum(5);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		panelSouth.add(slider, BorderLayout.EAST);

		panelSouth.add(panelButtons, BorderLayout.CENTER);
		panelButtons.setLayout(new BorderLayout(0, 0));

		panelButtons.add(panelButtons1, BorderLayout.NORTH);
		panelButtons1.add(buttonPrev);
		panelButtons1.add(buttonNext);

		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPrev.setEnabled(false);
				buttonNext.setEnabled(false);
				Main.next();
				buttonPrev.setEnabled(true);
				buttonNext.setEnabled(true);
			}
		});
		buttonPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonPrev.setEnabled(false);
				buttonNext.setEnabled(false);
				Main.prev();
				buttonPrev.setEnabled(true);
				buttonNext.setEnabled(true);
			}
		});
		btnFlickr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (photograph.getFlickrId().isEmpty()) {
					// upload
				} else {
					// show in browser
				}
			}
		});

		panelButtons.add(panelButtons2, BorderLayout.SOUTH);
		panelButtons2.add(btnFlickr);
		panel_2.setBorder(new TitledBorder(null, "Albums", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		albumPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		albumPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		albumPanel.setPreferredSize(new Dimension(200, 50));

		panelSouth.add(albumPanel, BorderLayout.WEST);
		panel_2.setLayout(new BorderLayout(0, 0));

		panelInformation.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		contentPane.add(panelInformation, BorderLayout.NORTH);
		panelInformation.setLayout(new BorderLayout(0, 0));
		txtDescription.setTabSize(4);
		txtDescription.setRows(4);
		panelInformation.add(txtDescription);
		txtDescription.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtDescription.setText("Description");
		txtDescription.setWrapStyleWord(true);
		txtDescription.setBackground(UIManager.getColor("Viewport.background"));
		txtDescription.setLineWrap(true);

		txtDescription.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				photograph.setDescription(txtDescription.getText());
			}
		});

		panelInformation.add(txtTitle, BorderLayout.NORTH);

		setMinimumSize(new Dimension(640, 640));
	}

	private void createMenubar() {
		setJMenuBar(menuBar);
		menuBar.add(mnFile);
		mnFile.add(mntmMetadata);
		mnFile.add(mnAlbum);
		mnAlbum.add(chckbxmntmNoAlbums);
		mnAlbum.add(checkBoxMenuItem);
		mnFile.add(separator);
		mnFile.add(mntmExit);
		menuBar.add(mnFilter);
		mnFilter.add(spinner);
		mnFilter.add(chckbxmntmNoAlbum);
		menuBar.add(mnFlickr);
		mnFlickr.add(mntmUpload);
	}

	public void setPhotograph(Photograph photograph) {
		this.photograph = photograph;
		imagePanel.setImage(photograph.getImage());
		albumPanel.setPhotograph(photograph);
		slider.setValue(photograph.getRating());
		txtTitle.setText(photograph.getTitle());
		txtDescription.setText(photograph.getDescription());
		if (photograph.getFlickrId().isEmpty()) {
			btnFlickr.setText("Upload to Flickr");
		} else {
			btnFlickr.setText("View on Flickr");
		}
	}
}
