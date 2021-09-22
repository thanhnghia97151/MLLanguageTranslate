package com.example.mllanguagetranslate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    TextView tvTranslateLanguage;
    Button btnTranslate;
    EditText ettOriginalText;
    String originalText = "";
    Translator englishVietTranslator;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTranslateLanguage= findViewById(R.id.tvTranslateLanguage);
        btnTranslate = findViewById(R.id.btnTranslateNow);
        ettOriginalText = findViewById(R.id.edOriginalText);

        //
        setUpProgressDialog();

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originalText = ettOriginalText.getText().toString();
                prepareModel();
            }
        });
    }

    private void setUpProgressDialog() {
        pDialog = new SweetAlertDialog(MainActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

    }

    private void prepareModel() {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.VIETNAMESE)
                .build();
        englishVietTranslator = Translation.getClient(options);
        //Download Model if is not download before
        pDialog.setTitleText("Translate Model Downloading...");
        pDialog.show();

        englishVietTranslator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Now model ready to use
                pDialog.dismissWithAnimation();
                translateLanguage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismissWithAnimation();
                tvTranslateLanguage.setText("Error: "+e.getMessage());
            }
        });
    }

    private void translateLanguage() {
        pDialog.setTitleText("Language Converting...");
        pDialog.show();
        englishVietTranslator.translate(originalText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                pDialog.dismissWithAnimation();
                tvTranslateLanguage.setText(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismissWithAnimation();
                tvTranslateLanguage.setText("Error: "+e.getMessage());
            }
        });
    }
}