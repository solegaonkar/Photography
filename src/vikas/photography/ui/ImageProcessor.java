package vikas.photography.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import vikas.photography.framework.CommonUtils;
import vikas.photography.framework.Constants;
import vikas.photography.image.EnhanceColor;
import vikas.photography.image.Monochrome;
import vikas.photography.image.Neon;
import vikas.photography.image.Pencil;
import vikas.photography.image.Photograph;
import vikas.photography.image.Photograph.Processor;
import vikas.photography.image.ScaleImage;
import vikas.photography.image.ScaleImage.Size;
import vikas.photography.image.SignImage;

public class ImageProcessor extends JFrame {
	private static final long					serialVersionUID	= -7859144435285217145L;
	private static final SynchronousQueue<File>	jpgFiles			= new SynchronousQueue<File>();

	private static final Processor				scale				= new ScaleImage(Size.S01024);
	private static final Processor				sign				= new SignImage("Vikas Solegaonkar");
	private static final Processor				enhance				= new EnhanceColor();
	private static final Processor				monochrome			= new Monochrome(new Color(255, 255, 255));
	private static final Processor				pencil				= new Pencil();
	private static final Processor				neon				= new Neon();
	private static final String					waiting				= "Waiting for the next image in input folder";

	private JPanel								contentPane;
	private JTabbedPane							tabbedPane			= new JTabbedPane(JTabbedPane.TOP);
	private JLabel								lblImageInfo		= new JLabel();
	private JLabel								lblWaiting			= new JLabel(waiting);

	/**
	 * Launch the application.
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageProcessor frame = new ImageProcessor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		for (File file : new File(Constants.InputPath).listFiles()) {
			jpgFiles.put(file);
		}
	}

	/**
	 * Create the frame.
	 */
	public ImageProcessor() {
		setTitle("Photo Album");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processNext();
			}
		});
		menuBar.add(btnNext);

		JButton btnDetails = new JButton("Details");
		btnDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		menuBar.add(btnDetails);

		JButton btnFlickr = new JButton("Flickr");
		btnFlickr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		menuBar.add(btnFlickr);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(tabbedPane, BorderLayout.CENTER);
		contentPane.add(lblImageInfo, BorderLayout.SOUTH);
	}

	/**
	 * Invoke the set of processors one after other and save the photograph at appropriate points.
	 * 
	 * @throws Exception
	 */
	private void processNext() {
		tabbedPane.removeAll();
		contentPane.add(lblWaiting, BorderLayout.CENTER);

		try {
			Photograph photograph = new Photograph(jpgFiles.take());

			tabbedPane.addTab("Plain",
					new ImagePanel(photograph.start().apply(scale).apply(sign), Constants.PlainPath));
			tabbedPane.addTab("Enhanced",
					new ImagePanel(photograph.start().apply(scale).apply(sign).apply(enhance), Constants.EnhancedPath));
			tabbedPane.addTab("Monochrome", new ImagePanel(
					photograph.start().apply(scale).apply(sign).apply(monochrome), Constants.MonochromePath));
			tabbedPane.addTab("Neon",
					new ImagePanel(photograph.start().apply(scale).apply(sign).apply(neon), Constants.NeonPath));
			tabbedPane.addTab("Pencil",
					new ImagePanel(photograph.start().apply(scale).apply(sign).apply(pencil), Constants.PencilPath));

			contentPane.add(tabbedPane, BorderLayout.CENTER);
			lblImageInfo.setText(photograph.getInfo());

			Files.move(Paths.get(photograph.getSourceFile().getAbsolutePath()),
					Paths.get(photograph.getTargetFile(Constants.OriginalsPath).getAbsolutePath()));

		} catch (Exception e) {
			CommonUtils.exception(e);
		}
		System.gc();
	}
}
