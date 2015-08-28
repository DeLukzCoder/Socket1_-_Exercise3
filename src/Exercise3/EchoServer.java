/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exercise3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LukaszKrawczyk
 */
public class EchoServer implements Runnable{
    Socket s;
    BufferedReader in;
    PrintWriter out;
    String echo;
    public EchoServer(Socket socket){
        s = socket;
    }
    
    @Override
    public void run() {
        while (true) {            
            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                echo = in.readLine();
                out = new PrintWriter(s.getOutputStream(), true);
                
                if (comandChooser(echo).equalsIgnoreCase("Something went wrong")) {
                    out.println("Fuck");
                }else{
                    out.println(comandChooser(echo));
                }
            } catch (IOException ex) {
                Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String comandChooser(String echo){
        
        String[] splitter = echo.split("#");
        
        switch(splitter[0]){
            case "UPPER":
                return splitter[1].toUpperCase();
            case "LOWER":
                return splitter[1].toLowerCase();
            case "REVERSE":
                return reverse(splitter[1]);
            case "TRANSLATE":
                return translate(splitter[1]);
            default:
                return "Something went wrong";
        }
    }
    
    
    public String reverse(String text){
        String temp = "";
        for (int i = text.length() -1; i > -1; i--) {
            temp = temp + text.charAt(i);
        }
        return temp;
    }
    
    public String translate(String text){
        if (text.equalsIgnoreCase("hund")) {
            return "dog";
        }else{
            return "Something went wrong";
        }
    }
    
    public static void main(String[] args) throws IOException {
        String ip = "localhost";
        int port = 4321;
        if (args.length == 2) {
            System.out.println("Args Found");
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }
        
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip, port));
        while(true){
            EchoServer e = new EchoServer(ss.accept());
            Thread t1 = new Thread(e);
            t1.start();
        }
        
    }
    
}
