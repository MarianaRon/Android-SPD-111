package step.learning.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalcActivity extends AppCompatActivity {
    private TextView tvHistory;
    private TextView tvResult;
    private boolean aOperand;
    private static Double num1;
    private static Double num2;
    private Character operand;
    private Animation clickAnimation;

    @SuppressLint("DiscouragedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        clickAnimation = AnimationUtils.loadAnimation(this,R.anim.calc);
        tvHistory = findViewById(R.id.calc_tv_histoty);
        tvResult = findViewById(R.id.calc_tv_result);
        if( savedInstanceState ==null){//немає збереженого стану - перший запуск
            tvResult.setText("0");
        }
           /* Задача: циклом перебрати ресурсні кнопки calc_btn_{i} і для
           кожної з них поставити один обробник onDigitButtonClick */
        for (int i = 0; i < 10; i++){
            findViewById(//на заміну R.id.calc_btn_0 проходить наступний вираз
                    getResources().getIdentifier( //R
                            "calc_btn_" + i,      // .calc_btnB
                            "id",                 // .id
                            getPackageName()
                    )
            ).setOnClickListener(this::onDigitButtonClick);
        }
        findViewById(R.id.calc_btn_percent).setOnClickListener(this::onPercentClick);
        findViewById(R.id.calc_btn_ce).setOnClickListener(this::on_CE_Click);
        findViewById(R.id.calc_btn_c).setOnClickListener(this::on_C_Click);
        findViewById(R.id.calc_btn_backspace).setOnClickListener(this::onBackspaceClick);
        findViewById(R.id.calc_btn_inverse).setOnClickListener(this::onInverseClick);

        findViewById(R.id.calc_btn_point).setOnClickListener(this::onPointClick);


    }
    private void onPercentClick(View view) {
        view.startAnimation(clickAnimation); //применение анимации
        String result = tvResult.getText().toString();
        double x = Double.parseDouble(result);
        if(num1 != null){
            num2 =  x * num1 / 100;
            x = num2;
        }
        result = (x == (int)x)
                ? String.valueOf((int)x)
                : String.valueOf(x);
        tvResult.setText(result);

    }

    private void on_CE_Click(View view) {
        view.startAnimation(clickAnimation); //анимация
        tvResult.setText("0");
    }


    private void on_C_Click(View view) {
        view.startAnimation(clickAnimation); //анимация
        tvResult.setText("0");
        tvHistory.setText("");

        aOperand = false;
        num1 = null;
        num2 = null;
        operand = null;
    }

    private void onBackspaceClick(View view) {
        view.startAnimation(clickAnimation); //анимация
        String result = tvResult.getText().toString();
        if(!result.isEmpty()){
            result = result.substring(0, result.length() - 1);
        }
        if(result.isEmpty() || result.equals("-")){
            result = "0";
        }
        tvResult.setText(result);
    }
    private void onInverseClick(View view) {
        view.startAnimation(clickAnimation); //анимация
        String result = tvResult.getText().toString();
        double x = Double.parseDouble(result);
        if(x == 0) {
            Toast.makeText(this, R.string.calc_zero_division, Toast.LENGTH_SHORT).show();
            return;
        }
        x = 1.0 / x;
        String str = (x == (int)x)
                ? String.valueOf((int) x )
                : String.valueOf( x );

        if( str.length() > 13 ) {
            str = str.substring(0, 13);
        }
        tvResult.setText( str );
    }
    private void onPointClick(View view) {
        view.startAnimation(clickAnimation); //анимация
        String result = tvResult.getText().toString();
        if (result.contains(".")) {
            return;
        } else {
            result += ".";
            tvResult.setText(result);
        }
    }







    /*  ПРИ ПОВОРОТАХ ЄКРАНУ (зміна конфігурації пристрою)

    При зміні конфігурації пристрою (поворотах, змінах налаштувань, тощо) відбувається
    перезапуск активності. При цьому подаються події життєвого циклу
    onSaveInstanceState - при виході з активності перед перезапуском
    onRestoreInstanceState - при відновленні активності після перезапуску
    До обробників передається Bundle, що є сховищем, яке дозволяє зберегти та відновити дані
    Також збережений Bundle передається до onCreate, що дозволяє визначити
     чи це перший запуск, чи перезапуск через зміну конфігурації
     */


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);//потрібно
        outState.putCharSequence( "tvResult", tvResult.getText() );
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvResult.setText( savedInstanceState.getCharSequence("tvResult"));
    }

    private void  onDigitButtonClick(View view){
        String result = tvResult.getText().toString();
        if(result.length() >= 10){
            Toast.makeText(this, R.string.calc_limit_exceeded, Toast.LENGTH_SHORT).show();
            return;
        }
        if(result.equals("0")){
            result = "";
        }
        result += ((Button) view).getText();
        tvResult.setText( result );
    }
}