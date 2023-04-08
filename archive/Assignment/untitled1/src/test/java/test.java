import org.example.Calculator;
import org.junit.*;
import static org.junit.Assert.*;
public class test {
    Calculator calculator;
    @Before
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void testAdd() {
        assertEquals(2, calculator.add(1, 1));
    }
    @Test
    public void testSubs() {
        assertEquals(0, calculator.sub(1, 1));
    }
    @Test
    public void testMul() {
        assertEquals(1, calculator.mul(1, 1));
    }
    @Test
    public void testDiv() {
        assertEquals(1, calculator.divide(1, 1));
    }
    @Test(expected = java.lang.ArithmeticException.class)
    public void testDivByZero() {
        calculator.divide(1,0);
    }


}

