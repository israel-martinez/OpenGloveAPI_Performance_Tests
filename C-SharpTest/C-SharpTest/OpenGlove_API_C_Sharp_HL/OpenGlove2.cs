using System;
using System.Collections.Generic;

namespace CSharpTest.OpenGlove_API_C_Sharp_HL
{
    public class OpenGlove2
    {
        public string Name { get; set; }
        public string BluetoothDeviceName { get { return this.BluetoothDeviceName; } set { this.BluetoothDeviceName = value; this.Communication.MessageGenerator.BluetoothDeviceName = value; } }
        public string ConfigurationName { get { return this.ConfigurationName; } set { this.ConfigurationName = value; this.Communication.MessageGenerator.ConfigurationName = value; } }
        public Communication2 Communication { get; set; }
        private bool _IsConnectedToWebSocketServer { get { return IsWebSocketConnected(); } set {_IsConnectedToWebSocketServer = value; } }
        private bool _IsConnectedToBluetoohDevice { get; set; }
        public bool IsConnectedToWebSocketServer { get { return _IsConnectedToWebSocketServer; } }
        public bool IsConnectedToBluetoohDevice { get { return _IsConnectedToBluetoohDevice; } }
        // TODO Need add data listener (flexors and IMU data ) EventHandlers ... for get subscribers in OnMessage of WebSocket ...

        public OpenGlove2(string name, string bluetoothDeviceName, string configurationName, string url)
        {
            this.Name = name;
            this.BluetoothDeviceName = bluetoothDeviceName;
            this.ConfigurationName = configurationName;
            this.Communication = new Communication2(bluetoothDeviceName, configurationName, url);
            this._IsConnectedToWebSocketServer = false;
            this._IsConnectedToBluetoohDevice = false;
        }

        //Modify for diferent validation of IsConnected to websocket server in other lenguages of programing and libraries
        private bool IsWebSocketConnected()
        {
            //if (this.Communication.WebSocket.State == WebSocket4Net.WebSocketState.Open)
            if(this._IsConnectedToWebSocketServer)
                return true;
            else
                return false;
        }


        public void Start()
        {
            this.Communication.StartOpenGlove();
        }

        public void Stop()
        {
            this.Communication.StopOpenGlove();
        }

        public void AssignOpenGloveConfiguration(string configurationName)
        {
            this.Communication.AssignOpenGloveConfiguration(configurationName);
        }

        public void InitializeOpenGloveConfigurationOnDevice()
        {
            this.Communication.InitializeOpenGloveConfigurationOnDevice();
        }

        public void AddOpenGloveDevice()
        {
            this.Communication.AddOpenGloveDevice();
        }

        public void RemoveOpenGloveDevice()
        {
            this.Communication.AddOpenGloveDevice();
        }

        public void SaveOpenGloveConfiguration()
        {
            this.Communication.MessageGenerator.SaveOpenGloveConfiguration();
        }

        public void ConnectToBluetoothDevice()
        {
            this.Communication.MessageGenerator.ConnectToBluetoothDevice();
            this._IsConnectedToBluetoohDevice = true; //TODO improve this with validation with message from server
        }

        public void DisconnectFromBluetoothDevice()
        {
            this.Communication.MessageGenerator.DisconnectFromBluetoothDevice();
            this._IsConnectedToBluetoohDevice = false; //TODO improve this with validation with message from server
        }

        public void StartCaptureDataFromServer()
        {
            this.Communication.MessageGenerator.StartCaptureDataFromServer();
        }

        public void StopCaptureDataFromServer()
        {
            this.Communication.MessageGenerator.StopCaptureDataFromServer();
        }

        public void AddActuator(int region, int positivePin, int negativePin)
        {
            this.Communication.AddActuator(region, positivePin, negativePin);
        }

        public void AddActuators(List<int> regions, List<int> positivePins, List<int> negativePins)
        {
            this.Communication.AddActuators(regions, positivePins, negativePins);
        }

        public void RemoveActuator(int region)
        {
            this.Communication.RemoveActuator(region);
        }

        public void RemoveActuators(List<int> regions)
        {
            this.Communication.RemoveActuators(regions);
        }

        public void ActivateActuators(List<int> regions, List<string> intensities)
        {
            this.Communication.ActivateActuators(regions, intensities);
        }

        public void TurnOnActuators()
        {
            this.Communication.TurnOnActuators();
        }

