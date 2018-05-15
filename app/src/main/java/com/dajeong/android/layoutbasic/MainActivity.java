package com.dajeong.android.layoutbasic;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ConstraintLayout layout;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnPlus, btnMinus, btnMultiply, btnDivide, btnCancle;
    Button btnBack, btnDot, btnBracket1, btnBracket2;
    ImageButton btnCalc;
    TextView result,preview;
    View target;
    float targetX, targetY; //textview의 좌표를 가져올 변수.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);
        result = findViewById(R.id.result);
        preview = findViewById(R.id.preview);
        target = findViewById(R.id.target);
        //view->target의 좌표가져오기.
        target.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                targetX = target.getX();
                targetY = target.getY();

            }
        });

        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        btnCalc = findViewById(R.id.btnCalc);
        btnCancle = findViewById(R.id.btnCancle);
        btnBack = findViewById(R.id.btnBack);
        btnDot = findViewById(R.id.btnDot);
        btnBracket1 = findViewById(R.id.btnBracket1);
        btnBracket2 = findViewById(R.id.btnBracket2);

        //리스너 생성
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnCalc.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnDot.setOnClickListener(this);
        btnBracket1.setOnClickListener(this);
        btnBracket2.setOnClickListener(this);


        // 버튼 for이용해 findViewId, setOnClickListener 처리하기. 위같은 리스너 생성을 간소화시킬수 있다.
        //1. 버튼 배열을 만든다.
         for(int i = 0; i<10; i++){
             int id = getResources().getIdentifier( "btn"+i, "id", getPackageName()); //getResources() =  문자열값을 int로
             //btns[i] = findViewById(id);
             //btns[i].setOnClickListener(this);
             findViewById(id).setOnClickListener(this); //이렇게 쓰는건 button만 가능.
         }
    }

    @Override
    public void onClick(View view) {

        String temp = preview.getText().toString();
        if("0".equals(temp)) temp = "";
        //String 리소스를 코드에서 이용하기.
        String PRE = getResources().getString(R.string.textPreview);
        if(PRE.equals(temp)) temp ="";
        switch (view.getId()){
            case R.id.btn0: temp +="0"; break;
            case R.id.btn1:
                temp +="1"; break;
            case R.id.btn2: temp +="2"; break;
            case R.id.btn3: temp +="3"; break;
            case R.id.btn4: temp +="4"; break;
            case R.id.btn5: temp +="5"; break;
            case R.id.btn6: temp +="6"; break;
            case R.id.btn7: temp +="7"; break;
            case R.id.btn8: temp +="8"; break;
            case R.id.btn9: temp +="9"; break;
            case R.id.btnPlus: temp +="+"; break;
            case R.id.btnMinus: temp +="-"; break;
            case R.id.btnMultiply: temp +="*"; break;
            case R.id.btnDivide : temp +="/"; break;
            case R.id.btnDot: temp +="."; break;
            case R.id.btnBracket1: temp +="("; break;
            case R.id.btnBracket2: temp +=")"; break;
            case R.id.btnCancle: temp ="0"; result.setText("0");break;
            case R.id.btnCalc:
                result.setText(calc(temp));
                temp = "미리보기";
                break;

        }

        if(view.getId() == R.id.btnBack){
            int nLength = temp.length();//입력된 수식의 길이.
            if(nLength >1){
                temp = temp.substring(0,nLength-1);
            }else{
                temp = "0";
            }
        }
        if("+".equals(temp) || "-".equals(temp) || "*".equals(temp) || "/".equals(temp)){//처음에 연산자가 먼저 입력되면
            switch (view.getId()) {
                case R.id.btnPlus: temp = "0"; break;
                case R.id.btnMinus: temp = "0"; break;
                case R.id.btnMultiply: temp = "0"; break;
                case R.id.btnDivide: temp = "0"; break;
            }
            Toast.makeText(getBaseContext(), "잘못된 수식입니다.", Toast.LENGTH_SHORT).show();
        }
        runAnimation(view);
        preview.setText(temp);

    }
    //애니메이션 생성.
    public void runAnimation(View current) {
        //가상버튼생성
        final Button dummy = new Button(this);
        //레이아웃에 버튼 추가
        layout.addView(dummy);
        //버튼의 속성을 정의.
        dummy.setWidth(current.getWidth()); //현재버튼값의 넓이 만큼 가상버튼 만들기
        dummy.setHeight(current.getHeight()); //현재버튼값의 높이 만큼 가상버튼 만들기
        dummy.setBackgroundColor(Color.GRAY); //가상버튼의 색상은 그레이
        dummy.setX(current.getX()); //현재버튼값의 x좌표에서 가상버튼 생성
        dummy.setY(current.getY()); //현재버튼값의 y좌표에서 가상버튼 생성
        dummy.setPivotX(50); //중심점
        dummy.setPivotY(50);


        ObjectAnimator aniX = ObjectAnimator.ofFloat(dummy,
                View.X,
                targetX);
        ObjectAnimator aniY = ObjectAnimator.ofFloat(dummy,
                View.Y,
                targetY);
        ObjectAnimator aniR = ObjectAnimator.ofFloat(dummy,
                View.ROTATION,
                720);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(dummy,
                View.SCALE_X,
                0.2f); //크기가 줄어드는것 (x좌표)
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(dummy,
                View.SCALE_Y,
                0.2f); //크기가 줄어드는것 (y좌표)
        AnimatorSet aniSet = new AnimatorSet();
        aniSet.playTogether(aniX, aniY, aniR, scaleX, scaleY); //애니매이터를 전부 합침
        aniSet.setDuration(1000);
        //애니메이션 완료시 처리를 위한 리스너
        aniSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //애니메이션이 완료되면 버튼을 제거한다.
                layout.removeView(dummy);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        aniSet.start();

    }

    public String calc(String str){
        String result = "";
        //1.연산자와 숫자를 분리해서 처리.
        //split함수에 공백을 넣으면 문자 하나 단위로 분리해 준다.

//원본
        String arr1[] = str.split("");

        List<String> arr2 = new ArrayList<>(); //배열을 하나 더 선언.
        String temp = "";
        for(String item : arr1){
            if(item.equals("+") ||item.equals("*") ||item.equals("/") || item.equals("-") ) { //연산자인지를 먼저 구별
                arr2.add(temp); //반복문이 돌면서 연산자 나오기 전까지의 숫자를 저장함.
                temp = "";  //저장한 숫자를 초기화 시킴.
                arr2.add(item); //연산자 arr2에 담는다.
            }else{ //연산자가 아닐경우
                temp = temp + item;
            }
        }
        arr2.add(temp); //마지막에 더한 문자(숫자)를 저장한다.

        //List arr2 = {46, +, 95, -, 32, *, 5}
        //반복문을 돌면서 * / 연산자를 먼저체크 (연산자 우선순위로)
        for(int i =0; i<arr2.size() ; i++){
            String item = arr2.get(i);
            if(item.equals("*") || item.equals("/")){
                int prev = Integer.parseInt(arr2.get(i-1)); //전의 아이템을 꺼내서 숫자로 변환
                int next = Integer.parseInt(arr2.get(i+1)); //다음의 아이템을 꺼내서 숫자로 변환
                int calced = 0;
                if(item.equals("*"))
                    calced = prev * next;
                else
                    calced = prev / next;

                arr2.set(i,calced + ""); //현재위치값에 곱한값을 넣는다.
                arr2.remove(i+1); //뒷배열 삭제
                arr2.remove(i-1); //앞배열 삭제
            }
        }

        //반복문을 돌면서 + - 연산자를 나중에 체크
        for(int i =0; i<arr2.size() ; i++){
            String item = arr2.get(i);
            if(item.equals("+") || item.equals("-")){
                int prev = Integer.parseInt(arr2.get(i-1)); //전의 아이템을 꺼내서 숫자로 변환
                int next = Integer.parseInt(arr2.get(i+1)); //다음의 아이템을 꺼내서 숫자로 변환
                int calced = 0;
                if(item.equals("+"))
                    calced = prev + next;
                else
                    calced = prev - next;

                arr2.set(i,calced + ""); //현재위치값에 곱한값을 넣는다.
                arr2.remove(i+1);
                arr2.remove(i-1);
            }
        }

        result = arr2.get(0);
        //원본



       /*//다시 해본 것. 첫번째...
        String memo = str.substring("(",")"); //괄호 안의 수식 가져옴.
        //String arr[] = memo.split(""); //arr배열에 괄호 안 수식 하나하나 구분.

        String arr1[] = memo.split(""); //문자 하나하나 구분.

        List<String> arr2 = new ArrayList<>(); //배열을 하나 더 선언.
        String temp = "";
        for(String item : arr1){
            if(item.equals("+") ||item.equals("*") ||item.equals("/") || item.equals("-")) { //연산자인지를 먼저 구별
                arr2.add(temp); //반복문이 돌면서 연산자 나오기 전까지의 숫자를 저장함.
                temp = "";  //저장한 숫자를 초기화 시킴.
                arr2.add(item); //연산자 arr2에 담는다.
            }else{ //연산자가 아닐경우
                temp = temp + item;
            }
        }
        arr2.add(temp); //마지막에 더한 문자(숫자)를 저장한다.

        //List arr2 = {46, +, 95, -, 32, *, 5}
        //반복문을 돌면서 * / 연산자를 먼저체크 (연산자 우선순위로)
        for(int i =0; i<arr2.size() ; i++){
            String item = arr2.get(i);
            if(item.equals("*") || item.equals("/")){
                int prev = Integer.parseInt(arr2.get(i-1)); //전의 아이템을 꺼내서 숫자로 변환
                int next = Integer.parseInt(arr2.get(i+1)); //다음의 아이템을 꺼내서 숫자로 변환
                int calced = 0;
                if(item.equals("*"))
                    calced = prev * next;
                else
                    calced = prev / next;

                arr2.set(i,calced + ""); //현재위치값에 곱한값을 넣는다.
                arr2.remove(i+1); //뒷배열 삭제
                arr2.remove(i-1); //앞배열 삭제
            }
        }

        //반복문을 돌면서 + - 연산자를 나중에 체크
        for(int i =0; i<arr2.size() ; i++){
            String item = arr2.get(i);
            if(item.equals("+") || item.equals("-")){
                int prev = Integer.parseInt(arr2.get(i-1)); //전의 아이템을 꺼내서 숫자로 변환
                int next = Integer.parseInt(arr2.get(i+1)); //다음의 아이템을 꺼내서 숫자로 변환
                int calced = 0;
                if(item.equals("+"))
                    calced = prev + next;
                else
                    calced = prev - next;

                arr2.set(i,calced + ""); //현재위치값에 곱한값을 넣는다.
                arr2.remove(i+1);
                arr2.remove(i-1);
            }
        }*/


        return result;
    }
}
