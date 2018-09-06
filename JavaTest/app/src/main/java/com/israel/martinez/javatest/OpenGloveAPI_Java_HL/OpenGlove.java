package com.israel.martinez.javatest.OpenGloveAPI_Java_HL;

import java.net.URI;
import java.util.List;

/**
 * Created by israel-martinez on 05-09-18.
 */

public class OpenGlove {
    public String Name;
    public String BluetoothDeviceName;
    public String ConfigurationName;
    public URI WebSocketEndpointUrl;
    public Communication Communication;
    private boolean _IsConnectedToWebSocketServer;
    private boolean _IsConnectedToBluetoothDevice;
    public boolean IsConnectedToWebSocketServer;
    public boolean IsConnectedToBluetoothDevice;


    public OpenGlove(String name, String bluetoothDeviceName, String configurationName, URI webSocketEndPointUrl) {
        this.Name = name;
        this.BluetoothDeviceName = bluetoothDeviceName;
        this.ConfigurationName = configurationName;
        this.Communication = new Communication(bluetoothDeviceName, configurationName, webSocketEndPointUrl);
        this.WebSocketEndpointUrl = webSocketEndPointUrl;
        //this.Communication.OnBluetoothDeviceConnectionStateChanged += OnBluetoothDeviceConnectionStateChanged;
    }

    private void OnBluetoothDeviceConnectionStateChanged(boolean isConnected)
    {
        this._IsConnectedToBluetoothDevice = isConnected;
    }

    //Modify for different validation of IsConnected to WebSocket server in other languages of programing and libraries
    private boolean IsWebSocketConnected()
    {
        return this.Communication.isOpen();
    }

    public void Start()
    {
        this.Communication.StartOpenGlove(BluetoothDeviceName);
    }

    public void Stop()
    {
        this.Communication.StopOpenGlove(BluetoothDeviceName);
    }

    public void AddOpenGloveDeviceToServer()
    {
        this.Communication.AddOpenGloveDeviceToServer(BluetoothDeviceName, ConfigurationName);
    }

    public void RemoveOpenGloveDeviceFromServer()
    {
        this.Communication.RemoveOpenGloveDeviceFromServer(BluetoothDeviceName);
    }

    public void SaveOpenGloveConfiguration()
    {
        this.Communication.SaveOpenGloveConfiguration(BluetoothDeviceName, ConfigurationName);
    }

    public void ConnectToBluetoothDevice()
    {
        this.Communication.ConnectToBluetoothDevice(BluetoothDeviceName);
    }

    public void DisconnectFromBluetoothDevice()
    {
        this.Communication.DisconnectFromBluetoothDevice(BluetoothDeviceName);
    }

    public void StartCaptureDataFromServer()
    {
        this.Communication.StartCaptureDataFromServer(BluetoothDeviceName);
    }

    public void StopCaptureDataFromServer()
    {
        this.Communication.StopCaptureDataFromServer(BluetoothDeviceName);
    }

    public void AddActuator(int region, int positivePin, int negativePin)
    {
        this.Communication.AddActuator(BluetoothDeviceName ,region, positivePin, negativePin);
    }

    public void AddActuators(List<Integer> regions, List<Integer> positivePins, List<Integer> negativePins)
    {
        this.Communication.AddActuators(BluetoothDeviceName, regions, positivePins, negativePins);
    }

    public void RemoveActuator(int region)
    {
        this.Communication.RemoveActuator(BluetoothDeviceName, region);
    }

    public void RemoveActuators(List<Integer> regions)
    {
        this.Communication.RemoveActuators(BluetoothDeviceName, regions);
    }

    public void ActivateActuators(List<Integer> regions, List<String> intensities)
    {
        this.Communication.ActivateActuators(BluetoothDeviceName, regions, intensities);
    }

    public void ActivateActuatorsTimeTest(List<Integer> regions, List<String> intensities)
    {
        this.Communication.ActivateActuatorsTimeTest(BluetoothDeviceName, regions, intensities);
    }

