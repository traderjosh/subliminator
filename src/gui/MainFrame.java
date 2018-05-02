package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Network.SocketBinder;
import controller.MessageController;
import controller.UserController;
import database.Database;
import gui.category.CategoryEvent;
import gui.category.CategoryListener;
import gui.category.CategoryPanel;
import gui.component.ProfileDropdownLabel;
import gui.controls.ControlPanel;
import gui.controls.MessageEvent;
import gui.controls.MessageListener;
import gui.dialogs.AddMessage;
import gui.dialogs.DeleteMessage;
import gui.dialogs.EditImage;
import gui.dialogs.EditMessage;
import gui.login.LoginEvent;
import gui.login.LoginFrame;
import gui.login.LoginListener;
import gui.login.LogoutEvent;
import gui.message.MessagePanel;
import gui.settings.SettingsEvent;
import gui.settings.SettingsListener;
import gui.settings.SettingsPanel;
import gui.util.CreateMenuBar;
import gui.util.IconFetch;
import gui.util.MBSystemTray;
import gui.util.SetScreenLocation;
import model.Message;
import model.User;
import utility.FontPicker;
import validation.ArrayValidator;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements CategoryListener, MessageListener, SettingsListener, LoginListener {

	public static final String appName = "Subliminator";
	private MessageController messageController;
	private UserController userController;
	private Database database;
	private LoginFrame loginFrame;
	private CategoryPanel categoryPanel;
	private MessagePanel messagePanel;
	private SettingsPanel settingsPanel;
	private ControlPanel controlPanel;
	private JDesktopPane desktopPane;
	private JPanel container;
	private MBSystemTray hideToSystemTray;
	private ProfileDropdownLabel profileDropdownLabel;
	private JLabel errorMsg;
	private UserProfileMenu userProfileMenu;

	public MainFrame() {

		if (SocketBinder.isApplicationRunning() == true) {
			JOptionPane.showMessageDialog(null, "More than one instance of this program is not supported",
					"Two instances", JOptionPane.ERROR_MESSAGE);
			dispose();
		} else {
			database = new Database();

			loginFrame = new LoginFrame();
			loginFrame.setLoginListener(this);

			messageController = new MessageController();
			userController = new UserController(database);

			categoryPanel = new CategoryPanel(messageController);
			categoryPanel.setCategorySelectionListener(this);

			messagePanel = new MessagePanel(messageController);
			messagePanel.setMessageStartListener(this);

			settingsPanel = new SettingsPanel(messageController.getSpeed(), messageController.getInterval());
			settingsPanel.setSettingsListener(this);

			controlPanel = new ControlPanel();
			controlPanel.setMessageStartListener(this);

			initComponents();
			setupUI();

			this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent evt) {
					container.setBounds(0, 0, (int) (getWidth() * 1.0), (int) (getHeight() * 1.0));
					profileDropdownLabel.revalidate();
					profileDropdownLabel.repaint();
					revalidate();
					repaint();
				}
			});
			setTitle(appName);
			setJMenuBar(new CreateMenuBar(messageController, controlPanel, messagePanel));
			setIconImage(IconFetch.getInstance().getIcon("/images/icon.png").getImage());
			setPreferredSize(new Dimension((int) (SetScreenLocation.screenSize.width * 0.95), (int) (SetScreenLocation.screenSize.height * 0.9)));
			setMinimumSize(new Dimension(new Dimension((int) (SetScreenLocation.screenSize.width * 0.85), (int) (SetScreenLocation.screenSize.height * 0.88))));
			pack();
			SetScreenLocation.centerFrame(this);
			setVisible(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}

	private void initComponents() {
		hideToSystemTray = new MBSystemTray(this);

		errorMsg = new JLabel();
		errorMsg.setForeground(Color.RED);
		errorMsg.setFont(FontPicker.getFont(FontPicker.latoBold, 18));
		errorMsg.setVisible(false);

		profileDropdownLabel = new ProfileDropdownLabel();
		profileDropdownLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				userProfileMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		userProfileMenu = new UserProfileMenu(userController, profileDropdownLabel, settingsPanel);
		userProfileMenu.setLoginListener(this);
	}

	private void setupUI() {
		container = new JPanel(new GridBagLayout());
		container.setVisible(true);
		container.setBackground(Color.decode("#efeff0"));

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.4;
		gbc.weighty = 0.8;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 0, 0);
		container.add(errorMsg, gbc);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.3;
		gbc.weighty = 0.65;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		container.add(categoryPanel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.1;
		gbc.weighty = 0.65;
		gbc.insets = new Insets(0, 0, 0, 0);
		container.add(messagePanel, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 20, 0, 20);
		container.add(profileDropdownLabel, gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.65;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 0, 0, 0);
		container.add(settingsPanel, gbc);

		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.35;
		gbc.insets = new Insets(0, 0, 0, 0);
		container.add(controlPanel, gbc);

		desktopPane = new JDesktopPane();
		desktopPane.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		desktopPane.add(container);
		add(desktopPane);
	}

	private void runMessageActivity(boolean[] screenPositions) throws InterruptedException {
		hideToSystemTray.hide();
		messageController.setSpeed(settingsPanel.getSpeed());
		messageController.setInterval(settingsPanel.getInterval());
		messageController.setActiveMessages(messagePanel.getSelectedMessages());
		messageController.startMessageActivity(screenPositions, settingsPanel.getMessageButtons());
		messageController.setMessagesOn(true);
		errorMsg.setText("");
		errorMsg.setVisible(false);
	}

	@Override
	public void messageEventOccurred(MessageEvent event) {
		try {
			if (messageController.isMessagesOn() == false) {
				if (messagePanel.getSelectedMessages().isEmpty()) {
					errorMsg.setText("No messages selected");
					errorMsg.setVisible(true);
					controlPanel.showStartButton();
				} else {
					if (ArrayValidator.areAllFalse(settingsPanel.getSelectedScreenPositions())) {
						errorMsg.setText("No message locations selected");
						errorMsg.setVisible(true);
						controlPanel.showStartButton();
					} else {
						if (userController.isUserPremium() == true) {
							runMessageActivity(settingsPanel.getSelectedScreenPositions());
						} else if (userController.isUserPremium() == false) {
							if (ArrayValidator.isMoreThanOneTrue(settingsPanel.getSelectedScreenPositions())) {
								userController.showPremiumPopup();
								controlPanel.showStartButton();
							} else {
								runMessageActivity(settingsPanel.getSelectedScreenPositions());
							}
						}
					}
				}
			} else if (messageController.isMessagesOn() == true) {
				messageController.stopMessageActivity();
				messageController.setMessagesOn(false);
			}
		} catch (InterruptedException e) {
			System.err.println("Failed to play messages");
		}
	}

	@Override
	public void messageSelectionEventOccurred(MessageEvent e) {
		if (e.isAllMessagesSelected() == true) {
			messagePanel.getMessageListSelectionModel().setAllMessagesActive();
		} else {
			messagePanel.getMessageListSelectionModel().clearSelection();
		}
	}

	@Override
	public void categorySelectionEventOccurred(CategoryEvent e) {
		messagePanel.removeMessages();
		messageController.setCategoryIndex(e.getCategoryIndex());
		messagePanel.setMessageList(messageController.getMessagesFromActiveTenseCategory());
		controlPanel.resetSelection();
	}

	@Override
	public void addMessageEventOccurred(MessageEvent e) {
		AddMessage addMessage = new AddMessage(messageController, messagePanel);
		desktopPane.add(addMessage);
		addMessage.setLocation((desktopPane.getWidth() - addMessage.getWidth()) / 2,
				(desktopPane.getHeight() - addMessage.getHeight()) / 2);
		addMessage.setVisible(true);
		messageController.save();
	}

	@Override
	public void editMessageEventOccurred(MessageEvent e) {

		if (messagePanel.getMessageListSelectionModel()
				.isSelectedIndex(messagePanel.getMessageListSelectionModel().getLastSelection())) {
			EditMessage editMessage = new EditMessage(messageController, messagePanel,
					messagePanel.getMessageListSelectionModel().getLastSelection());
			desktopPane.add(editMessage);
			editMessage.setLocation((desktopPane.getWidth() - editMessage.getWidth()) / 2,
					(desktopPane.getHeight() - editMessage.getHeight()) / 2);
			editMessage.setVisible(true);
			errorMsg.setVisible(false);
			messageController.save();
		} else {
			errorMsg.setText("You need to select a message to edit");
			errorMsg.setVisible(true);
		}
	}

	@Override
	public void deleteMessageEventOccurred(MessageEvent e) {
		int selectedMsg = messagePanel.getMessageList().getSelectedIndex();
		DeleteMessage deleteMessage = new DeleteMessage(messageController, messagePanel, selectedMsg);
		desktopPane.add(deleteMessage);
		deleteMessage.setLocation((desktopPane.getWidth() - deleteMessage.getWidth()) / 2,
				(desktopPane.getHeight() - deleteMessage.getHeight()) / 2);
		deleteMessage.setVisible(true);
		errorMsg.setVisible(false);
		messageController.save();
	}

	@Override
	public void editImageEventOccurred(MessageEvent e) {
		if (messagePanel.getMessageListSelectionModel().getLastSelection() < 0) {
			errorMsg.setText("You need to select a message to edit");
			errorMsg.setVisible(true);
		} else {
			Message message = messagePanel.getMessageList().getSelectedValue();
			EditImage editImage = new EditImage(messageController, message, messagePanel,
					messagePanel.getMessageListSelectionModel().getLastSelection());
			desktopPane.add(editImage);
			editImage.setLocation((desktopPane.getWidth() - editImage.getWidth()) / 2,
					(desktopPane.getHeight() - editImage.getHeight()) / 2);
			editImage.setVisible(true);
			errorMsg.setVisible(false);
			messageController.save();
		}
	}

	@Override
	public void settingsEventOccurred(SettingsEvent e) {
		messageController.setSpeed(e.getMessageSpeed());
		messageController.setInterval(e.getMessageInterval());
	}

	@Override
	public void loginEventOccurred(LoginEvent event) {

		String email = event.getUser();
		String pass = event.getPass();

		if (userController.isTempUserSelected(email)) {

			userController.setUserPremium(false);
			userController.runPremiumPrompter();

			settingsPanel.checkForActiveMessages();
			settingsPanel.deactivateActiveMessages();

			userProfileMenu.createMenuItemsForTempUser();

			profileDropdownLabel.setToUnregistered();

			setVisible(true);
			loginFrame.dispose();
		} else {

			if (database.connect() == false) {
				loginFrame.setErrorMessage("Unable to connect to the internet");
			} else {
				String errorMessage = userController.login(email, pass);

				if (userController.isLoggedIn() == false) {

					loginFrame.setErrorMessage(errorMessage);

				} else if (userController.isLoggedIn() == true) {
					User user = userController.getUser();
					profileDropdownLabel.setText(user.getFirstName() + " " + user.getLastName());

					settingsPanel.checkForActiveMessages();

					if (user.isUserPremium() == true) {

						userProfileMenu.createMenuItemsForPremiumUser();

						profileDropdownLabel.setToPremium();

						SettingsPanel.isUserPremium = true;
						userController.setUserPremium(true);
						userController.stopPremiumPrompter();

					} else if (user.isUserPremium() == false) {

						profileDropdownLabel.setToBasic();

						SettingsPanel.isUserPremium = false;
						userController.runPremiumPrompter();

						userProfileMenu.createMenuItemsForBasicUser();

						if (settingsPanel.isMoreThanOneMsgSelected() == true) {
							settingsPanel.deactivateActiveMessages();
						}
					}
					setVisible(true);
					loginFrame.dispose();
				}
			}
		}
	}

	@Override
	public void logoutEventOccurred(LogoutEvent event) {
		dispose();

		userController.setLoggedIn(false);

		userProfileMenu.removeAll();

		SettingsPanel.isUserPremium = false;
		userController.setUserPremium(false);

		userController.stopPremiumPrompter();

		loginFrame = new LoginFrame();
		loginFrame.setUserAndPassFields(userController.getUser().getEmail(), userController.getUser().getPassword());
		loginFrame.setLoginListener(this);
	}
}