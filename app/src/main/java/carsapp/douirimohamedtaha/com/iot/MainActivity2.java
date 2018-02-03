package carsapp.douirimohamedtaha.com.iot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity2 extends AppCompatActivity {
    Button btnOnOff;
    Button btnMessage;
    Button btnSeuilLum;
    Button btnSeuilTemp;
    boolean state;
    TextView txtLum;
    TextView etat;
    TextView txtTemp;
    EditText txtMessage;
    EditText txtSeuilLum;
    EditText txtSeuilTemp;

    String lum, temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnOnOff = (Button) findViewById(R.id.btnOnOff);
        btnMessage = (Button) findViewById(R.id.sendMsg);
        btnSeuilLum = (Button) findViewById(R.id.btnSeuilLum);
        btnSeuilTemp = (Button) findViewById(R.id.btnSeuilTemp);

        txtLum = (TextView) findViewById(R.id.tempLum);
        txtTemp = (TextView) findViewById(R.id.tempVal);
        etat = (TextView) findViewById(R.id.textView3);

        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtSeuilLum = (EditText) findViewById(R.id.seuilLum);
        txtSeuilTemp = (EditText) findViewById(R.id.txtSeuilTemp);

        state = false;
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!state) {
                    ClickOnButtonOnOff("on");

                } else {
                    ClickOnButtonOnOff("off");
                }
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run() {
                                if (state) {

                                    getLum();
                                    getTemp();

                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        // ooops
                        Log.d("Thread", "Interrupted");

                    }
                }
            }

        }).start();


        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(txtMessage.getText().toString());
            }
        });
        btnSeuilTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSeuilTemp(txtSeuilTemp.getText().toString());
            }
        });
        btnSeuilLum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSeuilLum(txtSeuilLum.getText().toString());
            }
        });
    }

    private void getTemp() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.18.3.115:3000/temperature";
// Request a string response from the provided URL.

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    String val = response.getString("val");
                    temp = val;
                    txtTemp.setText(val + " °");

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);

    }


    private void ClickOnButtonOnOff(String q) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.18.3.115:3000/" + q;
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("State", response.toString());
                        // Display the first 500 characters of the response string.
                        if (response.toString().equals("led on")) {
                            etat.setText("System On");
                            btnOnOff.setText("On");
                            state = true;
                        } else {
                            etat.setText("System Off");
                            btnOnOff.setText("Off");
                            state = false;
                            txtTemp.setText("");
                            txtLum.setText("");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getLum() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.18.3.115:3000/lum";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        lum = response.toString();
                        txtLum.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void sendMessage(String message) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.18.3.115:3000/message?data=" + message;
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Message", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void sendSeuilTemp(String message) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.18.3.115:3000/seuil?data=" + message;
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Message", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void sendSeuilLum(String message) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://172.18.3.115:3000/seuilLum?data=" + message;
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Message", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}
