package edu.masanz.da.en;

import java.util.*;

public class GameManager {

    private static final GameManager INSTANCE = new GameManager();

    private boolean alarma = false;

    private Map<String, Sala> mapSalas =  new TreeMap<>();
    private Map<String, Jugador> mapJugadores =  new TreeMap<>();
    private Map<Sala, List<Jugador>> mapSalasListaJugadores =  new TreeMap<>();
    private Map<Sala, List<Tarea>> mapSalasListaTareas =  new TreeMap<>();

    private Map<Jugador, Jugador> mapVotos = new TreeMap<>();
    private EstadoJuego estadoJuego = EstadoJuego.ESPERANDO;

    private Map<Jugador, Boolean> mapJugadorReady = new TreeMap<>();

    private String mapaTextual = """
[cocina] - [pasillo] - [dormitorio]
   |           |             |
   L-------[despensa]--------J
""";

    private GameManager() {
        initMapSalas();
        initMapSalasListaJugadores();
        initMapSalasListaTareas();
    }

    private void initMapSalasListaTareas() {
        mapSalasListaTareas.put(mapSalas.get("cocina"), new ArrayList<>());
        mapSalasListaTareas.put(mapSalas.get("pasillo"), new ArrayList<>());
        mapSalasListaTareas.put(mapSalas.get("dormitorio"), new ArrayList<>());
        mapSalasListaTareas.put(mapSalas.get("despensa"), new ArrayList<>());

        mapSalasListaTareas.get(mapSalas.get("cocina")).add(new Tarea("cafetera"));
        mapSalasListaTareas.get(mapSalas.get("despensa")).add(new Tarea("oxígeno"));
        mapSalasListaTareas.get(mapSalas.get("dormitorio")).add(new Tarea("calefacción"));
        mapSalasListaTareas.get(mapSalas.get("dormitorio")).add(new Tarea("trituradora"));
    }

    private void initMapSalas() {

        Sala cocina = new Sala("cocina");
        Sala pasillo = new Sala("pasillo");
        Sala dormitorio = new Sala("dormitorio");
        Sala despensa = new Sala("despensa");

        cocina.addSalaAdyacente(pasillo);
        cocina.addSalaAdyacente(despensa);

        pasillo.addSalaAdyacente(cocina);
        pasillo.addSalaAdyacente(dormitorio);
        pasillo.addSalaAdyacente(despensa);

        dormitorio.addSalaAdyacente(pasillo);
        dormitorio.addSalaAdyacente(despensa);

        despensa.addSalaAdyacente(cocina);
        despensa.addSalaAdyacente(pasillo);
        despensa.addSalaAdyacente(dormitorio);

        mapSalas.put("cocina", cocina);
        mapSalas.put("pasillo", pasillo);
        mapSalas.put("dormitorio", dormitorio);
        mapSalas.put("despensa", despensa);
    }

    private void initMapSalasListaJugadores() {
        mapSalasListaJugadores.put(mapSalas.get("cocina"), new ArrayList<>());
        mapSalasListaJugadores.put(mapSalas.get("pasillo"), new ArrayList<>());
        mapSalasListaJugadores.put(mapSalas.get("dormitorio"), new ArrayList<>());
        mapSalasListaJugadores.put(mapSalas.get("despensa"), new ArrayList<>());
    }

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public String getMapaTextual() {
        return mapaTextual;
    }

    public Jugador addJugador(String clientName) {

        Jugador jugador = new Jugador(clientName);
//
//        if (Math.random() < 0.3) {
//            jugador.setImpostor(true);
//        }

        // Meterlo en el mapa jugadores
        mapJugadores.put(clientName, jugador);

        // Elegir sala en la que aparece
        int i = (int) (Math.random() * mapSalas.size());
        Sala sala = (new ArrayList<>(mapSalas.values())).get(i);

        // Poner la sala en la que está al jugador
        jugador.setSala(sala);

        // Actualizar el mapa de salas y lista jugadores
        mapSalasListaJugadores.get(sala).add(jugador);

        return jugador;
    }

    public void removeJugador(String nombreJugador) {
        Jugador jugador = mapJugadores.get(nombreJugador);
        if(jugador == null){
            return;
        }
        mapJugadores.remove(nombreJugador);
        mapSalasListaJugadores.get(jugador.getSala()).remove(jugador);
        mapVotos.remove(jugador);
    }

