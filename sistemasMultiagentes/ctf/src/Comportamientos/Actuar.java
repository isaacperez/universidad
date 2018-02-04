package Comportamientos;

import Agentes.Agente;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by isaac on 5/04/16.
 */
public class Actuar extends OneShotBehaviour {

    private ACLMessage mensajeRecibido;
    private static final Logger logger = LoggerFactory.getLogger(Actuar.class);

    public Actuar(ACLMessage mensajeRecibido) {
        this.mensajeRecibido = mensajeRecibido;
    }

    @Override
    public void action() {

        // Contesto al servidor con el movimiento calculado
        ACLMessage respuesta = Agente.mensajeBaseParaPlataforma.createReply();
        respuesta.setContent(Agente.actuar(myAgent));
        myAgent.send(respuesta);
        Agente.time_end = System.currentTimeMillis();
        logger.info(respuesta.getSender().getLocalName()+" contesto con: "+respuesta.getContent());
        logger.info("Tiempo desde que recibi la actualizacion del mapa hasta que contesto: "+ ( Agente.time_end - Agente.time_start ) +" ms");
    }
}
