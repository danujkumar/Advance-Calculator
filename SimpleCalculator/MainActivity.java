package com.example.simplecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    String num="";
    boolean one_zero = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button one = findViewById(R.id.ones);
        Button two = findViewById(R.id.twos);
        Button three = findViewById(R.id.threes);
        Button four = findViewById(R.id.fours);
        Button five = findViewById(R.id.fives);
        Button six = findViewById(R.id.sixs);
        Button seven = findViewById(R.id.sevens);
        Button eight = findViewById(R.id.eights);
        Button nine = findViewById(R.id.nines);
        Button zero = findViewById(R.id.zeros);
        Button dot = findViewById(R.id.dots);
        Button plus = findViewById(R.id.pluss);
        Button minus = findViewById(R.id.minuss);
        Button mul = findViewById(R.id.muls);
        Button divs = findViewById(R.id.divs);
        Button equal = findViewById(R.id.equal);
        Button power = findViewById(R.id.up_solve);
        Button open = findViewById(R.id.open_bracket);
        Button close = findViewById(R.id.close_bracket);
        Button del  = findViewById(R.id.del);
        Button ac = findViewById(R.id.clears);
        EditText ans = findViewById(R.id.solution);
        ans.setFocusable(false);
        ans.setInputType(0);
        ArrayList<String> express = new ArrayList<>();

        //The digit from 0-9 including . is appended here
        one.setOnClickListener(view -> noAppender("1",ans,express));
        two.setOnClickListener(view -> noAppender("2",ans,express));
        three.setOnClickListener(view -> noAppender("3",ans,express));
        four.setOnClickListener(view -> noAppender("4",ans,express));
        five.setOnClickListener(view -> noAppender("5",ans,express));
        six.setOnClickListener(view -> noAppender("6",ans,express));
        seven.setOnClickListener(view -> noAppender("7",ans,express));
        eight.setOnClickListener(view -> noAppender("8",ans,express));
        nine.setOnClickListener(view -> noAppender("9",ans,express));
        zero.setOnClickListener((View view) -> noAppender("0",ans,express));
        dot.setOnClickListener(view -> {
            if(!one_zero)
            {
                num = num + ".";
                ans.append(".");
                one_zero = true;
            }
        });

        //Various operations such as plus, minus etc is controlled here
        power.setOnClickListener(view -> operation("^",ans,express));
        plus.setOnClickListener(view -> operation("+",ans,express));
        minus.setOnClickListener(view -> operation("-",ans,express));
        open.setOnClickListener(view -> operation("(",ans,express));
        close.setOnClickListener(view -> operation(")",ans,express));
        mul.setOnClickListener(view -> operation("*",ans,express));
        divs.setOnClickListener(view -> operation("/",ans,express));

        /*This clears all the values and string written into the text box as well as clears all the
        buffers like express, reset one_zero to false and num to null.*/
        ac.setOnClickListener(view -> {
            ans.setText("");
            num = "";
            express.clear();
            one_zero=false;
        });

        //This method is used to backspace the written expression.
        del.setOnClickListener(view -> {
            try {
                String s = ans.getText().toString();
                StringBuilder stringBuffer;
                StringBuilder stringBuilder;
                int lst = express.size()-1;
                if(!num.equals(""))
                {
                    stringBuffer = new StringBuilder(s);
                    stringBuilder = new StringBuilder(num);
                    if(num.charAt(num.length()-1) == '.')
                        one_zero = false;
                    stringBuffer.deleteCharAt(s.length()-1);
                    stringBuilder.deleteCharAt(num.length()-1);
                }
                else
                {
                    if(express.get(lst).equals("+") || express.get(lst).equals("-")
                            || express.get(lst).equals("/") || express.get(lst).equals("*") ||
                            express.get(lst).equals("^") || express.get(lst).equals("(")
                            || express.get(lst).equals(")"))
                    {
                        stringBuilder = new StringBuilder(num);
                        stringBuffer = new StringBuilder(s);
                        stringBuffer.deleteCharAt(s.length()-1);
                        express.remove(lst);
                        if(s.length()!=1) {
                            if (express.get(lst - 1).contains("."))
                                one_zero = true;
                        }
                    }
                    else
                    {
                        num = express.get(lst);
                        express.remove(lst);
                        stringBuilder = new StringBuilder(num);
                        stringBuffer = new StringBuilder(s);
                        if(num.charAt(num.length()-1) == '.')
                            one_zero = false;
                        stringBuffer.deleteCharAt(s.length() - 1);
                        stringBuilder.deleteCharAt(num.length()-1);
                    }
                }
                num = stringBuilder.toString();
                ans.setText(null);
                ans.append(stringBuffer.toString());
            }catch(IndexOutOfBoundsException e)
            {
                Log.e("Index_out_of_Range",e.toString());
            }
            Log.i("num:",num);
            Log.i("ans:",ans.getText().toString());
            for(int i=0;i<express.size();i++)
                Log.i("Express : "+i,express.get(i));
        });

        /*This solves the expression by first converting the given expression into the postfix then
        evaluating the value of the postfix expression.*/
        equal.setOnClickListener(view -> {
                if(!num.equals(""))
                    express.add(num);

                Stack<String> expression = new Stack<>();
                Stack<String> final_expression = new Stack<>();

                for(int i = 0;i<express.size();i++)
                {
                    if(express.get(i).equals("+") || express.get(i).equals("-") || express.get(i).equals("/") || express.get(i).equals("*") ||
                            express.get(i).equals("^") || express.get(i).equals("(") || express.get(i).equals(")"))
                    {
                        if(expression.size() == 0)
                        {
                            expression.add(express.get(i));
                        }
                        else
                        {
                            switch (express.get(i)) {
                                case "(":
                                    expression.add(express.get(i));
                                    break;
                                case ")":
                                    while (!expression.peek().equals("(")) {
                                        final_expression.add(expression.pop());
                                        if (expression.isEmpty())
                                            break;
                                    }
                                    expression.pop();
                                    break;
                                case "^":
                                    while (expression.peek().equals("^")) {
                                        final_expression.add(expression.pop());
                                        if (expression.isEmpty())
                                            break;
                                    }
                                    expression.add(express.get(i));
                                    break;
                                case "*":
                                case "/":
                                    while (expression.peek().equals("*") || expression.peek().equals("/") || expression.peek().equals("^")) {
                                        final_expression.add(expression.pop());
                                        if (expression.isEmpty())
                                            break;
                                    }
                                    expression.add(express.get(i));
                                    break;
                                case "+":
                                    while (expression.peek().equals("/") || expression.peek().equals("*") || expression.peek().equals("-") || expression.peek().equals("+") || expression.peek().equals("^")) {
                                        final_expression.add(expression.pop());
                                        if (expression.isEmpty())
                                            break;
                                    }
                                    expression.add(express.get(i));
                                    break;
                                default:
                                    while (expression.peek().equals("/") || expression.peek().equals("*") || expression.peek().equals("+") || expression.peek().equals("-") || expression.peek().equals("^")) {
                                        final_expression.add(expression.pop());
                                        if (expression.isEmpty())
                                            break;
                                    }
                                    expression.add(express.get(i));
                                    break;
                            }
                        }
                    }
                    else
                    {
                        final_expression.add(express.get(i));
                    }
                }

                if(!expression.isEmpty())
                    while(expression.size()!=0)
                    {
                        final_expression.add(expression.pop());
                    }
                    ans.setText("");
                    try {
                        ans.append(String.valueOf(getAnswer(final_expression)));
                    }    catch (Exception ex)
                    {
                        Toast.makeText(this,"Wrong expression given, please recheck again!!",Toast.LENGTH_LONG).show();
                    }
                    final_expression.clear();
                    expression.clear();
                    express.clear();
                    num = ans.getText().toString();
                    one_zero = num.contains(".");
        });
    }

    /*This method is used to apply the operation into the ans editbox as well as to append the
    operation into the express ArrayList.
     */
    private void operation(String append,EditText ans, ArrayList<String> express)
    {
        one_zero = false;
        if(!num.equals(""))
            express.add(num);
        num = "";
        ans.append(append);
        express.add(append);
    }

    /*
    This method is used to append the number into the num string by analyzing the previous expression.
     */
    private void noAppender(String append, EditText ans, ArrayList<String> express)
    {
        ans.append(append);
        if(num.equals(""))
        {
            if(express.isEmpty())
                num = num + append;
            else if(express.size() == 1)
            {
                if(express.get(0).equals("("))
                    num = num + append;
                else
                {
                    num = express.get(0) + append;
                    express.remove(0);
                }
            }
            else
            {
                if(express.get(express.size()-1).equals("(")|| express.get(express.size()-1).equals(")"))
                    num = num + append;
                else if (express.get(express.size()-2).equals("-") || express.get(express.size()-2).equals("+")
                        || express.get(express.size()-2).equals("/") || express.get(express.size()-2).equals("*") || express.get(express.size()-2).equals("^")
                        || express.get(express.size()-2).equals("("))
                {
                    num = express.get(express.size()-1) + append;
                    express.remove(express.size()-1);
                }
                else
                    num = num + append;
            }
        }
        else
            num = num + append;
    }

    //This method is used to evaluate the postfix expression given by the equal.
    private double getAnswer(@NonNull Stack<String> s)
    {
        Stack<Double> ans = new Stack<>();
        for(int i = 0;i<s.size();i++)
        {
            if(s.get(i).equals("/") || s.get(i).equals("*")|| s.get(i).equals("+") || s.get(i).equals("-") || s.get(i).equals("^"))
            {
                double first = ans.pop();
                double second = 0;
                if(!ans.isEmpty())
                    second = ans.pop();
                switch (s.get(i)) {
                    case "/":
                        ans.push(second / first);
                        break;
                    case "*":
                        ans.push(second * first);
                        break;
                    case "+":
                        ans.push(second + first);
                        break;
                    case "-":
                        ans.push(second - first);
                        break;
                    default:
                        if(second < 0)
                        {
                            second = -(second);
                            ans.push(-Math.pow(second,first));
                        }
                        else
                            ans.push(Math.pow(second, first));
                        break;
                }
            }
            else
                ans.push(Double.parseDouble(s.get(i)));
        }
        return ans.pop();
    }
}