import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;

import geometry.*;
import math.*;
import shader.*;

class App {
    public static void main(String[] args) throws IOException{
        int size = 512;
        
        HashSet<Geometry> geometries = new HashSet<>();
        geometries.add(new Sphere(new Point(0, 0, 150), 150));

        geometries.add(new Plane(new Vector(0, 100, 0), new Point(0, -200, 100)));

        geometries.add(new Sphere(new Point( 200, 000, 150), 100));
        geometries.add(new Sphere(new Point(-200, 000, 150), 100));
        geometries.add(new Sphere(new Point( 000,-200, 150), 100));

        geometries.add(new Plane(new Vector( 90,-45, 90), new Point( 100,-100, 150), 120));
        geometries.add(new Plane(new Vector(-90,-45, 90), new Point(-100,-100, 150), 120));

        Camera camera = new Camera(new Point(0, 0, -100), new Point(0, 0, 0), 110, size, size);

        // makeImage(size, new ScreenPixelShader());
        // makeImage(size, new ScreenDistanceShader());
        // makeImage(size, new ScreenNormalShader());

        makeImage(camera, new DistanceShader(),  geometries);
        makeImage(camera, new IntersectShader(), geometries);
        makeImage(camera, new NormalShader(),    geometries);
    }

    static void makeImage(Camera camera, Shader shader, HashSet<Geometry> geometries){
        int width = camera.getWidth();
        int height = camera.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Color def = new Color(113, 216, 237); // default color
        
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) {
            //setup
            Color  color = null;
            double z = Integer.MAX_VALUE;
            Ray ray = camera.generateRay(x, y);
            
            //use shader on geometries
            for(Geometry geometry : geometries){
                Color maybeColor = shader.getColor(ray, geometry);
                if(ray.t() < z){
                    color = maybeColor;
                    z = ray.t();
                }
            }
            if(color == null) color = def;
            // apply the color
            image.setRGB(x, height-y-1, color.rgb() );
        };

        writeImage(image, shader.getName());
    }

    static void makeImage(int size, ScreenShader shader){
        BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        Color def = new Color(41, 139, 95); // default color
        Color c = null;
        for (int x = 0; x < size; x++) for (int y = 0; y < size; y++) {
            c = shader.getColor(x, y, size);
            // apply the color
            if(c == null) c = def;
            image.setRGB(x, y, c.rgb());
        };

        writeImage(image, shader.getName());
    }

    static void writeImage(BufferedImage image, String name){
        File file = new File("./images/"+name+".png");
        try {
            ImageIO.write(image, "png", file);   
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}