/*
public class PrimLists{}
* Runs the code
    * Throws IOException if file not found
    * S = 12 for a starting vertex of L

********************
class GraphLists{}
* Encapsulates most data structures and methods

    Step 1: Reads .txt file into program
    Step 2: adds vertices and edges into an adjacency list
    Step 3: print results

    Node structure for linked lists. Use these to store graph edges in the linked lists
    Node[] adj; an array of linked lists to store the graph in memory.

    ********************
    Methods
    ********************
    DF() - Depth First Graph traversal
    BF() - Breadth First Graph traversal
    MST_Prim()
    * Adds adj list into heap
        dist = new int[V + 1];      // to record the current distance of a vertex from the MST.
        parent = new int[V + 1];    // to store the MST
        hPos = new int[V +1];       // records the position of any vertex with the heap array a[ ]
        
        Heap h to find the next vertex nearest the MST so that it can be added to the MST.

********************
class Heap{}
*Heap structure in the form of an array
    ********************
    Methods
    ********************
    isEmpty() - Returns a boolean value
    siftUp(int) - moves vertex up the heap to the parent's location till vertex is no longer larger than parent
    siftDown(int) - pops vertex at the top, is replaced with smallest value
    insert(int) - attaches new value to the end of the heap, then runs siftUp()
    remove() - removes top of the heap, uses siftDown() then puts null node into empty location. returns value of popped vertex

********************
class Queue{}
*A simple linked list implementation of a Queue

    Node head, tail, z;

    ********************
    Methods
    ********************
    isEmpty() - Returns a boolean value
    enQueue(int) - Adds new node to end of the Queue
    deQueue() - Head is deQueued
    
*/

import java.io.*;
import java.util.Scanner;


class GraphLists {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }

    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    // mst[] NOTE: holds values of parent[] from the Prim algorithm for printing
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;

    // used for traversing graph
    private int[] visited;
    private int id;


    // default constructor
    public GraphLists(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        String splits = " +";  // multiple whitespace as delimiter
        String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        reader.close();

        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        // create sentinel node
        z = new Node(); 
        z.next = z;

        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;               

       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);

            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));    

            // write code to put edge into adjacency matrix 
            t = new Node(); t.vert = v; t.wgt = wgt; t.next = adj[u]; adj[u] = t;

            t = new Node(); t.vert = u; t.wgt = wgt; t.next = adj[v]; adj[v] = t;    
        }      
    }

    // converts vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    // method to display the graph representation
    public void display() {
        int v;
        Node n;

        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }

        
    /*
    Depth first traversal
    */
    // method to initialise Depth First Traversal of Graph
    public void DF(int s) {
        //Initialising array
        visited = new int[V + 1];
        for(int v = 1; v <= V; v++) { //for each v e V
            visited[v] = 0;
        }

        id = 0; //time = 0

        System.out.print("\nDepth First Graph Traversal\n");
        System.out.println("Starting with Vertex " + toChar(s));
        if(visited[s] == 0){
            // start visiting graph vertices using DF from starting vertex s.
            dfVisit( 0, s); 
        }

        System.out.print("\n\n");
    }

    // Recursive Depth First Traversal for adjacency list
    private void dfVisit(int prev, int v) {
        Node t;
        int u;
        
        visited[v] = ++id;  // u.colour = Gray
        System.out.print("\n  DF just visited vertex " + toChar(v) + " along " + toChar(prev) + "--" + toChar(v) );

        for(t = adj[v]; t != z; t = t.next){    //for each u e adj(v)
            //pull data from node
            u = t.vert;
            if( visited[u] == 0){
                dfVisit(v, u);
            }
        }
    }//end dfVisit()

    /*
    Breadth first traversal using Queue
    */
    public void BF(int s) {
        visited = new int[V + 1];
        for(int v = 1; v <= V; v++) { //for each v e V
            visited[v] = 0;
        }
        id = 0;
        int u, v;
        Node t;

        visited[s] = ++id;  // u.colour = Gray
        System.out.print("\n  DF just visited vertex " + toChar(s));

        Queue Q = new Queue();
        Q.enQueue(s);

        while(!Q.isEmpty()){
            v = Q.deQueue();
            for(t = adj[v]; t != z; t = t.next){//for each v e adj[u]

                u = t.vert;
                if( visited[u] == 0){
                    visited[u] = ++id;  // u.colour = Gray
                    System.out.print("\n  DF just visited vertex " + toChar(u));
                    Q.enQueue(u);
                }
            }//end for
        }//end while

    }//End BF()

    /*
    Heap implementation of the prim algorithm
    */
    public void MST_Prim(int s)
    {
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        //Initialising arrays
        dist = new int[V + 1];      //the distance from starting vertex
        parent = new int[V + 1];    //array to hold parent of curent vertex
        hPos = new int[V +1];       //heap Position !!!important

        for(v = 0; v <= V; ++v){    //for each v e V

            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }
        /**************/

        dist[s] = 0;    //distance 

        Heap h =  new Heap(V, dist, hPos); //Heap initially empty
        h.insert(s);           // s will be the root of the MST

        while (! h.isEmpty())  //should repeat |V| -1 times
        {
            // most of alg here
            v = h.remove();    //add v to the MST
            wgt_sum += dist[v]; //add the wgt of v to sum

            dist[v] = -dist[v]; //mark v as now in the MST

            /****************/

            // Node t
            for(t = adj[v]; t != z; t = t.next){    //for each u e adj(v)
                //pull data from node
                u = t.vert;
                wgt = t.wgt;

                if(wgt < dist[u]){ //weight less than current value

                    dist[u] = wgt;
                    parent[u] = v;

                    if(hPos[u] == 0) {      // not in heap insert
                        h.insert(u);
                    } else {
                        h.siftUp(hPos[u]); //if already in heap siftup the modified heap node
                    }
                }
            }//end for

        }//end while()
        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");

        mst = parent;                           
    }//end MST_Prim()

    //Prints out MST
    public void showMST()
    {
        System.out.print("\n\nMinimum Spanning tree parent array is:\n");
        for(int v = 1; v <= V; ++v)
            System.out.println(toChar(v) + " -> " + toChar(mst[v]));
        System.out.println("");
    }

}

