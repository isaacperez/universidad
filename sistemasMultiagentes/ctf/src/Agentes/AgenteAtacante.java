package Agentes;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Comportamientos.BuscarPlataforma;
import Recursos.EstrategiaAtaque;
import Recursos.Mapa;
import jade.core.Agent;

/**
 * Created by isaac on 29/03/16.
 */
public class AgenteAtacante extends Agent {

    private Mapa              mapa;
    private String            nombreServicioPlataforma;
    private EstrategiaAtaque  estrategia;

    private static final Logger logger = LoggerFactory.getLogger(AgenteAtacante.class);

    protected void setup(){

        this.mapa                     = new Mapa(this);
        this.nombreServicioPlataforma = "CTF";


        this.estrategia = new EstrategiaAtaque(this.getMapa());

        this.addBehaviour(new BuscarPlataforma(nombreServicioPlataforma,true));

        logger.info("AgenteAtacante Creado: "+getLocalName());

    }

    public Mapa getMapa(){ return mapa;}

    public EstrategiaAtaque getEstrategia(){
        return estrategia;
    }

    public String getNombreServicioPlataforma() {
        return nombreServicioPlataforma;
    }

    public void resetEstrategia() {
        this.estrategia = new EstrategiaAtaque(this.getMapa());
    }
}
