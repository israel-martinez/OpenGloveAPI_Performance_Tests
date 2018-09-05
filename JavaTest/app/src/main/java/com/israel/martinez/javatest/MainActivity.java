package com.israel.martinez.javatest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.israel.martinez.javatest.OpenGloveAPI_Java_HL.MessageGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String MainSeparator = ";";
        String SecondarySeparator = ",";
        String Empty = "";
        int action = 8;
        String deviceName = "OpenGloveIZQ";
        String regions = "regions";
        String values = "values";
        String extraValues = "extraValues";

        List<Integer> integers = Arrays.asList(0,1,2,3,4);
        List<Integer> pins = Arrays.asList(17,17,17,17,17);
        List<String> strings = Arrays.asList("255","0","HIGH","LOW","255");


        List<String> myNameList = new ArrayList<String>();
        myNameList.add("Israel");
        myNameList.add("Sofía");

        System.out.println("######################################################");
        System.out.println(TextUtils.join(MainSeparator, integers));
        System.out.println(TextUtils.join(MainSeparator, strings));
        System.out.println(JoinList(" and ", myNameList));
        System.out.println(Join(MainSeparator, String.valueOf(action), deviceName, JoinListIntegers(SecondarySeparator, integers), JoinList(SecondarySeparator, strings), extraValues));
        System.out.println(Join(MainSeparator,  MessageGenerator.OpenGloveActions.ActivateActuators.ordinal()+"", deviceName, JoinListIntegers(SecondarySeparator, integers), JoinList(SecondarySeparator, strings), extraValues));
        System.out.println("######################################################");
        MessageGenerator messageGenerator = new MessageGenerator(";", ",", "");

        System.out.println("ActivateActuators: "+ messageGenerator.ActivateActuators("OpenGloveIZQ", integers, strings));
        System.out.println("AddFlexors: "+ messageGenerator.AddFlexors("OpenGloveIZQ", integers, pins));
        System.out.println("SetIMUStatus: "+ messageGenerator.SetIMUStatus("OpenGloveIZQ", true));
        System.out.println("######################################################");
    }



    public static String JoinListIntegers(String separator, List<Integer> list){
        List<String> stringsList = new ArrayList<>();
        for (Integer number : list){
            stringsList.add(String.valueOf(number));
        }
        return TextUtils.join(separator, stringsList);
    }

    public static String JoinList(String separator, List<String> list){
        return TextUtils.join(separator, list);
    }

    public static String Join(String separator, CharSequence ... elements){
        return TextUtils.join(separator, elements);
    }
}
