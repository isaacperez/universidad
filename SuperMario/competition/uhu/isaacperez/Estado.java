package competition.uhu.isaacperez;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by isaac on 26/11/16.
 */
public class Estado {

    private final int NUM_ESTADOS = 7;
    private int   estadoActual[];
    private int   estadoAnterior[];
    private boolean   primeraEjecucion;

    // Variables para generar el estado de mario
    int posicionMarioAnterior;
    int avanceMarioDerecha;
    int avanceMarioIzquierda;
    int contadorMarioAtascado;

    int enemigosMatadosAnterior;
    int nuevoEnemigoMatado;

    int numColisionesAnteriores;
    int colisionesConEnemigo;

    public Estado() {

        estadoActual   = new int[NUM_ESTADOS];
        estadoAnterior = new int[NUM_ESTADOS];
        primeraEjecucion = true;

        posicionMarioAnterior = 0;
        avanceMarioDerecha = 0;
        avanceMarioIzquierda = 0;
        contadorMarioAtascado = 0;

        enemigosMatadosAnterior = 0;
        nuevoEnemigoMatado = 0;

        numColisionesAnteriores = 0;
        colisionesConEnemigo = 0;

    }

    public void reset(){

        estadoActual   = new int[NUM_ESTADOS];
        estadoAnterior = new int[NUM_ESTADOS];
        primeraEjecucion = true;

        posicionMarioAnterior = 0;
        avanceMarioDerecha = 0;
        avanceMarioIzquierda = 0;
        contadorMarioAtascado = 0;

        enemigosMatadosAnterior = 0;
        nuevoEnemigoMatado = 0;

        numColisionesAnteriores = 0;
        colisionesConEnemigo = 0;

    }

    public String toString(){

        return Arrays.toString(estadoActual) + ", recompensa: " + getRecompensa(estadoActual);

    }

    public void actualizarEstado(Environment environment) {

        // Guardamos el estado actual como anterior
        estadoAnterior = estadoActual.clone();

        // Actualizamos la informacion para la recompensa
        actualizarInformacionMario(environment);

        // Actualizamos el estado actual
        guardarEnPosicion(0, modoDeMario(environment));
        guardarEnPosicion(1, puedeSaltarMario(environment));
        guardarEnPosicion(2, obstaculosAlLado(environment));
        guardarEnPosicion(3, estaMarioEnElSuelo(environment));
        guardarEnPosicion(4, sentidoMovimiento(environment));
        guardarEnPosicion(5, enemigosAlLado(environment));
        guardarEnPosicion(6, marioAtascado(environment));

    }

    private int marioAtascado(Environment environment) {
        return (contadorMarioAtascado>5)? 1: 0;
    }

