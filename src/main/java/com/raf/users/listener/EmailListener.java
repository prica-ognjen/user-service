package com.raf.users.listener;


import com.raf.users.listener.helper.MessageHelper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class EmailListener {

    private final MessageHelper messageHelper;

    public EmailListener(MessageHelper messageHelper){
        this.messageHelper = messageHelper;
    }



    @JmsListener(destination = "${destination.createUserEmail}", concurrency = "5-10")
    public void sendMailOnReservation(Message message) throws JMSException{

    }

    /*

Register
--------
Pozdrav %ime %prezime! Molimo vas da potvrdite identited klikom na sledeci link %link

Reset password
--------------
Pozdrav %ime %prezime! Poslat je zahtev za promenu sifre vaseg naloga. Ukoliko ste vi to poslali, molimo vas da posetite sledeci link kako biste resetovali sifru: %link

Reserved
--------
Client: Pozdrav %ime %prezime! Uspesno ste rezervisali sobu. Saljemo vam bitne podatke:
Hotel %imeHotela
Broj Rezervacije %idRezervacije
Tip Sobe %tipSobe
Datum %date check in

Manager: Pozdrav sefe %ime %prezime! Klijent je naseo i rezervisao sobu:
Broj Rezervacije %idRezervacije
Tip Sobe %tipSobe
Datum %date check in

2 dana pre check in-a:
Postovani za dva dana vam je vreme da dodjete u hotel



 */

}
