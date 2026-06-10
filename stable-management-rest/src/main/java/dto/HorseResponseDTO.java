package dto;

import model.Horse;

public class HorseResponseDTO {
    private int id;
    private String name;
    private String breed;
    private String type;
    private String status;
    private int age;
    private double price;
    private double weight;
    private String stableName;
    private double averageRating;

    public static HorseResponseDTO from(Horse horse) {
        HorseResponseDTO dto = new HorseResponseDTO();
        dto.id = horse.getId();
        dto.name = horse.getName();
        dto.breed = horse.getBreed();
        dto.type = horse.getType().name();
        dto.status = horse.getStatus().name();
        dto.age = horse.getAge();
        dto.price = horse.getPrice();
        dto.weight = horse.getWeight();
        dto.stableName = horse.getStable() != null ? horse.getStable().getName() : null;
        dto.averageRating = horse.getAverageRating();
        return dto;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public int getAge() {
        return age;
    }

    public double getPrice() {
        return price;
    }

    public double getWeight() {
        return weight;
    }

    public String getStableName() {
        return stableName;
    }

    public double getAverageRating() {
        return averageRating;
    }
}