package ru.nsu.sumaneev.controller;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JTextArea;

public interface ListenersGetter {
	
	public ActionListener getScoresListener();
	public ActionListener getScoresRestartListener();
	public ItemListener getComboBoxListener(JComboBox comboBox, JTextArea display);
	public ActionListener getStartListener(JComboBox comboBox);
	public ActionListener getRestartListener();
	public ActionListener getBreakListener();
	public KeyListener getKeyListener();
	
}
