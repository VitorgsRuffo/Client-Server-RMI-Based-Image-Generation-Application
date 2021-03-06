
/**
 * 
 */
import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



/**
 * Applet para brincar com um fractal MandelBrot. Suporta zoom in e out e cores
 * RGB.
 * 
 * @author Rodrigo
 */



public class MandelBrotApplet extends Applet implements MouseListener,
		MouseMotionListener {


	private static final long serialVersionUID = 1L;

	private static final int TAMANHO_X = 640;

	private static final int TAMANHO_Y = 480;

	private static final int MEIO_X = TAMANHO_X / 2;

	private static final int MEIO_Y = TAMANHO_Y / 2;

	private Vetor2D posicaoFractal;

	private Vetor2D posicaoJanela;

	private Image offscreen;

	private boolean recalcular = true;

	private boolean janelaZoom = true;

	private double zoom;

	private double fatorZoom;

	private int interacoes;

	private int py;

	/* ------ VETOR2D ------- */
	private class Vetor2D {
		public double x;
		public double y;
	
		public Vetor2D(){
			x=0;
			y=0;
		}
	
		public Vetor2D(double a, double b){
			x = a;
			y = b;
		}
	
		public double comprimento()
		{	double c;
			c = x - y;
			return c;
		}
	}
	/* ====================== */

	/**
	 * Inicializa recursos utilizados pelo Applet
	 */
	public void init() {
		offscreen = this.createImage(TAMANHO_X, TAMANHO_Y);

		posicaoFractal = new Vetor2D(-0.8, 0);
		posicaoJanela = new Vetor2D(TAMANHO_X / 2, TAMANHO_Y / 2);

		zoom = TAMANHO_X * 0.125296875;
		fatorZoom = 2.178;

		interacoes = (int) ((2048 - TAMANHO_X) * 0.049715909 * (Math.log(zoom) / Math
				.log(10)));

		this.resize(TAMANHO_X, TAMANHO_Y);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

	}

	/**
	 * Inicia applet e pinta o fractal pela primeira vez
	 */
	public void start() {
		Graphics i = offscreen.getGraphics();

		for (int x = 0; x < TAMANHO_X; x++) {
			for (int y = 0; y < TAMANHO_Y; y++) {
				i.setColor(pintaPonto(x, y));
				i.drawLine(x, y, x + 1, y);
			}
		}
	}

	/**
	 * Pinta um ponto do fractal
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Color pintaPonto(int x, int y) {

		Vetor2D z = new Vetor2D();
		int n = 0;
		double a;

		for (; n < interacoes && z.comprimento() < 3; n++) {
			a = z.x * z.x - (z.y * z.y) + posicaoFractal.x
					+ ((x - MEIO_X) / zoom);
			z.y = z.x * z.y * 2 + posicaoFractal.y + ((y - MEIO_Y) / zoom);
			z.x = a;
		}

		return (n == interacoes) ? new Color(0x000000) :
				new Color((int) ((Math.cos(n / 10.0f) + 1.0f) * 127.0f),
						(int) ((Math.cos(n / 20.0f) + 1.0f) * 127.0f),
						(int) ((Math.cos(n / 300.0f) + 1.0f) * 127.0f));
	}

	/**
	 * Recalcula imagem do fractal e desenha double buffer
	 */
	public void update(Graphics g) {

		Graphics i = offscreen.getGraphics();
		Color corPixel;

		if (recalcular) {
			for (int y = 0; y < TAMANHO_Y; y++) {
				for (int x = 0; x < TAMANHO_X; x++) {
					corPixel = pintaPonto(x, y);
					i.setColor(corPixel);
					i.drawLine(x, y, x + 1, y);
					g.setColor(corPixel);
					g.drawLine(x, y, x + 1, y);
				}
			}
		}

		paint(g);
	}

	/**
	 * Pinta double buffer na saida grafica e desenha o retangulo de zoom
	 */
	public void paint(Graphics g) {

		g.drawImage(offscreen, 0, 0, this);

		/* Desenha retangulo de zoom */
		if (janelaZoom) {
			g.setColor(new Color(0xFFFF00));
			g.drawRect((int) (posicaoJanela.x - (MEIO_X / fatorZoom)),
					(int) (posicaoJanela.y - (MEIO_Y / fatorZoom)),
					(int) (2.0f * (MEIO_X / fatorZoom)),
					(int) (2.0f * (MEIO_Y / fatorZoom)));
		}

	}

	/**
	 * Metodo chamado no evento de clique do mouse. Efetua zoom in ou out
	 * dependendo do botao
	 */
	public void mouseClicked(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1) {
			posicaoFractal.x += ((posicaoJanela.x - MEIO_X) / zoom);
			posicaoFractal.y += ((posicaoJanela.y - MEIO_Y) / zoom);
			zoom *= fatorZoom;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			posicaoFractal.x += (((MEIO_X - posicaoJanela.x) / zoom) * fatorZoom);
			posicaoFractal.y += (((MEIO_Y - posicaoJanela.y) / zoom) * fatorZoom);
			zoom /= fatorZoom;

		}
		else if (e.getButton() == MouseEvent.BUTTON2) {
			posicaoFractal.x = -0.8;
			posicaoFractal.y = 0;
			zoom = TAMANHO_X * 0.125296875;			
		}

		interacoes = (int) ((2048 - TAMANHO_X) * 0.049715909 * (Math.log(zoom) / Math
				.log(10)));

		recalcular = true;
		this.repaint();
	}

	/**
	 * Quando o mouse entra na area do applet
	 */
	public void mouseEntered(MouseEvent e) {
		janelaZoom = true;
	}

	/**
	 * Quando o mouse sai da area do applet
	 */
	public void mouseExited(MouseEvent e) {
		janelaZoom = false;
		
		recalcular = false;
		this.repaint();
	}

	/**
	 * Metodo nao utilizado
	 */
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Metodo nao utilizado
	 */
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Metodo chamado no evento de arrastar do mouse. causa o redimensionamento
	 * do retangulo de zoom
	 */
	public void mouseDragged(MouseEvent e) {

		if (py > e.getY()) {
			fatorZoom *= 0.98f;
		} else if (py < e.getY()) {
			fatorZoom *= 1.02f;
		}

		py = e.getY();

		recalcular = false;
		this.repaint();
	}

	/**
	 * Metodo chamado no evento de movimento do mouse. Reposiciona o retangulo
	 * de zoom.
	 */
	public void mouseMoved(MouseEvent e) {
		posicaoJanela.x = e.getX();
		py = e.getY();
		posicaoJanela.y = e.getY();

		recalcular = false;
		this.repaint();
	}

	static public void main (String argv[]) {
	    final Applet applet = new MandelBrotApplet();
	    Frame frame = new Frame (
	                 "MyApplet");
	    frame.addWindowListener (
	                  new WindowAdapter()
	    {
	      public void windowClosing (
	                   WindowEvent event)
	      {
	        applet.stop();
	        applet.destroy();
	        System.exit(0);
	      }
	    });
	    frame.add (
	      "Center", applet);
	    frame.setSize(TAMANHO_X, TAMANHO_Y);
	    frame.setVisible(true);
	    applet.init();
	    applet.start();
	  }

}
