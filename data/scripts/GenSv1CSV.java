package scripts;

public class GenSv1CSV {
    public static void main(String[] args) {

        String out = "";
        for (int w = 1; w < 60; w++) {
            out += "m" + w + ",";
        }
        for (int w = 1; w < 60; w++) {
            out += "w" + w + ",";
        }
        for (int w = 1; w < 60; w++) {
            out += "c" + w + ",";
        }
        System.out.println(out);
    }
}
