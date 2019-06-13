package graphic;

import java.awt.*;
import java.awt.event.*;
import java.lang.String;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Cube extends JFrame {
	public static void main(String[] args) {new Cube();}
	
	Cube(){
		super("Cubo");
		CvCubePers cube = new CvCubePers();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		setLayout(new BorderLayout());
		add("Center", cube);
		Dimension dim = getToolkit().getScreenSize();
        setSize(dim.width/2, dim.height/2);
		setLocation(dim.width/4, dim.height/4);
		setVisible(true);
				
	}
	

}

@SuppressWarnings("serial")
class CvCubePers extends Canvas implements Runnable {
	int centerX, centerY, w, h, i = 0;
	Hermite herm = new Hermite();
	Obj obj = new Obj();
	Color[] color = {Color.blue, Color.green, Color.cyan,
					 Color.magenta, Color.red, Color.yellow}; 	
	
	void rotate(Point3D a, Point3D b, double alpha) {
		obj.rotateCube(a, b, alpha);
	}
	
	void translate(double a, double b, double c) {
		obj.translateCube(a, b, c);
	}
	
	int iX(float x) {return Math.round(centerX + x);}
	int iY(float y) {return Math.round(centerY - y);}
	

	public void paint(Graphics g) {
		Dimension dim = getToolkit().getScreenSize();
		dim.setSize(dim.width/8, dim.height/8);
		int maxX = dim.width - 1, maxY = dim.height -1, 
				minMaxXY = Math.min(maxX, maxY);
		if (w != dim.width || h != dim.height) {
			w = dim.width; h = dim.height;
		}
		
		centerX = maxX/2;
		centerY = maxY/2;
		obj.d = obj.rho * minMaxXY / obj.objSize;
		obj.eyeAndScreen();
		Point2D[] p = new Point2D[4];
		
		for (int j = 0; j < 6; j++) {
			Polygon pol = new Polygon();
			Square sq = obj.f[j];
			
			for (int i = 0; i < 4; i++) {
				int vertexNr = sq.nr[i];
				p[i] = obj.vScr[vertexNr];
				pol.addPoint(iX(p[i].x), iY(p[i].y));
			}
			
			g.setColor(color[j]);
			if (Tools2D.area2(p[0], p[1], p[2]) > 0)
				g.fillPolygon(pol);
		}
		
		g.setColor(Color.black);		
		for (; i < 1000; i++) {
			g.drawOval(iX(herm.vec[i].x), iY(herm.vec[i].y), (int) (herm.vec[i].z/2), (int) (herm.vec[i].z/2));
		}
					
	}
	
	 public void getPoints() {
		    JFrame frame = new JFrame();
		    String p1 = JOptionPane.showInputDialog(frame, "Ponto Inicial p1(x, y, z)");
		    String p2 = JOptionPane.showInputDialog(frame, "Ponto Final p2(x, y, z)");
		    String r1 = JOptionPane.showInputDialog(frame, "Vetor Inicial r1 = (x, y, z)");
		    String r2 = JOptionPane.showInputDialog(frame, "Vetor Final r2 = (x, y, z)");
		    
		    if (p1 == null || p2 == null || r1 == null || r2 == null)
		    	System.exit(1);
		    
		    herm.setPoint(stringToPoint3D(p1), 0);
		    herm.setPoint(stringToPoint3D(p2), 1);
		    herm.setPoint(stringToPoint3D(r1), 2);
		    herm.setPoint(stringToPoint3D(r2), 3);
		    
	 }
	 
	 public static Point3D stringToPoint3D(String input) {
		 String[] tokens = input.split(",");
		 
		 Point3D p = new Point3D(Float.parseFloat(tokens[0]), 
				 				 Float.parseFloat(tokens[1]), 
				 				 Float.parseFloat(tokens[2]));

		 return p;
	 }

	Thread thr = new Thread(this);
	
	public void run() {
		Point3D a = new Point3D(200,10,0);
		Point3D b = new Point3D(100,-100,10);
		Point3D f = new Point3D(190,10,0);
		Point3D d = new Point3D(110,200,20);
		
		Point3D orig = new Point3D(0,0,0);
		Point3D axisX = new Point3D(1000, 0, 0);
		Point3D axisY = new Point3D(0, 1000, 0);
		Point3D axisZ = new Point3D(0, 0, 1000);
		
		herm.setPoint(a, 0);
		herm.setPoint(b, 1);
		herm.setPoint(f, 2);
		herm.setPoint(d, 3);
		
		double gradX0, gradY0, gradZ0, gradX1, gradY1, gradZ1;
		int p0 = 0, p1 = 1;
		
		try {
			//getPoints();
			
			herm.calc();
			
			translate(herm.vec[0].x *0.03, herm.vec[0].y *0.03, herm.vec[0].z * 0.03);
			
			gradX0 = Math.atan2(herm.vec[p1].x - herm.vec[p0].x, herm.vec[p1].y - herm.vec[p0].y);
			gradY0 = Math.atan2(herm.vec[p0].y, herm.vec[p1].y);
			gradZ0 = Math.atan2(herm.vec[p0].z, herm.vec[p1].z);
			
						
			for(int c = 1; c < 1000; c++) {
				
				translate((herm.vec[c].x - herm.vec[c-1].x) * 0.03,
						  (herm.vec[c].y - herm.vec[c-1].y) * 0.03,
						  (herm.vec[c].z - herm.vec[c-1].z) * 0.03);
				
				p0 = p1;
				p1 = c;
				
				gradX1 = Math.atan2(herm.vec[p1].x - herm.vec[p0].x, herm.vec[p1].y - herm.vec[p0].y);
				gradY1 = Math.atan2(herm.vec[p0].y, herm.vec[p1].y);
				gradZ1 = Math.atan2(herm.vec[p0].z, herm.vec[p1].z);
				
				//System.out.println("gx0: " + gradX0 + "		gx1: " + gradX1);
				//System.out.println("gy0: " + gradY0 + "		gy1: " + gradY1);
				
				//rotate(orig, axisX, gradX0 + gradX1);
				
				rotate(orig, axisY, Math.PI / (180 * (gradY1 - gradY0)));				
				//rotate(orig, axisZ, gradZ0 + gradZ1);
				
				gradX0 = gradX1;
				gradY0 = gradY1;
				gradZ0 = gradZ1;

				repaint();
				
				i = c;
				
				Thread.sleep(100);
			}
				
		} catch (InterruptedException e) {}
		
	}
	
