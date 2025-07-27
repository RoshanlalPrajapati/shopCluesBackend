package com.example.demo1.controller;

import com.example.demo1.model.*;
import com.example.demo1.repo.SubCategoryProductItemRepository;
import com.example.demo1.repo.UserRepository;
import com.example.demo1.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class categoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategory(){
        return new ResponseEntity<>(categoryService.getAllCategory(), HttpStatus.OK);
    }

    @GetMapping("/categories/{name}")
    public ResponseEntity<List<Category>> getCategoryBasedOnHeaderName(@PathVariable String name){
        return new ResponseEntity<>(categoryService.getCategoryBasedOnHeaderName(name), HttpStatus.OK);
    }

    @PostMapping("/addCategories")
    public ResponseEntity<Category> addCategory(@RequestBody Category data){
        return new ResponseEntity<>(categoryService.addCategory(data), HttpStatus.CREATED);
    }

    @GetMapping("/{header}/{subCategory}")
    public ResponseEntity<List<subCategoryData>> getItemBasedOnHeaderAndSubCategory(@PathVariable String header, @PathVariable String subCategory){
        return new ResponseEntity<>(categoryService.getItemBasedOnHeaderAndSubCategory(header, subCategory), HttpStatus.OK);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<subCategoryData>> getAllProducts(){
        return categoryService.getAllProducts();
    }

    @PostMapping("/addProductDetailsInSubCategoryProductItems")
    public ResponseEntity<subCategoryData> addProductDetailsInSubCategoryProductItems(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("desc") String desc,
            @RequestParam("subCategory") String subCategory,
            @RequestParam("header") String header) {
        try {
            subCategoryData savedData = categoryService.addProductDetailsInSubCategoryProductItems(
                    file, name, price, desc, subCategory, header
            );
            return new ResponseEntity<>(savedData, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws Exception {
        return new ResponseEntity<>(categoryService.registerUser(user),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) throws Exception {
        return new ResponseEntity<>(categoryService.loginUser(user.getEmailId(), user.getPassword()), HttpStatus.OK);
    }

    @GetMapping("/wishlist/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getWishlist(@PathVariable Integer userId) {
        List<Wishlist> wishlist = categoryService.getUserWishlist(userId);

        List<Map<String, Object>> response = new ArrayList<>();
        for (Wishlist w : wishlist) {
            Map<String, Object> item = new HashMap<>();
            item.put("emailId", w.getUser().getEmailId());
            item.put("userUniqueId", w.getUser().getUserUniqueId());

            subCategoryData p = w.getProduct();
            item.put("productId", p.getId());
            item.put("productName", p.getName());
            item.put("price", p.getPrice());
            item.put("imageUrl", p.getImageUrl());
            item.put("header", p.getHeader());
            item.put("description", p.getDescription());
            item.put("subCategory", p.getSubCategory());

            response.add(item);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/wishlist/delete/{productId}")
    public ResponseEntity<String> deleteProductItem(@PathVariable Integer productId){
        return categoryService.deleteProductFromWishlist(productId);
    }

    @PostMapping("/wishlist/add")
    public ResponseEntity<Wishlist> addToWishlist(@RequestParam Integer userId,
                                                  @RequestParam Integer productId) {
        Wishlist wishlist = categoryService.addToWishlist(userId, productId);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/addToBag/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getAllAddToBagData(@PathVariable Integer userId) {
//        return categoryService.getUserAddToBagData(userId);
        List<BagItems> bagItem = categoryService.getUserAddToBagData(userId);

        List<Map<String, Object>> response = new ArrayList<>();
        for (BagItems w : bagItem) {
            Map<String, Object> item = new HashMap<>();
            item.put("emailId", w.getUser().getEmailId());
            item.put("userUniqueId", w.getUser().getUserUniqueId());
            item.put("size", w.getSize());

            subCategoryData p = w.getProduct();
            item.put("productId", p.getId());
            item.put("productName", p.getName());
            item.put("price", p.getPrice());
            item.put("imageUrl", p.getImageUrl());
            item.put("header", p.getHeader());
            item.put("description", p.getDescription());
            item.put("subCategory", p.getSubCategory());

            response.add(item);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addToBag/addItemToBag")
    public ResponseEntity<BagItems> addItemToBag(@RequestParam Integer userId, @RequestParam Integer productId, @RequestParam String size){
        BagItems bagItem = categoryService.addItemToBag(userId, productId, size);
        return ResponseEntity.ok(bagItem);
    }

    @PostMapping("/addToBag/{productId}")
    public ResponseEntity<String> deleteItemsFromBag(@PathVariable Integer productId){
        return categoryService.deleteItemsFromBag(productId);
    }

    @PostMapping("/addAddress/{userId}")
    public ResponseEntity<Address> addAddress(@PathVariable Integer userId, @RequestBody Address address) {
        Address savedAddress = categoryService.addAddressToUser(userId, address);
        return ResponseEntity.ok(savedAddress);
    }

    @GetMapping("/getAddress/{userId}")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Long userId) {
        List<Address> addresses = categoryService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/deleteAddress/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable("addressId") Long id){
        return categoryService.deleteAddress(id);
    }
}
