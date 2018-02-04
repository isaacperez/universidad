package Recursos;

import Agentes.Agente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by isaac on 5/04/16.
 */
public class EstrategiaAtaque {

    private static final Logger logger = LoggerFactory.getLogger(EstrategiaAtaque.class);

    private Mapa                             mapa;

    // Agente
    private String                           miEquipo,
                                             equipoContrario,
                                             miBandera,
                                             banderaContraria,
                                             miBase,
                                             miBaseBandera,
                                             baseContraria,
                                             baseContrariaBandera;

    private int                              estadoActual;

    public EstrategiaAtaque(Mapa mapa){

        this.mapa                 = mapa;
        this.miEquipo             = CONSTANTES.EQUIPO_DEL_AGENTE;
        this.equipoContrario      = String.valueOf(7 + ( 8%Integer.valueOf(miEquipo)));

        llenarInformacionDelAgente();

        this.estadoActual             = ESTADO_INICIAL;

        logger.info("Estado actual: ESTADO_INICIAL ( SIN OBJETIVOS)");
    }

    private void llenarInformacionDelAgente() {

        if(miEquipo==CONSTANTES.EQUIPO_AZUL) {
            this.miBandera            = CONSTANTES.BANDERA_AZUL;
            this.banderaContraria     = CONSTANTES.BANDERA_ROJA;
            this.miBase               = CONSTANTES.BASE_AZUL;
            this.miBaseBandera        = CONSTANTES.BASE_BANDERA_AZUL;
            this.baseContraria        = CONSTANTES.BASE_ROJA;
            this.baseContrariaBandera = CONSTANTES.BASE_BANDERA_ROJA;
        }
        else {
            this.miBandera            = CONSTANTES.BANDERA_ROJA;
            this.banderaContraria     = CONSTANTES.BANDERA_AZUL;
            this.miBase               = CONSTANTES.BASE_ROJA;
            this.miBaseBandera        = CONSTANTES.BASE_BANDERA_ROJA;
            this.baseContraria        = CONSTANTES.BASE_AZUL;
            this.baseContrariaBandera = CONSTANTES.BASE_BANDERA_AZUL;
        }

    }

    public void recibirActualizacion() {

        int[] posAct = mapa.getPosAct();
        transitar(posAct); // Transitara si ha cambiado algo o la lista de objetivos esta vacia y sino seguira en el estado actual
    }


    public void transitar(int[] posAct) {

        switch(estadoActual){

            case ESTADO_INICIAL:{
                estadoInicial(posAct);
            }
                break;

            case ESTADO_COGER_BANDERA_ENEMIGA:{
                estadoCogerBanderaEnemiga(posAct);
            }
                break;

            case ESTADO_SEGUIR_PORTADOR:{
                estadoSeguirPortador(posAct);
            }
                break;

            case ESTADO_IR_A_MI_BASE:{
                estadoIrAMiBase(posAct);
            }
                break;

            default: estadoActual = ESTADO_ERROR;
        }

    }

    private void estadoInicial(int[] posAct) {
        estadoActual = ESTADO_COGER_BANDERA_ENEMIGA;
        logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_INICIAL -> ESTADO_COGER_BANDERA_ENEMIGA");
        transitar(posAct);
    }

    private void estadoCogerBanderaEnemiga(int[] posAct) {

        int[] posJugadorMiEquipoBanderaEnemiga = mapa.miEquipoTieneBanderaEnemiga();

        if(posJugadorMiEquipoBanderaEnemiga[0]==posAct[0] && posJugadorMiEquipoBanderaEnemiga[1]==posAct[1]){

            logger.info("Tengo la bandera enemiga");
            estadoActual = ESTADO_IR_A_MI_BASE;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_COGER_BANDERA_ENEMIGA -> ESTADO_IR_A_MI_BASE");
            transitar(posAct);

        }
        else if(posJugadorMiEquipoBanderaEnemiga[0]!=-1){

            logger.info("Alguien de mi equipo tiene la bandera enemiga");
            estadoActual = ESTADO_SEGUIR_PORTADOR;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_COGER_BANDERA_ENEMIGA -> ESTADO_SEGUIR_PORTADOR");
            transitar(posAct);

        }

    }


