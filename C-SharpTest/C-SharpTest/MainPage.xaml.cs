using Xamarin.Forms;
using CSharpTest.OpenGloveAPI_C_Sharp_HL;
using System;
using System.Collections.Generic;
using CSharpTest.OpenGloveLatencyTests;
using System.Diagnostics;

namespace C_SharpTest
{
    public partial class MainPage : ContentPage
    {
        public static string Url = "ws://127.0.0.1:7070";
        public OpenGlove leftHand = new OpenGlove("Left Hand", "OpenGloveIZQ", "leftHand", Url);
        public volatile int MessageReceivedCounter = 0;

        List<int> flexorRegions = new List<int> { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        List<int> flexorPins = new List<int> { 17, 17, 17, 17, 17, 17, 17, 17, 17, 17 }; //for simulate more flexors
        List<int> actuatorRegions = new List<int> { 0, 1, 2, 3, 4 };
        List<int> actuatorPositivePins = new List<int> { 11, 10, 9, 3, 6 };
        List<int> actuatorNegativePins = new List<int> { 12, 15, 16, 2, 8 };

        public LatencyTest latencyTest;
        public volatile int actuatorStepCounter = 0;

        List<int> samplesQuantityList = new List<int> { 100, 1000, 2000, 5000, 10000 };
        List<string> componentTypeList = new List<string> { "actuators", "flexors&IMU"}; //future supported test { "actuators", "flexors", "flexors&IMU", "actuators&flexor&IMU"};
        List<int> componentQuantityList = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

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
            leftHand.Communication.OnBluetoothDeviceConnectionStateChanged += OnBluetoothDeviceConnectionStateChanged;
            leftHand.Communication.OnWebSocketConnectionStateChangued += OnWebSocketConnectionStateChangued;

            // For latency Tests
            picker_SamplesQuantity.ItemsSource = samplesQuantityList;
            picker_ComponentType.ItemsSource = componentTypeList;
            picker_ComponentQuantity.ItemsSource = componentQuantityList;
        }

        public void OnWebSocketConnectionStateChangued(bool isConnected)
        {
            Device.BeginInvokeOnMainThread(() => {
                label_webSocketStatus.Text = isConnected ? "Connected" : "Disconnected";
                label_webSocketStatus.TextColor = isConnected ? Color.Green : Color.Red;
            });
        }

