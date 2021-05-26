package handler;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import dao.DataAccessException;
import request.LoadRequest;
import request.LoginRequest;
import result.LoadResult;
import result.LoginResult;
import service.LoadService;
import service.LoginService;

import java.io.IOException;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            //TODO: Figure out how to interpret this input data...
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                Gson gson = new Gson();
                LoadRequest request = (LoadRequest) gson.fromJson(reqData, LoadRequest.class);
                LoadService service = new LoadService();
                LoadResult result = service.load(request);

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

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
