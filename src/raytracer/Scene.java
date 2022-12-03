package raytracer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import math.Color;
import math.Point;
import raytracer.geometry.Geometry;
import raytracer.geometry.Sphere;
import raytracer.gson.GeometryAdapter;
import raytracer.gson.LightSourceAdapter;
import shader.Shader;

public class Scene {
    //#region Attributes
    HashSet<Geometry> geometries;
    HashSet<LightSource> lightSources;
    HashMap<String,Material> materials;
    Camera camera;

    static Gson gson = new GsonBuilder()
        .registerTypeAdapter(Geometry.class, new GeometryAdapter())
        .registerTypeAdapter(LightSource.class, new LightSourceAdapter())
        .create();

    //#endregion
    
    //#region Constructor

    public Scene(Camera camera) {
        this.geometries = new HashSet<>();
        this.lightSources = new HashSet<>();
        this.materials = new HashMap<String,Material>();
        this.materials.put(Geometry.NO_MATERIAL,Material.DEFAULT);
        this.camera = camera;
    }

    public Scene(String path){
        try {
            FileReader fR = new FileReader(new File(path));
            Scene s = gson.fromJson(fR, Scene.class);
            this.geometries = s.getGeometries();
            this.lightSources = s.getLightSources();
            this.materials = s.getMaterials();
            this.camera = s.getCamera();
        } catch (Exception e) {}
    }

    //#endregion

    public void renderImage(Shader shader, BufferedImage image){
        Camera camera = getCamera();
        int totalWidth = camera.width();
        int totalHeight = camera.height();


        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService exe =  Executors.newFixedThreadPool(threadCount);
        int width  = (totalWidth / threadCount)+1;
        int height = (totalHeight / threadCount)+1;
        for (int i = 0; i < threadCount; i++) for (int j = 0; j < threadCount; j++) 
            exe.submit(new Trace(this, image, i*width, j*height, width, height, shader));
        exe.shutdown();
        while(!exe.isTerminated());
        
    }

    public void toJson(String name){
        try {
            FileWriter fw = new FileWriter("./scenes/"+name+".json");
            fw.write(gson.toJson(this));
            fw.close();
        } catch (Exception e) {}
    }
    
    //#region Other

    public static Scene EMPTY = new Scene(new Camera(new Point(0, 0, -10), Point.ZERO, 90, 800, 800));

    public static Scene randomSpheres(int count){
        Scene s = new Scene(new Camera(new Point(0, 0, -12), Point.ZERO, 90, 800, 800));
        s.addBasicMaterials();
        s.addLightSource(new PointLightSource(new Point(0, 10, 10), Color.WHITE, 1));
        s.addLightSource(new PointLightSource(new Point(0, 10, -10), Color.PINK, 1));
        Object[] materials = s.getMaterials().keySet().toArray();
        Random rdm = new Random();
        for (int i = 0; i < count; i++) {
            double x = rdm.nextDouble(-10,10);
            double y = rdm.nextDouble(-10,10);
            double z = rdm.nextDouble(-10,10);
            double r = rdm.nextDouble(0.1,1);
            int m = rdm.nextInt(0,materials.length);
            if(materials[m] instanceof String)
                s.addGeometry(new Sphere(new Point(x,y,z), r, (String)materials[m]));
        }
        return s;
    }
    
    public void addGeometry(Geometry g){ 
        if(!materials.containsKey(g.material())){
            g.setMaterial(Geometry.NO_MATERIAL);
        }
        geometries.add(g);
    }
    public void addLightSource(LightSource l){ lightSources.add(l); }
    public void addMaterial(String key, Material m){ materials.put(key,m); } 
    public HashSet<Geometry>        getGeometries()   { return geometries; }
    public HashMap<String,Material> getMaterials()    { return materials; }
    public HashSet<LightSource>     getLightSources() { return lightSources; }
    public Camera                   getCamera()       { return camera; }

    public void addBasicMaterials(){
        addMaterial("red",       Material.RED);
        addMaterial("green",     Material.GREEN);
        addMaterial("blue",      Material.BLUE);
        addMaterial("cyan",      Material.CYAN);
        addMaterial("magenta",   Material.MAGENTA);
        addMaterial("yellow",    Material.YELLOW);
        addMaterial("white",     Material.WHITE);
        addMaterial("gray",      Material.GRAY);
        addMaterial("black",     Material.BLACK);
        
        addMaterial("pink",      Material.PINK   );
        addMaterial("orange",    Material.ORANGE );
        addMaterial("lemon",     Material.LEMON    );
        addMaterial("lime",      Material.LIME   );
        addMaterial("turqouise", Material.TURQOUISE  );
        addMaterial("purple",    Material.PURPLE );
        addMaterial("dark",      Material.DARK   );
        addMaterial("light",     Material.LIGHT  );
        
        addMaterial("gold",      Material.GOLD  );
        addMaterial("silver",    Material.SILVER  );
        addMaterial("bronze",    Material.BRONZE  );
        addMaterial("copper",    Material.COPPER  );

    }
    //#endregion
}
