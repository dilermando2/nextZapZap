/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nextzapzap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Marc
 */
public class NextZapzapServer extends Thread {

    private static ArrayList<BufferedWriter> clientes;
    private static ServerSocket server;
    private String usuario;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    public NextZapzapServer(Socket con) {
        this.con = con;
        try {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {
            String msg;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            clientes.add(bfw);
            usuario = msg = bfr.readLine();

            while (msg != null) {
                msg = bfr.readLine();
                sendToAll(bfw, msg);
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
        BufferedWriter bwS;

        for (BufferedWriter bw : clientes) {
            bwS = (BufferedWriter) bw;
            if (!(bwSaida == bwS)) {
                bw.write(usuario + ": " + msg + "\r\n");
                bw.flush();
            }
        }
    }

    public static void main(String[] args) {

        try {
            //Cria os objetos necessário para instânciar o servidor
            server = new ServerSocket(12124);
            clientes = new ArrayList<BufferedWriter>();

            while (true) {
                System.out.println("Aguardando conexão na porta 12124");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new NextZapzapServer(con);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
