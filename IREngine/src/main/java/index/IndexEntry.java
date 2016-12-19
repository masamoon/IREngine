package index;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Andre on 23/11/2016.
 */
public class IndexEntry implements Comparable<IndexEntry> {
    //public String term;
    private Integer doc_id;
    private Double weight;
    private Collection<Integer> positions;


    IndexEntry(Integer doc_id, Double weight, Collection<Integer> positions) {

        this.doc_id = doc_id;
        this.weight = weight;
        this.positions = positions;
    }


    public Integer getDoc_id() {
        return doc_id;
    }


    public Double getWeight() {
        return weight;
    }

    public Collection<Integer> getPositions() {
        return positions;
    }

    public String toString() {
        // StringBuilder stringBuilder = new StringBuilder();

        //stringBuilder.append(doc_id+"="+weight+"[");
        StringBuilder str = new StringBuilder().append(doc_id).append("=").append(weight);
        String pos = positions.stream()
                .map(Integer::toUnsignedString)
                .collect(Collectors.joining(",", "[", "]"));
        str.append(pos);
        return str.toString();
    }

    @Override
    public int compareTo(IndexEntry o) {

        return -Double.compare(o.weight, this.weight);
    }
}
