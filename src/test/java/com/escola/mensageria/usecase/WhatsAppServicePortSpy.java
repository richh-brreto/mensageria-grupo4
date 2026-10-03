package com.escola.mensageria.usecase;

import com.escola.mensageria.domain.gateway.WhatsAppServicePort;
import com.escola.mensageria.domain.valueobject.Telefone;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WhatsAppServicePortSpy implements WhatsAppServicePort {

    private final List<Envio> envios = new CopyOnWriteArrayList<>();

    private volatile RuntimeException erroNoPrimeiroEnvio;

    @Override
    public ResultadoEnvio enviar(Telefone destino, String mensagem) {
        envios.add(new Envio(destino, mensagem));

        RuntimeException erro = erroNoPrimeiroEnvio;
        if (erro != null) {
            erroNoPrimeiroEnvio = null;
            throw erro;
        }

        return ResultadoEnvio.enviado("SM" + envios.size() + "SPY");
    }

    public void falharNoPrimeiroEnvioCom(RuntimeException erro) {
        this.erroNoPrimeiroEnvio = erro;
    }

    public void limpar() {
        envios.clear();
    }

    public List<Envio> envios() {
        return List.copyOf(envios);
    }

    public Envio envio(int indice) {
        return envios.get(indice);
    }

    public record Envio(Telefone destino, String mensagem) {
    }
}
