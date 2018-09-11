package com.israel.martinez.javatest.OpenGloveAPI_Java_HL;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by israel-martinez on 05-09-18.
 */

public class MessageGenerator {
    
    public String MainSeparator;
    public String SecondarySeparator;
    public String Empty;

    public enum OpenGloveActions
    {
        StartOpenGlove, // 0
        StopOpenGlove,
        AddOpenGloveDeviceToServer,
        RemoveOpenGloveDeviceFromServer,
        SaveOpenGloveConfiguration,
        ConnectToBluetoothDevice,
        DisconnectFromBluetoothDevice,
        ConnectToWebSocketServer,
        DisconnecFromWebSocketServer,
        StartCaptureDataFromServer,
        StopCaptureDataFromServer,

        AddActuator, // 11
        AddActuators,
        RemoveActuator,
        RemoveActuators,
        ActivateActuators,
        ActivateActuatorsTimeTest,
        TurnOnActuators,
        TurnOffActuators,
        ResetActuators,

        AddFlexor, //20
        AddFlexors,
        RemoveFlexor,
        RemoveFlexors,
        CalibrateFlexors,
        ConfirmCalibration,
        SetThreshold,
        TurnOnFlexors,
        TurnOffFlexors,
        ResetFlexors,

        StartIMU, // 30
        SetIMUStatus,
        SetRawData,
        SetIMUChoosingData,
        ReadOnlyAccelerometerFromIMU,
        ReadOnlyGyroscopeFromIMU,
        ReadOnlyMagnetometerFromIMU,
        ReadOnlyAttitudeFromIMU,
        ReadAllDataFromIMU,
        CalibrateIMU,
        TurnOnIMU,
        TurnOffIMU,

        SetLoopDelay, // 42
        GetOpenGloveArduinoSoftwareVersion,
    }

    public MessageGenerator(String mainSeparator, String secondarySeparator, String empty)
    {
        this.MainSeparator = mainSeparator;
        this.SecondarySeparator = secondarySeparator;
        this.Empty = empty;
    }

    /* For implement this Join to other specific languages (C# String.Join using params object[] and Java String.Join using Object ...)
     * 
     * C#: String.Join https://docs.microsoft.com/en-us/dotnet/api/system.String.join?view=netframework-4.7.2#System_String_Join_System_String_System_Object___
     * You can test your C# code or other in:
     *  https://www.tutorialspoint.com/codingground.htm
     *  https://www.tutorialspoint.com/compile_csharp_online.php
    */


    public static String JoinListIntegers(String separator, List<Integer> list)
    {
        List<String> stringsList = new ArrayList<>();
        for (Integer number : list){
            stringsList.add(String.valueOf(number));
        }
        return TextUtils.join(separator, stringsList);
    }

    public static String JoinList(String separator, List<String> list)
    {
        return TextUtils.join(separator, list);
    }

    public static String Join(String separator, CharSequence ... elements)
    {
        return TextUtils.join(separator, elements);
    }

    private String BooleanString(boolean value)
    {
        String booleaneanString = (value) ? "True" : "False";
        return booleaneanString;
    }

