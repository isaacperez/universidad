package competition.uhu.isaacperez;

import ch.idsia.benchmark.mario.environments.Environment;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by isaac on 30/11/16.
 */
public class Qtabla {

    private HashMap<String, Double> tabla;
    private boolean[] ultimaAccion;
    private boolean   primeraEjecucion;
    private int[] arrayEstadoActual;

    public Qtabla() {

        tabla = new HashMap<>();

        ultimaAccion   = new boolean[Environment.numberOfKeys];
        primeraEjecucion = true;

    }

    public void reset(){
        primeraEjecucion = true;
    }

    public void actualizarQtabla(String estadoAnterior, String estadoActual,int[] arrayEstadoActual, double recompensaEstadoActual, boolean pintar) {

        this.arrayEstadoActual = arrayEstadoActual;

        if(!primeraEjecucion) {

            if (tabla.containsKey(estadoAnterior + getUltimaAccionString())) {

                double valorAnterior = tabla.get(estadoAnterior + getUltimaAccionString() );
                double recompensaFutura = getMaximoValorEnQtabla(estadoActual);
                tabla.put(estadoAnterior + getUltimaAccionString(), ((1-CONSTANTES.ALFA)*valorAnterior) + (CONSTANTES.ALFA * (recompensaEstadoActual + (CONSTANTES.GAMMA * recompensaFutura))));

                if(pintar){
                    System.out.println("\tActualizamos la Qtabla: Qtabla(" + estadoAnterior + getUltimaAccionString() + ") = " + valorAnterior + " + " + CONSTANTES.ALFA + " * " + recompensaEstadoActual + " + " + CONSTANTES.GAMMA + " * " + recompensaFutura);
                }

            } else {

                tabla.put(estadoAnterior + getUltimaAccionString(), (double) 0);

                if(pintar){
                    System.out.println("\tInicializamos la Qtabla: Qtabla(" + estadoAnterior + getUltimaAccionString() + ") = " + recompensaEstadoActual);
                }

            }

        }
        else{

            primeraEjecucion = false;

            if(pintar){

                System.out.println("Se trata del primer estado, no actualiamos la Qtabla...");

            }

        }

    }

