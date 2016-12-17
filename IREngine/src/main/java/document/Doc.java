package document; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import java.net.URI;

/**
 * document.Doc data type, which is responsible for storing
 * the document information.
 */
public class Doc {

    private int id;

    private String dataStream;

    private URI uri;

    /**
     * document.Doc class constructor.
     *
     * @param id         identifier of the document
     * @param dataStream stream of data present in the document
     * @param uri        resource identification of the document
     */
    public Doc(int id, String dataStream, URI uri) {
        this.id = id;
        this.dataStream = dataStream;
        this.uri = uri;
    }

    /**
     * Returns the identifier of the document
     *
     * @return Identifier of the document
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier value of the document
     *
     * @param id identifier of the document
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the stream of data present in the document
     *
     * @return String containing the stream of data of the document
     */
    public String getDataStream() {
        return dataStream;
    }

    /**
     * Sets the stream of data value present in the document
     *
     * @param dataStream Stream of data of the document
     */
    public void setDataStream(String dataStream) {
        this.dataStream = dataStream;
    }

    /**
     * Gets the resource identifier of the document
     *
     * @return URI of the document
     */
    public URI getUri() {
        return uri;
    }

    /**
     * Sets the resource identifier of the document
     *
     * @param uri Uniform Resource Identifier for the document
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }
}
