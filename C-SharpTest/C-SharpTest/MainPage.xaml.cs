using Xamarin.Forms;
using System.Threading;
using CSharpTest.OpenGloveAPI_C_Sharp_HL;
using System;
using System.Diagnostics;

namespace C_SharpTest
{
    public partial class MainPage : ContentPage
    {
        public static string Url = "ws://127.0.0.1:7070";
        public OpenGlove leftHand = new OpenGlove("Left Hand", "OpenGloveIZQ", "HandConfig", Url);
        public CancellationTokenSource cts = new CancellationTokenSource();
        public volatile int MessageReceivedCounter = 0;

        public MainPage()
        {
            InitializeComponent();
            label_webSocketStatus.TextColor = Color.Red;
            label_bluetoothDeviceStatus.TextColor = Color.Red;
            label_opengloveStatus.TextColor = Color.Red;
            UpdateOpenGloveInUI();
            leftHand.Communication.AllIMUValuesFunction += OnAllIMUValues;
        }

        public void OnAllIMUValues(float ax, float ay, float az, float gx, float gy, float gz, float mx, float my, float mz)
        {
            Debug.WriteLine(String.Join(",", ax, ay, az, gx, gy, gz, mx, my, mz));
            Device.BeginInvokeOnMainThread(() => {
                label_OnMessage.Text =  String.Join(",", ax, ay, az, gx, gy, gz, mx, my, mz);
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
                label_bluetoothDeviceStatus.Text = "Connected";
                label_bluetoothDeviceStatus.TextColor = Color.Green;
            }
            else
            {
                leftHand.DisconnectFromBluetoothDevice();
                label_bluetoothDeviceStatus.Text = "Disconnected";
                label_bluetoothDeviceStatus.TextColor = Color.Red;
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

    }
}
