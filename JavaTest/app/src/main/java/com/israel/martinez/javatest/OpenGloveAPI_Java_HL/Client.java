package com.israel.martinez.javatest.OpenGloveAPI_Java_HL;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

/**
 * Created by israel-martinez on 05-09-18.
 */

public class Client extends WebSocketClient {

    public String bluetoothDeviceName;
    public String configurationName;
    public MessageGenerator messageGenerator;

    public Client(MessageGenerator messageGenerator, String bluetoothDeviceName, String configurationName, URI serverUri) {
        super(serverUri);
        this.bluetoothDeviceName = bluetoothDeviceName;
        this.configurationName = configurationName;
        this.messageGenerator = new MessageGenerator(";",",","");
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Hello, it is me. Mario :)");
        System.out.println("new connection opened");
        this.send(messageGenerator.AddOpenGloveDeviceToServer(bluetoothDeviceName, configurationName));
        this.send(messageGenerator.StartCaptureDataFromServer(bluetoothDeviceName));
        System.out.println("OpenGlove.Communication.AddOpenGloveToServer()");
        System.out.println("Communication.StartCaptureDataFromServer()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}
