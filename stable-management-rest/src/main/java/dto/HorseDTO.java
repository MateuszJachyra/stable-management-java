package dto;

import enumeration.HorseStatus;
import enumeration.HorseType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HorseDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String breed;
    @NotNull
    private HorseType type;
    @NotNull
    private HorseStatus status;
    @Min(0)
    private int age;
    @Min(0)
    private double price;
    @Min(0)
    private double weight;
    @NotBlank
    private String stableName;

    public HorseDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public HorseType getType() {
        return type;
    }

    public void setType(HorseType type) {
        this.type = type;
    }

    public HorseStatus getStatus() {
        return status;
    }

    public void setStatus(HorseStatus status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getStableName() {
        return stableName;
    }

    public void setStableName(String stableName) {
        this.stableName = stableName;
    }
}