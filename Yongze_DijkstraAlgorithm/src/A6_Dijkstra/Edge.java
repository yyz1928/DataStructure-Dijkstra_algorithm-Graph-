package A6_Dijkstra;

public class Edge {
	private long id;
	private String source;
	private String destination;
	private long weight;
	private String label;

	public Edge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		id = idNum;
		source = sLabel;
		destination = dLabel;
		this.weight = weight;
		label = eLabel;
	}

	public long getEdgeID() {
		return id;
	}

	public String getEdgeLabel() {
		if(label == null){
			return null;
		}else{
		return label;
	}
	}
	public long getWeight() {
		return weight;
	}

	public String getSourceNode() {
		return source;
	}

	public String getDestinationNode() {
		return destination;
	}
}
