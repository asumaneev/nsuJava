package ru.nsu.sumaneev.isoline.painting;

import ru.nsu.sumaneev.isoline.functions.Function;
import ru.nsu.sumaneev.isoline.painting.ColorController.ColorType;
import ru.nsu.sumaneev.isoline.view.DrawPanel;
import ru.nsu.sumaneev.isoline.util.Utilites;

public class FunctionPainter {

	private Function function = null;
	private Canvas canvas = null;
	private ColorController colors = null;
	
	private DrawPanel panel = null;
	
	
	private int k = 0;
	private int m = 0;
	private double stepX = 0;
	private double stepY = 0;
	
	
	public FunctionPainter(Function function, Canvas canvas, ColorController colors, DrawPanel panel) {
		this.function = function;
		this.canvas = canvas;
		this.colors = colors;
		this.panel = panel;
		
		
	}

	
	public void setFunction(Function function) {
		this.function = function;
	}
	
	
	public void drawFunction() {

		if ((canvas.getWidth() != panel.getWidth()) || (canvas.getHeight() != panel.getHeight())) {
			canvas.resize(panel.getWidth(), panel.getHeight());			
		}

		
		
		if (ColorType.INTERPOLATION_AND_DITHER == colors.getType()) {
			
			canvas.clearCanvas();
			
			//	initial canvas is black
			
			for (int y = 0; y < canvas.getHeight(); ++y) {	
				for (int x = 0; x < canvas.getWidth(); ++x) {
					
					//TODO: how to?
					
					double domainX = Utilites.fromCanvasToDomain(x, 0, canvas.getWidth(), function.getA(), function.getB());
					double domainY = Utilites.fromCanvasToDomain(y, 0, canvas.getHeight(), function.getC(), function.getD());
					double value = function.getValue(domainX, domainY);

					//	interpolated color + error color
					int oldPixel = colors.appendColor(colors.getInterpolatedColor(value), canvas.getColor(x, y));

					//	closest color in colors
					int newPixel = colors.findClosestColor(oldPixel);
					
					canvas.setPixel(x, y, newPixel);
					
					
					int errorColor = colors.getColorDifference(oldPixel, newPixel);
					
					//	set error
					
					if (x + 1 < canvas.getWidth()) {
						
						canvas.setPixel(x + 1, y, colors.appendColor(canvas.getColor(x + 1, y), errorColor, 7.0 / 16));
						
						if (y + 1 < canvas.getHeight()) {
							
							canvas.setPixel(x + 1, y + 1, colors.appendColor(canvas.getColor(x + 1, y + 1), errorColor, 1.0 / 16));
						}
					}
					
					if (y + 1 < canvas.getHeight()) {

						canvas.setPixel(x, y + 1, colors.appendColor(canvas.getColor(x, y + 1), errorColor, 5.0 / 16));
						
						if (x - 1 > 0) {
						
							canvas.setPixel(x - 1, y + 1, colors.appendColor(canvas.getColor(x - 1, y + 1), errorColor, 3.0 / 16));
						}
					}
					
					
					
					
				}
			}
			
			
		}
		else {
		
			for (int y = 0; y < canvas.getHeight(); ++y) {
				
				for (int x = 0; x < canvas.getWidth(); ++x) {
					
					canvas.setPixel(x, y, colors.getColor(
							Utilites.fromCanvasToDomain(x, 0, canvas.getWidth(), function.getA(), function.getB()), 
							Utilites.fromCanvasToDomain(y, 0, canvas.getHeight(), function.getC(), function.getD())));
				}
				
			}
		
		}
	}
	
	public void refresh() {
		k = function.getK();
		m = function.getM();
		
		stepX = ( ((double) canvas.getWidth()) / k );
		stepY = ( ((double) canvas.getHeight()) / m );
	}
	
	public void drawIsolines() {
		
		refresh();
		
		//	for each crucial value
		double[] values = colors.getValues();
		for (double islolineValue : values) {
			drawIsoline(islolineValue);			
		}
		
	}
	
