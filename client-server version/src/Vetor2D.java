import java.io.Serializable;

public class Vetor2D implements Serializable {
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
