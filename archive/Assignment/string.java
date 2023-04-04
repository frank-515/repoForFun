public class string {
    public static void main(String[] args) {
        var s = "HelloWorld";
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            Character c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(Character.toUpperCase(c));
            }
        }
        System.out.println(sb.toString());
    }
}
