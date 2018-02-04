package Recursos;

import Agentes.AgenteDefensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by isaac on 5/04/16.
 */
public class EstrategiaDefensiva {

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
    // Automata
    private Map<String, String >             descripcionesObjetivos;
    private int                              estadoActual;
    private String                           objetivoActual;

    private static final Logger logger = LoggerFactory.getLogger(EstrategiaDefensiva.class);
    private int[] posObjetivo;

    public EstrategiaDefensiva(Mapa mapa){

        this.mapa                 = mapa;
        this.miEquipo             = CONSTANTES.EQUIPO_DEL_AGENTE;
        this.equipoContrario      = String.valueOf(7 + ( 8%Integer.valueOf(miEquipo)));

        llenarInformacionDelAgente();
        crearRelacionObjetivosReales();

        this.estadoActual             = ESTADO_INICIAL;
        this.objetivoActual           = SIN_OBJETIVO;
        this.posObjetivo              = new int[]{-1,-1};

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

    private void crearRelacionObjetivosReales() {

        // Objetivos reales
        this.descripcionesObjetivos = new HashMap<>();

        this.descripcionesObjetivos.put(SIN_OBJETIVO,CONSTANTES.POSICION_ACTUAL);
        this.descripcionesObjetivos.put(IR_A_BASE_CONTRARIA_BANDERA,baseContrariaBandera);
        this.descripcionesObjetivos.put(IR_A_BANDERA_CONTRARIA,banderaContraria);
        this.descripcionesObjetivos.put(IR_A_BASE_PROPIA,miBase);
        this.descripcionesObjetivos.put(IR_A_BASE_PROPIA_CON_BANDERA,miBaseBandera);

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
            case ESTADO_PERMANECER_POSICION_DEFENSA:{
                estadoPermanecerPosicionDefensa(posAct);
            }
            break;
            case ESTADO_IR_POR_ATACANTE:{
                estadoIrPorAtacante(posAct);
            }
            break;
            case ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA:{
                estadoPerseguirAtacanteConBandera(posAct);
            }
            break;
            case  ESTADO_COGER_BANDERA:{
                estadoCogerBandera(posAct);
            }
            break;
            default: estadoActual = ESTADO_ERROR;
        }

    }

    private void estadoCogerBandera(int[] posAct) {

        int[] posAtacanteConMiBandera = mapa.atacanteConMiBandera();

        if(posAtacanteConMiBandera[0]!=-1){
            estadoActual = ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA;
            this.posObjetivo = posAtacanteConMiBandera;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_COGER_BANDERA -> ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA");
            transitar(posAct);
        }
        else if(mapa.miBanderaNoEstaEnLaBase()){
            // Sigo en este estado
        }
        else{

            ArrayList<int[]> atacantesEnMiEntorno = mapa.getAtacantesEnEntornoDefensivoYCercaDeMi();
            boolean soyElegidoParaIrPorAtacante   = false;
            int indice = -1;

            for (int i = 0; i < atacantesEnMiEntorno.size() && !soyElegidoParaIrPorAtacante; i++) {
                int[] posDefensorElegido = mapa.quienVaPor(atacantesEnMiEntorno.get(i));
                if(mismaPosicion(posDefensorElegido,posAct)){
                    soyElegidoParaIrPorAtacante = true;
                    indice = i;
                }
            }

            logger.info("Atacantes en mi entorno:"+atacantesEnMiEntorno.size());

            if (!soyElegidoParaIrPorAtacante) {
                estadoActual = ESTADO_PERMANECER_POSICION_DEFENSA;
                logger.info("[" + posAct[0] + " " + posAct[1] + "] - " + "ESTADO_COGER_BANDERA -> ESTADO_PERMANECER_POSICION_DEFENSA");
                transitar(posAct);
            }
            else{
                estadoActual = ESTADO_IR_POR_ATACANTE;
                this.posObjetivo = atacantesEnMiEntorno.get(indice);
                logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_COGER_BANDERA -> ESTADO_IR_POR_ATACANTE");
                transitar(posAct);
            }

        }

    }

