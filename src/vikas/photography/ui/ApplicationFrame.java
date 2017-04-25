package vikas.photography.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import vikas.photography.framework.CommonUtils;
import vikas.photography.framework.Constants;
import vikas.photography.framework.Database.ImageType;
import vikas.photography.framework.Database.Record;
import vikas.photography.image.EnhanceColor;
import vikas.photography.image.Monochrome;
import vikas.photography.image.Neon;
import vikas.photography.image.Pencil;
import vikas.photography.image.Photograph;
import vikas.photography.image.Photograph.Processor;
import vikas.photography.image.ScaleImage;
import vikas.photography.image.ScaleImage.Size;
import vikas.photography.image.SignImage;

public class ApplicationFrame extends JFrame {
	private static final long					serialVersionUID	= -7859144435285217145L;
	private static final SynchronousQueue<File>	jpgFiles			= new SynchronousQueue<File>();

	private static final Processor				scale				= new ScaleImage(Size.S01024);
	private static final Processor				sign				= new SignImage("Vikas Solegaonkar");
	private static final Processor				enhance				= new EnhanceColor();
	private static final Processor				monochrome			= new Monochrome(new Color(255, 255, 255));
	private static final Processor				pencil				= new Pencil();
	private static final Processor				neon				= new Neon();

	private static JTabbedPane					tabbedPane			= new JTabbedPane(JTabbedPane.TOP);
	private static boolean						complete			= false;
	private static JPanel						contentPane;
	private static JPanel						panelButtons		= new JPanel();
	private static JButton						btnNext				= new JButton("Next");
	private static JButton						btnFlickr			= new JButton("Flickr");
	private static JButton						btnDetails			= new JButton("Details");

	private static Record						record				= null;

	/**
	 * Launch the application.
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ApplicationFrame frame = new ApplicationFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		for (File file : new File(Constants.InputPath).listFiles()) {
			jpgFiles.put(file);
		}
		complete = true;
	}

	/**
	 * Create the frame.
	 */
	public ApplicationFrame() {
		setTitle("Photo Album");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(100, 100, 665, 437);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processNext();
			}
		});

		btnDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DetailsDialog dd = new DetailsDialog(record);
				dd.setVisible(true);
			}
		});

		btnFlickr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panelButtons.add(btnDetails);
		panelButtons.add(btnNext);
		panelButtons.add(btnFlickr);
		contentPane.add(panelButtons, BorderLayout.NORTH);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		// processNext();
	}

	/**
	 * Invoke the set of processors one after other and save the photograph at appropriate points.
	 * 
	 * @throws Exception
	 */
	private void processNext() {
		try {
			if (!complete)
				new PhotographLoader(jpgFiles.take(), this).execute();
		} catch (Exception e) {
			CommonUtils.exception(e);
		}
		System.gc();
	}

	public static class PhotographLoader extends SwingWorker<String, Void> {
		private JFrame	frame;
		private File	jpg;

		public PhotographLoader(File jpg, JFrame frame) {
			this.jpg = jpg;
			this.frame = frame;
		}

		@Override
		protected String doInBackground() {
			try {
				CommonUtils.log("Starting PhotoLoader Background Thread");
				frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				record = new Record();
				tabbedPane.removeAll();
				Photograph photograph = new Photograph(jpg);
				tabbedPane.addTab("Plain", new ImagePanel(photograph.start().apply(scale).apply(sign),
						Constants.PlainPath, ImageType.Color, record));
				tabbedPane.addTab("Enhanced", new ImagePanel(photograph.start().apply(scale).apply(sign).apply(enhance),
						Constants.EnhancedPath, ImageType.Color, record));
				tabbedPane.addTab("Monochrome",
						new ImagePanel(photograph.start().apply(scale).apply(sign).apply(monochrome),
								Constants.MonochromePath, ImageType.Monochrome, record));
				tabbedPane.addTab("Neon", new ImagePanel(photograph.start().apply(scale).apply(sign).apply(neon),
						Constants.NeonPath, ImageType.Neon, record));
				tabbedPane.addTab("Pencil", new ImagePanel(photograph.start().apply(scale).apply(sign).apply(pencil),
						Constants.PencilPath, ImageType.Pencil, record));
				Files.move(Paths.get(photograph.getSourceFile().getAbsolutePath()),
						Paths.get(photograph.getTargetFile(Constants.OriginalsPath).getAbsolutePath()));
				CommonUtils.log("End of PhotoLoader Background Thread");
			} catch (Exception e) {
				CommonUtils.exception(e);
			}
			return null;
		}

		@Override
		protected void done() {
			if (complete)
				btnNext.setEnabled(false);
			frame.setCursor(Cursor.getDefaultCursor());
		}
	}
}
