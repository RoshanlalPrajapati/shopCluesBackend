package com.example.demo1.controller;

import com.example.demo1.model.Orders;
import com.example.demo1.repo.OrdersRepository;
import com.example.demo1.service.CategoryService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ordersController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Value("${razorpay.key.id}")
    private String razorpayId;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(razorpayId, razorpaySecret);
    }

    @GetMapping("/getOrderData")
    public ResponseEntity<?> getOrders(){
        return categoryService.getAllOrdersOfAllUsers();
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createOrder")
        public ResponseEntity<List<Orders>> createOrders(@RequestBody List<Orders> ordersList) throws RazorpayException {
            List<Orders> savedOrders = new ArrayList<>();
    
            for (Orders orders : ordersList) {
                Orders saved = categoryService.createOrders(orders);
                savedOrders.add(saved);
            }
            return new ResponseEntity<>(savedOrders, HttpStatus.CREATED);
    }

@PostMapping("/paymentCallback")
public ResponseEntity<String> paymentCallback(@RequestBody Map<String, String> payload) {
    try {
        String paymentId = payload.get("razorpay_payment_id");
        String orderId = payload.get("razorpay_order_id");
        String signature = payload.get("razorpay_signature");

        System.out.println("Payment ID: " + paymentId);
        System.out.println("Order ID: " + orderId);
        System.out.println("Signature: " + signature);

        String secret = razorpaySecret;
        String data = orderId + "|" + paymentId;
        String generatedSignature = HmacSHA256(data, secret);

        if (!generatedSignature.equals(signature)) {
            throw new RuntimeException("Invalid payment signature");
        }

        Orders order = ordersRepository.findByRazorpayOrderId(orderId);
        order.setOrderStatus("PAYMENT SUCCESS");
        ordersRepository.save(order);

        return ResponseEntity.ok("Payment verified and status updated");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Payment verification failed: " + e.getMessage());
    }
}

    private String HmacSHA256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
