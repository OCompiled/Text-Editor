/*
* Author: Olujimi Adetula
* Purpose: A program that reads, display, edits, and writes to a file, while allowing the search of specific words.
* Version: 1.3
*/
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Reader extends JFrame {

    //Class variable
    private int width =1150; //Window width/height
    private int height =600;
    
    //GUI variables
    private JFrame mainFrame; //Mother frame that holds everything
    //This is the Raw world text area, and seach/sort result Text area
    private JTextArea readTextArea,mainTextArea, searchResultsTextArea;
    private JScrollPane mainScrollPane,searchScrollPane, readScrollPane;
    private JPanel mainPanel, optionPanel,worldPanel;
    private JButton readBtn,searchBtn,updateBtn;
    private JLabel searchLbl,updateLbl,readLbl;
    private JTextField searchTextField; //The search box
    private JFileChooser fileChooser; 
    
    //Default Consturctor
    public Reader(){
        super("File Reader");
        this.width= 1150;
        this.height=600;
    }
    //valued Constructor
    public Reader(String title, int width, int height){
        super(title);
        this.width =1150;
        this.height=600;
        
    }
    
    private void createGUI() {
        
        //Setting up the layout
        this.mainPanel = new JPanel( new BorderLayout());
        this.worldPanel = new JPanel(new GridLayout(1,3,5,5));
        this.optionPanel = new JPanel(new GridLayout(1,10,5,5));
        
        //Creating main TextArea
        mainTextArea = new JTextArea();
        mainTextArea.setEditable(true); //Do not allow user to edit the file
        mainTextArea.setFont(new Font("Monospaced", 0,12)); //Setting font, might remove this
        mainTextArea.setLineWrap(true); //continue text on next line
        mainScrollPane = new JScrollPane(mainTextArea); //Make the TextArea Scrollable
        

        
        //Creating the Search result area
        searchResultsTextArea = new JTextArea();
        searchResultsTextArea.setEditable(false);
        searchResultsTextArea.setLineWrap(true);
        searchScrollPane = new JScrollPane(searchResultsTextArea);
        
        //Creating Read TextArea 
        readTextArea = new JTextArea();
        readTextArea.setEditable(false);
        readTextArea.setLineWrap(true);
        readScrollPane = new JScrollPane(readTextArea);
        
        //Creating Button
        readBtn = new JButton("Read");
        searchBtn = new JButton("Search");
        updateBtn = new JButton("Update");
        
        
        //Creating searchBox and Labels
        searchTextField = new JTextField("", 10);
        searchLbl = new JLabel ("Search:", JLabel.RIGHT); //To label the search area
        updateLbl=new JLabel("Update File:", JLabel.RIGHT);
        readLbl= new JLabel("Read File: ", JLabel.RIGHT);
        
        
        //Adding Components to the optionPanel
        optionPanel.add(readLbl);
        optionPanel.add(readBtn);
        optionPanel.add(updateLbl);
        optionPanel.add(updateBtn);
        optionPanel.add(searchLbl);
        optionPanel.add(searchTextField);
        optionPanel.add(searchBtn);
        
        //Adding Component to the worldPanel
        worldPanel.add(readScrollPane);
        worldPanel.add(mainScrollPane);
        worldPanel.add(searchScrollPane);
        
        //Adding component to main panel
        mainPanel.add(worldPanel, BorderLayout.CENTER);
        mainPanel.add(optionPanel, BorderLayout.NORTH);
        
        //Adding borders and title to Panels
        optionPanel.setBorder(BorderFactory.createTitledBorder("Tools"));
        readScrollPane.setBorder(BorderFactory.createTitledBorder("File View Area"));
        mainScrollPane.setBorder(BorderFactory.createTitledBorder("Update Area"));
        searchScrollPane.setBorder(BorderFactory.createTitledBorder("Search Result"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        //Setting up actions for the buttons
        readBtn.addActionListener((ActionEvent ee)->{ //For the Read Button
            readFile();
        });
        searchBtn.addActionListener((ActionEvent e)->{
           searchFile(); 
        });
        updateBtn.addActionListener((ActionEvent e)->{
            updateFile();
        });
        
        //Setting up JFrame
        mainFrame = new JFrame ("File Reader");
        mainFrame.setContentPane(mainPanel);
        mainFrame.setSize(width,height);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /*
    * Read Files after the Read Button has been pressed.
    * Returns void
    */
    private void readFile(){
        
        //Variable declaration
        int selection;
        readTextArea.setText("");
        mainTextArea.setText("");
        
        //Creating File Chooser object
        fileChooser = new JFileChooser();
        
        
        //Initalizing selection variable
        selection = this.fileChooser.showOpenDialog(new JFrame()); //Opening up selection window for file
        
        //If an approved option was selected and 0 was returned
        if(selection == JFileChooser.APPROVE_OPTION){
            try{
                //Setting what should be Scanner for input
                File files =fileChooser.getSelectedFile();
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(files)));
                readTextArea.read(input,"");
                mainTextArea.setText(readTextArea.getText());
            } catch (FileNotFoundException ex){
                System.out.println("Error: File nto found"); //Change this to a popup window
            }catch (IOException e){
            }
        }
    }
    /*
    * Search File after the Search Button has been pressed.
    * Return void
    */
    private void searchFile(){
        
        //Setting up Scanner, cearing the TextArea
        searchResultsTextArea.setText("");
        Scanner sc;
        sc = new Scanner(readTextArea.getText());
           
        //Holds all the occurance of the word found.
        ArrayList<String> searched = new ArrayList<>();
        
        //Scanning file for entered word
        while(sc.hasNextLine()){
            String line = sc.next().toLowerCase();
        
            int index=line.indexOf(searchTextField.getText());
            
            while(index>=0){
                if(line.contains(searchTextField.getText())){
                    searched.add(line);
            }
                else
                    System.out.println("FAILED");
                index--;
            }
        }
        sc.close();
        //parse thru each result and append it to the Textarea
       searched.forEach((String m) -> { searchResultsTextArea.append(m+"\n");});
    }
    
    /*
    * Update File after the Sort Button has been pressed
    * Return void
    */
    private void updateFile(){
        
      Scanner s;
        try {

            FileWriter fw = new FileWriter(fileChooser.getSelectedFile());
          try (BufferedWriter out = new BufferedWriter(fw)) {
              s = new Scanner(mainTextArea.getText());
              while(s.hasNext()){
                  String m = s.nextLine();
                  if(m.contains(".")){
                      out.write("\n");
                  }else if(m.contains("|")){
                      out.write("\t\t");
                  }
                  out.write(m);
              }
              
              out.flush();
          }
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
     readTextArea.setText(mainTextArea.getText());
    }
    
    /*
    * main method
    */
    public static void main(String[] args){
        Reader window = new Reader();
        window.createGUI();
    }
}
