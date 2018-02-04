package Recursos;

import Agentes.Agente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by Isaac on 03/04/2016.
 */
public class A_Estrella {

    private static final Logger logger = LoggerFactory.getLogger(A_Estrella.class);

    public static ArrayList<String> Calcular(Mapa mapa,int[] posSalida, int[] posMeta,int elementosIntroducirMapaObstaculos,ArrayList<int[]>posicionesNuevasDeObstaculos){

        PriorityQueue<Nodo>   ABIERTOS  = new PriorityQueue<Nodo>(mapa.getTam()*8);
        PriorityQueue<Nodo>   CERRADOS  = new PriorityQueue<Nodo>(mapa.getTam()*8);

        logger.info("A*: ["+posSalida[0]+" "+posSalida[1]+"] -> ["+posMeta[0]+" "+posMeta[1]+"]");

        // Mapa a usar por los nodos para generarse
        boolean[][] mapaObstaculos = mapa.getMapaObstaculos(elementosIntroducirMapaObstaculos, posicionesNuevasDeObstaculos,posMeta);

        // Generamos un nodo inicial
        Nodo actual = new Nodo(null,posSalida[0],posSalida[1],0,-1,posMeta[0],posMeta[1],mapaObstaculos);

        // Lo añadimos a la cola de abiertos
        ABIERTOS.add(actual);

        // Algoritmo A*
        while ( (!actual.esMeta()) && (ABIERTOS.size()>0)){

            actual = ABIERTOS.poll();

            ArrayList<Nodo> sucesores = actual.getSucesores();

            CERRADOS.add(actual);

            eliminarDuplicados(sucesores,ABIERTOS,CERRADOS);

            ABIERTOS.addAll(sucesores);

        }

        if(!actual.esMeta()){
            logger.error(mapa.getAgenteLanzador().getLocalName()+": A*: ["+posSalida[0]+" "+posSalida[1]+"] -> ["+posMeta[0]+" "+posMeta[1]+"] El nodo devuelto no es meta");
            String s = "";
            for (int i = 0; i < mapaObstaculos.length; i++) {
                for (int j = 0; j < mapaObstaculos[0].length; j++) {
                    if (mapaObstaculos[i][j]) s += " ";
                    else s += "H";
                }
                s = s + "\n";
            }
            logger.error(s);
            ArrayList<String> mov = new ArrayList<>();
            mov.add(CONSTANTES.ACION_NULA);
            return mov;
        }

        return actual.getMovimientos();

    }

    /**
     * Detecta de los nodos sucesores cuales estan presente en abiertos o cerrado con menor coste que estos y los elimina
     * @param sucesores nodos a analizar
     * @param ABIERTOS cola de abiertos
     * @param CERRADOS cola de cerrados
     */
    private static void eliminarDuplicados(ArrayList<Nodo> sucesores,PriorityQueue<Nodo> ABIERTOS,PriorityQueue<Nodo> CERRADOS) {

        for (int i = 0; i<sucesores.size();i++) {

            Nodo n = sucesores.get(i);

            boolean encontrado = false;
            Iterator<Nodo> it = ABIERTOS.iterator();

            while(it.hasNext() && encontrado==false){
                Nodo nodoEnAbierto= it.next();
                if (nodoEnAbierto.getCostoTotal()<=n.getCostoTotal() && nodoEnAbierto.equals(n)) encontrado = true;
            }

            it = CERRADOS.iterator();

            while(it.hasNext() && encontrado==false){
                Nodo nodoEnCerrado= it.next();
                if (nodoEnCerrado.getCostoTotal()<=n.getCostoTotal() && nodoEnCerrado.equals(n)) encontrado = true;
            }

            if (encontrado) {
                sucesores.remove(i);
                i--;
            }

        }

    }

    public static ArrayList<String> Calcular_Adaptativo(Mapa mapa,int[] posSalida, int[] posMeta,int elementosIntroducirMapaObstaculos,ArrayList<int[]>posicionesNuevasDeObstaculos) {

        PriorityQueue<Nodo>   ABIERTOS  = new PriorityQueue<Nodo>(mapa.getTam()*8);
        PriorityQueue<Nodo>   CERRADOS  = new PriorityQueue<Nodo>(mapa.getTam()*8);

        logger.info("A*: ["+posSalida[0]+" "+posSalida[1]+"] -> ["+posMeta[0]+" "+posMeta[1]+"]");

        if(posSalida[0]==-1 || posMeta[0]==-1){
            logger.error("posSalida["+posSalida[0]+" "+posSalida[1]+"] o posMeta["+posMeta[0]+" "+posMeta[1]+"] desconocido, devuelvo accion nula");
            ArrayList<String> mov = new ArrayList<>();
            mov.add(CONSTANTES.ACION_NULA);
            return mov;
        }

        // Mapa a usar por los nodos para generarse
        boolean[][] mapaObstaculos = mapa.getMapaObstaculos(CONSTANTES.M_ADD_NINGUNO, posicionesNuevasDeObstaculos,posMeta);

        // Generamos un nodo inicial
        Nodo actual = new Nodo(null,posSalida[0],posSalida[1],0,-1,posMeta[0],posMeta[1],mapaObstaculos);

        // Lo añadimos a la cola de abiertos
        ABIERTOS.add(actual);

        // Algoritmo A*
        while ( (!actual.esMeta()) && (ABIERTOS.size()>0)){

            actual = ABIERTOS.poll();

            ArrayList<Nodo> sucesores = actual.getSucesores();

            CERRADOS.add(actual);

            eliminarDuplicados(sucesores,ABIERTOS,CERRADOS);

            ABIERTOS.addAll(sucesores);

        }

        if(!actual.esMeta()){
            logger.error("El nodo devuelto no es meta");
            ArrayList<String> mov = new ArrayList<>();
            mov.add(CONSTANTES.ACION_NULA);
            return mov;
        }

        ArrayList<String> movimientosTotales = actual.getMovimientos();


        if(movimientosTotales.size()>2){
            int[] posParteEsquivar = Nodo.posicionFinalDeAplicarLosMovimientosDesdePosIni(posSalida,new ArrayList<>(movimientosTotales.subList( movimientosTotales.size()-2, movimientosTotales.size())));
            movimientosTotales = Calcular(mapa,posSalida,posParteEsquivar,elementosIntroducirMapaObstaculos,posicionesNuevasDeObstaculos);

        }
        else movimientosTotales = Calcular(mapa,posSalida,posMeta,elementosIntroducirMapaObstaculos,posicionesNuevasDeObstaculos);

        return movimientosTotales;
    }
}
