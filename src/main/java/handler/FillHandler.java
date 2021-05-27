package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.DataAccessException;
import request.FillRequest;
import result.FillResult;
import service.FillService;

import java.io.*;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                Gson gson = new Gson();
                String URLPath = exchange.getRequestURI().toString();
                URLPath = URLPath.substring("/fill/".length());
                String toFind = "/";
                int index = URLPath.indexOf(toFind);
                String numGen;
                int numGenerations;
                String username;
                if (index == -1) {
                    numGenerations = 4;
                    username = URLPath;
                }
                else {
                    username = URLPath.substring(0, index);
                    numGen = URLPath.substring(index + toFind.length());
                    numGenerations = Integer.parseInt(numGen);
                }
                FillRequest request = new FillRequest(username, numGenerations);
                FillService service = new FillService();
                FillResult result = service.fill(request);

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