    private void estadoPerseguirAtacanteConBandera(int[] posAct) {
        int[] posAtacanteConMiBandera = mapa.atacanteConMiBandera();

        if(posAtacanteConMiBandera[0]!=-1){
            this.posObjetivo = posAtacanteConMiBandera;
        }
        else if(mapa.miBanderaNoEstaEnLaBase()){
            estadoActual = ESTADO_COGER_BANDERA;
            logger.info("[" + posAct[0] + " " + posAct[1] + "] - " + "ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA -> ESTADO_COGER_BANDERA");
            transitar(posAct);
        }
        else {

            ArrayList<int[]> atacantesEnMiEntorno = mapa.getAtacantesEnEntornoDefensivoYCercaDeMi();
            boolean soyElegidoParaIrPorAtacante = false;
            int indice = -1;

            for (int i = 0; i < atacantesEnMiEntorno.size() && !soyElegidoParaIrPorAtacante; i++) {
                int[] posDefensorElegido = mapa.quienVaPor(atacantesEnMiEntorno.get(i));
                if (mismaPosicion(posDefensorElegido, posAct)) {
                    soyElegidoParaIrPorAtacante = true;
                    indice = i;
                }
            }


            if (!soyElegidoParaIrPorAtacante) {
                estadoActual = ESTADO_PERMANECER_POSICION_DEFENSA;
                logger.info("[" + posAct[0] + " " + posAct[1] + "] - " + "ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA -> ESTADO_PERMANECER_POSICION_DEFENSA");
                transitar(posAct);
            } else {
                estadoActual = ESTADO_IR_POR_ATACANTE;
                logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA -> ESTADO_IR_POR_ATACANTE");
                this.posObjetivo = atacantesEnMiEntorno.get(indice);
                transitar(posAct);
            }
        }
    }

    private void estadoInicial(int[] posAct) {
        estadoActual = ESTADO_PERMANECER_POSICION_DEFENSA;
        logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_INICIAL -> ESTADO_PERMANECER_POSICION_DEFENSA");
        transitar(posAct);
    }

    private void estadoPermanecerPosicionDefensa(int[] posAct) {

        int[] posAtacanteConMiBandera = mapa.atacanteConMiBandera();

        if(posAtacanteConMiBandera[0]!=-1){
            estadoActual = ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA;
            this.posObjetivo = posAtacanteConMiBandera;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_PERMANECER_POSICION_DEFENSA -> ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA");
            transitar(posAct);
        }
        else if(mapa.miBanderaNoEstaEnLaBase()){
            estadoActual = ESTADO_COGER_BANDERA;
            logger.info("[" + posAct[0] + " " + posAct[1] + "] - " + "ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA -> ESTADO_COGER_BANDERA");
            transitar(posAct);
        }
        else{

            ArrayList<int[]> atacantesEnMiEntorno = mapa.getAtacantesEnEntornoDefensivoYCercaDeMi();
            boolean soyElegidoParaIrPorAtacante   = false;
            int indice = -1;

            for (int i = 0; i < atacantesEnMiEntorno.size() && !soyElegidoParaIrPorAtacante; i++) {
                int[] posDefensorElegido = mapa.quienVaPor(atacantesEnMiEntorno.get(i));
                if(mismaPosicion(posDefensorElegido,posAct)){
                    soyElegidoParaIrPorAtacante = true;
                    indice = i;
                }
            }

            logger.info("Atacantes en mi entorno:"+atacantesEnMiEntorno.size());

            if(soyElegidoParaIrPorAtacante){
                estadoActual = ESTADO_IR_POR_ATACANTE;
                this.posObjetivo = atacantesEnMiEntorno.get(indice);
                logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_PERMANECER_POSICION_DEFENSA -> ESTADO_IR_POR_ATACANTE");
                transitar(posAct);
            }

        }

    }

