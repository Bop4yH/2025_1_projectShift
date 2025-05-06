package ru.shift.res;

public class Resource {
    private static int idCounter = 0;
    private final int id;

    private static final Object COUNTER = new Object();
    public Resource() {
        synchronized (COUNTER) {
            this.id = idCounter++;
        }
    }

    public int getId() {
        return id;
    }
}
