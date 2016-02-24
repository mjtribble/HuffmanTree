/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HuffmanTree;

// tree.java
// demonstrates binary tree
// to run this program: C>java TreeApp
import java.io.*;
import java.util.*; // for Stack class
////////////////////////////////////////////////////////////////
class Node
{
	public int freq; // data item (key)
	public char letter; // data item
	public Node leftChild; // this node's left child
	public Node rightChild; // this node's right child
	
        public void displayNode() // display ourself
        {
		System.out.print('{');
		System.out.print(freq);
		System.out.print(", ");
		System.out.print(letter);
		System.out.print("} ");
	}
        
        public int getFreq(){
            return freq;
        }
        
        public char getLetter(){
            return letter;
        }
} // end class Node
////////////////////////////////////////////////////////////////