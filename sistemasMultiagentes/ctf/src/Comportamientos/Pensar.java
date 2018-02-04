package Comportamientos;

import Agentes.Agente;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by isaac on 5/04/16.
 */
public class Pensar extends OneShotBehaviour {

    private ACLMessage mensajeRecibido;

    private static final Logger logger = LoggerFactory.getLogger(Pensar.class);

    public Pensar(ACLMessage mensajeRecibido) {

        this.mensajeRecibido = mensajeRecibido;

    }

    @Override
    public void action() {


        if(mensajeRecibido.getContent().contains("(") ||mensajeRecibido.getContent().contains("!")){

            if(Agente.soyDefensor(myAgent)) {
                logger.info("He recibido la respuesta de un agenteDefensor caido,notifico al mapa de dicha recepcion");
                Agente.getMapa(myAgent).defensorContestaSuPosicion(new int[]{-1, -1});

                if (Agente.getMapa(myAgent).puedoResponderAlServidor()) {
                    logger.info("Ya he recibido todas las posiciones asi que contesto con ellas");
                    myAgent.addBehaviour(new Actuar(mensajeRecibido));
                }
            }
            else{
                logger.info("He recibido la respuesta de un agenteAtacante caido,notifico al mapa de dicha recepcion");
                Agente.getMapa(myAgent).respuestaEscolta("¡-1 -1");
                myAgent.addBehaviour(new Actuar(mensajeRecibido));
            }

        }
        else if(mensajeRecibido.getContent().equals("Dame tu Posicion")){
            logger.info("He recibido la peticion de mi posicion, contesto con ella");
            ACLMessage respuesta = mensajeRecibido.createReply();
            //respuesta.setSender(myAgent.getAID());

            int[] posAct = Agente.getMapa(myAgent).getPosAct();
            if(Agente.estoyDentroDePlataforma) respuesta.setContent("$"+posAct[0]+" "+posAct[1]);
            else respuesta.setContent("$"+-1+" "+-1);

            myAgent.send(respuesta);
        }
        else if(mensajeRecibido.getContent().contains("$")){
            logger.info("He recibido la respuesta de un agenteDefensor con su posicion,notifico al mapa de dicha recepcion");
            String msn          = mensajeRecibido.getContent();
            int    posSeparador = msn.indexOf(" ");

            String fila = msn.substring(1,posSeparador);
            String col  = msn.substring(posSeparador+1,msn.length());

            Agente.getMapa(myAgent).defensorContestaSuPosicion(new int[]{Integer.valueOf(fila),Integer.valueOf(col)});

            // Si ya se han recibido todas las respuestas actuo
            if(Agente.getMapa(myAgent).puedoResponderAlServidor()){
                logger.info("Ya he recibido todas las posiciones asi que contesto con ellas");
                myAgent.addBehaviour(new Actuar(mensajeRecibido));
            }

        }
        else if(mensajeRecibido.getContent().contains("*")){
            logger.info("He recibido del portador la posicion a la que debo ir");
            String msn          = mensajeRecibido.getContent();
            int    posSeparador = msn.indexOf(" ");

            String fila = msn.substring(1,posSeparador);
            String col  = msn.substring(posSeparador+1,msn.length());

            Agente.getMapa(myAgent).portadorIndicaPos(new int[]{Integer.valueOf(fila),Integer.valueOf(col)});

            int[] posAct = Agente.getMapa(myAgent).getPosAct();
            ACLMessage respuesta = mensajeRecibido.createReply();
            if(Agente.estoyDentroDePlataforma) respuesta.setContent("¡"+posAct[0]+" "+posAct[1]);
            else respuesta.setContent("¡"+-1+" "+-1);

            myAgent.send(respuesta);


            myAgent.addBehaviour(new Actuar(mensajeRecibido));

        }
        else if(mensajeRecibido.getContent().contains("¡")){
            logger.info("He recibido del escolta su posicion y paso a actuar");
            Agente.getMapa(myAgent).respuestaEscolta(mensajeRecibido.getContent());
            myAgent.addBehaviour(new Actuar(mensajeRecibido));
        }
        else {

            Agente.time_start = System.currentTimeMillis();
            // Actualizo la informacion del mapa
            Agente.getMapa(myAgent).actualizarMapa(mensajeRecibido.getContent());
            Agente.mensajeBaseParaPlataforma = mensajeRecibido;

            logger.info("Informacion del entorno despues de recibir actualizacion:\n"+Agente.getMapa(myAgent).toString());
            if(Agente.soyDefensor(myAgent) && Agente.estoyDentroDePlataforma)Agente.getDefensas(myAgent);
            else if(!Agente.estoyDentroDePlataforma) myAgent.addBehaviour(new Actuar(mensajeRecibido));
            else {
                // Si me encuentro portando la bandera enemiga tengo que decidir que debe hacer mi escolta
                // si debe ir a por alguien o debe hacer lo que el vea
                if(Agente.EstadoPortadorBanderaEnemiga(myAgent)){
                    logger.info("Mando la posicion a la que debe ir mi escolta");
                    Agente.mandarAccionEscolta(myAgent);
                }
                else if(!Agente.estoyEscoltando(myAgent)) myAgent.addBehaviour(new Actuar(mensajeRecibido));
            }

        }

    }
}
