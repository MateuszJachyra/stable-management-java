package service;

import enumeration.HorseStatus;
import enumeration.HorseType;
import exception.CsvServiceException;
import exception.StableServiceOperationException;
import model.Horse;

import java.io.*;

public class CsvService {
    public void exportStableToCsv(StableService stableService, String stableName) throws CsvServiceException {
        try(FileWriter fw = new FileWriter(stableName+".csv")) {
            fw.write(stableName+"\n"
                    +stableService.getStable(stableName).getMaxCapacity()+"\n");
            for(Horse horse : stableService.getHorsesSorted(stableName)) {
                fw.write(horse.getName()+";"+horse.getBreed()+";"+horse.getType()
                        +";"+horse.getStatus()+";"+horse.getAge()+";"+horse.getPrice()+";"+horse.getWeight()+";"+horse.getRatings()+"\n");
            }
        }
        catch (IOException | StableServiceOperationException e) {
            throw new CsvServiceException("Failed to export");
        }
    }

    public void importStableFromCsv(StableService stableService, String fileName) throws CsvServiceException {
        String stableName;
        int capacity;

        int horseId;
        String horseName;
        String horseBreed;
        HorseType horseType;
        HorseStatus horseStatus;
        int horseAge;
        double horsePrice;
        double horseWeight;

        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            if((stableName = br.readLine()) != null && (line = br.readLine()) != null) {
                capacity = Integer.parseInt(line);
                stableService.createStable(stableName, capacity);
            }
            while((line = br.readLine()) != null) {
                String[] fields = line.split(";");
                horseName = fields[0];
                horseBreed = fields[1];
                horseType = HorseType.valueOf(fields[2]);
                horseStatus = HorseStatus.valueOf(fields[3]);
                horseAge = Integer.parseInt(fields[4]);
                horsePrice = Double.parseDouble(fields[5]);
                horseWeight = Double.parseDouble(fields[6]);

                Horse horse = new Horse(horseName,horseBreed,horseType,horseStatus,horseAge,horsePrice,horseWeight);

                String ratings = fields[7].replace("[","").replace("]","");
                if(!ratings.isEmpty()) {
                    String[] ratingArray = ratings.split(",");
                    for (String rating : ratingArray) {
                        horse.addRating(Integer.parseInt(rating.trim()));
                    }
                }
                stableService.addHorse(stableName,horse);

            }
        }
        catch (IOException e) {
            throw new CsvServiceException("Failed to import");
        }
    }
}
