package ru.nsu.sumaneev.pusher.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ru.nsu.sumaneev.controller.ListenersGetter;

@SuppressWarnings("serial")
public class ToolBar extends JMenuBar {

	private ListenersGetter actions = null;
	
	private JMenu game = null;
	private JMenu help = null;
	
	private JMenuItem scores = null;
	private JMenuItem returnToMain = null;
	private JMenuItem clearScores = null;
	private JMenuItem restart = null;
	private JMenuItem exit = null;
	
	private JMenuItem viewHelp = null;
	private JMenuItem about = null;
	
	public ToolBar(ListenersGetter actions){
		
		this.actions = actions;
	}
	
	public void initMainToolBar(){
	
		game = new JMenu("Game");
		
		initScoresItem();
		initClearScores();
		initExit();
		
		
		game.add(scores);
		game.add(clearScores);
		game.add(exit);
		
		initHelp();
		
		add(game);
		add(help);
				
	}
	
	public void initGameToolBar(){
		game = new JMenu("Game");
		
		initScoresItem();
		initRestart();
		initReturnToMainItem();
		initExit();
		
		
		game.add(scores);
		game.add(restart);
		game.add(returnToMain);
		game.add(exit);
		
		initHelp();
		
		add(game);
		add(help);
	}
	
	public void free(){
		game.removeAll();
		help.removeAll();
		
		
		removeAll();
	}
	
	private void initHelp(){
		help = new JMenu("Help");
		
		initViewHelp();
		initAbout();
		
		help.add(viewHelp);
		help.add(about);
	}
	
	private void initScoresItem(){
		
		
		scores = new JMenuItem("Scores");
		scores.setToolTipText("show recordTable");
		scores.addActionListener(actions.getScoresListener());
	}
	
	private void initReturnToMainItem(){
		
		returnToMain = new JMenuItem("Break");
		returnToMain.setToolTipText("return to main screen");
		returnToMain.addActionListener(actions.getBreakListener());
		
	}

	private void initClearScores(){
		clearScores = new JMenuItem("Reset game");
		
		clearScores.setToolTipText("clear all previous records");
		clearScores.addActionListener(actions.getScoresRestartListener());
	}

	private void initRestart(){
		restart = new JMenuItem("Restart");
		restart.setToolTipText("start this level again");
		
		restart.addActionListener(actions.getRestartListener());
	}

	private void initExit(){
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}
			
		});
	}

	private void initViewHelp(){
		viewHelp = new JMenuItem("Help");
		
		viewHelp.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				HelpToolBarDialog helpDialog = new HelpToolBarDialog();
				helpDialog.initHelp();
				helpDialog.setVisible(true);
			}
			
		});
	}

	private void initAbout(){
		about = new JMenuItem("About");
		
		about.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HelpToolBarDialog aboutDialog = new HelpToolBarDialog();
				aboutDialog.initAbout();
				aboutDialog.setVisible(true);
			}
			
			
		});
	}
}

@SuppressWarnings("serial")
class HelpToolBarDialog extends JDialog{

	private JTextArea area = null;
	
	public HelpToolBarDialog(){
		initToolBarDialog();
	}
	
	public void initHelp(){
		area.setText("Aim of the game: the boxes must be placed on target cells.\n\n" + 
			"You are able to move pusher due to arrow-keys.\n"
				);
	}
	
	public void initAbout(){
		area.append("Sokoban by Sumaneev Artem\n");
	}

	private void initToolBarDialog() {
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panel = new JPanel();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        area.setEditable(false);
         
        panel.add(area);
  
        add(panel);
        
        add(Box.createRigidArea(new Dimension(0, 30)));

        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
  
        close.setAlignmentX(0.5f);
        add(close);

        add(Box.createRigidArea(new Dimension(0, 15)));
        
        setModalityType(ModalityType.APPLICATION_MODAL);

        
        
        setTitle("About Notes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 250);
	}	
}