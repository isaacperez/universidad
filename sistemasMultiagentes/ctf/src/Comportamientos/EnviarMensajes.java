package Comportamientos;


import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by Isaac on 25/03/2016.
 */
public class EnviarMensajes extends OneShotBehaviour {

    AID    destino;
    String mensaje;
    int    intencion;

    public EnviarMensajes(String destino, String mensaje, int intencion){

        this.destino   = new AID(destino,AID.ISLOCALNAME);
        this.mensaje   = mensaje;
        this.intencion = intencion;

    }

    public void action(){

        ACLMessage m = new ACLMessage(intencion);
        m.addReceiver(destino);
        m.setContent(mensaje);

        myAgent.send(m);

    }

}
