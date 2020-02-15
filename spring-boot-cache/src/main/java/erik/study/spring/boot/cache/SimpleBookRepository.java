package erik.study.spring.boot.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SimpleBookRepository implements BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBookRepository.class);

    @Override
    @Cacheable("books")  //这个cache会根据函数名和参数来构造键。
    public Book getByIsbn(String isbn) {
        logger.info("method getByIsbn() was called with isbn:{}", isbn);
        simulateSlowService();
        return new Book(isbn, "Some book");
    }

    // Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
