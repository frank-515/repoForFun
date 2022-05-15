public class Abstract {
    public static void main(String[] args) {
        var s1 = new Point(1.0, 2.0);
        var s2 = new Circle(1.0, 2.0, 3.0);
        var s3 = new Cylinder(1.0, 2.0, 3.0, 4.0);
        s1.printInfo();
        System.out.println();
        s2.printInfo();
        System.out.println();
        s3.printInfo();
        System.out.println();
    }
}

abstract class Shape {
    abstract public double area();
    abstract public double volume();
    abstract public void printInfo();
}

class Point extends Shape {
    protected double x, y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point() {
        this.x = 0.0;
        this.y = 0.0;
    }
    @Override
    public double area() {
        return 0.0;
    }
    @Override
    public double volume() {
        return 0.0;
    }
    @Override
    public void printInfo() {
        System.out.printf("Point at (%f, %f), Area: %f, Volume: %f", this.x, this.y, area(), volume());
    }
}



class Circle extends Point {
    protected double radius;
    public Circle(double x, double y, double radius) {
        super(x, y);
        this.radius = radius;
    }
    @Override
    public double area() {
        return Math.pow(this.radius, 2.0) * Math.PI;
    }
    @Override
    public double volume() {
        return 0.0;
    }
    @Override
    public void printInfo() {
        System.out.printf("Point at (%f, %f), Area: %f, Volume: %f", this.x, this.y, area(), volume());
    }
}

class Cylinder extends Circle {
    private double height;
    public Cylinder(double x, double y, double radius, double height) {
        super(x, y, radius);
        this.height = height;
    }
    @Override
    public double area() {
        return Math.pow(super.radius, 2.0) * Math.PI;
    }
    @Override
    public double volume() {
        return area() * height;
    }
    @Override
    public void printInfo() {
        System.out.printf("Point at (%f, %f), Area: Unavailable, Volume: %f", this.x, this.y, volume());
    }
}