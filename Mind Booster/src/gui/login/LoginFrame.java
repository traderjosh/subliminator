package gui.login;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import gui.component.LoginButton;
import gui.component.RegisterButton;
import gui.util.IconFetch;
import gui.util.SetScreenLocation;
import utility.FontPicker;

public class LoginFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6685039386661838526L;
	public static final int W = 750, H = 750, minW = 700, minH = 700;
	private final String tempPass = "MjMGqzdkMSs4K4PNkN454Ufc";
	private JButton logo;
	private JLabel loginLbl, loginReminderLbl, registerLbl;
	private JLabel userLbl, passLbl;
	private JLabel errorLbl;
	private JTextField userTxt;
	private JPasswordField passTxt;
	private JButton submitBtn, tempLoginBtn, registerBtn;
	private LoginListener loginListener;

	public LoginFrame() {
		initComponents();
		setupUI();

		setTitle("Login");
		getContentPane().setBackground(Color.decode("#A9E2F3"));
		setPreferredSize(new Dimension(W, H));
		setMinimumSize(new Dimension(minW, minH));
		SetScreenLocation.centerFrame(this);
		setResizable(true);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void initComponents() {
		ImageIcon icon = IconFetch.getInstance().getIcon("/images/logo-small.png");

		logo = new JButton();
		logo.setIcon(icon);
		logo.setOpaque(false);
		logo.setContentAreaFilled(false);
		logo.setBorderPainted(false);
		logo.setFocusPainted(false);
		logo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					URI uri = new URI("https://www.psychotechnology.com/");
					Desktop dt = Desktop.getDesktop();
					dt.browse(uri);
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		loginLbl = new JLabel("Log In");
		loginLbl.setFont(FontPicker.getFont(FontPicker.latoRegular, 32));

		loginReminderLbl = new JLabel("Please enter your Username and Password");
		loginReminderLbl.setFont(FontPicker.getFont(FontPicker.latoRegular, 18));
		loginReminderLbl.setForeground(Color.BLACK);

		registerLbl = new JLabel("Not registered? Click the button below to register for a FREE account");
		registerLbl.setFont(FontPicker.getFont(FontPicker.latoRegular, 18));
		registerLbl.setForeground(Color.BLACK);

		errorLbl = new JLabel();
		errorLbl.setForeground(Color.RED);
		errorLbl.setFont(FontPicker.getFont(FontPicker.latoRegular, 16));

		userLbl = new JLabel("Username:");
		userLbl.setFont(FontPicker.getFont(FontPicker.latoRegular, 22));
		userLbl.setPreferredSize(new Dimension(350, 20));

		passLbl = new JLabel("Password:");
		passLbl.setFont(FontPicker.getFont(FontPicker.latoRegular, 22));
		passLbl.setPreferredSize(new Dimension(350, 110));

		userTxt = new JTextField(15);
		userTxt.setFont(FontPicker.getFont(FontPicker.latoRegular, 22));
		userTxt.setPreferredSize(new Dimension(350, 40));

		passTxt = new JPasswordField(15);
		passTxt.setFont(FontPicker.getFont(FontPicker.latoRegular, 22));
		passTxt.setPreferredSize(new Dimension(350, 40));

		submitBtn = new LoginButton("Login");
		submitBtn.addActionListener(this);

		tempLoginBtn = new LoginButton("Skip");
		tempLoginBtn.addActionListener(this);

		registerBtn = new RegisterButton("Register");
		registerBtn.addActionListener(this);
	}

	public void setUsername(String username) {
		userTxt.setText(username);
	}

	public void setPassword(String password) {
		passTxt.setText(password);
	}

	private void setupUI() {

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		setLayout(gbl);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(40, 0, 0, 0);
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		add(logo, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(0, 75, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.NONE;
		add(loginLbl, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(75, 75, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.NONE;
		add(loginReminderLbl, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(200, 75, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.NONE;
		add(errorLbl, gc);

		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(0, 75, 0, 0);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.NONE;
		add(userLbl, gc);

		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(35, 75, 0, 75);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(userTxt, gc);

		gc.gridx = 0;
		gc.gridy = 3;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(0, 75, 0, 0);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.NONE;
		add(passLbl, gc);

		gc.gridx = 0;
		gc.gridy = 3;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(75, 75, 0, 75);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(passTxt, gc);

		gc.gridx = 0;
		gc.gridy = 4;
		gc.gridheight = 1;
		gc.gridwidth = 2;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(0, 75, 0, 0);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.NONE;
		add(submitBtn, gc);

		gc.gridx = 0;
		gc.gridy = 4;
		gc.gridheight = 1;
		gc.gridwidth = 2;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(0, 215, 0, 0);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.NONE;
		add(tempLoginBtn, gc);

		gc.gridx = 0;
		gc.gridy = 5;
		gc.gridheight = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(5, 75, 0, 0);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.NONE;
		add(registerLbl, gc);

		gc.gridx = 0;
		gc.gridy = 5;
		gc.gridheight = 1;
		gc.gridwidth = 2;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		gc.insets = new Insets(35, 75, 0, 75);
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(registerBtn, gc);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == submitBtn) {
			if (loginListener != null) {
				String user = userTxt.getText();
				String pass = String.valueOf(passTxt.getPassword());
				LoginEvent loginEvent = new LoginEvent(this, user, pass);
				loginListener.loginEventOccurred(loginEvent);
			}
		} else if (ae.getSource() == tempLoginBtn) {
			if (loginListener != null) {
				LoginEvent loginEvent = new LoginEvent(this, tempPass, "");
				loginListener.loginEventOccurred(loginEvent);
			}
		} else if (ae.getSource() == registerBtn) {
			try {
				URI uri = new URI("https://www.psychotechnology.com/register");
				Desktop dt = Desktop.getDesktop();
				dt.browse(uri);
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void setLoginListener(LoginListener loginListener) {
		this.loginListener = loginListener;
	}

	public void setErrorMessage(String errorMsg) {
		errorLbl.setText(errorMsg);
	}
	
	public void setUserAndPassFields(String user, String pass) {
		userTxt.setText(user);
		passTxt.setText(pass);
	}
}
