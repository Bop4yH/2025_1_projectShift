package ru.shift.threads;

import ru.shift.res.Resource;
import ru.shift.res.ResourceStorage;

import static java.lang.Thread.sleep;
import static ru.shift.Main.log;

public class Consumer implements Runnable {
    private static final int DEFAULT_WORK_NUM = 10;
    private static int idCounter = 0;
    private final int id;
    private final int consumerTime;
    private final ResourceStorage resourceStorage;

    public Consumer(int consumerTime, ResourceStorage storage) {
        this.consumerTime = consumerTime;
        this.id = idCounter++;
        this.resourceStorage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < DEFAULT_WORK_NUM; i++) {
            try {
                sleep(consumerTime);
                Resource consumedResource = resourceStorage.getResource();

                int resId = consumedResource.getId();
                log.debug("consumed {}_id res", resId);

            } catch (InterruptedException ignore) {
            }
        }

    }

    public int getId() {
        return id;
    }
}