    private void estadoIrPorAtacante(int[] posAct) {


        int[] posAtacanteConMiBandera = mapa.atacanteConMiBandera();

        if(posAtacanteConMiBandera[0]!=-1){
            estadoActual = ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA;
            this.posObjetivo = posAtacanteConMiBandera;
            logger.info("["+posAct[0]+" "+posAct[1]+"] - "+"ESTADO_IR_POR_ATACANTE -> ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA");
            transitar(posAct);
        }
        else if(mapa.miBanderaNoEstaEnLaBase()){
            estadoActual = ESTADO_COGER_BANDERA;
            logger.info("[" + posAct[0] + " " + posAct[1] + "] - " + "ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA -> ESTADO_COGER_BANDERA");
            transitar(posAct);
        }
        else {

            ArrayList<int[]> atacantesEnMiEntorno = mapa.getAtacantesEnEntornoDefensivoYCercaDeMi();
            boolean soyElegidoParaIrPorAtacante = false;
            int indice = -1;

            for (int i = 0; i < atacantesEnMiEntorno.size() && !soyElegidoParaIrPorAtacante; i++) {
                int[] posDefensorElegido = mapa.quienVaPor(atacantesEnMiEntorno.get(i));
                if (mismaPosicion(posDefensorElegido, posAct)) {
                    soyElegidoParaIrPorAtacante = true;
                    indice = i;
                }
            }


            if (!soyElegidoParaIrPorAtacante) {
                estadoActual = ESTADO_PERMANECER_POSICION_DEFENSA;
                logger.info("[" + posAct[0] + " " + posAct[1] + "] - " + "ESTADO_IR_POR_ATACANTE -> ESTADO_PERMANECER_POSICION_DEFENSA");
                transitar(posAct);
            } else {
                this.posObjetivo = atacantesEnMiEntorno.get(indice);
                logger.info("Voy por el enemigo: ["+ atacantesEnMiEntorno.get(indice)[0]+" "+atacantesEnMiEntorno.get(indice)[1]+"]");
            }
        }

    }

