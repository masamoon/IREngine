package document; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * document.Doc Processor interface.
 */
public interface DocumentProcessor extends Iterable<Doc> {

    DocumentIterator iterator();

    abstract class DocumentIterator implements Iterator<Doc> {
        protected final Path path;
        protected Doc currentDoc;
        private boolean first;

        protected DocumentIterator(Path path) {
            this.path = path;
            first = true;

        }

        @Override
        public boolean hasNext() {
            if (first) fetchNextDocument();
            first = false;
            return currentDoc != null;
        }

        @Override
        public Doc next() {
            if (hasNext()) {
                Doc d = currentDoc;
                fetchNextDocument();
                return d;
            } else {
                throw new NoSuchElementException();
            }
        }

        protected abstract void fetchNextDocument();
    }
}
