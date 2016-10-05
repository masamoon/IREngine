import java.net.URI;

public class Document {

    private int id;

    private String dataStream;

    private URI uri;

    public Document(int id, String dataStream, URI uri) {
        this.id = id;
        this.dataStream = dataStream;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataStream() {
        return dataStream;
    }

    public void setDataStream(String dataStream) {
        this.dataStream = dataStream;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
