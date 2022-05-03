/*
Kruskal's Minimum Spanning Tree Algorithm

public class KruskalTrees{}
* Runs the code
    * Throws IOException if file not found

********************
class GraphLists{}
* Encapsulates data structures and methods

    Step 1: Reads .txt file into program
    Step 2: adds edges into an array of Edges
    Step 3: prints results

    Edge[] edge; an array of edges to store the graph in memory.
    It will be read into the Heap array and then sorted by weight

    ********************
    Methods
    ********************
    MST_Kruskal()
    * Adds Edges into heap
        Heap h to find the next edge with the lowest weight and add to the MST.

********************
class Heap{}
* Heap structure in the form of an array
    Initialises heap array of Edges and arranges them by weight
    ********************
    Methods
    ********************
    siftDown(int) - pops vertex at the top, is replaced with smallest value
    remove() - removes top of the heap, uses siftDown() then puts null node into empty location. returns value of popped vertex


class UnionFindSets{}
* UnionFind partition to support union-find operations
* Implementations of Union By Rank adn path compression

    Step 1: Intialises treeparent and rank arrays
    Step 2: assigns them values

    ********************
    Methods
    ********************
    findSet(int) - finds and returns root using path compression
    union(int, int) - UNUSED! Unifies two edges
    unionByRank(int, int) - ranks verices and sets the parent array accordingly
    showTrees() - Shows trees
    showSets() - Shows all of the existing sets
    showSet() - Shows a single set

*/


import java.io.*;
import java.util.Scanner;
 
class Edge {
    // starting vertex, ending vertex, weight of edge
    public int u, v, wgt;

    public Edge() {
        u = 0;
        v = 0;
        wgt = 0;
    }

    // inserts value into edges
    public Edge( int x, int y, int w) {
        this.u = x;
        this.v = y;
        this.wgt = w;
    }
    
    // Prints out edge
    public void show() {
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n") ;
    }
    
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}// END Edge()

/*

Heap code for sorting edges by weight

*/
class Heap
{
	private int[] a;    //heap array
    int N, Nmax;        //heap size
    Edge[] edge;        //edge[v].wgt = priority of v


    // Bottom up heap constructor
    //      1. maximum heap size
    //      2. reference to the array of edges
    public Heap(int _N, Edge[] _edge) {
        int i;
        Nmax = N = _N;
        a = new int[N+1];
        edge = _edge;
       
        // initially just fill heap array with 
        // indexes of edge[] array.
        for (i=0; i <= N; ++i) 
            a[i] = i;
           
        // Then convert h[] into a heap
        // from the bottom up.
        for(i = N/2; i > 0; --i)
            siftDown(a[i]);// missing line;
    }

    /*
    poping the vertex at the top of the heap
    replaces top with the smallest value in heap
    resizes and resorts heap
    */
    private void siftDown( int k) {
        int e, j;

        e = a[k];
        while( k <= N/2) {
            j = 2 * k;
            // if the left side of the tree is larger, incerent j
            if (j < N && edge[a[j]].wgt > edge[a[j+1]].wgt) j++;

            //if weight of parent vertex is less that its child, stop.
            if (edge[e].wgt <= edge[a[j]].wgt) break;

            a[k] = a[j];    // if parent is greater than child, assign parent's position

            k = j;          // update position
        }
        a[k] = e;           // resting index added to heap
        
    }

    public int remove() {
        a[0] = a[1];        // top of heap moved to position 0
        a[1] = a[N--];      // last node of heap moved to top
        siftDown(1);     // pass index at top to siftdown
        return a[0];        // returns edge at top of heap
    }
}// END Heap()

/****************************************************
*
*       UnionFind partition to support union-find operations
*       Implemented simply using Discrete Set Trees
*
*****************************************************/

class UnionFindSets
{
    private int[] treeParent;
    private int[] rank;
    private int N;
    
    public UnionFindSets( int V)
    {
        
        N = V;                      // amount of vertices
        treeParent = new int[V+1];  // location of the parent vertex
        rank = new int[V+1];        // rank of the vertex

        for(int i = 0; i < V; i++) { 
            treeParent[i] = i;  //vertexes are in seperate sets
            rank[i] = 0;        // array of rank values initialised to 0
        }
    }

