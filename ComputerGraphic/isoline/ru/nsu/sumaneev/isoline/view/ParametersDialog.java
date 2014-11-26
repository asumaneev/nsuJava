package ru.nsu.sumaneev.isoline.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ru.nsu.sumaneev.isoline.controllers.ChangedParametersReceiver;

public class ParametersDialog extends JDialog {

	private static final int OFFSET_SMALL = 5;
	private static final int OFFSET = 20;
	
	private int k = 0;
	private int m = 0;
	
	private int a = 0;
	private int b = 0;
	
	private int c = 0;
	private int d = 0;
	
	
	private JPanel kmPanel = null;
	private JPanel abPanel = null;
	private JPanel cdPanel = null;
	
	
	private JTextArea kText = null;
	private JTextArea mText = null;
	
	private JTextArea aText = null;
	private JTextArea bText = null;
	
	private JTextArea cText = null;
	private JTextArea dText = null;
	
	
	private JPanel okCancelPanel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	
	
	private ChangedParametersReceiver receiver = null;
	
	public ParametersDialog(int k, int m, int a, int b, int c, int d, ChangedParametersReceiver receiver) {
		this.k = k;
		this.m = m;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		
		this.receiver = receiver;
		
		initUI();
	}
	
	private void initUI() {
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		//	initialize k m
		kmPanel = new JPanel();
		
		JLabel kLabel =  new JLabel("k");
		kText = new JTextArea(String.valueOf(k));
		kText.setPreferredSize(new Dimension(75, 15));
		
		JLabel mLabel =  new JLabel("m");
		mText = new JTextArea(String.valueOf(m));
		mText.setPreferredSize(new Dimension(75, 15));
		
		kmPanel.add(kLabel);
		kmPanel.add(Box.createRigidArea(new Dimension(OFFSET_SMALL, 0)));
		kmPanel.add(kText);
		
		kmPanel.add(mLabel);
		kmPanel.add(Box.createRigidArea(new Dimension(OFFSET_SMALL, 0)));
		kmPanel.add(mText);
	
		kmPanel.setBorder(BorderFactory.createEmptyBorder(OFFSET_SMALL, OFFSET_SMALL, OFFSET_SMALL, OFFSET_SMALL));
		
		add(kmPanel);
		
		
		//	initialize a b
		abPanel = new JPanel();
		
		JLabel aLabel =  new JLabel("a");
		aText = new JTextArea(String.valueOf(a));
		aText.setPreferredSize(new Dimension(75, 15));
		
		JLabel bLabel =  new JLabel("b");
		bText = new JTextArea(String.valueOf(b));
		bText.setPreferredSize(new Dimension(75, 15));
		
		abPanel.add(aLabel);
		abPanel.add(Box.createRigidArea(new Dimension(OFFSET_SMALL, 0)));
		abPanel.add(aText);
		
		abPanel.add(bLabel);
		abPanel.add(Box.createRigidArea(new Dimension(OFFSET_SMALL, 0)));
		abPanel.add(bText);
	
		abPanel.setBorder(BorderFactory.createEmptyBorder(OFFSET_SMALL, OFFSET_SMALL, OFFSET_SMALL, OFFSET_SMALL));
		
		add(abPanel);
		
		//	initialize c d
		cdPanel = new JPanel();
		
		JLabel cLabel =  new JLabel("c");
		cText = new JTextArea(String.valueOf(c));
		cText.setPreferredSize(new Dimension(75, 15));
		
		JLabel dLabel =  new JLabel("d");
		dText = new JTextArea(String.valueOf(d));
		dText.setPreferredSize(new Dimension(75, 15));
		
		cdPanel.add(cLabel);
		cdPanel.add(Box.createRigidArea(new Dimension(OFFSET_SMALL, 0)));
		cdPanel.add(cText);
		
		cdPanel.add(dLabel);
		cdPanel.add(Box.createRigidArea(new Dimension(OFFSET_SMALL, 0)));
		cdPanel.add(dText);
	
		cdPanel.setBorder(BorderFactory.createEmptyBorder(OFFSET_SMALL, OFFSET_SMALL, OFFSET_SMALL, OFFSET_SMALL));
		
		add(cdPanel);
		
		
		
		add(Box.createVerticalGlue());
	
		
		//	initialize ok cancel
		okCancelPanel = new JPanel();
		okCancelPanel.setLayout(new BoxLayout(okCancelPanel, BoxLayout.X_AXIS));
		
		okButton = new JButton("ok");
		cancelButton = new JButton("cancel");
		
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				
				
				String s = kText.getText().trim();
				
				if (0 == s.length()) {
					showInvalidParameter("k");
					return;
				}
				
				int newK = Integer.parseInt(s);
				
				if (newK <= 0) {
					showInvalidParameter("k");
					return;
				}
				else {
					k = newK;
				}
				
				s = mText.getText().trim();
				
				if (0 == s.length()) {
					showInvalidParameter("s");
					return;
				}
				
				int newM = Integer.parseInt(s);
				
				if (newM <= 0) {
					showInvalidParameter("m");
					return;
				}
				else {
					m = newM;
				}
				
				try {
				
				a = Integer.parseInt(aText.getText());
				b = Integer.parseInt(bText.getText());
				c = Integer.parseInt(cText.getText());
				d = Integer.parseInt(dText.getText());
				
				}
				catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(getContentPane(), "invalid number format", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				receiver.sendParameters(k, m, a, b, c, d);
				
				
				finish();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finish();				
			}
		});
		
		
		okCancelPanel.add(okButton);
		okCancelPanel.add(Box.createRigidArea(new Dimension(OFFSET_SMALL, 0)));
		okCancelPanel.add(cancelButton);
		
		
		add(okCancelPanel);
		
		pack();
		setMinimumSize(getSize());
	}
	
	private void finish() {
		this.dispose();
	}
	
	private void showInvalidParameter(String parameter) {
		JOptionPane.showMessageDialog(this, "parameter " + parameter + " is invalid", "ERROR", JOptionPane.ERROR_MESSAGE);
	}
}

