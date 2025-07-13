package com.example.taco_cloud.app.common;

public enum Urls {
    CLIENT_HOST("http://localhost:8081"),
    AUTH_SERV_HOST("http://localhost:8082"),
    RES_SERV_HOST("http://localhost:8083"),
    HOME("home"),
    DESIGN("design"),
    ORDERS("orders"),
    CURRENT("current"),
    REGISTER("register"),
    LOGIN("login"),
    INGREDIENTS("ingredients"),
    API("api"),
    OAUTH2("oauth2/authorization");

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
