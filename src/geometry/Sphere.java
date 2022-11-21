package geometry;

import math.MUtils;
import math.Point;
import math.Ray;
import math.Vector;

public class Sphere extends Geometry{
    final Point c;    //center
    final double r;   //radius
    
    public Sphere(Point p, double r) {
        this.c = p;
        this.r = r;
    }

    @Override
    public boolean intersect(Ray ray){
        Vector dir = ray.dir();
        
        Vector L = ray.origin().sub(c);
        double a = dir.dot(dir);
        double b = 2 * dir.dot(L);
        double c = L.dot(L) - r*r;
        if(!MUtils.solveQuadratic(a,b,c,ray)) return false;
        if(ray.t() < 0) return false;
        ray.hit(this);
        return true;
    }
    
    @Override
    public Vector normal(Point hit){
        return c.sub(hit);
    }
}