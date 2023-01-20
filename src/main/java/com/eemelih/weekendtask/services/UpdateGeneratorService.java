package com.eemelih.weekendtask.services;

import com.eemelih.weekendtask.models.StockEvent;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
public class UpdateGeneratorService {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private ScheduledFuture<?> taskState;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());

    public ArrayList<StockEvent> generateEvents(Integer batchSize) {
        ArrayList<StockEvent> listOfUpdates = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            listOfUpdates.add(new StockEvent(
                    fakeValuesService.letterify("????", true),
                    Integer.valueOf(fakeValuesService.numerify("####"))
                    )
            );
        }

        return listOfUpdates;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        startGeneration(100);
    }

    @PreDestroy
    public void onShutdown() {
        scheduler.shutdown();
    }

    public void startGeneration(Integer updateFrequencyInMs) {
        taskState = scheduler.scheduleAtFixedRate(() -> {
            simpMessagingTemplate.convertAndSend(
                    "/topic/message",
                    generateEvents(50)
            );
        }, 0, updateFrequencyInMs, MILLISECONDS);
    }

    public void stopGeneration() {
        taskState.cancel(true);
    }
}
