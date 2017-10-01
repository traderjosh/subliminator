package com.psychotechnology.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.psychotechnology.Controller.Controller;
import com.psychotechnology.Controller.Validation;
import com.psychotechnology.GUI.Message.MessagePanel;
import com.psychotechnology.Model.MessageTense;

public class EditMessage extends JDialog {

	private static final long serialVersionUID = 5470112838506529493L;
	private Controller controller;
	private JLabel firstPersonLbl, secondPersonLbl;
	private JTextField firstPersonMsg, secondPersonMsg;
	private JButton submitBtn;

	public EditMessage(Controller controller, MessagePanel messagePanel, int index) {
		this.controller = controller;
		initComponents(index);
		setupUI();

		submitBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String msg1 = firstPersonMsg.getText();
				String msg2 = secondPersonMsg.getText();

				if (Validation.isMoreThanThreeChars(msg1) && Validation.isMoreThanThreeChars(msg2)) {
					
					controller.getMessagesFromCategory(controller.getCategoryIndex(), MessageTense.FIRST_PERSON).get(index).setMessage(msg1);
					controller.getMessagesFromCategory(controller.getCategoryIndex(), MessageTense.SECOND_PERSON).get(index).setMessage(msg2);
					
					messagePanel.getModel().clear();
					messagePanel.setMessageList(controller.getMessagesFromActiveTenseCategory());
					dispose();
				}
			}
		});

		MainFrame.centerFrame(this);
		setSize(400, 225);
		setModal(true);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void initComponents(int index) {
		setTitle("Edit Message");

		firstPersonLbl = new JLabel("1st Person");
		secondPersonLbl = new JLabel("2nd Person");

		firstPersonMsg = new JTextField(30);
		firstPersonMsg.setToolTipText("First Person Message");
		secondPersonMsg = new JTextField(30);
		secondPersonMsg.setToolTipText("Second Person Message");

		firstPersonMsg
				.setText(controller.getMessagesFromCategory(controller.getCategoryIndex(), MessageTense.FIRST_PERSON).get(index).getMessage());
		secondPersonMsg
				.setText(controller.getMessagesFromCategory(controller.getCategoryIndex(), MessageTense.SECOND_PERSON).get(index).getMessage());

		submitBtn = new JButton("Change");
		submitBtn.setToolTipText("Edit this message");
	}

	public void setupUI() {
		
		// Fonts
		firstPersonLbl.setFont(new Font("Courier New", Font.BOLD, 20));
		secondPersonLbl.setFont(new Font("Courier New", Font.BOLD, 20));
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		setLayout(gbl);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.insets = new Insets(10, 0, 10, 0);
		add(firstPersonLbl, gc);

		gc.gridy++;
		gc.insets = new Insets(0, 0, 10, 0);
		add(firstPersonMsg, gc);

		gc.gridy++;
		add(secondPersonLbl, gc);

		gc.gridy++;
		add(secondPersonMsg, gc);

		gc.gridy++;
		add(submitBtn, gc);
	}
}
