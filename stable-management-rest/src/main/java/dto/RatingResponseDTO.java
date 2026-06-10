package dto;

import model.Rating;

public class RatingResponseDTO {
    private int horseId;
    private int rating;


    public static RatingResponseDTO from(Rating rating) {
        RatingResponseDTO ratingResponseDTO = new RatingResponseDTO();
        ratingResponseDTO.horseId = rating.getHorse().getId();
        ratingResponseDTO.rating = rating.getRating();
        return ratingResponseDTO;
    }

    public int getHorseId() {
        return horseId;
    }

    public int getRating() {
        return rating;
    }
}
