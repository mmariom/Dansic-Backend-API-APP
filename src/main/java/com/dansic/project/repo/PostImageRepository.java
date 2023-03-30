package com.dansic.project.repo;

import com.dansic.project.entity.Post;
import com.dansic.project.entity.PostImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository  extends JpaRepository<PostImage,Long> {


    @Transactional
    void deletePostImagesByPostId(Long id);

}