    private void estadoSeguirPortador(int[] posAct){

        int[] posJugadorMiEquipoBanderaEnemiga = mapa.miEquipoTieneBanderaEnemiga();

        if(posJugadorMiEquipoBanderaEnemiga[0]==posAct[0] && posJugadorMiEquipoBanderaEnemiga[1]==posAct[1]){

            logger.info("Tengo la bandera enemiga");
            estadoActual = ESTADO_IR_A_MI_BASE;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_SEGUIR_PORTADOR -> ESTADO_IR_A_MI_BASE");
            transitar(posAct);

        }
        else if(posJugadorMiEquipoBanderaEnemiga[0]==-1){
            logger.info("Nadie de mi equipo tiene la bandera enemiga");
            estadoActual = ESTADO_COGER_BANDERA_ENEMIGA;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_SEGUIR_PORTADOR -> ESTADO_COGER_BANDERA_ENEMIGA");
            transitar(posAct);
        }



    }

    private void estadoIrAMiBase(int[] posAct) {

        int[] posJugadorMiEquipoBanderaEnemiga = mapa.miEquipoTieneBanderaEnemiga();

        if(posJugadorMiEquipoBanderaEnemiga[0]==-1){
            logger.info("Nadie de mi equipo tiene la bandera enemiga");
            estadoActual = ESTADO_COGER_BANDERA_ENEMIGA;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_IR_A_MI_BASE -> ESTADO_COGER_BANDERA_ENEMIGA");
            transitar(posAct);
        }
        else if(posJugadorMiEquipoBanderaEnemiga[0]!=posAct[0] || posJugadorMiEquipoBanderaEnemiga[1]!=posAct[1]){

            logger.info("Alguien de mi equipo tiene la bandera enemiga");
            estadoActual = ESTADO_SEGUIR_PORTADOR;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_IR_A_MI_BASE -> ESTADO_SEGUIR_PORTADOR");
            transitar(posAct);

        }
    }

    public String actuar() {

        switch (estadoActual){

            case ESTADO_COGER_BANDERA_ENEMIGA:{
                return accionEstadoCogerBanderaEnemiga();
            }

            case ESTADO_IR_A_MI_BASE:{
                return accionEstadoIrAMiBase();
            }

            case ESTADO_SEGUIR_PORTADOR:{
                return accionEstadoSeguirPortador();
            }

            default:{
                logger.error("No conozco la accion para el estado: "+estadoActual+", hago accion nula");
                return CONSTANTES.ACION_NULA;
            }

        }

    }

    private String accionEstadoSeguirPortador() {

        if(mapa.portadorADosCasillas()){
            logger.info("Portador a dos casillas me quedo quieto esperando que se aleje o se me acerque para cruzarnos");
            return CONSTANTES.ACION_NULA;
        }
        else {

            int[] posDestino = mapa.getPosDestinoEscolta();
            ArrayList<String> movimientos;

            if (posDestino[0] == -1) {
                logger.info("El portador no me ha indicado que hacer");

                ArrayList<int[]> posicionesMiEquipo = mapa.getPosicionesMiEquipo();

                if (mapa.estoyEnZonaDefensiva()) {
                    int[] posMejorParaDefender = mapa.damePosZonaDefensivaEscolta();
                    movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(), posMejorParaDefender, CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS, posicionesMiEquipo);
                    logger.info("Me situo en una casilla a defender ["+posMejorParaDefender[0]+" "+posMejorParaDefender[1]+"]");
                } else {
                    int[] posJugadorMiEquipoBanderaEnemiga = mapa.miEquipoTieneBanderaEnemiga();
                    logger.info("Siguo al portador yendo a  ["+posJugadorMiEquipoBanderaEnemiga[0]+" "+posJugadorMiEquipoBanderaEnemiga[1]+"]");
                    movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(), posJugadorMiEquipoBanderaEnemiga, CONSTANTES.M_ADD_NINGUNO, posicionesMiEquipo);

                }

            } else {

                ArrayList<int[]> posicionesMiEquipo = mapa.getPosicionesMiEquipo();

                logger.info("Voy a  ["+posDestino[0]+" "+posDestino[1]+"] como me ha indicado el portador");

                movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(), posDestino, CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS, CONSTANTES.M_ARRAYLIST_VACIO);

