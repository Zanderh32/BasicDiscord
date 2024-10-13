import java.net.*;
import java.io.*;
import java.util.*;
import java.net.ServerSocket;

public class MultiServer{
    
    private static Set<ClientData> clients;
    
    public static void main(String[] args) throws Exception{
        System.out.println("SERVER MAIN");
        clients = new LinkedHashSet<ClientData>();
        Thread AcceptThread = new Thread(new AcceptThread());
        AcceptThread.start();
        Thread TalkThread = new Thread(new TalkThread());
        TalkThread.start();
        while(true){}
    }
    
    public static void brodcast(String message){
        for(ClientData c: clients){
            c.getPW().println(message);
        }
    }
    public static String getUsers(){
        String users = "[USERLIST] ";
        for(ClientData c: clients){
            users+=c.getUsername() + " ";
            
        }
        return users;
    }

    private static class AcceptThread implements Runnable{
        public void run(){
            try{
                String myIP = InetAddress.getLocalHost().getHostAddress();
                int myPort = 4500;
                ServerSocket serverSocket = new ServerSocket(myPort);
                System.out.println("HOSTING SERVER ON " + myIP + ":"+myPort);
                while(serverSocket.isClosed() == false){
                    System.out.println("WAITING FOR SOMEONE TO CONNECT...");
                    Socket socket = serverSocket.accept(); //blocks
                    InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    System.out.println("...SOMEONE CONNECTED TO THE SERVER!");
                    String username = br.readLine();
                    clients.add(new ClientData(socket, username));
                    brodcast(getUsers());
                    //Thread talkThread = new Thread(new TalkThread(socket));
                    Thread listenThread = new Thread(new ListenThread(socket, username));
                    
                    //talkThread.start();
                    listenThread.start();
                }
            }
            catch(Exception e ){
                e.printStackTrace();
            }
        }
    }

    private static class TalkThread implements Runnable {
        public void run(){
            try{
                System.out.println("Talk Thread Started!");
                Scanner scan = new Scanner(System.in);
                while(true){
                    String message = scan.nextLine(); // blocks
                    brodcast("[SERVER]: "+message);
                }
            }
            catch(Exception e){ e.printStackTrace();}
        }
    }

    private static class ListenThread implements Runnable {
        private String username;
        private Socket socket;
        public ListenThread(Socket socket, String username) {
            this.socket = socket;
            this.username = username;
        }

        public void run(){
            try{
                System.out.println("Listen Thread Started!");
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                while(socket.isClosed() == false){
                    String message = br.readLine(); // blocks
                    System.out.println("["+username+"]: "+message);
                    brodcast("["+username+"]: "+message);
                }
            }
            catch(Exception e){ 
                for(ClientData c: clients){
                    if(c.getSocket().equals(socket)){
                        clients.remove(c);
                        brodcast(getUsers());
                    }
                }
            }

        }
    }

    private static class ClientData{
        private Socket socket;
        private String username;
        private BufferedReader br;
        private PrintWriter pw;
        public ClientData(Socket socket, String username){
            try{
                this.socket = socket;
                this.username = username;
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                this.br = new BufferedReader(isr);
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                this.pw = new PrintWriter(osw, true);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        public Socket getSocket(){return this.socket;}
        public String getUsername(){return this.username;}
        public BufferedReader getBR(){return this.br;}
        public PrintWriter getPW(){return this.pw;}
    }
}

