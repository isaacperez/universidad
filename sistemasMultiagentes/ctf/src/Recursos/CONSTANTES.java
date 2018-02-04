package Recursos;

import java.util.ArrayList;

/**
 * Created by Isaac on 03/04/2016.
 */
public class CONSTANTES {


    // Constantes para el Agente
    public static String EQUIPO_DEL_AGENTE = "8";

    // ID de los elementos de la plataforma
    public static final String EQUIPO_ROJO = "7";
    public static final String EQUIPO_AZUL = "8";

    public static final String BASE_BANDERA_ROJA              = "A";
    public static final String BASE_BANDERA_AZUL              = "B";
    public static final String BASE_ROJA                      = "C";
    public static final String BASE_AZUL                      = "D";
    public static final String BANDERA_ROJA                   = "E";
    public static final String BANDERA_AZUL                   = "F";
    public static final String ENTRADA_ROJA                   = "V";
    public static final String ENTRADA_AZUL                   = "W";
    public static final String JUGADOR_ROJO                   = "1";
    public static final String JUGADOR_AZUL                   = "2";
    public static final String JUGADOR_ROJO_BANDERA_PROPIA    = "3";
    public static final String JUGADOR_AZUL_BANDERA_PROPIA    = "4";
    public static final String JUGADOR_ROJO_BANDERA_CONTRARIA = "5";
    public static final String JUGADOR_AZUL_BANDERA_CONTRARIA = "6";
    public static final char C_JUGADOR_ROJO                   = '1';
    public static final char C_JUGADOR_AZUL                   = '2';
    public static final char C_JUGADOR_ROJO_BANDERA_PROPIA    = '3';
    public static final char C_JUGADOR_AZUL_BANDERA_PROPIA    = '4';
    public static final char C_JUGADOR_ROJO_BANDERA_CONTRARIA = '5';
    public static final char C_JUGADOR_AZUL_BANDERA_CONTRARIA = '6';
    public static final char C_BASE_ROJA                      = 'C';
    public static final char C_BASE_AZUL                      = 'D';

    public static final String FIN_DEL_JUEGO = "9";
    public static final char   PARED         = 'H';
    public static final char   VACIO         = ' ';
    public static final String PARED_STRING  = "H";

    // Acciones a mandar al servidor
    public static final String ACION_NULA = "0";
    public static final String ACION_DER  = "1";
    public static final String ACION_IZQ  = "2";
    public static final String ACION_ARR  = "3";
    public static final String ACION_ABJ  = "4";
    public static final String ACION_NE   = "21";
    public static final String ACION_NW   = "22";
    public static final String ACION_SE   = "23";
    public static final String ACION_SW   = "24";

    public static final String POSICION_ACTUAL = "-1";

    // Constantes para la clase mapa
    public static final int M_ADD_NINGUNO = -1;
    public static final int M_ADD_TODOS_LOS_ELEMENTOS = 2;
    public static final ArrayList<int[]> M_ARRAYLIST_VACIO = new ArrayList<>();
    public static final int ENTORNO_DEFENSIVO = 3;
    public static final double ESTA_A_UNA_ACCION_DE_LA_POSICION = 1.5; // Raiz cuadrada de 2
    public static final double ESTA_A_DOS_ACCIONES_DE_LA_POSICION = 2.9;
}