    public String actuar() {

        switch (estadoActual){

            case ESTADO_PERMANECER_POSICION_DEFENSA:{

                int[] posDestino = mapa.posMiAtacanteEnSuCamino();
                if(posDestino[0]==-1) posDestino = mapa.buscarPosicionDondeDefender();
                else{
                    logger.info("Estoy en medio del paso de uno de mis atacantes, voy a: ["+posDestino[0]+" "+posDestino[1]+"] para apartarme");
                }

                ArrayList<String> movimientos = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(), posDestino, CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS, CONSTANTES.M_ARRAYLIST_VACIO);

                logger.info("{ESTADO_PERMANECER_POSICION_DEFENSA} Accion a realizar: voy a defender mi bandera hago la accion ("+movimientos.get(movimientos.size() - 1)+") para situarme en "+posDestino[0]+" "+posDestino[1]);

                return movimientos.get(movimientos.size() - 1);

            }
            case ESTADO_IR_POR_ATACANTE:{

                if(mapa.calcularDistancia(mapa.getPosAct(),posObjetivo)<=1.45) {

                    int[] posElegida = posElegidaParaInteceptar(posObjetivo,mapa.getPosAct());

                    ArrayList<String> mov = A_Estrella.Calcular(mapa, mapa.getPosAct(), posElegida, CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS, CONSTANTES.M_ARRAYLIST_VACIO);

                    logger.info("{ESTADO_IR_POR_ATACANTE} Accion a realizar: tengo a mi enemigo a mi alcance, de forma aleatoria pienso que va a ["+posElegida[0]+" "+posElegida[1]+"]");
                    return mov.get(mov.size()-1);
                }
                else{

                    ArrayList<String> movimientosEnemigo = A_Estrella.Calcular_Adaptativo(mapa,posObjetivo,mapa.getPosMiBandera(),CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);
                    int[]             posInterceptar     = Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(posObjetivo,new ArrayList<>(movimientosEnemigo.subList( 1, movimientosEnemigo.size())));

                    ArrayList<String> movimientosParaInterceptarEnemigo = A_Estrella.Calcular_Adaptativo(mapa,mapa.getPosAct(),posInterceptar,CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);
                    logger.info("{ESTADO_IR_POR_ATACANTE} Accion a realizar: voy a la siguiente posicion a la que va a ir mi enemigo para llegar a mi bandera ("+(movimientosParaInterceptarEnemigo.size() - 1)+")");
                    return movimientosParaInterceptarEnemigo.get(movimientosParaInterceptarEnemigo.size() - 1);

                }

            }

            case ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA:{

                ArrayList<String> movimientosEnemigo = A_Estrella.Calcular_Adaptativo(mapa,posObjetivo,mapa.getPosBaseEnemiga(),CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);

                if(movimientosEnemigo.size()!=0) {

                    int[] posInterceptar = Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(posObjetivo, new ArrayList<>(movimientosEnemigo.subList(movimientosEnemigo.size() - 1, movimientosEnemigo.size())));
                    int[]            posMiBase = mapa.getPosMiBase();
                    ArrayList<int[]> posExtras = new ArrayList<>();
                    posExtras.add(posMiBase);

                    ArrayList<String> movimientosParaInterceptarEnemigo = A_Estrella.Calcular_Adaptativo(mapa, mapa.getPosAct(), posInterceptar, CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS, CONSTANTES.M_ARRAYLIST_VACIO);
                    logger.info("{ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA} Accion a realizar: voy a la siguiente posicion a la que va a ir mi enemigo para llegar a su base (" + (movimientosParaInterceptarEnemigo.size() - 1) + ") desitno:[" + posInterceptar[0] + " " + posInterceptar[1] + "]");
                    // MUCHAS VECES EL DEFENSA SE QUEDA QUIETO POR QUE SABE QUE EL ATACANTE TIENE QUE PASAR POR DONDE ESTA, ASI QUE LO ESPERA
                    return movimientosParaInterceptarEnemigo.get(movimientosParaInterceptarEnemigo.size() - 1);
                }
                else{
                    ArrayList<String> movimientosParaLlegarBaseEnemiga = A_Estrella.Calcular_Adaptativo(mapa,mapa.getPosAct(),mapa.getPosBaseEnemiga(),CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);
                    return movimientosParaLlegarBaseEnemiga.get(movimientosParaLlegarBaseEnemiga.size()-1);
                }

            }

            case ESTADO_COGER_BANDERA:{

                ArrayList<String> movimientos = A_Estrella.Calcular_Adaptativo(mapa,mapa.getPosAct(),mapa.getPosMiBandera(),CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);
                logger.info("{ESTADO_COGER_BANDERA} Accion a realizar: voy donde esta la bandera ("+(movimientos.get(movimientos.size()-1))+")");
                return movimientos.get(movimientos.size()-1);
            }

            default: {
                logger.info("NO ENCUENTRO ACCION PARA OBJETIVO " + objetivoActual + ", HAGO ACCION NULA.");
                return CONSTANTES.ACION_NULA;
            }

        }

    }

    private int[] posElegidaParaInteceptar(int[] posObjetivo,int[] posAct) {

        ArrayList<int[]> posVecinosYmia                  = mapa.getPosVecinasYmia(posAct);
        ArrayList<int[]> posDondePuedoInterceptarEnemigo = new ArrayList<>();

        for (int i = 0; i < posVecinosYmia.size(); i++) {
            if(mapa.calcularDistancia(posVecinosYmia.get(i),posObjetivo)<=1.42) posDondePuedoInterceptarEnemigo.add(posVecinosYmia.get(i));
        }

        Random rnd = new Random();

        int posElegidaInterceptar = rnd.nextInt(posDondePuedoInterceptarEnemigo.size());

        return posDondePuedoInterceptarEnemigo.get(posElegidaInterceptar);
    }

    private boolean mismaPosicion(int[] posA, int[] posB) {
        return posA[0]==posB[0] && posA[1]==posB[1];
    }

    public String getEquipo() {
        return miEquipo;
    }

    // Estados del automata
    private static final int ESTADO_INICIAL                        = 0;
    private static final int ESTADO_PERMANECER_POSICION_DEFENSA    = 1;
    private static final int ESTADO_IR_POR_ATACANTE                = 2;
    private static final int ESTADO_PERSEGUIR_ATACANTE_CON_BANDERA = 3;
    private static final int ESTADO_COGER_BANDERA                  = 4;
    public static final int ESTADO_FINAL                           = 100;
    public static final int ESTADO_ERROR                           = -1;


    // Objetivos a cumplir
    public static final String IR_A_BASE_CONTRARIA_BANDERA  = "1";
    public static final String IR_A_BASE_PROPIA_CON_BANDERA = "2";
    public static final String IR_A_BASE_PROPIA             = "3";
    public static final String IR_A_BANDERA_CONTRARIA       = "4";
    public static final String SIN_OBJETIVO                 = "-1";

}
