package ru.nsu.sumaneev.isoline.painting;

import ru.nsu.sumaneev.isoline.functions.Function;

public class ColorController {

	private Function function = null;
	
	//	number of crucial values
	private int n = 0;
	
	//	crucial values: 0 .. n - 1
	private double values[] = null;
	
	//	colors: 0 .. n
	private int colors[] = null;
	
	private int isolineColor = 0;
	
	private int netColor = 0;
	
	
	private ColorType colorType = ColorType.SIMPLE;
	
	
	public ColorController(Function function, int[] colors, int isolineColor, int netColor) {
		
		this.function = function;
		this.n = colors.length - 1;
		
		this.colors = colors;
		this.isolineColor = isolineColor;
		this.netColor = netColor;
		
		this.values = new double[n];
		setValues();
	}
	
	public void setColorType(ColorType colorType) {
		this.colorType = colorType;
	}
	
	public void setFunction(Function function) {
		this.function = function;
	
		setValues();
	}
	
	public void setColors(int[] colors) {
		this.colors = colors;
		
		n = colors.length - 1;
		
		setValues();
	}
	
	public void refresh() {
		setValues();
	}
	
	public void setIsolineColor(int isolineColor) {
		this.isolineColor = isolineColor;
	}
	
	public void setNetColor(int netColor) {
		this.netColor = netColor;
	}
	
	
	public int getIsolineColor() {
		return isolineColor;
	}
	
	public int getNetColor() {
		return netColor;
	}
	
	
	public double[] getValues() {
		return values;
	}
	
	public ColorType getType() {
		return colorType;
	}
	
	/**
	 * 
	 * @param x coordinate from domain
	 * @param y coordinate from domain
	 */
	public int getColor(double x, double y) {
		
		double value = function.getValue(x, y);
		
		switch (colorType) {
		case INTERPOLATION:
			return getInterpolatedColor(value);
		case INTERPOLATION_AND_DITHER:
			return getSimpleColor(value);
		case SIMPLE:
			return getSimpleColor(value);
		}
		
		return 0;
	}
	
	
	public int getSimpleColor(double value) {
		
		return colors[findMinIndex(value)];
		
	}
	
	
	public int getInterpolatedColor(double value) {
		
		int color = 0;
		
		int greaterIndex = findMinIndex(value);
		int lessIndex = greaterIndex - 1;
	
	
		if ((0 == greaterIndex) || (n == greaterIndex)) {
			return colors[greaterIndex];
		}
		
		
		double u1 = values[lessIndex];
		double u2 = values[greaterIndex];
		double du = Math.abs(u2 - u1);
		
		int color1 = colors[lessIndex + 1];
		int color2 = colors[greaterIndex + 1];
		
		//	for each color
		for (int i = 0; i < 3; ++i) {
			double component 
			= ( getColorComponent(color1, i) * Math.abs(u2 - value) + getColorComponent(color2, i) * Math.abs(value - u1) ) / (du);
			
			color = setColorComponent(color, i, (int) Math.round(component));
		}
		
		
		return color;
	}
	
	public int findClosestColor(int color) {
		
		int error = Integer.MAX_VALUE;
		
		int resultIndex = 0;
		
		for (int i = 0; i < colors.length; ++i) {
			
			int newError = getColorDifferenceError(color, colors[i]);
			
			if (newError < error) {
				resultIndex = i;
				error = newError;
			}
		}
		
		return colors[resultIndex];
	}
	
	public int getColorDifferenceError(int color1, int color2) {
		int error = 0;
		
		for (int i = 0; i < 3; ++i)  {
			error += (getColorComponent(color2, i) - getColorComponent(color1, i)) * (getColorComponent(color2, i) - getColorComponent(color1, i));
		}
		
		return error;
	}
	
	public int getColorDifference(int color1, int color2) {
		
		int errorColor = 0;
		
		for (int i = 0; i < 3; ++i)  {
			//errorColor = (getColorComponent(color2, i) - getColorComponent(color1, i)) * (getColorComponent(color2, i) - getColorComponent(color1, i));
			
			int component = getColorComponent(color1, i) - getColorComponent(color2, i);
			
			component = (component < 0) ? 0 : component;
			
			errorColor = setColorComponent(errorColor, i, component);
		}
		
		return errorColor;
	}
	
	public int appendColor(int color1, int color2) {
		
		int resultColor = 0;
		
		for (int i = 0; i < 3; ++i) {
			//resultColor = setColorComponent(resultColor, i, getColorComponent(color2, i) + getColorComponent(color1, i));
			
			int component = getColorComponent(color1, i) + getColorComponent(color2, i);
			
			component = ( component > 0xFF ) ? 0xFF : component;
			
			resultColor = setColorComponent(resultColor, i, component);
		}
		
		return resultColor;
		
	}
	
	public int appendColor(int color1, int color2, double coeff) {
		
		int resultColor = 0;
		
		for (int i = 0; i < 3; ++i) {
			//resultColor = setColorComponent(resultColor, i, getColorComponent(color2, i) + getColorComponent(color1, i));
			
			int component = getColorComponent(color1, i) + (int) (getColorComponent(color2, i) * coeff);
			
			component = ( component > 0xFF ) ? 0xFF : component;
			
			resultColor = setColorComponent(resultColor, i, component);
		}
		
		return resultColor;
		
	}
	
	
	
	private void setValues() {
		
		values[0] = function.getMinValue();
		values[n - 1] = function.getMaxValue();
		
		//	length of interval / number of intervals in interval
		double stepValue = (values[n - 1] - values[0]) / (n - 1);
		
		for (int i = 1; i < n - 1; ++i) {
			values[i] = values[0] + i * stepValue;
		}
	}
	
	private int findMinIndex(double value) {

		int index = 0;
		
		/*
		 * value < values[0] 		- return 0
		 * value >= value[0] 		- return 1
		 * value >= value[1] 		- return 2
		 * ...
		 * value >= value[i] 		- return i + 1
		 * ...
		 * value >= value[n - 2]	- return n - 1
		 * 
		 * value >= value[n - 1]	- return n
		 */
		
		
		//	if less than minimum
		if (value < values[0]) {
			index = 0;
		}
		//	if greater or equal to maximum
		else if (value >= values[n - 1]) {
			index = n; 
		}
		else {
			//	for 0 .. n - 2
			for (int i = 0; i < n - 1; ++i) {
				
				if ((value >= values[i]) && (value < values[i + 1])) {
					index = i + 1;
					break;
				}
			}	
		}
		return index;
	}
	
	private int getColorComponent(int color, int index) {
		
		return (color >> 8 * index) & 0xFF;
		
	}
	
	private int setColorComponent(int color, int index, int component) {

		return color | (component << (index * 8) );
		
	}
	
	public enum ColorType {
		SIMPLE,
		INTERPOLATION,
		INTERPOLATION_AND_DITHER
	};
}