    public void TurnOnActuators()
    {
        this.Communication.TurnOnActuators(BluetoothDeviceName);
    }

    public void TurnOffActuators()
    {
        this.Communication.TurnOffActuators(BluetoothDeviceName);
    }

    public void TurnOnFlexors()
    {
        this.Communication.TurnOnFlexors(BluetoothDeviceName);
    }

    public void TurnOffFlexors()
    {
        this.Communication.TurnOffFlexors(BluetoothDeviceName);
    }

    public void ResetActuators()
    {
        this.Communication.ResetActuators(BluetoothDeviceName);
    }

    public void AddFlexor(int region, int pin)
    {
        this.Communication.AddFlexor(BluetoothDeviceName, region, pin);
    }

    public void AddFlexors(List<Integer> regions, List<Integer> pins)
    {
        this.Communication.AddFlexors(BluetoothDeviceName, regions, pins);
    }

    public void RemoveFlexor(int region)
    {
        this.Communication.RemoveFlexor(BluetoothDeviceName, region);
    }

    public void RemoveFlexors(List<Integer> regions)
    {
        this.Communication.RemoveFlexors(BluetoothDeviceName, regions);
    }

    public void CalibrateFlexors()
    {
        this.Communication.CalibrateFlexors(BluetoothDeviceName);
    }

    public void ConfirmCalibration()
    {
        this.Communication.ConfirmCalibration(BluetoothDeviceName);
    }

    public void SetThreshold(int value)
    {
        this.Communication.SetThreshold(BluetoothDeviceName, value);
    }

    public void ResetFlexors()
    {
        this.Communication.ResetFlexors(BluetoothDeviceName);
    }

    public void StartIMU()
    {
        this.Communication.StartIMU(BluetoothDeviceName);
    }

    public void SetIMUStatus(boolean status)
    {
        this.Communication.SetIMUStatus(BluetoothDeviceName, status);
    }

    public void SetRawData(boolean status)
    {
        this.Communication.SetRawData(BluetoothDeviceName, status);
    }


        /* integer command      inside arduino code        IMU component
         * 0                 :          a            :      Accelerometer
         * 1                 :          g            :      Gyroscope
         * 2                 :          m            :      Magnetometer
         * 3                 :          r            :      Attitude
         * default (other)   :          z            :      Accelerometer, Gyroscope and Magnetometer
        */

    public void SetIMUChoosingData(int value)
    {
        this.Communication.SetIMUChoosingData(BluetoothDeviceName, value);
    }

    public void ReadOnlyAccelerometerFromIMU()
    {
        this.SetIMUChoosingData(0);
    }

    public void ReadOnlyGyroscopeFromIMU()
    {
        this.SetIMUChoosingData(1);
    }

    public void ReadOnlyMagnetometerFromIMU()
    {
        this.SetIMUChoosingData(2);
    }

    public void ReadOnlyAttitudeFromIMU()
    {
        this.SetIMUChoosingData(3);
    }

    public void ReadAllDataFromIMU()
    {
        this.SetIMUChoosingData(-1);
    }

    private void CalibrateIMU()
    {
        // Need Implement this on OpenGlove Aplication, see SwitchOpenGloveServer in OpenGloveServer class
        // Communication and MessageGenerator methods is OK
        //this.Communication.CalibrateIMU();
    }

    public void TurnOnIMU()
    {
        this.Communication.TurnOnIMU(BluetoothDeviceName);
    }

    public void TurnOffIMU()
    {
        this.Communication.TurnOffIMU(BluetoothDeviceName);
    }

    public void SetLoopDelay(int value)
    {
        this.Communication.SetLoopDelay(BluetoothDeviceName, value);
    }

    public void ConnectToWebSocketServer()
    {
        Communication = new Communication(BluetoothDeviceName, ConfigurationName, WebSocketEndpointUrl);
        this.Communication.ConnectToWebSocketServer();
    }

