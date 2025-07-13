package com.example.taco_cloud_authorization_server.app.common;

public enum Urls {
    CLIENT_HOST("http://localhost:8081"),
    CODE("code"),
    CONSENT("consent"),
    OAUTH2("oauth2"),
    REGISTER("register"),
    LOGIN("login");
    
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
