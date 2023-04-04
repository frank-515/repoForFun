package cg;

public class Tuple <T1, T2, T3, T4> {
    public T1 first;
    public T2 second;
    public T3 third;
    public T4 fourth;
    public Tuple(T1 first, T2 second, T3 third, T4 fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
}
