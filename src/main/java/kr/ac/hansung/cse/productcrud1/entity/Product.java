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
