/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HuffmanTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author melodytribble
 */
class Huffman {

    String s, inStr, outStr = "";
    String str = "";
    String code = "";//holds binary code for the entire message
    String deCode = "";
    String output = "";
    char[] array; //array to hold the letters of the message as a char value
    int[] freqTable = new int[29];//an array to hold the frequency of each letter in the message as an int
    String[] codeTable = new String[29];//an array to hold the binary code for each letter in the message as a String of 0's and 1's
    boolean flag = true;
    PriorityQ myQ;
    Tree huffTree;

    /*
     This runs the user input menu
     */
    public void runProgram() throws IOException {//runs I/O switch cases
        while (flag) {
            System.out.print("Enter first letter of Enter, Show, Code, Decode or Quit:");
            int choice = getChar(0);
            switch (choice) {
                //This shows the Huffman tree
                case 'S':
                    huffTree.displayTree();
                    break;
                //This enter's text, and creates frequency table 
                case 'E':
                    s = createString("Enter text lines, terminate with $: ");
                    createFreqTable(s); //creates frequency table
                    printFreqTable();   //prints frequency table
                    queueTree();        //inserts in to priority Q
                    makeHuffTree();     //creates huffman tree
                    break;
                //This converts message to binary and prints code
                case 'C':
                    encode();
                    break;
                //This decompresses binary to text and prints the message
                case 'D':
                    decode(huffTree.getRoot());
                    break;
                //This exits the program
                case 'Q':
                    flag = false;
                    break;
                //This give user option to reenter choice
                default:
                    System.out.print("Invalid entry\n");
            } // end switch
        } // end while

    }
    // -------------------------------------------------------------
    /*
     This reads in the message and converts characters to uppercase
     @return the message
     */

    public String getString() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        output += s;
        s = s.toUpperCase();