    public void DisconnectFromWebSocketServer()
    {
        this.Communication.DisconnectFromWebSocketServer();
    }

    public void GetOpenGloveArduinoVersionSoftware()
    {
        this.Communication.GetOpenGloveArduinoVersionSoftware(BluetoothDeviceName);
    }

    public enum HandRegion
    {
        // PalmarRegions
        PalmarFingerSmallDistal,
        PalmarFingerRingDistal,
        PalmarFingerMiddleDistal,
        PalmarFingerIndexDistal,

        PalmarFingerSmallMiddle,
        PalmarFingerRingMiddle,
        PalmarFingerMiddleMiddle,
        PalmarFingerIndexMiddle,

        PalmarFingerSmallProximal,
        PalmarFingerRingProximal,
        PalmarFingerMiddleProximal,
        PalmarFingerIndexProximal,

        PalmarPalmSmallDistal,
        PalmarPalmRingDistal,
        PalmarPalmMiddleDistal,
        PalmarPalmIndexDistal,

        PalmarPalmSmallProximal,
        PalmarPalmRingProximal,
        PalmarPalmMiddleProximal,
        PalmarPalmIndexProximal,

        PalmarHypoThenarSmall,
        PalmarHypoThenarRing,
        PalmarThenarMiddle,
        PalmarThenarIndex,

        PalmarFingerThumbProximal,
        PalmarFingerThumbDistal,

        PalmarHypoThenarDistal,
        PalmarThenar,

        PalmarHypoThenarProximal,


        // DorsalRegions
        DorsalFingerSmallDistal, // = 29,
        DorsalFingerRingDistal,
        DorsalFingerMiddleDistal,
        DorsalFingerIndexDistal,

        DorsalFingerSmallMiddle,
        DorsalFingerRingMiddle,
        DorsalFingerMiddleMiddle,
        DorsalFingerIndexMiddle,

        DorsalFingerSmallProximal,
        DorsalFingerRingProximal,
        DorsalFingerMiddleProximal,
        DorsalFingerIndexProximal,

        DorsalPalmSmallDistal,
        DorsalPalmRingDistal,
        DorsalPalmMiddleDistal,
        DorsalPalmIndexDistal,

        DorsalPalmSmallProximal,
        DorsalPalmRingProximal,
        DorsalPalmMiddleProximal,
        DorsalPalmIndexProximal,

        DorsalHypoThenarSmall,
        DorsalHypoThenarRing,
        DorsalThenarMiddle,
        DorsalThenarIndex,

        DorsalFingerThumbProximal,
        DorsalFingerThumbDistal,

        DorsalHypoThenarDistal,
        DorsalThenar,

        DorsalHypoThenarProximal,
    }

    public enum FlexorsRegion
    {
        ThumbInterphalangealJoint, // = 0,
        IndexInterphalangealJoint,
        MiddleInterphalangealJoint,
        RingInterphalangealJoint,
        SmallInterphalangealJoint,

        ThumbMetacarpophalangealJoint,
        IndexMetacarpophalangealJoint,
        MiddleMetacarpophalangealJoint,
        RingMetacarpophalangealJoint,
        SmallMetacarpophalangealJoint
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBluetoothDeviceName() {
        return BluetoothDeviceName;
    }

    public void setBluetoothDeviceName(String bluetoothDeviceName) {
        BluetoothDeviceName = bluetoothDeviceName;
    }

    public String getConfigurationName() {
        return ConfigurationName;
    }

    public void setConfigurationName(String configurationName) {
        ConfigurationName = configurationName;
    }

    public URI getWebSocketEndpointUrl() {
        return WebSocketEndpointUrl;
    }

    public void setWebSocketEndpointUrl(URI webSocketEndpointUrl) {
        WebSocketEndpointUrl = webSocketEndpointUrl;
    }

    public boolean isConnectedToWebSocketServer() {
        return IsConnectedToWebSocketServer;
    }

    public boolean isConnectedToBluetoothDevice() {
        return IsConnectedToBluetoothDevice;
    }
}
