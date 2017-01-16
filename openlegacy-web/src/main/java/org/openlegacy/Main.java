package org.openlegacy;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.post;
import spark.Request;
import spark.Response;
import spark.Route;


public class Main {
    public static void main(String[] args) {
        get("/test", (req, res) -> "Test");
    }
}
