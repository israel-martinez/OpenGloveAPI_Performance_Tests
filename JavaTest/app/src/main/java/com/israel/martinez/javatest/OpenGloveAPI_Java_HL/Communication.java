package com.israel.martinez.javatest.OpenGloveAPI_Java_HL;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.InterfaceAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by israel-martinez on 05-09-18.
 */

public class Communication extends WebSocketClient{

    //public Client WebSocket;
    public URI ServerURI;
    public MessageGenerator MessageGenerator;
    public String BluetoothDeviceName;
    public String ConfigurationName;

    private TimeTestServerLatencyActivateActuatorsReceived OnTimeTestServerLatencyActivateActuatorsReceived  = null;
    private TimeTestArduinoLatencyActivateActuatorsReceived OnTimeTestArduinoLatencyActivateActuatorsReceived = null;
    private FlexorMovement OnFlexorValueReceived  = null;
    private AccelerometerValues OnAccelerometerValuesReceived = null;
    private GyroscopeValues OnGyroscopeValuesReceived = null;
    private MagnetometerValues OnMagnetometerValuesReceived = null;
    private AllIMUValues OnAllIMUValuesReceived = null;
    private BluetoothDeviceConnectionState OnBluetoothDeviceConnectionStateChanged = null;
    private WebSocketConnectionState OnWebSocketConnectionStateChanged = null;
    private InfoMessage OnInfoMessagesReceived = null;

    public Communication(String bluetoothDeviceName, String configurationName, URI serverURI) {
        super(serverURI);
        this.BluetoothDeviceName = bluetoothDeviceName;
        this.ConfigurationName = configurationName;
        this.ServerURI = serverURI;
        this.MessageGenerator = new MessageGenerator(";", ",", "");
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("new connection opened");
        this.send(MessageGenerator.AddOpenGloveDeviceToServer(BluetoothDeviceName, ConfigurationName));
        this.send(MessageGenerator.StartCaptureDataFromServer(BluetoothDeviceName));
        System.out.println("OpenGlove.Communication.AddOpenGloveToServer()");
        System.out.println("Communication.StartCaptureDataFromServer()");

        if (OnWebSocketConnectionStateChanged != null) OnWebSocketConnectionStateChanged.run(this.isOpen());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
        if (OnWebSocketConnectionStateChanged != null) OnWebSocketConnectionStateChanged.run(this.isOpen());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);
        MessageHandler(message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }


    @FunctionalInterface
    public interface TimeTestServerLatencyActivateActuatorsReceived { void run(long nanoseconds); }
    @FunctionalInterface
    public interface TimeTestArduinoLatencyActivateActuatorsReceived { void run(long nanoseconds); }
    @FunctionalInterface
    public interface FlexorMovement { void run(int mapping, int value); }
    @FunctionalInterface
    public interface AccelerometerValues { void run(float ax, float ay, float az); }
    @FunctionalInterface
    public interface GyroscopeValues { void run(float gx, float gy, float gz); }
    @FunctionalInterface
    public interface MagnetometerValues { void run(float mx, float my, float mz); }
    @FunctionalInterface
    public interface AttitudeValues { void  run(float pitch, float roll, float yaw); }
    @FunctionalInterface
    public interface AllIMUValues { void run(float ax, float ay, float az, float gx, float gy, float gz, float mx, float my, float mz); }
    @FunctionalInterface
    public interface BluetoothDeviceConnectionState { void run(boolean isConnected); }
    @FunctionalInterface
    public interface WebSocketConnectionState { void run(boolean isConnected); }
    @FunctionalInterface
    public interface InfoMessage { void run(String message); }


    public void setOnTimeTestServerLatencyActivateActuatorsReceived(TimeTestServerLatencyActivateActuatorsReceived onTimeTestServerLatencyActivateActuatorsReceived) {
        OnTimeTestServerLatencyActivateActuatorsReceived = onTimeTestServerLatencyActivateActuatorsReceived;
    }

