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
     var edad:Int?=null
    var x:Float?=null
    val pi=3.1416

    //El siguiente codigo de peticion es un entero, que nos va a ayudar a garantizar el objeto TextToSéech
    //SE iniicio completamente
    private val CODIGO_PETICION=100



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Iniciamos ahora i la varible tts para que ya no este en null
        tts= TextToSpeech(this,this)

        //Invocamos la clase Log
        Log.i("XYZ", "Se acaba de iniciar el metodo OnCreate")
        Log.i("XYZ", "Tu edad en dias es "+tuEdadEnDias(21))
        //El signo e pesos en kotlin se conoce como interpolacion de string junto con llaves
        Log.i("XYZ", "Tu edad en dias es ${tuEdadEnDias(21)}  ya sale bien")
        //En kotlin las funciones TAMBIEN SON VARIABLES y su ambito se puede definir solo con llaves
        Log.i("XYZ","La siguiente es otro ejemplo ${4+5} te dara una suma de 9")
        //En kotlin, ademas de ser orienatdo a objetos: TAMBIEN ES FUNCIONAL
        //es decir las funciones son tratadas como una VARIABLE.
        var x=2
        //En Kotlin una funcion puede ser declarada DENTRO DE OTRA PORQUE SON TRATADAS COMO VARIABLES
        fun funcioncita()={
            print("Una funcioncita ya con notacion funcional!!");

        }
        //Otro ejemplo con argumentos
        fun otraFuncion(x:Int, y:Int)={
          println(  "Esta funcion hace la suma de los argumentos que le pases ${x+y}")
        }

        Log.i("XYZ", "Mi primer funcion con notacion funcional ${funcioncita()}  listooo")
        //Se invoca ddirectamente abajo:
        otraFuncion(5,4)

        //Funciones de orden superior y operador lambda

        //Para este ejercicion necesitamos crear una nueva clase
        class Ejemplito:(Int)->Int{
            override fun invoke(p1: Int): Int {
                TODO("Not yet implemented")
            }

        }


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

    //Implementamos un metodo o funcion, que es lo mismo
    fun  saludar( mensaje:String){
        Log.i("HOLA", "Un mensaje dentro de kotlin")
    }

    fun saludar2(mensaje:String):String{

        return "Mi mensaje de bienvenida"
    }

    fun tuEdadEnDias(edad:Int):Int{
        val diasAnio=365

        return diasAnio*edad
    }


}