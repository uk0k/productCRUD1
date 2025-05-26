
# Product CRUD 관리 애플리케이션 with Spring Security

**상품 등록/수정/삭제/조회 기능**에 **Spring Security 기반 로그인 인증, 권한(Role) 기반 접근 제어**,  
**Form 유효성 검사**, **관리자 전용 페이지**를 포함한 **보안 강화 CRUD 웹 애플리케이션**입니다.

1) GitHub Repository 링크
   https://github.com/uk0k/productCRUD1.git
---

## 기술 스택

| 구분       | 기술                                               |
|------------|----------------------------------------------------|
| Backend    | Spring Boot 3.2.5, Spring Data JPA, Spring Security |
| Frontend   | Thymeleaf, Bootstrap 5                             |
| Database   | MySQL 8.x                                          |
| Validation | Jakarta Bean Validation (`jakarta.validation.*`)   |
| Tool       | IntelliJ IDEA, Git, GitHub                         |

---

## 구현 기능 요약

### 1. 사용자 인증 (Authentication)
- 회원가입 (이메일 + 비밀번호)
- 로그인 / 로그아웃 기능 (`/register`, `/login`)
- 비밀번호는 `BCryptPasswordEncoder`로 암호화되어 저장됨
- 로그인 성공/실패 시 사용자 맞춤 메시지 표시

### 2. 권한 기반 인가 (Authorization)
- 기본 권한: `ROLE_USER`, `ROLE_ADMIN`
- `ROLE_USER`: 상품 목록 조회만 가능 (`GET /products`)
- `ROLE_ADMIN`: 상품 등록/수정/삭제 가능 (`POST`, `PUT`, `DELETE`)
- 일반 사용자에겐 **Edit/Delete 버튼 UI도 비노출**

### 3. 추가 기능
- 상품 등록/수정 시 유효성 검사 적용  
  - 이름, 브랜드, 제조국: 필수 입력  
  - 가격: 0 이상
- 관리자 전용 페이지 (`/admin/users`) 구현  
  - 전체 사용자 목록 확인 가능 (관리자 전용)

---

## 2) 단계별 수행결과



회원가입화면 

 <img width="457" alt="image" src="https://github.com/user-attachments/assets/024a835b-191f-474e-8a05-f9db0bd49e06" />


로그인화면

<img width="457" alt="image" src="https://github.com/user-attachments/assets/c93830a4-2e06-4516-99cf-07766bb70d0c" />
 

아이디 혹은 비밀번호 입력 안했을 시 또는 아이디 혹은 비밀번호가 틀렸을 때

<img width="458" alt="image" src="https://github.com/user-attachments/assets/fb362e4e-954e-4f2e-9667-e246ce06e04f" />


일반유저일때 상품목록 (Create New Product, Edit, Delete, 관리자 페이지 버튼이 안보임)

<img width="458" alt="image" src="https://github.com/user-attachments/assets/437bc52e-be7b-435e-bd63-d6c645efba32" />


관리자 로그인 (BCrypt 인코딩된 비밀번호 사용)
DB 에다가 INSERT INTO users (email, password, role) VALUES
('admin@example.com', '$2a$10$Z7vuAnkK3qH2c98IVzF30u3/7X7VpqM16d3ZzK8Zx6TbKx3cLbiIu', 'ROLE_ADMIN');
-- 비밀번호: admin123

<img width="458" alt="image" src="https://github.com/user-attachments/assets/21d76a5a-7c0d-41dd-8106-95671f907bc4" />


새로운 상품 만들기

<img width="458" alt="image" src="https://github.com/user-attachments/assets/3f753c55-a56d-4963-9a21-decac5c5125a" />


새로운 상품 입력안했을 때 에러메시지 출력 (유효성검사)

<img width="458" alt="image" src="https://github.com/user-attachments/assets/2973b6b5-ad3a-4704-9efa-8ecead671814" />


관리자일때 목록 (새로운 상품 추가됨 K3GT)

<img width="458" alt="image" src="https://github.com/user-attachments/assets/b5c17232-13ad-4afb-83b0-cdc9c60cc307" />


편집할때도 마찬가지로 아무것도 입력안하면 에러메시지 출력