//Driver Code
public class PrimLists {
    public static void main(String[] args) throws IOException
    {
        Scanner sc = new Scanner(System. in );
        String fname;
        int s;
        int mode = 0;   //switch-case variable
        /* TESTER CODE
        fname = "wGraph1.txt";   
        s = 12;     //starting vertex L = 12
        */

        System.out.println("\n\nWelcome to the Graph traversal and Prim's Algorithm Program\n");
        System.out.print("\nPlease enter the Filename:  ");
        fname = sc.nextLine();
        
        System.out.print("\nPlease enter the starting vertex:  ");
        s = sc.nextInt();
        
        System.out.print("\nPlease choose the method:\n1 - Depth First Graph Traversal\n2 - Breadth First Graph Traversal\n3 - Prim's Algorithm\n\nInput:  ");
        mode = sc.nextInt();
        sc.close(); // Closing Scanner after use
        
        GraphLists g = new GraphLists(fname);
        g.display();
        switch (mode){
            case 1:{
                g.DF(s);
                break;
            }
            case 2:{
                g.BF(s);
                break;
            }
            case 3:{
                g.MST_Prim(s);
                g.showMST();
                break;
            }
            default: System.out.println("\nInvalid input");
        }//end switch

        //g.DF(s);

        //g.BF(s);

        //g.MST_Prim(s);
        //g.showMST();

    }

}

/*

Heap Code for efficient implementation of Prim's Alg

*/

class Heap
{
    private int[] a;       // heap array
    private int[] hPos;    // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v
    private int N;         // heap size

    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = a[k];

        a[0] = 0;
        dist[0] = Integer.MIN_VALUE;

        // while distance of last vertex in heap smaller than parent's
        while(dist[v] < dist[a[k/2]]){
            a[k] = a[k/2];  // parent moves up the tree to the position of the child
            hPos[a[k]] = k; // heap position hPos[] modified

            k = k/2;        // index changed to parent's
        }

        a[k] = v;           // resting index added to heap
        hPos[v] = k;        // resting index added to hPos[]

    }

    /*
    poping the vertex at the top of the heap
    replaces top with the smallest value in heap
    resizes and resorts heap
    */
    public void siftDown( int k) 
    {
        int v, j;
        v = a[k];  

        while(k <= N/2){
            j = 2 * k;
            // if the left side of the tree is larger, increment j
            if(j < N && dist[a[j]] > dist[a[j + 1]]) ++j;

            //if size of parent vertex is less than its child, stop.
            if(dist[v] <= dist[a[j]]) break;

            a[k] = a[j];    // if parent is greater than child, assign parent's position
            hPos[a[k]] = k; // update new pos of last vertex on tree

            k = j;          // update position
        }
        a[k] = v;           // resting index added to heap
        hPos[v] = k;        // resting index added to hPos[]
    }


    public void insert( int x) 
    {
        a[++N] = x;         // attaches new vertex to the end of the heap
        siftUp(N);          // passes same index for siftup
    }


    public int remove() 
    {   
        int v = a[1];   
        hPos[v] = 0;    // v is no longer in heap
        
        a[1] = a[N--];  // last node of heap moved to top
        siftDown(1); // pass index at top to siftdown
        
        a[N+1] = 0;     // put null node into empty spot

        return v;       // return vertex at top of heap
    }
    
}//End Heap IMPLEMENTATION


/*

Queue Code for implementation of BF

*/
class Queue{
    class Node {
        public int vert;
        public Node next;
    }
    public Queue() {
        z = new Node(); // the sentinel
        z.next = z; // points to itself
        head = z; // head is pointing to the sentinel
        tail = null;
    }

    /*****************
     * QUEUE IMPLEMENTATION*/
    Node head, tail, z;

    //checks if queue is empty - head reaching the sentinel
    public boolean isEmpty() {
        return head == head.next;
    }

    public void enQueue(int num) {
        Node newN = new Node();
        newN.vert = num;
        newN.next = z; // new node is initialised to point at sentinel

        if (head == z) // case of empty list
        {
            head = newN;
        } else // case of list not empty
        {
            tail.next = newN;
        }

        tail = newN; // new node is now at the tail
    }

    //head points to the next node, deleting it
    public int deQueue() {
        int output = head.vert;
        head = head.next;
        return output;
    }

    /*
    //End QUEUE IMPLEMENTATION
    ******************/
}
