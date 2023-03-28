package com.dansic.project.controller;

import com.dansic.project.payload.GetAllPostsDto;
import com.dansic.project.payload.GetSinglePostDto;
import com.dansic.project.payload.PostDto;
import com.dansic.project.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostServiceImpl postService;

    @PostMapping("/secured/createPost")
    public ResponseEntity<PostDto> createPost(
            @RequestParam("title") String postTitle,
            @RequestParam("description") String postDescription,
            @RequestParam("address") String postAddress,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("condition") Integer condition,
            @RequestParam("files") List<MultipartFile> files

           ) throws IOException {


        return new ResponseEntity<>(postService.createPost(postTitle,postDescription,postAddress,categoryId,condition,files), HttpStatus.CREATED);
    }

    @GetMapping("/getPosts/{id}")
    public ResponseEntity<GetSinglePostDto> getPostById(@PathVariable("id") Long id){
//        return postService.getPostById(id);
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }


    @GetMapping("/getAllPosts")
    public ResponseEntity<List<GetAllPostsDto>> getAllPosts(){
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/getAllPosts/category/{categoryId}")
    public ResponseEntity<List<GetAllPostsDto>> getAllPostsByCategory(
//            @RequestParam(defaultValue = "0") Integer pageNo,
//            @RequestParam(defaultValue = "10") Integer pageSize,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(required = true) Integer categoryId
            @PathVariable("categoryId") Integer categoryId
    ){
        return new ResponseEntity<>(postService.getAllPostsByCategory(0,10,"id",categoryId), HttpStatus.OK);
    }


    @DeleteMapping("/secured/deletePost/{id}")
    public ResponseEntity<Map<String,String>> deletePostById(@PathVariable("id") Long id) throws Exception{
        postService.deletePostById(id);
        Map response = new HashMap<String, String>();
        response.put("body", "Post deleted successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
