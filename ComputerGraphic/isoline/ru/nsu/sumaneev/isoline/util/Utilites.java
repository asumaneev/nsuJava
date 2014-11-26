package ru.nsu.sumaneev.isoline.util;

import java.awt.image.BufferedImage;

public class Utilites {

	public static int fromDomainToCanvas(
										//	x or y
										double domainCoordinate,
										//	u0 or v0
										int canvasStart,
										//	u1 or v1
										int canvasEnd,
										//	a or c
										double domainStart,
										//	b or d
										double domainEnd) {
		
		if ((domainCoordinate < domainStart) || (domainCoordinate > domainEnd)) {
			throw new IllegalArgumentException
			("invalid coordinate: " + domainCoordinate + " .Must be between " + domainStart + " and " + domainEnd);
		}
		
		return (int) Math.floor( (canvasEnd - canvasStart) * (domainCoordinate - domainStart) / (domainEnd - domainStart) + canvasStart + 0.5 );
	} 
	
	public static double fromCanvasToDomain(
											//	u or v
											double canvasCoordinate,
											//	u0 or v0
											int canvasStart,
											//	u1 or v1
											int canvasEnd,
											//	a or c
											double domainStart,
											//	b or d
											double domainEnd)
	{
		
		return (domainEnd - domainStart) * (canvasCoordinate - canvasStart) / (canvasEnd - canvasStart) + domainStart;
	}
	
	public static void drawBresenhamLine(int x1, int y1, int x2, int y2, BufferedImage canvas, int color) {
		
		int dx = x2 - x1;
		int dy = y2 - y1;
	 
		int incx = (dx > 0) ? 1 : (dx < 0) ? -1 : 0;
		int incy = (dy > 0) ? 1 : (dy < 0) ? -1 : 0;

		
		dx = Math.abs(dx);
		dy = Math.abs(dy);
	 
		int pdx = 0;
		int pdy = 0;
		int es = 0;
		int el = 0;
		
		if (dx > dy) {
			
			pdx = incx;
			es = dy;
			el = dx;
		}
		else {
			pdy = incy;
			es = dx;
			el = dy;
		}
	 
		int x = x1;
		int y = y1;
		int err = el/2;
		
		if (x >= canvas.getWidth()) {
			x = canvas.getWidth() - 1;
		}
		
		if (y >= canvas.getHeight()) {
			y = canvas.getHeight() - 1;
		}
		
		canvas.setRGB(x, y, color);
	 
		for (int t = 0; t < el; ++t) {
			
			err -= es;
			
			if (err < 0)
			{
				err += el;
				x += incx;
				y += incy;
			}
			else
			{
				x += pdx;
				y += pdy;
			}
			

			if (x >= canvas.getWidth()) {
				x = canvas.getWidth() - 1;
			}
			
			if (y >= canvas.getHeight()) {
				y = canvas.getHeight() - 1;
			}
			
			canvas.setRGB(x, y, color);
		}
	}
}
