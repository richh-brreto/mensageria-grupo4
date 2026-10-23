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

    public void enviarMensagem(String telefone, String mensagem) throws IOException {
        OkHttpClient client = new OkHttpClient();

        if (telefone != null && !telefone.startsWith("55")) {
            telefone = "55" + telefone;
        }

        MediaType mediaType = MediaType.parse("application/json");
        String mensagemEscapada = mensagem.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
        RequestBody body = RequestBody.create("{\"to\":\"" + telefone + "\",\"body\":\"" + mensagemEscapada + "\",\"typing_time\":0}", mediaType);
        Request request = new Request.Builder()
          .url("https://gate.whapi.cloud/messages/text")
          .post(body)
          .addHeader("accept", "application/json")
          .addHeader("content-type", "application/json")
          .addHeader("authorization", "Bearer INgE2mruL7l03CRDn5aYPSEQqT7UKQMe")
          .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("Mensagem enviada com sucesso");
        } else {
            System.out.println("Erro ao enviar a mensagem: " + response.code());
        }
    }
}
