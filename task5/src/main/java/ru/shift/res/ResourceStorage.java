package ru.shift.res;

import java.util.LinkedList;
import java.util.Queue;

import static ru.shift.Main.log;

public class ResourceStorage {
    private final Queue<Resource> resources = new LinkedList<>();
    private final int maxSize;

    private int waitingConsumers = 0;

    public ResourceStorage(int storageSize) {
        this.maxSize = storageSize;
    }

    public void addResource(Resource resource) throws InterruptedException {
        synchronized (resources) {
            while (resources.size() >= maxSize) {
                log.debug("thread waits because storage is full");
                resources.wait();
            }

            while (waitingConsumers > 0 && !resources.isEmpty()) {
                int size = resources.size();
                log.debug("thread waits because storage has {} res and has {} consumers.", size, waitingConsumers);

                resources.wait();
            }

            resources.add(resource);
            int size = resources.size();
            log.debug("{} res at storage. Delivered now {}_id res.", size, resource.getId());

            resources.notifyAll();
        }
    }

    public void consumeResource() throws InterruptedException {
        synchronized (resources) {
            waitingConsumers++;

            try {
                while (resources.isEmpty()) {
                    log.debug("thread waits because storage is empty");
                    resources.wait();
                }

                Resource resource = resources.remove();

                int size = resources.size();
                log.debug("{} res at storage. Consumed now {}_id res.", size,resource.getId());

                resources.notifyAll();
            } finally {
                waitingConsumers--;
            }
        }
    }
}
