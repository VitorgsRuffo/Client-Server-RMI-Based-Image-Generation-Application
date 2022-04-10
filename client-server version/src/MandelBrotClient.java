
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
import java.rmi.*;
import java.awt.image.BufferedImage;



/**
 * Applet para brincar com um fractal MandelBrot. Suporta zoom in e out e cores
 * RGB.
 * 
 * @author Rodrigo
 * @modified by Vitor and Wellinton
 */



public class MandelBrotClient extends Applet implements MouseListener,
		MouseMotionListener {

	private final MandelServer[] servers;

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


	public MandelBrotClient(MandelServer[] servers){
		this.servers = servers;
	}

	/**
	 * Inicializa recursos utilizados pelo Applet
	 */
	public void init() {
        offscreen = new BufferedImage(TAMANHO_X, TAMANHO_Y, BufferedImage.TYPE_INT_RGB);
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

		//open 4 threads
		//assign each one of them to send to a server the request for calculating 1/4 of the image (i.e, draw to a image's graphics instance and return that Image instance).
		MandelBrotThread thread1 = new MandelBrotThread(1, this.servers[0], 0, MEIO_X, 0, MEIO_Y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y);
		MandelBrotThread thread2 = new MandelBrotThread(2, this.servers[1], MEIO_X, TAMANHO_X, 0, MEIO_Y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y);
		MandelBrotThread thread3 = new MandelBrotThread(3, this.servers[2], 0, MEIO_X, MEIO_Y, TAMANHO_Y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y);
		MandelBrotThread thread4 = new MandelBrotThread(4, this.servers[3], MEIO_X, TAMANHO_X, MEIO_Y, TAMANHO_Y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y);

		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		
		//wait for all the 4 results.
		while(thread1.isAlive()){}
		while(thread2.isAlive()){}
		while(thread3.isAlive()){}
		while(thread4.isAlive()){}

		//draw the resulting 4 images to the offscreen's graphics instance.
		Image img1 = thread1.getResultImage();
		Image img2 = thread2.getResultImage();
		Image img3 = thread3.getResultImage();
		Image img4 = thread4.getResultImage();

		Graphics i = offscreen.getGraphics();
		
		i.drawImage(img1, 0, 0, MEIO_X, MEIO_Y, 0, 0, MEIO_X, MEIO_Y, this);
		i.drawImage(img2, MEIO_X, 0, TAMANHO_X, MEIO_Y, MEIO_X, 0, TAMANHO_X, MEIO_Y, this);
		i.drawImage(img3, 0, MEIO_Y, MEIO_X, TAMANHO_Y, 0, MEIO_Y, MEIO_X, TAMANHO_Y, this);
		i.drawImage(img4, MEIO_X, MEIO_Y, TAMANHO_X, TAMANHO_Y, MEIO_X, MEIO_Y, TAMANHO_X, TAMANHO_Y, this);
	}

	

	/**
	 * Recalcula imagem do fractal e desenha double buffer
	 */
	public void update(Graphics g) {

		if (recalcular) {

			//open 4 threads
			//assign each one of them to send to a server the request for calculating 1/4 of the image (i.e, draw to a image's graphics instance and return that Image instance).
			MandelBrotThread thread1 = new MandelBrotThread(1, this.servers[0], 0, MEIO_X, 0, MEIO_Y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y);
			MandelBrotThread thread2 = new MandelBrotThread(2, this.servers[1], MEIO_X, TAMANHO_X, 0, MEIO_Y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y);
			MandelBrotThread thread3 = new MandelBrotThread(3, this.servers[2], 0, MEIO_X, MEIO_Y, TAMANHO_Y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y);
			MandelBrotThread thread4 = new MandelBrotThread(4, this.servers[3], MEIO_X, TAMANHO_X, MEIO_Y, TAMANHO_Y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y);

			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
			
			//wait for all the 4 results.
			while(thread1.isAlive()){}
			while(thread2.isAlive()){}
			while(thread3.isAlive()){}
			while(thread4.isAlive()){}

			//draw the resulting 4 images to the offscreen's graphics instance.
			Image img1 = thread1.getResultImage();
			Image img2 = thread2.getResultImage();
			Image img3 = thread3.getResultImage();
			Image img4 = thread4.getResultImage();

			Graphics i = offscreen.getGraphics();
			
			i.drawImage(img1, 0, 0, MEIO_X, MEIO_Y, 0, 0, MEIO_X, MEIO_Y, this);
			i.drawImage(img2, MEIO_X, 0, TAMANHO_X, MEIO_Y, MEIO_X, 0, TAMANHO_X, MEIO_Y, this);
			i.drawImage(img3, 0, MEIO_Y, MEIO_X, TAMANHO_Y, 0, MEIO_Y, MEIO_X, TAMANHO_Y, this);
			i.drawImage(img4, MEIO_X, MEIO_Y, TAMANHO_X, TAMANHO_Y, MEIO_X, MEIO_Y, TAMANHO_X, TAMANHO_Y, this);
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
	Metodo nao utilizado
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
		System.setSecurityManager(new RMISecurityManager());

		if (argv.length!=4) {
			System.err.println("Usage: MandelBrotClient <server1-rmi-url> <server2-rmi-url> <server3-rmi-url> <server4-rmi-url>");
			System.exit(-1);
		}

		String fullname1 = argv[0];
		String fullname2 = argv[1];
		String fullname3 = argv[2];
		String fullname4 = argv[3];

		MandelServer[] servers = {null, null, null, null};    
		
		try {
			servers[0] = (MandelServer) Naming.lookup(fullname1);
			servers[1] = (MandelServer) Naming.lookup(fullname2);
			servers[2] = (MandelServer) Naming.lookup(fullname3);
			servers[3] = (MandelServer) Naming.lookup(fullname4);

		} catch (Exception e) {
			System.out.println("Caught an exception doing name lookup: "+e);
			System.exit(-1);
		}

	    final Applet applet = new MandelBrotClient(servers);
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
