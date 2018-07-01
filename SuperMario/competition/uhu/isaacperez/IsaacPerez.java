package competition.uhu.isaacperez;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by isaac on 26/11/16.
 */
public class IsaacPerez implements Agent
{

    protected boolean action[] = new boolean[Environment.numberOfKeys];
    protected String  name = "Instance_of_BasicAIAgent._Change_this_name";

    /*final*/
    protected byte[][] levelScene;
    /*final */
    protected byte[][] enemies;
    protected byte[][] mergedObservation;

    protected float[] marioFloatPos = null;
    protected float[] enemiesFloatPos = null;

    protected int[]   marioState = null;

    protected int     marioStatus;
    protected int     marioMode;
    protected boolean isMarioOnGround;
    protected boolean isMarioAbleToJump;
    protected boolean isMarioAbleToShoot;
    protected boolean isMarioCarrying;
    protected int     getKillsTotal;
    protected int     getKillsByFire;
    protected int     getKillsByStomp;
    protected int     getKillsByShell;

    protected int     receptiveFieldWidth;
    protected int     receptiveFieldHeight;
    protected int     marioEgoRow;
    protected int     marioEgoCol;

    // values of these variables could be changed during the Agent-Environment interaction.
// Use them to get more detailed or less detailed description of the level.
// for information see documentation for the benchmark <link: marioai.org/marioaibenchmark/zLevels
    int zLevelScene = 1;
    int zLevelEnemies = 0;

    private Estado estado;
    private Qtabla qtabla;
    private boolean pintar;

    public IsaacPerez(String s)
    {

        estado = new Estado();
        qtabla = new Qtabla();
        pintar = false;
        setName(s);

        System.out.println("Jugador IsaacPerez creado.");
        System.out.println("Parametros de aprendizaje: ");
        System.out.println("\t- Alfa: " + CONSTANTES.ALFA);
        System.out.println("\t- Gamma: " + CONSTANTES.GAMMA);
        System.out.println("\t- ProbabilidadGreedy: " + CONSTANTES.PROBABILIDAD_HACER_MEJOR_ACCION);
        cargarTablaHash(CONSTANTES.RUTA_TABLA_HASH);
        System.out.println("¡Tabla Hash cargada!");
        System.out.println("");


    }


    public boolean[] getAction()
    {
        return qtabla.getAccionGreedy(estado.getEstadoActualString(), pintar);
    }

    // Recibe un objeto MarioEnvironment con el estado actual y a partir de este montamos nuestro vector de estado
    public void integrateObservation(Environment environment)
    {

        levelScene = environment.getLevelSceneObservationZ(zLevelScene);
        enemies = environment.getEnemiesObservationZ(zLevelEnemies);
        mergedObservation = environment.getMergedObservationZZ(1, 0);

        this.marioFloatPos = environment.getMarioFloatPos();
        this.enemiesFloatPos = environment.getEnemiesFloatPos();
        this.marioState = environment.getMarioState();

        receptiveFieldWidth = environment.getReceptiveFieldWidth();
        receptiveFieldHeight = environment.getReceptiveFieldHeight();

        // It also possible to use direct methods from Environment interface.
        //
        marioStatus = marioState[0];
        marioMode = marioState[1];
        isMarioOnGround = marioState[2] == 1;
        isMarioAbleToJump = marioState[3] == 1;
        isMarioAbleToShoot = marioState[4] == 1;
        isMarioCarrying = marioState[5] == 1;
        getKillsTotal = marioState[6];
        getKillsByFire = marioState[7];
        getKillsByStomp = marioState[8];
        getKillsByShell = marioState[9];

        // Actualizamos el estado
        estado.actualizarEstado(environment);

        if(true) {

            System.out.println("-------------------------------------------------");

            if (CONSTANTES.PROBABILIDAD_HACER_MEJOR_ACCION == 1)
                System.out.println("(MODO MEJOR): LLEGA ACTUALIZACION DEL ENTORNO");
            else System.out.println("(MODO APRENDIZ): LLEGA ACTUALIZACION DEL ENTORNO");

            System.out.println("\nEstado del mario en el entorno recibido: " + estado);
            System.out.println("Estado del mario anterior al actual: " + estado.getEstadoAnteriorString());


        }
        // Actualizamos la Qtabla
        //if(CONSTANTES.PROBABILIDAD_HACER_MEJOR_ACCION != 1) qtabla.actualizarQtabla(estado.getEstadoAnteriorString(), estado.getEstadoActualString(),estado.getEstadoActual(), estado.getRecompensa(estado.getEstadoActual()),pintar);
        //else qtabla.actualizarEstado(estado.getEstadoActual());
        qtabla.actualizarEstado(estado.getEstadoActual());
    }

    //*****************************************************************
    // FIN METODOS NUEVOS
    //*****************************************************************

    public void giveIntermediateReward(float intermediateReward)
    {

    }

    public void reset()
    {

        estado.reset();
        qtabla.reset();
    }

    public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
    {
        receptiveFieldWidth = rfWidth;
        receptiveFieldHeight = rfHeight;

        marioEgoRow = egoRow;
        marioEgoCol = egoCol;
    }

    public String getName() { return name; }

    public void setName(String Name) { this.name = Name; }

    public void cargarTablaHash(String rutaTablaHash) {

        qtabla.setTabla(rutaTablaHash);

    }


}