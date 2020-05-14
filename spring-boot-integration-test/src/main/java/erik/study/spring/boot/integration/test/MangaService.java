
package erik.study.spring.boot.integration.test;

import erik.study.spring.boot.integration.test.model.Manga;
import erik.study.spring.boot.integration.test.model.MangaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MangaService {

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);

//    private static final String MANGA_SEARCH_URL = "http://api.jikan.moe/search/manga/";

    private static final String MANGA_SEARCH_URL = "https://api.jikan.moe/v3/search/manga?q=Fate/Zero&page=1";

    @Autowired
    RestTemplate restTemplate;

    public List<Manga> getMangasByTitle(String title) {

        return restTemplate.getForEntity(MANGA_SEARCH_URL, MangaResult.class).getBody().getResult();
    }
}