    /*
    Recursive implementation
    If the parent of the vertex is not the root, 
    find root and make into parent
    utilises path compression
    */
    public int findSet( int vertex)
    {   
        if(treeParent[vertex] != vertex){
            treeParent[vertex] = findSet(treeParent[vertex]);
        }
        return treeParent[vertex];
    }
    
    // Unifies two edges. Parent of tree will be the source vertex of the edge
    public void union( int set1, int set2)
    {
        for(int i = 0; i < N; i++){
            if(treeParent[i] == set2){
                treeParent[i] = set1;
            }
        }
    }

    //Ranks vertices and sets the parent array accordingly
    public void unionByRank(int set1, int set2){
        int u = findSet(set1);  // parent of the source
        int v = findSet(set2);  // parent of the destination

        // if rank is smaller, attach to higher rank
        // else make one of them as the root and increment their rank
        if (rank[u] < rank[v]){
            treeParent[u] = v;
        } else if (rank[u] > rank[v]){
            treeParent[v] = u;
        } else {
            treeParent[v] = u;
            rank[u]++;
        }
    }
    
    //Shows trees
    public void showTrees()
    {
        int i;
        for(i=1; i<=N; ++i)
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  " );
        System.out.print("\n");
    }
    
    //Shows sets
    public void showSets()
    {
        int u, root;
        int[] shown = new int[N+1];
        for (u=1; u<=N; ++u)
        {   
            root = findSet(u);  // find the root
            if(shown[root] != 1) {
                showSet(root);
                shown[root] = 1;
            }            
        }   
        System.out.print("\n");
    }

    private void showSet(int root)
    {
        int v;
        System.out.print("Set{");
        for(v=1; v<=N; ++v)
            if(findSet(v) == root)
                System.out.print(toChar(v) + " ");
        System.out.print("}  ");
    
    }
    
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}

class Graph 
{ 
    // V = number of vertices
    // E = number of edges
    // mst = Minimum Spanning Tree
    private int V, E;
    private Edge[] edge;
    private Edge[] mst;        

    // constructor
    public Graph(String graphFile) throws IOException
    {
        int u, v;
        int w, e;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create edge array
        edge = new Edge[E+1];   
        
        // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            w = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + w + ")--" + toChar(v));                         
            
            edge[e] = new Edge(u, v, w); // create Edge object  
        }
        reader.close();
    }


    /**********************************************************
    *
    *       Kruskal's minimum spanning tree algorithm
    *
    **********************************************************/
    public Edge[] MST_Kruskal() 
    {
        int i = 0;
        Edge e;
        int uSet, vSet; //set1 and set2
        UnionFindSets partition;
        
        // create edge array to store MST
        // Initially it has no edges.
        mst = new Edge[V-1]; // A <- 0

        // heap for sorting indices of array of edges
        Heap h = new Heap(E, edge);

        // create partition of singleton sets for the vertices
        System.out.println("\nSets before Kruskal's:");
        partition = new UnionFindSets(V);
        partition.showSets();

        while(i < V-1){
            /*
            inserts the edge from the top of the heap,
            removes that edge from the heap 
            then sorts the edge heap array by the edge's weight
            */
            e = h.edge[h.remove()];
            
            uSet =  partition.findSet(e.u);
            vSet = partition.findSet(e.v);
            if(uSet != vSet){
                partition.unionByRank(uSet, vSet);
                System.out.print("Inserting egde to MST: ");
                e.show();
                mst[i++] = e;
            }
            
        }
        System.out.println("\nTree of vertices:");
        partition.showTrees();
        System.out.println("\nSets after Kruskal's:");
        partition.showSets();
        return mst;
    }// END Kruskal's


    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    public void showMST()
    {
        System.out.print("\nMinimum spanning tree build from following edges:\n");
        for(int e = 0; e < V-1; ++e) {
            mst[e].show(); 
        }
        System.out.println();
       
    }

} // end of Graph class
    
// Driver code
class KruskalTrees {
    public static void main(String[] args) throws IOException
    {
        Scanner sc = new Scanner(System. in );
        String fname = "wGraph1.txt";


        System.out.print("\nInput name of file with graph definition: ");
        fname = sc.nextLine();
        sc.close(); // Closing Scanner after use

        Graph g = new Graph(fname);

        g.MST_Kruskal();

        g.showMST();
        
    }
}    


