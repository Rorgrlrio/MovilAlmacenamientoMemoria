package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.Guard;

public class ArchivoActivity extends AppCompatActivity {
    
    //Objetos para asociar los componentes
    EditText nombreArchivo;
    Button accion;
    
    //Variables para identificar accion y contenido a guardar en archivo
    String contenido = "";
    int tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivo);
        
        //Asociar objetos con componenetes
        nombreArchivo = (EditText) findViewById(R.id.txtNombreArchivo);
        accion = (Button) findViewById(R.id.btnAccion);
        
        //Se identifica el tipo de accion 1-Abrir 2-Guardar por default 1
        tipo = (int) getIntent().getIntExtra("tipo", 1);
        
        //Se cambio el nombre del boton
        switch(tipo){
            case 1: accion.setText("Abrir"); break;
            case 2: accion.setText("Guardar");
                //Se obtine la informacion a guardar
                contenido = (String) getIntent().getStringExtra("contenido");
                break;
        }//switch
        
    }//onCreate

    public void realizaAccion(View view){
        //Se obtiene lo escrito en el cuadro de texto
        String nomArch = nombreArchivo.getText().toString();

        //Se valida que no este vacio
        if(nomArch != ""){
            //De acuerdo al tipo de accion se ejecuta el método correspondiente
            switch(tipo){
                case 1: abrirArchivoSD(nomArch); break;
                case 2: guardarArchivoSD(nomArch); break;
            }//switch
            finish();

        }else{
            Toast.makeText(this, "Debes escribir un nombre", Toast.LENGTH_SHORT).show();
        }
    } //realizaAccion
    
    private void abrirArchivoSD(String n){
        try{
            //Se obtiene la ruta de almcaenamiento externo
            File tarjetaSD = Environment.getExternalStorageDirectory();
            //Se define el nombre del archivo y la ruta donde sera guardado
            File archivo = new File(tarjetaSD.getPath(), n);
            //Abrir el archivo
            InputStreamReader abrirArchivo = new InputStreamReader(openFileInput(archivo.getName()));
            //Asocia el flujo con el archivo para obtener la informacion
            BufferedReader leerArchivo = new BufferedReader(abrirArchivo);
            //Lectura del archivo y colocar en una variable de tipo cadena
            String linea = leerArchivo.readLine();
            //Variable que guarddara la informacion leida desde el archivo, se inicializa en vacio
            String textoLeido = "";
            
            //Ciclo para leer el contenido del archivo
            while (linea !=null){
                textoLeido += linea + '\n';
                linea = leerArchivo.readLine();
            }//while
            
            //Cerrar el fluijo del archivo
            leerArchivo.close();
            //Cerrar el archivo
            abrirArchivo.close();
            
            //Objeto para conectar a otra activity para guardar archivo
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("contenido", textoLeido);
            
            //Iniciar la activity
            startActivity(intent);
            
        }catch (IOException e){
            Toast.makeText(this, "El archivo no se pudo leer", Toast.LENGTH_SHORT).show();
        }
    }//abrirArchivoSD

    private  void guardarArchivoSD(String n){

        try{
            //Se obtiene la ruta del almacenamiento externo
            File tarjetaSD = Environment.getExternalStorageDirectory();
            //Se muestra la ruta
            Toast.makeText(this, tarjetaSD.getPath(), Toast.LENGTH_SHORT).show();
            //Se define rl nombre del archivo y la ruta donde sera guardado
            File archivo = new File(tarjetaSD.getPath(), n);
            //Crea el archivo y establece el modo de acceso
            OutputStreamWriter crearArchivo = new OutputStreamWriter(openFileOutput(archivo.getName(), Activity.MODE_PRIVATE));
            //Se escribe el contenido en el archivo
            crearArchivo.write(contenido);
            //Se limpia el buffer
            crearArchivo.flush();
            //Se cierra el archivo
            crearArchivo.close();
            Toast.makeText(this, "Informacion almacenada !!!", Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            Toast.makeText(this, "El archivo no se pudo guardar", Toast.LENGTH_SHORT).show();
        }//catch

    }//guardarArchivoSD
    
}