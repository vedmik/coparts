package software.sigma.bu003.internship.coparts.entity;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageParts {
    private List<Part> parts;
    private Long totalItems;
    private Integer currentPage;
    private Integer totalPages;

    public PageParts(Page<Part> pageParts) {
        this.parts = pageParts.getContent();
        this.totalItems = pageParts.getTotalElements();
        this.currentPage = pageParts.getNumber();
        this.totalPages = pageParts.getTotalPages();
    }
}