        return s;
    }
    // -------------------------------------------------------------
    /*
     This converts user input string to a char for the switch statement
     @parameter position of char
     @returns character of users choice
     */
    public char getChar(int i) throws IOException {
        String s = getString();
        return s.charAt(i);
    }
    //-------------------------------------------------------------
    /*
     This reads in a string from the user's input
     @parameter Instructions for user
     @return User input as a capitolized string of characters
     */
    public String createString(String input) throws IOException {

        while (true) {
            //calls the buffered reader and converts to capitols
            inStr = getString();

            //cuts the string at the $ char
            if (inStr.charAt(inStr.length() - 1) == '$') {
                outStr += inStr.substring(0, inStr.length() - 1);
                
                return outStr;
            } else//if it doesn't find the $ char on current line, reads in the next line
            {
                outStr += inStr + "\n";
            }
        }
    }
    //-------------------------------------------------------------
    /*
     This creates a table of the frequency of each character
     @parameter user's input
     */
    public void createFreqTable(String str) throws IOException {
        //initializes each position in array to 0 frequency
        for (int i = 0; i < 29; i++) {
            freqTable[i] = 0;
        }

        int i = 0;
        array = str.toCharArray();//converts the string to an array of chars in order 

        for (int j = 0; j < array.length; j++) {
            if (array[i] == ' ')//converts spaces to the char[ inserts into array
            {
                array[i] = '[';
            } else if (array[i] == '\n')//converts new lines to the char \ and inserts in to array
            {
                array[i] = '\\';
            }
            int value = array[i] - 65;//converts the acsii value of each character to an appropriate array location int
            freqTable[value] += 1;//A=0, B=1...
            i++;
        }
    }
    //-------------------------------------------------------------
    /*
     This prints the frequency table
     */

    public void printFreqTable() {

        String output = "";

        for (int i = 0; i < array.length; i++) {
            output += array[i]; //
        }
        output += "\n";

        for (int j = 65; j <= 93; j++)//adds characters A-Z... to output
        {
            output += (char) j + " ";
        }
        output += "\n";

        for (int k = 0; k < freqTable.length; k++) {
            output += freqTable[k] + " ";
        }
        System.out.println(output+"\n\n");
    }
    //-------------------------------------------------------------
    /*
     This creates a priority queue from the frequency table
     */

    public void queueTree() {
        myQ = new PriorityQ(freqTable.length);//creates priority queue

        for (int i = 0; i < freqTable.length; i++) {//runs through the frequency table

            if (freqTable[i] != 0) {            //checks each character to see if it occurs in the text message

                Tree newItem = new Tree();  //creates a tree containing the character and it's frequency
                newItem.insert(freqTable[i], (char) (i + 65));//creates a new root with frequeny and character references                               
                myQ.insert(newItem);        //inserts the tree into the priority Q
            }
        }
    }
    //-------------------------------------------------------------
    /*
     This creates a huffman tree from the priority queue
     */

    public void makeHuffTree() {

        while (myQ.size() > 1) {

            //creates three nodes one for the new parent and 2 to hold the dequeued trees
            Tree parent = new Tree();

            //dequeues the two least frequency letters and assigns them to a placeholder
            Tree child1 = myQ.remove();
            Tree child2 = myQ.remove();
            myQ.print();

            //inserts the parent Node into a new tree, and adds the two dequeued trees as it's children
            parent.insert(child1.getFreq() + child2.getFreq(), '+');//creates a new tree parent
            parent.getRoot().leftChild = child1.getRoot();
            parent.getRoot().rightChild = child2.getRoot();

            //inserts new parent tree back into priority Q
            myQ.insert(parent);
        }
        huffTree = myQ.remove();
    }
    //-------------------------------------------------------------
    /*
     This converts message into binary using huff tree and code table
     */

    public void encode() {
        createCodeTable(huffTree.getRoot(), str);   //creates a code table
        createCodedMsg();      //converts char message to binary String
        printCodeTable();
        printCodedMsg();
    }
    //-------------------------------------------------------------
    /*
     This decodes a binary message using the code table and converts it into a text message
     @parameter starting node of the tree
     */
    public void decode(Node currentNode) 
    {
        for (int i = 0; i <= code.length(); i++) {
            //we have reached a leaf node, sets current back to the root
            if (currentNode.getLetter() != '+') 
            {
                deCode += currentNode.getLetter();             
                currentNode = huffTree.getRoot();
            }
            if(i!=code.length())//This is not the last digit in the code
            {            
                if (code.charAt(i) == '0') 
                {
                    currentNode = currentNode.leftChild;
                } else//the next number is 1 traverse to the right
                {
                    currentNode = currentNode.rightChild;
                }
            }
        }
        System.out.println("Decoded message:\n" + deCode+"\n\n");
    }
    //-------------------------------------------------------------
    /*
     This converts text message into binary using Huffman Tree and inserts binary into a code table
     @parameter current root, current output
     */
    public void createCodeTable(Node localRoot, String output) {
        if (localRoot != null) {
            //if we are not at a leaf node,it will keep traversing tree recursively 
            if (localRoot.rightChild != null && localRoot.leftChild != null && localRoot.getLetter() == '+') {
                createCodeTable(localRoot.leftChild, output + "0");//adds 0 for each left move until moving left is null
                createCodeTable(localRoot.rightChild, output + "1");//adds 1 for a right move and returns until at a leaf node
            } else//we are at a leaf node, adds the current binary output to proper spot in the code table
            {
                codeTable[(int) (localRoot.getLetter()) - 65] = output;
            }
        }
    }
    //-------------------------------------------------------------
    /*
     *prints the code table values from A-Z
     */
    public void printCodeTable() {

        for (int i = 0; i < codeTable.length; i++) {
            if (codeTable[i] != null)//only prints characters in the message
            {
                System.out.print("\n" + (char) (i + 65) + " " + codeTable[i]);
            }
        }
    }
    /*
     This uses the code table and message array to provide binary code for each letter
     */
    public void createCodedMsg() {
        for (int i = 0; i < array.length; i++) {
            code += codeTable[(int) array[i] - 65];
        }
    }
    /*
     This prints the codes message
     */
    public void printCodedMsg() {
        System.out.println("\nCoded message:\n" + code+"\n\n");
    }
}
