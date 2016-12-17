package index;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andre on 23/11/2016.
 */
public class IndexEntry {
    //public String term;
    public Integer doc_id;
    public Double weight;
    public List<Integer> positions;


    public IndexEntry( Integer doc_id, Double weight, List<Integer> positions) {

        this.doc_id = doc_id;
        this.weight = weight;
        this.positions = positions;
    }



    public Integer getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(Integer doc_id) {
        this.doc_id = doc_id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public String toString(){
       // StringBuilder stringBuilder = new StringBuilder();

        //stringBuilder.append(doc_id+"="+weight+"[");

        String pos = positions.stream()
                .map(i -> i.toString())
                .collect(Collectors.joining(","));

        String str = new String (doc_id+"="+weight+"["+pos+"]\n");

        return str;

    }
}
