package ru.shift.threads;


import ru.shift.res.Resource;
import ru.shift.res.ResourceStorage;

import static java.lang.Thread.sleep;
import static ru.shift.Main.log;

public class Producer implements Runnable {
    private static final int DEFAULT_WORK_NUM = 10;
    private static int idCounter = 0;
    private final int id;
    private final int producerTime;
    private final ResourceStorage resourceStorage;

    public Producer(int producerTime, ResourceStorage storage) {
        this.producerTime = producerTime;
        this.id = idCounter++;
        this.resourceStorage = storage;
    }


    @Override
    public void run() {
        for (int i = 0; i < DEFAULT_WORK_NUM; i++) {
            try {
                sleep(producerTime);
                Resource madeResource = new Resource();

                log.debug("made {}_id res", madeResource.getId());

                resourceStorage.addResource(madeResource);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public int getId() {
        return id;
    }
}
