package raytracer.geometry;

import math.MUtils;
import math.Point;
import math.Ray;
import math.Vector;
import raytracer.Payload;

public class Sphere extends Geometry{
    final Point c;    //center
    final double r;   //radius
    
    public static Sphere ZERO = new Sphere(Point.ZERO, 1);

    public Sphere(Point p, double r) {
        this.c = p;
        this.r = r;
        this.m = NO_MATERIAL;
    }

    public Sphere(Point p, double r, String m) {
        this.c = p;
        this.r = r;
        this.m = m;
    }

    @Override public boolean intersect(Ray ray,Payload payload){
        Vector dir = ray.dir();
        Vector L = ray.origin().sub(c);
        double a = dir.dot(dir);
        double b = 2 * dir.dot(L);
        double c = L.dot(L) - r*r;
        return MUtils.solveQuadratic(a,b,c,payload,this);
    }
    
    @Override public Vector normal(Point hit){
        return hit.sub(c).norm();
    }
}