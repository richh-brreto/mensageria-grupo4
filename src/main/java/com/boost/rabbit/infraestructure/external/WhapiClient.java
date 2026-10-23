package com.boost.rabbit.infraestructure.external;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WhapiClient {

    public void enviarMensagem() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\"to\":\"5511961113838\",\"body\":\"Falaaaaaa MIURAAAA\",\"typing_time\":0}", mediaType);
        Request request = new Request.Builder()
          .url("https://gate.whapi.cloud/messages/text")
          .post(body)
          .addHeader("accept", "application/json")
          .addHeader("content-type", "application/json")
          .addHeader("authorization", "Bearer INgE2mruL7l03CRDn5aYPSEQqT7UKQMe")
          .build();

        Response response = client.newCall(request).execute();
    }
}
