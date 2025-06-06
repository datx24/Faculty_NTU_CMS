package faculty.ntu.cms.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithPostDTO {
    private Category category;
    private Post post;

    public static CategoryWithPostDTO fromEntity(Category category, Post newestPost) {
        if (category == null) {
            return null;
        }
        return new CategoryWithPostDTO(
            category,newestPost
        );
    }
}