    private double getMaximoValorEnQtabla(String estadoActual) {

        // nos quedamos con el valor maximo en la qTabla para todas las posibles acciones en el estado actual, devolviendo 0 e caso de que
        // no exista

        int numAccionesDisponibles = CONSTANTES.ACCIONES_POSIBLES.size();
        int mejorAccion = -1;

        for (int i = 0; i < numAccionesDisponibles; i++) {

            String claveEstadoAccion = estadoActual + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES.get(i));

            if(tabla.containsKey(claveEstadoAccion) && ((!CONSTANTES.ACCIONES_POSIBLES.get(i)[CONSTANTES.KEY_JUMP] && !CONSTANTES.ACCIONES_POSIBLES.get(i)[CONSTANTES.KEY_SPEED]) ||(arrayEstadoActual[0]==1 || arrayEstadoActual[3]==0))){

                if (mejorAccion==-1) mejorAccion = i;
                else if(tabla.get(claveEstadoAccion) > tabla.get(estadoActual + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES.get(mejorAccion)))) mejorAccion = i;

            }

        }

        if(mejorAccion==-1) return 0;
        else return tabla.get(estadoActual + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES.get(mejorAccion)));

    }

    public String getUltimaAccionString() {
        return Arrays.toString(ultimaAccion);
    }

    public boolean[] getAccionGreedy(String estadoActual, boolean pintar) {

        boolean[] accionElegida;

        if(deboHacerMejorAccion()){

            if(pintar) System.out.println("\tDebo hacer accion mejor...");
            accionElegida = getMejorAccionParaEstado(estadoActual, pintar);

        }
        else{

            if(pintar) System.out.println("\tDebo hacer accion aleatoria...");
            accionElegida = getAccionAleatoria(pintar);

        }

        ultimaAccion = accionElegida;
        return accionElegida;

    }

    private boolean[] getAccionAleatoria(boolean pintar) {

        int indiceAccionElegida;

        // Si puedo saltar ( marioPuedeSaltar OR !marioEstaEnElSuelo)
        if( (arrayEstadoActual[1]==1) || (arrayEstadoActual[3]==0) ){

            indiceAccionElegida = CONSTANTES.RND.nextInt(CONSTANTES.ACCIONES_POSIBLES.size());

            if(pintar){
                System.out.println("\t\tHago accion aleatoria: " + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES.get(indiceAccionElegida)));
            }

            return CONSTANTES.ACCIONES_POSIBLES.get(indiceAccionElegida);

        }
        else{

            indiceAccionElegida = CONSTANTES.RND.nextInt(CONSTANTES.ACCIONES_POSIBLES_SIN_SALTO.size());

            if(pintar){
                System.out.println("\t\tHago accion aleatoria: " + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES_SIN_SALTO.get(indiceAccionElegida)));
            }

            return CONSTANTES.ACCIONES_POSIBLES_SIN_SALTO.get(indiceAccionElegida);

        }


    }

    private boolean[] getMejorAccionParaEstado(String estadoActual, boolean pintar) {

        int numAccionesDisponibles = CONSTANTES.ACCIONES_POSIBLES.size();
        int mejorAccion = -1;

        for (int i = 0; i < numAccionesDisponibles; i++) {

            String claveEstadoAccion = estadoActual + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES.get(i));

            if(tabla.containsKey(claveEstadoAccion)){

                if((!CONSTANTES.ACCIONES_POSIBLES.get(i)[CONSTANTES.KEY_JUMP] && !CONSTANTES.ACCIONES_POSIBLES.get(i)[CONSTANTES.KEY_SPEED] ) || (arrayEstadoActual[1]==1) || (arrayEstadoActual[3]==0)){

                    if (mejorAccion==-1) mejorAccion = i;
                    else if(tabla.get(claveEstadoAccion) > tabla.get(estadoActual + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES.get(mejorAccion)))) mejorAccion = i;

                }

            }

        }

        if(pintar && mejorAccion!=-1){
            System.out.println("\t\tHago accion mejor: " + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES.get(mejorAccion)));
            System.out.println("\t\t\tValores para las otras opciones disponibles: ");

            if((arrayEstadoActual[1]==0) && (arrayEstadoActual[3]==1)) System.out.println("NO PUEDO SALTAR");

            for (int i = 0; i < numAccionesDisponibles; i++) {
                String claveEstadoAccion = estadoActual + Arrays.toString(CONSTANTES.ACCIONES_POSIBLES.get(i));
                if(tabla.containsKey(claveEstadoAccion) && ((!CONSTANTES.ACCIONES_POSIBLES.get(i)[CONSTANTES.KEY_JUMP] && !CONSTANTES.ACCIONES_POSIBLES.get(i)[CONSTANTES.KEY_SPEED] ) || (arrayEstadoActual[1]==1) || (arrayEstadoActual[3]==0))) System.out.println("\t\t\t" + claveEstadoAccion + ": " + tabla.get(claveEstadoAccion));
                else if((tabla.containsKey(claveEstadoAccion))) System.out.println("\t\t\t" + claveEstadoAccion + ": NO PUEDO EJECUTARLA EN EL ESTADO ACTUAL ");
            }

        }

        if(mejorAccion == -1) return getAccionAleatoria(pintar);
        else return CONSTANTES.ACCIONES_POSIBLES.get(mejorAccion);

    }

    private boolean deboHacerMejorAccion() {

        return CONSTANTES.RND.nextDouble() <= CONSTANTES.PROBABILIDAD_HACER_MEJOR_ACCION;

    }


    public HashMap<String,Double> getTabla() {
        return tabla;
    }

    public void setTabla(String ruta) {

        File toRead = new File(ruta);

        FileInputStream fis = null;
        try {

            fis = new FileInputStream(toRead);
            ObjectInputStream ois = new ObjectInputStream(fis);

            tabla = (HashMap<String,Double>)ois.readObject();

            ois.close();
            fis.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void actualizarEstado(int[] estadoActual) {
        this.arrayEstadoActual = estadoActual;
    }

}
