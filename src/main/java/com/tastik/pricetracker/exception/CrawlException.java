package com.tastik.pricetracker.exception;

public class CrawlException extends RuntimeException {

    public CrawlException(String message, Throwable cause) {
        super(message, cause);
    }
}
