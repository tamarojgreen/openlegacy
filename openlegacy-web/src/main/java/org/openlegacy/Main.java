package org.openlegacy;

import static spark.Spark.*;


public class Main {
    public static void main(String[] args) {
        get("/test", (req, res) -> "Test");
    }
}
