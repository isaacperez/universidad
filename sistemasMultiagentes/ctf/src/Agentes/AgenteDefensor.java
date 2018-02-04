package Agentes;

import Comportamientos.BuscarPlataforma;
import Recursos.EstrategiaAtaque;
import Recursos.EstrategiaDefensiva;
import Recursos.Mapa;
import jade.core.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by isaac on 29/03/16.
 */
public class AgenteDefensor extends Agent {

    private Mapa                mapa;
    private String              nombreServicioPlataforma;
    private EstrategiaDefensiva estrategia;

    private static final Logger logger = LoggerFactory.getLogger(AgenteDefensor.class);

    protected void setup(){

        this.mapa                     = new Mapa(this);
        this.nombreServicioPlataforma = "CTF";

        this.estrategia = new EstrategiaDefensiva(this.getMapa());

        this.addBehaviour(new BuscarPlataforma(nombreServicioPlataforma,true));

        logger.info("AgenteDefensor Creado: "+getLocalName());

    }

    public Mapa getMapa(){ return mapa;}

    public EstrategiaDefensiva getEstrategia(){
        return estrategia;
    }

    public String getNombreServicioPlataforma() {
        return nombreServicioPlataforma;
    }

    public void resetEstrategia() {
        this.estrategia = new EstrategiaDefensiva(this.getMapa());
    }

}
