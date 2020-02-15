package erik.study.spring.boot.cache;

public interface BookRepository {

	Book getByIsbn(String isbn);

}
