package com.daniel.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity2 extends AppCompatActivity {

    EditText txtIdUser, txtFullNameUser, txtEmailUser;
    Button btnAdd;
    String url;
    Persona persona;
    Intent intent;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        txtIdUser = findViewById(R.id.id);
        txtFullNameUser = findViewById(R.id.nombres);
        txtEmailUser = findViewById(R.id.correo);
        btnAdd = findViewById(R.id.Agregar);



        url  = "https://jsonplaceholder.typicode.com/users";
        intent = getIntent();
        try {
            persona = (Persona)intent.getSerializableExtra("persona");

            txtIdUser.setText(String.valueOf(persona.getId()));
            txtEmailUser.setText(persona.getCorreo());
            txtFullNameUser.setText(persona.getNombres());

            if(persona != null){
                //desactivo el campo para que no sea editable
                txtIdUser.setTextIsSelectable(false);
                txtIdUser.setClickable(false);
                txtIdUser.setFocusable(false);
            }

        }catch (Exception ex){
            persona = new Persona();
        }
    }
    public void volver_pantalla(View view){
        finish();
    }

    public void addPersona(View view){
        String nameData = String.valueOf(txtEmailUser.getText());
        String emailData = String.valueOf(txtEmailUser.getText());

        if(nameData.equals("")){
            Toast.makeText(MainActivity2.this, "El NOMBRE es requerido!", Toast.LENGTH_LONG).show();
            return;
        }

        if(emailData.equals("")){
            Toast.makeText(MainActivity2.this, "El CORREO es requerido!", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("name", nameData);
            postData.put("email",  emailData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response -> " + response.toString());
                        try {
                            String id = response.getString("id");
                            Toast.makeText(MainActivity2.this, "Persona creada con Exito!", Toast.LENGTH_LONG).show();
                            cleanField();
                        }catch (Exception ex){
                            Toast.makeText(MainActivity2.this, "ERROR AL CREAR LA PERSONA!", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error.Response -> " + error.toString());
                        Toast.makeText(MainActivity2.this, "ERROR AL CREAR LA PERSONA!", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this); // 'this' is the Context
        requestQueue.add(jsonObjectRequest);
    }

    public  void updatePersona(View view){

        String idData = String.valueOf(txtIdUser.getText());
        String nameData = String.valueOf(txtEmailUser.getText());
        String emailData = String.valueOf(txtEmailUser.getText());

       checkIfIdIsEmpty();

        if(nameData.equals("")){
            Toast.makeText(MainActivity2.this, "El NOMBRE es requerido!", Toast.LENGTH_LONG).show();
            return;
        }

        if(emailData.equals("")){
            Toast.makeText(MainActivity2.this, "El CORREO es requerido!", Toast.LENGTH_LONG).show();
            return;
        }

        //PRIMERO VALIDO QUE EL CMAPO ID TENGA UN ID VALIDO
        if(checkIfIdIsEmpty()){
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", idData);
            postData.put("name", nameData);
            postData.put("email",  emailData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, url +"/"+txtIdUser.getText(), postData, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response -> " + response.toString());
                        try {
                            String id = response.getString("id");
                            cleanField();
                            Toast.makeText(MainActivity2.this, "Persona actualizada con Exito!", Toast.LENGTH_LONG).show();
                        }catch (Exception ex){
                            Toast.makeText(MainActivity2.this, "ERROR AL ACTUALIZAR LA PERSONA!", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error.Response -> " + error.toString());
                        Toast.makeText(MainActivity2.this, "ERROR AL ACTUALIZAR LA PERSONA!", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this); // 'this' is the Context
        requestQueue.add(jsonObjectRequest);
    }

    public void deletePersona(View view){

        //PRIMERO VALIDO QUE EL CMAPO ID TENGA UN ID VALIDO
        if(checkIfIdIsEmpty()){
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        cleanField();
                        Toast.makeText(MainActivity2.this, "PERSONA ELIMINADA CON EXITO", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity2.this, "ERROR ELIMINANDO PERSONA", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    public void searchPersonById(View view){
        //PRIMERO VALIDO QUE EL CMAPO ID TENGA UN ID VALIDO
        if(checkIfIdIsEmpty()){
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url + "/" + txtIdUser.getText(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            txtFullNameUser.setText( response.getString("name") );
                            txtEmailUser.setText( response.getString("email") );
                        }catch (Exception ex){
                            System.out.println("exception -> " + ex.getMessage());
                            Toast.makeText(MainActivity2.this, "ERROR BUSCANDO PERSONA", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error -> " + error.getMessage());
                        Toast.makeText(MainActivity2.this, "ERROR BUSCANDO PERSONA", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Limpia los campos, es decir los deja con string vacíos
     * */
    private void cleanField(){
        txtIdUser.setText("");
        txtFullNameUser.setText("");
        txtEmailUser.setText("");
    }

    private Boolean checkIfIdIsEmpty(){
        //PRIMERO VALIDO QUE EL CMAPO ID TENGA UN ID VALIDO
        String isEmpty = String.valueOf(txtIdUser.getText());
        if(isEmpty.equals("")){
            Toast.makeText(MainActivity2.this, "DEBES AGREGAR UN ID", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

}