        public void OnBluetoothDeviceConnectionStateChanged(bool isConnected)
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
                label_webSocketStatus.Text = "Conecting";
                label_webSocketStatus.TextColor = Color.Gray;
            }
            else
            {
                leftHand.DisconnectFromWebSocketServer();
                label_webSocketStatus.Text = "Disconnecting";
                label_webSocketStatus.TextColor = Color.Gray;
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

        public void OnLatencyTestCompleted(string testType, string folderName, string fileName, string storagePath, string columnTitle, int samples, int writingCicleCounter, int readingCicleCounter, int messageReceivedCounter)
        {
            Device.BeginInvokeOnMainThread(() => {
                switch (testType)
                {
                    case "flexors&IMU":
                        leftHand.Stop();
                        latencyTest.RemoveFlexorsAndIMUEvents();
                        DisplayAlert("Test Completed", $"The latency test of {samples} samples for test [{testType}]. \n - readingCicleCounter: {readingCicleCounter}\n writingCicleCounter: {writingCicleCounter} \n - The Test is storage in {storagePath}", "Ok");
                        break;
                    case "actuators":
                        actuatorStepCounter++;
                        Debug.WriteLine($"actuatorStepCounter: {actuatorStepCounter}");
                        if (actuatorStepCounter == 3)
                        {
                            latencyTest.GenerateCSVFileForActuatorsLatencyTest();
                        }
                        if (actuatorStepCounter == 4)
                        {
                            actuatorStepCounter = 0;
                            latencyTest.RemoveActuatorsEvents();
                            leftHand.Stop();
                            DisplayAlert("Test Completed", $"The latency test of {samples} samples for test [{testType}]. \n - readingCicleCounter: {readingCicleCounter}\n writingCicleCounter: {writingCicleCounter} \n - The Test is storage in {storagePath}", "Ok");
                        }
                        break;
                    default:
                        break;
                }
            });
        }

        void Handle_Toggled_LoadLatencyTestConfiguration(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            if(e.Value)
            {
                int samplesQuantity = (int)picker_SamplesQuantity.SelectedItem;
                string componentType = (string)picker_ComponentType.SelectedItem;
                int componentQuantity = (int)picker_ComponentQuantity.SelectedItem;

                leftHand.SetLoopDelay(0);
                leftHand.SetThreshold(0);
                if(componentType.Equals("flexors&IMU"))
                {
                    leftHand.AddFlexors(flexorRegions.GetRange(0, componentQuantity), flexorPins.GetRange(0, componentQuantity));
                    leftHand.SetIMUStatus(true);
                }
                if (componentType.Equals("actuators"))
                {
                    leftHand.AddActuators(actuatorRegions.GetRange(0, componentQuantity), actuatorPositivePins.GetRange(0, componentQuantity), actuatorNegativePins.GetRange(0, componentQuantity));
                }
                leftHand.SaveOpenGloveConfiguration(); //Register OpenGlove Configuration on Server
                    
            }
            else
            {
                leftHand.ResetFlexors();
                leftHand.ResetActuators();
                leftHand.TurnOffIMU();
            }

        }

        async void Handle_Toggled_StartResetLatencyTest(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            if(e.Value)
            {
                bool startTest = await DisplayAlert("Confirm Start Test", $"You are sure start latency test for get {picker_SamplesQuantity.SelectedItem} samples with:\n Component(s): {picker_ComponentType.SelectedItem} \n Quantity Component(s): {picker_ComponentQuantity.SelectedItem}", "Ok", "Cancel");
                if (startTest)
                {
                    latencyTest = new LatencyTest();
                    int samplesQuantity = (int)picker_SamplesQuantity.SelectedItem;
                    string componentType = (string)picker_ComponentType.SelectedItem;
                    int componentQuantity = (int)picker_ComponentQuantity.SelectedItem;
                    string folderName = "latencyTests_CSharpAPI";
                    string fileName = componentType + componentQuantity + "Xamarin" + "Galaxy";
                    string fileExtension = ".csv";
                    string columnTitle = "latencies-ns"; //nanoseconds

                    if (componentType.Equals("flexors&IMU"))
                    {
                        latencyTest.OnLatencyTestCompleted += OnLatencyTestCompleted;
                        latencyTest.FlexorAndIMUTest(this.leftHand, folderName, fileName + fileExtension, columnTitle, samplesQuantity, componentQuantity);
                        leftHand.Start(); //load configuration on bluetoothDevice
                    }
                    if (componentType.Equals("actuators"))
                    {
                        latencyTest.OnLatencyTestCompleted += OnLatencyTestCompleted;
                        leftHand.Start();
                        latencyTest.ActuatorsTest2(this.leftHand, folderName, fileName + fileExtension, columnTitle, samplesQuantity, componentQuantity, actuatorRegions.GetRange(0, componentQuantity));
                    }
                }
                else
                {
                    switch_startResetLatencyTest.IsToggled = false;

                }
            }
            else
            {
                latencyTest.OnLatencyTestCompleted -= OnLatencyTestCompleted;
                actuatorStepCounter = 0;
                leftHand.Stop();
            }
        }


        void Handle_Toggled_LoadConfiguration(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            UpdateOpenGloveInUI();
            if (e.Value)
            {
                leftHand.GetOpenGloveArduinoVersionSoftware();
                leftHand.AddActuators(actuatorRegions, actuatorPositivePins.GetRange(0,5), actuatorNegativePins.GetRange(0,5));
                leftHand.AddFlexors(flexorRegions.GetRange(0,2), flexorPins.GetRange(0,2));
                leftHand.SetThreshold(0);

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
