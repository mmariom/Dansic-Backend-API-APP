package com.dansic.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private String title;

    private String description;

    private String address;

    private String category;

    private String condition;

    private String nickname;

    private String email;

    private String phone;
//    private List<PostImage> images = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDto postDto = (PostDto) o;
        return Objects.equals(title, postDto.title) && Objects.equals(description, postDto.description) && Objects.equals(address, postDto.address) && Objects.equals(category, postDto.category) && Objects.equals(condition, postDto.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, address, category, condition);
    }
}
