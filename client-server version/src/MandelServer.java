import java.rmi.*;
import javax.swing.ImageIcon;

public interface MandelServer extends Remote {
    public ImageIcon calculateMandelBrotFractalImage(int x_start, int x_end, int y_start, int y_end, int interacoes, Vetor2D posicaoFractal, double zoom, int MEIO_X, int MEIO_Y) throws RemoteException;
}
