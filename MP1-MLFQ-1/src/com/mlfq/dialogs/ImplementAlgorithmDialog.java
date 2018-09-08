package com.mlfq.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mlfq.panels.AdditionalInformationPanel;
import com.mlfq.panels.ProcessControlBlockPanel;
import com.mlfq.utilities.MLFQHandler;

import net.miginfocom.swing.MigLayout;

public class ImplementAlgorithmDialog extends JDialog implements ActionListener, ItemListener
{
	private static final long serialVersionUID = 1L;
	
	private JButton addQueue, deleteQueue, submitButton, cancelButton;
	private JLabel priorityLabel, queuesLabel, queueNumberLabel, quantumTimeLabel;
	private JTextField quantumTimeField;
	private JPanel panel, queuesPanel, buttonsPanel, addedPanel;
	private JComboBox<String> priorityPolicy, algorithms;
	
	private ArrayList<JComboBox<String>> selectedAlgo;
	private ArrayList<JTextField> quantumTime;
	
	private String[] algorithmsArray = {"First Come First Serve", "Shortest Job First", "Shortest Remaining Time First", "Preemptive Priority Scheduling", "Non-preemptive Priority Scheduling", "Round Robin"};
	private String[] policyArray = {"Higher before lower", "Fixed time slots"};	
	public ImplementAlgorithmDialog()
	{
		setLayout(new MigLayout("fillx, insets 20"));
		addComponents();
		addListeners();
		setSize(new Dimension(400, 340));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addComponents()
	{
		selectedAlgo = new ArrayList<JComboBox<String>>();
		quantumTime = new ArrayList<JTextField>();
		
		priorityLabel = new JLabel("Choose priority policy:", JLabel.CENTER);
		priorityPolicy = new JComboBox<String>(policyArray);
		
		queuesLabel = new JLabel("Choose classical algorithm for each queue:", JLabel.CENTER);
		
		queuesPanel = new JPanel(new MigLayout("fillx", "[grow, 30%][grow, 70%]"));
		buttonsPanel = new JPanel(new MigLayout("fillx", "[grow, 50%][grow, 50%]"));
		addQueue = new JButton("Add Queue");
		deleteQueue = new JButton("Delete Queue");
		deleteQueue.setEnabled(false);
		buttonsPanel.add(addQueue, "align right");
		buttonsPanel.add(deleteQueue, "align left");
		queuesPanel.add(buttonsPanel, "spanx 2, grow, align center, wrap");
		
		submitButton = new JButton("Submit");
		submitButton.setEnabled(false);
		cancelButton = new JButton("Cancel");
		
		panel = new JPanel(new MigLayout("fillx", "[grow, 50%][grow, 50%]"));
		panel.add(priorityLabel, "spanx 2, grow, align center, wrap");
		panel.add(priorityPolicy, "spanx 2, align center, wrap");
		panel.add(queuesLabel, "grow, spanx 2, align center, wrap");
		panel.add(queuesPanel, "grow, spanx 2, align center, wrap");
		panel.add(submitButton, "align right");
		panel.add(cancelButton, "align left");
		
		add(panel, "grow, align center");
	}
	
	private void addListeners()
	{
		addQueue.addActionListener(this);
		deleteQueue.addActionListener(this);
		submitButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	private void addToList()
	{
		queueNumberLabel = new JLabel("Q" + (selectedAlgo.size() + 1), JLabel.CENTER);
		algorithms = new JComboBox<String>(algorithmsArray);
		algorithms.addItemListener(this);
		
		quantumTimeLabel = new JLabel("Quantum Time", JLabel.CENTER);
		quantumTimeLabel.setEnabled(false);
		quantumTimeField = new JTextField();
		quantumTimeField.setText("0");
		quantumTimeField.setEnabled(false);
		
		addedPanel = new JPanel(new MigLayout("fillx", "[grow, 20%][grow, 30%][grow, 20%][grow, 30%]"));
		addedPanel.add(queueNumberLabel, "grow, align center");
		addedPanel.add(algorithms, "grow, align center");
		addedPanel.add(quantumTimeLabel, "grow, align center");
		addedPanel.add(quantumTimeField, "grow, align center");
		
		queuesPanel.add(addedPanel, "spanx 2, grow, align center, wrap");
		
		selectedAlgo.add(algorithms);
		quantumTime.add(quantumTimeField);
	}
	
	private void removeFromList()
	{
		queuesPanel.removeAll();
		queuesPanel.repaint();
		queuesPanel.revalidate();
		selectedAlgo.remove(selectedAlgo.size() - 1);
		quantumTime.remove(quantumTime.size() - 1);
		
		buttonsPanel.add(addQueue, "align right");
		buttonsPanel.add(deleteQueue, "align left");
		queuesPanel.add(buttonsPanel, "spanx 2, grow, align center, wrap");
		
		for(int i = 0; i < selectedAlgo.size(); i++) {
			queueNumberLabel = new JLabel("Q" + (i + 1), JLabel.CENTER);
			algorithms = new JComboBox<String>(algorithmsArray);
			algorithms.addItemListener(this);
			quantumTimeLabel = new JLabel("Quantum Time", JLabel.CENTER);
			quantumTimeField = new JTextField();
			
			addedPanel = new JPanel(new MigLayout("fillx", "[grow, 20%][grow, 30%][grow, 20%][grow, 30%]"));
			addedPanel.add(queueNumberLabel, "grow, align center");
			addedPanel.add(algorithms, "grow, align center");
			addedPanel.add(quantumTimeLabel, "grow, align center");
			addedPanel.add(quantumTimeField, "grow, align center");
			
			queuesPanel.add(addedPanel, "spanx 2, grow, align center, wrap");
			
			algorithms.setSelectedIndex(selectedAlgo.get(i).getSelectedIndex());
			quantumTimeField.setText("" + quantumTime.get(i).getText());
			
			if (algorithms.getSelectedIndex() == 5) {
				quantumTimeLabel.setEnabled(true);
				quantumTimeField.setEnabled(true);
			} else {
				quantumTimeLabel.setEnabled(false);
				quantumTimeField.setEnabled(false);
			}
		}
		
		queuesPanel.repaint();
		queuesPanel.revalidate();
	}
	
	private void checkConstraints()
	{
		if (selectedAlgo.size() <= 0 && quantumTime.size() <= 0) {
			deleteQueue.setEnabled(false);
			submitButton.setEnabled(false);
		} else if (selectedAlgo.size() >= 3 && quantumTime.size() >= 3) {
			addQueue.setEnabled(false);
			submitButton.setEnabled(true);
		} else {
			addQueue.setEnabled(true);
			deleteQueue.setEnabled(true);
			submitButton.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == addQueue) {
			addToList();
			repaint();
			revalidate();
		} else if (event.getSource() == deleteQueue) {
			removeFromList();
			repaint();
			revalidate();
		} else if (event.getSource() == submitButton) {
			String[] algorithms = new String[selectedAlgo.size()];
			for(int i = 0; i < selectedAlgo.size(); i++) {
				algorithms[i] = selectedAlgo.get(i).getSelectedItem().toString();
			}
			AdditionalInformationPanel.setDisplayAlgoAndPolicy(algorithms, priorityPolicy.getSelectedItem().toString());
			new MLFQHandler(ProcessControlBlockPanel.getTableModel(), selectedAlgo, quantumTime);
			dispose();
		} else if (event.getSource() == cancelButton) {
			dispose();
		}
		
		checkConstraints();
	}

	@Override
	public void itemStateChanged(ItemEvent event)
	{
		if (algorithms.getSelectedIndex() == 5) {
			quantumTimeLabel.setEnabled(true);
			quantumTimeField.setEnabled(true);
		} else {
			quantumTimeLabel.setEnabled(false);
			quantumTimeField.setEnabled(false);
		}
	}
}