    public void setOnTimeTestArduinoLatencyActivateActuatorsReceived(TimeTestArduinoLatencyActivateActuatorsReceived onTimeTestArduinoLatencyActivateActuatorsReceived) {
        OnTimeTestArduinoLatencyActivateActuatorsReceived = onTimeTestArduinoLatencyActivateActuatorsReceived;
    }

    public void setOnFlexorValueReceived(FlexorMovement onFlexorValueReceived) {
        this.OnFlexorValueReceived = onFlexorValueReceived;
    }

    public void setOnAccelerometerValuesReceived(AccelerometerValues onAccelerometerValuesReceived) {
        OnAccelerometerValuesReceived = onAccelerometerValuesReceived;
    }

    public void setOnGyroscopeValuesReceived(GyroscopeValues onGyroscopeValuesReceived) {
        OnGyroscopeValuesReceived = onGyroscopeValuesReceived;
    }

    public void setOnMagnometerValuesReceived(MagnetometerValues onMagnometerValuesReceived) {
        OnMagnetometerValuesReceived = onMagnometerValuesReceived;
    }

    public void setOnAllIMUValuesReceived(AllIMUValues onAllIMUValuesReceived) {
        OnAllIMUValuesReceived = onAllIMUValuesReceived;
    }

    public void setOnBluetoothDeviceConnectionStateChanged(BluetoothDeviceConnectionState onBluetoothDeviceConnectionStateChanged) {
        OnBluetoothDeviceConnectionStateChanged = onBluetoothDeviceConnectionStateChanged;
    }

    public void setOnWebSocketConnectionStateChanged(WebSocketConnectionState onWebSocketConnectionStateChanged) {
        OnWebSocketConnectionStateChanged = onWebSocketConnectionStateChanged;
    }

    public void setOnInfoMessagesReceived(InfoMessage onInfoMessagesReceived) {
        OnInfoMessagesReceived = onInfoMessagesReceived;
    }

