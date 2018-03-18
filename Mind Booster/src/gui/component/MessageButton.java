package gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import constants.CustomColor;
import gui.util.JFontChooser;
import utility.FontPicker;

public class MessageButton extends JPanel {

	private static final long serialVersionUID = -890456094498670386L;
	private Preferences prefs;
	private JPopupMenu menu;
	private JLabel label;
	private Color activeColour, activeBackground;
	private GreenSwitch circlePanel;
	private final String categoryName;
	private ImageIcon image;
	private double btnToScreenWRatio, btnToScreenHRatio;
	private boolean active = false;
	private JRadioButtonMenuItem bgOff, bgOn;
	private Font font;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public MessageButton(final String categoryName, int x, int y, int w, int h) {
		this.categoryName = categoryName;
		setBounds(x, y, w, h);

		prefs = Preferences.userRoot().node(this.getClass().getName());
		active = prefs.getBoolean(categoryName + "active", false);
		activeColour = new Color(prefs.getInt(categoryName + "colorforeground", 000000));
		activeBackground = new Color(prefs.getInt(categoryName + "colorbackground", -1));

		createMenu();

		font = FontPicker.getFont(FontPicker.latoBlack, 36);

		label = new JLabel(categoryName, JLabel.CENTER);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setForeground(activeColour);
		label.setBackground(activeBackground);
		label.setFont(font);
		label.setOpaque(true);

		circlePanel = new GreenSwitch(CustomColor.green);
		circlePanel.setActiveColour(active ? CustomColor.green : CustomColor.lightGrey);
		circlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		circlePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isActive()) {
					circlePanel.setActiveColour(CustomColor.lightGrey);
					circlePanel.repaint();
					setActive(false);
					prefs.putBoolean(categoryName + "active", false);
				} else {
					circlePanel.setActiveColour(CustomColor.green);
					circlePanel.repaint();
					setActive(true);
					prefs.putBoolean(categoryName + "active", true);
				}
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(label, BorderLayout.CENTER);
		add(circlePanel);
		setOpaque(true);
		setBackground(Color.WHITE);

		if (isActive()) {
			circlePanel.setActiveColour(CustomColor.green);
			setActive(true);
		} else {
			circlePanel.setActiveColour(CustomColor.lightGrey);
			setActive(false);
		}
	}

	private void createMenu() {
		this.menu = new JPopupMenu();

		bgOff = new JRadioButtonMenuItem("Background Off");
		bgOff.setFont(FontPicker.getFont(FontPicker.latoBlack, 20));
		bgOff.setSelected(prefs.getBoolean(categoryName + "bgOff", true));
		bgOff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.putBoolean(categoryName + "bgOn", false);
				prefs.putBoolean(categoryName + "bgOff", true);
			}
		});

		bgOn = new JRadioButtonMenuItem("Background On");
		bgOn.setFont(FontPicker.getFont(FontPicker.latoBlack, 20));
		bgOn.setSelected(prefs.getBoolean(categoryName + "bgOn", true));
		bgOn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.putBoolean(categoryName + "bgOn", true);
				prefs.putBoolean(categoryName + "bgOff", false);
			}
		});

		ButtonGroup bg = new ButtonGroup();
		bg.add(bgOff);
		bg.add(bgOn);

		JMenuItem foregroundPickerItem = new JMenuItem("Choose Colour");
		foregroundPickerItem.setFont(FontPicker.getFont(FontPicker.latoBlack, 20));
		foregroundPickerItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(null, "Pick Color", getBackground());
				if (newColor != null) {
					prefs.putInt(categoryName + "colorforeground", newColor.getRGB());
					label.setForeground(newColor);
					label.repaint();
				}
			}
		});

		JMenuItem backgroundPickerItem = new JMenuItem("Choose Background");
		backgroundPickerItem.setFont(FontPicker.getFont(FontPicker.latoBlack, 20));
		backgroundPickerItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (bgOn.isSelected()) {
					Color newColor = JColorChooser.showDialog(null, "Pick Color", getBackground());
					if (newColor != null) {
						prefs.putInt(categoryName + "colorbackground", newColor.getRGB());
						label.setBackground(newColor);
						label.repaint();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Background selector is turned off. Turn it on to change it.",
							"Message Background", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		JMenuItem fontPickerItem = new JMenuItem("Choose Font");
		fontPickerItem.setFont(FontPicker.getFont(FontPicker.latoBlack, 20));
		fontPickerItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFontChooser fontChooser = new JFontChooser();
				fontChooser.setSelectedFontFamily(font.getFamily());
				fontChooser.setSelectedFontSize(font.getSize());
				fontChooser.setSelectedFontStyle(font.getStyle());
				int result = fontChooser.showDialog(null);
				if (result == JFontChooser.OK_OPTION) {
					setFont(fontChooser.getSelectedFont());
					label.setFont(font);
				}
			}
		});

		menu.add(foregroundPickerItem);
		menu.add(bgOff);
		menu.add(bgOn);
		menu.add(backgroundPickerItem);
		menu.add(fontPickerItem);
	}

	@Override
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getActiveColour() {
		return label.getForeground();
	}

	public void setActiveColour(Color activeColour) {
		this.activeColour = activeColour;
	}

	public boolean isBackgroundSelected() {
		return bgOff.isSelected() ? false : true;
	}

	public Color getActiveBackground() {
		return label.getBackground();
	}

	public void setActiveBackground(Color activeBackground) {
		this.activeBackground = activeBackground;
	}

	public double getBtnToScreenWRatio() {
		return btnToScreenWRatio;
	}

	public void setBtnToScreenWRatio(double btnToScreenWRatio) {
		this.btnToScreenWRatio = btnToScreenWRatio;
	}

	public double getBtnToScreenHRatio() {
		return btnToScreenHRatio;
	}

	public void setBtnToScreenHRatio(double btnToScreenHRatio) {
		this.btnToScreenHRatio = btnToScreenHRatio;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}
}