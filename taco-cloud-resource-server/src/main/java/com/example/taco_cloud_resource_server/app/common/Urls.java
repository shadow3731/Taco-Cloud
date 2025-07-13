package com.example.taco_cloud_resource_server.app.common;

public enum Urls {
    CLIENT_HOST("http://localhost:8081");

    private final String value;

    Urls(String value) {
        this.value = value;
    }

    public String get() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.get();
    }
}
