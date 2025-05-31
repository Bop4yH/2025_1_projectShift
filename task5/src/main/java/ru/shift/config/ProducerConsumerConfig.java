package ru.shift.config;

public record ProducerConsumerConfig(
        int producerCount,
        int consumerCount,
        int producerTime,
        int consumerTime,
        int storageSize
) {}
