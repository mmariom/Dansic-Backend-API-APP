package com.dansic.project.service.impl;

import com.dansic.project.entity.*;
import com.dansic.project.exceptions.ResourceNotFound;
import com.dansic.project.payload.GetAllPostsDto;
import com.dansic.project.payload.GetSinglePostDto;
import com.dansic.project.payload.PostDto;
import com.dansic.project.repo.*;
import com.dansic.project.service.PostService;
import com.dansic.project.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private  ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ConditionRepository conditionRepository;







    @Override
    public PostDto createPost(String postTitle,String postDescription,String postAddress,Integer categoryId,Integer conditionId,List<MultipartFile> files) throws IOException {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category foundedCategoryById = categoryRepository.findById(categoryId).orElseThrow();
        Condition foundedConditionById = conditionRepository.findById(conditionId).orElseThrow();



        Post entityPost = new Post();
        entityPost.setTitle(postTitle);
        entityPost.setDescription(postDescription);
        entityPost.setAddress(postAddress);
        entityPost.setUser(userDetails);
        entityPost.setCategory(foundedCategoryById);
        entityPost.setCondition(foundedConditionById);

        Post savedPost = postRepository.save(entityPost);

        List<PostImage> saveImages = saveImages(files, savedPost);
        entityPost.setImages(saveImages);
        postRepository.save(entityPost);


        PostDto dtoResponse = new PostDto();
        dtoResponse.setTitle(savedPost.getTitle());
        dtoResponse.setDescription(savedPost.getDescription());
        dtoResponse.setAddress(savedPost.getAddress());
        dtoResponse.setCategory(foundedCategoryById.getName());
        dtoResponse.setCondition(foundedConditionById.getName());
        dtoResponse.setEmail(userDetails.getEmail());
        dtoResponse.setPhone(userDetails.getPhoneNo());
        dtoResponse.setNickname(userDetails.getNickname());


        return  dtoResponse;
    }




    @Override
    public List<GetAllPostsDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        List<GetAllPostsDto> collect = posts.stream().map(post -> {
            GetAllPostsDto getAllPostsDto = new GetAllPostsDto();

            getAllPostsDto.setId(post.getId());
            getAllPostsDto.setTitle(post.getTitle());
            getAllPostsDto.setAddress(post.getAddress());
            getAllPostsDto.setCategory(post.getCategory().getName());
            getAllPostsDto.setPreviewImage(ImageUtils.decompressImage(post.getImages().get(0).getData()));

            return getAllPostsDto;
        }).collect(Collectors.toList());

        return collect;
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

        List<GetAllPostsDto> collect = posts.stream().map(post -> {
            GetAllPostsDto getAllPostsDto = new GetAllPostsDto();

            getAllPostsDto.setId(post.getId());
            getAllPostsDto.setTitle(post.getTitle());
            getAllPostsDto.setAddress(post.getAddress());
            getAllPostsDto.setCategory(post.getCategory().getName());
            getAllPostsDto.setPreviewImage(ImageUtils.decompressImage(post.getImages().get(0).getData()));

            return getAllPostsDto;
        }).collect(Collectors.toList());

        return collect;
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
    public void deletePostById(Long id) throws Exception {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post not found","id", id));
        if (post.getUser().getId().equals(currentUser.getId())) {
            postRepository.delete(post);
        } else {
            throw new Exception("You are not authorized to delete this post.");
        }
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




    private PostDto mapToDto(Post post) {
        return modelMapper.map(post,PostDto.class);

    }

    private Post mapToEntity(PostDto postDto) {
        return modelMapper.map(postDto,Post.class);

    }


}


