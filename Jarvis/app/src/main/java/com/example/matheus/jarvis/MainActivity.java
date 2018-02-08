package com.example.matheus.jarvis;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnDSListener {

    public final String TAG = "Activity_DroidSpeech";
    public String lastCommand = "";

    private DroidSpeech droidSpeech;
    private TextView finalSpeechResult;
    private ImageView start, stop;

    // MARK: Activity Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the layout;[.
        setContentView(R.layout.activity_main);

        // Initializing the droid speech and setting the listener
        droidSpeech = new DroidSpeech(this, getFragmentManager());
        droidSpeech.setOnDroidSpeechListener(this);
        droidSpeech.setShowRecognitionProgressView(true);
        //droidSpeech.setOneStepResultVerify(true);
        droidSpeech.setRecognitionProgressMsgColor(Color.WHITE);
        droidSpeech.setOneStepVerifyConfirmTextColor(Color.WHITE);
        droidSpeech.setOneStepVerifyRetryTextColor(Color.WHITE);

        finalSpeechResult = findViewById(R.id.text);

        droidSpeech.startDroidSpeechRecognition();
    }

    // MARK: DroidSpeechListener Methods

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        Log.i(TAG, "Current speech language = " + currentSpeechLanguage);
        Log.i(TAG, "Supported speech languages = " + supportedSpeechLanguages.toString());

        if (supportedSpeechLanguages.contains("pt-BR")) {
            // Setting the droid speech preferred language as tamil if found
            droidSpeech.setPreferredLanguage("pt-BR");
        }
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {
        // Log.i(TAG, "Rms change value = " + rmsChangedValue);
    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {
        Log.i(TAG, "Live speech result = " + liveSpeechResult);
    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult) {
        // Setting the final speech result
        Toast.makeText(this, finalSpeechResult, Toast.LENGTH_SHORT).show();

        if(!lastCommand.equals(finalSpeechResult))
            chamaApiJarvis(finalSpeechResult);
        lastCommand = finalSpeechResult;

        if (droidSpeech.getContinuousSpeechRecognition()) {
            int[] colorPallets1 = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA};
            int[] colorPallets2 = new int[]{Color.YELLOW, Color.RED, Color.CYAN, Color.BLUE, Color.GREEN};

            // Setting random color pallets to the recognition progress view
            droidSpeech.setRecognitionProgressViewColors(new Random().nextInt(2) == 0 ? colorPallets1 : colorPallets2);
        }
    }

    @Override
    public void onDroidSpeechClosedByUser() {
        droidSpeech.startDroidSpeechRecognition();
    }

    @Override
    public void onDroidSpeechError(String errorMsg) {
        // Speech error
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    // MARK: DroidSpeechPermissionsListener Method
    private void chamaApiJarvis(final String text){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.15.18.42:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ConversationServices conversationServices = retrofit.create(ConversationServices.class);

        Call<RepoConversation> repos = conversationServices.postMessage(text);
        repos.enqueue(new Callback<RepoConversation>() {
            @Override
            public void onResponse(Call<RepoConversation> call, retrofit2.Response<RepoConversation> response) {

                Log.d("TESTE", response.body().toString());
                //System.out.println(response.body().toString());
                //RepoConversation repoResponse = response.body();
                //System.out.println(repoResponse.getResponse());
            }

            @Override
            public void onFailure(Call<RepoConversation> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}