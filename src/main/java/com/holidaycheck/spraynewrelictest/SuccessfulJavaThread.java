package com.holidaycheck.spraynewrelictest;

import scala.concurrent.Promise;

public class SuccessfulJavaThread extends Thread {

    private Promise<String> promise;

    public SuccessfulJavaThread(Promise<String> promise) {
        this.promise = promise;
    }

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        promise.success("waited 1s in Java thread");
    }
}
