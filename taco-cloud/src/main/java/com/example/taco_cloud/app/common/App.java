package com.example.taco_cloud.app.common;

public enum App {
    AUTH_CLIENT("client2");

    private String value;

    App(String value) {
        this.value = value;
    }

    public String get() {
        return this.value;
    }
}
