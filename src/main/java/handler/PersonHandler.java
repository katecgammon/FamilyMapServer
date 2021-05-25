package handler;

import java.io.*;
import java.net.*;
import com.google.gson.Gson;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import request.LoginRequest;
import result.LoginResult;
import result.PersonResult;
import service.LoginService;
import service.PersonService;


public class PersonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        PersonService service = new PersonService();
        PersonResult result;
        String URLPath = exchange.getRequestURI().toString().substring(8);
        Gson gson = new Gson();

        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                result = service.findAllPeople();

                if (result != null) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    exchange.getResponseBody().close();
                }

                OutputStream resBody = exchange.getResponseBody();
                String JSONString = gson.toJson(result);
                writeString(JSONString, resBody);
                resBody.close();
                success = true;

            }
            else if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                result = service.findPerson(URLPath);
                if (result != null) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    exchange.getResponseBody().close();
                }

                OutputStream resBody = exchange.getResponseBody();
                String JSONString = gson.toJson(result);
                writeString(JSONString, resBody);
                resBody.close();
                success = true;

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