    private int enemigosAlLado(Environment environment) {

        // Posicion del mario en la matriz
        int[] posMarioEnMatriz = environment.getMarioEgoPos();

        // Miramos en la casilla a la derecha de mario, y-1,y, y+1, y+2
        byte[][] entorno = environment.getEnemiesObservationZ(1);

        boolean[] codificacion = new boolean[8];

        int indice = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                //if((i != 0) || (j != 0)) System.out.println("En la posicion [" + indice + "] con i = " + i + " y j = " + j + " asigno: " + hayEnemigo(entorno[posMarioEnMatriz[0]+i][posMarioEnMatriz[1]+j]));
                if((i != 0) || (j != 0)){
                    codificacion[indice] = hayEnemigo(entorno[posMarioEnMatriz[0]+i][posMarioEnMatriz[1]+j]);
                    indice ++;
                }

            }
        }

         //System.out.println(Arrays.toString(codificacion) + ", Codificacion: " + valorDecimaldeNumeroBinario(codificacion));

        // Mostramos por pantalla la informacion que llega
        /*for (int i = 0; i < entorno.length; i++) {
            String s = "";
            for (int j = 0; j < entorno[i].length; j++) {

                if(i == 9 && j == 9) s += "[" + entorno[i][j] + "] ";
                else s += entorno[i][j] + " ";
            }
            System.out.println(s);
        }*/

        return valorDecimaldeNumeroBinario(codificacion);


    }

    private int sentidoMovimiento(Environment environment) {

        if(avanceMarioDerecha>1) return 2;
        else if( avanceMarioIzquierda > 1) return 1;
        else return 0;
    }

    private void actualizarInformacionMario(Environment environment) {

        // Comprobamos el avance del mario
        int posicionMario = environment.getEvaluationInfo().distancePassedPhys;
        //System.out.println("Posicion mario: " + posicionMario + ", posicion mario anterior: " + posicionMarioAnterior);

        if(posicionMario > posicionMarioAnterior){

            avanceMarioDerecha = posicionMario - posicionMarioAnterior;
            if(avanceMarioDerecha > 10) avanceMarioDerecha = 10;

            avanceMarioIzquierda = 0;
            if(avanceMarioDerecha>1) contadorMarioAtascado = 0;
            else contadorMarioAtascado = Math.min(contadorMarioAtascado+1,10);

        }
        else if(posicionMario < posicionMarioAnterior){

            avanceMarioIzquierda =  posicionMarioAnterior - posicionMario;
            if(avanceMarioIzquierda > 10) avanceMarioIzquierda = 10;

            avanceMarioDerecha = 0;
            if(avanceMarioIzquierda>1) contadorMarioAtascado = 0;
            else contadorMarioAtascado = Math.min(contadorMarioAtascado+1,10);

        }else{

            avanceMarioIzquierda = 0;
            avanceMarioDerecha = 0;

            contadorMarioAtascado = Math.min(contadorMarioAtascado+1,10);

        }

        // Comprobamos si ha colisionado con un enemigo
        int numColisionesActuales = environment.getEvaluationInfo().collisionsWithCreatures;

        if (numColisionesActuales == numColisionesAnteriores) colisionesConEnemigo = 0;
        else colisionesConEnemigo = 1;

        // Comprobamos si ha matado a algun enemigo
        int enemigosMatadosActual = environment.getEvaluationInfo().computeKillsTotal();

        if(enemigosMatadosActual> enemigosMatadosAnterior) nuevoEnemigoMatado = 1;
        else nuevoEnemigoMatado = 0;

        // Actualizamos las variables
        posicionMarioAnterior = posicionMario;
        numColisionesAnteriores = numColisionesActuales;
        enemigosMatadosAnterior = enemigosMatadosActual;

    }

    private int estaMarioEnElSuelo(Environment environment) {

        return (environment.isMarioOnGround()) ? 1 : 0;

    }

    private int obstaculosAlLado(Environment environment) {

        /// Devuelve 0,1,2,3 dependiendo de la altura del obstaculo que tiene delante
        // -85 = GeneralizerLevelScene.FLOWER_POT_OR_CANNON // La tuberia verde o la negra
        // -60 = BORDER_CANNOT_PASS_THROUGH // El suelo normal
        // -24 = BRICK // Ladrillo

        // Posicion del mario en la matriz
        int[] posMarioEnMatriz = environment.getMarioEgoPos();

        // Miramos en la casilla a la derecha de mario, y-1,y, y+1, y+2
        byte[][] entorno = environment.getLevelSceneObservationZ(1);

        boolean[] codificacion = new boolean[18];

        int indice = 0;
        for (int i = -2; i <= 1; i++) {
            for (int j = -1; j <= 2; j++) {
                //if((i != 0) || (j != 0)) System.out.println("En la posicion [" + indice + "] con i = " + i + " y j = " + j + " asigno: " + hayObstaculo_o_enemigo(entorno[posMarioEnMatriz[0]+i][posMarioEnMatriz[1]+j]));
                if((i != 0) || (j != 0)){
                    codificacion[indice] = hayObstaculo(entorno[posMarioEnMatriz[0]+i][posMarioEnMatriz[1]+j]);
                    indice ++;
                }

            }
        }

       // System.out.println(Arrays.toString(codificacion) + ", Codificacion: " + valorDecimaldeNumeroBinario(codificacion));

        // Mostramos por pantalla la informacion que llega
        /*for (int i = 0; i < entorno.length; i++) {
            String s = "";
            for (int j = 0; j < entorno[i].length; j++) {

                if(i == 9 && j == 9) s += "[" + entorno[i][j] + "] ";
                else s += entorno[i][j] + " ";
            }
            System.out.println(s);
        }*/

        return valorDecimaldeNumeroBinario(codificacion);

    }

    private boolean hayObstaculo(byte valor) {

        switch (valor){

            case GeneralizerLevelScene.BRICK:   // Ladrillo: 24
            case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH: // Suelo: -60
            case GeneralizerLevelScene.FLOWER_POT_OR_CANNON: // Tuberia verde o negra: -85
            case GeneralizerLevelScene.LADDER: // 61
                return true;

            default: return false;
        }

    }

    private boolean hayEnemigo(byte valor) {

        switch (valor){

            case Sprite.KIND_GOOMBA:
            case Sprite.KIND_SPIKY:
                return true;

            default: return false;
        }

    }

    private int valorDecimaldeNumeroBinario(boolean[] codificacion) {

        int n = 0, l = codificacion.length;

        for (int i = 0; i < l; ++i) {
            n = (n << 1) + (codificacion[i] ? 1 : 0);
        }

        return n;

    }


    private int modoDeMario(Environment environment) {

        return environment.getMarioMode();

    }

    private int puedeSaltarMario(Environment environment) {

        return (environment.isMarioAbleToJump()) ? 1 : 0;

    }

    private void guardarEnPosicion(int posicionEnEstado, int valorAsignado) {
        estadoActual[posicionEnEstado] = valorAsignado;
    }

    public String getEstadoAnteriorString() {
        return Arrays.toString(estadoAnterior);
    }

    public double getRecompensa(int[] estado) {

        double recompensa;

        if(colisionesConEnemigo>0) recompensa = -100;
        else if (nuevoEnemigoMatado==1) recompensa = (Math.max(avanceMarioDerecha-1,0)) * CONSTANTES.PONDERACION_AVANCE_DERECHA +
                avanceMarioIzquierda * CONSTANTES.PONDERACION_AVANCE_IZQUIERDA +
                0 * CONSTANTES.PONDERACION_ATASCADO +
                nuevoEnemigoMatado * CONSTANTES.PONDERACION_NUEVO_ENEMIGO_MATADO
                + ((estadoActual[3]==0 && avanceMarioDerecha>0)? 70 : 0);
        else recompensa =  (Math.max(avanceMarioDerecha-1,0)) * CONSTANTES.PONDERACION_AVANCE_DERECHA +
                    avanceMarioIzquierda* CONSTANTES.PONDERACION_AVANCE_IZQUIERDA +
                    estadoActual[6] * CONSTANTES.PONDERACION_ATASCADO +
                    nuevoEnemigoMatado * CONSTANTES.PONDERACION_NUEVO_ENEMIGO_MATADO+
                    + ((estadoActual[3]==0 && avanceMarioDerecha>0)? 70 : 0);

        /*if(colisionesConEnemigo>0) recompensa = CONSTANTES.recompensaMinima;
        else if(nuevoEnemigoMatado==1) recompensa = avanceMarioDerecha * CONSTANTES.PONDERACION_AVANCE_DERECHA +
                            avanceMarioIzquierda * CONSTANTES.PONDERACION_AVANCE_IZQUIERDA +
                            0 * CONSTANTES.PONDERACION_ATASCADO +
                            nuevoEnemigoMatado * CONSTANTES.PONDERACION_NUEVO_ENEMIGO_MATADO;

        else recompensa = avanceMarioDerecha * CONSTANTES.PONDERACION_AVANCE_DERECHA +
                avanceMarioIzquierda * CONSTANTES.PONDERACION_AVANCE_IZQUIERDA +
                    (Math.max(contadorMarioAtascado-1,0)) * CONSTANTES.PONDERACION_ATASCADO +
                nuevoEnemigoMatado * CONSTANTES.PONDERACION_NUEVO_ENEMIGO_MATADO;


        if(recompensa>0) recompensa = Math.min(recompensa,CONSTANTES.recompensaMaxima);

        // Normalizamos la recompensa entre -1 y 1
        return (2 * ((recompensa - CONSTANTES.recompensaMinima) / (CONSTANTES.recompensaMaxima - CONSTANTES.recompensaMinima))) - 1;*/
        return recompensa;

    }

    public int[] getEstadoActual(){ return estadoActual; }

    public String getEstadoActualString() {
        return Arrays.toString(estadoActual);
    }
}
