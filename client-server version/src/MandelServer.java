import java.rmi.*;
import java.awt.Image;

public interface MandelServer extends Remote 
{

    public Image calculateMandelBrotFractalImage(Image plainImage, int x_start, int x_end, int y_start, int y_end, int interacoes, Vetor2D posicaoFractal, double zoom, int MEIO_X, int MEIO_Y) throws RemoteException;
}
