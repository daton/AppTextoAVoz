package com.unitec.apptextoavoz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    //Este objeto es el intermediario entre neustra app y TextToSpeech
     private var tts:TextToSpeech?=null
    //El siguiente codigo de peticion es un entero, que nos va a ayudar a garantizar el objeto TextToSéech
    //SE iniicio completamente
    private val CODIGO_PETICION=100



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Iniciamos ahora i la varible tts para que ya no este en null
        tts= TextToSpeech(this,this)


        hablar.setOnClickListener {
            val intent =Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            try{
                startActivityForResult(intent,CODIGO_PETICION)
            }catch (e:Exception){  }
        }

        //Programamos el clicqueo del boton para que inptete lo escrito
        interpretar.setOnClickListener {
            if(fraseEscrita.text.isEmpty()){
             Toast.makeText(this,"Debes escribir algo para que lo hable",Toast.LENGTH_LONG).show()
            }else{
                //Este metodo ahorita lo vamos a implementar
                hablarTexto(fraseEscrita.text.toString())
            }
        }

        //KEMOSION!!!! VAMOS A ESCUCHAR ESA VOCESITA DE ANDROID, DE BIENVENIDA
        Timer("Bienvenida", false).schedule(1000){
            tts!!.speak(
                  "Hola, bienvenido a mi aplicación mis chavos de UNITEC, espero les encante, oprime el botón  para que te escuche!!",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    ""
            )
        }

    }

    override fun onInit(estado: Int) {
     //ESTE O FUNCION  SIRVE PARA QUE SE INICIALIZE LA CONFIGURACION AL ARRANCAR LA APP.(IDIOMA)
        if(estado==TextToSpeech.SUCCESS){
            //Si el if se cumplio la ejecucion seguira aqui dentro
            var local=Locale("spa","MEX")
            //La siguente variable es para internamemte nosotros sepasmos que la app va bien
            val resultado=tts!!.setLanguage(local)
            if(resultado==TextToSpeech.LANG_MISSING_DATA){
                Log.i("PESIMO","NOOOOOOOOO, NO FUNCIONO EL LENGUAJE, ALGO ANDA MAL")
            }

        }
    }

    //Esta funcion es la que nos ayudaa interpretar lo que se escriba en el texto de la frase
    fun hablarTexto(textoHablar:String){
        tts!!.speak(textoHablar, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    //Este metodo es opcional pero se los recomiendo para limpiar la memoria de esta app cuando la cierren
    override fun onDestroy(){
        super.onDestroy()
        if(tts!=null){
            //En el caso de las aplicaciones de espionaje estos dos renglones NUNCA SE APAGAN.
            tts!!.stop()
            tts!!.shutdown()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODIGO_PETICION->{
                if(resultCode== RESULT_OK && null!=data){
                    val result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    //Finalmente le vamos a decir a nuestro texto estatico que aqui nos muestre lo
                    //Lo que dijimos peor en txto
                    textoIntepretado.setText(result!![0])
                }
            }
        }
    }
}