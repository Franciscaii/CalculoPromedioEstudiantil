package com.example.franciscafigueroaeva2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Spinner menu_asignaturas;
    ArrayAdapter<CharSequence> adaptador;
    EditText editxt_pon1, editxt_pon2, editxt_pon3, editxt_pon4;
    EditText editxt_nota1, editxt_nota2, editxt_nota3, editxt_nota4;
    EditText rut, nombre, seccion;
    Button btnCalcular, btnAgregar,botonbuscar,btnModificar,btnEliminar;
    TextView texvisituacion;
    String asignaturaSeleccionada;
    boolean isFirstSelection = true;
    Map<String, int[]> ponderacionesPorAsignatura;
    boolean promedioCalculado = false;
    // inicializa el listview
    private ListView listViewAlumnos;
    private ArrayAdapter<String> alumnoAdapter;
    private ArrayList<String> alumnosList = new ArrayList<>();
    // firebase
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // inicializar el listview y su adaptador
        listViewAlumnos = findViewById(R.id.lstListaAlumnos);
        alumnoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alumnosList);
        listViewAlumnos.setAdapter(alumnoAdapter);
        // inicializar firebase
        FirebaseApp.initializeApp(this);
        // obtener la referencia a la base de datos de firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("alumnos");

        // escuchar los cambios en los datos de firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alumnosList.clear(); // Limpiar la lista antes de agregar nuevos datos
                // Recorrer los datos recibidos desde Firebase
                for (DataSnapshot alumnoSnapshot : dataSnapshot.getChildren()) {
                    Alumno alumno = alumnoSnapshot.getValue(Alumno.class);
                    if (alumno != null) {
                        String displayText = "Rut: "+ alumno.rut + "\n" + "Nombre:" + alumno.nombre + "\n" + "Asignatura: "
                                + alumno.asignatura + "\n" + "Sección:" +alumno.seccion +"\n" + "Nota1:"+ alumno.nota1 + "\n" + "nota2:"+ alumno.nota2
                                + "\n" + "Nota3:"+ alumno.nota3 + "\n" + "Nota4:"+ alumno.nota4 + "\n" + "Pon1:" + alumno.pon1
                                + "\n" + "Pon2:" + alumno.pon2 + "\n" + "Pon3:" + alumno.pon3 + "\n" + "Pon4:" + alumno.pon4
                                + "\n" +"Promedio:" + alumno.promedio + "\n" + "Situación:" + alumno.situacion;
                        alumnosList.add(displayText); // agregar el texto del alumno a la lista
                    }
                }
                alumnoAdapter.notifyDataSetChanged(); // notificar al adaptador de los cambios
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al leer los datos", databaseError.toException());
            }
        });
        // inicializar componentes
        menu_asignaturas = findViewById(R.id.Asignaturas);
        editxt_pon1 = findViewById(R.id.editxt_pon1);
        editxt_pon2 = findViewById(R.id.editxt_pon2);
        editxt_pon3 = findViewById(R.id.editxt_pon3);
        editxt_pon4 = findViewById(R.id.editxt_pon4);
        editxt_nota1 = findViewById(R.id.editxt_nota1);
        editxt_nota2 = findViewById(R.id.editxt_nota2);
        editxt_nota3 = findViewById(R.id.editxt_nota3);
        editxt_nota4 = findViewById(R.id.editxt_nota4);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnAgregar = findViewById(R.id.btnAgregar);
        botonbuscar = findViewById(R.id.btnBuscar);
        btnModificar=findViewById(R.id.btnModificar);
        btnEliminar=findViewById(R.id.btnEliminar);
        texvisituacion = findViewById(R.id.texvisituacion);
        rut = findViewById(R.id.editRut);
        nombre = findViewById(R.id.editNombre);
        seccion = findViewById(R.id.editSeccion);
        adaptador = ArrayAdapter.createFromResource(this, R.array.valoresArray,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        menu_asignaturas.setAdapter(adaptador);
        // inicializar mapa de ponderaciones
        inicializarPonderaciones();
        // configurar listener del spinner
        menu_asignaturas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                asignaturaSeleccionada = adaptador.getItem(i).toString();
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }
                if (!asignaturaSeleccionada.equals("Seleccione una asignatura")) {
                    Toast.makeText(getApplicationContext(), "Asignatura seleccionada: " + asignaturaSeleccionada, Toast.LENGTH_LONG).show();
                    int[] ponderaciones = ponderacionesPorAsignatura.get(asignaturaSeleccionada);
                    if (ponderaciones != null) {
                        editxt_pon1.setText(String.valueOf(ponderaciones[0]));
                        editxt_pon2.setText(String.valueOf(ponderaciones[1]));
                        editxt_pon3.setText(String.valueOf(ponderaciones[2]));
                        editxt_pon4.setText(String.valueOf(ponderaciones[3]));
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        // listener del boton modificar
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ModificarInformacion();}
        });
        // listener del boton buscar
        botonbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { buscarAlumno();}
        });
        // listener del boton calcular
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {calcularPromedio();}
        });
        // listener del boton agregar
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {agregarAlumnoAFirebase();}
        });
        // listener del boton eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {eliminarAlumno();}
        });
    }
    // metodo para inicializar las ponderaciones de cada asignatura
    private void inicializarPonderaciones() {
        ponderacionesPorAsignatura = new HashMap<>();
        ponderacionesPorAsignatura.put("Aplicaciones Móviles Para Iot", new int[]{15, 25, 30, 30});
        ponderacionesPorAsignatura.put("Desarrollo De Videojuegos", new int[]{20, 30, 25, 25});
        ponderacionesPorAsignatura.put("Ingeniería De Software", new int[]{25, 25, 25, 25});
        ponderacionesPorAsignatura.put("Programación Back End", new int[]{10, 20, 30, 40});
        ponderacionesPorAsignatura.put("Taller De Diseño Y Desarrollo De Soluciones", new int[]{30, 20, 20, 30});
    }
    // metodo para validar el rut
    private boolean validarRut(String rut) {
        // eliminar puntos y guiones del rut ingresado
        rut = rut.replace(".", "").replace("-", "");
        // el rut debe tener al menos 8 caracteres
        if (rut.length() < 8 || rut.length() > 9) {
            return false; // rut invalido
        }
        // el ultimo caracter es el digito verificador ,puede ser un numero o una K
        char digitoVerificador = rut.charAt(rut.length() - 1);
        // convertir el digito verificador a mayuscula
        digitoVerificador = Character.toUpperCase(digitoVerificador);
        // los demas caracteres deben ser solo numeros
        String cuerpoRut = rut.substring(0, rut.length() - 1);
        if (!cuerpoRut.matches("\\d+")) {
            return false; // el cuerpo del rut debe ser numerico
        }
        int suma = 0;
        int multiplicador = 2;
        // se recorren los numeros del rut desde el ultimo hasta el primero
        for (int i = cuerpoRut.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(cuerpoRut.charAt(i)) * multiplicador;
            multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
        }
        int resto = 11 - (suma % 11);
        char digitoCalculado;
        // convertir el resultado en el digito verificador correspondiente
        if (resto == 11) {
            digitoCalculado = '0';
        } else if (resto == 10) {
            digitoCalculado = 'K';
        } else {
            digitoCalculado = (char) (resto + '0');
        }
        // comparar el digito calculado con el proporcionado
        if (digitoVerificador != digitoCalculado) {
            Toast.makeText(getApplicationContext(), "El RUT ingresado no es válido", Toast.LENGTH_LONG).show();
            return false;
        }
        return true; // rut valido
    }
    // metodo para calcular el promedio
    public Map<String, Object> calcularPromedio() {
        Map<String, Object> resultado = new HashMap<>(); // inicializar el map que se retornara
        try {
            // validar que rut, nombre y seccion no esten vacios
            if (rut.getText().toString().trim().isEmpty() ||
                    nombre.getText().toString().trim().isEmpty() ||
                    seccion.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, ingresa el Rut, Nombre y Sección antes de calcular el promedio.", Toast.LENGTH_LONG).show();
                return resultado; // retornar el map vacio
            }
            // contadores para verificar cuantas notas y ponderaciones faltan
            int notasFaltantes = 0;
            int ponderacionesFaltantes = 0;
            // verificar si se han ingresado todas las notas
            if (editxt_nota1.getText().toString().trim().isEmpty()) {
                notasFaltantes++;
            }
            if (editxt_nota2.getText().toString().trim().isEmpty()) {
                notasFaltantes++;
            }
            if (editxt_nota3.getText().toString().trim().isEmpty()) {
                notasFaltantes++;
            }
            if (editxt_nota4.getText().toString().trim().isEmpty()) {
                notasFaltantes++;
            }
            // verificar si se han ingresado todas las ponderaciones
            if (editxt_pon1.getText().toString().trim().isEmpty()) {
                ponderacionesFaltantes++;
            }
            if (editxt_pon2.getText().toString().trim().isEmpty()) {
                ponderacionesFaltantes++;
            }
            if (editxt_pon3.getText().toString().trim().isEmpty()) {
                ponderacionesFaltantes++;
            }
            if (editxt_pon4.getText().toString().trim().isEmpty()) {
                ponderacionesFaltantes++;
            }
            if (notasFaltantes > 0) {
                if(notasFaltantes == 1){
                    Toast.makeText(getApplicationContext(), "Falta ingresar al menos " + notasFaltantes + " nota", Toast.LENGTH_LONG).show();
                    return resultado;
                } else if (notasFaltantes > 1){
                    Toast.makeText(getApplicationContext(), "Falta ingresar al menos " + notasFaltantes + " notas", Toast.LENGTH_LONG).show();
                    return resultado;
                }
            }
            if (ponderacionesFaltantes > 0) {
                if(ponderacionesFaltantes ==1 ){
                    Toast.makeText(getApplicationContext(), "Falta ingresar " + ponderacionesFaltantes + " ponderacion", Toast.LENGTH_LONG).show();
                    return resultado;
                }else if (ponderacionesFaltantes > 1 ){
                    Toast.makeText(getApplicationContext(), "Falta ingresar " + ponderacionesFaltantes + " ponderaciones", Toast.LENGTH_LONG).show();
                    return resultado;
                }
            }
            // validar rut
            String Rut = rut.getText().toString().trim();
            if (!validarRut(Rut)) {
                return resultado; // Si el rut es invalido, retornar map vacio
            }
            // obtener notas y ponderaciones
            double nota1 = Double.parseDouble(editxt_nota1.getText().toString());
            double nota2 = Double.parseDouble(editxt_nota2.getText().toString());
            double nota3 = Double.parseDouble(editxt_nota3.getText().toString());
            double nota4 = Double.parseDouble(editxt_nota4.getText().toString());
            int pon1 = Integer.parseInt(editxt_pon1.getText().toString());
            int pon2 = Integer.parseInt(editxt_pon2.getText().toString());
            int pon3 = Integer.parseInt(editxt_pon3.getText().toString());
            int pon4 = Integer.parseInt(editxt_pon4.getText().toString());
            int sumaPonderaciones = pon1 + pon2 + pon3 + pon4;
            // validar que las notas esten en el rango de 1.0 a 7.0
            if (nota1 < 1.0 || nota1 > 7.0 || nota2 < 1.0 || nota2 > 7.0 ||
                    nota3 < 1.0 || nota3 > 7.0 || nota4 < 1.0 || nota4 > 7.0) {
                Toast.makeText(getApplicationContext(), "Las notas deben estar entre 1.0 y 7.0", Toast.LENGTH_LONG).show();
                return resultado; // retornar el map vacio si alguna nota no es valida
            }
            // validar que las ponderaciones sumen 100%
            if (sumaPonderaciones != 100) {
                Toast.makeText(getApplicationContext(), "Las ponderaciones deben sumar exactamente 100%", Toast.LENGTH_LONG).show();
                return resultado; // retornar el map vacio si las ponderaciones no suman 100%
            }
            // calcular el promedio ponderado
            double promedio = (nota1 * pon1 + nota2 * pon2 + nota3 * pon3 + nota4 * pon4) / 100;
            // verificar si el alumno aprobo o reprobo
            String situacion = promedio >= 4.0 ? "Aprobado" : "Reprobado";
            // mostrar el promedio y la situacion del alumno en el textview
            texvisituacion.setText("Promedio: " + String.format("%.1f", promedio) + " - Situación: " + situacion);
            promedioCalculado = true;
            // almacenar el resultado en el map
            resultado.put("promedio", promedio);
            resultado.put("situacion", situacion);
            return resultado;
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Por favor, ingresa todas las notas y ponderaciones correctamente.", Toast.LENGTH_LONG).show();
            return resultado; // retornar el map vacio en caso de error
        }
    }
    private void agregarAlumnoAFirebase() {
        if (!promedioCalculado) {
            Toast.makeText(getApplicationContext(), "Debes calcular el promedio antes de agregar al alumno.", Toast.LENGTH_LONG).show();
            return;
        }
        // obtener los valores del promedio y la situacion
        Map<String, Object> resultado = calcularPromedio();
        // verificar si el calculo del promedio retorno valores validos
        if (resultado.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Hubo un error al calcular el promedio. Por favor, verifica los datos ingresados.", Toast.LENGTH_LONG).show();
            return;
        }
        // obtener datos del alumno
        double promedio = (double) resultado.get("promedio");
        String situacion = (String) resultado.get("situacion");
        String Rut = rut.getText().toString().trim();
        String Nombre = nombre.getText().toString().trim();
        String Seccion = seccion.getText().toString().trim();
        String Asignatura = asignaturaSeleccionada;
        double nota1 = Double.parseDouble(editxt_nota1.getText().toString());
        double nota2 = Double.parseDouble(editxt_nota2.getText().toString());
        double nota3 = Double.parseDouble(editxt_nota3.getText().toString());
        double nota4 = Double.parseDouble(editxt_nota4.getText().toString());
        int pon1 = Integer.parseInt(editxt_pon1.getText().toString());
        int pon2 = Integer.parseInt(editxt_pon2.getText().toString());
        int pon3 = Integer.parseInt(editxt_pon3.getText().toString());
        int pon4 = Integer.parseInt(editxt_pon4.getText().toString());
        // crear map con todos los datos del alumno para firebase
        Map<String, Object> alumno = new HashMap<>();
        alumno.put("rut", Rut);
        alumno.put("nombre", Nombre);
        alumno.put("seccion", Seccion);
        alumno.put("asignatura", Asignatura);
        alumno.put("nota1", nota1);
        alumno.put("nota2", nota2);
        alumno.put("nota3", nota3);
        alumno.put("nota4", nota4);
        alumno.put("pon1", pon1);
        alumno.put("pon2", pon2);
        alumno.put("pon3", pon3);
        alumno.put("pon4", pon4);
        alumno.put("promedio", promedio); // promedio calculado
        alumno.put("situacion", situacion); // situación calculada ,Aprobado o Reprobado
        // guardar los datos en firebase
        databaseReference.push().setValue(alumno).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Alumno agregado correctamente a Firebase", Toast.LENGTH_LONG).show();
                limpiarCampos(); // limpiar los campos despues de agregar al alumno
            } else {
                Toast.makeText(getApplicationContext(), "Error al agregar alumno a Firebase", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void buscarAlumno() {
        String Rut = rut.getText().toString().trim();
        String asignatura = menu_asignaturas.getSelectedItem().toString();
        // consultar a firebase
        databaseReference.orderByChild("rut").equalTo(Rut).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean alumnoEncontrado = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Alumno alumno = snapshot.getValue(Alumno.class);
                    if (alumno != null && alumno.asignatura.equals(asignatura)) {
                        // llenar los campos con la informacion del alumno
                        nombre.setText(alumno.nombre);
                        seccion.setText(alumno.seccion);
                        editxt_nota1.setText(String.valueOf(alumno.nota1));
                        editxt_nota2.setText(String.valueOf(alumno.nota2));
                        editxt_nota3.setText(String.valueOf(alumno.nota3));
                        editxt_nota4.setText(String.valueOf(alumno.nota4));
                        editxt_pon1.setText(String.valueOf(alumno.pon1));
                        editxt_pon2.setText(String.valueOf(alumno.pon2));
                        editxt_pon3.setText(String.valueOf(alumno.pon3));
                        editxt_pon4.setText(String.valueOf(alumno.pon4));
                        texvisituacion.setText("Promedio: " + alumno.promedio + " - Situación: " + alumno.situacion);
                        alumnoEncontrado = true;
                        break; // salir del bucle si encuentra al alumno
                    }
                }
                // si no encuentra al alumno, mostrar un mensaje
                if (!alumnoEncontrado) {
                    Toast.makeText(MainActivity.this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error al acceder a los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean calculoRealizado = false; // nueva variable para rastrear si el calculo fue realizado

    private void ModificarInformacion() {
        String Rut = rut.getText().toString().trim();
        String asignatura = menu_asignaturas.getSelectedItem().toString();

        // consultar a firebase
        databaseReference.orderByChild("rut").equalTo(Rut).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Alumno[] alumno = {null}; // usar array para permitir la modificacion dentro de la clase interna
                final String[] alumnoKey = {null}; // almacenar la clave aqui
                boolean alumnoEncontrado = false;

                for (DataSnapshot alumnoSnapshot : dataSnapshot.getChildren()) {
                    Alumno tempAlumno = alumnoSnapshot.getValue(Alumno.class);
                    if (tempAlumno != null && tempAlumno.asignatura.equals(asignatura)) {
                        alumno[0] = tempAlumno;
                        alumnoKey[0] = alumnoSnapshot.getKey(); // guardar clave del alumno

                        // rellenar los campos con la informacion del alumno
                        nombre.setText(alumno[0].nombre);
                        seccion.setText(alumno[0].seccion);
                        editxt_nota1.setText(String.valueOf(alumno[0].nota1));
                        editxt_nota2.setText(String.valueOf(alumno[0].nota2));
                        editxt_nota3.setText(String.valueOf(alumno[0].nota3));
                        editxt_nota4.setText(String.valueOf(alumno[0].nota4));
                        editxt_pon1.setText(String.valueOf(alumno[0].pon1));
                        editxt_pon2.setText(String.valueOf(alumno[0].pon2));
                        editxt_pon3.setText(String.valueOf(alumno[0].pon3));
                        editxt_pon4.setText(String.valueOf(alumno[0].pon4));
                        texvisituacion.setText("Promedio: " + alumno[0].promedio + " - Situación: " + alumno[0].situacion);
                        alumnoEncontrado = true;
                        break; // salir del bucle si encuentra al alumno
                    }
                }

                // si no se encuentra el alumno, mostrar un mensaje
                if (!alumnoEncontrado) {
                    Toast.makeText(MainActivity.this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                // manejar el evento del boton calcular
                btnCalcular.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // reutilizar el metodo calcularpromedio
                        Map<String, Object> resultado = calcularPromedio();

                        // validar el resultado
                        if (resultado.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Debe calcular el promedio antes de modificar", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // actualizar el promedio y la situacion
                        alumno[0].promedio = (double) resultado.get("promedio");
                        alumno[0].situacion = (String) resultado.get("situacion");

                        // establecer que el calculo fue realizado
                        calculoRealizado = true; // calculo fue hecho

                        Toast.makeText(MainActivity.this, "Pinche nuevamente en modificar para guardar los cambios", Toast.LENGTH_SHORT).show();
                    }
                });

                // manejar el evento del boton modificar
                btnModificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // validar si hay campos vacios
                        if (nombre.getText().toString().isEmpty() || seccion.getText().toString().isEmpty() ||
                                editxt_nota1.getText().toString().isEmpty() || editxt_pon1.getText().toString().isEmpty() ||
                                editxt_nota2.getText().toString().isEmpty() || editxt_pon2.getText().toString().isEmpty() ||
                                editxt_nota3.getText().toString().isEmpty() || editxt_pon3.getText().toString().isEmpty() ||
                                editxt_nota4.getText().toString().isEmpty() || editxt_pon4.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "No puede dejar campos vacíos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // si se cambian las notas o ponderaciones, se debe calcular primero
                        if (!calculoRealizado && (
                                alumno[0].nota1 != Double.parseDouble(editxt_nota1.getText().toString()) ||
                                        alumno[0].nota2 != Double.parseDouble(editxt_nota2.getText().toString()) ||
                                        alumno[0].nota3 != Double.parseDouble(editxt_nota3.getText().toString()) ||
                                        alumno[0].nota4 != Double.parseDouble(editxt_nota4.getText().toString()) ||
                                        alumno[0].pon1 != Integer.parseInt(editxt_pon1.getText().toString()) ||
                                        alumno[0].pon2 != Integer.parseInt(editxt_pon2.getText().toString()) ||
                                        alumno[0].pon3 != Integer.parseInt(editxt_pon3.getText().toString()) ||
                                        alumno[0].pon4 != Integer.parseInt(editxt_pon4.getText().toString())
                        )) {
                            Toast.makeText(MainActivity.this, "Debe calcular antes de modificar las notas o ponderaciones", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // actualizar los valores del alumno
                        alumno[0].nombre = nombre.getText().toString();
                        alumno[0].seccion = seccion.getText().toString();
                        alumno[0].nota1 = Double.parseDouble(editxt_nota1.getText().toString());
                        alumno[0].pon1 = Integer.parseInt(editxt_pon1.getText().toString());
                        alumno[0].nota2 = Double.parseDouble(editxt_nota2.getText().toString());
                        alumno[0].pon2 = Integer.parseInt(editxt_pon2.getText().toString());
                        alumno[0].nota3 = Double.parseDouble(editxt_nota3.getText().toString());
                        alumno[0].pon3 = Integer.parseInt(editxt_pon3.getText().toString());
                        alumno[0].nota4 = Double.parseDouble(editxt_nota4.getText().toString());
                        alumno[0].pon4 = Integer.parseInt(editxt_pon4.getText().toString());

                        // actualizar los datos en firebase usando la clave guardada
                        databaseReference.child(alumnoKey[0]).setValue(alumno[0]).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Modificación realizada con éxito", Toast.LENGTH_SHORT).show();
                                texvisituacion.setText("Promedio: " + String.format("%.1f", alumno[0].promedio) + " - Situación: " + alumno[0].situacion);
                                limpiarCampos();
                                calculoRealizado = false; // reiniciar la bandera para la proxima modificación

                            } else {
                                Toast.makeText(MainActivity.this, "Error al modificar los datos", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // manejo de errores
            }
        });
    }

    private void eliminarAlumno() {
        String Rut = rut.getText().toString().trim();
        String asignatura = menu_asignaturas.getSelectedItem().toString();

        // validar que el rut y la asignatura no esten vacíos
        if (Rut.isEmpty() || asignatura.equals("Seleccione una asignatura")) {
            Toast.makeText(MainActivity.this, "Debes ingresar el RUT y seleccionar una asignatura", Toast.LENGTH_SHORT).show();
            return;
        }

        // consultar en firebase usando el rut
        databaseReference.orderByChild("rut").equalTo(Rut).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean alumnoEncontrado = false;

                for (DataSnapshot alumnoSnapshot : dataSnapshot.getChildren()) {
                    Alumno alumno = alumnoSnapshot.getValue(Alumno.class);

                    // verificar que el alumno existe y coincide la asignatura
                    if (alumno != null && alumno.asignatura.equals(asignatura)) {
                        alumnoSnapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Alumno eliminado correctamente", Toast.LENGTH_SHORT).show();
                                limpiarCampos(); // Limpiar los campos después de eliminar al alumno
                            } else {
                                Toast.makeText(MainActivity.this, "Error al eliminar alumno", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alumnoEncontrado = true;
                        break; // detener la busqueda despues de encontrar y eliminar al alumno
                    }
                }

                // si no se encuentra el alumno, mostrar un mensaje
                if (!alumnoEncontrado) {
                    Toast.makeText(MainActivity.this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error al acceder a los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        rut.setText("");
        nombre.setText("");
        seccion.setText("");
        editxt_nota1.setText("");
        editxt_nota2.setText("");
        editxt_nota3.setText("");
        editxt_nota4.setText("");
        editxt_pon1.setText("");
        editxt_pon2.setText("");
        editxt_pon3.setText("");
        editxt_pon4.setText("");
        texvisituacion.setText("");
        menu_asignaturas.setSelection(0); // volver a la opción "Seleccione una asignatura"
        promedioCalculado = false;
    }
}





