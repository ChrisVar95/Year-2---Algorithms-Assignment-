// Kruskal's Minimum Spanning Tree Algorithm
// Union-find implemented using disjoint set trees without compression

import java.io.*;

//import Heap;    
 
class Edge {
    public int u, v, wgt;

    public Edge() {
        u = 0;
        v = 0;
        wgt = 0;
    }

    public Edge( int x, int y, int w) {
        this.u = x;
        this.v = y;
        this.wgt = w;
    }
    
    public void show() {
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n") ;
    }
    
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}


class Heap
{
	private int[] a;
    int N, Nmax;
    Edge[] edge;


    // Bottom up heap construc
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

    private void siftDown( int k) {
        int e, j;

        e = a[k];
        while( k <= N/2) {
            j = 2 * k;
            if (j < N && edge[a[j]].wgt > edge[a[j+1]].wgt) j++;

            if (edge[e].wgt <= edge[a[j]].wgt){
                break;
            }
            a[k] = a[j];

            k = j;
        }
        a[k] = e;
/*
        System.out.print( "\n\nNew array:  ");
        for (int i = 1; i < Nmax; i ++ ){

            System.out.print( "\n wgt= " + edge[a[i]].wgt+ " u= " +edge[a[i]].u + " v= " + edge[a[i]].v);
        }
            
        */
    }

    public int remove() {
        a[0] = a[1];
        a[1] = a[N--];
        siftDown(1);
        return a[0];
    }
}

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
        
        N = V;
        treeParent = new int[V+1];
        rank = new int[V+1];

        for(int i = 0; i < V; i++) { 
            treeParent[i] = i;  //vertexes are in seperate sets
            rank[i] = 0;
        }
    }

    public int findSet( int vertex)
    {   
        if(treeParent[vertex] != vertex){
            treeParent[vertex] = findSet(treeParent[vertex]);
        }
        return treeParent[vertex];
        //return 0;
    }
    
    public void union( int set1, int set2)
    {
        for(int i = 0; i < N; i++){
            if(treeParent[i] == set2){
                treeParent[i] = set1;
            }
        }
    }

    public void unionByRank(int set1, int set2){
        int u = findSet(set1);
        int v = findSet(set2);

        if (rank[u] < rank[v]){
            treeParent[u] = v;
        } else if (rank[u] > rank[v]){
            treeParent[v] = u;
        } else {
            treeParent[v] = u;
            rank[u]++;
        }
    }
    
    public void showTrees()
    {
        int i;
        for(i=1; i<=N; ++i)
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  " );
        System.out.print("\n");
    }
    
    public void showSets()
    {
        int u, root;
        int[] shown = new int[N+1];
        for (u=1; u<=N; ++u)
        {   
            root = findSet(u);
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
    private int V, E;
    private Edge[] edge;
    private Edge[] mst;        

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
    }


/**********************************************************
*
*       Kruskal's minimum spanning tree algorithm
*
**********************************************************/
    public Edge[] MST_Kruskal() 
    {
        int ei, i = 0;
        Edge e;
        int uSet, vSet; //set1 and set2
        UnionFindSets partition;
        
        // create edge array to store MST
        // Initially it has no edges.
        mst = new Edge[V-1]; // A <- 0

        // ???priority queue for indices of array of edges
        Heap h = new Heap(E, edge);

        // create partition of singleton sets for the vertices
        partition = new UnionFindSets(V);
        
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
            
            //partition.showSets();
            partition.showTrees();
        }
        return mst;
    }


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
    
    // test code
class KruskalTrees {
    public static void main(String[] args) throws IOException
    {
        String fname = "wGraph1.txt";
        //System.out.print("\nInput name of file with graph definition: ");
        //fname = Console.ReadLine();

        Graph g = new Graph(fname);

        g.MST_Kruskal();

        g.showMST();
        
    }
}    


