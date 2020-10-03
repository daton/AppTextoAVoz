package com.unitec.apptextoavoz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
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
}