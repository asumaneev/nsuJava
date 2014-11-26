package ru.nsu.sumaneev.tetragon.util;

public class Tetragon {

	//	a b c d	
	private Point[] points = null;
	
	private int minX = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxY = Integer.MIN_VALUE;
	
	private boolean isConcave = false;
	private int concaveVertexIndex = -1;
	
	public int getMinX() {
		return minX;
	}


	public int getMaxX() {
		return maxX;
	}


	public int getMinY() {
		return minY;
	}


	public int getMaxY() {
		return maxY;
	}


	public Tetragon(Point a, Point b, Point c, Point d) throws TetragonException {
		points = new Point[4];
		points[0] = a;
		points[1] = b;
		points[2] = c;
		points[3] = d;		
		
		initMax();
		
		Line ac = new Line(a.getX(), a.getY(), c.getX(), c.getY());
		Line bd = new Line(b.getX(), b.getY(), d.getX(), d.getY());
		
		if (ac.getIntersection2(bd)) {
			isConcave = false;
		}
		else {
			isConcave = true;
			
			int x = (a.getX() + c.getX()) / 2;
			int y = (int) Math.round(ac.getY(x));
			
			//	b or d
			if (!pointBelongs(x, y, maxX, maxY)) {
				
				//	if Sabc < Sadc => b
				//	else d
				
				double ab = a.getLength(b);
				double bc = b.getLength(c);
				double ca = c.getLength(a);
				double dc = d.getLength(c);
				double ad = a.getLength(d);
				
				double pABC = (ab + bc + ca) / 2;
				double pADC = (ad + dc + ca) / 2;
				
				double sABC = Math.sqrt(pABC * (pABC - ab) * (pABC - bc) * (pABC - ca));
				double sADC = Math.sqrt(pADC * (pADC - ad) * (pADC - dc) * (pADC - ca));
				
				if (sABC < sADC) {
					concaveVertexIndex = 1;
				}
				else {
					concaveVertexIndex = 3;
				}
				
			}
			//	a or c
			else {
			
				//	if Sbcd < Sbad => c
				//	else a
				
				double bc = b.getLength(c);
				double cd = c.getLength(d);
				double db = d.getLength(b);
				double ba = b.getLength(a);
				double ad = a.getLength(d);
				
				double pBCD = (bc + cd + db) / 2;
				double pBAD = (ba + ad + db) / 2;
				
				double sBCD = Math.sqrt(pBCD * (pBCD - bc) * (pBCD - cd) * (pBCD - db));
				double sBAD = Math.sqrt(pBAD * (pBAD - ba) * (pBAD - ad) * (pBAD - db));
				
				if (sBCD < sBAD) {
					concaveVertexIndex = 2;
				}
				else {
					concaveVertexIndex = 0;
				}
				
			}
		}
	}

	public void setA(Point p) {
		points[0] = p;
		
		initMax();
	}
	
	public void setB(Point p) {
		points[1] = p;
		
		initMax();
	}
	
	public void setC(Point p) {
		points[2] = p;
		
		initMax();
	}
	
	public void setD(Point p) {
		points[3] = p;
		
		initMax();
	}
	
	public Point getA() {
		return points[0];
	}


	public Point getB() {
		return points[1];
	}


	public Point getC() {
		return points[2];
	}


	public Point getD() {
		return points[3];
	}
	
	public int getConcaveVertexIndex() {
		return concaveVertexIndex;
	}
	
	public boolean pointBelongs(int x, int y, int width, int heght) {
		
		for (int i = 0; i < 4; ++i) {
			if ((points[i].getX() == x) && (points[i].getY() == y)) {
				return true;
			}
		}
		
		Line pointRightHorizontalLine = new Line(x, y, width, y);
		Line pointLeftHorizontalLine = new Line(0, y, x, y);
		
		Line pointUpVerticalLine = new Line(x, y, x, heght);
		Line pointDownVerticalLine = new Line(x, 0, x, y);

		int leftHorizontalIntersectons = 0;
		int rightHorizontalIntersectons = 0;
		
		int upVerticalIntersectons = 0;
		int downVerticalIntersectons = 0;
		
		for (int i = 0; i < 4; ++i) {
			
			
			Line tetragonSide = new Line(points[i].getX(), points[i].getY(), points[(i + 1) % 4].getX(), points[(i + 1) % 4].getY());
			

			if (pointLeftHorizontalLine.getIntersection2(tetragonSide)) {
				++leftHorizontalIntersectons;
			}
			
			if (pointRightHorizontalLine.getIntersection2(tetragonSide)) {
				++rightHorizontalIntersectons;
			}
			
			if (pointDownVerticalLine.getIntersection2(tetragonSide)) {
				++downVerticalIntersectons;
			}
			
			if (pointUpVerticalLine.getIntersection2(tetragonSide)) {
				++upVerticalIntersectons;
			}
			
		}

		for (int i = 0; i < 4; ++i) {
			
			if (points[i].getY() == y) {
				if (x < points[i].getX()) {
					--rightHorizontalIntersectons;
				}
				
				if (x > points[i].getX()) {
					--leftHorizontalIntersectons;
				}
			}
			
			if (points[i].getX() == x) {
				if (y < points[i].getY()) {
					--upVerticalIntersectons;
				}
				
				if (y > points[i].getY()) {
					--downVerticalIntersectons;
				}
			}
		}
		
		
		if ( (0 == rightHorizontalIntersectons % 2) || (0 == leftHorizontalIntersectons % 2) ) {
			return false;
		}
		
		
		if ( (0 == upVerticalIntersectons % 2) || (0 == downVerticalIntersectons % 2) ) {
			return false;
		}
		
		
		/*
		if ( (0 == (rightHorizontalIntersectons) % 2) ) {
			return false;
		}
		*/
		
		return true;
	}
	
	public boolean isConcave() {
		return isConcave;
	}
	
	private void initMax() {
		for (int i = 0; i < 4; ++i) {
			if (points[i].getX() < minX) {
				minX = points[i].getX();
			}
			
			if (points[i].getX() > maxX) {
				maxX = points[i].getX();
			}
			
			if (points[i].getY() < minY) {
				minY = points[i].getY();
			}
			
			if (points[i].getY() > maxY) {
				maxY = points[i].getY();
			}
		}
	}

}