<img width="458" alt="image" src="https://github.com/user-attachments/assets/8d816b50-cefc-401f-b9e2-c375c3568dab" />


K3GT 가격을 30000.0 에서 35000.0 으로 수정 

<img width="458" alt="image" src="https://github.com/user-attachments/assets/acd980b3-a420-4697-90cd-8acb677d1481" />


K3GT 를 삭제했을 때

<img width="458" alt="image" src="https://github.com/user-attachments/assets/4b704d9a-0e7c-4f78-b755-1626fac22fe3" />


관리자페이지 목록 (현재 ADMIN 인 admin1@hansung.ac.kr 과 그냥 USER인 2071053@hansung.ac.kr 이 있는 것을 볼 수 있다.)

<img width="458" alt="image" src="https://github.com/user-attachments/assets/7f551ff8-fe84-467f-ba9c-0347f1532d9f" />


3) 주요 소스 및 설명
 
1. 사용자 인증 기능: 회원가입, 로그인, 비밀번호 암호화
User.java 
package kr.ac.hansung.cse.productcrud1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

   public enum Role {
        ROLE_USER, ROLE_ADMIN
    }

   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(nullable = false, unique = true)
   private String email;

  @Column(nullable = false)
  private String password;

   @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  // ✅ 기본 생성자
    public User() {}

   // ✅ getter/setter 직접 명시
    public Long getId() {
        return id;
    }

   public String getEmail() {
        return email;
    }

   public void setEmail(String email) {
        this.email = email;
    }

   public String getPassword() {
        return password;
    }

  public void setPassword(String password) {
        this.password = password;
    }

   public Role getRole() {
        return role;
    }

   public void setRole(Role role) {
        this.role = role;
    }
}
•  사용자 정보를 저장하는 엔티티로, 이메일, 비밀번호, 권한(Role)을 관리함
•  ROLE_USER, ROLE_ADMIN을 enum으로 선언해 Role 기반 인가에 사용
UserService.java
package kr.ac.hansung.cse.productcrud1.service;

import kr.ac.hansung.cse.productcrud1.entity.User;
import kr.ac.hansung.cse.productcrud1.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
   private final BCryptPasswordEncoder encoder;

   @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

  public void registerUser(String email, String rawPassword, User.Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(rawPassword));
        user.setRole(role);
        userRepository.save(user);
    }

  @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
      return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
