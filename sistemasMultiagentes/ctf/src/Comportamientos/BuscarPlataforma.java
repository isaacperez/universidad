package Comportamientos;

import Agentes.Agente;
import Agentes.AgenteAtacante;
import Agentes.AgenteDefensor;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by isaac on 29/03/16.
 */
public class BuscarPlataforma extends Behaviour {

    private boolean encontrado, entrar;
    private String  servicioBuscado;

    private static final Logger logger = LoggerFactory.getLogger(BuscarPlataforma.class);

    public BuscarPlataforma(String servicioBuscado, boolean quiereEntrar){

        this.encontrado      = false;
        this.entrar          = quiereEntrar;
        this.servicioBuscado = servicioBuscado;

    }

    public void action(){

        logger.info("Buscando el servicio "+servicioBuscado+" de la plataforma...");

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd       = new ServiceDescription();

        // Tipo de servicio a buscar
        sd.setType(this.servicioBuscado);

        template.addServices(sd);

        AID[] resultados = null;

        try {

            DFAgentDescription[] result = DFService.search(myAgent,template);

            resultados = new AID[result.length];

            for (int i=0; i< result.length; i++) resultados[i] = result[i].getName();

        } catch (FIPAException fe) {
            logger.error("Error al buscar el servicio "+servicioBuscado);
            resultados = null; fe.printStackTrace();
        }


        if (resultados == null) myAgent.doDelete();
        else {

            // No hay errores en la busqueda

            if (resultados.length != 0) {

                // Paramos la busqueda ya que se ha encontrado al menos un servicio
                encontrado = true;

                logger.info("He encontrado a " + resultados[0].getName()+ " comienzo FIPA-REQUEST con "+resultados[0].getLocalName());

                // Negociación de la entrada dentro de la plataforma
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

                // En local la plataforma debe tener el nombre "entorno3213"
                msg.addReceiver(new AID(resultados[0].getLocalName(),AID.ISLOCALNAME));
                msg.setContent(Agente.getEquipo(myAgent));
                msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

                if(entrar) myAgent.addBehaviour(new NegociarEntrada(myAgent,msg));

            }
            else logger.warn("No he encontrado nada al buscar el servicio "+servicioBuscado);

        }

    }

    public boolean done(){
        return encontrado;
    }

}