                if(movimientos.get(0).equals(CONSTANTES.ACION_NULA)){
                    movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(), posDestino, CONSTANTES.M_ADD_NINGUNO, posicionesMiEquipo);
                }

            }


            return movimientos.get(movimientos.size() - 1);
        }
    }

    private String accionEstadoIrAMiBase() {

        int[]            posMiBase = mapa.getPosMiBase();
        ArrayList<int[]> posExtras = new ArrayList<>();
        if (mapa.getPosEscolta()[0] != -1) posExtras.add(mapa.getPosEscolta());

        ArrayList<String> movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(),posMiBase,CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);

        if(movimientos.get(0).equals(CONSTANTES.ACION_NULA)) {

            movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(), posMiBase, CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS, posExtras);
        }

        int[] posSiguiente;
        if(movimientos.size()<2) posSiguiente = mapa.getPosAct();
        else posSiguiente = Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(mapa.getPosAct(),new ArrayList<>(movimientos.subList(movimientos.size()-1,movimientos.size())));


        ArrayList<int[]> enemigosEnMiCamino = mapa.enemigoEnMiCaminoBanderaEnemiga(posSiguiente);

        if(enemigosEnMiCamino.size()!=0){

            logger.info("He encontrado enemigos en mi camino");
            if(mapa.escoltaRespondeOK()){
                int[] posElegida = mapa.esquivarEnemigoEnMiCaminoConEscolta(enemigosEnMiCamino,posSiguiente,posMiBase);
                logger.info("Voy a ["+posElegida[0]+" "+posElegida[1]+"] para esquivar a mi enemigo y que me protega el escolta");
                movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(),posElegida,CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,posExtras);            }
            else {

                logger.info("No tengo escolta");
                int[] posElegida = mapa.esquivarEnemigoEnMiCamino(enemigosEnMiCamino, posSiguiente, posMiBase);
                movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(), posElegida, CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,posExtras);
                logger.info("Los intento esquivar, voy a [" + posElegida[0] + " " + posElegida[1] + "]");

            }

        }
        else logger.info("No he encontrado enemigos en mi camino, hago: "+movimientos.get(movimientos.size()-1));

        return movimientos.get(movimientos.size()-1);

    }

    private String accionEstadoCogerBanderaEnemiga() {

        int[] posBanderaEnemiga       = mapa.getPosAccesibleBanderaEnemiga();
        ArrayList<String> movimientos = A_Estrella.Calcular_Adaptativo(mapa,mapa.getPosAct(),posBanderaEnemiga,CONSTANTES.M_ADD_NINGUNO,CONSTANTES.M_ARRAYLIST_VACIO);

        int[] posSiguiente;

        // Comprobar que enemigo o alguien este en medio y no pueda pasar
        if(movimientos.size()<2) posSiguiente = mapa.getPosAct();
        else posSiguiente = Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(mapa.getPosAct(),new ArrayList<>(movimientos.subList(movimientos.size()-1,movimientos.size())));

        ArrayList<int[]> enemigosEnMiCamino = mapa.enemigoEnMiCaminoBanderaEnemiga(posSiguiente);
        if(enemigosEnMiCamino.size()!=0){

            logger.info("He encontrado enemigos en mi camino");
            int[] posElegida = mapa.esquivarEnemigoEnMiCamino(enemigosEnMiCamino,posSiguiente,posBanderaEnemiga);
            movimientos = A_Estrella.Calcular_Adaptativo(mapa,mapa.getPosAct(),posElegida,CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);
            logger.info("Los intento esquivar, voy a ["+posElegida[0]+" "+posElegida[1]+"]");

        }
        else{
            logger.info("No he encontrado enemigos en mi camino");
            // Comprobamos si hay algun compañero en la posicion a la que voy
        }

        return movimientos.get(movimientos.size()-1);

    }

    public String getEquipo() {
        return miEquipo;
    }

    // Estados del automata
    public static final int ESTADO_INICIAL                     = 0;
    public static final int ESTADO_COGER_BANDERA_ENEMIGA       = 1;
    public static final int ESTADO_SEGUIR_PORTADOR             = 2;     // El me dice en todo momento a donde debo ir
    public static final int ESTADO_IR_A_MI_BASE                = 3;
    public static final int ESTADO_FINAL                  = 100;
    public static final int ESTADO_ERROR                  = -1;


    // Objetivos a cumplir
    public static final String SIN_OBJETIVO                 = "-1";


    public boolean tengoBanderaEnemiga() {
        return estadoActual==ESTADO_IR_A_MI_BASE;
    }

    public boolean estoyEscoltando() {
        return estadoActual==ESTADO_SEGUIR_PORTADOR;
    }
}
