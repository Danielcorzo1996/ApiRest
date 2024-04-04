package com.daniel.ar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button botonIrASegundoLayout;
    String url;
    ListView listview;
    private Persona persona;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dataList;

    private ArrayList<Persona> personas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url  = "https://jsonplaceholder.typicode.com/users";
        botonIrASegundoLayout = findViewById(R.id.btnAgregar);
        listview = findViewById(R.id.listview);


        personas = new ArrayList<>();

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listview.setAdapter(adapter);

        //AQUÍ CARGO LOS USUARIOS DESDE LA API
        loadUsers();

        //PARA ABRI LA OTRA VENTANA SIN DATA
        botonIrASegundoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        //ABRE LA OTRA VENTANA CON LA DATA DEL USUARIO SELECIONADO
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when an item is clicked
                String selectedItem = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selectedItem + " clicked", Toast.LENGTH_SHORT).show();
                persona = new Persona();

                for(Persona personInList : personas){
                    if(personInList.getNombres().equals(selectedItem)){
                        persona = personInList;
                        break;
                    }
                }

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("persona", persona);
                startActivity(intent);
            }
        });

    }

    private void loadUsers(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                Persona newPersona = new Persona();

                                String item = response.getString(i);

                                JSONObject jsonObject = new JSONObject(item);

                                newPersona.setId(Integer.parseInt(jsonObject.getString("id")));
                                newPersona.setNombres(jsonObject.getString("name"));
                                newPersona.setCorreo(jsonObject.getString("email"));
                                //System.out.println(jsonObject.getString("email"));
                                personas.add(newPersona);

                                dataList.add(jsonObject.getString("name"));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }
}