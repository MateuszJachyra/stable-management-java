package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StableDTO {
    @NotBlank(message = "Stable name is required")
    private String name;
    @Min(value = 1, message = "Capacity must be at least 1")

    private int capacity;

    public StableDTO() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}