package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import constants.MessageTense;
import data.InBuiltCategory;
import gui.component.MessageButton;
import gui.subliminal.SubliminalFrame;
import gui.subliminal.SubliminalTask;
import gui.util.SetScreenLocation;
import model.Category;
import model.Message;

public class MessageController {
	private boolean messagesOn = false;
	private ScheduledExecutorService scheduledExecutorService;
	private Preferences prefs;
	private final int startDelay = 0;
	private int speed;
	private int interval;
	private int categoryIndex;
	private ArrayList<Category> categories;
	private MessageTense messageTense;
	private SubliminalFrame frame1, frame2, frame3, frame4, frame5;
	private SubliminalTask subliminalTask1, subliminalTask2, subliminalTask3, subliminalTask4, subliminalTask5;
	private List<Message> activeMessages;

	public MessageController() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
		speed = prefs.getInt("speed", 50);
		interval = prefs.getInt("interval", 7);
		categoryIndex = prefs.getInt("categoryIndex", 0);
		messageTense = (prefs.getInt("tense", 0)) == 0 ? MessageTense.FIRST_PERSON : MessageTense.SECOND_PERSON;
		frame1 = new SubliminalFrame();
		SetScreenLocation.topLeft(frame1);
		frame1.setActive(prefs.getBoolean("topleft", false));

		frame2 = new SubliminalFrame();
		SetScreenLocation.topRight(frame2);
		frame2.setActive(prefs.getBoolean("topright", false));

		frame3 = new SubliminalFrame();
		SetScreenLocation.botLeft(frame3);
		frame3.setActive(prefs.getBoolean("bottomleft", false));

		frame4 = new SubliminalFrame();
		SetScreenLocation.botRight(frame4);
		frame4.setActive(prefs.getBoolean("bottomright", false));

		frame5 = new SubliminalFrame();
		SetScreenLocation.center(frame5);
		frame5.setActive(prefs.getBoolean("center", false));

		subliminalTask1 = new SubliminalTask(this, frame1);
		subliminalTask2 = new SubliminalTask(this, frame2);
		subliminalTask3 = new SubliminalTask(this, frame3);
		subliminalTask4 = new SubliminalTask(this, frame4);
		subliminalTask5 = new SubliminalTask(this, frame5);

		activeMessages = new ArrayList<Message>();

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

