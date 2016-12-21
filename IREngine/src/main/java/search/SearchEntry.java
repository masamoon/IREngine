package search;

/**
 * Created by Andre on 20/12/2016.
 */
public class SearchEntry implements Comparable<SearchEntry> {

    private int docId;
    private double weight;

    public SearchEntry(int docId, double weight) {
        this.docId = docId;
        this.weight = weight;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(SearchEntry o) {
        if(o.getWeight() > getWeight())
            return 1;
        else if (o.getWeight() < getWeight())
            return -1;
        else
            return 0;
    }
}
