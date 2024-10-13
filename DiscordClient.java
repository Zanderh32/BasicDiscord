import java.net.*;
import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class DiscordClient implements ActionListener{

    private static Socket socket;
    private static JTextArea displayArea, userlistArea;
    private static JTextField messageField;

    public static void main(String[] args) throws Exception{
        System.out.println("CLIENT MAIN METHOD");
        String myIP = InetAddress.getLocalHost().getHostAddress();
        int myPort = 50000;
        Scanner scan = new Scanner(System.in);
        System.out.println("ENTER IP TO CONNECT TO: ");
        String theirIP = scan.nextLine();
        System.out.println("ENTER PORT TO CONNECT TO: ");
        int theirPort = scan.nextInt(); scan.nextLine();
        System.out.println("TYPE YOUR USERNAME HERE:");
        String username = scan.nextLine();
        System.out.println("TRYING TO CONNECT TO THE SERVER");
        socket = new Socket(theirIP, theirPort);
        System.out.println("\n ...SUCCESSFULLY JOINED THE SERVER!");
        new DiscordClient();
        sendMessage(username);

        Thread talkThread = new Thread(new TalkThread());
        Thread listenThread = new Thread(new ListenThread());

        talkThread.start();
        listenThread.start();


        while(true){
        }
    }

    public DiscordClient(){
        // CREATE FRAME AND PANEL
        JFrame frame = new JFrame("Chat Application");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //panel.setBackground(Color.BLACK);
        // CREATE COMPONETS
        int hsb = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
        int vsb = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
        Font font = new Font("Times New Roman", Font.PLAIN, 40);

        displayArea = new JTextArea();
        displayArea.setFont(font);
        JScrollPane displayPane = new JScrollPane(displayArea, vsb, hsb);
        displayPane.setPreferredSize(new Dimension(750,700));
        
        userlistArea = new JTextArea();
        userlistArea.setFont(font);
        JScrollPane userlistPane = new JScrollPane(userlistArea, vsb, hsb);
        userlistPane.setPreferredSize(new Dimension(250,700));

        messageField = new JTextField("");
        messageField.setFont(font);
        messageField.setPreferredSize(new Dimension(1000, 100));

        // ADD COMPNENTS TO PANEL

        panel.add(displayPane, BorderLayout.CENTER);
        panel.add(userlistPane, BorderLayout.WEST);
        panel.add(messageField, BorderLayout.SOUTH);

        // FINAL FRAME SETTINGS
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);

        // MESSAGE FIELD ACTIONS

        messageField.addActionListener(this);
        messageField.grabFocus();
        messageField.requestFocus();

    }
    
    

    public void actionPerformed(ActionEvent e){
        try{
            String message = messageField.getText();
            messageField.setText("");
            sendMessage(message);
        } 
        catch(Exception err){
            err.printStackTrace();
        }
    }

    public static void sendMessage(String message){
        try{
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
            PrintWriter pw = new PrintWriter(osw, true);
            pw.println(message);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private static class TalkThread implements Runnable {

        public void run(){
            try{
                System.out.println("Talk Thread Started!");
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                PrintWriter pw = new PrintWriter(osw, true);
                Scanner scan = new Scanner(System.in);
                while(socket.isClosed() == false){
                    String message = scan.nextLine(); // blocks
                    pw.println(message);
                }
            }
            catch(Exception e){ e.printStackTrace();}
        }
    }

    private static class ListenThread implements Runnable {

        public void run(){
            try{
                System.out.println("Listen Thread Started!");
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                while(socket.isClosed() == false){
                    String message = br.readLine(); // blocks
                    
                    
                    if(message.startsWith("[USERLIST]")) { 
                        userlistArea.setText(""); 
                        int lastI = 0;
                        for(int i = 0; i < message.length(); i++){
                            if(message.charAt(i) == ' '){
                                userlistArea.append(message.substring(lastI, i) + "\n");
                                lastI = i+1;
                            }
                        }
                    } 
                    else { 
                        System.out.println(message);
                        displayArea.append(message + "\n");
                    }
                }
            }
            catch(Exception e){ e.printStackTrace();}

        }
    }
}