	CvCubePers() {thr.start();}
		
}


	/*	   7 - - - 6
	 * 	  / |	  /|
	 * 	 /	|	 / |   
	 *  4 - - - 5  |
	 *  |	3 - |- 2
	 *  |  /	| /
	 *  | /		|/
	 *  0 - - - 1
	 *  
	 * */

class Obj { // Contém dados do objeto 3D
	float rho, theta = (float) (-Math.PI/2), // Página 47
			phi = 0F, d, objSize,
			v11, v12, v13, v21, v22, v23, v32, v33, v43; // Elementos da matriz de visualização V
	Point3D[] w; 	// Coordenadas do universo
	Point2D[] vScr;	// Coordenadas da tela
	Square[] f;
	
	Obj(){
		w = new Point3D[8];
		vScr = new Point2D[8];
		f = new Square[6];
		// Superfície da base:
		w[0] = new Point3D( 1, -1, -1);
		w[1] = new Point3D( 1,  1, -1);
		w[2] = new Point3D(-1,  1, -1);
		w[3] = new Point3D(-1, -1, -1);
		// Superfície do topo:
		w[4] = new Point3D( 1, -1,  1);
		w[5] = new Point3D( 1,  1,  1);
		w[6] = new Point3D(-1,  1,  1);
		w[7] = new Point3D(-1, -1,  1);
		// Quadrados:
		f[0] = new Square(0, 1, 5, 4); // Frente 	(Azul)
		f[1] = new Square(1, 2, 6, 5); // Direita 	(Verde)
		f[2] = new Square(2, 3, 7, 6); // Atrás		(Ciano)
		f[3] = new Square(3, 0, 4, 7); // Esquerda	(Magenta)
		f[4] = new Square(4, 5, 6, 7); // Topo		(Vermelho)
		f[5] = new Square(0, 3, 2, 1); // Base		(Amarelo)
		
		objSize = (float) Math.sqrt(12F);
			// = sqrt(2*2 + 2*2 + 2*2);
			// = distância entre dois vértices opostos
		
		rho = objSize * 5;
	}
	
	void rotateCube(Point3D axis1, Point3D axis2, double alpha) {
		Rota3D.initRotate(axis1, axis2, alpha);
		
		for(int i = 0; i < 8; i++)
			w[i] = Rota3D.rotate(w[i]);
	}
	
	void translateCube(double x, double y, double z) {
		for(int i = 0; i < 8; i++) {
			w[i].x += x;
			w[i].y += y;
			w[i].z += z;
		}
	}
	
	void resetCube() {
		w[0] = new Point3D( 1, -1, -1);
		w[1] = new Point3D( 1,  1, -1);
		w[2] = new Point3D(-1,  1, -1);
		w[3] = new Point3D(-1, -1, -1);
		w[4] = new Point3D( 1, -1,  1);
		w[5] = new Point3D( 1,  1,  1);
		w[6] = new Point3D(-1,  1,  1);
		w[7] = new Point3D(-1, -1,  1);
	}
	
	void initPersp() {
		float costh = (float) Math.cos(theta),
			  sinth = (float) Math.sin(theta),
			  cosph = (float) Math.cos(phi),
			  sinph = (float) Math.sin(phi);
		v11 = -sinth;
		v12 = -cosph * costh;
		v13 = sinph * costh;
		v21 = costh;
		v22 = -cosph * sinth;
		v23 = sinph * sinth;
		v32 = sinph;
		v33 = cosph;
		v43 = -rho;
	}
	
	void eyeAndScreen() {
		initPersp();
		for (int i = 0; i < 8; i++) {
			Point3D p = w[i];
			float x = v11 * p.x + v21 * p.y,
				  y = v12 * p.x + v22 * p.y + v32 * p.z,
				  z = v13 * p.x + v23 * p.y + v33 * p.z + v43;
			vScr[i] = new Point2D(-d * x/z, -d * y/z);
				  
		}
	}
}

class Square {
	int nr[];
	Square(int iA, int iB, int iC, int iD){
		nr = new int[4];
		nr[0] = iA;
		nr[1] = iB;
		nr[2] = iC;
		nr[3] = iD;
	}
}