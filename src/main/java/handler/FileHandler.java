package handler;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import com.sun.net.httpserver.*;

public class FileHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String URLPath = exchange.getRequestURI().toString();
        if (URLPath.equals("/") || URLPath.equals(null)) {
            URLPath = "/index.html";
        }
        URLPath = "web" + URLPath;
        File file = new File(URLPath);
        if (file.exists()) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream respBody = exchange.getResponseBody();
            Files.copy(file.toPath(), respBody);
            respBody.close();
        }
        else {
            URLPath = "web/HTML/404.html";
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            File newFile = new File(URLPath);
            OutputStream respBody = exchange.getResponseBody();
            Files.copy(newFile.toPath(), respBody);
            respBody.close();
        }
        exchange.getResponseBody().close();
    }
}
