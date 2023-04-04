public class Inherit {
    public static void main(String[] args) throws Exception {
        Circle O1 = new Circle(new Point(1.0, 2.0), 4.0);
        System.out.println(O1.getArea());
    }
}

class Point {
    private Double x, y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point() {
        this.x = 0.0;
        this.y = 0.0;
    }
    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
    }
    public void setX(double x) {
        this.x = x;
    }
    void setY(double y) {
        this.y = y;
    }
    Double getX() {
        return this.x;
    }
    Double getY() {
        return this.y;
    }
}   

class Circle extends Point { //显然这里更应该用has-a关系而不是is-a关系
    private Double radius;
    public Circle(Double x, Double y, Double radius) {
        super(x, y);
        this.radius = radius;
    }
    public Circle(Point center, Double radius) {
        super(center);
        this.radius = radius;
    }
    public Circle(Double radius) {
        super();
        this.radius = radius;
    }
    public Circle() {
        super();
        this.radius = 0.0;
    }
    public double getArea() {
        return Math.pow(radius, 2.0) * Math.PI;
    }
}