package com.example.demo1.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo1.model.*;
import com.example.demo1.repo.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryProductItemRepository subCategoryProductItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private AddToBagRepository addToBagRepository;

    @Autowired
    private OrdersRepository orderRepository;

    @Value("${razorpay.key.id}")
    private String razorpayId;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    private RazorpayClient razorpayClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    @PostConstruct
    public void init() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(razorpayId, razorpaySecret);
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public Category addCategory(Category data) {
        return categoryRepository.save(data);
    }

    public List<Category> getCategoryBasedOnHeaderName(String name) {
        return categoryRepository.findByHeader(name);
    }

    public List<subCategoryData> getItemBasedOnHeaderAndSubCategory(String header, String subCategory) {
        return subCategoryProductItemRepository.findByHeaderAndSubCategory(header, subCategory);
    }

    public User registerUser(User user) throws Exception {
        Optional<User> existingUser = userRepository.findByEmailId(user.getEmailId());
        if(existingUser.isPresent()){
            throw new Exception("User with this email already exists.");
        }

        Random random = new Random();
        int uniqueId = random.nextInt(900000) + 100000;
        user.setUserUniqueId(uniqueId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User loginUser(String emailId, String password) throws Exception {
        Optional<User> existingUser = userRepository.findByEmailId(emailId);
        if(existingUser.isPresent()){
            if(passwordEncoder.matches(password, existingUser.get().getPassword())){
                User userDada = existingUser.get();
                userDada.setPassword(null);
                return userDada;
            } else {
                throw new Exception("Invalid password.");
            }
        } else {
            throw new Exception("User not found with this email.");
        }
    }

    public subCategoryData addProductDetailsInSubCategoryProductItems(
            MultipartFile file,
            String name,
            double price,
            String desc,
            String subCategory,
            String header
    ) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "your_folder_name")); // optional folder

            String imageUrl = uploadResult.get("secure_url").toString();

            // Create entity object
            subCategoryData data = new subCategoryData();
            data.setName(name);
            data.setPrice(price);
            data.setDescription(desc);
            data.setSubCategory(subCategory);
            data.setHeader(header);
            data.setImageUrl(imageUrl); // save Cloudinary image URL

            return subCategoryProductItemRepository.save(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Wishlist> getUserWishlist(Integer userId) {
        return wishlistRepository.findAllByUserId(userId);
    }

    public Wishlist addToWishlist(Integer userId, Integer productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        subCategoryData product = subCategoryProductItemRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        return wishlistRepository.save(wishlist);
    }

    public ResponseEntity<String> deleteProductFromWishlist(Integer productId) {
        wishlistRepository.deleteByProduct_Id(productId);
        return ResponseEntity.ok("Deleted successfully");
    }

    public ResponseEntity<List<subCategoryData>> getAllProducts() {
        List<subCategoryData> allData = subCategoryProductItemRepository.findAll();
        return new ResponseEntity<>(allData, HttpStatus.OK);
    }

    public List<BagItems> getUserAddToBagData(Integer userId) {
        return addToBagRepository.findByUserId(userId);

    }

    public BagItems addItemToBag(Integer userId, Integer productId, String size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        subCategoryData product = subCategoryProductItemRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        BagItems bagItem = new BagItems();
        bagItem.setUser(user);
        bagItem.setProduct(product);
        bagItem.setSize(size);

        return addToBagRepository.save(bagItem);
    }

    public ResponseEntity<String> deleteItemsFromBag(Integer productId) {
        addToBagRepository.deleteByProduct_Id(productId);
        return ResponseEntity.ok("Deleted successfully");
    }

    public Orders createOrders(Orders orders) throws RazorpayException {
        if (orders.getProduct() != null && orders.getProduct().getId() != null) {
            subCategoryData product = subCategoryProductItemRepository.findById(orders.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            orders.setProduct(product);
        }

        if(orders.getUser() != null && orders.getUser().getEmailId() != null){
            User user = userRepository.findById(orders.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            orders.setUser(user);
        }

        JSONObject json = new JSONObject();

        json.put("amount", orders.getTotalAmount() * 100);
        json.put("currency", "INR");
        String receiptId = "rcpt_" + System.currentTimeMillis();
        json.put("receipt", receiptId);
        json.put("payment_capture", 1); // auto capture

        Order razorpayOrder = razorpayClient.orders.create(json);

        orders.setRazorpayOrderId(razorpayOrder.get("id"));
        orders.setOrderStatus(razorpayOrder.get("status"));
        orders.setOrderDate(LocalDateTime.now());

        return orderRepository.save(orders);
    }

    public Address addAddressToUser(Integer userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        address.setUser(user);
        return addressRepository.save(address);
    }

    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    public ResponseEntity<String> deleteAddress(Long id) {
        addressRepository.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    public ResponseEntity<List<Orders>> getAllOrdersOfAllUsers() {
        List<Orders> allOrders = orderRepository.findAll();
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }
}
