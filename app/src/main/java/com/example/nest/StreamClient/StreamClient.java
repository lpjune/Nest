package com.example.nest.StreamClient;

import android.util.Log;

import com.example.nest.TCPClient.TcpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class StreamClient {
    // server computer's IPV4 Address
    private static String SERVER_IP;
    private static int SERVER_PORT;

    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private StreamClient.OnMessageReceived mMessageListener;
    // while this is true, the server will continue running
    public boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public StreamClient(StreamClient.OnMessageReceived listener) {
        mMessageListener = listener;
    }

    public static void getSERVER_IP(String ip) {
        StreamClient.SERVER_IP = ip;
    }

    public static void getSERVER_PORT(String port) {
        StreamClient.SERVER_PORT = Integer.parseInt(port);
    }


    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;

    }

    public void run() {

        mRun = false;

        try {
            // gets IP
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);

            Log.e("TCP Client", "C: Connecting...");

            // create a socket to make the connection with the server
            Socket socket = new Socket(serverAddress, SERVER_PORT);
            try {

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                mRun = true;
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array();
                    mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {
                mRun = false;
                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {
            mRun = false;
            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asyncTask doInBackground
    public interface OnMessageReceived {
        void messageReceived(String message);
    }
}
