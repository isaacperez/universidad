package Agentes;


import Comportamientos.BuscarPlataforma;
import Recursos.CONSTANTES;
import Recursos.EstrategiaAtaque;
import Recursos.Mapa;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by isaac on 29/03/16.
 */
public class AgenteCreador extends Agent {

    private static final Logger logger = LoggerFactory.getLogger(AgenteCreador.class);
    public static boolean HaEntrado = false;

    protected void setup(){

        logger.info("Agente creador creado");
        PlatformController pt = this.getContainerController();
        AgentController control;
        AgentController control2;

        try {

            if(getLocalName().equals("isaacCreadorP1")) {
                CONSTANTES.EQUIPO_DEL_AGENTE = CONSTANTES.EQUIPO_ROJO;
                // Lanzo a los tres defensores
                control = pt.createNewAgent("isaacDefensor1", "Agentes.AgenteDefensor", null);
                control.start();

                Thread.sleep(700);
                System.out.println("Ya ha sido creado");
                control = pt.createNewAgent("isaacDefensor2", "Agentes.AgenteDefensor", null);
                control.start();

            }
            else if(getLocalName().equals("isaacCreadorP2")){
                CONSTANTES.EQUIPO_DEL_AGENTE = CONSTANTES.EQUIPO_AZUL;
                // Lanzo a los dos atacantes

                control = pt.createNewAgent("isaacAtacante1", "Agentes.AgenteAtacante", null);
                control.start();

                Thread.sleep(700);
                control = pt.createNewAgent("isaacAtacante2", "Agentes.AgenteAtacante", null);
                control.start();
            }
            else {

                // Lanzo a los tres defensores
                control  = pt.createNewAgent("isaacDefensor1", "Agentes.AgenteDefensor", null);
                control.start();

                while(!HaEntrado){
                    Thread.sleep(20);
                }
                AgenteCreador.HaEntrado = false;
                Thread.sleep(650);

                control2 = pt.createNewAgent("isaacDefensor2", "Agentes.AgenteDefensor", null);
                control2.start();

                while(!HaEntrado){
                    Thread.sleep(20);
                }
                AgenteCreador.HaEntrado = false;
                Thread.sleep(650);

                control = pt.createNewAgent("isaacDefensor3", "Agentes.AgenteDefensor", null);
                control.start();

                // Lanzo a los dos atacantes
                while(!HaEntrado){
                    Thread.sleep(20);
                }
                AgenteCreador.HaEntrado = false;
                Thread.sleep(650);

                control = pt.createNewAgent("isaacAtacante1", "Agentes.AgenteAtacante", null);
                control.start();

                while(!HaEntrado){
                    Thread.sleep(20);
                }
                AgenteCreador.HaEntrado = false;
                Thread.sleep(650);

                control = pt.createNewAgent("isaacAtacante2", "Agentes.AgenteAtacante", null);
                control.start();
            }

        } catch (ControllerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
