package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    public EditText editor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asociando componente con objeto
        editor = (EditText) findViewById(R.id.txtEditor);

        //Mostrar el contenido leido de la tarjeta SD
        editor.setText((String) getIntent().getStringExtra("contenido"));
    }

    //Método para mostrar u ocultar el menu dentro del activity
    public boolean onCreateOptionsMenu(Menu menu){
        //Se asocia el xml del menu al Activity
        getMenuInflater().inflate(R.menu.menu_archivo, menu);
        return true;
    }//onCreateOptionsMenu

    //Método para asignar funcionalidad a cada opcion del menu
    public boolean onOptionsItemSelectedMenu(MenuItem item){
        //Variable para identificar la opcion
        int id = item.getItemId();

        switch(id){
            case R.id.itemAbrir:abrirArchivo(); break;
            case R.id.itemGuardar: guardarArchivo(); break;
            case R.id.itemAbrirSD:
                //Objeto para conectar a otra activity para abrir archivo
                intent = new Intent(this, ArchivoActivity.class);
                //Envio el tipo de accion 1- Abrir;
                intent.putExtra("tipo", 1);
                //Iniciar la activity
                startActivity(intent);
                break;
            case R.id.itemGuardarSD:
                //Objeto para conectar a otra activyty para guardar archivo
                intent = new Intent(this, ArchivoActivity.class);
                //Envio el tipo de accion 2- guardar
                intent.putExtra("tipo", 2);
                intent.putExtra("contenido", editor.getText().toString());
                //Iniciar Activity
                startActivity(intent);
                //Limpiar el componente de texto
                editor.setText("");
                break;
        }//switch

        return super.onOptionsItemSelected(item);

    }//onOptionsItemSelected

    private void abrirArchivo(){
        //Obtemer la lista de los archivos internos de la aplicacion
        String archivos[] = fileList();

        //Validar la existencia del archivo interno
        if(existeArchivo(archivos, "appclase10.txt")){
            try{
                //Objeto que asocia el archivo especificado para lectura
                InputStreamReader archivoInterno = new InputStreamReader(openFileInput("appclase10.txt"));

                //Objeto que relaciona el archivo con el flujo de entrada (lectura)
                BufferedReader leerArchivo = new BufferedReader(archivoInterno);

                //Lectura del archivo y colocar en una variable de tipo cadena
                String linea = leerArchivo.readLine();

                //Variable que guardará la información leida desde el archivo, se inicializara
                String textoLeido = "";

                //Ciclo para leer el contenido del archivo
                while(linea != null){
                    textoLeido += linea + '\n';
                    linea = leerArchivo.readLine();
                }//while

                //Cerrar el flujo del archivo
                leerArchivo.close();
                //Cerrar el archivo
                archivoInterno.close();

                //Se coloca la informacion del archivo en el componente multiline text
                editor.setText(textoLeido);

            }catch (IOException e){
                Toast.makeText(MainActivity.this, "Error al leer el archivo.",
                        Toast.LENGTH_SHORT).show();
            }//catch
        }else{
            Toast.makeText(this, "Error al leer el archivo.",
            Toast.LENGTH_SHORT).show();
        } //if

    }//abrirArchivo

    private boolean existeArchivo(String[] archivos, String s){
        //Recorrido de la lista de nombres archivos internos de la aplicacion
        for(int i=0; i < archivos.length; i++)
            //Se busca el archivo especificado
            if(s.equals(archivos[i]))
                //En caso de existir, termina el ciclo y retorna verdadero
                return true;

        //En caso de no existir ua recorrido el archivo de archivos, retorna falso
        return false;
    }

    private void guardarArchivo(){
        try{
            //Objeto que asocia al archivo especificado para escritura
            OutputStreamWriter archivoInterno = new OutputStreamWriter(
                    openFileOutput("appclase10.txt",
                            Activity.MODE_PRIVATE
                    ));
        }catch (IOException e){
            Toast.makeText(this, "Erroor al escribir en el archivo", Toast.LENGTH_SHORT).show();
        }

        //Notificacion de informacion almacenada
        Toast.makeText(this, "Archivo guardado", Toast.LENGTH_SHORT).show();
        //Limpiar el componente de texto
        editor.setText("");

    }//guardarArchivo

}



















