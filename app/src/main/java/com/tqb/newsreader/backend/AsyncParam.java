package com.tqb.newsreader.backend;

public class AsyncParam {
    public static Controller controller;
    public static String type;
    public static String category = "";

    public AsyncParam(Controller _controller, String _type) {
        controller = _controller;
        type = _type;
    }

    public AsyncParam(Controller _controller, String _type, String _category) {
        controller = _controller;
        type = _type;
        category = _category;
    }

    public static Controller getController() {
        return controller;
    }

    public static String getType() {
        return type;
    }

    public static String getCategory() {
        return category;
    }
}
