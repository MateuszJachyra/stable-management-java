package app.model;

import app.exception.StableOperationException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="stables")
public class Stable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="stable_id", unique = true, nullable = false)
    private int id;
    @Column(name="name", unique = true, nullable = false)
    private String name;
    @Column(name="max_capacity", nullable = false)
    private int maxCapacity;
    @OneToMany(mappedBy="stable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Horse> horses = new ArrayList<>();

    public Stable(String name, int maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    protected Stable() {}

    @Override
    public String toString() {
        return "Name: " + name + "\nOccupation: " + horses.size() + '/' + maxCapacity
                + " horses\n"+getFillPercentage()+"% filled";
    }

    public Boolean isFull() {
        return horses.size() >= maxCapacity;
    }

    public double getFillPercentage() {
        return ((double)horses.size() / (double)maxCapacity) * 100;
    }

    public int addHorse(Horse horse) throws StableOperationException {
        if (!isFull()) {
            horses.add(horse);
            return horses.get(horses.size() - 1).getId();
        }
        else {
            throw new StableOperationException("Stable is full");
        }
    }

    public int getId() {
        return id;
    }

    public void deleteHorse(long id) {
        horses.removeIf(horse -> horse.getId() == id);
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public List<Horse> getHorses() {
        return horses;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
