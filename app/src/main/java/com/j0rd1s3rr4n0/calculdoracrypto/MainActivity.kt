package com.j0rd1s3rr4n0.calculdoracrypto

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    var value_api_data : Double = 0.0
    lateinit var txtView  : TextView
    lateinit var txtRes   : TextView
    lateinit var btnOne   : Button
    lateinit var btnTwo   : Button
    lateinit var btnThree : Button
    lateinit var btnFour  : Button
    lateinit var btnFive  : Button
    lateinit var btnSix   : Button
    lateinit var btnSeven : Button
    lateinit var btnEight : Button
    lateinit var btnNine  : Button
    lateinit var btnZero  : Button
    lateinit var btnComa  : Button
    lateinit var coinOne  : TextView
    lateinit var coinTwo  : TextView

    val API_KEY_COINMARKETCAP :String = "bc482525-aea3-451f-98b4-7192dd7c2056"
    val API_URL : String = "https://pro-api.coinmarketcap.com/v2/tools/price-conversion"

    var val_txt_data : String = ""
    var val_res_data : String = ""
    private var val_txtcoin_data : String = ""
    private var val_rescoin_data : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtView  = findViewById(R.id.tv_one)
        txtRes   = findViewById(R.id.tv_res)
        btnOne   = findViewById(R.id.btn_one)
        btnTwo   = findViewById(R.id.btn_two)
        btnThree = findViewById(R.id.btn_three)
        btnFour  = findViewById(R.id.btn_four)
        btnFive  = findViewById(R.id.btn_five)
        btnSix   = findViewById(R.id.btn_six)
        btnSeven = findViewById(R.id.btn_seven)
        btnEight = findViewById(R.id.btn_eight)
        btnNine  = findViewById(R.id.btn_nine)
        btnZero  = findViewById(R.id.btn_zero)
        btnComa  = findViewById(R.id.btn_coma)
        coinOne  = findViewById(R.id.tv_coin_2)
        coinTwo  = findViewById(R.id.tv_coin_1)
        if(savedInstanceState!=null){
            txtView.text = savedInstanceState?.getString("txtview_data").toString()
            txtRes.text = savedInstanceState?.getString("txtres_data").toString()
            coinOne.text = savedInstanceState?.getString("coinone_data").toString()
            coinTwo.text = savedInstanceState?.getString("cointwo_data").toString()
        }
        val_txt_data = txtView.text.toString()
        val_res_data = txtRes.text.toString()
        val_txtcoin_data = coinOne.text.toString()
        val_rescoin_data = coinTwo.text.toString()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("txtview_data", val_txt_data)
        outState.putString("txtres_data", val_res_data)
        outState.putString("coinone_data", val_txtcoin_data)
        outState.putString("cointwo_data", val_rescoin_data)
    }



    fun alertSnackBar(texto:String,color:String?){
        val parentLayout = findViewById<View>(android.R.id.content)
        val snack = Snackbar.make(parentLayout, "$texto", Snackbar.LENGTH_SHORT)
        val viewSnack = snack.view
        val params = viewSnack.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_VERTICAL
        viewSnack.layoutParams = params
        when(color){
            "red" -> viewSnack.setBackgroundColor(getColor(R.color.red))
            "blue" -> viewSnack.setBackgroundColor(getColor(R.color.azulado))
            else -> {viewSnack.setBackgroundColor(getColor(R.color.amarilaldo))}
        }
        snack.show()
    }
    fun alertSnackBarTop(texto:String,color:String?){
        val parentLayout = findViewById<View>(android.R.id.content)
        val snack = Snackbar.make(parentLayout, "$texto", Snackbar.LENGTH_SHORT)

        // Forzamos el TOP
        val viewSnack = snack.view
        val params = viewSnack.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        viewSnack.layoutParams = params
        when(color){
            "red" -> viewSnack.setBackgroundColor(getColor(R.color.red))
            "blue" -> viewSnack.setBackgroundColor(getColor(R.color.azulado))
            else -> {viewSnack.setBackgroundColor(getColor(R.color.amarilaldo))}
        }
        snack.show()
    }

    fun getCoins(){
        //alertSnackBarTop("HACIENDO PETICIÓN")
        val client = OkHttpClient()
        val apiUrl = API_URL
        val c1 = coinOne.text
        val c2 = coinTwo.text // USD,GBP,EUR
        val amount = txtView.text
        val_txt_data = txtView.text.toString()
        val postData = arrayOf("$c1","$amount","$c2")
        val postDataTitle = arrayOf("symbol","amount","convert")
        if(Math.round(txtView.text.toString().toDouble()).toString() == "0" || txtView.text.isEmpty()){
            txtRes.text = "0"
            //  alertSnackBarTop("PETICIÓN COMPLETADA")
        }else{
            try {
                val request = Request.Builder()
                    .url(/* url = */ apiUrl+"?amount=$amount&symbol=$c1&convert=$c2")
                    .addHeader("X-CMC_PRO_API_KEY",API_KEY_COINMARKETCAP)
                    .addHeader(postDataTitle[1],postData[1])
                    .addHeader(postDataTitle[0],postData[0])
                    .addHeader(postDataTitle[2],postData[2])
                    .build()
                //    alertSnackBarTop("PETICIÓN MANDADA")

                client.newCall(request).enqueue(object: Callback {

                    override fun onFailure(call: Call, e: IOException) {
                        error("Failed"+ e.printStackTrace().toString())
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body()?.string()
                        println(body)
                        if("\"erro_code\":400" in body.toString()){
                            alertSnackBarTop(resources.getString(R.string.error_ammount_higher),"red")
                            txtRes.text = "0"
                        }else{
                            var parsed_price = body.toString().split("{")[5].split(",")[0].split(":")[1]
                            println("PRECIO: $parsed_price")

                            parsed_price = (parsed_price.toDouble()).toString()
                            value_api_data = parsed_price.toDouble()

                            val diferencia = parsed_price.toString().split(".")[1].length-8
                            var valforSubstring:Int = (value_api_data.toString().length-diferencia)
                            if(valforSubstring < 0){
                                valforSubstring = value_api_data.toString().length
                            }
                            var value_api_data_parsed:String = value_api_data.toString()

                            if("E-" in value_api_data.toString()){
                                alertSnackBarTop(resources.getString(R.string.error_ammount_smaller),"red")
                                value_api_data_parsed = "0"
                            }else if("E" in value_api_data.toString()){
                                alertSnackBarTop(resources.getString(R.string.error_ammount_higher),"red")
                                value_api_data_parsed = "0"
                            }else{
                                if(value_api_data.toString().split(".")[1].length > 8){
                                    value_api_data_parsed = value_api_data.toString().substring(0,value_api_data_parsed.indexOf(".")+9)
                                }
                            }
                            txtRes.text = value_api_data_parsed
                            val_res_data = value_api_data_parsed
                            saveStats()
                            //          alertSnackBarTop("PETICIÓN COMPLETADA")
                        }
                    }
                })
            }catch (e:Exception){
                alertSnackBarTop(resources.getString(R.string.error_request),"red")
            }
        }
    }





    fun updateConversion() {
        // Recover Valor Numerico   de 1ª Moneda
        var valnumerico = 1.0
        valnumerico  = txtView.text.toString().toDouble()
        // Recoger Valor Conversion de 2ª Moneda
        var conversion = 1
        //txtRes.text.toString().replace(",",".").toDouble(
        var result = valnumerico * conversion + 0.0
        println(result)

        if("." in result.toString()){
            var resultat_split = result.toString().split(".")
            if(resultat_split[1].length < 2){
                if(resultat_split[1].toInt()==0 || resultat_split[1].isEmpty()){
                    //    txtRes.text = resultat_split[0].toString()
                }
                //  txtRes.text = result.toString()
            }
            //txtRes.text = result.toString()
        }else{
            //txtRes.text = result.toString()
        }
        try{
            getCoins()
            saveStats()
        }catch(e:java.lang.Exception){
            alertSnackBarTop(resources.getString(R.string.error_request),"red")
            txtRes.text = "ERROR REQUEST" //TODO PONER ESTO CON SUS CORRESPONDIENTE MSG
        }
    }






    fun addNumber(view: View){
        if(view is Button){
            if(calcularLongitudTextView() >= 12){
                alertSnackBarTop(resources.getString(R.string.error_ammount_higher),"red")
            }else{
                if("." in txtView.text){
                    if(txtView.text.toString().split(".")[1].length < 8){
                        txtView.append(view.text)
                    }
                }
                else{
                    if(true){}
                    //calcularLongitudTextView() < 8{
                    if( txtView.text.length < 2 && txtView.text.toString() == "0") {
                        txtView.text.toString().dropLast(1)
                        txtView.setText(view.text)
                    }else{
                        txtView.append(view.text)
                    }
                }

            }
            try{

                updateConversion()
                saveStats()
            }catch (e:Exception){
                alertSnackBarTop(resources.getString(R.string.error_request),"red")
            }
        }

    }








    fun calcularLongitudTextView(): Int {
        var texto = this.txtView.text.toString()
        if("." in texto){
            if(texto[texto.length-1].toString() == "."){
                saveStats()
                return texto.length-1
            }else{
                var tv_without_comma = texto.split('.')[1]//.replace(",","")
                saveStats()
                return tv_without_comma.length
            }

        }else{
            saveStats()
            return texto.length

        }
        //println(tv_without_comma+" , "+tv_without_comma.length.toString())

    }




    fun cleanAll(view: View){
        txtView.text = "0"
        txtRes.text = "0"
        saveStats()
    }




    fun insertComa(view: View){
        if("." in txtView.text.toString()){
            //SnackBar Ya Existe este Valor
        }else{
            if(this.calcularLongitudTextView() > 8){ // SE TIENE QUE QUITAR LA LONGITUD QUE OCUPA LA COMA
                // EXCEDE LOS 8 CARACTEERS
            }else {
                if(txtView.text != ""){
                    txtView.append(".")
                    saveStats()
                }else{
                    txtView.append("0.")
                    saveStats()

                }
            }
        }
        saveStats()
    }



    fun delLast(view: View){
        var str = txtView.text.toString().dropLast(1)
        txtView.text = str
        if(calcularLongitudTextView() < 1){
            txtView.setText("0")
            saveStats()

        }
        if(txtView.text.toString()[txtView.text.toString().length-1].toString() == "."){
            //TODO NO CONVERTIR
        }else{
            try{
                updateConversion()
                saveStats()
            }catch(e:Exception){
                alertSnackBarTop(resources.getString(R.string.error_request),"red")
            }
        }
        val_txt_data = txtView.text.toString()
        val_res_data = txtRes.text.toString()

    }




    fun switchConversion(view: View){
        //TEXTOS
        var txt1 = coinOne.text
        var txt2 = coinTwo.text
        var tmp_text = txt1.toString()

        coinOne.text = txt2.toString()
        coinTwo.text = tmp_text

        //CALCULOS
        var numberOneCrypto = txtView.text
        var numberTwoCrypto = txtRes.text
        txtView.text = numberTwoCrypto
        txtRes.text = numberOneCrypto
        saveStats()
        try{
            updateConversion()
            saveStats()
        }catch(e:Exception){
            alertSnackBarTop(resources.getString(R.string.error_request),"red")
        }
    }


    fun createFormDialog(forWhoisChoosing:Int): androidx.appcompat.app.AlertDialog {

        var inflater: LayoutInflater = layoutInflater

        val v: View = inflater.inflate(R.layout.dialoglayout, null)

        val btn_btc_dialog:Button = v.findViewById(R.id.btccoin)
        val btn_ltc_dialog:Button = v.findViewById(R.id.ltccoin)
        val btn_ada_dialog:Button = v.findViewById(R.id.adacoin)
        val btn_local_dialog:Button = v.findViewById(R.id.locaicoin)
        var buttonclicked: String? = ""

        var builder = MaterialAlertDialogBuilder(this)
        builder.setView(v)

        val activeOne:TextView = coinOne
        val activeTwo:TextView = coinTwo
        when(forWhoisChoosing){
            1 -> {
                activeOne.text = coinOne.text
                val_txtcoin_data = coinOne.text.toString()
            }
            else -> {
                activeTwo.text = coinTwo.text
                val_txtcoin_data = coinTwo.text.toString()

            }
        }
        saveStats()

        btn_btc_dialog.setOnClickListener{
            buttonclicked = btn_btc_dialog.text.toString()
        }
        btn_ada_dialog.setOnClickListener{
            buttonclicked = btn_ada_dialog.text.toString()
        }
        btn_local_dialog.setOnClickListener{
            buttonclicked = btn_local_dialog.text.toString()
        }
        btn_ltc_dialog.setOnClickListener {
            buttonclicked = btn_ltc_dialog.text.toString()
        }









        val ok: Button = v.findViewById(R.id.btnOk)
        val cancel: Button = v.findViewById(R.id.btnCancel)

        val dialog = builder.create()

        ok.setOnClickListener{
            println("OK")
            if(buttonclicked!="" || !(buttonclicked!!.isEmpty())) {
                when (forWhoisChoosing) {
                    1 -> coinOne.text = buttonclicked
                    else -> coinTwo.text = buttonclicked
                }
            }
            var texto = resources.getString(R.string.value_choosed)+" $buttonclicked"
            alertSnackBar(texto,"blue")

            dialog.dismiss()

        }
        cancel.setOnClickListener{
            println("Cancelar")
            dialog.dismiss()
        }
        saveStats()
        return dialog
    }

    fun dialogChooseone(view:View) {
        createFormDialog(1).show()

    }
    fun dialogChooseTwo(view:View) {
        createFormDialog(2).show()
    }


    fun saveStats(){
        val_txt_data = txtView.text.toString()
        val_res_data = txtRes.text.toString()
        val_txtcoin_data = coinOne.text.toString()
        val_rescoin_data = coinTwo.text.toString()
    }


}

