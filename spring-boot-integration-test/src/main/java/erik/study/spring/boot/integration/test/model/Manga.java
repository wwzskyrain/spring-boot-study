package erik.study.spring.boot.integration.test.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manga {
    private String title;
    private String description;
    private Integer volumes;
    private Double score;
}