package scene;

import math.Color;
import math.Point;

public class PointLightSource extends LightSource{
    Point pos;
    Color color;
    double intensity;

    public PointLightSource(Point pos, Color color, double intensity){
        this.pos = pos;
        this.color = color;
        this.intensity = intensity;
    }
}
