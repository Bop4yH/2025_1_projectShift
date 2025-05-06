package ru.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.config.Config;
import ru.shift.config.ProducerConsumerConfig;
import ru.shift.res.ResourceStorage;
import ru.shift.threads.Consumer;
import ru.shift.threads.Producer;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Main {
    public static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Program start");
        try {
            Path configPath = Path.of("task5", "config.properties");
            ProducerConsumerConfig config = Config.loadConfig(configPath)
                    .orElseThrow(() -> new IllegalStateException("Can't load config"));

            startThreads(config);

        }   catch (InvalidPathException|IllegalStateException e) {
            System.exit(0);
        }

        log.info("Program end");
    }

    public static void startThreads(ProducerConsumerConfig config){
        int producerCount = config.producerCount();
        int consumerCount = config.consumerCount();
        int producerTime = config.producerTime();
        int consumerTime = config.consumerTime();
        int storageSize = config.storageSize();

        ResourceStorage resourceStorage = new ResourceStorage(storageSize);

        for (int i = 0; i < producerCount; i++){
            Producer producer = new Producer(producerTime,resourceStorage);
            Thread thread = new Thread(producer);
            thread.setName("Producer-" + producer.getId());
            thread.start();
        }

        for (int i = 0; i < consumerCount; i++){
            Consumer consumer = new Consumer(consumerTime,resourceStorage);
            Thread thread = new Thread(consumer);
            thread.setName("Consumer-" + consumer.getId());
            thread.start();
        }
    }
}
