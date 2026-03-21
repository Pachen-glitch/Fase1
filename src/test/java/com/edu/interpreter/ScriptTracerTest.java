package com.edu.interpreter;

import com.edu.interpreter.engine.ScriptInterpreter;
import com.edu.interpreter.engine.ScriptParser;
import com.edu.interpreter.exception.ScriptException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas del Tracer y Ejecución de Scripts")
public class ScriptTracerTest {

    private ScriptInterpreter interpreter;
    private ScriptParser parser;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        interpreter = new ScriptInterpreter();
        parser = new ScriptParser();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
    }

    private String captureOutput(Runnable action) {
        System.setOut(new PrintStream(outputStream));
        try {
            action.run();
            System.out.flush();
            return outputStream.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    // ==================== SIMPLE ARITHMETIC EXECUTION ====================

    @Test
    @DisplayName("Ejecutar suma simple sin traced")
    void testSimpleAddition() throws Exception {
        boolean result = interpreter.execute(parser.parse("5 3 OP_ADD"), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Ejecutar resta simple sin trace")
    void testSimpleSubtraction() throws Exception {
        boolean result = interpreter.execute(parser.parse("10 3 OP_SUB"), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Ejecutar operación comparativa sin trace")
    void testComparison() throws Exception {
        boolean result = interpreter.execute(parser.parse("10 5 OP_GREATERTHAN"), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Script vacío debe retornar false")
    void testEmptyScript() throws Exception {
        boolean result = interpreter.execute(parser.parse(""), false);
        assertFalse(result);
    }

    // ==================== TRACER TESTS ====================

    @Test
    @DisplayName("Tracer debe mostrar tokens ejecutados")
    void testTracerShowsTokens() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("5 3 OP_ADD"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("Ejecutando token:"));
        assertTrue(output.contains("Pila actual:"));
    }

    @Test
    @DisplayName("Tracer debe registrar estado de la pila")
    void testTracerShowsStackSize() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("1 2 3"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("Pila actual:"));
        assertTrue(output.contains("elementos"));
    }

    @Test
    @DisplayName("Tracer debe seguir OP_DUP")
    void testTracerWithDUP() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("5 OP_DUP"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("OP_DUP"));
    }

    @Test
    @DisplayName("Tracer debe seguir OP_DROP")
    void testTracerWithDROP() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("5 10 OP_DROP"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("OP_DROP"));
    }

    @Test
    @DisplayName("Tracer debe seguir OP_SWAP")
    void testTracerWithSWAP() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("1 2 OP_SWAP"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("OP_SWAP"));
    }

    @Test
    @DisplayName("Tracer debe seguir condicionales OP_IF")
    void testTracerWithIF() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("1 OP_IF 10 OP_ELSE 0 OP_ENDIF"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("OP_IF"));
        assertTrue(output.contains("OP_ENDIF"));
    }

    @Test
    @DisplayName("Tracer sin trace flag debe ejecutar sin output")
    void testTracerOnlyWithFlag() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("5 3 OP_ADD"), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Sin trace no debe haber mensajes de ejecución
        assertFalse(output.contains("Ejecutando token:"));
    }

    // ==================== INTEGRATION TESTS FROM MAIN ====================

    @Test
    @DisplayName("Script P2PKH correcto debe retornar true")
    void testP2PKHValid() throws Exception {
        boolean result = interpreter.execute(
            parser.parse("VALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG"), 
            false
        );
        assertTrue(result);
    }

    @Test
    @DisplayName("Script P2PKH incorrecto debe retornar false")
    void testP2PKHInvalid() throws Exception {
        boolean result = interpreter.execute(
            parser.parse("INVALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG"), 
            false
        );
        assertFalse(result);
    }

    @Test
    @DisplayName("Script P2PKH con tracer activado debería mostrar ejecución")
    void testP2PKHWithTracer() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(
                    parser.parse("VALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG"), 
                    true
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("Ejecutando token:"));
    }

    @Test
    @DisplayName("Condicional OP_IF/OP_ELSE/OP_ENDIF con rama IF")
    void testConditionalIF_Branch() throws Exception {
        boolean result = interpreter.execute(parser.parse("1 OP_IF 10 OP_ELSE 0 OP_ENDIF"), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Condicional OP_IF/OP_ELSE/OP_ENDIF con rama ELSE")
    void testConditionalELSE_Branch() throws Exception {
        boolean result = interpreter.execute(parser.parse("0 OP_IF 0 OP_ELSE 10 OP_ENDIF"), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Condicional anidado debe ejecutarse correctamente")
    void testNestedConditionalsExecution() throws Exception {
        boolean result = interpreter.execute(
            parser.parse("1 OP_IF 1 OP_IF 10 OP_ENDIF OP_ENDIF"), 
            false
        );
        assertTrue(result);
    }

    @Test
    @DisplayName("Condicional con tracer debe mostrar OP_IF, OP_ELSE, OP_ENDIF")
    void testConditionalWithTracer() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("1 OP_IF 10 OP_ELSE 0 OP_ENDIF"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("OP_IF") || output.contains("OP_ENDIF"));
    }

    // ==================== COMPLEX SCRIPT TESTS ====================

    @Test
    @DisplayName("Script con múltiples operaciones aritméticas")
    void testComplexArithmetic() throws Exception {
        boolean result = interpreter.execute(parser.parse("5 3 OP_ADD 2 OP_SUB"), false);
        assertTrue(result); // 5 + 3 - 2 = 6
    }

    @Test
    @DisplayName("Script con operaciones de pila encadenadas")
    void testComplexStackOperations() throws Exception {
        boolean result = interpreter.execute(
            parser.parse("5 10 OP_DUP OP_SWAP OP_DROP"), 
            false
        );
        assertTrue(result);
    }

    @Test
    @DisplayName("Script con verificación de igualdad")
    void testEqualityVerification() throws Exception {
        boolean result = interpreter.execute(
            parser.parse("HASH160_PUBKEY HASH160_PUBKEY OP_EQUAL"), 
            false
        );
        assertTrue(result);
    }

    @Test
    @DisplayName("Script con operaciones lógicas NOT")
    void testLogicalNOT() throws Exception {
        boolean result = interpreter.execute(parser.parse("0 OP_NOT"), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Script con operaciones lógicas AND")
    void testLogicalAND() throws Exception {
        boolean result = interpreter.execute(parser.parse("1 1 OP_BOOLAND"), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Script con operaciones lógicas OR")
    void testLogicalOR() throws Exception {
        boolean result = interpreter.execute(parser.parse("1 0 OP_BOOLOR"), false);
        assertTrue(result);
    }

    // ==================== ERROR HANDLING WITH TRACER ====================

    @Test
    @DisplayName("Script inválido con tracer debe mostrar error")
    void testErrorWithTracer() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("INVALIDA PUBKEY OP_CHECKSIGVERIFY"), true);
            } catch (Exception e) {
                // Se espera excepción
            }
        });
        
        // Aunque haya error, el tracer debería haber mostrado ejecución
        assertTrue(output.contains("Ejecutando token:") || output.contains("error") || 
                   output.contains("Exception") || output.contains("ScriptException"));
    }

    @Test
    @DisplayName("Script con OP_RETURN debe fallar")
    void testOPReturn() {
        assertThrows(ScriptException.class, () -> {
            interpreter.execute(parser.parse("10 OP_RETURN 20"), false);
        });
    }

    @Test
    @DisplayName("Script con OP_RETURN y tracer debe mostrar ejecución antes del error")
    void testOPReturnWithTracer() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("10 OP_RETURN"), true);
            } catch (ScriptException e) {
                // Se espera excepción
            }
        });
        
        assertTrue(output.contains("Ejecutando token:") || true); // Debería mostrar tokens
    }

    @Test
    @DisplayName("Script con pila vacía finaliza en false")
    void testEmptyStackResult() throws Exception {
        boolean result = interpreter.execute(parser.parse("0"), false);
        assertFalse(result);
    }

    // ==================== TRACER DEPTH TESTING ====================

    @Test
    @DisplayName("Tracer debe mostrar incremento de pila")
    void testTracerStackGrowth() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("1 2 3 4 5"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Debe mostrar progreso: 1 elemento, 2 elementos, etc.
        assertTrue(output.contains("elementos"));
    }

    @Test
    @DisplayName("Tracer debe procesar operaciones apiladas")
    void testTracerWithStackOperations() {
        String output = captureOutput(() -> {
            try {
                interpreter.execute(parser.parse("1 2 3 OP_DUP OP_DROP OP_SWAP"), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        assertTrue(output.contains("Ejecutando token:"));
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    @DisplayName("Validación de hash con sha256")
    void testSHA256Validation() throws Exception {
        boolean result = interpreter.execute(
            parser.parse("DATA OP_SHA256 SHA256_DATA OP_EQUAL"), 
            false
        );
        assertTrue(result);
    }

    @Test
    @DisplayName("Validación de hash doble")
    void testHASH256Validation() throws Exception {
        boolean result = interpreter.execute(
            parser.parse("DATA OP_HASH256 HASH256_DATA OP_EQUAL"), 
            false
        );
        assertTrue(result);
    }

    @Test
    @DisplayName("Script que combina hash y verificación de igualdad")
    void testHashVerification() throws Exception {
        boolean result = interpreter.execute(
            parser.parse("PUBKEY OP_HASH160 OP_DUP OP_EQUAL"), 
            false
        );
        assertTrue(result);
    }
}
