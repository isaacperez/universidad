package Recursos;

import Agentes.Agente;
import jade.core.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by isaac on 31/03/16.
 */
public class Mapa {

    private int filas, columnas;
    private String miEquipo;
    private int[] posAct;
    private int[] posVentana;
    private char[][] mapaCompleto;
    private boolean[][] noHayObstaculo;
    private boolean[][] noHayNada;
    private Map<String, int[]> posiciones;
    private ArrayList<int[]> posicionesMiEquipo;
    private ArrayList<int[]> posicionesEnemigos;
    private  ArrayList<int[]> posDefensas;
    private int   contadorRespuestasPorRecibir;
    private Agent agenteLanzador;

    private ArrayList<int[]> posDefensiva; // {arr, der,abj,izq}
    private int[] posDefVecinasAnt;
    private int[] posDefVecinasPost;

    private static final Logger logger = LoggerFactory.getLogger(Mapa.class);
    private boolean escoltaRespondeOK;
    private int[] posEscolta;
    private int[] posDestinoEscolta;

    public Mapa(Agent agenteLanzador) {

        this.agenteLanzador = agenteLanzador;
        this.miEquipo = CONSTANTES.EQUIPO_DEL_AGENTE;

        this.posAct = new int[2];
        this.posVentana = new int[2];

        this.posiciones = new HashMap<>();
        this.posicionesMiEquipo = new ArrayList<>();
        this.posDefensas = new ArrayList<>(3);
        this.posDefensas.add(new int[]{-1,-1});
        this.posDefensas.add(new int[]{-1,-1});
        this.posDefensas.add(new int[]{-1,-1});
        this.contadorRespuestasPorRecibir = 0;
        this.posEscolta = new int[]{-1,-1};
        this.posDestinoEscolta = new int[]{-1,-1};
        this.escoltaRespondeOK = false;

        // Posiciones por defecto de todos los elementos que pueden estar en presentes en el mapa
        posiciones.put(CONSTANTES.BASE_BANDERA_ROJA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BASE_BANDERA_AZUL, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BASE_ROJA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BASE_AZUL, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BANDERA_ROJA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BANDERA_AZUL, new int[]{-1, -1});
        posiciones.put(CONSTANTES.ENTRADA_ROJA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.ENTRADA_AZUL, new int[]{-1, -1});
        posiciones.put(CONSTANTES.JUGADOR_ROJO, new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        posiciones.put(CONSTANTES.JUGADOR_AZUL, new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        posiciones.put(CONSTANTES.JUGADOR_ROJO_BANDERA_PROPIA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.JUGADOR_AZUL_BANDERA_PROPIA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.POSICION_ACTUAL, new int[]{-1, -1});

    }

    /**
     * Recibe el primer mensaje de la plataforma y genera el mapa.
     *
     * @param mensaje mensaje enviado por la plataforma
     */
    public void montarMapa(String mensaje) {

        int posComienzoDescripcionMapa = mensaje.indexOf("H");

        // Extraccion de de informacion
        String descripcionMapa = mensaje.substring(posComienzoDescripcionMapa);
        String caracteristicasMapa = mensaje.substring(0, posComienzoDescripcionMapa);

        obtenerVariables(caracteristicasMapa);
        obtenerDescripcion(descripcionMapa);

        Agente.recibirActualizacion(agenteLanzador);
        //crearPosicionesDefensivas();

    }

    /*
    private void crearPosicionesDefensivas() {

        int[] posBase = getPosMiBase();

        this.posDefensiva = new ArrayList<>(8);

        posDefensiva.add(new int[]{posBase[0] - 1, posBase[1] - 1});
        posDefensiva.add(new int[]{posBase[0] - 1, posBase[1]});
        posDefensiva.add(new int[]{posBase[0] - 1, posBase[1] + 1});
        posDefensiva.add(new int[]{posBase[0], posBase[1] + 1});
        posDefensiva.add(new int[]{posBase[0] + 1, posBase[1] + 1});
        posDefensiva.add(new int[]{posBase[0] + 1, posBase[1]});
        posDefensiva.add(new int[]{posBase[0] + 1, posBase[1] - 1});
        posDefensiva.add(new int[]{posBase[0], posBase[1] - 1});

        this.posDefVecinasAnt = new int[8];

        posDefVecinasAnt[0] = 7;
        posDefVecinasAnt[1] = 0;
        posDefVecinasAnt[2] = 1;
        posDefVecinasAnt[3] = 2;
        posDefVecinasAnt[4] = 3;
        posDefVecinasAnt[5] = 4;
        posDefVecinasAnt[6] = 5;
        posDefVecinasAnt[7] = 6;

        this.posDefVecinasPost = new int[8];

        posDefVecinasPost[0] = 1;
        posDefVecinasPost[1] = 2;
        posDefVecinasPost[2] = 3;
        posDefVecinasPost[3] = 4;
        posDefVecinasPost[4] = 5;
        posDefVecinasPost[5] = 6;
        posDefVecinasPost[6] = 7;
        posDefVecinasPost[7] = 0;

    }
    */
    /**
     * Actualiza las posiciones del jugador y de los elementos en el mapa
     *
     * @param mensaje mensaje recibido del servidor
     */
    public void actualizarMapa(String mensaje) {

        int posComienzoDescripcionMapa = mensaje.indexOf("H");
        this.posicionesMiEquipo = new ArrayList<>();
        this.posicionesEnemigos = new ArrayList<>();
        this.posiciones = new HashMap<>();
        // Posiciones por defecto de todos los elementos que pueden estar en presentes en el mapa
        posiciones.put(CONSTANTES.BASE_BANDERA_ROJA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BASE_BANDERA_AZUL, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BASE_ROJA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BASE_AZUL, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BANDERA_ROJA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.BANDERA_AZUL, new int[]{-1, -1});
        posiciones.put(CONSTANTES.ENTRADA_ROJA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.ENTRADA_AZUL, new int[]{-1, -1});
        posiciones.put(CONSTANTES.JUGADOR_ROJO, new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        posiciones.put(CONSTANTES.JUGADOR_AZUL, new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        posiciones.put(CONSTANTES.JUGADOR_ROJO_BANDERA_PROPIA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.JUGADOR_AZUL_BANDERA_PROPIA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA, new int[]{-1, -1});
        posiciones.put(CONSTANTES.POSICION_ACTUAL, new int[]{-1, -1});


        // Extraccion de de informacion
        String descripcionMapa = mensaje.substring(posComienzoDescripcionMapa);
        String caracteristicasMapa = mensaje.substring(0, posComienzoDescripcionMapa);

        // Tratamiento caracteristicas
        String[] caracteristica = caracteristicasMapa.split(",");

        // Actualizacion de posicion actual del jugador
        this.posAct[1] = Integer.valueOf(caracteristica[0]);
        this.posAct[0] = Integer.valueOf(caracteristica[1]);

        posiciones.put(CONSTANTES.POSICION_ACTUAL, posAct);

        // Actualizacion de elementos de interes
        for (int f = 0; f < filas; f++) {

            String filaAct = descripcionMapa.substring(f * columnas, (f * columnas) + columnas);

            for (int c = 0; c < columnas; c++) {

                char caracter = filaAct.charAt(c);

                mapaCompleto[f][c] = caracter;
                noHayObstaculo[f][c] = caracter != CONSTANTES.PARED;
                noHayNada[f][c] = caracter == CONSTANTES.VACIO;

                // Solo se almacena en mapa las paredes, los demas elementos son almacenados en la tabla hash posiciones
                if ((caracter != CONSTANTES.VACIO) && (caracter != CONSTANTES.PARED)) {

                    posiciones.get(String.valueOf(caracter))[0] = f;
                    posiciones.get(String.valueOf(caracter))[1] = c;

                    if (caracter == CONSTANTES.C_JUGADOR_AZUL || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_PROPIA){
                        if (miEquipo.equals(CONSTANTES.EQUIPO_AZUL)) posicionesMiEquipo.add(new int[]{f, c});
                        else    posicionesEnemigos.add(new int[]{f,c});
                    } else if (caracter == CONSTANTES.C_JUGADOR_ROJO || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_PROPIA){
                        if (miEquipo.equals(CONSTANTES.EQUIPO_ROJO)) posicionesMiEquipo.add(new int[]{f, c});
                        else    posicionesEnemigos.add(new int[]{f,c});
                    }

                }

            }

        }

        // Se notifica al cerebro para que sepa que se ha actualizado la informacion
        Agente.recibirActualizacion(agenteLanzador);

    }


    public Agent getAgenteLanzador(){
        return agenteLanzador;
    }
    /**
     * Obtencion de los valores de los diferentes atributos del mapa
     *
     * @param caracteristicasMapa String con la descripcion de tamaño y posicion de salida inicial
     */
    private void obtenerVariables(String caracteristicasMapa) {

        // Caracteristicas del mapa
        String[] caracteristica = caracteristicasMapa.split(",");

        // Dimensiones del mapa
        this.columnas = Integer.valueOf(caracteristica[0]);
        this.filas = Integer.valueOf(caracteristica[1]);

        // Dimensiones de la ventana
        this.posVentana[1] = Integer.valueOf(caracteristica[2]);
        this.posVentana[0] = Integer.valueOf(caracteristica[3]);

        // Posicion del agente en el mapa
        this.posAct[1] = Integer.valueOf(caracteristica[4]);
        this.posAct[0] = Integer.valueOf(caracteristica[5]);
        posiciones.put(CONSTANTES.POSICION_ACTUAL, posAct);

    }

    /**
     * Obtencion de la estructura del mapa y la posicion de cada elemento en el
     *
     * @param descripcionMapa String con dicha descripcion
     */
    private void obtenerDescripcion(String descripcionMapa) {

        // Representacion interna de las paredes que hay en el mapa
        this.noHayObstaculo = new boolean[filas][columnas];
        this.noHayNada = new boolean[filas][columnas];
        this.mapaCompleto = new char[filas][columnas];
        this.posicionesMiEquipo = new ArrayList<>();
        this.posicionesEnemigos = new ArrayList<>();

        for (int f = 0; f < filas; f++) {

            String filaAct = descripcionMapa.substring(f * columnas, (f * columnas) + columnas);

            for (int c = 0; c < columnas; c++) {

                char caracter = filaAct.charAt(c);

                mapaCompleto[f][c] = caracter;
                noHayObstaculo[f][c] = caracter != CONSTANTES.PARED;
                noHayNada[f][c] = caracter == CONSTANTES.VACIO;

                // Solo se almacena en mapa las paredes, los demas elementos son almacenados en la tabla hash posiciones
                if ((caracter != CONSTANTES.VACIO) && (caracter != CONSTANTES.PARED)) {

                    posiciones.get(String.valueOf(caracter))[0] = f;
                    posiciones.get(String.valueOf(caracter))[1] = c;

                    if (caracter == CONSTANTES.C_JUGADOR_AZUL || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_PROPIA){
                        if (miEquipo.equals(CONSTANTES.EQUIPO_AZUL)) posicionesMiEquipo.add(new int[]{f, c});
                        else    posicionesEnemigos.add(new int[]{f,c});
                    } else if (caracter == CONSTANTES.C_JUGADOR_ROJO || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_PROPIA){
                        if (miEquipo.equals(CONSTANTES.EQUIPO_ROJO)) posicionesMiEquipo.add(new int[]{f, c});
                        else    posicionesEnemigos.add(new int[]{f,c});
                    }

                }

            }

        }

    }

    /**
     * Devuelve la posicion en el mapa del elemento indicado
     *
     * @param elemento elemento del que se desea conocer su posicion en el mapa
     * @return array de dos dimensiones (fila,columna) con la posicion del parametro en el mapa
     */
    public int[] getPosElemento(String elemento) {

        return posiciones.get(elemento);
    }

    public int[] getPosAct() {
        return posAct;
    }

    public int getTam() {
        return filas * columnas;
    }

    public boolean[][] getMapaObstaculos(int mapaBase, ArrayList<int[]> posicionesExtraAIntroducir, int[] posMeta) {

        boolean[][] mapaTemp;

        mapaTemp = getMapaBase(mapaBase);
        mapaTemp = addNuevasPosiciones(mapaTemp, posicionesExtraAIntroducir);

        mapaTemp[posMeta[0]][posMeta[1]] = true;
        return mapaTemp;

    }

    private boolean[][] getMapaBase(int mapaBase) {
        switch (mapaBase) {
            case CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS: {
                boolean[][] mapaElegido = noHayNada.clone();
                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        if(mapaCompleto[i][j]==CONSTANTES.C_BASE_ROJA || mapaCompleto[i][j]==CONSTANTES.C_BASE_AZUL) mapaElegido[i][j] = true;
                    }
                }
                return mapaElegido.clone();
            }
            case CONSTANTES.M_ADD_NINGUNO: return noHayObstaculo.clone();
        }
        return null;
    }

    private boolean[][] addNuevasPosiciones(boolean[][] mapaTemp, ArrayList<int[]> posicionesExtraAIntroducir) {
        for (int[] elemento : posicionesExtraAIntroducir) {
            mapaTemp[elemento[0]][elemento[1]] = true;
        }
        return mapaTemp;
    }

    public String toString() {

        String s = "";


        s += "Sitios donde no hay obstaculos: \n";
        for (int i = 0; i < noHayObstaculo.length; i++) {
            for (int j = 0; j < noHayObstaculo[0].length; j++) {
               /* if (noHayObstaculo[i][j]) s += " ";
                else s += "H";*/
                s += mapaCompleto[i][j];
            }
            s = s + "\n";
        }

        s += "Posicion elementos de interes:\n";

        s += "BASE_R_CON_BANDERA_R -> (" + posiciones.get("A")[0] + ", " + posiciones.get("A")[1] + ")\n";
        s += "BASE_A_CON_BANDERA_A -> (" + posiciones.get("B")[0] + ", " + posiciones.get("B")[1] + ")\n";
        s += "BASE_R -> (" + posiciones.get("C")[0] + ", " + posiciones.get("C")[1] + ")\n";
        s += "BASE_A -> (" + posiciones.get("D")[0] + ", " + posiciones.get("D")[1] + ")\n";
        s += "BANDERA_R -> (" + posiciones.get("E")[0] + ", " + posiciones.get("E")[1] + ")\n";
        s += "BANDERA_A -> (" + posiciones.get("F")[0] + ", " + posiciones.get("F")[1] + ")\n";
        s += "ENTRADA_A -> (" + posiciones.get("V")[0] + ", " + posiciones.get("V")[1] + ")\n";
        s += "ENTRADA_R -> (" + posiciones.get("W")[0] + ", " + posiciones.get("W")[1] + ")\n";
        s += "JUGADOR_R -> (" + posiciones.get("1")[0] + ", " + posiciones.get("1")[1] + ")" +
                " (" + posiciones.get("1")[2] + ", " + posiciones.get("1")[3] + ")" +
                " (" + posiciones.get("1")[4] + ", " + posiciones.get("1")[5] + ")" +
                " (" + posiciones.get("1")[6] + ", " + posiciones.get("1")[7] + ")" +
                " (" + posiciones.get("1")[8] + ", " + posiciones.get("1")[9] + ")\n";
        s += "JUGADOR_A -> (" + posiciones.get("2")[0] + ", " + posiciones.get("2")[1] + ")" +
                " (" + posiciones.get("2")[2] + ", " + posiciones.get("2")[3] + ")" +
                " (" + posiciones.get("2")[4] + ", " + posiciones.get("2")[5] + ")" +
                " (" + posiciones.get("2")[6] + ", " + posiciones.get("2")[7] + ")" +
                " (" + posiciones.get("2")[8] + ", " + posiciones.get("2")[9] + ")\n";
        s += "JUGADOR_R_BANDERA_PROPIA -> (" + posiciones.get("3")[0] + ", " + posiciones.get("3")[1] + ")\n";
        s += "JUGADOR_A_BANDERA_PROPIA -> (" + posiciones.get("4")[0] + ", " + posiciones.get("4")[1] + ")\n";
        s += "JUGADOR_R_BANDERA_CONTRARA -> (" + posiciones.get("5")[0] + ", " + posiciones.get("5")[1] + ")\n";
        s += "JUGADOR_A_BANDERA_CONTRARA -> (" + posiciones.get("6")[0] + ", " + posiciones.get("6")[1] + ")\n";


        s += "\nfilas: " + filas + ", columnas: " + columnas + "\n";
        s += "filasVentana:" + posVentana[0] + ", columnasVentana: " + posVentana[1] + "\n";
        s += "Posicion actual: " + posAct[0] + " " + posAct[1];
        return s;
    }

    public int[] buscarPosicionDondeDefender() {

        return mejorCasillaEntornoDefensa();
    }

    private int[] mejorCasillaDefensivaVecinos() {
        int minF = posAct[0]-1;
        int maxF = posAct[0]+1;
        int minC = posAct[1]-1;
        int maxC = posAct[1]+1;

        int[]  posMejorCasilla        = new int[]{-1,-1};
        double heuristicaMejorCasilla = Double.MAX_VALUE;

        for (int i = Math.max(0,minF); i < filas && i <= maxF; i++) {
            for (int j = Math.max(0, minC); j < columnas && j <= maxC; j++) {
                double heuristicaCasillaActual = calcularHeuristicaDefensiva(i,j);
                if((mapaCompleto[i][j]==CONSTANTES.VACIO || (posAct[0]==i && posAct[1]==j)) && esAccesible(i,j) && heuristicaCasillaActual<heuristicaMejorCasilla){
                    heuristicaMejorCasilla = heuristicaCasillaActual;
                    posMejorCasilla[0] = i;
                    posMejorCasilla[1] = j;
                }
            }
        }

        return posMejorCasilla;

    }

    private int[] mejorCasillaEntornoDefensa() {

        logger.info("Posiciones defensas:"+posDefensas.get(0)[0] + " "+posDefensas.get(0)[1]+", "+posDefensas.get(1)[0] + " "+posDefensas.get(1)[1]+", "+posDefensas.get(2)[0] + " "+posDefensas.get(2)[1]);
        int[] posBase = getPosMiBandera();
        int minF = posBase[0]-CONSTANTES.ENTORNO_DEFENSIVO;
        int maxF = posBase[0]+CONSTANTES.ENTORNO_DEFENSIVO;
        int minC = posBase[1]-CONSTANTES.ENTORNO_DEFENSIVO;
        int maxC = posBase[1]+CONSTANTES.ENTORNO_DEFENSIVO;

        int[]  posMejorCasilla        = new int[]{-1,-1};
        double heuristicaMejorCasilla = Double.MAX_VALUE;

        ArrayList<int[]> posPasaPortador = getPosPasaPortador();

        for (int i = Math.max(0,minF); i < filas && i <= maxF; i++) {
            for (int j = Math.max(0, minC); j < columnas && j <= maxC; j++) {
                if(!estaCasillaPasaPortadorBanderaEnemiga(i,j,posPasaPortador) && calcularDistancia(posBase,new int[]{i,j})>CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION){
                    double heuristicaCasillaActual = calcularHeuristicaDefensiva(i,j);
                    if((mapaCompleto[i][j]==CONSTANTES.VACIO || (posAct[0]==i && posAct[1]==j)) && esAccesible(i,j) && heuristicaCasillaActual<heuristicaMejorCasilla) {
                        heuristicaMejorCasilla = heuristicaCasillaActual;
                        posMejorCasilla[0] = i;
                        posMejorCasilla[1] = j;
                    }
                }
            }
        }

        return posMejorCasilla;
    }

    private boolean estaCasillaPasaPortadorBanderaEnemiga(int i, int j, ArrayList<int[]> posPasaPortador) {
        for (int[] pos:posPasaPortador) {
            if(pos[0]==i && pos[1]==j) return true;
        }
        return false;
    }

    private boolean esAccesible(int a, int b) {

        for (int i = a-1; i <= (a+1); i++) {
            for (int j = b - 1; j <=(b + 1);j++){
                if ((a!=i || b!=j) && mapaCompleto[i][j] == CONSTANTES.VACIO) return true;
            }
        }
        return false;
    }

    private double calcularHeuristicaDefensiva(int i, int j) {

        int[]  posCasilla = new int[]{i,j};
        double distancia  = 1000;

        for (int[] pos:posDefensas) {
            if(pos[0]!=-1 && (pos[0]!=posAct[0] || pos[1]!=posAct[1]) && estaEnElEntornoDeDefensa(pos))distancia -= calcularDistancia(posCasilla,pos);
        }

        distancia += (3*calcularDistancia(posCasilla,getPosMiBandera()));

        return distancia;
    }

    private boolean estoyEnElEntornoDeDefensa() {
        int[] posBase = getPosMiBase();
        return (Math.abs(posBase[0]-posAct[0])<=CONSTANTES.ENTORNO_DEFENSIVO) && (Math.abs(posBase[1]-posAct[1])<=CONSTANTES.ENTORNO_DEFENSIVO);
    }

    private boolean estaEnElEntornoDeDefensa(int[] pos) {
        int[] posBase = getPosMiBase();
        return (Math.abs(posBase[0]-pos[0])<=CONSTANTES.ENTORNO_DEFENSIVO) && (Math.abs(posBase[1]-pos[1])<=CONSTANTES.ENTORNO_DEFENSIVO);
    }

    public double calcularDistancia(int[] posInicial, int[] posDestino) {
        return Math.sqrt(Math.pow(posInicial[0] - posDestino[0], 2) + Math.pow(posInicial[1] - posDestino[1], 2));
    }

    private int[] getPosSalidaEnemigo() {
        int[] pos;
        if (miEquipo.equals(CONSTANTES.EQUIPO_AZUL)) {
            pos =  posiciones.get(CONSTANTES.ENTRADA_ROJA);
            if(pos[0]==-1) pos = posiciones.get(CONSTANTES.BASE_ROJA);
            if(pos[0]==-1) pos = posiciones.get(CONSTANTES.BASE_BANDERA_ROJA);
        }
        else{
            pos = posiciones.get(CONSTANTES.ENTRADA_AZUL);
            if(pos[0]==-1) pos = posiciones.get(CONSTANTES.BASE_AZUL);
            if(pos[0]==-1) pos = posiciones.get(CONSTANTES.BASE_BANDERA_AZUL);
        }

        return pos;
    }

    public int[] getPosMiBase() {

        int[] base;

        if (miEquipo.equals(CONSTANTES.EQUIPO_AZUL)) {
            base = posiciones.get(CONSTANTES.BASE_AZUL);
            if (base[0] == -1) base = posiciones.get(CONSTANTES.BASE_BANDERA_AZUL);
            if(base[0]==-1){
                // Mi base esta donde esta el jugador contrario con mi bandera
                if(calcularDistancia(posiciones.get(CONSTANTES.BANDERA_ROJA),posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA))<CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION){
                    base = posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA);
                }
            }

        } else {
            base = posiciones.get(CONSTANTES.BASE_ROJA);
            if (base[0] == -1) base = posiciones.get(CONSTANTES.BASE_BANDERA_ROJA);
            if(base[0]==-1){
                // Mi base esta donde esta el jugador contrario con mi bandera
                if(calcularDistancia(posiciones.get(CONSTANTES.BANDERA_AZUL),posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA))<CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION){
                    base = posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA);
                }

            }
        }

        return base;

    }

    public int[] quienVaPor(int[] posAtacante) {

        /*getDefensas();*/

        int[]  elegido        =  new int[]{-1,-1};
        double menorDistancia = Double.MAX_VALUE;

        for (int[] pos:posicionesMiEquipo) {
            double distanciaAct = calcularDistancia(pos,posAtacante);
            if(esDefensa(pos) && (distanciaAct<menorDistancia)){
                elegido = pos;
                menorDistancia = distanciaAct;
            }
        }

        return elegido;

    }

    private boolean esDefensa(int[] pos) {
        // IMPORTANTE HACER QUE SE ENVIEN MENSAJES PARA VER QUIEN ES DEFENSA

        return true;
    }

    public ArrayList<int[]> getAtacantesEnEntornoDefensivoYCercaDeMi() {

        int[] posBase = getPosMiBase();
        int minF = posBase[0]-(CONSTANTES.ENTORNO_DEFENSIVO+2);
        int maxF = posBase[0]+(CONSTANTES.ENTORNO_DEFENSIVO+2);
        int minC = posBase[1]-(CONSTANTES.ENTORNO_DEFENSIVO+2);
        int maxC = posBase[1]+(CONSTANTES.ENTORNO_DEFENSIVO+2);

        int minF2 = posAct[0]-(3);
        int maxF2 = posAct[0]+(3);
        int minC2 = posAct[1]-(3);
        int maxC2 = posAct[1]+(3);

        ArrayList<int[]> atacantesEnEntornoDefensivo = new ArrayList<>();

        if(miEquipo.equals(CONSTANTES.EQUIPO_AZUL)) {

            for (int i = Math.max(0,minF); i < filas && i < maxF; i++) {
                for (int j = Math.max(0,minC); j < columnas && j < maxC; j++) {
                    char caracter = mapaCompleto[i][j];
                    if (caracter == CONSTANTES.C_JUGADOR_ROJO || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_PROPIA) atacantesEnEntornoDefensivo.add(new int[]{i,j});
                }
            }

            for (int i = Math.max(0,minF2); i < filas && i < maxF2; i++) {
                for (int j = Math.max(0, minC2); j < columnas && j < maxC2; j++) {
                    char caracter = mapaCompleto[i][j];
                    if (caracter == CONSTANTES.C_JUGADOR_ROJO || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_PROPIA) atacantesEnEntornoDefensivo.add(new int[]{i,j});
                }
            }

        }
        else{

            for (int i =  Math.max(0,minF); i < filas && i < maxF; i++) {
                for (int j = Math.max(0,minC); j < columnas && j < maxC; j++) {
                    char caracter = mapaCompleto[i][j];
                    if (caracter == CONSTANTES.C_JUGADOR_AZUL || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_PROPIA) atacantesEnEntornoDefensivo.add(new int[]{i,j});
                }
            }

            for (int i = Math.max(0,minF2); i < filas && i < maxF2; i++) {
                for (int j = Math.max(0, minC2); j < columnas && j < maxC2; j++) {
                    char caracter = mapaCompleto[i][j];
                    if (caracter == CONSTANTES.C_JUGADOR_AZUL || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_PROPIA) atacantesEnEntornoDefensivo.add(new int[]{i,j});
                }
            }

        }

        return atacantesEnEntornoDefensivo;
    }

    public int[] getPosMiBandera() {

        int[] posMiBandera;

        if(miEquipo.equals(CONSTANTES.EQUIPO_AZUL)){
            posMiBandera = posiciones.get(CONSTANTES.BANDERA_AZUL);
            if(posMiBandera[0]==-1) posMiBandera = posiciones.get(CONSTANTES.BASE_BANDERA_AZUL);
            if(posMiBandera[0]==-1) posMiBandera = posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA);
            else if(posMiBandera[0]==-1) posMiBandera = posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_PROPIA);
        }
        else{
            posMiBandera = posiciones.get(CONSTANTES.BANDERA_ROJA);
            if(posMiBandera[0]==-1) posMiBandera = posiciones.get(CONSTANTES.BASE_BANDERA_ROJA);
            if(posMiBandera[0]==-1) posMiBandera = posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA);
            else if(posMiBandera[0]==-1) posMiBandera = posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_PROPIA);
        }

        return posMiBandera;
    }

    public ArrayList<int[]> getPosVecinasYmia(int[] posAct) {

        ArrayList<int[]> posVecinos = new ArrayList<>();

        int f = posAct[0];
        int c = posAct[1];

        int[] movimientos         = new int[]{f-1,c-1,f-1,c,f-1,c+1,f,c-1,f,c+1,f+1,c-1,f+1,c,f+1,c+1,f,c};

        for (int i = 0; i < 18; i+=2) {
            if(mapaCompleto[movimientos[i]][movimientos[i+1]]!=CONSTANTES.PARED){
                posVecinos.add(new int[]{movimientos[i],movimientos[i+1]});
            }
        }

        return posVecinos;

    }

    public int[] atacanteConMiBandera() {
        if(miEquipo.equals(CONSTANTES.EQUIPO_AZUL)){
            return posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA);
        }
        else return posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA);
    }

    public int[] getPosBaseEnemiga() {

        int[] baseEnemiga;

        if (miEquipo.equals(CONSTANTES.EQUIPO_AZUL)) {
            baseEnemiga = posiciones.get(CONSTANTES.BASE_ROJA);
            if (baseEnemiga[0] == -1) baseEnemiga = posiciones.get(CONSTANTES.BASE_BANDERA_ROJA);
        } else {
            baseEnemiga = posiciones.get(CONSTANTES.BASE_AZUL);
            if (baseEnemiga[0] == -1) baseEnemiga = posiciones.get(CONSTANTES.BASE_BANDERA_AZUL);
        }

        return baseEnemiga;
    }

    public ArrayList<int[]> getPosEnemigos() {
        return posicionesEnemigos;
    }

    public boolean miBanderaNoEstaEnLaBase() {
        if (miEquipo.equals(CONSTANTES.EQUIPO_AZUL)) return posiciones.get(CONSTANTES.BASE_BANDERA_AZUL)[0]==-1;
        else return posiciones.get(CONSTANTES.BASE_BANDERA_ROJA)[0]==-1;
    }

    public void defensorContestaSuPosicion(int[] posDefensa) {
       if(contadorRespuestasPorRecibir!=-1) {
           contadorRespuestasPorRecibir--;
           posDefensas.add(posDefensa);
       }
    }

    public boolean puedoResponderAlServidor() {
        if(contadorRespuestasPorRecibir==0){
            contadorRespuestasPorRecibir = -1;
            return true;
        }
        else return false;
    }

    public void prepararseParaRecibirPosDefensivas() {
        this.contadorRespuestasPorRecibir = 3;
        this.posDefensas = new ArrayList<>();
    }

    /*
        IMPLEMENTAR TODOS LOS METODOS DE AQUI ABAJO
     */

    public boolean banderaEnemigaEnMiBase() {

        int[] posMiBase = getPosMiBase();

        char miEquipoConBandera;
        if(miEquipo.equals(CONSTANTES.EQUIPO_ROJO)) miEquipoConBandera = CONSTANTES.C_JUGADOR_ROJO_BANDERA_CONTRARIA;
        else                                        miEquipoConBandera = CONSTANTES.C_JUGADOR_AZUL_BANDERA_CONTRARIA;


        for (int f = posMiBase[0]-1; f <= (posMiBase[0]+1); f++) {
            for (int c = posMiBase[1]-1; c<= (posMiBase[1]+1); c++) {
                if(mapaCompleto[f][c]==miEquipoConBandera && (f!=posAct[0] || c!=posAct[1]))   return true;
            }
        }

        return false;
    }

    public int[] miEquipoTieneBanderaEnemiga() {
        if(miEquipo.equals(CONSTANTES.EQUIPO_ROJO)) return posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA);
        else                                        return posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA);
    }


    public ArrayList<int[]> enemigoEnMiCaminoBanderaEnemiga(int[] posDondeDeboIr) {

        ArrayList<int[]> enemigosEnMiCamino = new ArrayList<>();

        if(miEquipo.equals(CONSTANTES.EQUIPO_ROJO)){

            for (int f = Math.max(posAct[0]-2,0); f <= (posAct[0]+2) && f<filas; f++) {
                for (int c = Math.max(posAct[1]-2,0); c<= (posAct[1]+2) && c<columnas; c++) {
                    char caracter = mapaCompleto[f][c];
                    if (caracter == CONSTANTES.C_JUGADOR_AZUL || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_PROPIA) {
                        if(calcularDistancia(posDondeDeboIr,new int[]{f,c})<=CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION) enemigosEnMiCamino.add(new int[]{f,c});
                    }

                }
            }

        }
        else{

            for (int f = Math.max(posAct[0]-2,0); f <= (posAct[0]+2) && f<filas; f++) {
                for (int c = Math.max(posAct[1]-2,0); c<= (posAct[1]+2) && c<columnas; c++) {
                    char caracter = mapaCompleto[f][c];
                    if (caracter == CONSTANTES.C_JUGADOR_ROJO || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_PROPIA){
                        if(calcularDistancia(posDondeDeboIr,new int[]{f,c})<=CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION) enemigosEnMiCamino.add(new int[]{f,c});
                    }

                }
            }

        }

        return enemigosEnMiCamino;

    }

    public int[] getPosAccesibleBanderaEnemiga() {

        int[] posBanderaEnemiga;

        if(miEquipo.equals(CONSTANTES.EQUIPO_ROJO)) {

            // Si el enemigo la esta portando se dirige a mi base, veo a que posicion va a ir en el siguiente paso y la devuelvo como posicion a la que debo ir
            if(posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_PROPIA)[0]!=-1){
                ArrayList<String> movimientosEnemigo = A_Estrella.Calcular_Adaptativo(this,posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_PROPIA),getPosMiBase(),CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);
                return Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_PROPIA),new ArrayList<>(movimientosEnemigo.subList(movimientosEnemigo.size()-1,movimientosEnemigo.size())));
            }
            else{

                posBanderaEnemiga = posiciones.get(CONSTANTES.BANDERA_AZUL);
                if(posBanderaEnemiga[0]==-1) posBanderaEnemiga = posiciones.get(CONSTANTES.BASE_BANDERA_AZUL);
                // Si la bandera no la lleva uno de los del equipo contrario, ni esta en algun sitio tirada, ni esta en su base desconozco que pasa, devuelvo posAct
                if(posBanderaEnemiga[0]==-1) posBanderaEnemiga = posAct;

                if(!esAccesible(posBanderaEnemiga[0],posBanderaEnemiga[1])) {

                    int [] posMejor       = posBanderaEnemiga.clone();
                    double menorDistancia = Double.MAX_VALUE;

                    for (int f = posBanderaEnemiga[0]-1; f <= (posBanderaEnemiga[0]+1); f++) {
                        for (int c = posBanderaEnemiga[1]-1; c<= (posBanderaEnemiga[1]+1); c++) {

                            char caracter = mapaCompleto[f][c];
                            if(caracter!=CONSTANTES.PARED){

                                double distancia  = calcularDistancia(posAct,posMejor);
                                if (distancia < menorDistancia){
                                    posMejor[0] = f;
                                    posMejor[1] = c;
                                    menorDistancia = distancia;
                                }

                            }

                        }
                    }

                    posBanderaEnemiga = posMejor;

                }

            }
        }
        else {

            // Si el enemigo la esta portando se dirige a mi base, veo a que posicion va a ir en el siguiente paso y la devuelvo como posicion a la que debo ir
            if(posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_PROPIA)[0]!=-1){
                ArrayList<String> movimientosEnemigo = A_Estrella.Calcular_Adaptativo(this,posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_PROPIA),getPosMiBase(),CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);
                return Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_PROPIA),new ArrayList<>(movimientosEnemigo.subList(movimientosEnemigo.size()-1,movimientosEnemigo.size())));
            }
            else{

                posBanderaEnemiga = posiciones.get(CONSTANTES.BANDERA_ROJA);
                if(posBanderaEnemiga[0]==-1) posBanderaEnemiga = posiciones.get(CONSTANTES.BASE_BANDERA_ROJA);
                // Si la bandera no la lleva uno de los del equipo contrario, ni esta en algun sitio tirada, ni esta en su base desconozco que pasa, devuelvo posAct
                if(posBanderaEnemiga[0]==-1) posBanderaEnemiga = posAct;

                if(!esAccesible(posBanderaEnemiga[0],posBanderaEnemiga[1])) {

                    int [] posMejor       = posBanderaEnemiga.clone();
                    double menorDistancia = Double.MAX_VALUE;

                    for (int f = posBanderaEnemiga[0]-1; f <= (posBanderaEnemiga[0]+1); f++) {
                        for (int c = posBanderaEnemiga[1]-1; c<= (posBanderaEnemiga[1]+1); c++) {

                            char caracter = mapaCompleto[f][c];
                            if(caracter!=CONSTANTES.PARED){

                                double distancia  = calcularDistancia(posAct,posMejor);
                                if (distancia < menorDistancia){
                                    posMejor[0] = f;
                                    posMejor[1] = c;
                                    menorDistancia = distancia;
                                }

                            }

                        }
                    }

                    posBanderaEnemiga = posMejor;

                }

            }

        }

        return posBanderaEnemiga;

    }



    public int[] esquivarEnemigoEnMiCamino(ArrayList<int[]> posEnemigosEsquivar, int[] posSiguienteHaciaObjetivo, int[] posObjetivoFinal) {


        int[] posParaEsquivar = new int[]{-1,-1};

            // Miramos si hay enemigos en mi entorno 1 en las posiciones que me puedan alcanzar a donde voy, si es asi, me dirijo a la posicion donde este el enemigo
            for (int[] pos:posEnemigosEsquivar) {
                if(calcularDistancia(pos,posSiguienteHaciaObjetivo)<CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION && calcularDistancia(posAct,pos)<CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION){
                    posParaEsquivar[0] = pos[0];
                    posParaEsquivar[1] = pos[1];
                }
            }

        if(posParaEsquivar[0]==-1) {

            double minNumEnemigos = 10;
            // Si pueden alcanzar a donde voy pero estan en mi entorno 2 hago un movimiento aleatorio
            for (int f = posSiguienteHaciaObjetivo[0] - 1; f <= (posSiguienteHaciaObjetivo[0] + 1); f++) {
                for (int c = posSiguienteHaciaObjetivo[1] - 1; c <= (posSiguienteHaciaObjetivo[1] + 1); c++) {

                    char caracter = mapaCompleto[f][c];
                    if((f!=posSiguienteHaciaObjetivo[0] || c!=posSiguienteHaciaObjetivo[1]) && caracter!=CONSTANTES.PARED && (f!=posAct[0] || c!=posAct[1])) {

                        // Compruebo si puedo acceder a esta posicion de un paso
                        if (calcularDistancia(posAct, new int[]{f, c}) < CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION) {

                            // Cuento el numero de enemigos que tendria en mi entorno si me dirijo a esta casilla
                            double numEnemigos = 0;
                            for (int[] posEnemigo:posEnemigosEsquivar) {
                                if(calcularDistancia(posEnemigo, new int[]{f, c})< CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION) numEnemigos++;
                            }

                            if(numEnemigos<minNumEnemigos) {
                                minNumEnemigos = numEnemigos;
                                posParaEsquivar[0] = f;
                                posParaEsquivar[1] = c;
                            }

                        }
                    }

                }
            }

        }

        // Si no hay ningun enemigo en vecindad 1 en mi camino ni puedo ir a ninguna posicion de al lado de mi objetivo voy a donde iba con una prob del 5% sino me quedo quieto a ver si se me acerca
        if(posParaEsquivar[0]==-1){
            Random rnd = new Random();
            if(rnd.nextDouble()<0.05) return posSiguienteHaciaObjetivo;
            else return posAct;
        }
        else return posParaEsquivar;

        // SI TENGO UN ENEMIGO A UNA POSICION DE DISTANCIA IR A ESTA POSICION SOLO SI EN ESTA POSICION LA DISTANCIA HACIA MI OBJETIVO ES MENOR IGUAL QUE DESDE MI POSICION ACTUAL( SIGNIFICA QUE EL ENEMIGO ESTA ENTRE MI Y EL OBJETIVO)
        // SINO EXISTE ENENMIGO CON LA CONDICION ANTERIOR => // COMPROBAR QUE EN ESTA POSICION NO ME ALCANZA A MATARME
            // IR A LA POSICION QUE NO ME ALCANCE CON MENOS DISTANCIA AL OBJETIVO


        // SI EL ENEMIGO ESTA EN TODAS LAS CASILLAS QUE ME PERMITE AVANZAR, IR A UNA CASILLA QUE ME PERMITA RETROCEDER
            // SI TANTO POR DELANTE COMO POR DETRAS ESTOY ACORRALADO INTENTAR IR HACIA DELANTE YENDO A LA POSICION DEL ENEMIGO
        // PARA TODOS LOS ENEMIGOS EN MI VECINDAD ELEGIR EL MAS CERCANO A LA BANDERA E IR A SU POSICION ACTUAL CON LA IDEA DE QUE EL SE VA A MOVER SIGUIENDO ALGUN RAZONAMIENTO
        // SE SUPONE QUE UNA VEZ EN SU POSICION LO HABREMOS DEJADO ATRAS Y PODREMOS SEGUIR HACIA LA BANDERA SINO VOLVEREMOS A HACER LO MISMO
        // DEJAR PROGRAMADO PARA QUE DE FORMA ALEATORIA CON UN 25 % DE PROB SI PUEDE IR A UNA CASILLA DE SU VECINDAD DONDE EL ENEMIGO NO LE ALCANCE PUEDA IR CON DICHA PROBABILIDAD
    }

    public boolean haTerminadoElJuego() {

        if(posiciones.get(CONSTANTES.BASE_BANDERA_AZUL)[0]!=-1 && posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA)[0]!=-1 && calcularDistancia(posiciones.get(CONSTANTES.BASE_BANDERA_AZUL),posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA))<CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION) return true;
        return posiciones.get(CONSTANTES.BASE_BANDERA_ROJA)[0] != -1 && posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA)[0] != -1 && calcularDistancia(posiciones.get(CONSTANTES.BASE_BANDERA_ROJA), posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA)) < CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION;

    }

    public void respuestaEscolta(String msn) {

        int    posSeparador = msn.indexOf(" ");

        String fila = msn.substring(1,posSeparador);
        String col  = msn.substring(posSeparador+1,msn.length());

        this.posEscolta = new int[]{Integer.valueOf(fila),Integer.valueOf(col)};
        this.escoltaRespondeOK = posEscolta[0]!=-1;
    }

    public int[] esquivarEnemigoEnMiCaminoConEscolta(ArrayList<int[]> enemigosEnMiCamino, int[] posSiguiente, int[] posMiBase) {
        // Si mi escolta esta a mas de 1 posicion de mi mandarlo que vaya a donde estoy y yo ir a la posicion hacia atras que me aleje de los enemigos
        // sin que sea una donde vaya a estar mi escolta

        boolean enemigoMasDeUnaCasilla = true;
        boolean escoltaMasDeDosCasilla = (calcularDistancia(posEscolta,posAct)>CONSTANTES.ESTA_A_DOS_ACCIONES_DE_LA_POSICION);

        for (int[] posEnemigo: enemigosEnMiCamino) {
            if(calcularDistancia(posEnemigo,posAct)<CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION) enemigoMasDeUnaCasilla = false;
        }

        // Si el enemigo esta a mas de una casilla y mi defensor esta a mas de una de mi => esperar a que se acerque a mi para intercambiarnos
        if(enemigoMasDeUnaCasilla && escoltaMasDeDosCasilla) return posAct;
        // Si mi escolta esta a una casilla de mi => me intercambio con el
        else if(!escoltaMasDeDosCasilla) return posEscolta;
        // Si mi escolta no esta a una casilla de mi tengo que moverme para que no me coja el defensor que tengo cerca
        else{

            int[] posElegida = posAct.clone();

            for (int f = posAct[0] - 1; f <= (posAct[0] + 1); f++) {
                for (int c = posAct[1] - 1; c <= (posAct[1] + 1); c++) {

                    char caracter = mapaCompleto[f][c];
                    if(caracter!=CONSTANTES.PARED)
                    {
                        // Cuento el numero de enemigos que tendria en mi entorno si me dirijo a esta casilla
                        double numEnemigos = 0;
                        for (int[] posEnemigo:enemigosEnMiCamino) {
                            if(calcularDistancia(posEnemigo, new int[]{f, c})< CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION) numEnemigos++;
                        }

                        if(numEnemigos==0){
                            posElegida[0] = f;
                            posElegida[1] = c;
                        }
                    }

                }
            }

            return posElegida;

        }

    }

    public boolean escoltaRespondeOK() {
        return escoltaRespondeOK;
    }

    public void portadorIndicaPos(int[] posDestinoEscolta) {
        this.posDestinoEscolta = posDestinoEscolta;
    }

    public int[] getPosDestinoEscolta(){
        return posDestinoEscolta;
    }

    public ArrayList<int[]> getPosicionesMiEquipo() {
        return posicionesMiEquipo;
    }

    public boolean estoyEnZonaDefensiva() {

        int[] posBase = getPosMiBase();

        int difF = Math.abs(posBase[0]-posAct[0]);
        int difC = Math.abs(posBase[1]-posAct[1]);

        return (difF<5 && difC<5);

    }

    public int[] damePosZonaDefensivaEscolta() {

        ArrayList<int[]> enemigosEnMiEntorno = enemigoEnMiCaminoBanderaEnemiga(posAct);
        int[] posBase = getPosMiBase();

        if(enemigosEnMiEntorno.size()==0) {
            int[] posBaseEnemiga = getPosBaseEnemiga();
            int[] posDondeDefender;

            ArrayList<String> movEnemigo = A_Estrella.Calcular_Adaptativo(this, posBaseEnemiga, posBase, CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS, CONSTANTES.M_ARRAYLIST_VACIO);

            if(movEnemigo.size()<2) posDondeDefender = posAct;
            else if (movEnemigo.size() < 4)
                posDondeDefender = Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(posBaseEnemiga, movEnemigo);
            else
                posDondeDefender = Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(posBaseEnemiga, new ArrayList<>(movEnemigo.subList(4, movEnemigo.size())));


            for (int f = Math.max(posDondeDefender[0] - 1, 0); f <= (posDondeDefender[0] + 1) && f < filas; f++) {
                for (int c = Math.max(posDondeDefender[1] - 1, 0); c <= (posDondeDefender[1] + 1) && c < columnas; c++) {
                    char caracter = mapaCompleto[f][c];
                    if (caracter != CONSTANTES.PARED && posDondeDefender[0]==-1) {
                        posDondeDefender = new int[]{f, c};
                    }
                }
            }

            if(posDondeDefender[0]==-1) return posAct;
            else return posDondeDefender;
        }
        else{
            // Ir por el enemigo
            ArrayList<String> movimientosEnemigo = A_Estrella.Calcular_Adaptativo(this,enemigosEnMiEntorno.get(0),posBase,CONSTANTES.M_ADD_TODOS_LOS_ELEMENTOS,CONSTANTES.M_ARRAYLIST_VACIO);
            return Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(enemigosEnMiEntorno.get(0), new ArrayList<>(movimientosEnemigo.subList(movimientosEnemigo.size() - 1, movimientosEnemigo.size())));
        }
    }

    public int[] darPosicionEscolta() {
        if(estoyEnZonaDefensiva()){
            logger.info("Le indico al escolta que vaya a [ -1, -1]");
            return new int[]{-1,-1};
        }
        else{
            logger.info("Le indico al escolta que vaya a ["+posAct[0]+" "+posAct[1]+"]");
            return posAct;
        }
    }

    public int[] getPosEscolta() {
        return posEscolta;
    }

    public boolean portadorADosCasillas() {

        int[] posPortador = miEquipoTieneBanderaEnemiga();

        int difF = Math.abs(posPortador[0]-posAct[0]);
        int difC = Math.abs(posPortador[1]-posAct[1]);

        return (calcularDistancia( posPortador,posAct)>CONSTANTES.ESTA_A_UNA_ACCION_DE_LA_POSICION) && (difF<=2 && difC<=2);
    }

    public ArrayList<int[]> getPosPasaPortador() {

        int[] posPortador;

        if(miEquipo.equals(CONSTANTES.EQUIPO_AZUL)) {
            posPortador = posiciones.get(CONSTANTES.JUGADOR_AZUL_BANDERA_CONTRARIA);
            if(posPortador[0]==-1) posPortador = getPosBaseEnemiga();
        }
        else{
            posPortador = posiciones.get(CONSTANTES.JUGADOR_ROJO_BANDERA_CONTRARIA);
            if(posPortador[0]==-1) posPortador = getPosBaseEnemiga();
        }

        ArrayList<int[]> posPasaPortador = new ArrayList<>();

        if(posPortador[0]!=-1){

            int[]            posMiBase  = getPosMiBase();

            logger.error("PASILLO: ["+posPortador[0]+" "+posPortador[1]+"] ->["+posMiBase[0]+" "+posMiBase[1]+"]");
            ArrayList<String> movimientos = A_Estrella.Calcular(this,posPortador, posMiBase,CONSTANTES.M_ADD_NINGUNO,CONSTANTES.M_ARRAYLIST_VACIO);

            // Añado los 10 ultimos movimientos o si hay menos los que haya al array posPortador.
            for (int i = 0; i < movimientos.size() && i<40; i++) {
                posPasaPortador.add(Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(posPortador,new ArrayList<>(movimientos.subList((1+i),movimientos.size()))));
            }

        }

        return posPasaPortador;
    }

    public int[] posMiAtacanteEnSuCamino() {

        int[] pos = new int[]{-1,-1};

        for (int f = posAct[0] - 1; f <= (posAct[0] + 1); f++) {
            for (int c = posAct[1] - 1; c <= (posAct[1] + 1); c++) {
                if(hayAtacanteMiEquipoEn(f,c)) pos = dejarPasoAtacante(f,c);
            }
        }

        return pos;
    }

    private int[] dejarPasoAtacante(int fAtacante, int cAtacante) {

        int f = posAct[0];
        int c = posAct[1];

        ArrayList<int[]> posPasaPortador = getPosPasaPortador();
        int[] movDesplazamiento = new int[]{f,c+1,f+1,c+1,f+1,c,f+1,c-1,f,c-1,f-1,c-1,f-1,c,f-1,c+1};

        for (int i = 0; i <= 14; i+=2) {
            if(mapaCompleto[movDesplazamiento[i]][movDesplazamiento[i+1]]==CONSTANTES.VACIO && !atacanteVaPasarPor(movDesplazamiento[i],movDesplazamiento[i+1],posPasaPortador) ) return new int[]{movDesplazamiento[i],movDesplazamiento[i+1]};
        }

        return new int[]{fAtacante,cAtacante};
    }

    private boolean atacanteVaPasarPor(int f, int c, ArrayList<int[]> posPasaPortador) {
        for (int[] pos:posPasaPortador) {
            if(pos[0]==f && pos[1]==c) return true;
        }
        return false;
    }

    private boolean hayAtacanteMiEquipoEn(int f, int c) {

        for (int[] pos:posDefensas) {
            if(f==pos[0] && c==pos[1]) return false;
        }

        char caracter = mapaCompleto[f][c];

        if(miEquipo.equals(CONSTANTES.EQUIPO_AZUL)){
            if (caracter == CONSTANTES.C_JUGADOR_AZUL || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_AZUL_BANDERA_PROPIA)  return true;
        }
        else{
            if (caracter == CONSTANTES.C_JUGADOR_ROJO || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_CONTRARIA || caracter == CONSTANTES.C_JUGADOR_ROJO_BANDERA_PROPIA) return true;
        }

        return false;

    }
}

