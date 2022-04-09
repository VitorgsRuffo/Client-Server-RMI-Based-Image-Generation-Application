import java.awt.Image;


public class MandelBrotThread extends Thread {
    private int threadNum;
    private MandelServer server;
    private Image plainImage;
    private int x_start;
    private int x_end;
    private int y_start;
    private int y_end;
    private int interacoes;
    private Vetor2D posicaoFractal;
    private double zoom;
    private int MEIO_X;
    private int MEIO_Y;
    private Image resultImage;

    public MandelBrotThread(int threadNum, MandelServer server, Image plainImage, int x_start, int x_end, int y_start, int y_end, int interacoes, Vetor2D posicaoFractal, double zoom, int MEIO_X, int MEIO_Y){
        this.threadNum = threadNum;
        this.server = server; 
        this.plainImage = plainImage; 
        this.x_start = x_start;
        this.x_end = x_end;
        this.y_start = y_start;
        this.y_end = y_end;
        this.interacoes = interacoes;
        this.posicaoFractal = posicaoFractal;
        this.zoom = zoom;
        this.MEIO_X = MEIO_X;
        this.MEIO_Y = MEIO_Y;
        this.resultImage = null;
    }

    public void run() {
        try {
            this.resultImage = this.server.calculateMandelBrotFractalImage(this.plainImage, this.x_start, this.x_end, this.y_start, this.y_end, this.interacoes, this.posicaoFractal, this.zoom, this.MEIO_X, this.MEIO_Y);
        } catch (Exception e) {
            System.out.println("Exception caught while processing 1/4 of the image in server"+this.threadNum+": "+e);
            System.exit(-1);
        }

    }

    public Image getResultImage(){
        return this.resultImage;
    }
}
