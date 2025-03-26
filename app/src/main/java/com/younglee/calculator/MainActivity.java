//package com.younglee.calculator;
//
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//
//    }
//}

package com.younglee.calculator;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText display;
    private String input = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isNewOp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.text);
        // 禁止手动输入，仅用作显示
        display.setFocusable(false);
        display.setFocusableInTouchMode(false);

        setButtonListeners();
    }

    private void setButtonListeners() {
        // 数字按钮
        int[] numberButtons = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        };
        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(this::onNumberClick);
        }

        // 运算符按钮（加、减、乘、除）
        int[] operatorButtons = {
                R.id.btn_jia, R.id.btn_jian, R.id.btn_cheng, R.id.btn_chu
        };
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(this::onOperatorClick);
        }

        // "%" 按钮：转换为百分数（除以 100）
        findViewById(R.id.btn_baifen).setOnClickListener(v -> convertToPercentage());

        findViewById(R.id.btn_c).setOnClickListener(v -> clear());
        findViewById(R.id.btn_del).setOnClickListener(v -> deleteLast());
        findViewById(R.id.btn_deng).setOnClickListener(v -> calculate());
        findViewById(R.id.btn_dot).setOnClickListener(v -> addDecimal());
    }

    // 数字按钮点击：追加数字并实时更新显示
    private void onNumberClick(View view) {
        if (isNewOp) {
            display.setText("");
            isNewOp = false;
        }
        Button button = (Button) view;
        input += button.getText().toString();
        display.setText(input);
    }

    // 运算符按钮点击：记录第一个数、追加运算符到显示中，并准备输入第二个数
    private void onOperatorClick(View view) {
        if (!input.isEmpty()) {
            try {
                // 将当前输入解析为第一个操作数
                firstNumber = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                clear();
                return;
            }
            Button button = (Button) view;
            operator = button.getText().toString();
            isNewOp = true;
            input += " " + operator + " ";
            display.setText(input);
        }
    }

    // 等号按钮点击：计算并显示结果
    private void calculate() {
        if (!input.isEmpty() && !operator.isEmpty()) {
            String[] parts = input.split(" ");
            if (parts.length < 3) return;
            double secondNumber;
            try {
                secondNumber = Double.parseDouble(parts[2]);
            } catch (NumberFormatException e) {
                clear();
                return;
            }
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "—":
                    result = firstNumber - secondNumber;
                    break;
                case "×":
                    result = firstNumber * secondNumber;
                    break;
                case "÷":
                    result = (secondNumber != 0) ? firstNumber / secondNumber : 0;
                    break;
            }

            // 如果结果为整数，则不显示小数位
            String resultText;
            if (result == (long) result) {
                resultText = String.valueOf((long) result);
            } else {
                resultText = String.valueOf(result);
            }
            display.setText(resultText);
            input = resultText;
            operator = "";
            isNewOp = true;
        }
    }

    // 小数点按钮点击：仅允许输入一次小数点
    private void addDecimal() {
        if (!input.contains(".")) {
            input += ".";
            display.setText(input);
        }
    }

    // 删除按钮点击：删除输入的最后一位字符
    private void deleteLast() {
        if (!input.isEmpty()) {
            input = input.substring(0, input.length() - 1);
            display.setText(input);
        }
    }

    // 清除按钮点击：清空所有输入和状态
    private void clear() {
        input = "";
        operator = "";
        firstNumber = 0;
        display.setText("0");
        isNewOp = true;
    }

    // "%" 按钮点击：将当前输入的数字转换为百分数（除以 100）
    private void convertToPercentage() {
        if (!input.isEmpty()) {
            try {
                double value = Double.parseDouble(input);
                double result = value / 100;
                String resultText;
                if (result == (long) result) {
                    resultText = String.valueOf((long) result);
                } else {
                    resultText = String.valueOf(result);
                }
                display.setText(resultText);
                input = resultText;
                isNewOp = true;
            } catch (NumberFormatException e) {
                clear();
            }
        }
    }
}
