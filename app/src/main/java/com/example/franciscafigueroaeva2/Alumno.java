package com.example.franciscafigueroaeva2;

public class Alumno {
    public String rut;
    public String nombre;
    public String seccion;
    public String asignatura;
    public double nota1;
    public double nota2;
    public double nota3;
    public double nota4;
    public int pon1;
    public int pon2;
    public int pon3;
    public int pon4;
    public double promedio;

    public String situacion;

    // constructor vacio necesario para firebase
    public Alumno() {}

    public Alumno(String rut, String nombre, String seccion, String asignatura, double nota1, double nota2,
                  double nota3, double nota4, int pon1, int pon2, int pon3, int pon4, double promedio,String situacion) {
        this.rut = rut;
        this.nombre = nombre;
        this.seccion = seccion;
        this.asignatura = asignatura;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
        this.nota4 = nota4;
        this.pon1 = pon1;
        this.pon2 = pon2;
        this.pon3 = pon3;
        this.pon4 = pon4;
        this.promedio = promedio;
        this.situacion = situacion;
    }
}
