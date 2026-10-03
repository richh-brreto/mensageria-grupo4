package com.escola.mensageria.usecase;

import com.escola.mensageria.domain.entity.AvisoContrato;
import com.escola.mensageria.domain.exception.FalhaEnvioWhatsAppException;
import com.escola.mensageria.domain.support.Precondicoes;
import com.escola.mensageria.domain.valueobject.Telefone;

import java.util.List;

public interface EnviarAvisosWhatsAppUseCase {

    Resultado executar(AvisoContrato aviso) throws FalhaEnvioWhatsAppException;

    record Resultado(List<EnvioConfirmado> envios) {

        public Resultado {
            envios = List.copyOf(Precondicoes.naoNulo(envios, "envios"));
        }
    }

    record EnvioConfirmado(String destinatario, Telefone telefone, String identificadorMensagem) {

        public EnvioConfirmado {
            destinatario = Precondicoes.textoObrigatorio(destinatario, "destinatario");
            telefone = Precondicoes.naoNulo(telefone, "telefone");
            identificadorMensagem = Precondicoes.textoObrigatorio(identificadorMensagem, "identificadorMensagem");
        }
    }
}
