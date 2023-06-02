package com.tqb.newsreader.backend;

public class AsyncParam {
    public static Controller controller;
    public static String type;

    public AsyncParam(Controller _controller, String _type) {
        controller = _controller;
        type = _type;
    }

    public static Controller getController() {
        return controller;
    }

    public static String getType() {
        return type;
    }
}
