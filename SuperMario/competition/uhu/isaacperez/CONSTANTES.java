package competition.uhu.isaacperez;

import ch.idsia.benchmark.mario.environments.Environment;

import java.util.ArrayList;
import java.util.Random;
import java.util.zip.CheckedOutputStream;

/**
 * Created by isaac on 30/11/16.
 */
public class CONSTANTES {

    public static final String RUTA_TABLA_HASH = "./src/competition/uhu/isaacperez/0.2_0.8_0.8.properties";
    // PARAMETROS DEL ALGORITMO
    public static double ALFA = 0.2;
    public static double GAMMA = 0.8;

    public static final Random RND = new Random(System.currentTimeMillis());
    public static double PROBABILIDAD_HACER_MEJOR_ACCION = 1;


    // TECLAS DISPONIBLES
    public static final int KEY_LEFT = 0;
    public static final int KEY_RIGHT = 1;
    public static final int KEY_DOWN = 2; // No sirve
    public static final int KEY_JUMP = 3;
    public static final int KEY_SPEED = 4;
    public static final int KEY_UP = 5; // No sirve


    // ACCIONES DISPONIBLES PARA MARIO
    public static final boolean[] ACCION_PARAR = {false, false, false, false, false, false};

    public static final boolean[] ACCION_DERECHA             = {false, true, false, false, false, false};
    private static final boolean[] ACCION_DERECHA_SALTO       = {false, true, false, true, false, false};
    public static final boolean[] ACCION_DERECHA_CORRE       = {false, true, false, false, true, false};
    private static final boolean[] ACCION_DERECHA_SALTO_CORRE = {false, true, false, true, true, false};

    private static final boolean[] ACCION_IZQUIERDA             = {true, false, false, false, false, false};
    private static final boolean[] ACCION_IZQUIERDA_SALTO       = {true, false, false, true, false, false};
    private static final boolean[] ACCION_IZQUIERDA_CORRE       = {true, false, false, false, true, false};
    private static final boolean[] ACCION_IZQUIERDA_SALTO_CORRE = {true, false, false, true, true, false};

    public static final boolean[] ACCION_SALTA = {false, false, false, true, false, false};

    public static final ArrayList<boolean[]> ACCIONES_POSIBLES = new ArrayList<boolean[]>() {{

        add(ACCION_DERECHA);
        add(ACCION_DERECHA_SALTO);
        add(ACCION_DERECHA_CORRE);
        add(ACCION_DERECHA_SALTO_CORRE);
        add(ACCION_IZQUIERDA);
        add(ACCION_IZQUIERDA_SALTO);
        add(ACCION_IZQUIERDA_CORRE);
        add(ACCION_IZQUIERDA_SALTO_CORRE);
        add(ACCION_SALTA);

    }};

    public static final ArrayList<boolean[]> ACCIONES_POSIBLES_SIN_SALTO = new ArrayList<boolean[]>() {{

        add(ACCION_DERECHA);
        add(ACCION_IZQUIERDA);
        add(ACCION_DERECHA_CORRE);
        add(ACCION_IZQUIERDA_CORRE);

    }};

    // CONSTANTES PARA CREAR LOS ESTADOS
    public static final double PONDERACION_AVANCE_DERECHA = 10;
    public static final double PONDERACION_AVANCE_IZQUIERDA = -10;
    public static final double PONDERACION_ATASCADO = -50;
    public static final double PONDERACION_NUEVO_ENEMIGO_MATADO = 20;


}
