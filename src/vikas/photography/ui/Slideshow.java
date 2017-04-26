package vikas.photography.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import vikas.photography.framework.CommonUtils;
import vikas.photography.framework.Database;
import vikas.photography.framework.Database.AlbumRecords;
import vikas.photography.framework.Database.ImageType;

/**
 * 
 * @author vs0016025
 *
 */
public class Slideshow extends JFrame {
	private static final long	serialVersionUID	= -8692686619330564220L;
	private static AlbumRecords	albumRecords		= Database.getRecords();
	private static ImagePanel	imagePanel			= null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Slideshow frame = new Slideshow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public Slideshow() throws Exception {
		setTitle("Album");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 665, 437);
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		JPanel contentPane;
		createMenuBar();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		addPrevNextButtons(contentPane);
		imagePanel = new ImagePanel(albumRecords.next());
		contentPane.add(imagePanel, BorderLayout.CENTER);
	}

	private void addPrevNextButtons(JPanel contentPane) {
		JButton btnPrev = new JButton("<<");
		btnPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					imagePanel.update(albumRecords.prev());
				} catch (Exception e1) {
					CommonUtils.exception(e1);
				}
			}
		});
		contentPane.add(btnPrev, BorderLayout.WEST);

		JButton btnNext = new JButton(">>");
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					imagePanel.update(albumRecords.next());
				} catch (Exception e1) {
					CommonUtils.exception(e1);
				}
			}
		});
		contentPane.add(btnNext, BorderLayout.EAST);
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmAddNew = new JMenuItem("Add New");
		mntmAddNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmAddNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				AddNewImages.start();
				setVisible(true);
			}
		});
		mnFile.add(mntmAddNew);

		/*
		 * JMenuItem mntmFlickrUpload = new JMenuItem("Flickr Upload");
		 * mntmFlickrUpload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		 * mntmFlickrUpload.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * 
		 * } }); mnFile.add(mntmFlickrUpload);
		 */
		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		createFilterMenus(menuBar);
	}

	private void createFilterMenus(JMenuBar menuBar) {
		JMenu mnAlbum = new JMenu("Album");
		menuBar.add(mnAlbum);

		JMenuItem mntmAllAlbums = new JMenuItem("All Albums");
		mnAlbum.add(mntmAllAlbums);
		mntmAllAlbums.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Database.setAlbum(null);
				albumRecords = Database.getRecords();
				try {
					imagePanel.update(albumRecords.next());
				} catch (Exception e1) {
					CommonUtils.exception(e1);
				}
			}
		});
		mnAlbum.addSeparator();
		for (String album : Database.getAllAlbums()) {
			JMenuItem mnmt = new JMenuItem(album);
			mnmt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Database.setAlbum(album);
					albumRecords = Database.getRecords();
					try {
						imagePanel.update(albumRecords.next());
					} catch (Exception e1) {
						CommonUtils.exception(e1);
					}
				}
			});
			mnAlbum.add(mnmt);
		}

		JMenu mnTypes = new JMenu("Types");
		menuBar.add(mnTypes);
		JMenuItem mntmAllTypes = new JMenuItem("All Albums");
		mnTypes.add(mntmAllTypes);
		mntmAllTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Database.setType(null);
				albumRecords = Database.getRecords();
				try {
					imagePanel.update(albumRecords.next());
				} catch (Exception e1) {
					CommonUtils.exception(e1);
				}
			}
		});
		mnTypes.addSeparator();
		for (ImageType it : ImageType.values()) {
			JMenuItem mnmt = new JMenuItem(it.toString());
			mnmt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Database.setType(it);
					albumRecords = Database.getRecords();
					try {
						imagePanel.update(albumRecords.next());
					} catch (Exception e1) {
						CommonUtils.exception(e1);
					}
				}
			});
			mnTypes.add(mnmt);
		}
	}

}
