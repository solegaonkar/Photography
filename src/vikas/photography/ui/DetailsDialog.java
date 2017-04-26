package vikas.photography.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import vikas.photography.framework.Database;
import vikas.photography.framework.Database.Record;

public class DetailsDialog extends JDialog {
	private static final long	serialVersionUID	= -4750573851210548807L;
	private final JPanel		contentPanel		= new JPanel();
	private JTextField			textFieldTitle		= new JTextField();
	private JTextArea			textAreaInfo		= new JTextArea(6, 60);

	/**
	 * Create the dialog.
	 * 
	 * @throws Exception
	 */
	public DetailsDialog(Record record) {
		super();
		setTitle("Details");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(5, 5));

		addTextComponents(record);
		createAlbumPanel(record);

		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();

		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		setLocation((w - getSize().width) / 2, (h - getSize().height) / 2);
	}

	private void createAlbumPanel(Record record) {
		JPanel albumsPanel = new JPanel();
		albumsPanel.setLayout(new GridLayout(0, 6, 3, 3));
		JLabel lblNew = new JLabel("New Album");
		lblNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String album = JOptionPane.showInputDialog("New Album Name:");
				if (album != null && album.length() > 0) {
					AlbumCheckBox acb = new AlbumCheckBox(album, record);
					acb.setSelected(true);
					albumsPanel.add(acb);
					Database.getAllAlbums().add(album);
					albumsPanel.validate();
					albumsPanel.repaint();
				}
			}
		});
		albumsPanel.add(lblNew);
		for (String album : Database.getAllAlbums()) {
			AlbumCheckBox acb = new AlbumCheckBox(album, record);
			if (record.getAlbums().contains(album)) {
				acb.setSelected(true);
			}
			albumsPanel.add(acb);
		}
		contentPanel.add(albumsPanel, BorderLayout.SOUTH);
	}

	private void addTextComponents(Record record) {
		textFieldTitle.setText(record.getTitle());
		textFieldTitle.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Title"));
		textFieldTitle.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				record.setTitle(textFieldTitle.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				record.setTitle(textFieldTitle.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				record.setTitle(textFieldTitle.getText());
			}
		});
		textAreaInfo.setText(record.getInfo());
		textAreaInfo.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				record.setInfo(textAreaInfo.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				record.setInfo(textAreaInfo.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				record.setInfo(textAreaInfo.getText());
			}
		});

		contentPanel.add(textFieldTitle, BorderLayout.NORTH);
		textAreaInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "More Info"));
		textAreaInfo.setAutoscrolls(true);
		contentPanel.add(textAreaInfo, BorderLayout.CENTER);
	}

	private static class AlbumCheckBox extends JCheckBox {
		private static final long serialVersionUID = -489432572970882597L;

		public AlbumCheckBox(String album, Record record) {
			super(album);

			this.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (isSelected()) {
						record.getAlbums().add(album);
					} else {
						record.getAlbums().remove(album);
					}
				}
			});
		}

	}

}
