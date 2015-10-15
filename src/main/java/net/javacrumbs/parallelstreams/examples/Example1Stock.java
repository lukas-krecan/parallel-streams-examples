/**
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.parallelstreams.examples;

import net.javacrumbs.common.StockInfo;

import java.util.Comparator;
import java.util.List;

import static java.lang.Math.abs;
import static java.util.Arrays.asList;
import static net.javacrumbs.common.Utils.log;
import static net.javacrumbs.common.Utils.measure;
import static net.javacrumbs.common.Utils.sleep;

public class Example1Stock {

    private static final List<String> SYMBOLS = asList(
            "AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE",
            "AMZN", "CRAY", "CSCO", "DELL", "GOOG", "INTC", "INTU",
            "MSFT", "ORCL", "TIBX", "VRSN", "YHOO");

    public static void main(String[] args) {
        new Example1Stock().doRun(SYMBOLS);
    }

    private void doRun(List<String> symbols) {
        measure(() ->
                symbols.stream()
                        .parallel()
                        .map(this::getStockInfo)
                        .max(Comparator.comparingDouble(StockInfo::getPrice))
                        .ifPresent(System.out::println)
        );
    }

    public StockInfo getStockInfo(String symbol) {
        return new StockInfo(symbol, calculatePrice(symbol));
    }

    // Simulating long network task
    private double calculatePrice(String symbol) {
        log("Getting price for symbol " + symbol);
        sleep(100);
        return abs(symbol.hashCode()) % 1000.0;
    }
}
