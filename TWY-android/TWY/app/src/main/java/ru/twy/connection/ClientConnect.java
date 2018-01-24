package ru.twy.connection;

import android.net.http.*;
import android.net.*;
import java.net.*;
import java.io.*;
import android.os.*;
import javax.net.ssl.*;
import org.json.*;

import ru.twy.core.Settings;

/**
 * Created by irit on 13.10.16.
 */
public class ClientConnect {

    String serverAddr;
    int port;
    HttpURLConnection httpClient;

    private static ClientConnect ourInstance = new ClientConnect();

    public static ClientConnect getInstance() {
        return ourInstance;
    }

    private ClientConnect() {
        serverAddr = "";
        int port = Settings.PORT;
    }

    public void connectToURL(String url) {
        serverAddr = url;
        try {
            URL site = new URL("http://" + serverAddr);
            httpClient = (HttpURLConnection) site.openConnection();
            httpClient.setRequestMethod("POST");
            httpClient.setDoOutput(true);
            httpClient.setDoInput(true);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String sendPost(String cmd) {
        byte[] data = null;
        InputStream incoming = null;
        if (httpClient == null )
            return "Error! Server address not found";
        try {
            URL site = new URL("http://" + serverAddr);
            httpClient.setRequestProperty("Content-Length", "" + Integer.toString(cmd.getBytes().length) );
            OutputStream sender = httpClient.getOutputStream();
            //Записываем данные в буфер
            data = cmd.getBytes("UTF-8");
            sender.write(data);
            data = null;

            //Совершаем подключение и отправляем команды
            httpClient.connect();
            int responseCode = httpClient.getResponseCode();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (responseCode == 200) { //Хороший ответ
                incoming = httpClient.getInputStream();
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ( (bytesRead = incoming.read(buffer) ) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                data = baos.toByteArray();
                return new String(data, "UTF-8");
            }
            if (responseCode == 500) {  //Проблемы на сервере
                return "Server refused";
            }
            return "Error! " + String.valueOf(responseCode);
        }
        catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
