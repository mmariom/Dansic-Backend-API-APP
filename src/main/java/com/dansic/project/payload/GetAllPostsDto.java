package com.dansic.project.payload;

import lombok.Data;

@Data
public class GetAllPostsDto {

    private Long id;

    private String title;

    private String address;

    private String category;

    private byte[] previewImage;
}
