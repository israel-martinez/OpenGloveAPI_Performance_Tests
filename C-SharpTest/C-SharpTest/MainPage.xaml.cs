using Xamarin.Forms;
using System.Threading;
using CSharpTest.OpenGloveAPI_C_Sharp_HL;

namespace C_SharpTest
{
    public partial class MainPage : ContentPage
    {
        public static string Url = "ws://127.0.0.1:7070";
        public OpenGlove leftHand = new OpenGlove("Left Hand", "OpenGloveIZQ", "HandConfig", Url);
        //public WebSocket WebSocket = new WebSocket(Url);
        public CancellationTokenSource cts = new CancellationTokenSource();

        public MainPage()
        {
            InitializeComponent();
            label_webSocketStatus.TextColor = Color.Red;
            label_bluetoothDeviceStatus.TextColor = Color.Red;
            label_opengloveStatus.TextColor = Color.Red;
        }

        void Handle_Toggled_WebSocketConnection(object sender, Xamarin.Forms.ToggledEventArgs e)
        {
            DisplayAlert("info", $"Url: {leftHand.WebsocketEndpointUrl}", "OK");
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
