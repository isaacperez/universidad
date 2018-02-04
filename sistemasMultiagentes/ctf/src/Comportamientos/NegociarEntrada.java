package Comportamientos;

import Agentes.Agente;
import Agentes.AgenteAtacante;
import Agentes.AgenteCreador;
import Recursos.A_Estrella;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by isaac on 29/03/16.
 */
public class NegociarEntrada extends SimpleAchieveREInitiator {

    private static final Logger logger = LoggerFactory.getLogger(NegociarEntrada.class);

    private Agent a;
    private ACLMessage msgComienzo;

    public NegociarEntrada(Agent a, ACLMessage msg){

        super(a,msg);
        this.a = a;
        this.msgComienzo = msg;
        logger.error("Comienzo de la entrada a la plataforma");
    }

    public void handleAgree(ACLMessage msg){

        logger.error("Recibido handleAgree: "+ msg.getContent());


    }

    public void handleRefuse(ACLMessage msg){

        logger.error("Recibido handleRefuse: "+ msg.getContent());

        Random rnd = new Random();
        try {
            Thread.sleep(rnd.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // myAgent.addBehaviour(new BuscarPlataforma(Agente.getNombreServicioPlataforma(myAgent),true));
        myAgent.addBehaviour(new NegociarEntrada(a,msgComienzo));

    }

    public void handleInform(ACLMessage msg){

        logger.error("Recibido handleInform: "+ msg.getContent());

        logger.error("Estamos conectado, comienza la recepcion de mensajes");


        Agente.estoyDentroDePlataforma = true;
        Agente.resetEstrategia(myAgent);
        Agente.getMapa(myAgent).montarMapa(msg.getContent());
        // Primera accion
        ACLMessage msg2 = msg.createReply();
        msg2.setContent(Agente.actuar(myAgent));
        myAgent.send(msg2);
        AgenteCreador.HaEntrado = true;
        // Empezar a recibir mensajes de la plataforma
        myAgent.addBehaviour(new RecibirMensajes());
    }

    public void handleNotUnderstood(ACLMessage msg){

        logger.error("Recibido handleNotUnderstood: "+ msg.getContent());

    }

    public void handleOutOfSequence(ACLMessage msg){

        logger.error("Recibido handleOutOfSequence: "+ msg.getContent());

    }

}
