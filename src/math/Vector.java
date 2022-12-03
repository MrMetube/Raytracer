package math;
public class Vector extends Tuple{

    public static Vector Ypos    = new Vector(0,1,0);
    public static Vector Yneg  = new Vector(0,-1,0);
    public static Vector Xpos = new Vector(1,0,0);
    public static Vector Xneg  = new Vector(-1,0,0);
    public static Vector Zpos  = new Vector(0,0,1);
    public static Vector Zneg  = new Vector(0,0,-1);
    public static Vector ZERO  = new Vector(0,0,0);

    public Vector(double x, double y, double z){ super(x,y,z,0); }

    public Vector add(double a){
        return new Vector(x+a, y+a, z+a);
    }

    public Vector add(Vector... vecs){
        Vector sum = new Vector(x,y,z);
        for (Vector v : vecs) sum = new Vector(
            sum.x()+v.x(), 
            sum.y()+v.y(), 
            sum.z()+v.z()
        );
        return sum;
    }

    public Vector sub(Vector... vecs){
        Vector dif = new Vector(x,y,z);
        for (Vector v : vecs) dif = new Vector(
            dif.x()-v.x(), 
            dif.y()-v.y(), 
            dif.z()-v.z()
        );
        return dif;
    }

    public Vector cross(Vector t) { return new Vector( y*t.z() - z*t.y(), z*t.x() - x*t.z(), x*t.y() - y*t.x()); }
    public Vector mul(double scl){ return new Vector(x*scl, y*scl, z*scl);}
    public Vector div(double scl){ return new Vector(x/scl, y/scl, z/scl);}
    public Vector neg(){ return new Vector(0, 0, 0).sub(this);}

    public Vector norm() {
        double mag = this.mag();
        return new Vector(x/mag, y/mag, z/mag);
    }

    public Vector refl(Vector normal){
        Vector n = normal.norm();
        return this.sub(n.mul(2*this.dot(n)));
    }

    @Override
    public String toString(){ return String.format("(%.02f | %.02f | %.02f)",x,y,z); }
}
