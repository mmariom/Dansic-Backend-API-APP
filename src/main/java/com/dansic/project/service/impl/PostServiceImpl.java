package com.dansic.project.service.impl;

import com.dansic.project.entity.*;
import com.dansic.project.exceptions.ResourceNotFound;
import com.dansic.project.payload.GetAllPostsDto;
import com.dansic.project.payload.GetSinglePostDto;
import com.dansic.project.payload.PostDto;
import com.dansic.project.repo.CategoryRepository;
import com.dansic.project.repo.ConditionRepository;
import com.dansic.project.repo.PostImageRepository;
import com.dansic.project.repo.PostRepository;
import com.dansic.project.service.PostService;
import com.dansic.project.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ConditionRepository conditionRepository;

    @Autowired
    private PostImageRepository postImageRepository;




    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow();
    }

    private Condition getConditionById(Integer conditionId) {
        return conditionRepository.findById(conditionId).orElseThrow();
    }





    @Override
    public PostDto createPost(String postTitle, String postDescription, String postAddress, Integer categoryId, Integer conditionId, List<MultipartFile> files) throws IOException {
        User currentUser = getCurrentUser();
        Category foundedCategoryById = getCategoryById(categoryId);
        Condition foundedConditionById = getConditionById(conditionId);

        Post savedPost = savePost(postTitle, postDescription, postAddress, currentUser, foundedCategoryById, foundedConditionById, files);

        return createPostDto(savedPost, foundedCategoryById, foundedConditionById, currentUser);
    }




    @Override
    public List<GetAllPostsDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return convertListPostToListGetAllPostsDTO(posts);
    }

    @Override
    public List<GetAllPostsDto> getAllPostsByCategory(Integer pageNo, Integer pageSize, String sortBy, Integer categoryId) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Post> posts;
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            posts = postRepository.findByCategory(category, paging);
        } else {
            posts = postRepository.findAll(paging);
        }

        return convertListPostToListGetAllPostsDTO(posts.getContent());
    }

    @Override
    public GetSinglePostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post not found","id", id));

        GetSinglePostDto getSinglePostDto = new GetSinglePostDto();
        getSinglePostDto.setId(post.getId());
        getSinglePostDto.setTitle(post.getTitle());
        getSinglePostDto.setDescription(post.getDescription());
        getSinglePostDto.setAddress(post.getAddress());
        getSinglePostDto.setCategory(post.getCategory().getName());
        getSinglePostDto.setCondition(post.getCondition().getName());
        getSinglePostDto.setEmail(post.getUser().getEmail());
        getSinglePostDto.setPhone(post.getUser().getPhoneNo());
        getSinglePostDto.setNickname(post.getUser().getNickname());
        getSinglePostDto.setImages(post.getImages().stream().map(postImage -> ImageUtils.decompressImage(postImage.getData())).collect(Collectors.toList()));
        return getSinglePostDto;
    }




    @Override
    public PostDto updatePost(Long id, String postTitle, String postDescription, String postAddress, Integer categoryId, Integer conditionId, List<MultipartFile> files) throws Exception {
        User currentUser = getCurrentUser();
        Category foundedCategoryById = getCategoryById(categoryId);
        Condition foundedConditionById = getConditionById(conditionId);

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post not found", "id", id));
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new Exception("You are not authorized to update this post.");
        }

        Post updatedPost = updatePostDetails(post, postTitle, postDescription, postAddress, foundedCategoryById, foundedConditionById, files);

        return createPostDto(updatedPost, foundedCategoryById, foundedConditionById, currentUser);
    }



    @Override
    public void deletePostById(Long id) throws Exception {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post not found","id", id));
        if (post.getUser().getId().equals(currentUser.getId())) {
            postRepository.delete(post);
        } else {
            throw new Exception("You are not authorized to delete this post.");
        }
    }


    private List<GetAllPostsDto> convertListPostToListGetAllPostsDTO(List<Post> posts) {
        return posts.stream().map(post -> {
            GetAllPostsDto getAllPostsDto = new GetAllPostsDto();

            getAllPostsDto.setId(post.getId());
            getAllPostsDto.setTitle(post.getTitle());
            getAllPostsDto.setAddress(post.getAddress());
            getAllPostsDto.setCategory(post.getCategory().getName());
            getAllPostsDto.setPreviewImage(ImageUtils.decompressImage(post.getImages().get(0).getData()));

            return getAllPostsDto;
        }).collect(Collectors.toList());
    }


    private Post savePost(String postTitle, String postDescription, String postAddress, User currentUser, Category category, Condition condition, List<MultipartFile> files) throws IOException {
        Post entityPost = new Post();
        entityPost.setTitle(postTitle);
        entityPost.setDescription(postDescription);
        entityPost.setAddress(postAddress);
        entityPost.setUser(currentUser);
        entityPost.setCategory(category);
        entityPost.setCondition(condition);

        Post savedPost = postRepository.save(entityPost);

        List<PostImage> savedImages = saveImages(files, savedPost);
        entityPost.setImages(savedImages);

        return postRepository.save(entityPost);
    }

    private PostDto createPostDto(Post savedPost, Category category, Condition condition, User currentUser) {
        PostDto dtoResponse = new PostDto();
        dtoResponse.setTitle(savedPost.getTitle());
        dtoResponse.setDescription(savedPost.getDescription());
        dtoResponse.setAddress(savedPost.getAddress());
        dtoResponse.setCategory(category.getName());
        dtoResponse.setCondition(condition.getName());
        dtoResponse.setEmail(currentUser.getEmail());
        dtoResponse.setPhone(currentUser.getPhoneNo());
        dtoResponse.setNickname(currentUser.getNickname());

        return dtoResponse;
    }


    private Post updatePostDetails(Post post, String postTitle, String postDescription, String postAddress, Category category, Condition condition, List<MultipartFile> files) throws IOException {
        post.setTitle(postTitle);
        post.setDescription(postDescription);
        post.setAddress(postAddress);
        post.setCategory(category);
        post.setCondition(condition);



        if (files != null && !files.isEmpty()) {

            postImageRepository.deletePostImagesByPostId(post.getId());

            post.getImages().clear();
            List<PostImage> updatedImages = saveImages(files, post);
            post.setImages(updatedImages);
        }

        return postRepository.save(post);
    }





    protected List<PostImage> saveImages(List<MultipartFile> files, Post post) throws IOException {
        return files.stream().map(file -> {
            try {
                return PostImage.builder()
                        .name(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .data(ImageUtils.compressImage(file.getBytes()))
                        .post(post)
                        .build();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }




}


