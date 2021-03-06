package medik247.vs.com.medik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Indosurplus on 5/5/2017.
 */
public class Singuppatient extends Activity {
    EditText edFname,edLname,edemail,edphonenumber,epassword,edconfirmpassword,edofficeaddress,edhomeaddress;
    String url;
    String link="http://vertosys.com/docpat/register.php?";
    ProgressDialog progressDialog;
    private static final int IMG_RESULT3 =25 ;
    private static final int IMG_RESULT2 =222 ;
    private static int IMG_RESULT = 3;
    String ImageDecode;
    EditText editTextaddress;
    ImageView imageViewLoad1,imageViewLoad2;
    Button LoadImage1,LoadImage2;
    Intent intent;
    String[] FILE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientreg);
        progressDialog = new ProgressDialog(Singuppatient.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        edFname = (EditText) findViewById(R.id.edfname);
        editTextaddress= (EditText) findViewById(R.id.homeaddressdata);

        edLname = (EditText) findViewById(R.id.edlname);
        edemail = (EditText) findViewById(R.id.editTextemail);
        edconfirmpassword = (EditText) findViewById(R.id.edconfirmpassword);
        epassword = (EditText) findViewById(R.id.editTextpassword);
        edphonenumber = (EditText) findViewById(R.id.phonenumber);
        imageViewLoad1 = (ImageView) findViewById(R.id.imageView_1);
        imageViewLoad2 = (ImageView) findViewById(R.id.imageView_2);
        LoadImage1 = (Button) findViewById(R.id.buttonpick_1);
        LoadImage2 = (Button) findViewById(R.id.buttonpick_2);

        Button button = (Button) findViewById(R.id.sumbit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname2 = edFname.getText().toString();
                String lname = edLname.getText().toString();
                String email = edemail.getText().toString();

                String phonenumber = edphonenumber.getText().toString();
                String password = epassword.getText().toString();
                String confirmpassword = edconfirmpassword.getText().toString();
                Log.e("Data", fname2 + lname + password + phonenumber + email + confirmpassword);

//                    if(validate(email)==true) {
////    apihit();
//                    }
                    if(fname2.equals("")){
                        edFname.setError("Field not entered");
                        edFname.requestFocus();
                    }
                    if(lname.equals("")){
                        edLname.setError("Field not entered");
                        edLname.requestFocus();
                    }
                    if(email.equals("")){
                        edemail.setError("Field not entered");
                        edemail.requestFocus();
                    }
                    if(phonenumber.equals("")){
                        edphonenumber.setError("Field not entered");
                        edphonenumber.requestFocus();
                    }
                    if(password.equals("")){

                        epassword.setError("Field  not entered");
                        epassword.requestFocus();

                    }


                    if (password.equals(confirmpassword)==false) {
                        Toast.makeText(Singuppatient
                                .this, "Password Not Match", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        url = link + "first_name=" + fname2 + "&last_name=" + lname + "&email=" +
                                email + "&password=" + password + "&phone_number=" + phonenumber + "&profile_pic=pic.jpeg&user_type=patient"+"&DeviceID=";
                        //http://vertosys.com/docpat/register.php?first_name=varinder&last_name=singh&email=varinder2@gmail.com&password=123456&phone_number=0987654321&profile_pic=pic.jpeg&user_type=patient
                        Log.e("URL", url);
                        progressDialog.show();
                        apihit();
                    }




            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.backsignup);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Singuppatient.this, Sigin.class);
                startActivity(intent);
            }
        });


        LoadImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, IMG_RESULT);

            }
        });

        LoadImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, IMG_RESULT2);

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();
                String[] FILE = { MediaStore.Images.Media.DATA };


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();
                imageViewLoad1.setImageBitmap(BitmapFactory
                        .decodeFile(ImageDecode));

            }

            if (requestCode == IMG_RESULT2 && resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();
                String[] FILE = { MediaStore.Images.Media.DATA };


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();
                try {
                    imageViewLoad2.setImageBitmap(BitmapFactory
                            .decodeFile(ImageDecode));
                }catch (Exception e)
                {}









































































































            }

        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show();
        }

    }
    private String generateID() {
        String deviceId = android.provider.Settings.Secure.getString(this.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        if ("9774d56d682e549c".equals(deviceId) || deviceId == null) {
            deviceId = ((TelephonyManager) this
                    .getSystemService(Context.TELEPHONY_SERVICE))
                    .getDeviceId();
            if (deviceId == null) {
                Random tmpRand = new Random();
                deviceId = String.valueOf(tmpRand.nextLong());
            }
        }
        return getHash(deviceId);
    }

    public String getHash(String stringToHash) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] result = null;
        try {
            result = digest.digest(stringToHash.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        for (byte b : result) {
            sb.append(String.format("%02X", b));
        }

        String messageDigest = sb.toString();
        return messageDigest;
    }
    public void apihit(){
        RequestQueue queue = Volley.newRequestQueue(Singuppatient.this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()  {

            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                try {
                    status=response.getString("success");
                    status=response.getString("success");
                    Log.e("Status",status);
                    if(status.equals("true")){
                        progressDialog.dismiss();
                        Intent intent=new Intent(Singuppatient.this,Main2Activity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();

                    e.printStackTrace();
                }
                try {
                    progressDialog.dismiss();

                    String message=response.getString("message");
                    Intent intent=new Intent(Singuppatient.this,ScreenSignIn.class);
                    startActivity(intent);
//                    Toast.makeText(Singuppatient.this,message,Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    progressDialog.dismiss();

                    e.printStackTrace();
                }
                // TODO Auto-generated method stub

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                // TODO Auto-generated method stub
//                Toast.makeText(Singuppatient.this,"Error"+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        queue.add(jsObjRequest);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent in=new Intent(this,Sigin.class);
        startActivity(in);

    }

}
