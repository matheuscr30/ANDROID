package com.example.beerapp.activity;

import android.app.TimePickerDialog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.beerapp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class CadastroEstabelecimentoActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText editTextNomeEstabelecimento;
    private Button buttonHorarioAbertura;
    private Button buttonHorarioFechamento;
    private String horaFechamento;
    private String horaAbertura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_estabelecimento);
        getSupportActionBar().hide();

        editTextNomeEstabelecimento = (EditText)findViewById(R.id.editTextCadastroEstabelecimentoNome);
        buttonHorarioAbertura = (Button)findViewById(R.id.buttonHorarioAbertura);
        buttonHorarioFechamento = (Button)findViewById(R.id.buttonHorarioFechamento);


    }

    public void configurarHorarioAbertura(View view){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CadastroEstabelecimentoActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                horaAbertura = String.format("%02d:%02d", selectedHour, selectedMinute);
                buttonHorarioAbertura.setText(horaAbertura);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void configurarHorarioFechamento(View view){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CadastroEstabelecimentoActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                horaFechamento = String.format("%02d:%02d", selectedHour, selectedMinute);
                buttonHorarioFechamento.setText(horaFechamento);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
