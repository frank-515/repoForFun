package cg;

public class Sphere {
    public Vec3 center = new Vec3();
    public Float radius = 0.f;
    public Material material = new Material();
    Sphere(Vec3 center, Float radius, Material material){
        this.center = center;
        this.radius = radius;
        this.material = material;
    }
}
