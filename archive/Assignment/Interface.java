public class Interface {
    public static void main(String[] args) {
        (new Test()).main();
    }
}

interface ComputeInterface {
    abstract public int computer(int n, int m);
}

class Adder implements ComputeInterface {
    public int computer(int n, int m) {
        return m + n;
    }
}

class Subtracter implements ComputeInterface {
    @Override
    public int computer(int n, int m) {
        return m - n;
    }
}

class Multiplier implements ComputeInterface {
    @Override
    public int computer(int n, int m) {
        return m * n;
    }
}
class Divider implements ComputeInterface {
    @Override
    public int computer(int n, int m) {
        return m / n;
    }
}

class UseCompute {
    public static void useCon(ComputeInterface op, int first, int second) {
        System.out.println(op.computer(first, second));
    }
}

class Test {
    public void main() {
        UseCompute.useCon(new Divider(), 1, 2);
        UseCompute.useCon(new Adder(), 1, 2);
        UseCompute.useCon(new Subtracter(), 2, 1);
        UseCompute.useCon(new Multiplier(), 1, 2);
    }
}