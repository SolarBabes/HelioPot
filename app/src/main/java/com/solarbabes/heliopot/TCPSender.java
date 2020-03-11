package com.solarbabes.heliopot;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPSender extends AsyncTask<String, Void, Void> {

    String IP = "192.168.5.1";
    int port = 8765;
    Socket s;
    DataOutputStream dos;
    PrintWriter pw;



    protected Void doInBackground(String... voids) {

        String message = voids[0];

        try {
            s = new Socket(IP, port);
            pw= new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();
            pw.close();
            s.close();

        }catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
