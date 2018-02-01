package A6_Dijkstra;

import java.util.HashMap;
import java.util.HashSet;

public class Node {
	private int inDegree=0;
	private long instanceIdNum;
	private String instanceLabel;
	private Node prevNode;
	private boolean known;
	private long distance;
	private HashSet<Edge> inEdgeSet = new HashSet<Edge>();
	private HashSet<Edge> outEdgeSet= new HashSet<Edge>();	
	public Node(long idNum, String label) {
		instanceIdNum = idNum;
		instanceLabel = label;
		known=false;
	}

	public long getNodeID() {
		return instanceIdNum;
	}

	public String getNodeLabel() {
		return instanceLabel;
	}
	
	public void addInEdge(Edge b){
		inEdgeSet.add(b);
		inDegree++;
	}
	public void addOutEdge(Edge b){
		outEdgeSet.add(b);
	}
	public HashSet<Edge> getInEdgeSet(){
		return inEdgeSet;
	}
	public HashSet<Edge> getOutEdgeSet(){
		return outEdgeSet;
	}
	public void removeInEdge(Edge a){
		inEdgeSet.remove(a);
		inDegree--;
	}
	public void removeOutEdge(Edge a){
		outEdgeSet.remove(a);
	}
	public int getInDegree(){
		return inDegree;
	}
	
	public void decreaseInDegree(){
		inDegree--;
	}
	
	public void setDistance(long d){
		distance = d;
	}
	public long getDistance(){
		return distance;
	}
	public void setPrevNode(Node n){
		prevNode = n;
	}
	public Node getPrevNode(){
		return prevNode;
	}
	public void setKnown(boolean a){
		known = a;
	}
	public boolean getKnown(){
		return known;
	}

}
