package dto;
import model.Stable;

public class StableResponseDTO {
    private int id;
    private String name;
    private int maxCapacity;
    private int horseCount;
    private double fillPercentage;

    public static StableResponseDTO from(Stable stable) {
        StableResponseDTO dto = new StableResponseDTO();
        dto.id = stable.getId();
        dto.name = stable.getName();
        dto.maxCapacity = stable.getMaxCapacity();
        dto.horseCount = stable.getHorses().size();
        dto.fillPercentage = stable.getFillPercentage();
        return dto;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getHorseCount() {
        return horseCount;
    }

    public double getFillPercentage() {
        return fillPercentage;
    }
}