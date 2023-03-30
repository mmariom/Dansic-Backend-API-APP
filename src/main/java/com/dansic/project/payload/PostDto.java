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


}
