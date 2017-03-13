package xyz.jayadev.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    ProgressDialog pDialog;
    String user_name, password, confirm_password, fname, mname, lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        final EditText userName = (EditText) findViewById(R.id.email);
        final EditText pwd = (EditText) findViewById(R.id.password);
        final EditText confirm_pwd = (EditText) findViewById(R.id.confirm_password);
        final EditText fName = (EditText) findViewById(R.id.fname);
        final EditText mName = (EditText) findViewById(R.id.mname);
        final EditText lName = (EditText) findViewById(R.id.lname);
        Button signup = (Button) findViewById(R.id.sign_up);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name = userName.getText().toString();
                password = pwd.getText().toString();
                confirm_password = confirm_pwd.getText().toString();
                fname = fName.getText().toString();
                mname = mName.getText().toString();
                lname = lName.getText().toString();

                if (isValidEmaillId(user_name)) {
                    if (password.equals(confirm_password)) {
                        Log.d("Alpha", user_name);
                        apiCall();
                    } else {
                        Toast.makeText(getApplicationContext(), "password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "InValid Email Address", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void apiCall() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userName", user_name);
        params.put("profileUsername", " ");
        params.put("password", password);
        params.put("confirmPassword", confirm_password);
        params.put("firstName", fname);
        params.put("middleName", mname);
        params.put("lastName", lname);
        params.put("dateOfBirth", "2016-08-12");
        params.put("gender", "male");


        client.post(Config.URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                pDialog = new ProgressDialog(SignUp.this, ProgressDialog.THEME_HOLO_DARK);
                pDialog.setTitle("Please Wait");
                pDialog.setMessage("Loading Image ...");
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
                Log.d("Alpha", "started");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] response) {
                String resp = new String(response);
                Log.d("Alpha", "onSucc:" + resp + statusCode);


            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Alpha", statusCode + "");
                pDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Unable to connect to server.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

            @Override
            public void onFinish() {
                // Completed the request (either success or failure)
                pDialog.dismiss();
                Log.d("Alpha", "done");

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                // Progress notification

            }
        });
    }
}
