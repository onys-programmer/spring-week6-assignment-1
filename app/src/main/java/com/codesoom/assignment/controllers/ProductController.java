package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Product 관련 HTTP 요청을 처리합니다.
 */

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    private final AuthenticationService authenticationService;

    public ProductController(ProductService productService, AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    /**
     * 모든 제품 리스트를 응답합니다.
     * @return 제품리스트
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 특정 제품을 찾아 응답합니다.
     * @param id 찾을 제품의 식별자
     * @return 찾은 제품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 주어진 제품 정보대로 제품을 생성합니다.
     * @param productData 제품 정보
     * @return 생성된 제품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = authorization.substring("Bearer ".length());

        Long userId = authenticationService.parseToken(accessToken);

        return productService.createProduct(productData);
    }

    /**
     * 제품을 찾아 정보를 수정하고 리턴합니다.
     * @param id 찾을 제품의 식별자
     * @param productData 수정된 제품 정보
     * @return 갱신된 제품
     */
    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * 특정 제품을 찾아 삭제합니다.
     * @param id 찾을 제품의 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public void handleMissingRequestHeaderException() {
        //
    }
}