•  Spring Security에서 로그인 시 사용자 정보를 인증하기 위한 로직을 loadUserByUsername으로 구현
•  회원가입 시 비밀번호는 BCryptPasswordEncoder를 통해 안전하게 해싱
SecurityConfig.java
package kr.ac.hansung.cse.productcrud1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Spring Boot 3에서는 람다 방식으로 csrf 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/products/new", "/products/edit/**", "/products/delete/**").hasRole("ADMIN")
                        .requestMatchers("/products/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/products", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );
                return http.build();
    }

   @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
•  인증되지 않은 사용자 접근 제한
•  로그인 실패/성공 메시지 출력 및 역할 기반 접근 제어 설정

2. 상품 CRUD 및 유효성 검증
Product.java
package kr.ac.hansung.cse.productcrud1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "product")
public class Product {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

  @NotBlank(message = "브랜드를 입력해주세요.")
    private String brand;

   @NotBlank(message = "제조국을 입력해주세요.")
    private String madeIn;

   @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private double price;

  public Product() {}

   public Product(String name, String brand, String madeIn, double price) {
        this.name = name;
        this.brand = brand;
        this.madeIn = madeIn;
        this.price = price;
    }

  // Getter
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getMadeIn() { return madeIn; }
    public double getPrice() { return price; }

   // Setter
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setMadeIn(String madeIn) { this.madeIn = madeIn; }
    public void setPrice(double price) { this.price = price; }
}
•  @NotBlank, @Min 등으로 유효성 검사
•  유효성 위반 시 BindingResult로 처리하여 View에서 메시지 출력

ProductController.java
package kr.ac.hansung.cse.productcrud1.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.cse.productcrud1.entity.Product;
import kr.ac.hansung.cse.productcrud1.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

   private final ProductService service;

   public ProductController(ProductService service) {
        this.service = service;
    }

  @GetMapping({"", "/"})
    public String viewHomePage(Model model) {
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts", listProducts);
        return "index";
    }

   @GetMapping("/new")
    public String showNewProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "new_product";
    }

  @GetMapping("/edit/{id}")
    public String showEditProductPage(@PathVariable Long id, Model model) {
        Product product = service.get(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

  @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return (product.getId() == null) ? "new_product" : "edit_product";
        }
        service.save(product);
        return "redirect:/products";
    }

   @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/products";
    }
}
•  유효성 검증 실패 시 오류 메시지와 함께 다시 입력 폼으로 리턴
•  성공 시 상품 저장 후 리스트 페이지로 이동

new_product.html 
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">

<head th:insert="~{common :: commonHead}">
    <title>Create New Product</title>
</head>

<body>
<div class="container">
    <h1 class="mt-5 text-center">Create New Product</h1>
    <br />
    <form th:action="@{/products/save}" th:object="${product}" method="post">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="mb-3">
                    <label for="productName" class="form-label">Product Name:</label>
                    <input type="text" id="productName" class="form-control" th:field="*{name}" />
                    <div class="text-danger" th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></div>
                </div>
                <div class="mb-3">
                    <label for="brand" class="form-label">Brand:</label>
                    <input type="text" id="brand" class="form-control" th:field="*{brand}" />
                    <div class="text-danger" th:if="${#fields.hasErrors('brand')}" th:errors="*{brand}"></div>
                </div>
                <div class="mb-3">
                    <label for="madeIn" class="form-label">Made In:</label>
                    <input type="text" id="madeIn" class="form-control" th:field="*{madeIn}" />
                    <div class="text-danger" th:if="${#fields.hasErrors('madeIn')}" th:errors="*{madeIn}"></div>
                </div>
                <div class="mb-3">
                    <label for="price" class="form-label">Price:</label>
                    <input type="number" id="price" class="form-control" th:field="*{price}" />
                    <div class="text-danger" th:if="${#fields.hasErrors('price')}" th:errors="*{price}"></div>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
    </form>
</div>

<!-- Bootstrap JS 삽입 -->
<div th:insert="~{common :: commonScript}"></div>
</body>
</html>

edit_product.html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">

<head th:insert="~{common :: commonHead}">
  <title>Edit Product</title>
</head>

<body>
<div class="container">
  <h1 class="mt-5 text-center">Edit Product</h1>
  <br />

  <form th:action="@{/products/save}" th:object="${product}" method="post">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="mb-3">
          <label for="productId" class="form-label">Product ID:</label>
          <input type="text" id="productId" class="form-control" th:field="*{id}" readonly />
        </div>
        <div class="mb-3">
          <label for="productName" class="form-label">Product Name:</label>
          <input type="text" id="productName" class="form-control" th:field="*{name}" />
          <div class="text-danger" th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></div>
        </div>
        <div class="mb-3">
          <label for="brand" class="form-label">Brand:</label>
          <input type="text" id="brand" class="form-control" th:field="*{brand}" />
          <div class="text-danger" th:if="${#fields.hasErrors('brand')}" th:errors="*{brand}"></div>
        </div>
        <div class="mb-3">
          <label for="madeIn" class="form-label">Made In:</label>
          <input type="text" id="madeIn" class="form-control" th:field="*{madeIn}" />
          <div class="text-danger" th:if="${#fields.hasErrors('madeIn')}" th:errors="*{madeIn}"></div>
        </div>
        <div class="mb-3">
          <label for="price" class="form-label">Price:</label>
          <input type="number" id="price" class="form-control" th:field="*{price}" />
          <div class="text-danger" th:if="${#fields.hasErrors('price')}" th:errors="*{price}"></div>
        </div>
        <div class="text-center">
          <button type="submit" class="btn btn-primary">Save</button>
        </div>
      </div>
    </div>
  </form>
</div>

<!-- Bootstrap JS 로딩 -->
<div th:insert="~{common :: commonScript}"></div>
</body>
</html>
•  th:errors, #fields.hasErrors()로 유효성 메시지 출력
•  동일한 방식으로 name, brand, madeIn 필드에도 적용

AdminController.java
package kr.ac.hansung.cse.productcrud1.controller;

import kr.ac.hansung.cse.productcrud1.entity.User;
import kr.ac.hansung.cse.productcrud1.repo.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

   private final UserRepository userRepository;

   public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   @GetMapping("/users")
    public String userList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }
}

admin.html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:insert="~{common :: commonHead}">
    <title>Admin Dashboard</title>
</head>
<body>
<div class="container mt-5">
    <h2>관리자 페이지</h2>
    <h4 class="mt-4">전체 사용자 목록</h4>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>User ID</th>
            <th>Email</th>
            <th>Role</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="user : ${users}">
            <td th:text="${user.id}">1</td>
            <td th:text="${user.email}">admin@hansung.ac.kr</td>
            <td th:text="${user.role}">ROLE_ADMIN</td>
        </tr>
        </tbody>
    </table>
</div>
<div th:insert="~{common :: commonScript}"></div>
</body>
</html>
•	관리자만 접근 가능하며, 전체 사용자 정보(ID, 이메일, 권한)를 조회 가능


product_list.html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">

<head th:insert="~{common :: commonHead}">
    <title>Product Manager</title>
</head>

<body>
<div class="container">
    <h1 class="mt-5">Product List</h1>
    <!-- 관리자 전용 버튼: 상품 등록 / 관리자 페이지 진입 -->
    <div class="mb-3 d-flex justify-content-between">
        <a class="btn btn-primary"
           th:href="@{/products/new}"
           sec:authorize="hasRole('ROLE_ADMIN')">Create New Product</a>
        <a class="btn btn-secondary"
           th:href="@{/admin/users}"
           sec:authorize="hasRole('ROLE_ADMIN')">관리자 페이지</a>
    </div>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Product ID</th>
            <th>Name</th>
            <th>Brand</th>
            <th>Made In</th>
            <th>Price</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="product : ${listProducts}">
            <td th:text="${product.id}">Product ID</td>
            <td th:text="${product.name}">Name</td>
            <td th:text="${product.brand}">Brand</td>
            <td th:text="${product.madeIn}">Made in</td>
            <td th:text="${product.price}">Price</td>
            <td>
                <a class="btn btn-primary btn-sm"
                   th:href="@{'/products/edit/' + ${product.id}}"
                   sec:authorize="hasRole('ROLE_ADMIN')">Edit</a>
                <a class="btn btn-danger btn-sm"
                   th:href="@{'/products/delete/' + ${product.id}}"
                   sec:authorize="hasRole('ROLE_ADMIN')">Delete</a>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<!-- Bootstrap JS fragment 삽입 -->
<div th:insert="~{common :: commonScript}"></div>
</body>
</html>
•  sec:authorize로 일반 사용자는 버튼 자체를 보지 못함
•  보안 및 UX를 동시에 강화

application.properties
spring.application.name=productCRUD1

# context path:  http://localhost:8080/productCRUD1
server.servlet.context-path=/productCRUD1

# === DataSource ===
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.datasource.url=jdbc:mysql://localhost:3306/sales?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
spring.datasource.username=root
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# === SQL ì´ê¸°í (ê°ë° íë¡íì¼ ì ì©) ===
# executes initialization scripts(schema.sql, data.sql) every time the application is run
spring.sql.init.mode=always
# used to configure the encoding of initialization scripts
spring.sql.init.encoding= UTF-8

#  === JPA ===
# ì´ìì validate, ê°ë°ì create ëë update
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=false
# After the ddl-auto execution, data.sql is executed and the data is applied
spring.jpa.defer-datasource-initialization=true

# === Logging ===
logging.level.kr.ac.hansung=trace
•. application.properties: DB 연결, JPA 설정, 로그 출력 설정







4) 기능 구현 여부 및 자기 평가

① 사용자 인증 기능
•	회원가입 및 로그인 기능은 이메일과 비밀번호를 기반으로 정상적으로 구현되었습니다.
•	Spring Security의 formLogin 기능을 사용하여 인증 로직을 처리하였고, 로그인 성공 시 /products로 리다이렉션되며, 실패 시 사용자에게 "❗ Invalid email or password" 메시지가 출력되도록 구성했습니다.
•	비밀번호는 BCryptPasswordEncoder를 통해 안전하게 해시 처리되며, MySQL DB에는 암호화된 문자열 형태로 저장됩니다.
•	관리자 계정은 수동 SQL 삽입을 통해 생성되며, 해당 해시값은 BCryptPasswordEncoder로 사전 생성한 값을 직접 넣었습니다.
② 권한(Role) 기반 인가 기능
•	애플리케이션에는 ROLE_USER, ROLE_ADMIN 두 가지 권한이 존재하며, 회원가입 시 기본값은 ROLE_USER로 부여됩니다.
•	사용자 권한에 따라 접근 가능한 기능을 명확히 제한했습니다.
o	ROLE_USER는 상품 목록을 조회할 수만 있고, 상품 등록/수정/삭제 URL 접근은 차단됩니다.
o	ROLE_ADMIN은 상품 등록, 수정, 삭제 요청(/products/new, /products/edit/**, /products/delete/**)이 모두 허용됩니다.
•	UI 요소에서도 Thymeleaf sec:authorize와 #authorization.expression()을 활용하여 일반 사용자에게는 상품의 Edit, Delete 버튼이 보이지 않도록 처리했습니다.
o	단순히 접근 제어뿐 아니라 버튼 자체도 숨김 처리함으로써 UX 차원에서도 완성도를 높였습니다.
③ 추가 기능
•	로그인 성공/실패 시 사용자 맞춤 메시지:
o	param.error 및 param.logout을 통해 로그인 실패 및 로그아웃 성공 시 메시지가 정상적으로 표시되며, 사용자 피드백 경험을 개선했습니다.
•	상품 등록/수정 시 유효성 검사:
o	@Valid, BindingResult, 그리고 @NotBlank, @Min 등의 Bean Validation을 사용하여 서버 측 유효성 검사를 적용했습니다.
o	상품명, 브랜드, 제조국은 빈 값이 입력되지 않도록 하고, 가격은 0 이상이어야만 등록되도록 설정했으며, 유효성 실패 시 각 필드 아래에 빨간색 오류 메시지가 표시됩니다.
o	JSP/Thymeleaf에서 th:errors, #fields.hasErrors()를 통해 오류 메시지를 자연스럽게 표현했습니다.
•	관리자 전용 페이지 (/admin/users) 구현:
o	관리자만 접근 가능한 사용자 관리 페이지를 별도로 구현하였으며, 전체 사용자 목록(이메일, 권한 등)을 테이블 형태로 출력하도록 구성했습니다.
o	URL 접근 제어뿐만 아니라 UI 내에서도 해당 링크는 관리자에게만 표시되도록 설정했습니다.

과제 수행하면서 느낀 점
이번 과제를 수행하면서 Spring Security와 Spring MVC, JPA의 연동 방식에 대해 보다 깊이 이해할 수 있었습니다.
단순한 CRUD 기능을 넘어서 사용자 인증 및 권한 제어를 직접 구현하면서 실제 서비스 수준의 웹 애플리케이션을 만드는 경험을 쌓았습니다.
특히 어려웠던 점은 다음과 같습니다:
•	Spring Security 설정에서 권한별 접근 제어를 SecurityFilterChain으로 나누어 명확히 구성해야 했고,
•	Circular dependency 문제로 인해 UserService와 SecurityConfig 사이의 의존성을 분리해야 했으며,
•	유효성 검사 시 @Valid와 BindingResult를 연동해 오류 메시지를 동적으로 보여주는 로직 구현도 처음에는 쉽지 않았습니다.
하지만 모든 기능을 하나씩 완성해가며 Spring의 보안 설정과 백엔드 설계의 핵심 개념들을 실습으로 체득할 수 있었고,
실제 서비스에서 필수적인 보안 구조, 유효성 처리, 권한 UI 제어 등을 구현해보며 백엔드 개발자로서의 실력을 한 단계 끌어올릴 수 있는 좋은 기회가 되었다고 생각합니다.

