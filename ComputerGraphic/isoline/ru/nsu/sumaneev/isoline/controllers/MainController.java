package ru.nsu.sumaneev.isoline.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import ru.nsu.sumaneev.isoline.functions.Function;
import ru.nsu.sumaneev.isoline.functions.LegendFunction;
import ru.nsu.sumaneev.isoline.functions.TestFunction;
import ru.nsu.sumaneev.isoline.painting.Canvas;
import ru.nsu.sumaneev.isoline.painting.ColorController;
import ru.nsu.sumaneev.isoline.painting.ColorController.ColorType;
import ru.nsu.sumaneev.isoline.painting.DrawPanelRepainter;
import ru.nsu.sumaneev.isoline.painting.FunctionPainter;
import ru.nsu.sumaneev.isoline.view.DrawPanel;
import ru.nsu.sumaneev.isoline.view.MainWindow;
import ru.nsu.sumaneev.isoline.view.ParametersDialog;
import ru.nsu.sumaneev.isoline.util.Utilites;



public class MainController {

	private MainWindow mainWindow = null;
	private DrawPanel plotPanel = null;
	private DrawPanel legendPanel = null;
	
	private Function plot = null;
	private Function legend = null;
	
	
	private ColorController plotColors = null;
	private ColorController legendColors = null;
	
	
	private Canvas plotCanvas = null;
	private Canvas legendCanvas = null;
	
	
	private FunctionPainter plotPainter = null;
	private FunctionPainter legendPainter = null;
	
	//	listeners
	private PlotRepainter plotRepainter = null;
	private LegendRepainter legendRepainter = null;
	private IsolineListener isolineListener = null;
	private NetListener netListner = null;
	private PaintingTypeListener paintingTypeListener = null;
	private ParameterButtomListener parametersButtomListener = null;
	private ActionListener resetIsolinesButtomListener = null;
	
	private MouseMoveAndClickListener plotMouseListener = null;
	
	
	private int initialA = 0;
	private int initialB = 0;
	private int initialC = 0;
	private int initialD = 0;
	private int initialK = 0;
	private int initialM = 0;
	private int[] initialColors = null;
	private int initialIsolineColor = 0;
	private int initialNetColor = 0;
	
	public MainController(String file) {
	
		
		//try (BufferedReader in = new BufferedReader(new FileReader(file))) {
		try {
		
			readFromStream(file);
		
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		//	initialize function
		plot = new TestFunction(initialA, initialB, initialC, initialD, initialK, initialM);
		
		legend = new LegendFunction(initialA, initialB, initialC, initialD, initialK, initialM);
		
		//	initialize colors' controller
		//int[] col = {0x190707, 0x610B0B, 0xDF0101, 0xFA5858, 0xF6CECE, 0xFBEFEF};
		
		
		plotColors = new ColorController(plot, initialColors, initialIsolineColor, initialNetColor);
		legendColors = new ColorController(legend, initialColors, initialIsolineColor, initialNetColor);
	
		
		//	initialize GUI

		plotPanel = new DrawPanel(400, 400);
		legendPanel = new DrawPanel(200, 400);
		legendPanel.setMaximumSize(legendPanel.getSize());
		legendPanel.setMinimumSize(legendPanel.getSize());
		
		mainWindow = new MainWindow(plotPanel, legendPanel, legendColors.getValues());
		
		
		
		//	initialize listeners
		plotRepainter = new PlotRepainter();
		legendRepainter = new LegendRepainter();
		isolineListener = new IsolineListener();
		netListner = new NetListener();
		paintingTypeListener = new PaintingTypeListener();
		parametersButtomListener = new ParameterButtomListener();
		resetIsolinesButtomListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				plotMouseListener.clearAdditionalIsolines();
				
				repaintPlot();								
			}
		};
		
		legendPanel.setDrawPanelRepainter(legendRepainter);
		
		
		
		mainWindow.setIsolineListener(isolineListener);
		mainWindow.setNetListener(netListner);
		mainWindow.setPaintingTypeListener(paintingTypeListener);
		mainWindow.setChangeParametersListener(parametersButtomListener);
		mainWindow.setResetCustomIsolinesListener(resetIsolinesButtomListener);
	
		
		plotCanvas = new Canvas(plotPanel.getWidth(), plotPanel.getHeight());
		legendCanvas = new Canvas(legendPanel.getWidth(), legendPanel.getHeight());
		
		plotPainter = new FunctionPainter(plot, plotCanvas, plotColors, plotPanel);
		legendPainter = new FunctionPainter(legend, legendCanvas, legendColors, legendPanel);
		
		
		plotMouseListener = new MouseMoveAndClickListener(plot, plotPanel);
		
