package cometsim;

public class XYZCoordinates extends OrbitalElements {

    double x, y, z, vx, vy, vz;

    public XYZCoordinates(double x, double y, double z, double vx, double vy, double vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public double[] coordinates(double julian) {
        double[] c = new double[3];
        c[0] = x+vx*julian;
        c[1] = y+vy*julian;
        c[2] = z+vz*julian;
        return c;
    }

    public double[] getIVs() {
        double[] ivs = {x, y, z, vx, vy, vz};
        return ivs;
    }
}
