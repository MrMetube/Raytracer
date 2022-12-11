package raytracer.geometry;

import math.MUtils;
import math.Point;
import math.Ray;
import math.Vector;
import raytracer.Payload;

public class AABBox extends Geometry{

    private Point min;
    private Point max;

    public AABBox(Point min, Point max, String material) {
        this.min = min;
        this.max = max;
        this.m = material;
    }
    public AABBox(Point min, Point max) {
        this.min = min;
        this.max = max;
        this.m = NO_MATERIAL;
    }

    @Override
    public boolean intersect(Payload payload) {
        Ray ray = payload.ray();
        Vector invdir = ray.dir();
        double t, tmin, tmax, tymin, tymax, tzmin, tzmax;

        if(invdir.x() >= 0){
            tmin = (min.x() - ray.origin().x()) / invdir.x();
            tmax = (max.x() - ray.origin().x()) / invdir.x();
        }else{
            tmin = (max.x() - ray.origin().x()) / invdir.x();
            tmax = (min.x() - ray.origin().x()) / invdir.x();
        }
        if(invdir.y() >= 0){
            tymin = (min.y() - ray.origin().y()) / invdir.y();
            tymax = (max.y() - ray.origin().y()) / invdir.y();
        }else{
            tymin = (max.y() - ray.origin().y()) / invdir.y();
            tymax = (min.y() - ray.origin().y()) / invdir.y();
        }

        if(tmin > tymax || tymin > tmax) return false;
        if(tymin > tmin) tmin = tymin;
        if(tymax < tmax) tmax = tymax;

        if(invdir.z() >= 0){
            tzmin = (min.z() - ray.origin().z()) / invdir.z();
            tzmax = (max.z() - ray.origin().z()) / invdir.z();
        }else{
            tzmin = (max.z() - ray.origin().z()) / invdir.z();
            tzmax = (min.z() - ray.origin().z()) / invdir.z();
        }

        if(tmin > tzmax || tzmin > tmax) return false;
        
        if(tzmin > tmin) tmin = tzmin;
        if(tzmax < tmax) tmax = tzmax;
        
        t = Math.min(tmin, tmax);
        if(t<0) return false;
        payload.hit(this, t);
        return true;
    }

    @Override
    public Vector normal(Point hit) {
        // Is this always correct??
             if(MUtils.approxEqual(hit.x(), min.x(), 0.0001)) return Vector.Xneg;
        else if(MUtils.approxEqual(hit.x(), max.x(), 0.0001)) return Vector.Xpos;
        else if(MUtils.approxEqual(hit.y(), min.y(), 0.0001)) return Vector.Yneg;
        else if(MUtils.approxEqual(hit.y(), max.y(), 0.0001)) return Vector.Ypos;
        else if(MUtils.approxEqual(hit.z(), min.z(), 0.0001)) return Vector.Zneg;
        else if(MUtils.approxEqual(hit.z(), max.z(), 0.0001)) return Vector.Zpos;
        return Vector.Yneg;
    }
    
}