		plotPanel.setDrawPanelRepainter(plotRepainter);
		plotPanel.setMouseMoveListener(plotMouseListener);
		plotPanel.setMouseClickListener(plotMouseListener);
	}
	
	public void start() {
		SwingUtilities.invokeLater(new Runnable() {

            public void run() {
            	mainWindow.setVisible(true);
            }
        });
	}
	
	private void readFromStream(String file) throws IOException {
		
		Properties properties = new Properties();
		
		properties.load(new FileInputStream(file));
		
		initialK = Integer.valueOf(properties.getProperty("k"));
		initialM = Integer.valueOf(properties.getProperty("m"));
		
		initialA = Integer.valueOf(properties.getProperty("a"));
		initialB = Integer.valueOf(properties.getProperty("b"));
		initialC = Integer.valueOf(properties.getProperty("c"));
		initialD = Integer.valueOf(properties.getProperty("d"));
		
		int n = Integer.valueOf(properties.getProperty("n"));
		
		initialColors = new int[n + 1];
		
		for (int i = 0; i <= n; ++i) {
			initialColors[i] = Integer.valueOf(properties.getProperty("c" + String.valueOf(i)), 16);
		}
		
		initialIsolineColor = Integer.valueOf(properties.getProperty("isoline"), 16);
		initialNetColor = Integer.valueOf(properties.getProperty("net"), 16);

	}

	private void repaintPlot() {
		
		
		plotPanel.paint(plotRepainter.getImage());
		legendPanel.paint(legendRepainter.getImage());
	}
	
	private class IsolineListener implements ActionListener {

		private boolean state = false;
		
		@Override
		public void actionPerformed(ActionEvent e) {
		
			state = ( (JCheckBox) e.getSource() ).isSelected();
			
			repaintPlot();
		}
		
		public boolean isSelected() {
			return state;
		}
		
	}
	
	private class NetListener implements ActionListener {

		private boolean state = false;
		
		@Override
		public void actionPerformed(ActionEvent e) {
		
			state = ( (JCheckBox) e.getSource() ).isSelected();
			
			repaintPlot();
		}
		
		public boolean isSelected() {
			return state;
		}
		
	}
	
	private class PaintingTypeListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			
			//int index = ((JComboBox<String>) e.getSource()).getSelectedIndex();
			ColorType type = (ColorType) ((JComboBox<String>) e.getSource()).getSelectedItem();
			
			plotColors.setColorType(type);
			legendColors.setColorType(type);
			repaintPlot();
		}
	}
	
	private class ParameterButtomListener implements ActionListener, ChangedParametersReceiver {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			ParametersDialog dialog = new ParametersDialog(
					plot.getK(), 
					plot.getM(),
					plot.getA(),
					plot.getB(),
					plot.getC(),
					plot.getD(),
					this);
			
			dialog.setVisible(true);
		}

		@Override
		public void sendParameters(int k, int m, int a, int b, int c, int d) {
			
			plot.setK(k);
			plot.setM(m);
			plot.setA(a);
			plot.setB(b);
			plot.setC(c);
			plot.setD(d);
			
			plot.refresh();
			plotColors.refresh();
			
			legend.setK(k);
			legend.setM(m);
			legend.setA(a);
			legend.setB(b);
			legend.setC(c);
			legend.setD(d);
			
			legend.refresh();
			legend.refresh();
			
			repaintPlot();
		}
		
	}
	
	private class PlotRepainter implements DrawPanelRepainter {
		
		@Override
		public BufferedImage getImage() {
			
			plotPainter.drawFunction();
			
			if (isolineListener.isSelected()) {
				
				plotPainter.drawIsolines();
			}
			
			List<Double> additionalIsolines = plotMouseListener.getAdditionalIsolines();
			
			plotPainter.refresh();
			
			for (Double isolineValue: additionalIsolines) {
				plotPainter.drawIsoline(isolineValue);
			}
			
			if (netListner.isSelected()) {
				
				plotPainter.drawNet();	
			}
			
			
			
			return plotCanvas.getBufferedImage();
		}
	}
	
	private class LegendRepainter implements DrawPanelRepainter {
		
		@Override
		public BufferedImage getImage() {
			
			legendPainter.drawFunction();
				
			legendPainter.drawIsolines();	
			
			
			return legendCanvas.getBufferedImage();
		}
	}
	
	private class MouseMoveAndClickListener implements MouseMotionListener, MouseListener {

		private Function functuion = null;
		private DrawPanel panel = null;
		private List<Double> additionalIsolines = null;
		
		
		public MouseMoveAndClickListener(Function function, DrawPanel panel) {
			this.functuion = function;
			this.panel = panel;
			
			additionalIsolines = new LinkedList<Double>();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
			int canvasX = e.getX();
			int canvasY = e.getY();
			
			double x = Utilites.fromCanvasToDomain(canvasX, 0, panel.getWidth(), functuion.getA(), functuion.getB());
			double y = Utilites.fromCanvasToDomain(canvasY, 0, panel.getHeight(), functuion.getC(), functuion.getD());
			double value = functuion.getValue(x, y);
			
			mainWindow.setPlotStatus( 
					(double) Math.round(x * 100) / 100, 
					(double) Math.round(y * 100) / 100, 
					(double) Math.round(value * 100) / 100);
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			int canvasX = e.getX();
			int canvasY = e.getY();
			
			double x = Utilites.fromCanvasToDomain(canvasX, 0, panel.getWidth(), functuion.getA(), functuion.getB());
			double y = Utilites.fromCanvasToDomain(canvasY, 0, panel.getHeight(), functuion.getC(), functuion.getD());
			double value = functuion.getValue(x, y);
			
			additionalIsolines.add(value);
			
			repaintPlot();
		}
		
		public void clearAdditionalIsolines() {
			additionalIsolines.clear();
		}
		
		public List<Double> getAdditionalIsolines() {
			return additionalIsolines;
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {

			mouseClicked(e);			
		}
		
	}
	
	
	public static void main(String[] args) {
		
		String file = "isoline/input.txt";
		
		MainController c = new MainController(file);
		c.start();
	}
	
}
