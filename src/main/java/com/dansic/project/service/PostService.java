package com.dansic.project.service;

import com.dansic.project.entity.Post;
import com.dansic.project.payload.GetAllPostsDto;
import com.dansic.project.payload.GetSinglePostDto;
import com.dansic.project.payload.PostDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    PostDto createPost(String postTitle,String postDescription,String postAddress,Integer categoryId,Integer conditionId,List<MultipartFile> files) throws Exception;

    List<GetAllPostsDto> getAllPosts();
    List<GetAllPostsDto> getAllPostsByCategory(Integer pageNo, Integer pageSize, String sortBy, Integer categoryId);

    GetSinglePostDto getPostById(Long id);

    PostDto updatePost(Long id, String postTitle, String postDescription, String postAddress, Integer categoryId, Integer conditionId, List<MultipartFile> files) throws Exception;

    void deletePostById(Long id) throws Exception;
}
