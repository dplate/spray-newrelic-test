package com.holidaycheck.spraynewrelictest;

import scala.concurrent.Promise;

public class FailingJavaThread extends Thread {

    private Promise<String> promise;

    public FailingJavaThread(Promise<String> promise) {
        this.promise = promise;
    }

    public void run() {
        promise.failure(new Exception("Error with Java thread"));
    }
}
