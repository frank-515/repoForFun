package cg;
import java.util.*;
import java.io.*;
public class tinyraytracer {
    int WIDTH = 1280;
    int HEIGHT = 720;
    ArrayList<Vec3> background;
    //硬编码材质
    final static Float[] ivoryAlbedo = {0.9f, 0.5f, 0.1f, 0.0f};
    final static Float[] glassAlbedo = {0.0f, 0.9f, 0.1f, 0.8f};
    final static Float[] redRubberAlbedo = {1.4f, 0.3f, 0.0f, 0.0f};
    final static Float[] mirrorAlbedo = {0.0f, 16.0f, 0.8f, 0.0f};

    final Material ivory = new Material(1.f, ivoryAlbedo, 50.f, new Vec3(0.4f, 0.4f, 0.3f));
    final Material glass = new Material(1.5f, glassAlbedo, 125.f, new Vec3(0.6f, 0.7f, 0.8f));
    final Material redRubber = new Material(1.f, redRubberAlbedo, 10.f, new Vec3(0.3f, 0.1f, 0.1f));
    final Material mirror = new Material(1.f, mirrorAlbedo, 1425.f, new Vec3(1.f, 1.f, 1.f));
    //四个球体
    Sphere[] spheres = {
        new Sphere(new Vec3(-3.f, 0.f, -16.f), 2.f, ivory),
        new Sphere(new Vec3(-1.f, -1.5f, -12f), 2.f, glass),
        new Sphere(new Vec3(1.5f, -0.5f, -18f), 3.f, redRubber),
        new Sphere(new Vec3(7.f, 5.f, -18f), 4.f, mirror)
    };
    //三束光线
    Vec3[] lights = { 
        new Vec3(-20.f, 20.f, 20.f), 
        new Vec3(30f, 50f, -25f), 
        new Vec3(30f, 20f, 30f)
    };
    //反射 - 入射光与法线
    public static Vec3 reflect(final Vec3 I, final Vec3 N) {
        return new Vec3(I.subs(N.dot(2.f).dot(I.dot(N))));
    }
    //折射
    public static Vec3 refract(final Vec3 I, final Vec3 N, final Float eta_t, final Float eta_i){// Snell's law , eta_i 具有默认值1.f
        Float cosi = -Math.max(-1.f, Math.min(1.f, I.dot(N)));
        if (cosi < 0) return refract(I, N.dot(-1.f), eta_i, eta_t);// if the ray comes from the inside the object, swap the air and the media
        Float eta = eta_i / eta_t;
        Float k = 1 - eta * eta * (1 - cosi * cosi);
        return k < 0 ? new Vec3(1.f, 0.f, 0.f) : I.dot(eta).add(N.dot(eta * cosi - (float) Math.sqrt(k))); // k<0 = total reflection, no ray to refract. I refract it anyways, this has no physical meaning
    }
    public static Tuple<Boolean, Float, Float, Float>raySphereIntersect(final Vec3 orig, final Vec3 dir, final Sphere s){ // ret value is a pair [intersection found, distance]
        Vec3 L = s.center.subs(orig);
        Float tca = L.dot(dir);
        Float d2 = L.dot(L) - tca * tca;
        if (d2 > s.radius * s.radius) return new Tuple<Boolean, Float, Float, Float>(false, 0.f, 0.f, 0.f);
        Float thc = (float) Math.sqrt(s.radius * s.radius - d2);
        Float t0 = tca - thc, t1 = tca + thc;
        if (t0 > .001f) return new Tuple<Boolean, Float, Float, Float>(true, t0, 0.f, 0.f); // offset the original point by .001 to avoid occlusion by the object itself
        if (t1 > .001f) return new Tuple<Boolean, Float, Float, Float>(true, t1, 0.f, 0.f);
        return new Tuple<Boolean, Float, Float, Float>(false, 0.f, 0.f, 0.f);
    }

