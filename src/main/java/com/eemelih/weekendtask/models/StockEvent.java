package com.eemelih.weekendtask.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockEvent {
    private String symbol;
    private Integer price;
}