		loadStoredLists();
	}

	@SuppressWarnings("unchecked")
	private void loadStoredLists() {
		try {
			FileInputStream fileIn = new FileInputStream("data.cats");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			categories = (ArrayList<Category>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			loadInBuiltCategories();
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}
	}

	public void loadInBuiltCategories() {
		categories = new InBuiltCategory().getCateories();
	}

	public void save() {
		if (categories != null) {
			try {
				FileOutputStream fileOut = new FileOutputStream("data.cats");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(categories);
				out.close();
				fileOut.close();
			} catch (IOException i) {
				i.printStackTrace();
			}
		}
	}

	public synchronized void startMessageActivity(boolean msgLocationsSelected[], MessageButton[] messageButtons)
			throws InterruptedException {
		if (scheduledExecutorService.isShutdown()) {
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		}
		if (messageButtons[0].isActive() == true) {
			frame1.setActive(true);
			prefs.putBoolean("topleft", true);
			frame1.setFont(messageButtons[0].getFont());
			frame1.setColor(messageButtons[0].getActiveColour());
			frame1.setActiveBackground(messageButtons[0].getActiveBackground());
			frame1.setBackgroundSelected(messageButtons[0].isBackgroundSelected());
			scheduledExecutorService.scheduleWithFixedDelay(subliminalTask1, startDelay, interval, TimeUnit.SECONDS);
		} else {
			prefs.putBoolean("topleft", false);
		}
		if (messageButtons[1].isActive() == true) {
			frame2.setActive(true);
			prefs.putBoolean("topright", true);
			frame2.setFont(messageButtons[1].getFont());
			frame2.setColor(messageButtons[1].getActiveColour());
			frame2.setActiveBackground(messageButtons[1].getActiveBackground());
			frame2.setBackgroundSelected(messageButtons[1].isBackgroundSelected());
			scheduledExecutorService.scheduleWithFixedDelay(subliminalTask2, startDelay, interval, TimeUnit.SECONDS);
		} else {
			prefs.putBoolean("topright", false);
		}
		if (messageButtons[2].isActive() == true) {
			frame3.setActive(true);
			prefs.putBoolean("bottomleft", true);
			frame3.setFont(messageButtons[2].getFont());
			frame3.setColor(messageButtons[2].getActiveColour());
			frame3.setActiveBackground(messageButtons[2].getActiveBackground());
			frame3.setBackgroundSelected(messageButtons[2].isBackgroundSelected());
			scheduledExecutorService.scheduleWithFixedDelay(subliminalTask3, startDelay, interval, TimeUnit.SECONDS);
		} else {
			prefs.putBoolean("bottomleft", false);
		}
		if (messageButtons[3].isActive() == true) {
			frame4.setActive(true);
			prefs.putBoolean("bottomright", true);
			frame4.setFont(messageButtons[3].getFont());
			frame4.setColor(messageButtons[3].getActiveColour());
			frame4.setActiveBackground(messageButtons[3].getActiveBackground());
			frame4.setBackgroundSelected(messageButtons[3].isBackgroundSelected());
			scheduledExecutorService.scheduleWithFixedDelay(subliminalTask4, startDelay, interval, TimeUnit.SECONDS);
		} else {
			prefs.putBoolean("bottomright", false);
		}
		if (messageButtons[4].isActive() == true) {
			frame5.setActive(true);
			prefs.putBoolean("center", true);
			frame5.setFont(messageButtons[4].getFont());
			frame5.setColor(messageButtons[4].getActiveColour());
			frame5.setActiveBackground(messageButtons[4].getActiveBackground());
			frame5.setBackgroundSelected(messageButtons[4].isBackgroundSelected());
			scheduledExecutorService.scheduleWithFixedDelay(subliminalTask5, startDelay, interval, TimeUnit.SECONDS);
		} else {
			prefs.putBoolean("center", false);
		}
	}

	public synchronized void stopMessageActivity() throws InterruptedException {
		scheduledExecutorService.shutdown();
		System.gc();
	}

	public boolean isMessagesOn() {
		return this.messagesOn;
	}

	public void setMessagesOn(boolean messagesOn) {
		this.messagesOn = messagesOn;
	}

	/**
	 * 
	 * @return list of categories
	 */
	public ArrayList<Category> getCategories() {
		return categories;
	}

	/**
	 * 
	 * @return names of all categories
	 */
	public String[] getCategoryNames() {

		int i;
		String[] categoryNames = new String[categories.size()];

		for (i = 0; i < categories.size(); i++) {
			categoryNames[i] = categories.get(i).getCategoryName();
		}
		return categoryNames;
	}

	public String getActiveCategoryName() {
		return categories.get(categoryIndex).getCategoryName();
	}

	/**
	 * 
	 * @param messageIndex
	 * @return A message from the selected category, message index, and tense
	 */
	public Message getMessageFromActiveTenseCategory(int messageIndex) {
		return categories.get(categoryIndex).getMessages().get(messageTense.getTenseVal()).get(messageIndex);
	}

	/**
	 * 
	 * @param catIndex
	 *            category index
	 * @return List of all messages in a selected category
	 */
	public List<Message> getMessagesFromActiveTenseCategory() {
		int i;
		List<Message> messages = new ArrayList<Message>();
		int numMessagesInCategory = categories.get(categoryIndex).getMessages().get(messageTense.getTenseVal()).size();

		for (i = 0; i < numMessagesInCategory; i++) {
			messages.add(categories.get(categoryIndex).getMessages().get(messageTense.getTenseVal()).get(i));
		}
		return messages;
	}

	/**
	 * 
	 * @param catIndex
	 *            category index
	 * @return List of all messages in a selected category
	 */
	public List<Message> getMessagesFromTenseCategory(MessageTense messageTense) {
		int i;
		List<Message> messages = new ArrayList<Message>();
		int numMessagesInCategory = categories.get(categoryIndex).getMessages().get(messageTense.getTenseVal()).size();

		for (i = 0; i < numMessagesInCategory; i++) {
			messages.add(categories.get(categoryIndex).getMessages().get(messageTense.getTenseVal()).get(i));
		}
		return messages;
	}

	public List<Message> getMessagesFromCategory(int categoryIdx, MessageTense tense) {
		return categories.get(categoryIdx).getMessages().get(tense.getTenseVal());
	}

	public Message getMessageFromCategory(int categoryIdx, int msgIdx, MessageTense tense) {
		return categories.get(categoryIdx).getMessages().get(tense.getTenseVal()).get(msgIdx);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		prefs.putInt("speed", speed);
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
		prefs.putInt("interval", interval);
	}

	public int getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(int categoryIndex) {
		this.categoryIndex = categoryIndex;
		prefs.putInt("categoryIndex", categoryIndex);
	}

	public MessageTense getMessageTense() {
		return messageTense;
	}

	public void setMessageTense(MessageTense messageTense) {
		this.messageTense = messageTense;
		prefs.putInt("tense", messageTense == MessageTense.FIRST_PERSON ? 0 : 1);
	}

	public List<Message> getActiveMessages() {
		return activeMessages;
	}

	public void setActiveMessages(List<Message> activeMessages) {
		this.activeMessages = activeMessages;
	}
}
