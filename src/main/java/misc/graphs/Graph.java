package misc.graphs;

import misc.Searcher;
import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    private IDictionary<V, IDictionary<V, IList<E>>> adjacencyList;
    private int numVertices;
    private int numEdges;
    private ISet<V> verticesWithEdges;
    private IList<E> edgesWithEdges;
    
    // Constructs a new graph based on the given vertices and edges.
    // @throws IllegalArgumentException  if any of the edges have a negative weight
    // @throws IllegalArgumentException  if one of the edges connects to a vertex not present in the 'vertices' list
    public Graph(IList<V> vertices, IList<E> edges) {    
        adjacencyList = new ChainedHashDictionary<V, IDictionary<V, IList<E>>>(vertices.size());
        verticesWithEdges = new ChainedHashSet<V>();
        int numEdge = 0;
        
        for (V vertex : vertices) {
            adjacencyList.put(vertex, null);
        }
        
        for (E edge : edges) {
            V vertex1 = edge.getVertex1();
            V vertex2 = edge.getVertex2();
            
            if (edge.getWeight() < 0 || !adjacencyList.containsKey(vertex1) || 
                    !adjacencyList.containsKey(vertex2)) {
                throw new IllegalArgumentException();
            }
            
            if (!verticesWithEdges.contains(vertex1)) {
                verticesWithEdges.add(vertex1);
            } 
            
            if (!verticesWithEdges.contains(vertex2)) {
                verticesWithEdges.add(vertex2);
            } 
            
            addEdgeToGraph(vertex1, vertex2, edge);         
            addEdgeToGraph(vertex2, vertex1, edge);
            
            numEdge++;
        }  
        
        numVertices = adjacencyList.size();
        numEdges = numEdge;
        edgesWithEdges = edges;
    }
    
    // Checks if the vertex1 has any edges, if not adds a new dictionary with the new edge
    private void addEdgeToGraph(V vertex1, V vertex2, E edge) {
        if (adjacencyList.get(vertex1) == null) {  
            IDictionary<V, IList<E>> edgeDict = new ChainedHashDictionary<V, IList<E>>(5); 
            IList<E> edgeList = new DoubleLinkedList<E>();
            edgeList.add(edge);
            edgeDict.put(vertex2, edgeList);
            adjacencyList.put(vertex1, edgeDict);
        } else if (!adjacencyList.get(vertex1).containsKey(vertex2)) {
            IList<E> edgeList = new DoubleLinkedList<E>();
            edgeList.add(edge);
            adjacencyList.get(vertex1).put(vertex2, edgeList);
        } else {
            if (adjacencyList.get(vertex1).get(vertex2).get(0).compareTo(edge) <= 0) {
                adjacencyList.get(vertex1).get(vertex2).add(edge);
            } else {
                adjacencyList.get(vertex1).get(vertex2).insert(0, edge);
            }     
        }
    }

    public Graph(ISet<V> vertices, ISet<E> edges) {
        this(setToList(vertices), setToList(edges));
    }

    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    // Returns the number of vertices contained within this graph.
    public int numVertices() {
        return this.numVertices;
    }

    // Returns the number of edges contained within this graph.
    public int numEdges() {
        return this.numEdges;
    }

   
    // Returns the set of all edges that make up the minimum spanning tree of this graph.
    // Precondition: the graph does not contain any unconnected components.
    public ISet<E> findMinimumSpanningTree() {
        if (adjacencyList == null || edgesWithEdges == null) {
            throw new IllegalArgumentException();
        }

        IDisjointSet<V> disSet = new ArrayDisjointSet<V>(verticesWithEdges.size());  
        for (V vertex : verticesWithEdges) {
            disSet.makeSet(vertex);   
        }
        
        ISet<E> minSpanTree = new ChainedHashSet<E>();
        IList<E> sortedEdges = Searcher.topKSort(numEdges, edgesWithEdges);
        
        for (E edge : sortedEdges) {
            if (disSet.findSet(edge.getVertex1()) != disSet.findSet(edge.getVertex2())) {
                minSpanTree.add(edge);
                disSet.union(edge.getVertex1(), edge.getVertex2());
            }
        }
        
        return minSpanTree;        
    }
    


    // Returns the edges that make up the shortest path from the start to the end.
    // Returns an empty list if the start and end vertices are the same.
    // @throws NoPathExistsException  if there does not exist a path from the start to the end
    public IList<E> findShortestPathBetween(V start, V end) {
        if (start == null || end == null || !adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            throw new IllegalArgumentException();
        }
        
        if (adjacencyList.get(start) == null || adjacencyList.get(end) == null) {
            throw new NoPathExistsException();
        }
        
        if (start.equals(end)) {
           return new DoubleLinkedList<E>();
        }
        
        IPriorityQueue<Vertex<V>> heap = new ArrayHeap<>();
        IDictionary<V, Vertex<V>> vertexObjects = new ChainedHashDictionary<V, Vertex<V>>();
        
        for (KVPair<V, IDictionary<V, IList<E>>> vertices : adjacencyList) {
            if (vertices.getValue() != null) {
                Vertex<V> vertexOb;
                if (start.equals(vertices.getKey())) {
                    vertexOb = new Vertex<V>(start, 0.0);
                } else {
                    vertexOb = new Vertex<V>(vertices.getKey());
                }
                
                heap.insert(vertexOb);
                vertexObjects.put(vertexOb.vert, vertexOb);
            }
        }
        
        while (!heap.isEmpty()) {
            Vertex<V> vertPath = heap.removeMin();
            
            if (vertPath.vert.equals(end)) {
                IList<E> fullPath = new DoubleLinkedList<E>();
                
                while (vertPath.predesessor != null) {
                    fullPath.insert(0, adjacencyList.get(vertPath.vert).get(vertPath.predesessor.vert).get(0));
                    vertPath = vertPath.predesessor;
                }
                
                if (vertPath.vert.equals(start)) {
                    return fullPath;
                } else {
                    throw new NoPathExistsException();
                }
            }
            
            for (KVPair<V, IList<E>> vertEdges : adjacencyList.get(vertPath.vert)) {
                if (!vertEdges.getKey().equals(vertPath.vert)) {   
                    E shortEdge = vertEdges.getValue().get(0);
                    Vertex<V> otherVert = vertexObjects.get(shortEdge.getOtherVertex(vertPath.vert));
                    
                    if (vertPath.cost + shortEdge.getWeight() < otherVert.cost) {
                        Vertex<V> temp = new Vertex<V>(otherVert.vert, vertPath.cost + shortEdge.getWeight(), vertPath);
                        heap.insert(temp); 
                        vertexObjects.put(otherVert.vert, temp);
                    }  
                }
            }    
        }
        
        throw new NoPathExistsException();   
    }
      
    private static class Vertex<V> implements Comparable<Vertex<V>>  {
        private V vert;
        private Vertex<V> predesessor;
        private double cost;
        
        public Vertex(V vertex, double cost, Vertex<V> pred) {
            this.vert = vertex;
            this.cost = cost;
            this.predesessor = pred;
        }
        
        public Vertex(V vertex, double cost) {
            this(vertex, cost, null);
        }
        
        public Vertex(V vertex) {
            this(vertex, Double.POSITIVE_INFINITY);
        }

        public int compareTo(Vertex<V> other) {
            return Double.compare(this.cost, other.cost);
        }
    }
  
}