	public void drawIsoline(double isolineValue) {
		
		for (int j = 0; j < m; ++j) {
			for (int i = 0; i < k; ++i) {
				
				double[] f = {
						
						//	left bottom 
						function.getValue(
								Utilites.fromCanvasToDomain(i * stepX, 0, canvas.getWidth(), function.getA(), function.getB()),
								Utilites.fromCanvasToDomain(j * stepY, 0, canvas.getHeight(), function.getC(), function.getD()) ),
						
						//	right bottom
						function.getValue(
								Utilites.fromCanvasToDomain((i + 1) * stepX, 0, canvas.getWidth(), function.getA(), function.getB()),
								Utilites.fromCanvasToDomain(j * stepY, 0, canvas.getHeight(), function.getC(), function.getD()) ),
						
						//	right top	
						function.getValue(
								Utilites.fromCanvasToDomain((i + 1) * stepX, 0, canvas.getWidth(), function.getA(), function.getB()),
								Utilites.fromCanvasToDomain((j + 1) * stepY, 0, canvas.getHeight(), function.getC(), function.getD()) ),
								
						//	left top
						function.getValue(
								Utilites.fromCanvasToDomain(i * stepX, 0, canvas.getWidth(), function.getA(), function.getB()),
								Utilites.fromCanvasToDomain((j + 1) * stepY, 0, canvas.getHeight(), function.getC(), function.getD()) )
						
				};
				
				double fCenter = 0;
				
				int binaryIndex = 0;
				
				for (int q = 0; q < 4; ++q) {
					
					if (f[q] >= isolineValue) {
						binaryIndex = binaryIndex |  (1 << q);
					}
					
					fCenter += f[q];
				}
				
				fCenter /= 4;
				
				//	no contour
				if ((0 == binaryIndex) || (15 == binaryIndex)) {
					continue;
				}
				//	single segment
				else if ((1 == binaryIndex) || (14 == binaryIndex)) {
					//	from (i, j + 1/2) to (i + 1/2, j)
					//	between 3 and 0, between 1 and 0
					
					double deltaY = (Math.abs(f[0] - isolineValue) / Math.abs(f[3] - f[0]));
					double deltaX = (Math.abs(f[0] - isolineValue) / Math.abs(f[1] - f[0]));
					
					Utilites.drawBresenhamLine(
							(int) Math.round(i * stepX), 
							(int) Math.round( (j + deltaY) * stepY ), 
							
							(int) Math.round( (i + deltaX) * stepX ), 
							(int) Math.round(j * stepY), 
							
							canvas.getBufferedImage(), 
							colors.getIsolineColor());
				}
				else if ((2 == binaryIndex) || (13 == binaryIndex)) {
					//	from (i + 1, j + 1/2) to (i + 1/2, j)
					//	between 2 and 1, between 1 and 0
					
					double deltaY = (Math.abs(f[1] - isolineValue) / Math.abs(f[2] - f[1])); 
					double deltaX = (Math.abs(f[0] - isolineValue) / Math.abs(f[1] - f[0]));
					
					Utilites.drawBresenhamLine(
							(int) Math.round( (i + 1) * stepX ), 
							(int) Math.round( (j + deltaY) * stepY ), 
							
							(int) Math.round( (i + deltaX) * stepX ), 
							(int) Math.round( j * stepY ), 
							
							canvas.getBufferedImage(), 
							colors.getIsolineColor());
				}
				else if ((4 == binaryIndex) || (11 == binaryIndex)) {
					//	from (i + 1/2, j + 1) to (i + 1, j + 1/2)
					//	between 3 and 2, between 2 and 1
					
					double deltaY = (Math.abs(f[1] - isolineValue) / Math.abs(f[2] - f[1]));
					double deltaX = (Math.abs(f[3] - isolineValue) / Math.abs(f[3] - f[2]));
					
					Utilites.drawBresenhamLine(
							(int) Math.round( (i + deltaX) * stepX ), 
							(int) Math.round( (j + 1) * stepY ), 
							
							(int) Math.round( (i + 1) * stepX ), 
							(int) Math.round( (j + deltaY) * stepY ), 
							
							canvas.getBufferedImage(), 
							colors.getIsolineColor());
				}
				else if ((8 == binaryIndex) || (7 == binaryIndex)) {
					//	from (i + 1/2, j + 1) to (i, j + 1/2)
					//	between 3 and 2, between 3 and 0
					
					double deltaY = (Math.abs(f[0] - isolineValue) / Math.abs(f[3] - f[0]));
					double deltaX = (Math.abs(f[3] - isolineValue) / Math.abs(f[3] - f[2]));
					
					
					Utilites.drawBresenhamLine(
							(int) Math.round( (i + deltaX) * stepX ), 
							(int) Math.round( (j + 1) * stepY ), 
							
							(int) Math.round( i * stepX ), 
							(int) Math.round( (j + deltaY) * stepY ), 
							
							canvas.getBufferedImage(), 
							colors.getIsolineColor());
				}
				
				
				else if ((3 == binaryIndex) || (12 == binaryIndex)) {
					//	from (i, j + 1/2) to (i + 1, j + 1/2)
					//	between 3 and 0, between 2 and 1
					
					
					double delta1 = (Math.abs(f[0] - isolineValue) / Math.abs(f[3] - f[0]));
					double delta2 = (Math.abs(f[1] - isolineValue) / Math.abs(f[2] - f[1]));
					
					Utilites.drawBresenhamLine(
							(int) Math.round( i * stepX ), 
							(int) Math.round( (j + delta1) * stepY ), 
							
							(int) Math.round( (i + 1) * stepX ), 
							(int) Math.round( (j + delta2) * stepY ), 
							
							canvas.getBufferedImage(), 
							colors.getIsolineColor());
				}
				else if ((6 == binaryIndex) || (9 == binaryIndex)) {
					//	from (i + 1/2, j + 1) to (i + 1/2, j)
					//	between 3 and 2, between 1 and 0
					
					double delta1 = (Math.abs(f[3] - isolineValue) / Math.abs(f[3] - f[2]));
					double delta2 = (Math.abs(f[0] - isolineValue) / Math.abs(f[1] - f[0]));
					
					Utilites.drawBresenhamLine(
							(int) Math.round( (i + delta1) * stepX ), 
							(int) Math.round( (j + 1) * stepY ), 
							
							(int) Math.round( (i + delta2) * stepX ), 
							(int) Math.round( j * stepY ), 
							
							canvas.getBufferedImage(), 
							colors.getIsolineColor());
				}
				//	two-segment saddle
				else if (10 == binaryIndex) {
					
					if (fCenter >= isolineValue) {
						//	4 and 1 case
						//	from (i + 1/2, j + 1) to (i + 1, j + 1/2)
						//	from (i, j + 1/2) to (i + 1/2, j)
						
						
						double deltaY = (Math.abs(f[1] - isolineValue) / Math.abs(f[2] - f[1]));
						double deltaX = (Math.abs(f[3] - isolineValue) / Math.abs(f[3] - f[2]));
						
						Utilites.drawBresenhamLine(
								(int) Math.round( (i + deltaX) * stepX ), 
								(int) Math.round( (j + 1) * stepY ), 
								
								(int) Math.round( (i + 1) * stepX ), 
								(int) Math.round( (j + deltaY) * stepY ), 
								
								canvas.getBufferedImage(), 
								colors.getIsolineColor());
						
						deltaY = (Math.abs(f[0] - isolineValue) / Math.abs(f[3] - f[0]));
						deltaX = (Math.abs(f[0] - isolineValue) / Math.abs(f[1] - f[0]));
						
						Utilites.drawBresenhamLine(
								(int) Math.round(i * stepX), 
								(int) Math.round( (j + deltaY) * stepY ), 
								
								(int) Math.round( (i + deltaX) * stepX ), 
								(int) Math.round(j * stepY), 
								
								canvas.getBufferedImage(), 
								colors.getIsolineColor());
						
					}
					else {
						//	8 and 2 case
						//	from (i + 1/2, j + 1) to (i, j + 1/2)
						//	from (i + 1, j + 1/2) to (i + 1/2, j)
						
						double deltaY = (Math.abs(f[0] - isolineValue) / Math.abs(f[3] - f[0]));
						double deltaX = (Math.abs(f[3] - isolineValue) / Math.abs(f[3] - f[2]));
						
						
						Utilites.drawBresenhamLine(
								(int) Math.round( (i + deltaX) * stepX ), 
								(int) Math.round( (j + 1) * stepY ), 
								
								(int) Math.round( i * stepX ), 
								(int) Math.round( (j + deltaY) * stepY ), 
								
								canvas.getBufferedImage(), 
								colors.getIsolineColor());
						
						deltaY = (Math.abs(f[1] - isolineValue) / Math.abs(f[2] - f[1])); 
						deltaX = (Math.abs(f[0] - isolineValue) / Math.abs(f[1] - f[0]));
						
						Utilites.drawBresenhamLine(
								(int) Math.round( (i + 1) * stepX ), 
								(int) Math.round( (j + deltaY) * stepY ), 
								
								(int) Math.round( (i + deltaX) * stepX ), 
								(int) Math.round( j * stepY ), 
								
								canvas.getBufferedImage(), 
								colors.getIsolineColor());
					}
				}
				else if (5 == binaryIndex) {
					
					if (fCenter >= isolineValue) {
						//	8 and 2 case
						//	from (i + 1/2, j + 1) to (i, j + 1/2)
						//	from (i + 1, j + 1/2) to (i + 1/2, j)
						
						double deltaY = (Math.abs(f[0] - isolineValue) / Math.abs(f[3] - f[1]));
						double deltaX = (Math.abs(f[3] - isolineValue) / Math.abs(f[3] - f[2]));
						
						Utilites.drawBresenhamLine(
								(int) Math.round( (i + deltaX) * stepX ), 
								(int) Math.round( (j + 1) * stepY ), 
								
								(int) Math.round( i * stepX ), 
								(int) Math.round( (j + deltaY) * stepY ), 
								
								canvas.getBufferedImage(), 
								colors.getIsolineColor());
						
						deltaY = (Math.abs(f[1] - isolineValue) / Math.abs(f[2] - f[1])); 
						deltaX = (Math.abs(f[0] - isolineValue) / Math.abs(f[1] - f[0]));
						
						Utilites.drawBresenhamLine(
								(int) Math.round( (i + 1) * stepX ), 
								(int) Math.round( (j + deltaY) * stepY ), 
								
								(int) Math.round( (i + deltaX) * stepX ), 
								(int) Math.round( j * stepY ), 
								
								canvas.getBufferedImage(), 
								colors.getIsolineColor());
						
						
					}
					else {
						
						//	4 and 1 case
						//	from (i + 1/2, j + 1) to (i + 1, j + 1/2)
						//	from (i, j + 1/2) to (i + 1/2, j)
						
						double deltaY = (Math.abs(f[1] - isolineValue) / Math.abs(f[2] - f[1]));
						double deltaX = (Math.abs(f[3] - isolineValue) / Math.abs(f[3] - f[2]));
						
						Utilites.drawBresenhamLine(
								(int) Math.round( (i + deltaX) * stepX ), 
								(int) Math.round( (j + 1) * stepY ), 
								
								(int) Math.round( (i + 1) * stepX ), 
								(int) Math.round( (j + deltaY) * stepY ), 
								
								canvas.getBufferedImage(), 
								colors.getIsolineColor());
						
						deltaY = (Math.abs(f[0] - isolineValue) / Math.abs(f[3] - f[0]));
						deltaX = (Math.abs(f[0] - isolineValue) / Math.abs(f[1] - f[0]));
						
						Utilites.drawBresenhamLine(
								(int) Math.round(i * stepX), 
								(int) Math.round( (j + deltaY) * stepY ), 
								
								(int) Math.round( (i + deltaX) * stepX ), 
								(int) Math.round(j * stepY), 
								
								canvas.getBufferedImage(), 
								colors.getIsolineColor());
					}
					
				}
			}
		}
		
	}
	
	public void drawNet() {
		
		refresh();
		
		
		for (int j = 0; j < function.getM(); ++j) {
			
			Utilites.drawBresenhamLine(0, (int) Math.round( j * stepY ), canvas.getWidth() - 1, (int) Math.round( j * stepY ), canvas.getBufferedImage(), colors.getNetColor());
			
		}
		
		for (int i = 0; i < function.getK(); ++i) {
			
			Utilites.drawBresenhamLine((int) Math.round( i * stepX ), 0, (int) Math.round( i * stepX ), canvas.getHeight() - 1, canvas.getBufferedImage(), colors.getNetColor());
			
		}
		
	}
	
	private void drawInterpolated() {
		
		for (int y = 0; y < canvas.getHeight(); ++y) {
			
			for (int x = 0; x < canvas.getWidth(); ++x) {
				
				canvas.setPixel(x, y, colors.getInterpolatedColor(
						function.getValue(
						Utilites.fromCanvasToDomain(x, 0, canvas.getWidth(), function.getA(), function.getB()), 
						Utilites.fromCanvasToDomain(y, 0, canvas.getHeight(), function.getC(), function.getD()))
					)
				);
			}
			
		}
	}
	
}
