package geometry;

import math.MUtils;
import math.Point;
import math.Ray;
import math.Vector;
import scene.Material;

public class Sphere extends Geometry{
    final Point c;    //center
    final double r;   //radius
    final Material m;
    
    public Sphere(Point p, double r) {
        this.c = p;
        this.r = r;
        this.m = standardMaterial;
    }

    public Sphere(Point p, double r, Material m) {
        this.c = p;
        this.r = r;
        this.m = m;
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
        return hit.sub(c).norm();
    }
}
