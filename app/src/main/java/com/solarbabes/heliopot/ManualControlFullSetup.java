package com.solarbabes.heliopot;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ManualControlFullSetup extends AppCompatActivity {
    String SERVER_IP = "192.168.43.89"; // IP for Waffle on Manav's.
//    String SERVER_IP = "192.168.105.184";
//    String SERVER_IP = "192.168.105.41";
    //String SERVER_PORT = "9800";
    int port = 9800;

//    int windowNumber = 1;

//    private Socket socket;
//    private ObjectOutputStream oos;
    InetAddress address;
    DatagramSocket udpSoc;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.manual_control_map_setup);

        Intent intent = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbarmovement);
        toolbar.setTitle("Map Setup");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            address = InetAddress.getByName(SERVER_IP);
            udpSoc = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ip = intent.getStringExtra("IPADDRESS");

        if (ip != null) {
            SERVER_IP = ip;
            try {
                address = InetAddress.getByName(SERVER_IP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final ImageButton forwardBtn = findViewById(R.id.button_forward);
        final ImageButton backBtn = findViewById(R.id.button_back);
        final ImageButton cwBtn = findViewById(R.id.button_cw);
        final ImageButton acwBtn = findViewById(R.id.button_acw);


        forwardBtn.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        onForwardClick(v);
                        forwardBtn.setImageResource(R.drawable.up_clicked);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        onStopClick(v);
                        forwardBtn.setImageResource(R.drawable.up);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        backBtn.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        onBackClick(v);
                        backBtn.setImageResource(R.drawable.down_clicked);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        onStopClick(v);
                        backBtn.setImageResource(R.drawable.down);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });


        cwBtn.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        onCWClick(v);
                        cwBtn.setImageResource(R.drawable.right_clicked);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        onStopClick(v);
                        cwBtn.setImageResource(R.drawable.right);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        acwBtn.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        onACWClick(v);
                        acwBtn.setImageResource(R.drawable.left_clicked);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        onStopClick(v);
                        acwBtn.setImageResource(R.drawable.left);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        //new Thread(new ClientThread()).start();

        // Socket is now open. Can send movement messages.
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Controls the toolbar back button behaviour.
        // Required so back bar and back button has the same animation.
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        udpSoc.close();

//        try {
//            //socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            oos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    // Button click event handlers.
    public void onForwardClick(View view) throws IOException {
        // A new thread is started to send the message.
        new Thread(new SendMSGThread("FORWARD")).start();
    }

    public void onBackClick(View view) throws IOException {
        new Thread(new SendMSGThread("BACK")).start();
    }

    public void onACWClick(View view) throws IOException {
        new Thread(new SendMSGThread("ACW")).start();
    }

    public void onCWClick(View view) throws IOException {
        new Thread(new SendMSGThread("CW")).start();
    }

    public void onStopClick(View view) throws IOException {
        new Thread(new SendMSGThread("STOP")).start();
    }

    public void onWindowClick(View view) throws IOException {
        new Thread(new SendMSGThread("LOCATION_WINDOW")).start();
    }

    public void onStationClick(View view) throws IOException {
        new Thread(new SendMSGThread("LOCATION_STATION")).start();
    }

    public void onFinishClick(View view) throws IOException {
        new Thread(new SendMSGThread("FINISH")).start();
        Intent intent = getIntent();
        String fromExtra = intent.getStringExtra("FROM");

        // Variables relevant based on the activity this was initiated from.
        if ((fromExtra != null) && (fromExtra.equals("MANUAL_CONTROL"))) {
            ManualControl.resumed = true;
        } else {
            AddPlant.mapCreated = true;
        }
        onBackPressed();
    }

//    class ClientThread implements Runnable {
//        // Thread for opening a socket with the manual control server (on the Turtlebot).
//
//        @Override
//        public void run() {
//            try {
//                socket = new Socket(SERVER_IP, Integer.parseInt(SERVER_PORT));
//            } catch (UnknownHostException e1) {
//                e1.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                oos = new ObjectOutputStream(socket.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    class SendMSGThread implements Runnable {
        // Thread for sending messages.
        // Constructed with the given message to be sent.

        private String message;

        public SendMSGThread(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                //oos.writeObject(message);
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                udpSoc.send(packet);
                System.out.println("send");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

