package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket s1=new ServerSocket(3000);
        while(true){
            Socket s= s1.accept();
            try{

            BufferedReader in =new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out= new DataOutputStream(s.getOutputStream());
            String firstLine=in.readLine();
            System.out.println(firstLine);
//leggo la linea di richiesta http

            String[] request=firstLine.split(" ");
            String method =request[0]; //metodo
            String resource=request[1]; //url
            String version=request[2]; //versione protocollo

           //leggo intestazione della richiesta
            String header;
            do{
             header =in.readLine();
             System.out.println(header);
            }while(!header.isEmpty());

            //faccio i controlli e rispondo 
            if(resource.equals("/")){
                resource="index.html";
             }

            File file =new File("htdocs/"+resource);


            if(file.exists()){
               
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Length: "+file.length()+"\n");
                out.writeBytes("Content-Type:"+getContentType(file)+"\n");
                out.writeBytes("\n");
              
                InputStream input=new FileInputStream(file);
                byte[] buf= new byte[8192];
                int n;
                while((n=input.read(buf))!= -1){
                    out.write(buf,0,n);
                }
                input.close();

            } else{
                String msg="File non trovato";
                out.writeBytes("HTTP/1.1 404 Not Found\n");
                out.writeBytes("Content-Lenght: "+msg.length()+"\n");
                out.writeBytes("Content-Type: text/plain\n");
                out.writeBytes("\n");
                out.writeBytes(msg);

            }
            s.close();
            
            

        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    
}
//gestione vari casi di estensione
private static String getContentType(File f){
    String[] s=f.getName().split("\\.");
    String ext = s[s.length-1];
    switch(ext){
        case "html":
        return "text/html";

        case "png" :
        return "image/png";

        case "jpg" :
        case "jpeg" :
        return "image/jpeg";

        case "css":
        return "text/css";

        case "js":
        return "application/javascript";

        default:
            return "";
    }
}
}





