package cg;
//用class来描述结构体时把数据都放在public这样好吗
public class Material {
    public Float refractiveIndex = 1.f;
    public Float[] albedo = {2.f, 0.f, 0.f, 0.f}; //
    public Vec3 diffuse = new Vec3(0.f, 0.f, 0.f);
    public Float specular_exponent = 0.f;
    Material(Float refractiveIndex, Float[] albedo, Float specular_exponent, Vec3 diffuse){
        this.refractiveIndex = refractiveIndex;
        this.albedo = albedo;
        this.specular_exponent = specular_exponent;
        this.diffuse = diffuse;
    }
    Material() {}
}
