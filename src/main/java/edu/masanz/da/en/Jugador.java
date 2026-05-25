package edu.masanz.da.en;

import java.util.Objects;

public class Jugador implements Comparable<Jugador> {

    private String nombre;
    private boolean impostor;
    private boolean vivo;
    private Sala sala;
    private boolean ready;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.impostor = false;
        this.vivo = true;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isImpostor() {
        return impostor;
    }

    public void setImpostor(boolean impostor) {
        this.impostor = impostor;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(nombre, jugador.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }


    @Override
    public int compareTo(Jugador other) {
        return this.nombre.compareTo(other.nombre);
    }

    public void setReady(boolean b) {
        this.ready = b;
    }

    public boolean isReady() {
        return ready;
    }
}
