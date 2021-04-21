
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class EncoderGui extends JFrame {

    public static int position, response;
    public static String data, normaldata;

    public static void main(String[] args) {
        //Initilize the main window of Jframe
        JFrame frame = new JFrame("Encode the Message");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Initialize the Choosing Jlebel
        JLabel jLabel = new JLabel();


        //Initialize the ProgressBar
        JProgressBar progressBar = new JProgressBar(0, 2000);
        progressBar.setBounds(40, 40, 160, 30);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        //Initialize the Buttons
        JButton button1 = new JButton("Choose File");
        JButton writebutton = new JButton("Write the text");
        JButton button2 = new JButton("Save");

        //At first make the save button invissible
        button2.setVisible(false);

        //Initialize the JcomboBox for choosing the Encode or Decode Operation
        String ar[] = {"Encode", "Decode"};
        JComboBox box = new JComboBox(ar);

        //Getting the position index on which choise is selected at first
        position = box.getSelectedIndex();

        //adding a action listener to the combo Box to get the choosed item index
        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                progressBar.setValue(0);
                position = box.getSelectedIndex();
                if (position == 0) {
                    //if encoding is selected then the write button or text area to write will be shown
                    writebutton.setVisible(true);
                    jLabel.setText("<html><h3>Choose the txt file you want to Encode</h3></html>");
                } else if (position == 1) {
                    writebutton.setVisible(false);
                    jLabel.setText("<html><h3>Choose the txt file you want to Decode</h3></html>");
                }

            }
        });

        //Initialize the main lebel of the Gui
        JLabel lebel = new JLabel();
        lebel.setText("<html><h2>Choose if you want to Decode or encode the data</h2></html>");
        lebel.setHorizontalAlignment(JLabel.CENTER);

        JLabel lbl = new JLabel("\n");

        jLabel.setText("<html><h3>Choose the txt file you want to Encode</h3></br><h3> or Write the text to Encode</h3></html>");


        //Make a Jpanel for the buttons and progress Bar
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(196, 242, 10));

        //Here we Choose a TEXT file from the system to Encode
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                progressBar.setValue(0);
                //File Chooser to Choose the file from the system
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choose file");
                chooser.setFileFilter(new FileNameExtensionFilter("Text File", "txt"));//filter to show only text file
                response = chooser.showOpenDialog(null);
                //response store the value which define if the file has been choosed or not
                if (response == JFileChooser.APPROVE_OPTION) {
                    boolean status = true;
                    try {

                        if (position == 0) {
                            String msg = "<html><h3>" + chooser.getSelectedFile().getName() + " is Encoded </h3></html>";
                            //here We calling the encoding function to cncode the text file
                            status = encode(chooser.getSelectedFile().getAbsolutePath(), 0, jLabel, msg);
                        } else {
                            String dmsg="<html><h3>" + chooser.getSelectedFile().getName() + " is Decoded </h3></html>";
                            //Here we call the decode methode to decode the encoded file made by this application only
                            status = decode(chooser.getSelectedFile().getAbsolutePath(),jLabel,dmsg);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (status == true) {
                        //post work is to update the progress bar and make a little delay in thread
                        postwork(button2, progressBar);
                    }

                } else {

                }

            }
        });


        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //Here we call the methode to save the encoded or decoded file
                Savefile();
            }
        });
        //this button open another jframe to write on it to encode the written message
        writebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                progressBar.setValue(0);
                jLabel.setText("<html><h3>Choose the txt file you want to Encode</h3></br><h3> or Write the text to Encode</h3></html>");
                //Initialize new JFrame
                JFrame frame1 = new JFrame();
                frame1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                //Initialize new Jtext field to write on it
                JTextArea textField = new JTextArea();
                textField.setLineWrap(true);
                textField.setWrapStyleWord(true);
                textField.setColumns(30);
                textField.setRows(20);
                textField.setBorder(BorderFactory.createBevelBorder(1));
                textField.setFont(new Font("Arial", Font.PLAIN, 20));
                textField.setBackground(Color.white);

                //add a scroll pane to get a scroll bar to scroll the long text field
                JScrollPane scrollPane = new JScrollPane(textField);
                scrollPane.createVerticalScrollBar();
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                //Initialize a button to get the text and encode it
                JButton encodeBtn = new JButton("Encode the message");
                encodeBtn.setBorder(new RoundedBorder(10));
                encodeBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        normaldata = textField.getText();
                        String msg = "<html><h3>Your written text is Encoded</h3></html>";
                       if (normaldata.isEmpty()){
                           JOptionPane.showMessageDialog(null,"Please write something to Encode");
                       }
                       else{
                           boolean status = true;
                           frame1.dispose();
                           try {

                               status = encode("", 1, jLabel, msg);
                           } catch (IOException e) {
                               JOptionPane.showMessageDialog(null, e.getMessage());
                           }
                           if (status == true) {
                               //add delay and update progress bar
                               postwork(button2, progressBar);
                           }
                       }
                    }
                });


                frame1.add(scrollPane);
                frame1.add(encodeBtn);

                frame1.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
                frame1.getContentPane().setBackground(new Color(196, 242, 10));
                frame1.setSize(650, 610);
                frame1.setLocation(800, 150);
                frame1.setVisible(true);
            }
        });

        //Adding all the elements into the Jpanel
        panel.add(box);
        panel.add(Box.createRigidArea(new Dimension(100, 10)));
        panel.add(button1);
        panel.add(Box.createRigidArea(new Dimension(100, 10)));
        panel.add(writebutton);
        panel.add(Box.createRigidArea(new Dimension(100, 10)));
        panel.add(progressBar);
        panel.add(Box.createRigidArea(new Dimension(100, 10)));
        panel.add(button2);

        //adding all the elements including Jpanel to the Main Jframe
        frame.add(lebel);
        frame.add(lbl);
        frame.add(panel);
        frame.add(jLabel);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        frame.setSize(550, 400);
        frame.getContentPane().setBackground(new Color(196, 242, 10));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }


    public static boolean encode(String filepath, int finder, JLabel jLabel, String msg) throws IOException {
        //Here we use a variable call finder iin the methode encode to differentiate
        //if the data is from the text field or from Text file
        try {
            if (finder == 0) {
                File file = new File(filepath);
                if (file.getName().endsWith(".txt")) {

                    Scanner fc = new Scanner(file);
                    String is = "";
                    while (fc.hasNext()) {
                        is = is + fc.next() + " ";
                    }
                    //Encoding Logic
                    String out = "";
                    String[] ar = is.split(" ");
                    for (long i = 0; i < ar.length; i++) {
                        String check = ar[(int) i];
                        for (long j = 0; j < check.length(); j++) {
                            out = out + Integer.toBinaryString(check.charAt((int) j)) + " ";
                        }
                        out = out + "@";
                    }

                    data = out;
                    jLabel.setText(msg);
                    return true;

                } else {
                    JOptionPane.showMessageDialog(null, "Please select a Text file to Encode");
                    return false;
                }
            } else if (finder == 1) {

                String out = "";
                String[] ar = normaldata.split(" ");
                for (long i = 0; i < ar.length; i++) {
                    String check = ar[(int) i];
                    for (long j = 0; j < check.length(); j++) {
                        out = out + Integer.toBinaryString(check.charAt((int) j)) + " ";
                    }
                    out = out + "@";
                }

                data = out;
                jLabel.setText(msg);
                return true;

            } else {
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }


    }

    public static void Savefile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save The file");
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    return file.getName().endsWith(".txt");
                }
            }

            @Override
            public String getDescription() {
                return "Text File (.txt)";
            }
        });
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //it creates a file with extention .txt
            if (!file.getPath().toLowerCase().endsWith(".txt")) {
                file = new File(file.getPath() + ".txt");
            }
            try {
                FileWriter writer = new FileWriter(file.getPath());
                writer.write(data);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        }

    }

    private static boolean decode(String absolutePath,JLabel jLabel,String msg) {
        try {
            File file = new File(absolutePath);

            if (file.getName().endsWith(".txt")) {
                Scanner fc = null;

                fc = new Scanner(file);

                String is = "";
                while (fc.hasNext()) {
                    is = is + fc.next() + " ";
                }
                //Decoding logic
                String[] ar = is.split("@");
                String answer = "";
                for (int i = 0; i < ar.length; i++) {
                    String[] br = ar[i].split(" ");
                    for (int j = 0; j < br.length; j++) {
                        answer = answer + (char) Integer.parseInt(br[j], 2);
                    }
                    answer = answer + " ";

                }
                data = answer;
                jLabel.setText(msg);
                showDecodeData(answer);
                return true;

            } else {
                JOptionPane.showMessageDialog(null, "Please select a Text file to Encode");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exception occur while decoding \n"+e.toString());
            return false;
        }
    }

    private static void showDecodeData(String answer) {
        //Showing the decoded message into a another Jframe with text field
        JFrame frame1 = new JFrame();
        frame1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        JTextArea textField = new JTextArea();
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        textField.setColumns(30);
        textField.setRows(20);
        textField.setBorder(BorderFactory.createBevelBorder(1));
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setBackground(Color.white);
        textField.setEditable(false);
        textField.setText(answer);


        JScrollPane scrollPane = new JScrollPane(textField);
        scrollPane.createVerticalScrollBar();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JButton encodeBtn = new JButton("Close");
        encodeBtn.setBorder(new RoundedBorder(10));
        encodeBtn.addActionListener(actionEvent -> {
            frame1.dispose();
        });


        frame1.add(scrollPane);
        frame1.add(encodeBtn);

        frame1.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        frame1.getContentPane().setBackground(new Color(196, 242, 10));
        frame1.setSize(650, 610);
        frame1.setLocation(800, 150);
        frame1.setVisible(true);

    }

    public static void postwork(JButton button2, JProgressBar progressBar) {
        //Updating progressbar
        int i = 0;
        button2.setVisible(false);
        while (i <= 2000) {
            int val = progressBar.getValue();
            progressBar.setValue(val + 20);
            i = i + 20;
            try {
                //This trhe the code to delay the main thread
                Thread.sleep(70, 50);
            } catch (Exception e) {
            }

        }
        button2.setVisible(true);
    }

    static class RoundedBorder implements Border {
        //To round the corner of any button
        int radius;
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }
        public boolean isBorderOpaque() {
            return true;
        }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x,y,width-1,height-1,radius,radius);
        }
    }

}
