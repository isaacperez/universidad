package Recursos;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Isaac on 03/04/2016.
 */
public class Nodo implements Comparable<Nodo> {

    private Nodo padre;
    private float heuristica;
    private float costoActual;
    private float costoTotal;
    private int[] movimientos;
    private int fMeta, cMeta;
    public int f,c;
    private boolean[][] puedoEstarEn;
    private int movimientoGenerador;

    public Nodo(Nodo padre,int f, int c,float costoActual,int movimientoGenerador,int fMeta,int cMeta,boolean[][] puedoEstarEn){

        this.padre               = padre;
        this.f                   = f;
        this.c                   = c;
        this.costoActual         = costoActual;
        this.movimientoGenerador = movimientoGenerador;
        this.fMeta               = fMeta;
        this.cMeta               = cMeta;
        this.puedoEstarEn        = puedoEstarEn;

        // Usamos como heuristica la distancia euclidea
        this.heuristica          = (float) Math.sqrt(Math.pow(fMeta-f,2)+ Math.pow(cMeta-c,2));
        this.costoTotal          = costoActual + heuristica;

        // Todos los posibles movimientos que se pueden hacer
        this.movimientos         = new int[]{f-1,c-1,f-1,c,f-1,c+1,f,c-1,f,c+1,f+1,c-1,f+1,c,f+1,c+1};

    }

    @Override
    public int compareTo(Nodo o) {

        if      (o.getCostoTotal() == this.costoTotal) return 0;
        else if (o.getCostoTotal() <  this.costoTotal) return 1;
        else                                           return -1;

    }

    public float getHeuristica(){
        return heuristica;
    }

    public Nodo getPadre(){
        return padre;
    }

    public boolean esMeta(){
        return f==fMeta && c==cMeta;
    }

    /**
     * Genera todos los nodos que se pueden visitar a partir de este, es decir que no vayan a una pared ("H")
     * @return ArrayList con todos los nodos que se pueden visitar
     */
    public ArrayList<Nodo> getSucesores() {

        ArrayList<Nodo> sucesores = new ArrayList<>();

        for (int i = 0; i <16; i+=2) {
            if(puedoEstarEn[movimientos[i]][movimientos[i+1]]){
                sucesores.add(new Nodo(this,movimientos[i],movimientos[i+1],costoActual+1,i,fMeta,cMeta,puedoEstarEn));
            }
        }

        return sucesores;
    }

    public boolean equals(Object o){

        if (o instanceof Nodo){
            Nodo n = (Nodo) o;
            return (f==n.f && c==n.c);
        }
        else return false;

    }

    public float getCostoTotal(){
        return costoTotal;
    }

    /**
     * Obtiene el movimiento que hizo el nodo inicial para llegar a este nodo
     * @return movimiento realizado por el nodo inicial
     */
    public  ArrayList<String> getMovimientos() {

        ArrayList<String> movimientosRealizados = new ArrayList<>();

        Nodo nodoPadre = this;
        while(nodoPadre.getPadre()!=null && nodoPadre.getPadre().getMovimientoGenerador()!=-1 ){

            switch(nodoPadre.getMovimientoGenerador()){
                case 0:   movimientosRealizados.add(CONSTANTES.ACION_NW);
                    break;
                case 2:   movimientosRealizados.add(CONSTANTES.ACION_ARR);
                    break;
                case 4:   movimientosRealizados.add(CONSTANTES.ACION_NE);
                    break;
                case 6:   movimientosRealizados.add(CONSTANTES.ACION_IZQ);
                    break;
                case 8:   movimientosRealizados.add(CONSTANTES.ACION_DER);
                    break;
                case 10:  movimientosRealizados.add(CONSTANTES.ACION_SW);
                    break;
                case 12:  movimientosRealizados.add(CONSTANTES.ACION_ABJ);
                    break;
                case 14:  movimientosRealizados.add(CONSTANTES.ACION_SE);
            }

            nodoPadre = nodoPadre.getPadre();

        }

        switch(nodoPadre.getMovimientoGenerador()){
            case 0:   movimientosRealizados.add(CONSTANTES.ACION_NW);
                break;
            case 2:   movimientosRealizados.add(CONSTANTES.ACION_ARR);
                break;
            case 4:   movimientosRealizados.add(CONSTANTES.ACION_NE);
                break;
            case 6:   movimientosRealizados.add(CONSTANTES.ACION_IZQ);
                break;
            case 8:   movimientosRealizados.add(CONSTANTES.ACION_DER);
                break;
            case 10:  movimientosRealizados.add(CONSTANTES.ACION_SW);
                break;
            case 12:  movimientosRealizados.add(CONSTANTES.ACION_ABJ);
                break;
            case 14:  movimientosRealizados.add(CONSTANTES.ACION_SE);
                break;
            case -1: movimientosRealizados.add(CONSTANTES.ACION_NULA);
        }

        return movimientosRealizados;
    }

    public int getMovimientoGenerador(){
        return movimientoGenerador;
    }

    public static int[] posicionFinalDeAplicarLosMovimientosDesdePosIni(int[] posIni,ArrayList<String>movimientos){

        int[] movimientosPosibles = new int[]{-1,-1,-1,0,-1,1,0,-1,0,1,1,-1,1,0,1,1,0,0};
        int[] posFinal            = posIni.clone();

        for (int i = movimientos.size()-1;i>=0;i--) {
            String movimiento = movimientos.get(i);
            int indice = getIndiceMovimiento(movimiento);
            posFinal[0]+=movimientosPosibles[indice];
            posFinal[1]+=movimientosPosibles[indice+1];
        }

        return posFinal;
    }

    private static int getIndiceMovimiento(String accion) {
        switch (accion){
            case CONSTANTES.ACION_NW:   return 0;
            case CONSTANTES.ACION_ARR:  return 2;
            case CONSTANTES.ACION_NE:   return 4;
            case CONSTANTES.ACION_IZQ:  return 6;
            case CONSTANTES.ACION_DER:  return 8;
            case CONSTANTES.ACION_SW:   return 10;
            case CONSTANTES.ACION_ABJ:  return 12;
            case CONSTANTES.ACION_SE:   return 14;
            default: return 16; // ACCION NULA
        }
    }
}