    private void MessageHandler(String message)
    {
        int mapping, value;
        float valueX, valueY, valueZ;
        String[] words;

        if (message != null)
        {
            words = message.split(",");
            try
            {
                switch (words[0])
                {
                    case "f":
                        mapping = Integer.parseInt(words[1]);
                        value = Integer.parseInt(words[2]);
                        if (OnFlexorValueReceived != null)
                            OnFlexorValueReceived.run(mapping, value);
                        break;
                    case "a":
                        valueX = Float.parseFloat(words[1]);
                        valueY = Float.parseFloat(words[2]);
                        valueZ = Float.parseFloat(words[3]);
                        if (OnAccelerometerValuesReceived != null )
                            OnAccelerometerValuesReceived.run(valueX, valueY, valueZ);
                        break;
                    case "g":
                        valueX = Float.parseFloat(words[1]);
                        valueY = Float.parseFloat(words[2]);
                        valueZ = Float.parseFloat(words[3]);
                        if (OnGyroscopeValuesReceived != null)
                            OnGyroscopeValuesReceived.run(valueX, valueY, valueZ);
                        break;
                    case "m":
                        valueX = Float.parseFloat(words[1]);
                        valueY = Float.parseFloat(words[2]);
                        valueZ = Float.parseFloat(words[3]);
                        if (OnMagnetometerValuesReceived != null)
                            OnMagnetometerValuesReceived.run(valueX, valueY, valueZ);
                        break;
                    case "z":
                         if (OnAllIMUValuesReceived != null)
                             OnAllIMUValuesReceived.run(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3]), Float.parseFloat(words[4]), Float.parseFloat(words[5]), Float.parseFloat(words[6]), Float.parseFloat(words[7]), Float.parseFloat(words[8]), Float.parseFloat(words[9]));
                        break;
                    case "b":
                        //Received "True" or "False" from WebSocketServer
                        if (OnBluetoothDeviceConnectionStateChanged != null)
                            OnBluetoothDeviceConnectionStateChanged.run(Boolean.parseBoolean(words[1].toLowerCase()));
                        break;
                    case "us":
                        if (OnTimeTestArduinoLatencyActivateActuatorsReceived != null)
                            OnTimeTestArduinoLatencyActivateActuatorsReceived.run(Long.parseLong(words[1]));
                        break;
                    case "ns":
                        if (OnTimeTestServerLatencyActivateActuatorsReceived!= null)
                            OnTimeTestServerLatencyActivateActuatorsReceived.run(Long.parseLong(words[1]));
                        break;

                    default:
                        if (OnInfoMessagesReceived != null)
                            OnInfoMessagesReceived.run(message);
                        break;
                }

            }
            catch (Exception e)
            {
                System.out.println("ERROR: BAD FORMAT on Communication.MessageHandler message: " + message);
            }
        }
    }


    public void StartOpenGlove(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.StartOpenGlove(bluetoothDeviceName));
    }

    public void StopOpenGlove(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.StopOpenGlove(bluetoothDeviceName));
    }

    public void AddOpenGloveDeviceToServer(String bluetoothDeviceName, String configurationName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.AddOpenGloveDeviceToServer(bluetoothDeviceName, configurationName));
    }

    public void RemoveOpenGloveDeviceFromServer(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.RemoveOpenGloveDeviceFromServer(bluetoothDeviceName));
    }
    public void SaveOpenGloveConfiguration(String bluetoothDeviceName, String configurationName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.SaveOpenGloveConfiguration(bluetoothDeviceName, configurationName));
    }

    public void ConnectToBluetoothDevice(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ConnectToBluetoothDevice(bluetoothDeviceName));
    }

    public void DisconnectFromBluetoothDevice(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.DisconnectFromBluetoothDevice(bluetoothDeviceName));
    }

    public void StartCaptureDataFromServer(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.StartCaptureDataFromServer(bluetoothDeviceName));
    }

    public void StopCaptureDataFromServer(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.StopCaptureDataFromServer(bluetoothDeviceName));
    }

    public void AddActuator(String bluetoothDeviceName, int region, int positivePin, int negativePin)
    {
        if (this.isOpen())
            this.send(MessageGenerator.AddActuator(bluetoothDeviceName, region, positivePin, negativePin));
    }

    public void AddActuators(String bluetoothDeviceName, List<Integer> regions, List<Integer> positivePins, List<Integer> negativePins)
    {
        System.out.println("AddActuators: " + MessageGenerator.AddActuators(bluetoothDeviceName, regions, positivePins, negativePins) + "isOpen: "+this.isOpen());
        if (this.isOpen())
            this.send(MessageGenerator.AddActuators(bluetoothDeviceName, regions, positivePins, negativePins));
    }

    public void RemoveActuator(String bluetoothDeviceName, int region)
    {
        if (this.isOpen())
            this.send(MessageGenerator.RemoveActuator(bluetoothDeviceName, region));
    }

    public void RemoveActuators(String bluetoothDeviceName, List<Integer> regions)
    {
        if (this.isOpen())
            this.send(MessageGenerator.RemoveActuators(bluetoothDeviceName, regions));
    }

    public void ActivateActuators(String bluetoothDeviceName, List<Integer> regions, List<String> intensities)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ActivateActuators(bluetoothDeviceName, regions, intensities));
    }

    public void ActivateActuatorsTimeTest(String bluetoothDeviceName, List<Integer> regions, List<String> intensities)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ActivateActuatorsTimeTest(bluetoothDeviceName, regions, intensities));
    }

    public void TurnOnActuators(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.TurnOnActuators(bluetoothDeviceName));
    }

    public void TurnOffActuators(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.TurnOffActuators(bluetoothDeviceName));
    }

    public void ResetActuators(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ResetActuators(bluetoothDeviceName));
    }

    public void AddFlexor(String bluetoothDeviceName, int region, int pin)
    {
        if (this.isOpen())
            this.send(MessageGenerator.AddFlexor(bluetoothDeviceName, region, pin));
    }

    public void AddFlexors(String bluetoothDeviceName, List<Integer> regions, List<Integer> pins)
    {
        System.out.println("AddFlexors: " + MessageGenerator.AddFlexors(bluetoothDeviceName, regions, pins) + "isOpen: "+this.isOpen());
        if (this.isOpen())
            this.send(MessageGenerator.AddFlexors(bluetoothDeviceName, regions, pins));
    }

    public void RemoveFlexor(String bluetoothDeviceName, int region)
    {
        if (this.isOpen())
            this.send(MessageGenerator.RemoveFlexor(bluetoothDeviceName, region));
    }

    public void RemoveFlexors(String bluetoothDeviceName, List<Integer> regions)
    {
        if (this.isOpen())
            this.send(MessageGenerator.RemoveFlexors(bluetoothDeviceName, regions));
    }

    public void CalibrateFlexors(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.CalibrateFlexors(bluetoothDeviceName));
    }

    public void ConfirmCalibration(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ConfirmCalibration(bluetoothDeviceName));
    }

    public void SetThreshold(String bluetoothDeviceName, int value)
    {
        if (this.isOpen())
            this.send(MessageGenerator.SetThreshold(bluetoothDeviceName, value));
    }

    public void TurnOnFlexors(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.TurnOnFlexors(bluetoothDeviceName));
    }

    public void TurnOffFlexors(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.TurnOffFlexors(bluetoothDeviceName));
    }

    public void ResetFlexors(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ResetFlexors(bluetoothDeviceName));
    }

    public void StartIMU(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.StartIMU(bluetoothDeviceName));
    }

    public void SetIMUStatus(String bluetoothDeviceName, boolean status)
    {
        System.out.println("SetIMUStatus: " + MessageGenerator.SetIMUStatus(bluetoothDeviceName, status) + "isOpen: "+this.isOpen());
        if (this.isOpen())
            this.send(MessageGenerator.SetIMUStatus(bluetoothDeviceName, status));
    }

    public void SetRawData(String bluetoothDeviceName, boolean status)
    {
        if (this.isOpen())
            this.send(MessageGenerator.SetRawData(bluetoothDeviceName, status));
    }

    public void SetIMUChoosingData(String bluetoothDeviceName, int value)
    {
        if (this.isOpen())
            this.send(MessageGenerator.SetIMUChoosingData(bluetoothDeviceName, value));
    }

    public void ReadOnlyAccelerometerFromIMU(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ReadOnlyAccelerometerFromIMU(bluetoothDeviceName));
    }

    public void ReadOnlyGyroscopeFromIMU(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ReadOnlyGyroscopeFromIMU(bluetoothDeviceName));
    }

    public void ReadOnlyMagnetometerFromIMU(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ReadOnlyMagnetometerFromIMU(bluetoothDeviceName));
    }

    public void ReadOnlyAttitudeFromIMU(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ReadOnlyAttitudeFromIMU(bluetoothDeviceName));
    }

    public void ReadAllDataFromIMU(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.ReadAllDataFromIMU(bluetoothDeviceName));
    }

    public void CalibrateIMU(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.CalibrateIMU(bluetoothDeviceName));
    }

    public void TurnOnIMU(String bluetoohDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.TurnOnIMU(bluetoohDeviceName));
    }

    public void TurnOffIMU(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.TurnOffIMU(bluetoothDeviceName));
    }

    public void SetLoopDelay(String bluetoothDeviceName, int value)
    {
        if (this.isOpen())
            this.send(MessageGenerator.SetLoopDelay(bluetoothDeviceName, value));
    }
    
    public void ConnectToWebSocketServer()
    {
        this.connect();
        if (OnWebSocketConnectionStateChanged != null) OnWebSocketConnectionStateChanged.run(this.isOpen());
    }

    public void DisconnectFromWebSocketServer()
    {
        this.close();
        if (OnWebSocketConnectionStateChanged != null) OnWebSocketConnectionStateChanged.run(this.isOpen());
    }

    public void GetOpenGloveArduinoVersionSoftware(String bluetoothDeviceName)
    {
        if (this.isOpen())
            this.send(MessageGenerator.GetOpenGloveArduinoVersionSoftware(bluetoothDeviceName));
    }
}
