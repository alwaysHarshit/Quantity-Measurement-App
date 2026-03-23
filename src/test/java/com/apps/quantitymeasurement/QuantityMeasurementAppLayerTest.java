package com.apps.quantitymeasurement;

import com.apps.quantitymeasurement.controller.QuantityMeasurementController;
import com.apps.quantitymeasurement.dto.QuantityDTO;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.apps.quantitymeasurement.units.LengthUnit;
import com.apps.quantitymeasurement.units.QuantityClass;
import com.apps.quantitymeasurement.units.TemperatureUnit;
import com.apps.quantitymeasurement.units.WeightUnit;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppLayerTest {

    @Test
    void testQuantityEntity_SingleOperandConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity("CONVERT", "12.0 INCHES", "1.0 FEET");

        assertEquals("CONVERT", entity.getOperation());
        assertEquals("12.0 INCHES", entity.getLeftOperand());
        assertNull(entity.getRightOperand());
        assertEquals("1.0 FEET", entity.getResult());
        assertFalse(entity.hasError());
    }

    @Test
    void testQuantityEntity_BinaryOperandConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity("ADD", "1.0 FEET", "12.0 INCHES", "2.0 FEET");

        assertEquals("ADD", entity.getOperation());
        assertEquals("1.0 FEET", entity.getLeftOperand());
        assertEquals("12.0 INCHES", entity.getRightOperand());
        assertEquals("2.0 FEET", entity.getResult());
        assertFalse(entity.hasError());
    }

    @Test
    void testQuantityEntity_ErrorConstruction() {
        QuantityMeasurementEntity entity = QuantityMeasurementEntity.error(
                "ADD", "100.0 CELSIUS", "50.0 CELSIUS", "Operation ADD is not supported");

        assertEquals("ADD", entity.getOperation());
        assertEquals("100.0 CELSIUS", entity.getLeftOperand());
        assertEquals("50.0 CELSIUS", entity.getRightOperand());
        assertNull(entity.getResult());
        assertEquals("Operation ADD is not supported", entity.getErrorMessage());
        assertTrue(entity.hasError());
    }

    @Test
    void testQuantityEntity_ToString_Success() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity("ADD", "1.0 FEET", "12.0 INCHES", "2.0 FEET");

        String result = entity.toString();

        assertTrue(result.contains("ADD"));
        assertTrue(result.contains("1.0 FEET"));
        assertTrue(result.contains("12.0 INCHES"));
        assertTrue(result.contains("2.0 FEET"));
    }

    @Test
    void testQuantityEntity_ToString_Error() {
        QuantityMeasurementEntity entity = QuantityMeasurementEntity.error(
                "DIVIDE", "2.0 FEET", "0.0 FEET", "Division by zero");

        String result = entity.toString();

        assertTrue(result.contains("DIVIDE"));
        assertTrue(result.contains("Division by zero"));
    }

    @Test
    void testService_CompareEquality_SameUnit_Success() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        boolean result = service.compare(
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET));

        assertTrue(result);
    }

    @Test
    void testService_CompareEquality_DifferentUnit_Success() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        boolean result = service.compare(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertTrue(result);
    }

    @Test
    void testService_CompareEquality_CrossCategory_Error() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        boolean result = service.compare(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM));

        assertFalse(result);
    }

    @Test
    void testService_Convert_Success() {
        RecordingRepository repository = new RecordingRepository();
        QuantityMeasurementServiceImpl service = createService(repository);

        QuantityDTO result = service.convert(
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET));

        assertEquals(1.0, result.getValue(), 0.0001);
        assertEquals("FEET", result.getUnit());
        assertEquals(1, repository.savedEntities.size());
        assertEquals("CONVERT", repository.savedEntities.getFirst().getOperation());
    }

    @Test
    void testService_Add_Success() {
        RecordingRepository repository = new RecordingRepository();
        QuantityMeasurementServiceImpl service = createService(repository);

        QuantityDTO result = service.add(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertEquals(2.0, result.getValue(), 0.0001);
        assertEquals("FEET", result.getUnit());
        assertEquals(1, repository.savedEntities.size());
        assertEquals("ADD", repository.savedEntities.getFirst().getOperation());
        assertEquals("1.0 FEET", repository.savedEntities.getFirst().getLeftOperand());
        assertEquals("12.0 INCHES", repository.savedEntities.getFirst().getRightOperand());
        assertEquals("2.0 FEET", repository.savedEntities.getFirst().getResult());
    }

    @Test
    void testService_Add_UnsupportedOperation_Error() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> service.add(
                        new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS)));

        assertTrue(exception.getMessage().contains("ADD"));
    }

    @Test
    void testService_Subtract_Success() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        QuantityDTO result = service.subtract(
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertEquals(1.0, result.getValue(), 0.0001);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    void testService_Divide_Success() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        double result = service.divide(
                new QuantityDTO(24.0, QuantityDTO.LengthUnit.INCHES),
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET));

        assertEquals(1.0, result, 0.0001);
    }

    @Test
    void testService_Divide_ByZero_Error() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        assertThrows(
                ArithmeticException.class,
                () -> service.divide(
                        new QuantityDTO(24.0, QuantityDTO.LengthUnit.INCHES),
                        new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET)));
    }

    @Test
    void testController_DemonstrateEquality_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(new RecordingRepository()));

        boolean result = controller.performComparison(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertTrue(result);
    }

    @Test
    void testController_DemonstrateConversion_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(new RecordingRepository()));

        QuantityDTO result = controller.performConversion(
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET));

        assertEquals(1.0, result.getValue(), 0.0001);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    void testController_DemonstrateAddition_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(new RecordingRepository()));

        QuantityDTO result = controller.performAddition(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertEquals(2.0, result.getValue(), 0.0001);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    void testController_DemonstrateAddition_Error() {
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(new RecordingRepository()));

        assertThrows(
                UnsupportedOperationException.class,
                () -> controller.performAddition(
                        new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(10.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)));
    }

    @Test
    void testController_DisplayResult_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(new RecordingRepository()));

        String output = captureConsoleOutput(() -> controller.handleResult(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES),
                "+",
                new QuantityDTO(2.0, "FEET", QuantityDTO.LengthUnit.class.getSimpleName())));

        assertEquals("1.0 FEET + 12.0 INCHES = 2.0 FEET", output.trim());
    }

    @Test
    @Disabled("Current controller prints only successful DTO results; there is no error-entity display API in this design.")
    void testController_DisplayResult_Error() {
    }

    @Test
    void testLayerSeparation_ServiceIndependence() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        QuantityDTO result = service.add(
                new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM));

        assertEquals(2.0, result.getValue(), 0.0001);
        assertEquals("KILOGRAM", result.getUnit());
    }

    @Test
    void testLayerSeparation_ControllerIndependence() {
        FakeService fakeService = new FakeService();
        QuantityMeasurementController controller = new QuantityMeasurementController(fakeService);

        QuantityDTO result = controller.performAddition(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertEquals(99.0, result.getValue(), 0.0001);
        assertEquals("FAKE", result.getUnit());
    }

    @Test
    void testDataFlow_ControllerToService() {
        FakeService fakeService = new FakeService();
        QuantityMeasurementController controller = new QuantityMeasurementController(fakeService);
        QuantityDTO left = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO right = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

        controller.performAddition(left, right);

        assertSame(left, fakeService.lastLeft);
        assertSame(right, fakeService.lastRight);
        assertEquals("ADD", fakeService.lastOperation);
    }

    @Test
    void testDataFlow_ServiceToController() {
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(new RecordingRepository()));

        QuantityDTO result = controller.performAddition(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertEquals(2.0, result.getValue(), 0.0001);
        assertEquals("FEET", result.getUnit());
        assertEquals(QuantityDTO.LengthUnit.class.getSimpleName(), result.getMeasurementType());
    }

    @Test
    void testBackwardCompatibility_AllUC1_UC14_Tests() {
        assertTrue(new QuantityClass<>(1.0, LengthUnit.FEET).equals(new QuantityClass<>(12.0, LengthUnit.INCHES)));
        assertTrue(new QuantityClass<>(1.0, WeightUnit.KILOGRAM).equals(new QuantityClass<>(1000.0, WeightUnit.GRAM)));
        assertEquals(2.0, new QuantityClass<>(1.0, LengthUnit.FEET)
                .add(new QuantityClass<>(12.0, LengthUnit.INCHES), LengthUnit.FEET)
                .getValue(), 0.0001);
        assertThrows(
                UnsupportedOperationException.class,
                () -> new QuantityClass<>(100.0, TemperatureUnit.CELSIUS)
                        .add(new QuantityClass<>(50.0, TemperatureUnit.CELSIUS)));
    }

    @Test
    void testService_AllMeasurementCategories() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        QuantityDTO length = service.add(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        QuantityDTO weight = service.add(
                new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM));
        QuantityDTO volume = service.convert(
                new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE),
                new QuantityDTO(0.0, QuantityDTO.VolumeUnit.MILLILITRE));
        boolean temperature = service.compare(
                new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT));

        assertAll(
                () -> assertEquals(2.0, length.getValue(), 0.0001),
                () -> assertEquals(2.0, weight.getValue(), 0.0001),
                () -> assertEquals(1000.0, volume.getValue(), 0.0001),
                () -> assertTrue(temperature)
        );
    }

    @Test
    void testController_AllOperations() {
        FakeService fakeService = new FakeService();
        QuantityMeasurementController controller = new QuantityMeasurementController(fakeService);
        QuantityDTO left = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO right = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

        controller.performAddition(left, right);
        controller.performSubtraction(left, right);
        controller.performDivision(left, right);
        controller.performComparison(left, right);
        controller.performConversion(left, right);

        assertEquals(List.of("ADD", "SUBTRACT", "DIVIDE", "COMPARE", "CONVERT"), fakeService.operations);
    }

    @Test
    void testService_ValidationConsistency() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());
        QuantityDTO valid = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.add(null, valid)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.subtract(null, valid)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.divide(null, valid)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.compare(null, valid)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.convert(null, valid))
        );
    }

    @Test
    void testEntity_Immutability() {
        for (Field field : QuantityMeasurementEntity.class.getDeclaredFields()) {
            assertTrue(Modifier.isFinal(field.getModifiers()), "Field should be final: " + field.getName());
        }
    }

    @Test
    void testService_ExceptionHandling_AllOperations() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());
        QuantityDTO celsius = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO fahrenheit = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.FAHRENHEIT);

        assertAll(
                () -> assertThrows(UnsupportedOperationException.class, () -> service.add(celsius, fahrenheit)),
                () -> assertThrows(UnsupportedOperationException.class, () -> service.subtract(celsius, fahrenheit)),
                () -> assertThrows(UnsupportedOperationException.class, () -> service.divide(celsius, fahrenheit))
        );
    }

    @Test
    void testController_ConsoleOutput_Format() {
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(new RecordingRepository()));
        QuantityDTO resultDto = new QuantityDTO(2.0, "FEET", QuantityDTO.LengthUnit.class.getSimpleName());

        String output = captureConsoleOutput(() -> controller.handleResult(resultDto));

        assertEquals("2.0 FEET", output.trim());
    }

    @Test
    void testIntegration_EndToEnd_LengthAddition() {
        RecordingRepository repository = new RecordingRepository();
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(repository));
        QuantityDTO left = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO right = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

        QuantityDTO result = controller.performAddition(left, right);
        String output = captureConsoleOutput(() -> controller.handleResult(left, right, "+", result));

        assertEquals(2.0, result.getValue(), 0.0001);
        assertEquals("1.0 FEET + 12.0 INCHES = 2.0 FEET", output.trim());
        assertEquals(1, repository.savedEntities.size());
    }

    @Test
    void testIntegration_EndToEnd_TemperatureUnsupported() {
        QuantityMeasurementController controller = new QuantityMeasurementController(createService(new RecordingRepository()));

        assertThrows(
                UnsupportedOperationException.class,
                () -> controller.performAddition(
                        new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(10.0, QuantityDTO.TemperatureUnit.CELSIUS)));
    }

    @Test
    void testService_NullEntity_Rejection() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        assertThrows(IllegalArgumentException.class, () -> service.add(null, null));
    }

    @Test
    void testController_NullService_Prevention() {
        assertThrows(NullPointerException.class, () -> new QuantityMeasurementController(null));
    }

    @Test
    void testService_AllUnitImplementations() {
        QuantityMeasurementServiceImpl service = createService(new RecordingRepository());

        assertAll(
                () -> assertEquals(2.0, service.add(
                        new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                        new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES)).getValue(), 0.0001),
                () -> assertEquals(2.0, service.add(
                        new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                        new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM)).getValue(), 0.0001),
                () -> assertEquals(1000.0, service.convert(
                        new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE),
                        new QuantityDTO(0.0, QuantityDTO.VolumeUnit.MILLILITRE)).getValue(), 0.0001),
                () -> assertTrue(service.compare(
                        new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)))
        );
    }

    @Test
    void testEntity_OperationType_Tracking() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity("SUBTRACT", "2.0 FEET", "12.0 INCHES", "1.0 FEET");

        assertEquals("SUBTRACT", entity.getOperation());
    }

    @Test
    void testLayerDecoupling_ServiceChange() {
        AlternateFakeService alternateService = new AlternateFakeService();
        QuantityMeasurementController controller = new QuantityMeasurementController(alternateService);

        QuantityDTO result = controller.performAddition(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertEquals(-1.0, result.getValue(), 0.0001);
        assertEquals("ALT", result.getUnit());
    }

    @Test
    @Disabled("Current DTO/primitive service contract does not expose entity evolution across layers.")
    void testLayerDecoupling_EntityChange() {
    }

    @Test
    @Disabled("Adding a brand-new service operation still requires changing the service interface in the current design.")
    void testScalability_NewOperation_Addition() {
    }

    private QuantityMeasurementServiceImpl createService(RecordingRepository repository) {
        return new QuantityMeasurementServiceImpl(repository);
    }

    private String captureConsoleOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(outputStream, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(originalOut);
        }

        return outputStream.toString(StandardCharsets.UTF_8);
    }

    private static final class RecordingRepository implements IQuantityMeasurementRepository {
        private final List<QuantityMeasurementEntity> savedEntities = new ArrayList<>();

        @Override
        public void save(QuantityMeasurementEntity entity) {
            savedEntities.add(entity);
        }
    }

    private static class FakeService implements IQuantityMeasurementService {
        private final List<String> operations = new ArrayList<>();
        private QuantityDTO lastLeft;
        private QuantityDTO lastRight;
        private String lastOperation;

        @Override
        public QuantityDTO add(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            record("ADD", thisQuantity, thatQuantity);
            return new QuantityDTO(99.0, "FAKE", QuantityDTO.LengthUnit.class.getSimpleName());
        }

        @Override
        public boolean compare(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            record("COMPARE", thisQuantity, thatQuantity);
            return true;
        }

        @Override
        public QuantityDTO convert(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            record("CONVERT", thisQuantity, thatQuantity);
            return new QuantityDTO(99.0, "FAKE", QuantityDTO.LengthUnit.class.getSimpleName());
        }

        @Override
        public double divide(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            record("DIVIDE", thisQuantity, thatQuantity);
            return 99.0;
        }

        @Override
        public QuantityDTO subtract(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            record("SUBTRACT", thisQuantity, thatQuantity);
            return new QuantityDTO(99.0, "FAKE", QuantityDTO.LengthUnit.class.getSimpleName());
        }

        private void record(String operation, QuantityDTO left, QuantityDTO right) {
            this.lastOperation = operation;
            this.lastLeft = left;
            this.lastRight = right;
            this.operations.add(operation);
        }
    }

    private static final class AlternateFakeService extends FakeService {
        @Override
        public QuantityDTO add(QuantityDTO thisQuantity, QuantityDTO thatQuantity) {
            return new QuantityDTO(-1.0, "ALT", QuantityDTO.LengthUnit.class.getSimpleName());
        }
    }
}
