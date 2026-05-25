package edu.masanz.da.en;

public class Tarea implements Comparable<Tarea> {

    private String nombre;
    private boolean funciona;

    public Tarea(String nombre) {
        this.nombre = nombre;
        funciona = false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isFunciona() {
        return funciona;
    }

    public void setFunciona(boolean funciona) {
        this.funciona = funciona;
    }

    @Override
    public int compareTo(Tarea other) {
        return this.nombre.compareTo(other.nombre);
    }

}