        public void TurnOffActuators()
        {
            this.Communication.TurnOffFlexors();
        }

        public void TurnOnFlexors()
        {
            this.Communication.TurnOnFlexors();
        }

        public void TurnOffFlexors()
        {
            this.Communication.TurnOffFlexors();
        }

        public void ResetActuators()
        {
            this.Communication.ResetActuators();
        }

        public void AddFlexor(int region, int pin)
        {
            this.Communication.AddFlexor(region, pin);
        }

        public void AddFlexors(List<int> regions, List<int> pins)
        {
            this.Communication.AddFlexors(regions, pins);
        }

        public void RemoveFlexor(int region)
        {
            this.Communication.RemoveFlexor(region);
        }

        public void RemoveFlexors(List<int> regions)
        {
            this.Communication.RemoveFlexors(regions);
        }

        public void CalibrateFlexors()
        {
            this.Communication.CalibrateFlexors();
        }

        public void ConfirmCalibration()
        {
            this.Communication.ConfirmCalibration();
        }

        public void SetThreshold(int value)
        {
            this.Communication.SetThreshold(value);
        }

        public void ResetFlexors()
        {
            this.Communication.ResetFlexors();   
        }

        public void StartIMU()
        {
            this.Communication.StartIMU();
        }

        public void SetIMUStatus(bool status)
        {
            this.Communication.SetIMUStatus(status);
        }

        public void SetRawData(bool status)
        {
            this.Communication.SetRawData(status); 
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
            this.Communication.SetIMUChoosingData(value);
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

        public void CalibrateIMU()
        {
            throw new NotImplementedException();
            // Need Implement this on OpenGlove Aplication, see SwitchOpenGloveServer in OpenGloveServer class
            // Communication and MessageGenerator methods is OK
            //this.Communication.CalibrateIMU();
        }

        public void SetLoopDelay(int value)
        {
            this.Communication.SetLoopDelay(value);
        }

        public void ConnectToWebSocketServer()
        {
            this.Communication.ConnectToWebSocketServer();
        }

        public void DisconnectFromWebSocketServer()
        {
            this.Communication.DisconnectFromWebSocketServer();
        }
    }

    /*
     * Create your custom Regions for other types of Bluetooth wereables 
     * how helmets, general joints capture and actuators zones.
     * 
     * Palmar, Dorsal and Flexors Regions is for hands Mappings.
     * Enjoy!
    */

    public enum PalmarRegion
    {
        FingerSmallDistal,
        FingerRingDistal,
        FingerMiddleDistal,
        FingerIndexDistal,

        FingerSmallMiddle,
        FingerRingMiddle,
        FingerMiddleMiddle,
        FingerIndexMiddle,

        FingerSmallProximal,
        FingerRingProximal,
        FingerMiddleProximal,
        FingerIndexProximal,

        PalmSmallDistal,
        PalmRingDistal,
        PalmMiddleDistal,
        PalmIndexDistal,

        PalmSmallProximal,
        PalmRingProximal,
        PalmMiddleProximal,
        PalmIndexProximal,

        HypoThenarSmall,
        HypoThenarRing,
        ThenarMiddle,
        ThenarIndex,

        FingerThumbProximal,
        FingerThumbDistal,

        HypoThenarDistal,
        Thenar,

        HypoThenarProximal
    }

    public enum DorsalRegion
    {
        FingerSmallDistal = 29,
        FingerRingDistal,
        FingerMiddleDistal,
        FingerIndexDistal,

        FingerSmallMiddle,
        FingerRingMiddle,
        FingerMiddleMiddle,
        FingerIndexMiddle,

        FingerSmallProximal,
        FingerRingProximal,
        FingerMiddleProximal,
        FingerIndexProximal,

        PalmSmallDistal,
        PalmRingDistal,
        PalmMiddleDistal,
        PalmIndexDistal,

        PalmSmallProximal,
        PalmRingProximal,
        PalmMiddleProximal,
        PalmIndexProximal,

        HypoThenarSmall,
        HypoThenarRing,
        ThenarMiddle,
        ThenarIndex,

        FingerThumbProximal,
        FingerThumbDistal,

        HypoThenarDistal,
        Thenar,

        HypoThenarProximal
    }

    public enum FlexorsRegion
    {
        ThumbInterphalangealJoint = 0,
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
}
