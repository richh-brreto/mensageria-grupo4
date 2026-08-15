package com.escola.notification.usecases.ports.output;

import com.escola.notification.domain.entities.Notification;

/**
 * Output Port abstrato para envio de e-mail. Implementations podem usar SMTP
 * via Spring Mail ou serviços externos. Deve prover operação assíncrona.
 */
public interface EmailSenderPort {
    /**
     * Envia o e-mail representado pela Notification.
     */
    void sendEmail(Notification notification);
}
