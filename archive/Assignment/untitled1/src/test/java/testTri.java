import org.example.Triangle;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class testTri {
    Triangle triangle;

    @Test(expected = Exception.class)
    public void negativeSide() throws Exception {
        triangle = new Triangle(new float[]{-1, -1, -1});
    }

    @Test(expected = Exception.class)
    public void sideUnbreakable() throws Exception {
        triangle = new Triangle(new float[]{1, 1, 999});
    }
    @Test
    public void equilateral() throws Exception {
        triangle = new Triangle(new float[]{1, 1, 1});
        assertEquals(triangle.getTriangleName(), "equilateral");
    }

    @Test
    public void right() throws Exception {
        triangle = new Triangle(new float[]{3, 4, 5});
        assertEquals(triangle.getTriangleName(), "right");
    }

    @Test
    public void isosceles() throws Exception {
        triangle = new Triangle(new float[]{1.2f, 1, 1});
        assertEquals(triangle.getTriangleName(), "isosceles");
    }

    @Test
    public void normal() throws Exception {
        triangle = new Triangle(new float[]{1.2f, 1.3f, 1});
        assertEquals(triangle.getTriangleName(), "normal");
    }

}

