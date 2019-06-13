package graphic;

import java.awt.Canvas;

@SuppressWarnings("serial")
public class Hermite extends Canvas {
	public static void main(String[] args) {new Hermite();}
	
	Point3D[] vec = new Point3D[1000];
	
	Point3D a = new Point3D(40, -25, 0),
			b = new Point3D(-100, 77, 0);
	
	Point3D[] v = new Point3D[2];
	

    /*--------------------------------------------------------------------------
     *  Compute Hermite Cubic Points Derivatives 
     *  +-----------------+   +----------------+
     *  |  2  -2   1   1  |   |  x0   y0   z0  |
     *  | -3   3  -2  -1  | * |  x1   y1   z1  | * [t^3 t^2 t 1] = [x, y, z]
     *  |  0   0   1   0  |   |  x'0  y'0  z'0 |
     *  |  1   0   0   0  |   |  x'1  y'1  z'1 |
     *  +-----------------+   +----------------+
     *  		Mh					  Gh				T				P
     -------------------------------------------------------------------------*/
	
	public void calc() {
		int c = 0;
		
		/* step = t; a = P1; b = P4; v[0] = R1; v[1] = R4;
		 * P(t) = T * Mh * Gh;
		 * P(t) = P1 * (2t³ - 3t² +1) + P4 * (-2t³ + 3t²) + R1 * (t³ - 2t² + t) + R4 * (t³ - t²); 
		 * */
		for(double step = 0.0; step < 1; step += 0.001) {
			
			double x = (a.x * ((2 * step * step * step) - (3 * step * step) + 1)) +
					   (b.x * ((-2 * step * step * step) +(3 * step * step))) +
					   (v[0].x * ((step * step * step) - (2 * step * step) + step)) +
					   (v[1].x * ((step * step * step) - (step * step)));
			
			double y = (a.y * ((2 * step * step * step) - (3 * step * step) + 1)) +
					   (b.y * ((-2 * step * step * step) +(3 * step * step))) +
					   (v[0].y * ((step * step * step) - (2 * step * step) + step)) +
					   (v[1].y * ((step * step * step) - (step * step)));
			
			double z = (a.z * ((2 * step * step * step) - (3 * step * step) + 1)) +
					   (b.z * ((-2 * step * step * step) +(3 * step * step))) +
					   (v[0].z * ((step * step * step) - (2 * step * step) + step)) +
					   (v[1].z * ((step * step * step) - (step * step)));
			
			Point3D q = new Point3D(x, y, z);
			vec[c] = q;
			c++;
			System.out.println("xc: " + q.x + "	yc: " + q.y + "	zc: " + q.z);
		}
	
	}
	
	public void setPoint(Point3D p, int id) {
		if (id == 0)
			a = p;
		else if (id == 1)
			b = p;
		else if (id == 2)
			v[0] = p;
		else
			v[1] = p;
	}
	
}

