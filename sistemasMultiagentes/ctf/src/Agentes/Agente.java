package Agentes;

import Comportamientos.EnviarMensajes;
import Recursos.Mapa;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by isaac on 29/03/16.
 */
public class Agente {

    private static final Logger logger = LoggerFactory.getLogger(Agente.class);

    public static ACLMessage mensajeBaseParaPlataforma;
    public static long time_start, time_end;
    public static boolean estoyDentroDePlataforma = false;


    public static String getEquipo(Agent agenteLanzador) {
        if(agenteLanzador instanceof AgenteAtacante){
            return ((AgenteAtacante)agenteLanzador).getEstrategia().getEquipo();
        }
        else if(agenteLanzador instanceof AgenteDefensor){
            return ((AgenteDefensor)agenteLanzador).getEstrategia().getEquipo();
        }
        else{
            logger.error("El Agente no es ni atacante ni defensor");
            return "ERROR";
        }
    }

    public static String actuar(Agent agenteLanzador) {
        if(agenteLanzador instanceof AgenteAtacante){
            return ((AgenteAtacante)agenteLanzador).getEstrategia().actuar();
        }
        else if(agenteLanzador instanceof AgenteDefensor){
            return ((AgenteDefensor)agenteLanzador).getEstrategia().actuar();
        }
        else{
            logger.error("El Agente no es atacante ni defensor");
            return "ERROR";
        }
    }

    public static void recibirActualizacion(Agent agenteLanzador) {
        if(agenteLanzador instanceof AgenteAtacante){

            ((AgenteAtacante)agenteLanzador).getEstrategia().recibirActualizacion();
        }
        else if(agenteLanzador instanceof AgenteDefensor){
            ((AgenteDefensor)agenteLanzador).getEstrategia().recibirActualizacion();
        }
        else logger.error("El Agente no es atacante ni defensor");
    }

    public static Mapa getMapa(Agent agenteLanzador){
        if(agenteLanzador instanceof AgenteAtacante){
            return ((AgenteAtacante)agenteLanzador).getMapa();
        }
        else if(agenteLanzador instanceof AgenteDefensor){
            return ((AgenteDefensor)agenteLanzador).getMapa();
        }
        else{

            logger.error("El Agente no es atacante ni defensor");
            return new Mapa(agenteLanzador);
        }
    }


    public static void getDefensas(Agent agenteLanzador) {
        if(agenteLanzador instanceof AgenteDefensor) {


            ((AgenteDefensor) agenteLanzador).getMapa().prepararseParaRecibirPosDefensivas();

            EnviarMensajes envioDefensa1 = new EnviarMensajes("isaacDefensor1", "Dame tu Posicion", ACLMessage.INFORM);
            EnviarMensajes envioDefensa2 = new EnviarMensajes("isaacDefensor2", "Dame tu Posicion", ACLMessage.INFORM);
            EnviarMensajes envioDefensa3 = new EnviarMensajes("isaacDefensor3", "Dame tu Posicion", ACLMessage.INFORM);

            agenteLanzador.addBehaviour(envioDefensa1);
            System.out.println("He enviado al agente 1");
            agenteLanzador.addBehaviour(envioDefensa2);
            System.out.println("He enviado al agente 2");
            agenteLanzador.addBehaviour(envioDefensa3);
            System.out.println("He enviado al agente 3");
        } if(agenteLanzador instanceof AgenteAtacante){

        }

    }

    public static boolean soyDefensor(Agent agenteLanzador) {
        if(agenteLanzador instanceof AgenteAtacante){
            return false;
        }
        else if(agenteLanzador instanceof AgenteDefensor){
            return true;
        }
        else{

            logger.error("El Agente no es atacante ni defensor");
            return false;
        }
    }

    public static boolean EstadoPortadorBanderaEnemiga(Agent agenteLanzador) {

        if(agenteLanzador instanceof AgenteAtacante){
            return ((AgenteAtacante)agenteLanzador).getEstrategia().tengoBanderaEnemiga();
        }
        else if(agenteLanzador instanceof AgenteDefensor){
            return false;
        }
        else{

            logger.error("El Agente no es atacante ni defensor");
            return false;
        }
    }

    public static boolean estoyEscoltando(Agent agenteLanzador) {

        if(agenteLanzador instanceof AgenteAtacante){
            return ((AgenteAtacante)agenteLanzador).getEstrategia().estoyEscoltando();
        }
        else if(agenteLanzador instanceof AgenteDefensor){
            return false;
        }
        else{

            logger.error("El Agente no es atacante ni defensor");
            return false;
        }
    }

    public static void mandarAccionEscolta(Agent agenteLanzador) {
        if(agenteLanzador instanceof AgenteAtacante){

            int[] posParaEscolta =  ((AgenteAtacante)agenteLanzador).getMapa().darPosicionEscolta();

            EnviarMensajes envioAtacante;
            if(agenteLanzador.getLocalName().equals("isaacAtacante1")) envioAtacante = new EnviarMensajes("isaacAtacante2", "*"+posParaEscolta[0]+" "+posParaEscolta[1], ACLMessage.INFORM);
            else envioAtacante = new EnviarMensajes("isaacAtacante1", "*"+posParaEscolta[0]+" "+posParaEscolta[1], ACLMessage.INFORM);

            agenteLanzador.addBehaviour(envioAtacante);
            System.out.println("He enviado al atacante escolta");
        }
        else{
            logger.error("El Agente no es atacante ");
        }
    }

    public static String getNombreServicioPlataforma(Agent agenteLanzador) {
        if(agenteLanzador instanceof AgenteAtacante) return ((AgenteAtacante)agenteLanzador).getNombreServicioPlataforma();
        else if(agenteLanzador instanceof AgenteDefensor) return ((AgenteDefensor)agenteLanzador).getNombreServicioPlataforma();
        else logger.error("El Agente no es atacante ");
        return null;

    }

    public static void resetEstrategia(Agent agenteLanzador) {
        if(agenteLanzador instanceof AgenteAtacante) ((AgenteAtacante)agenteLanzador).resetEstrategia();
        else if(agenteLanzador instanceof AgenteDefensor) ((AgenteDefensor)agenteLanzador).resetEstrategia();
    }

}
