package A6_Dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class DiGraph implements DiGraph_Interface {

	// in here go all your data and methods for the graph
	// and the topo sort operation
	private HashMap<String, Node> nodeMap = new HashMap<String, Node>();
	private HashSet<Long> nodeID = new HashSet<Long>();
	private HashSet<Long> edgeID = new HashSet<Long>();

	public DiGraph() { // default constructor
		// explicitly include this
		// we need to have the default constructor
		// if you then write others, this one will still be there
	}

	@Override
	public boolean addNode(long idNum, String label) {
		// TODO Auto-generated method stub
		if (idNum < 0 || label == null)
			return false;
		boolean b = nodeMap.containsKey(label);
		if (b)
			return false;
		boolean a = nodeID.add(idNum);
		if (a != true)
			return false;
		nodeMap.put(label, new Node(idNum, label));
		return true;
		// }
	}

	@Override
	public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		// TODO Auto-generated method stub
		if (idNum < 0 || edgeID.contains(idNum))
			return false;
		if (weight < 0)
			return false;
		if (nodeMap.containsKey(sLabel) == false || nodeMap.containsKey(dLabel) == false)
			return false;
		Node sourceNode = nodeMap.get(sLabel);
		Node destinationNode = nodeMap.get(dLabel);
		HashSet<Edge> sourceOutEdge = sourceNode.getOutEdgeSet();
		for (Edge a : sourceOutEdge) {
			if (a.getDestinationNode() == dLabel) {
				return false;
			}
		}
		Edge addedEdge = new Edge(idNum, sLabel, dLabel, weight, eLabel);
		sourceNode.addOutEdge(addedEdge);
		destinationNode.addInEdge(addedEdge);
		nodeMap.put(sLabel, sourceNode);
		nodeMap.put(dLabel, destinationNode);
		edgeID.add(idNum);
		return true;
	}

	@Override
	public boolean delNode(String label) {
		// TODO Auto-generated method stub
		Node deletedNode = nodeMap.get(label);
		if (deletedNode == null) {
			return false;
		}
		nodeMap.remove(label);
		HashSet<Edge> outEdge = deletedNode.getOutEdgeSet();
		HashSet<Edge> inEdge = deletedNode.getInEdgeSet();
		for (Edge a : outEdge) {
			Node destinationNode = nodeMap.get(a.getDestinationNode());
			destinationNode.removeInEdge(a);
			edgeID.remove(a.getEdgeID());
			nodeMap.put(a.getDestinationNode(), destinationNode);
		}
		for (Edge a : inEdge) {
			Node sourceNode = nodeMap.get(a.getSourceNode());
			sourceNode.removeOutEdge(a);
			edgeID.remove(a.getEdgeID());
			nodeMap.put(a.getSourceNode(), sourceNode);
		}
		nodeID.remove(deletedNode.getNodeID());
		return true;
	}

	@Override
	public boolean delEdge(String sLabel, String dLabel) {
		// TODO Auto-generated method stub
		if (nodeMap.get(sLabel) == null || nodeMap.get(dLabel) == null) {
			return false;
		}
		Node sourceNode = nodeMap.get(sLabel);
		Node destinationNode = nodeMap.get(dLabel);
		HashSet<Edge> sourceOutEdge = sourceNode.getOutEdgeSet();
		Edge removedEdge = null;
		for (Edge a : sourceOutEdge) {
			if (a.getDestinationNode() == dLabel) {
				removedEdge = a;
				sourceNode.removeOutEdge(a);
			}
		}
		if (removedEdge == null) {
			return false;
		}
		HashSet<Edge> destinationInEdge = destinationNode.getInEdgeSet();
		for (Edge a : destinationInEdge) {
			if (a.getSourceNode() == sLabel) {
				destinationNode.removeInEdge(a);
			}
		}
		edgeID.remove(removedEdge.getEdgeID());
		nodeMap.put(sLabel, sourceNode);
		nodeMap.put(dLabel, destinationNode);
		return true;
	}

	@Override
	public long numNodes() {
		// TODO Auto-generated method stub
		return nodeID.size();
	}

	@Override
	public long numEdges() {
		// TODO Auto-generated method stub
		return edgeID.size();
	}

	@Override
	public String[] topoSort() {
		// TODO Auto-generated method stub
		if (nodeID.isEmpty() || edgeID.isEmpty()) {
			return null;
		}
		if (nodeID == null || edgeID == null) {
			return null;
		}
		HashMap<String, Node> copiedNodeMap = nodeMap;
		Stack<Node> zeroInDegreeStack = new Stack();
		int currentIndex = 0;
		String[] topoArray = new String[nodeID.size()];
		// get the start node
		for (String s : copiedNodeMap.keySet()) {
			Node a = copiedNodeMap.get(s);
			if (a.getInDegree() == 0) {
				zeroInDegreeStack.push(a);
			}
		}
		// check whether it is a complete cycle or not;
		if (zeroInDegreeStack.isEmpty()) {
			return null;
		}
		while (zeroInDegreeStack.empty() == false) {
			Node currentNode = zeroInDegreeStack.pop();
			topoArray[currentIndex] = currentNode.getNodeLabel();
			currentIndex++;
			if (currentNode.getOutEdgeSet().isEmpty()) {
				continue;
			}
			for (Edge b : currentNode.getOutEdgeSet()) {
				Node outNode = nodeMap.get(b.getDestinationNode());// for every
																	// chird
																	// node, do
																	// below
				outNode.decreaseInDegree();
				if (outNode.getInDegree() == 0) {
					zeroInDegreeStack.push(outNode);
				}
				copiedNodeMap.put(b.getDestinationNode(), outNode); // update
																	// the
																	// newest
																	// inDegree
																	// infor;
			}
			copiedNodeMap.remove(currentNode.getNodeLabel());
		}
		if (topoArray[nodeID.size() - 1] == null) {
			return null;
		} else {
			return topoArray;
		}
	}

	@Override
	public ShortestPathInfo[] shortestPath(String label) {
		// TODO Auto-generated method stub
		ShortestPathInfo[] pathInfo = new ShortestPathInfo[nodeID.size()];
		Node startNode = nodeMap.get(label);
		EntryPair start = new EntryPair(label, 0);
		MinBinHeap binHeap = new MinBinHeap();
		startNode.setDistance(0);
		startNode.setPrevNode(null);
		binHeap.insert(start);
		int currentIndex = 0;
		while (binHeap.size() > 0) {
			Node a = nodeMap.get(binHeap.getMin().getValue());
			ShortestPathInfo info = new ShortestPathInfo(a.getNodeLabel(), a.getDistance());
			binHeap.delMin();
			if (a.getKnown() == true) {
				continue;
			} else {
				a.setKnown(true);
			}
			pathInfo[currentIndex] = info;
			currentIndex++;
			for (Edge e : a.getOutEdgeSet()) {
				Node destinationNode = nodeMap.get(e.getDestinationNode());
				if (e.getWeight() + a.getDistance() < destinationNode.getDistance() || destinationNode.getDistance() == 0) {
					destinationNode.setDistance(e.getWeight() + a.getDistance());
					destinationNode.setPrevNode(a);
					EntryPair entry = new EntryPair(destinationNode.getNodeLabel(), (int) destinationNode.getDistance());
					binHeap.insert(entry);
				}
			}
		}
		if (pathInfo[nodeID.size()-1] != null) {
			return pathInfo;
		} else {
			int thisOneIsNull=0;
			Set<String> copiedNodeSet= nodeMap.keySet();
			for(int i =0 ; i<nodeID.size(); i++){
				if(pathInfo[i]!=null){
				copiedNodeSet.remove(pathInfo[i].getDest());
				}else{
					thisOneIsNull=i;
					break;
				}
			}
			for (String a: copiedNodeSet){
				pathInfo[thisOneIsNull]=new ShortestPathInfo(a,-1);
				thisOneIsNull++;
			}
			return pathInfo;
		}
	}
}