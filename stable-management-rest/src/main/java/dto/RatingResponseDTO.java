package dto;

import model.Rating;

public class RatingResponseDTO {
    private int id;
    private int horseId;
    private int value;
    private String comment;


    public static RatingResponseDTO from(Rating rating) {
        RatingResponseDTO ratingResponseDTO = new RatingResponseDTO();
        ratingResponseDTO.id = rating.getId();
        ratingResponseDTO.horseId = rating.getHorse().getId();
        ratingResponseDTO.value = rating.getValue();
        ratingResponseDTO.comment = rating.getComment();
        return ratingResponseDTO;
    }

    public int getHorseId() {
        return horseId;
    }

    public int getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }
    public int getId() {
        return id;
    }
}
