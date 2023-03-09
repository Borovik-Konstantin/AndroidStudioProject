package com.example.softwaredevelopment;





import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


import android.app.Activity;

import android.content.Intent;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.softwaredevelopment.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityResultLauncher activityResultLauncher;


    // Used to load the 'softwaredevelopment' library on application startup.
    static {
        System.loadLibrary("softwaredevelopment");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int res = initRng();
        byte[] v = randomBytes(10);


        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
        activityResultLauncher  = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>(){

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            // обработка результата
                            String pin = data.getStringExtra("pin");
                            System.out.println("Correct!");
                            Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                            System.out.println("Correct!");
                        }
                    }
                });
    }
    public static byte[] stringToHex(String s)
    {
        byte[] hex;
        try{
            hex = Hex.decodeHex(s.toCharArray());
        }
        catch(DecoderException ex)
        {
            hex = null;
        }
        return hex;
    }
    public void onButtonClick(View v)
    {
        Intent it = new Intent(this, PinpadActivity.class);
        activityResultLauncher.launch(it);
    }

    /**
     * A native method that is implemented by the 'softwaredevelopment' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte [] encrypt(byte[] key, byte[] data);
    public static native byte [] decrypt(byte[] key, byte[] data);

}