package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.DataAccessException;
import result.AllEventResult;
import result.EventResult;
import service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EventService service = new EventService();
        AllEventResult aResult;
        EventResult result;
        String authToken = new String();
        String URLPath = new String();
        Gson gson = new Gson();
        boolean success = false;

        String URLPath1 = exchange.getRequestURI().toString();
        if (!URLPath1.equals("/event")) {
            URLPath = URLPath1.substring("/event/".length());
        }

        Headers reqHeaders = exchange.getRequestHeaders();
        if(reqHeaders.containsKey("Authorization")) {
            authToken = reqHeaders.getFirst("Authorization");
        }

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                if (URLPath1.equals("/event")) {
                    aResult = service.findAllEvents(authToken);

                    if (aResult.getSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                    }

                    OutputStream resBody = exchange.getResponseBody();
                    String JSONString = gson.toJson(aResult);
                    writeString(JSONString, resBody);
                    resBody.close();
                    success = aResult.getSuccess();
                }
                else {
                    result = service.find(URLPath, authToken);
                    if (result.getSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }

                    OutputStream resBody = exchange.getResponseBody();
                    String JSONString = gson.toJson(result);
                    writeString(JSONString, resBody);
                    resBody.close();
                    success = result.getSuccess();
                }
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
