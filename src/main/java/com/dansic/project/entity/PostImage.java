package com.dansic.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@Builder
@Entity
@Table(name = "post_image")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contentType;

    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    public PostImage() {
    }

    public PostImage(Long id, String name, String contentType, byte[] data, Post post) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.data = data;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostImage postImage = (PostImage) o;
        return Objects.equals(id, postImage.id) && Objects.equals(name, postImage.name) && Objects.equals(contentType, postImage.contentType) && Arrays.equals(data, postImage.data) && Objects.equals(post, postImage.post);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, contentType, post);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
