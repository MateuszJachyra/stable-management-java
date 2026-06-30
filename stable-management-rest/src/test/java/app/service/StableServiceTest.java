package app.service;

import app.dto.StableDTO;
import app.dto.StableResponseDTO;
import app.enumeration.HorseStatus;
import app.enumeration.HorseType;
import app.exception.HorseOperationException;
import app.exception.StableServiceOperationException;
import app.model.Horse;
import app.model.Stable;
import app.repository.HorseRepository;
import app.repository.StableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StableServiceTest {
    @Mock
    private StableRepository stableRepository;
    @Mock
    private HorseRepository horseRepository;

    @InjectMocks
    private StableService stableService;

    @Test
    void givenNegativeCapacity_whenCreateStable_ThrowsStableServiceOperationException() {
        String name = "name";
        int capacity = -1;

        assertThatThrownBy(() -> stableService.createStable(name, capacity))
                .isInstanceOf(StableServiceOperationException.class)
                .hasMessageContaining("Capacity must be greater than 0");
    }

    @Test
    void givenValidData_whenUpdateStable_ReturnsStableResponseDTO() {
        String name = "stable";
        int oldCapacity = 10;
        int newCapacity = 100;
        StableDTO updatedStableDTO = new StableDTO();
        updatedStableDTO.setName(name);
        updatedStableDTO.setCapacity(newCapacity);

        Stable existingStable = new Stable(name, oldCapacity);
        when(stableRepository.findByName(name)).thenReturn(Optional.of(existingStable));
        when(stableRepository.save(any(Stable.class))).thenReturn(existingStable);


        StableResponseDTO result = stableService.updateStable(name, updatedStableDTO);


        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getCapacity()).isEqualTo(newCapacity);
    }

    @Test
    void givenForceTrue_whenDeleteStable_DeletesStable() {
        String name = "stable";
        boolean force = true;
        Stable existingStable = new Stable(name, 10);
        List<Horse> horses = List.of(
                new Horse("a","a",HorseType.COLDBLOODED,
                        HorseStatus.HEALTHY,1,1,1),
                new Horse("b","b",HorseType.COLDBLOODED,
                        HorseStatus.HEALTHY,1,1,1));
        existingStable.addHorse(horses.get(0));
        existingStable.addHorse(horses.get(1));

        when(stableRepository.findByName(name)).thenReturn(Optional.of(existingStable));


        stableService.deleteStable(name, force);


        verify(horseRepository).deleteAll(horses);
        verify(stableRepository).delete(existingStable);
        verify(horseRepository, never()).findStableIdWithRatings(any());
    }

    @Test
    void givenForceFalseAndNonEmptyStable_whenDeleteStable_throwsStableServiceOperationsException() {
        String name = "stable";
        boolean force = false;
        Stable existingStable = new Stable(name, 10);
        existingStable.addHorse(new Horse("a","a",HorseType.COLDBLOODED,
                HorseStatus.HEALTHY,1,1,1));

        when(stableRepository.findByName(name)).thenReturn(Optional.of(existingStable));
        when(horseRepository.findStableIdWithRatings(existingStable.getId()))
                .thenReturn(existingStable.getHorses());

        assertThatThrownBy(() -> stableService.deleteStable(name,force))
                .isInstanceOf(StableServiceOperationException.class)
                .hasMessageContaining("Stable not empty");


        verify(stableRepository, never()).delete(any());
    }


    @Test
    void givenTooLowValue_whenAddRating_ThrowsHorseOperationException() {
        int horseId = 1;
        int value = -1;


        assertThatThrownBy(() -> stableService.addRating(horseId,value, null))
                .isInstanceOf(HorseOperationException.class)
                .hasMessageContaining("Invalid rating");


        verify(horseRepository, never()).save(any());
    }

    @Test
    void givenTooHighValue_whenAddRating_ThrowsHorseOperationException() {
        int horseId = 1;
        int value = 100;


        assertThatThrownBy(() -> stableService.addRating(horseId,value, null))
                .isInstanceOf(HorseOperationException.class)
                .hasMessageContaining("Invalid rating");


        verify(horseRepository, never()).save(any());
    }

    @Test
    void givenValidData_whenAddRating_ReturnsRatingDTO() {
        int horseId = 1;
        Horse horse = new Horse("a","a",HorseType.COLDBLOODED,
                HorseStatus.HEALTHY,1,1,1);
        int value = 3;
        when(horseRepository.findById(horseId)).thenReturn(Optional.of(horse));
        when(horseRepository.save(horse)).thenReturn(horse);


        stableService.addRating(horseId,value, null);


        verify(horseRepository, times(1)).save(horse);
    }
}