    public boolean puedeIr(String clientName, String nombreSalaDestino) {
        // TODO
        return true;
    }

    public boolean cambiaSala(String nombreJugador, String nombreSalaDestino) {
        Jugador jugador = mapJugadores.get(nombreJugador);
        // Si está muerto --> false
        if (jugador == null) { return false; }
        if (!jugador.isVivo()) { return false; }
        Sala salaDestino = mapSalas.get(nombreSalaDestino);
        // Si la sala no existe --> false
        if (salaDestino == null) { return false; }
        // Si la sala no es accesible --> false
        Sala salaJugador = jugador.getSala();
        if (!salaJugador.isAdyacente(salaDestino)) { return false; }
        // Actualizar sala del jugador
        jugador.setSala(salaDestino);
        // Actualizar mapSalasListaJugadores
        mapSalasListaJugadores.get(salaJugador).remove(jugador);
        mapSalasListaJugadores.get(salaDestino).add(jugador);
        return true;
    }

    public Sala whereIs(String nombreJugador) {
        if(mapJugadores.containsKey(nombreJugador)){
            return mapJugadores.get(nombreJugador).getSala();
        }
        return null;
    }

    public Map<Sala, List<Jugador>> getMapSalasListaJugadores() {
        return mapSalasListaJugadores;
    }

    public Map<Sala, List<Tarea>> getMapSalasListaTareas() {
        return mapSalasListaTareas;
    }

    public boolean kill(String nombreAsesino, String nombreObjetivo) {
        Jugador asesino = mapJugadores.get(nombreAsesino);
        Jugador objetivo = mapJugadores.get(nombreObjetivo);
        if(asesino == null || objetivo == null){
            return false;
        }
        if(!asesino.getSala().equals(objetivo.getSala())){
            return false;
        }
        if(!asesino.isImpostor() || objetivo.isImpostor()){
            return false;
        }
        if(!asesino.isVivo() || !objetivo.isVivo()){
            return false;
        }
        objetivo.setVivo(false);
        return true;
    }

    public boolean alert(String nombreJugador) {
        Jugador jugador = mapJugadores.get(nombreJugador);
        if (!jugador.isVivo()){
            return false;
        }
        if(estadoJuego != EstadoJuego.JUGANDO){
            return false;
        }
        mapVotos.clear();
        estadoJuego = EstadoJuego.REUNION;
        return true;
    }

    public boolean arreglaTarea(String nombreJugador, String nombreTarea) {
        Jugador jugador = mapJugadores.get(nombreJugador);
        if(jugador == null || nombreTarea == null || nombreTarea.isEmpty()){
            return false;
        }
        if(!jugador.isVivo() || jugador.isImpostor()){
            return false;
        }
        List<Tarea> listaTareas = mapSalasListaTareas.get(jugador.getSala());
        if(listaTareas == null || listaTareas.isEmpty()){
            return false;
        }
        for (Tarea tarea : listaTareas) {
            if(tarea.getNombre().equalsIgnoreCase(nombreTarea)){
                if(tarea.isFunciona()){
                    return false;
                }
                tarea.setFunciona(true);
                return true;
            }
        }
        return false;
    }


    public boolean vote(String nombreJugador, String nombreObjetivo) {
        Jugador jugador = mapJugadores.get(nombreJugador);
        Jugador objetivo = mapJugadores.get(nombreObjetivo);
        if (jugador == null || objetivo == null){
            return votacionFinalizada();
        }
        if (!jugador.isVivo()){
            return votacionFinalizada();
        }
        if (!objetivo.isVivo()){
            return votacionFinalizada();
        }
        mapVotos.put(jugador, objetivo);
        return votacionFinalizada();
    }

    public boolean votacionFinalizada() {
        for (Jugador jugador : mapJugadores.values()) {
            if (jugador.isVivo() && mapVotos.containsKey(jugador)){
                return false;
            }
        }
        return true;
    }

