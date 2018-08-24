using Xamarin.Forms;
using System.Threading;
using CSharpTest.OpenGloveAPI_C_Sharp_HL;
using System;
using System.Diagnostics;
using System.Collections.Generic;

namespace C_SharpTest
{
    public partial class MainPage : ContentPage
    {
        public static string Url = "ws://127.0.0.1:7070";
        public OpenGlove leftHand = new OpenGlove("Left Hand", "OpenGloveIZQ", "leftHand", Url);
        public CancellationTokenSource cts = new CancellationTokenSource();
        public volatile int MessageReceivedCounter = 0;

        List<int> flexorRegions = new List<int> { 0, 5 };
        List<int> flexorPins = new List<int> { 17, 17 };
        List<int> actuatorRegions = new List<int> { 0, 1, 2, 3, 4 };
        List<int> actuatorPositivePins = new List<int> { 11, 10, 9, 3, 6 };
        List<int> actuatorNegativePins = new List<int> { 12, 15, 16, 2, 8 };

        public MainPage()
        {
            InitializeComponent();
            label_webSocketStatus.TextColor = Color.Red;
            label_bluetoothDeviceStatus.TextColor = Color.Red;
            label_opengloveStatus.TextColor = Color.Red;
            label_loadConfiguration.TextColor = Color.Red;
            label_actuator0.TextColor = Color.Red;
            label_actuator1.TextColor = Color.Red;
            label_actuator2.TextColor = Color.Red;
            label_actuator3.TextColor = Color.Red;
            label_actuator4.TextColor = Color.Red;

            UpdateOpenGloveInUI();
            leftHand.Communication.OnAllIMUValuesReceived += OnAllIMUValues;
            leftHand.Communication.OnFlexorValueReceived+= OnFlexorFunction;
            leftHand.Communication.OnInfoMessagesReceived += OnInfoMessage;
            leftHand.Communication.OnBluetoothDeviceStateChanged += OnBluetoothDeviceStateChanged;
        }

        public void OnBluetoothDeviceStateChanged(bool isConnected)
        {
            Device.BeginInvokeOnMainThread(() => {
                MessageReceivedCounter++;
                label_MessagesReceivedCounter.Text = MessageReceivedCounter.ToString();
                label_OnMessage.Text = "b,"+isConnected.ToString();
                label_bluetoothDeviceStatus.Text = isConnected ? "Connected" : "Disconnected";
                label_bluetoothDeviceStatus.TextColor = isConnected ? Color.Green : Color.Red;
            });
        }

        public void OnInfoMessage(string message)
        {
            Device.BeginInvokeOnMainThread(() => {
                label_OnMessage.Text = message;
                MessageReceivedCounter++;
                label_MessagesReceivedCounter.Text = MessageReceivedCounter.ToString();
            });
        }
        
        public void OnAllIMUValues(float ax, float ay, float az, float gx, float gy, float gz, float mx, float my, float mz)
        {
            Device.BeginInvokeOnMainThread(() => {
                label_OnIMU.Text =  String.Join(",", ax, ay, az, gx, gy, gz, mx, my, mz);
                MessageReceivedCounter++;
                label_MessagesReceivedCounter.Text = MessageReceivedCounter.ToString();
            });
        }

        public void OnFlexorFunction(int region, int value)
        {
            Device.BeginInvokeOnMainThread(() => {
                label_OnFlexor.Text = $"region: {region}, value: {value}";
                MessageReceivedCounter++;
                label_MessagesReceivedCounter.Text = MessageReceivedCounter.ToString();
            });
        }

        public void UpdateOpenGloveInUI()
        {
            label_Name.Text = leftHand.Name;
            label_BluetoothDeviceName.Text = leftHand.BluetoothDeviceName;
            label_ConfigurationName.Text = leftHand.ConfigurationName;
            label_WebSocketEndPoint.Text = leftHand.WebsocketEndpointUrl;
            label_MessagesReceivedCounter.Text = MessageReceivedCounter.ToString();
        }

        void Handle_Toggled_WebSocketConnection(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            UpdateOpenGloveInUI();
            if(e.Value)
            {
                leftHand.ConnectToWebSocketServer();
                label_webSocketStatus.Text = "Connected";
                label_webSocketStatus.TextColor = Color.Green;
            }
            else
            {
                leftHand.DisconnectFromWebSocketServer();
                label_webSocketStatus.Text = "Disconnected";
                label_webSocketStatus.TextColor = Color.Red;
            }
        }

        void Handle_Toggled_BluetoothDeviceConnection(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            UpdateOpenGloveInUI();
            if (e.Value)
            {
                leftHand.ConnectToBluetoothDevice();
                label_bluetoothDeviceStatus.Text = "Connecting";
                label_bluetoothDeviceStatus.TextColor = Color.Gray;
            }
            else
            {
                leftHand.DisconnectFromBluetoothDevice();
                label_bluetoothDeviceStatus.Text = "Disconnecting";
                label_bluetoothDeviceStatus.TextColor = Color.Gray;
            }
        }

        void Handle_Toggled_LoadConfiguration(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            UpdateOpenGloveInUI();
            if (e.Value)
            {
                leftHand.GetOpenGloveArduinoVersionSoftware();
                leftHand.AddActuators(actuatorRegions, actuatorPositivePins, actuatorNegativePins);
                leftHand.AddFlexors(flexorRegions, flexorPins);

                label_loadConfiguration.Text = "Loaded";
                label_loadConfiguration.TextColor = Color.Green;
            }
            else
            {
                leftHand.ResetActuators();
                leftHand.ResetFlexors();

                label_loadConfiguration.Text = "Reseted";
                label_loadConfiguration.TextColor = Color.Red;
            }
        }

        void Handle_Toggled_StartStopOpenGlove(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            UpdateOpenGloveInUI();
            if (e.Value)
            {
                leftHand.Start();
                label_opengloveStatus.Text = "Started";
                label_opengloveStatus.TextColor = Color.Green;
            }
            else
            {
                leftHand.Stop();
                label_opengloveStatus.Text = "Stoped";
                label_opengloveStatus.TextColor = Color.Red;
            }
        }

        void Handle_Toggled_ActivateActuator0(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            ActivateOrDeactivate(e.Value, 0, label_actuator0);
        }

        void Handle_Toggled_ActivateActuator1(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            ActivateOrDeactivate(e.Value, 1, label_actuator1);
        }

        void Handle_Toggled_ActivateActuator2(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            ActivateOrDeactivate(e.Value, 2, label_actuator2);
        }

        void Handle_Toggled_ActivateActuator3(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            ActivateOrDeactivate(e.Value, 3, label_actuator3);
        }

        void Handle_Toggled_ActivateActuator4(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            ActivateOrDeactivate(e.Value, 4, label_actuator4);
        }


        public void ActivateOrDeactivate(bool activateOrDeactivate, int actuator, Label label)
        {
            List<int> actuators = new List<int> { actuator };
            List<string> intensitiesOn = new List<string> { "255" };
            List<string> intensitiesOff =new List<string> { "0" };

            if (activateOrDeactivate)
            {
                leftHand.ActivateActuators(actuators, intensitiesOn);
                label.Text = "Activated";
                label.TextColor = Color.Green;
            }
            else
            {
                leftHand.ActivateActuators(actuators, intensitiesOff);
                label.Text = "Deactivated";
                label.TextColor = Color.Red;
            }
        }



    }
}
