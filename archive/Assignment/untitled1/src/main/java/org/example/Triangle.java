package org.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Triangle {
    private ArrayList<Float> side;
    public Triangle(float[] sides) throws Exception {
        side = new ArrayList<Float>();
        for (var i : sides) {
            side.add(i);
        }

        Collections.sort(side);
        for (var i : side) {
            if (i <= 0) {
                throw new Exception();
            }
        }
        if (side.get(0) + side.get(1) <= side.get(2)) {
            throw new Exception();
        }
        if (side.size() != 3) {
            throw new IllegalArgumentException();
        }
    }

    public String getTriangleName() {
        if (Objects.equals(side.get(0), side.get(1)) && Objects.equals(side.get(1), side.get(2))) {
            return "equilateral";
        }
        if (Objects.equals(side.get(0), side.get(1)) || Objects.equals(side.get(1), side.get(2))) {
            return "isosceles";
        }
        if (side.get(0) * side.get(0) + side.get(1) * side.get(1) == side.get(2) * side.get(2)) {
            return "right";
        }
        return "normal";
    }

}
