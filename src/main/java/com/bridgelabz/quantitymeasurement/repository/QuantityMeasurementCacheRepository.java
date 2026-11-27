
package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UC15: Singleton repository with in-memory caching
 * and serialized history persistence.
 */
public final class QuantityMeasurementCacheRepository
        implements IQuantityMeasurementRepository {

    private static final String FILE_NAME =
            "quantity-measurement-history.ser";

    private static final QuantityMeasurementCacheRepository INSTANCE =
            new QuantityMeasurementCacheRepository();

    private final List<QuantityMeasurementEntity> measurements =
            new ArrayList<>();

    private QuantityMeasurementCacheRepository() {
        loadFromDisk();
    }

    public static QuantityMeasurementCacheRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void save(QuantityMeasurementEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Measurement entity cannot be null");
        }

        measurements.add(entity);
        saveToDisk();
    }

    @Override
    public synchronized List<QuantityMeasurementEntity>
    getAllMeasurements() {

        return new ArrayList<>(measurements);
    }

    /**
     * Saves the complete history to disk.
     */
    private void saveToDisk() {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME))) {

            output.writeObject(measurements);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to save measurement history",
                    exception);
        }
    }

    /**
     * Restores previously saved measurement history.
     */
    private void loadFromDisk() {

        Path path = Paths.get(FILE_NAME);

        if (!Files.exists(path)) {
            return;
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(FILE_NAME))) {

            Object stored = input.readObject();

            if (!(stored instanceof List<?> list)) {
                throw new IllegalStateException(
                        "Invalid measurement history format");
            }

            for (Object item : list) {

                if (!(item instanceof QuantityMeasurementEntity)) {
                    throw new IllegalStateException(
                            "Invalid measurement history entry");
                }

                measurements.add(
                        (QuantityMeasurementEntity) item);
            }

        } catch (IOException | ClassNotFoundException exception) {
            throw new IllegalStateException(
                    "Unable to load measurement history",
                    exception);
        }
    }
}