    public Tuple<Boolean, Vec3, Vec3, Material> sceneIntersect(final Vec3 orig, final Vec3 dir){
        Vec3 pt = new Vec3(), N = new Vec3();
        Material material = new Material();
        Float nearestDist = 1e10f;
        if (Math.abs(dir.y) > .001) {// intersect the ray with the checkerboard, avoid division by zero
            Float d = -(orig.y + 4) / dir.y;// the checkerboard plane has equation y = -4
            Vec3 p = orig.add(dir.dot(d));
            if (d > .001f && d < nearestDist && Math.abs(p.x) < 10 && p.z < -10 && p.z > -30){
                nearestDist = d;
                pt = new Vec3(p); 
                N = new Vec3(0.f, 1.f, 0.f); //地狱代码
                material.diffuse = (((int)(.5 * pt.x + 1000) + (int) (.5 * pt.z)) == 0 ? 2 : ((int)(.5 * pt.x + 1000) + (int) (.5 * pt.z))) % 2 == 0 ? new Vec3(.3f, .3f, .3f) : new Vec3(.3f, .2f, .1f);
            }
        }
        //这里导致这个方法不能static
        for (Sphere s : spheres){ // intersect the ray with all spheres
            Tuple<Boolean, Float, Float, Float> T = raySphereIntersect(orig, dir, s);
            Boolean intersection = T.first;
            Float d = T.second;
            if (!intersection || d > nearestDist) continue;
            nearestDist = d;
            pt = orig.add(dir.dot(nearestDist));
            N = (pt.subs(s.center)).normalize();
            material = s.material;
        }
        return new Tuple<Boolean, Vec3, Vec3, Material>(nearestDist < 1000, pt, N, material);
    }
    //这里depth应该具有0的初始值
    public Vec3 castRay(final Vec3 orig, final Vec3 dir, final Integer depth) {
        Tuple<Boolean, Vec3, Vec3, Material> T = sceneIntersect(orig, dir);
        Boolean hit = T.first;
        Vec3 point = T.second;
        Vec3 N = T.third;
        Material material = T.fourth;
        if (depth > 4 || !hit) return new Vec3(.2f, .7f, .8f); //background color
        // if (depth > 4 || !hit) {
        //     Float index = WIDTH * (HEIGHT / 2.f - dir.y - 0.5f) + dir.x + WIDTH / 2.f - 0.5f;
        //     return new Vec3(background.get( Math.round(index)));
        // }
        Vec3 reflectDir = reflect(dir, N).normalize();
        Vec3 refractDir = refract(dir, N, material.refractiveIndex, 1.f).normalize();
        Vec3 reflectColor = castRay(point, reflectDir, depth + 1);
        Vec3 refractColor = castRay(point, refractDir, depth + 1);

        Float diffuseLightIntensity = 0.f, specularLightIntensity = 0.f;
        for (Vec3 light : lights){ // checking if the point lies in the shadow of the light
            Vec3 lightDir = new Vec3(light.subs(point)).normalize();
            Tuple<Boolean, Vec3, Vec3, Material> F = sceneIntersect(point, lightDir);
            Boolean hitF = F.first;
            Vec3 shadowPt = F.second;
            if (hitF && (shadowPt.subs(point)).norm() < (light.subs(point)).norm()) continue;
            diffuseLightIntensity += Math.max(0.f, lightDir.dot(N));
            specularLightIntensity += (float) Math.pow(Math.max(0.f, -reflect(lightDir.dot(-1.f), N).dot(dir)), material.specular_exponent);
        } //希望能运行，同时感谢向量点乘具有交换律
        return new Vec3(material.diffuse.dot(diffuseLightIntensity).dot(material.albedo[0]).add(new Vec3(1.f, 1.f, 1.f).dot(specularLightIntensity).dot(material.albedo[1])).add(reflectColor.dot(material.albedo[2])).add(refractColor.dot(material.albedo[3])));
    }
    public void MAIN() throws Exception {
        int width = 1280;
        int height = 720;
        Float fov = 1.05f; // 60 degrees field of view in radians
        ArrayList<Vec3> framebuffer = new ArrayList<Vec3>(height * height);
        //这里应该要并行 :#pragma omp parallel for
        for (int pix = 0; pix < width * height; pix++) {// actual rendering loop
            Float dirX = (pix % width + .5f) - width / 2.f;
            Float dirY = -(pix / width + .5f) + height / 2.f; // this flips the image at the same time
            Float dirZ = -height/(2.f * (float) Math.tan(fov / 2.f));
            framebuffer.add(castRay(new Vec3(0.f, 0.f, 0.f), new Vec3(dirX, dirY, dirZ).normalize(), 0));
        }
        //OutputStream ofs = new FileOutputStream("./out.ppm");
        //OutputStreamWriter writer = new OutputStreamWriter(ofs, "UTF-8");
        //writer.append(("P3\n" + width + " " + height + "\n255\n"));
        System.out.print("P3\n" + width + " " + height + "\n255\n");
        for (int i = 0; i < framebuffer.size(); i++){
            var color = framebuffer.get(i);
            //if (((i == 0) ? 1 : i ) % width == 0) writer.append('\n');
            Float max = Math.max(1.f, Math.max(color.x, Math.max(color.y, color.z)));
            for (int chan = 0; chan < 3; chan++){
                //writer.append(((Integer) (int) (255 * (chan == 0 ? color.x : (chan == 1 ? color.y : color.z)) / max)).toString() + " ");
                System.out.print(((Integer) (int) (255 * (chan == 0 ? color.x : (chan == 1 ? color.y : color.z)) / max)).toString() + " ");
            }
        }
        //ofs.flush();
        //ofs.close();
    }
    public static ArrayList<Vec3> loadPPM(final String filename) throws Exception {
        InputStream ifs = new FileInputStream(new File(filename));
        InputStreamReader reader = new InputStreamReader(ifs, "UTF-8");
        Scanner scanner = new Scanner(reader);
        String mode = "";
        mode = scanner.nextLine();
        if (!mode.equals("P3")) {
            System.out.println("Unable to open " + filename);
            scanner.close();
            return new ArrayList<Vec3>();
        }
        int width = scanner.nextInt(), height = scanner.nextInt(), maxValue = scanner.nextInt();
        //System.out.println(mode + " " + height + " " + width + " " + maxValue);
        ArrayList<Vec3> image = new ArrayList<Vec3>();
        for (int i = 0; i < width * height; i++){
            image.add(new Vec3(scanner.nextInt() / (float) maxValue, scanner.nextInt() / (float) maxValue, scanner.nextInt() / (float) maxValue));
        }
        scanner.close();
        return image;
    }
    public tinyraytracer() throws Exception{
        //background = loadPPM("./a.ppm");
    }
    public static void main(String[] args) throws Exception {
        tinyraytracer rt = new tinyraytracer();
        try {
            rt.MAIN();
        } catch (Exception e) {
            System.out.println("Unable to open out file.\n");
        }
        //var image = loadPPM("a.ppm");
        
    }

}    
