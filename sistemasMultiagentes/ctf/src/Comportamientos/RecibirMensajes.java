package Comportamientos;

import Agentes.Agente;
import Recursos.A_Estrella;
import Recursos.CONSTANTES;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by isaac on 30/03/16.
 */
public class RecibirMensajes extends CyclicBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(RecibirMensajes.class);

    public RecibirMensajes() {

    }

    @Override
    public void action() {

        ACLMessage mensajeRecibido = myAgent.receive();

        if(mensajeRecibido != null) {

            logger.info(myAgent.getLocalName()+": He recibido de la plataforma: \n"+ mensajeRecibido.getContent());


            if (mensajeRecibido.getContent().equals(CONSTANTES.FIN_DEL_JUEGO)){
                Agente.estoyDentroDePlataforma = false;
                logger.info("No estoy en la plataforma, vuelvo a intentar conectarme");
                myAgent.addBehaviour(new BuscarPlataforma(Agente.getNombreServicioPlataforma(myAgent),true));
                myAgent.removeBehaviour(this);

            }
            else{

                // Analizamos el mensaje recibido por el servidor
                myAgent.addBehaviour(new Pensar(mensajeRecibido));
            }

        } else block();
    }
}
