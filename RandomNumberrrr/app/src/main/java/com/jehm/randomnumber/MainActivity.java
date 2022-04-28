package com.jehm.randomnumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFrom;
    private EditText editTextTo;

    private View divider;
    private TextView textViewRandomIs;
    private TextView textViewResult;

    private Button buttonRandom;

    private static int MIN_ALLOWED = -214000000;
    private static int MAX_ALLOWED = 214000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        editTextTo = (EditText) findViewById(R.id.editTextTo);

        divider = (View) findViewById(R.id.dividerH);
        textViewRandomIs = (TextView) findViewById(R.id.textViewRandomIs);
        textViewResult = (TextView) findViewById(R.id.textViewResult);

        buttonRandom = (Button) findViewById(R.id.buttonRandom);

        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextFrom.getText().toString().isEmpty() && !editTextTo.getText().toString().isEmpty()) {

                    if (Double.parseDouble(editTextFrom.getText().toString()) < MIN_ALLOWED) {
                        Toast.makeText(MainActivity.this, "The Minimum number allowed is: " + MIN_ALLOWED, Toast.LENGTH_LONG).show();
                    } else if (Double.parseDouble(editTextTo.getText().toString()) > MAX_ALLOWED) {
                        Toast.makeText(MainActivity.this, "The Maximum number allowed is: " + MAX_ALLOWED, Toast.LENGTH_LONG).show();
                    } else {
                        int minNumber = Integer.parseInt(editTextFrom.getText().toString());
                        int maxNumber = Integer.parseInt(editTextTo.getText().toString());

                        if (minNumber < maxNumber) {

                            int result = Randomize(minNumber, maxNumber);

                            if (textViewResult.getVisibility() == View.VISIBLE) {
                                //Asignamos el resultado convertido a String
                                textViewResult.setText(String.valueOf(result));
                            } else {
                                //Asignamos el resultado convertido a String
                                textViewResult.setText(String.valueOf(result));
                                //Lo Hacemos visible
                                divider.setVisibility(View.VISIBLE);
                                textViewRandomIs.setVisibility(View.VISIBLE);
                                textViewResult.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Please enter the numbers From MINIMUM To MAXIMUM", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You must enter a Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int Randomize(int from, int to) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int randomResult = ThreadLocalRandom.current().nextInt(from, to + 1);
            return randomResult;
        } else {
            Toast.makeText(MainActivity.this, "Your Android Version does not support this Function", Toast.LENGTH_LONG).show();
            return 0;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuReset:

                editTextFrom.setText("1");
                editTextTo.setText("100");

                divider.setVisibility(View.INVISIBLE);
                textViewRandomIs.setVisibility(View.INVISIBLE);
                textViewResult.setVisibility(View.INVISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
