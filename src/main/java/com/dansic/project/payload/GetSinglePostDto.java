package com.dansic.project.payload;

import lombok.Data;

import java.util.List;

@Data
public class GetSinglePostDto {

    private Long id;

    private String title;

    private String description;

    private String address;

    private String category;

    private String condition;

    private String nickname;

    private String email;

    private String phone;

    private List<byte[]> images;
}
