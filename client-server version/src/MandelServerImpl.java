// TicketServerImpl.java
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

public class MandelServerImpl extends UnicastRemoteObject implements MandelServer {
  
  MandelServerImpl() throws RemoteException { };

  public ImageIcon calculateMandelBrotFractalImage(int x_start, int x_end, int y_start, int y_end, int interacoes, Vetor2D posicaoFractal, double zoom, int MEIO_X, int MEIO_Y) throws RemoteException {

	Image plainImg = new BufferedImage(MEIO_X*2, MEIO_Y*2, BufferedImage.TYPE_INT_RGB);
	Graphics i = plainImg.getGraphics();

	for (int x = x_start; x < x_end ; x++) {
		for (int y = y_start; y < y_end; y++) {
			i.setColor(pintaPonto(x, y, interacoes, posicaoFractal, zoom, MEIO_X, MEIO_Y));
			i.drawLine(x, y, x + 1, y);
		}
	}

    return new ImageIcon(plainImg);
  }

  /**
	 * Pinta um ponto do fractal
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Color pintaPonto(int x, int y, int interacoes, Vetor2D posicaoFractal, double zoom, int MEIO_X, int MEIO_Y) {

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

  public static void main(String [] args) {

    System.setSecurityManager(new RMISecurityManager());

    if (args.length!=1) {
      System.err.println("Usage: MandelServerImpl <server-rmi-url>");
      System.exit(-1);
    }

    try {  
      String name = args[0];
      MandelServerImpl server = new MandelServerImpl();
      Naming.rebind(name, server);
      System.out.println("Started MandelServer, registered as " + name);
    }
    catch(Exception e) {
      System.out.println("Caught exception while registering: " + e);
      System.exit(-1);
    }
  }
}