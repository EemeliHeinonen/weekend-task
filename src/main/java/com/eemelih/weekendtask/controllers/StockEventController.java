package com.eemelih.weekendtask.controllers;

import com.eemelih.weekendtask.models.StartGenerationMessage;
import com.eemelih.weekendtask.services.UpdateGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StockEventController {
    @Autowired
    UpdateGeneratorService updateGeneratorService;

    @MessageMapping("/update-frequency")
    public void start(StartGenerationMessage message) {
        updateGeneratorService.stopGeneration();
        updateGeneratorService.startGeneration(message.getUpdateFrequencyInMs());
    }
}