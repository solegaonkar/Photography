package org.github.solegaonkar.photography.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.github.solegaonkar.photography.framework.Database;
import org.github.solegaonkar.photography.framework.Photograph;

public class AlbumPanel extends JScrollPane {
	private static final long			serialVersionUID	= -2805747012132346940L;
	private HashMap<String, Boolean>	albums				= new HashMap<String, Boolean>();
	private JPanel						panel				= null;

	public AlbumPanel() {
		super();
		this.setWheelScrollingEnabled(true);
	}

	public void setPhotograph(Photograph photograph) {
		panel = new JPanel();
		viewport.removeAll();
		viewport.add(panel);
		panel.setLayout(new GridLayout(0, 1));
		createButton(photograph);
		createCheckboxes(photograph);
	}

	private void createCheckboxes(Photograph photograph) {
		albums.clear();
		Database.getDb().getAlbums().forEach(a -> albums.put(a, false));
		photograph.getAlbums().forEach(a -> albums.put(a, true));
		for (Entry<String, Boolean> entry : albums.entrySet()) {
			JCheckBox cb = new JCheckBox(entry.getKey());
			cb.setSelected(entry.getValue());
			cb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (cb.isSelected())
						photograph.getAlbums().add(entry.getKey());
					else
						photograph.getAlbums().remove(entry.getKey());
				}
			});
			panel.add(cb);
		}
	}

	private void createButton(Photograph photograph) {
		JButton button = new JButton("Create New Album");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = JOptionPane.showInputDialog(null, "Please enter the name of the new album");
				if (albums.containsKey(s)) {
					JOptionPane.showMessageDialog(null, "This album exists already. Please select it.\n"
							+ "If you really need a new album, try another name");
				} else {
					albums.put(s, true);
					photograph.getAlbums().add(s);
					setPhotograph(photograph);
				}
			}
		});
		panel.add(button);
	}
}
