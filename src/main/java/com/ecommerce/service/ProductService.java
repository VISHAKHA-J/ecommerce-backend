package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(ProductRepository productRepository, CloudinaryService cloudinaryService){
        this.productRepository=productRepository;
        this.cloudinaryService=cloudinaryService;
    }


    public Product createProduct(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            List<MultipartFile> images
    ) {
        validateProductDetails(name, price, stock);
        validateImages(images, true);
        List<String> imageUrls = new ArrayList<>();

        for(MultipartFile image : images){
            String imageUrl = cloudinaryService.uploadImage(image);
            imageUrls.add(imageUrl);
        }

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .imageUrls(imageUrls)
                .build();

        return productRepository.save(product);
    }

    private void validateProductDetails(
            String name,
            BigDecimal price,
            Integer stock
    ){
        if(name==null || name.isBlank()){
            throw new IllegalArgumentException("Product Name is Required or Invalid");
        }

        if(price==null || price.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Product price must be greater than zero");
        }

        if(stock==null || stock < 0){
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
    }

    private void validateImages(
            List<MultipartFile> images,
            boolean required
    ){
        if(required && (images==null || images.isEmpty())){
            throw new IllegalArgumentException("At least one product image is required");
        }
        if(images != null){
            for(MultipartFile image : images){
                if(image.isEmpty()){
                    throw new IllegalArgumentException("Images file cannot be empty");
                }

                String fileName = image.getOriginalFilename();
                if(fileName == null){
                    throw new IllegalArgumentException("Invalid image file");
                }

                String lowerFileName = fileName.toLowerCase();
                if(
                        !(lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg") || lowerFileName.endsWith(".png") || lowerFileName.endsWith(".webp")
                        )){
                    throw new IllegalArgumentException("Only jpg, jpeg, png, webp image types are allowed");
                }
            }
        }
    }


//    crud
    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Product Not Found"));
    }


    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        productRepository.delete(product);
        System.out.println("Deleted successfully: { " + product.toString() + " }");
    }


    public Product updateProduct(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            List<MultipartFile> images
    ){
        validateImages(images, false);
        validateProductDetails(name, price, stock);

        Product product = productRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Product Not Found"));

        product.setName(name);
        product.setDescription(description);
        product.setStock(stock);
        product.setPrice(price);

        if(images!=null && !images.isEmpty()){
            List<String> imageUrls = new ArrayList<>();
            for(MultipartFile image : images){
                String imageUrl = cloudinaryService.uploadImage(image);
                imageUrls.add(imageUrl);
            }
            product.setImageUrls(imageUrls);
        }
        return productRepository.save(product);
    }


    public Page<Product> getAllProducts(String keyword, int page, int size){
        if(page<0){
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if(size<0){
            throw new IllegalArgumentException(("Size cannot be negative"));
        }
        Pageable pageable = PageRequest.of(page, size);
        if(keyword==null || keyword.isBlank()){
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
    }























}
