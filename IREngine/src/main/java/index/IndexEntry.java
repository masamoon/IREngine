package index;

import java.util.List;

/**
 * Created by Andre on 23/11/2016.
 */
public class IndexEntry {
    public String term;
    public Integer doc_id;
    public Double weight;
    public List<Integer> positions;


    public IndexEntry(String term, Integer doc_id, Double weight, List<Integer> positions) {
        this.term = term;
        this.doc_id = doc_id;
        this.weight = weight;
        this.positions = positions;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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
}
