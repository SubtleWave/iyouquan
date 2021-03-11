package template;

/**
 * a* 1
 * b* 0
 * c* 0
 * d* 1
 * tx* 0
 * ty* 0
 */
public class Transform {
    private double a =1;
    private double b =0;
    private double c =0;
    private double d =1;
    private double tx =0;
    private double ty =0;

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getTx() {
        return tx;
    }

    public void setTx(double tx) {
        this.tx = tx;
    }

    public double getTy() {
        return ty;
    }

    public void setTy(double ty) {
        this.ty = ty;
    }
}
