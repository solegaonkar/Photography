package vikas.photography.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
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
		setTitle("Photo Album");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds(w / 4, h / 4, w / 2, h / 2);

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

	private DropTarget			dropTarget			= new DropTarget(this, DnDConstants.ACTION_MOVE, null);;
	private DropTargetHandler	dropTargetHandler	= new DropTargetHandler();


	@Override
	public void addNotify() {
		super.addNotify();
		try {
			dropTarget.addDropTargetListener(dropTargetHandler);
		} catch (TooManyListenersException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void removeNotify() {
		super.removeNotify();
		dropTarget.removeDropTargetListener(dropTargetHandler);
	}

	protected void importFiles(final List files) {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				setVisible(false);
				AddNewImages.start(files);
				createMenuBar();
				setVisible(true);
			}
		};
		SwingUtilities.invokeLater(run);
	}

	protected class DropTargetHandler implements DropTargetListener {

		protected void processDrag(DropTargetDragEvent dtde) {
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrag(DnDConstants.ACTION_MOVE);
			} else {
				dtde.rejectDrag();
			}
		}

		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			processDrag(dtde);
		}

		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			processDrag(dtde);
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {
		}

		@Override
		public void dragExit(DropTargetEvent dte) {
		}

		@Override
		public void drop(DropTargetDropEvent dtde) {

			Transferable transferable = dtde.getTransferable();
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrop(dtde.getDropAction());
				try {
					List transferData = (List) transferable.getTransferData(DataFlavor.javaFileListFlavor);
					if (transferData != null && transferData.size() > 0) {
						importFiles(transferData);
						dtde.dropComplete(true);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				dtde.rejectDrop();
			}
		}
	}


}
