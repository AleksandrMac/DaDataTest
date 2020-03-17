package com.example.dadatatest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject


val api_key     = BuildConfig.CONSUMER_KEY
val secret_key  = BuildConfig.CONSUMER_SECRET

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun findContragent(view: View) {
        val textView = findViewById<TextView>(R.id.textViewValue)

        val queue = Volley.newRequestQueue(this)
        val url = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/findById/party"

        val params = HashMap<String, String>()
        params["query"] = editText.text.toString() //1027700132195 280128647006

        val jsonParams = JSONObject(params as Map<*, *>)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url,jsonParams,
            Response.Listener {response ->
                val fullJSON    = JSONObject(response.toString())
                val suggestions = JSONArray(fullJSON.get("suggestions").toString())
                val arr0        = JSONObject(suggestions.get(0).toString())
                val data        = JSONObject(arr0.get("data").toString())
                val address     = JSONObject(data.get("address").toString())
                Log.d("DaData ",response.toString())


                textViewValue_.text     = if (arr0.has("value"))     arr0.get("value").toString()       else ""
                textViewAddress_.text   = if (address.has("value"))  address.get("value").toString()    else ""
                textViewINN_.text       = if (data.has("inn"))       data.get("inn").toString()         else ""
                textViewKPP_.text       = if (data.has("kpp"))       data.get("kpp").toString()         else ""
                textViewOGRN_.text      = if (data.has("ogrn"))      data.get("ogrn").toString()        else ""
                textViewOGRNDate_.text  = if (data.has("ogrn_date")) data.get("ogrn_date").toString()   else ""
            },
            Response.ErrorListener {err ->
                Log.d("DaData ","$err что то пошло не так")
            }){

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Token ${api_key}"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }
}
