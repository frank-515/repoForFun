package cg;
public class Vec3 {
    public Float x = 0.f, y = 0.f, z = 0.f;
    public Vec3() {x = 0.f; y = 0.f; z = 0.f;}
    public Vec3(Vec3 v) {x = v.x; y = v.y; z = v.z;}
    public Vec3(Float x, Float y, Float z) {this.x = x; this.y = y; this.z = z;}
    public Float get(int i) {return i == 0 ? x : (i == 1 ? y : z);};
    public void set(int i, Float v) {
        switch (i) { //怎么不用getter & setter表示 Float&
            case 0:
                this.x = v;
                break;
            case 1:
                this.y = v;
                break;
            case 2:
                this.z = v;
            default:
                throw new IllegalArgumentException("Invalid value for vec3");
        }
    };
    //重载运算符？
    public Vec3 dot(final Float v) {return new Vec3(this.x * v, this.y * v, this.z * v);}
    public Float dot(final Vec3 v) {return this.x * v.x + this.y * v.y + this.z * v.z;}
    public Vec3 add(final Vec3 v) {return new Vec3(v.x + this.x, v.y + this.y, v.z + this.z);}
    public Vec3 subs(final Vec3 v) {return new Vec3(this.x - v.x, this.y - v.y, this.z - v.z);}
    public Float norm() {return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);}
    public Vec3 normalize() {return this.dot(1.f / this.norm());} //应该可以省略this吧

    public static Vec3 cross(final Vec3 v1, final Vec3 v2) {
        return new Vec3(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
    }
}
