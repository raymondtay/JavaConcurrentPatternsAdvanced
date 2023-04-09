package sg.airlab;

// What is happening if two threads are calling `getInstance()` ?
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton instance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
