package edu.masanz.da.en;

import java.util.ArrayList;
import java.util.List;

public class Sala implements Comparable<Sala> {

    private String nombre;
    private List<Sala> salasAdyacentes;

    public Sala(String nombre) {
        this.nombre = nombre;
        salasAdyacentes = new ArrayList<>();
    }

    // region getters & setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Sala> getSalasAdyacentes() {
        return salasAdyacentes;
    }

    public void setSalasAdyacentes(List<Sala> salasAdyacentes) {
        this.salasAdyacentes = salasAdyacentes;
    }

    // endregion

    public void addSalaAdyacente(Sala sala){
        salasAdyacentes.add(sala);
    }

    public boolean isAdyacente(Sala sala){
        return salasAdyacentes.contains(sala);
    }

    public boolean isAdyacente(String nombreSala){
        for (Sala sala : salasAdyacentes) {
            if (sala.getNombre().equalsIgnoreCase(nombreSala)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Sala other) {
        return this.nombre.toUpperCase().compareTo(other.nombre.toUpperCase());
    }

}