    public String StartOpenGlove(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.StartOpenGlove.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String StopOpenGlove(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.StopOpenGlove.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String AddOpenGloveDeviceToServer(String bluetoothDeviceName, String configurationName)
    {
        return Join(MainSeparator, OpenGloveActions.AddOpenGloveDeviceToServer.ordinal()+"", bluetoothDeviceName, Empty, configurationName, Empty);
    }

    public String RemoveOpenGloveDeviceFromServer(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.RemoveOpenGloveDeviceFromServer.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }
    public String SaveOpenGloveConfiguration(String bluetoothDeviceName, String configurationName)
    {
        return Join(MainSeparator, OpenGloveActions.SaveOpenGloveConfiguration.ordinal()+"", bluetoothDeviceName, Empty, configurationName, Empty);
    }

    public String ConnectToBluetoothDevice(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ConnectToBluetoothDevice.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String DisconnectFromBluetoothDevice(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.DisconnectFromBluetoothDevice.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String StartCaptureDataFromServer(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.StartCaptureDataFromServer.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String StopCaptureDataFromServer(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.StopCaptureDataFromServer.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String AddActuator(String bluetoothDeviceName, int region, int positivePin, int negativePin)
    {
        return Join(MainSeparator, OpenGloveActions.AddActuator.ordinal()+"", bluetoothDeviceName, region+"", positivePin+"", negativePin+"");
    }

    public String AddActuators(String bluetoothDeviceName, List<Integer> regions, List<Integer> positivePins, List<Integer> negativePins)
    {
        return Join(MainSeparator, OpenGloveActions.AddActuators.ordinal()+"", bluetoothDeviceName, JoinListIntegers(SecondarySeparator, regions), JoinListIntegers(SecondarySeparator, positivePins), JoinListIntegers(SecondarySeparator, negativePins));
    }

    public String RemoveActuator(String bluetoothDeviceName, int region)
    {
        return Join(MainSeparator, OpenGloveActions.RemoveActuator.ordinal()+"", bluetoothDeviceName, region+"", Empty, Empty);
    }

    public String RemoveActuators(String bluetoothDeviceName, List<Integer> regions)
    {
        return Join(MainSeparator, OpenGloveActions.RemoveActuators.ordinal()+"", bluetoothDeviceName, JoinListIntegers(SecondarySeparator, regions), Empty, Empty);
    }

    public String ActivateActuators(String bluetoothDeviceName, List<Integer> regions, List<String> intensities)
    {
        return Join(MainSeparator, OpenGloveActions.ActivateActuators.ordinal()+"", bluetoothDeviceName, JoinListIntegers(SecondarySeparator, regions), JoinList(SecondarySeparator, intensities), Empty);
    }

    public String ActivateActuatorsTimeTest(String bluetoothDeviceName, List<Integer> regions, List<String> intensities)
    {
        return Join(MainSeparator, OpenGloveActions.ActivateActuatorsTimeTest.ordinal()+"", bluetoothDeviceName, JoinListIntegers(SecondarySeparator, regions), JoinList(SecondarySeparator, intensities), Empty);
    }

    public String TurnOnActuators(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.TurnOnActuators.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String TurnOffActuators(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.TurnOffActuators.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String ResetActuators(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ResetActuators.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String AddFlexor(String bluetoothDeviceName, int region, int pin)
    {
        return Join(MainSeparator, OpenGloveActions.AddFlexor.ordinal()+"", bluetoothDeviceName, region+"", pin+"", Empty);
    }

    public String AddFlexors(String bluetoothDeviceName, List<Integer> regions, List<Integer> pins)
    {
        return Join(MainSeparator, OpenGloveActions.AddFlexors.ordinal()+"", bluetoothDeviceName, JoinListIntegers(SecondarySeparator, regions), JoinListIntegers(SecondarySeparator, pins), Empty);
    }

    public String RemoveFlexor(String bluetoothDeviceName, int region)
    {
        return Join(MainSeparator, OpenGloveActions.RemoveFlexor.ordinal()+"", bluetoothDeviceName, region+"", Empty, Empty);
    }

    public String RemoveFlexors(String bluetoothDeviceName, List<Integer> regions)
    {
        return Join(MainSeparator, OpenGloveActions.RemoveFlexors.ordinal()+"", bluetoothDeviceName, JoinListIntegers(SecondarySeparator, regions), Empty, Empty);
    }

    public String CalibrateFlexors(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.CalibrateFlexors.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String ConfirmCalibration(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ConfirmCalibration.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String SetThreshold(String bluetoothDeviceName, int value)
    {
        return Join(MainSeparator, OpenGloveActions.SetThreshold.ordinal()+"", bluetoothDeviceName, Empty, value+"", Empty);
    }

    public String TurnOnFlexors(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.TurnOnFlexors.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String TurnOffFlexors(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.TurnOffFlexors.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String ResetFlexors(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ResetFlexors.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String StartIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.StartIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String SetIMUStatus(String bluetoothDeviceName, boolean status)
    {
        return Join(MainSeparator, OpenGloveActions.SetIMUStatus.ordinal()+"", bluetoothDeviceName, Empty, BooleanString(status), Empty);
    }

    public String SetRawData(String bluetoothDeviceName, boolean status)
    {
        return Join(MainSeparator, OpenGloveActions.SetRawData.ordinal()+"", bluetoothDeviceName, Empty, BooleanString(status), Empty);
    }

    public String SetIMUChoosingData(String bluetoothDeviceName, int value)
    {
        return Join(MainSeparator, OpenGloveActions.SetIMUChoosingData.ordinal()+"", bluetoothDeviceName, Empty, value+"", Empty);
    }

    public String ReadOnlyAccelerometerFromIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ReadOnlyAccelerometerFromIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String ReadOnlyGyroscopeFromIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ReadOnlyGyroscopeFromIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String ReadOnlyMagnetometerFromIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ReadOnlyMagnetometerFromIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String ReadOnlyAttitudeFromIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ReadOnlyAttitudeFromIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String ReadAllDataFromIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.ReadAllDataFromIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }


    public String CalibrateIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.CalibrateIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String TurnOnIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.TurnOnIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String TurnOffIMU(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.TurnOffIMU.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }

    public String SetLoopDelay(String bluetoothDeviceName, int value)
    {
        return Join(MainSeparator, OpenGloveActions.SetLoopDelay.ordinal()+"", bluetoothDeviceName, Empty, value+"", Empty);
    }

    public String GetOpenGloveArduinoVersionSoftware(String bluetoothDeviceName)
    {
        return Join(MainSeparator, OpenGloveActions.GetOpenGloveArduinoSoftwareVersion.ordinal()+"", bluetoothDeviceName, Empty, Empty, Empty);
    }
}