    public Jugador resultadoVotacion() {
        Map<Jugador, Integer> votosPorJugador = new HashMap<>();

        for (Jugador objetivo : mapVotos.values()) {
            Integer numVotos = votosPorJugador.get(objetivo);
            if (numVotos == null){
                numVotos = 0;
            }
            numVotos = numVotos + 1;
            votosPorJugador.put(objetivo, numVotos);
        }

        Jugador jugadorConMasVotos = null;
        int numVotosMax = 0;
        for (Map.Entry<Jugador, Integer> entry : votosPorJugador.entrySet()) {
            Jugador jugador = entry.getKey();
            int votos = entry.getValue();
            if(numVotosMax<votos){
                jugadorConMasVotos = jugador;
                numVotosMax = votos;
            }
        }
        // TODO: implementar regla correcta de numero de votos necesarios
        jugadorConMasVotos.setVivo(false);
        actualizarEstadoJuego();
        return jugadorConMasVotos;
    }

    public void actualizarEstadoJuego() {

        if(estadoJuego == EstadoJuego.REUNION && !votacionFinalizada()){
            return;
        }else if(estadoJuego == EstadoJuego.REUNION){
            reubicarJugadores();
        }

        int numImpostores = 0;
        int numNoImpostores = 0;
        for (Jugador jugador : mapJugadores.values()) {
            if(jugador.isVivo() && jugador.isImpostor()){
                numImpostores++;
            } else if(jugador.isVivo() && !jugador.isImpostor()){
                numNoImpostores++;
            }
        }
        if(numImpostores == 0){
            estadoJuego = EstadoJuego.GANAN_TRIPULANTES;
        } else if (numImpostores >= numNoImpostores) {
            estadoJuego = EstadoJuego.GANAN_IMPOSTORES;
        } else {
            estadoJuego = EstadoJuego.JUGANDO;
        }
    }

    private void reubicarJugadores() {
        for (Jugador jugador : mapJugadores.values()) {
            mapSalasListaJugadores.get(jugador.getSala()).remove(jugador);
            int i = (int) (Math.random() * mapSalas.size());
            Sala sala = (new ArrayList<>(mapSalas.values())).get(i);
            jugador.setSala(sala);
            mapSalasListaJugadores.get(sala).add(jugador);
        }
    }

    public EstadoJuego getEstadoDeJuego() {
        return estadoJuego;
    }

    public List<Jugador> getJugadores() {
        return new ArrayList<>(mapJugadores.values());
    }

    public void killJugador(String nombreJugador) {
        mapJugadores.get(nombreJugador).setVivo(false);
    }

    public List<Tarea> getTareasDeSala(Sala sala) {
        return mapSalasListaTareas.get(sala);
    }

    public List<Jugador> getJugadores(Sala sala) {
        return mapSalasListaJugadores.get(sala);
    }


    public void setJugadorReady(String clientName) {
        mapJugadores.get(clientName).setReady(true); // Opción 1
        // Comprobar si todos están READY
        for (Jugador jugador : mapJugadores.values()) {
            if (!jugador.isReady()) {
                return;
            }
        }
        estadoJuego = EstadoJuego.JUGANDO;
        // Definir intrusos
        int numImpostores = (int) Math.ceil(mapJugadores.size() * 0.2);
        // TODO: setJugadorReady
        List<Jugador> jugadores = new ArrayList<>();
        jugadores.addAll(mapJugadores.values());
        Collections.shuffle(jugadores);
        for (int i = 0; i < numImpostores; i++) {
            jugadores.get(i).setImpostor(true);
        }
    }

    public boolean esImpostor(String nombreJugador) {
        Jugador jugador = mapJugadores.get(nombreJugador);
        if (jugador == null){
            return false;
        }
        return jugador.isImpostor();
    }

    public boolean destrozarTarea(String nombreJugador, String nombreTarea) {
        Jugador jugador = mapJugadores.get(nombreJugador);
        if(jugador == null || nombreTarea == null || nombreTarea.isEmpty()){
            return false;
        }
        if(!jugador.isVivo() && !jugador.isImpostor()){
            return false;
        }
        List<Tarea> listaTareas = mapSalasListaTareas.get(jugador.getSala());
        if(listaTareas == null || listaTareas.isEmpty()){
            return false;
        }
        for (Tarea tarea : listaTareas) {
            if(tarea.getNombre().equalsIgnoreCase(nombreTarea)){
                if(tarea.isFunciona() && jugador.isImpostor()){
                    tarea.setFunciona(false);
                    return true;
                };
                return false;
            }
        }
        return false;
    }
}
