package handler;

import java.io.*;
import java.net.*;
import com.google.gson.Gson;

import com.sun.net.httpserver.*;
import dao.DataAccessException;
import request.LoginRequest;
import result.ClearResult;
import result.LoginResult;
import service.ClearService;
import service.LoginService;


public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
//                InputStream reqBody = exchange.getRequestBody();
//                String reqData = readString(reqBody);
                Gson gson = new Gson();
//                LoginRequest request = (LoginRequest) gson.fromJson(reqData, LoginRequest.class);
                ClearService service = new ClearService();
                ClearResult result = service.clear();

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
