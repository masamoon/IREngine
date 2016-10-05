/**
 * Created by Andre on 05/10/2016.
 */
public class Match {
    private int docid;
    private int nocurr;

    public Match(int docid, int nocurr) {
        this.docid = docid;
        this.nocurr = nocurr;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public int getNocurr() {
        return nocurr;
    }

    public void setNocurr(int nocurr) {
        this.nocurr = nocurr;
    }


}
