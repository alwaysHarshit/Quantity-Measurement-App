package com.apps.quantitymeasurement.service;


import com.apps.quantitymeasurement.utils.IMeasurable;
import com.apps.quantitymeasurement.utils.LengthUnit;
import com.apps.quantitymeasurement.utils.QuantityClass;
import com.apps.quantitymeasurement.utils.TemperatureUnit;
import com.apps.quantitymeasurement.utils.VolumeUnit;
import com.apps.quantitymeasurement.utils.WeightUnit;
import com.apps.quantitymeasurement.dto.QuantityDTO;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {
    private static final String LENGTH_TYPE = QuantityDTO.LengthUnit.class.getSimpleName();
    private static final String WEIGHT_TYPE = QuantityDTO.WeightUnit.class.getSimpleName();
    private static final String TEMPERATURE_TYPE = QuantityDTO.TemperatureUnit.class.getSimpleName();
    private static final String VOLUME_TYPE = QuantityDTO.VolumeUnit.class.getSimpleName();

    private final IQuantityMeasurementRepository repository;
    private final Map<String, MeasurementAdapter<? extends IMeasurable>> measurementAdapters;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository iQuantityMeasurementRepository) {
        this.repository = Objects.requireNonNull(iQuantityMeasurementRepository, "Repository cannot be null");
        this.measurementAdapters = Map.of(
                LENGTH_TYPE, new MeasurementAdapter<>(this::toLengthUnit),
                WEIGHT_TYPE, new MeasurementAdapter<>(this::toWeightUnit),
                TEMPERATURE_TYPE, new MeasurementAdapter<>(this::toTemperatureUnit),
                VOLUME_TYPE, new MeasurementAdapter<>(this::toVolumeUnit)
        );
    }

    @Override
    public QuantityDTO add(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return resolveAdapter(thisQuantity, thatQuantity, true).add(thisQuantity, thatQuantity);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return resolveAdapter(thisQuantity, thatQuantity, true).subtract(thisQuantity, thatQuantity);
    }

    @Override
    public double divide(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return resolveAdapter(thisQuantity, thatQuantity, true).divide(thisQuantity, thatQuantity);
    }

    @Override
    public boolean compare(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        validateQuantityInputs(thisQuantity, thatQuantity);
        if (!isSameMeasurementType(thisQuantity, thatQuantity)) {
            return false;
        }
        return resolveAdapter(thisQuantity, thatQuantity, false).compare(thisQuantity, thatQuantity);
    }

    @Override
    public QuantityDTO convert(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return resolveAdapter(thisQuantity, thatQuantity, true).convert(thisQuantity, thatQuantity);
    }

    private void validateQuantityInputs(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        if (thisQuantity == null || thatQuantity == null) {
            throw new IllegalArgumentException("Operands cannot be null");
        }

        if (!Double.isFinite(thisQuantity.getValue()) || !Double.isFinite(thatQuantity.getValue())) {
            throw new IllegalArgumentException("Operands must be finite");
        }
    }

    private MeasurementAdapter<? extends IMeasurable> resolveAdapter(
            QuantityDTO thisQuantity,
            QuantityDTO thatQuantity,
            boolean sameCategoryRequired) {
        validateQuantityInputs(thisQuantity, thatQuantity);

        if (sameCategoryRequired && !isSameMeasurementType(thisQuantity, thatQuantity)) {
            throw new IllegalArgumentException("Unit categories must match");
        }

        MeasurementAdapter<? extends IMeasurable> measurementAdapter =
                measurementAdapters.get(normalizeType(thisQuantity.getMeasurmentType()));

        if (measurementAdapter == null) {
            throw new IllegalArgumentException("Unsupported measurement type: " + thisQuantity.getMeasurmentType());
        }

        return measurementAdapter;
    }

    private boolean isSameMeasurementType(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
        return normalizeType(thisQuantity.getMeasurmentType()).equals(normalizeType(thatQuantity.getMeasurmentType()));
    }

    private <U extends IMeasurable> QuantityClass<U> createQuantity(QuantityDTO quantityDTO, U unit) {
        return new QuantityClass<>(quantityDTO.getValue(), unit);
    }

    private <U extends IMeasurable> QuantityDTO buildAndStoreResult(
            String operation,
            QuantityDTO thisQuantity,
            QuantityDTO thatQuantity,
            QuantityClass<U> result) {
        QuantityDTO resultDto = new QuantityDTO(
                result.getValue(),
                result.getUnit().getUnitName(),
                normalizeType(thisQuantity.getMeasurmentType()));

        repository.save(new QuantityMeasurementEntity(
                operation,
                formatDto(thisQuantity),
                formatDto(thatQuantity),
                formatDto(resultDto)));

        return resultDto;
    }

    private LengthUnit toLengthUnit(String unit) {
        return switch (normalizeUnit(unit)) {
            case "FEET", "FEETS" -> LengthUnit.FEET;
            case "INCHES", "INCHS" -> LengthUnit.INCHES;
            case "YARDS" -> LengthUnit.YARDS;
            case "CM", "CENTIMETERS" -> LengthUnit.CM;
            default -> throw new IllegalArgumentException("Unsupported length unit: " + unit);
        };
    }

    private WeightUnit toWeightUnit(String unit) {
        return switch (normalizeUnit(unit)) {
            case "KILOGRAM" -> WeightUnit.KILOGRAM;
            case "GRAM" -> WeightUnit.GRAM;
            case "POUND" -> WeightUnit.POUND;
            default -> throw new IllegalArgumentException("Unsupported weight unit: " + unit);
        };
    }

    private VolumeUnit toVolumeUnit(String unit) {
        return switch (normalizeUnit(unit)) {
            case "LITRE" -> VolumeUnit.LITRE;
            case "MILLILITRE" -> VolumeUnit.MILLILITRE;
            case "GALLON" -> VolumeUnit.GALLON;
            default -> throw new IllegalArgumentException("Unsupported volume unit: " + unit);
        };
    }

    private TemperatureUnit toTemperatureUnit(String unit) {
        return switch (normalizeUnit(unit)) {
            case "CELSIUS" -> TemperatureUnit.CELSIUS;
            case "FAHRENHEIT" -> TemperatureUnit.FAHRENHEIT;
            default -> throw new IllegalArgumentException("Unsupported temperature unit in current domain model: " + unit);
        };
    }

    private String normalizeType(String measurementType) {
        return measurementType == null ? "" : measurementType.trim();
    }

    private String normalizeUnit(String unit) {
        return unit == null ? "" : unit.trim().toUpperCase();
    }

    private String formatDto(QuantityDTO quantityDTO) {
        return quantityDTO.getValue() + " " + quantityDTO.getUnit();
    }

    // Each adapter reuses the same old QuantityClass logic and only varies by unit resolver.
    private final class MeasurementAdapter<U extends IMeasurable> {
        private final Function<String, U> unitResolver;

        private MeasurementAdapter(Function<String, U> unitResolver) {
            this.unitResolver = unitResolver;
        }

        private QuantityDTO add(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            QuantityClass<U> quantity1 = toQuantity(thisQuantity);
            QuantityClass<U> quantity2 = toQuantity(thatQuantity);
            QuantityClass<U> result = quantity1.add(quantity2, quantity1.getUnit());
            return buildAndStoreResult("ADD", thisQuantity, thatQuantity, result);
        }

        private QuantityDTO subtract(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            QuantityClass<U> quantity1 = toQuantity(thisQuantity);
            QuantityClass<U> quantity2 = toQuantity(thatQuantity);
            QuantityClass<U> result = quantity1.subtract(quantity2, quantity1.getUnit());
            return buildAndStoreResult("SUBTRACT", thisQuantity, thatQuantity, result);
        }

        private double divide(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            QuantityClass<U> quantity1 = toQuantity(thisQuantity);
            QuantityClass<U> quantity2 = toQuantity(thatQuantity);

            quantity1.getUnit().validateOperationSupport("DIVIDE");

            double base1 = quantity1.getUnit().convertToBaseUnit(quantity1.getValue());
            double base2 = quantity2.getUnit().convertToBaseUnit(quantity2.getValue());

            if (base2 == 0.0) {
                throw new ArithmeticException("Division by zero");
            }

            return base1 / base2;
        }

        private boolean compare(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            return toQuantity(thisQuantity).equals(toQuantity(thatQuantity));
        }

        private QuantityDTO convert(QuantityDTO thisQuantity, QuantityDTO targetQuantity) {
            QuantityClass<U> source = toQuantity(thisQuantity);
            QuantityClass<U> result = source.convertTo(unitResolver.apply(targetQuantity.getUnit()));
            return buildAndStoreResult("CONVERT", thisQuantity, targetQuantity, result);
        }

        private QuantityClass<U> toQuantity(QuantityDTO quantityDTO) {
            return createQuantity(quantityDTO, unitResolver.apply(quantityDTO.getUnit()));
        }
    }
}
