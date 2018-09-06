package com.israel.martinez.javatest;

import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.israel.martinez.javatest.OpenGloveAPI_Java_HL.Communication;
import com.israel.martinez.javatest.OpenGloveAPI_Java_HL.MessageGenerator;
import com.israel.martinez.javatest.OpenGloveAPI_Java_HL.OpenGlove;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    public int MessageReceivedCounter = 0;

    List<Integer> flexorRegions = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    List<Integer> flexorPins = Arrays.asList(17, 17, 17, 17, 17, 17, 17, 17, 17, 17 ); //for simulate more flexors
    List<Integer> actuatorRegions = Arrays.asList(0, 1, 2, 3, 4);
    List<Integer> actuatorPositivePins = Arrays.asList(11, 10, 9, 3, 6);
    List<Integer> actuatorNegativePins = Arrays.asList(12, 15, 16, 2, 8);

    //public LatencyTest latencyTest;
    public volatile int actuatorStepCounter = 0;

    List<Integer> samplesQuantityList = Arrays.asList(100, 1000, 2000, 5000, 10000);
    List<String> componentTypeList = Arrays.asList("actuators", "flexors&IMU"); //future supported test { "actuators", "flexors", "flexors&IMU", "actuators&flexor&IMU"};
    List<Integer> componentQuantityList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    OpenGlove leftHand;
    Switch switchWebSocketStatus, switchBluetoothDeviceStatus, switchConfigurationStatus, switchLatencyStatus, switchTurnOnOffActuators;
    Spinner spinnerSamplesQuantity, spinnerComponentType, spinnerComponentQuantity;
    TextView textViewTotalOfMessageCounter, textViewOnMessage, textViewOnFlexor, textViewOnIMU;

    public int totalOfMessageCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTotalOfMessageCounter = findViewById(R.id.textView_TotalOfMessageReceived);
        textViewOnMessage = findViewById(R.id.textView_OnMessage);
        textViewOnFlexor = findViewById(R.id.textView_OnFlexor);
        textViewOnIMU = findViewById(R.id.textView_OnIMU);

        switchWebSocketStatus = findViewById(R.id.switch_WebSocketStatus);
        switchBluetoothDeviceStatus = findViewById(R.id.switch_BluetoothDeviceStatus);
        switchConfigurationStatus = findViewById(R.id.switch_ConfigurationStatus);
        switchLatencyStatus = findViewById(R.id.switch_LatencyTestStatus);
        switchTurnOnOffActuators = findViewById(R.id.switch_TurnOnOffActuators);

        switchWebSocketStatus.setOnCheckedChangeListener(this);
        switchBluetoothDeviceStatus.setOnCheckedChangeListener(this);
        switchConfigurationStatus.setOnCheckedChangeListener(this);
        switchLatencyStatus.setOnCheckedChangeListener(this);
        switchTurnOnOffActuators.setOnCheckedChangeListener(this);

        spinnerSamplesQuantity = findViewById(R.id.spinner_SamplesQuantity);
        spinnerComponentType = findViewById(R.id.spinner_ComponentType);
        spinnerComponentQuantity = findViewById(R.id.spinner_ComponentsQuantity);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.samples_quantity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSamplesQuantity.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.component_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComponentType.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.component_quantity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComponentQuantity.setAdapter(adapter);

        testMessageGenerator();
        try {
            intantiateOpenGlove();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public void testMessageGenerator(){
        String MainSeparator = ";";
        String SecondarySeparator = ",";
        String Empty = "";
        String action = "action";
        String device = "deviceName";
        String deviceName = "OpenGloveIZQ";
        String regions = "regions";
        String values = "values";
        String extraValues = "extraValues";

        List<Integer> integers = Arrays.asList(0,1,2,3,4);
        List<Integer> pins = Arrays.asList(17,17,17,17,17);
        List<String> strings = Arrays.asList("255","0","HIGH","LOW","255");

        MessageGenerator messageGenerator = new MessageGenerator(MainSeparator, SecondarySeparator, Empty);

        System.out.println("######################################################");
        System.out.println("Message Format: "+ messageGenerator.Join(MainSeparator, action, device, regions, values, extraValues));
        System.out.println("ActivateActuators: "+ messageGenerator.ActivateActuators(deviceName, integers, strings));
        System.out.println("AddFlexors: "+ messageGenerator.AddFlexors(deviceName, integers, pins));
        System.out.println("SetIMUStatus: "+ messageGenerator.SetIMUStatus(deviceName, true));
        System.out.println("######################################################");
    }

    public void intantiateOpenGlove() throws URISyntaxException {
        String url =  "ws://127.0.0.1:7070";
        leftHand = new OpenGlove("Left Hand", "OpenGloveIZQ", "leftHand", new URI(url));
        //leftHand.Communication.OnFlexorValueReceived = this::OnFlexorValueReceived;//(mapping, value) -> OnFlexorValueReceived(mapping, value);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        onCheckedChangedHandler(buttonView, isChecked);
    }

    public void onCheckedChangedHandler(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_WebSocketStatus:
                System.out.println("OnSwitch_WebSocketStatus, isChecked: "+isChecked);
                if (isChecked) {
                    leftHand.ConnectToWebSocketServer();
                    leftHand.Communication.setOnFlexorValueReceived(this::OnFlexorValueReceived);
                    leftHand.Communication.setOnAllIMUValuesReceived(this::OnAllIMUValueReceived);
                    leftHand.Communication.setOnInfoMessagesReceived(this::OnInfoMessageReceived);
                }
                else
                    leftHand.DisconnectFromWebSocketServer();
                break;

            case R.id.switch_BluetoothDeviceStatus:
                System.out.println("OnSwitch_BluetoothDeviceStatus, isChecked: "+isChecked);
                if (isChecked)
                    leftHand.ConnectToBluetoothDevice();
                else
                    leftHand.DisconnectFromBluetoothDevice();
                break;

            case R.id.switch_ConfigurationStatus:
                System.out.println("OnSwitch_ConfigurationStatus, isChecked: "+isChecked);
                if (isChecked) {
                    leftHand.GetOpenGloveArduinoVersionSoftware();
                    leftHand.AddActuators(actuatorRegions, actuatorPositivePins, actuatorNegativePins);
                    leftHand.AddFlexors(flexorRegions, flexorPins);
                    leftHand.SetIMUStatus(true);
                    leftHand.SetLoopDelay(0);
                    leftHand.SetThreshold(0);
                    leftHand.SaveOpenGloveConfiguration();
                } else {
                    leftHand.ResetFlexors();
                    leftHand.ResetActuators();
                    leftHand.TurnOffIMU();
                }
                break;

            case R.id.switch_LatencyTestStatus:
                System.out.println("OnSwitch_LatencyTestStatus, isChecked: "+isChecked);
                if (isChecked) {
                    leftHand.Start();
                } else {
                    leftHand.Stop();
                }
                break;
            case R.id.switch_TurnOnOffActuators:
                System.out.println("OnSwitch_TurnOnOffActuators, isChecked: "+isChecked);
                if (isChecked) {
                    leftHand.TurnOnActuators();
                } else {
                    leftHand.TurnOffActuators();
                }
                break;

            default:
                System.out.println("OnSwitch_Default?, isChecked: "+isChecked);
                break;
        }
    }

    public void OnFlexorValueReceived(int mapping, int value) {
        totalOfMessageCounter++;
        this.runOnUiThread(() -> textViewOnFlexor.setText(String.valueOf(this.totalOfMessageCounter)));
    }

    public void OnAllIMUValueReceived(float ax, float ay, float az, float gx, float gy, float gz, float mx, float my, float mz) {
        totalOfMessageCounter++;
        this.runOnUiThread(() -> textViewOnIMU.setText(TextUtils.join(",", Arrays.asList(ax, ay, az, gx, gy, gz, mx, my, mz))));
    }

    public void OnInfoMessageReceived(String message) {
        totalOfMessageCounter++;
        this.runOnUiThread(() -> textViewOnMessage.setText(message));
    }
}
