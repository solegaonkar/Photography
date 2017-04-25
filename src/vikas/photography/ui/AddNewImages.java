package vikas.photography.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
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

public class AddNewImages extends JDialog {
	private static final long					serialVersionUID	= -7859144435285217145L;
	private static final SynchronousQueue<File>	jpgFiles			= new SynchronousQueue<File>();

	private static final Processor				scale				= new ScaleImage(Size.S01024);
	private static final Processor				sign				= new SignImage("Vikas Solegaonkar");
	private static final Processor				enhance				= new EnhanceColor();
	private static final Processor				monochrome			= new Monochrome(new Color(255, 255, 255));
	private static final Processor				pencil				= new Pencil();
	private static final Processor				neon				= new Neon();

	private static final JTabbedPane			tabbedPane			= new JTabbedPane(JTabbedPane.TOP);
	private static final JPanel					contentPane			= new JPanel();
	private static final JButton				btnNext				= new JButton("Process the Next Photograph");

	private static Record						record				= null;
	private static volatile boolean				complete			= false;

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
					AddNewImages frame = new AddNewImages();
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
	public AddNewImages() {
		setTitle("Photo Album");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, Toolkit.getDefaultToolkit().getScreenSize().width - 100,
				Toolkit.getDefaultToolkit().getScreenSize().height - 100);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		contentPane.add(btnNext, BorderLayout.SOUTH);
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					btnNext.setEnabled(false);
					if (!complete) {
						PhotographLoader pl = new PhotographLoader(jpgFiles.take(), contentPane);
						pl.execute();
					}
				} catch (InterruptedException e) {
					CommonUtils.exception(e);
				}
			}
		});
	}

	public static class PhotographLoader extends SwingWorker<String, Void> {
		private Component	parent;
		private File		jpg;

		public PhotographLoader(File jpg, Component parent) {
			this.jpg = jpg;
			this.parent = parent;
		}

		@Override
		protected String doInBackground() {
			try {
				CommonUtils.log("Starting PhotoLoader Background Thread");
				parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				record = new Record();
				tabbedPane.removeAll();
				Photograph photograph = new Photograph(jpg);
				{
					ImagePanel ip = new ImagePanel(photograph.start().apply(scale).apply(sign).getWip());
					ip.createMouseListener(photograph, Constants.PlainPath, ImageType.Color, record);
					tabbedPane.addTab("Plain", ip);
				}
				{
					ImagePanel ip = new ImagePanel(photograph.start().apply(scale).apply(sign).apply(enhance).getWip());
					ip.createMouseListener(photograph, Constants.EnhancedPath, ImageType.Color, record);
					tabbedPane.addTab("Enhanced", ip);
				}
				{
					ImagePanel ip =
							new ImagePanel(photograph.start().apply(scale).apply(sign).apply(monochrome).getWip());
					ip.createMouseListener(photograph, Constants.MonochromePath, ImageType.Monochrome, record);
					tabbedPane.addTab("Monochrome", ip);
				}
				{
					ImagePanel ip = new ImagePanel(photograph.start().apply(scale).apply(sign).apply(neon).getWip());
					ip.createMouseListener(photograph, Constants.NeonPath, ImageType.Neon, record);
					tabbedPane.addTab("Neon", ip);
				}
				{
					ImagePanel ip = new ImagePanel(photograph.start().apply(scale).apply(sign).apply(pencil).getWip());
					ip.createMouseListener(photograph, Constants.PencilPath, ImageType.Pencil, record);
					tabbedPane.addTab("Pencil", ip);
				}
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
			if (!complete)
				btnNext.setEnabled(true);
			parent.setCursor(Cursor.getDefaultCursor());
		}
	}
}