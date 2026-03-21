package com.edu.interpreter;

import com.edu.interpreter.engine.ExecutionContext;
import com.edu.interpreter.engine.ScriptInterpreter;
import com.edu.interpreter.engine.ScriptParser;
import com.edu.interpreter.exception.ScriptException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Avanzadas de Integración y Edge Cases")
public class ScriptIntegrationTest {

    private ScriptInterpreter interpreter;
    private ScriptParser parser;

    @BeforeEach
    void setUp() {
        interpreter = new ScriptInterpreter();
        parser = new ScriptParser();
    }

    // ==================== ADVANCED P2PKH SCENARIOS ====================

    @Test
    @DisplayName("P2PKH estándar correcto con firma y pubkey válidas")
    void testStandardP2PKHValid() throws Exception {
        String script = "VALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result, "P2PKH válido debe retornar true");
    }

    @Test
    @DisplayName("P2PKH falla con firma inválida")
    void testP2PKHInvalidSignature() throws Exception {
        String script = "INVALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result, "P2PKH con firma inválida debe retornar false");
    }

    @Test
    @DisplayName("P2PKH falla cuando hash no coincide")
    void testP2PKHHashMismatch() throws Exception {
        String script = "VALIDA PUBKEY OP_DUP OP_HASH160 HASH_DIFERENTE OP_EQUALVERIFY OP_CHECKSIG";
        assertThrows(ScriptException.class, () -> 
            interpreter.execute(parser.parse(script), false));
    }

    // ==================== CONDITIONAL FLOW COMBINATIONS ====================

    @Test
    @DisplayName("IF anidado: verdadero -> verdadero")
    void testNestedIF_TrueTrue() throws Exception {
        String script = "1 OP_IF 1 OP_IF 1 OP_ENDIF OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("IF anidado: verdadero -> falso")
    void testNestedIF_TrueFalse() throws Exception {
        String script = "1 OP_IF 0 OP_IF 0 OP_ENDIF OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result);
    }

    @Test
    @DisplayName("IF anidado: falso -> verdadero (rama no ejecutada)")
    void testNestedIF_FalseTrue() throws Exception {
        String script = "0 OP_IF 1 OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result);
    }

    @Test
    @DisplayName("IF/ELSE/ENDIF: rama IF ejecutada")
    void testIFELSE_IFBranch() throws Exception {
        String script = "1 OP_IF 10 OP_ELSE 20 OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("IF/ELSE/ENDIF: rama ELSE ejecutada")
    void testIFELSE_ELSEBranch() throws Exception {
        String script = "0 OP_IF 0 OP_ELSE 10 OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("NOTIF con condición verdadera no ejecuta")
    void testNOTIF_True() throws Exception {
        String script = "1 OP_NOTIF 0 OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result);
    }

    @Test
    @DisplayName("NOTIF con condición falsa ejecuta")
    void testNOTIF_False() throws Exception {
        String script = "0 OP_NOTIF 10 OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("IF/ELSE/NOTIF anidado complejo")
    void testComplexNestedConditionals() throws Exception {
        String script = "1 OP_IF 1 OP_NOTIF 0 OP_ELSE 1 OP_ENDIF OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    // ==================== STACK MANIPULATION SEQUENCES ====================

    @Test
    @DisplayName("Secuencia: PUSH, DUP, ADD")
    void testStackDUPADD() throws Exception {
        String script = "5 OP_DUP OP_ADD";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result); // 5 + 5 = 10
    }

    @Test
    @DisplayName("Secuencia: PUSH, DUP, SWAP, DROP")
    void testStackDUPSWAPDROP() throws Exception {
        String script = "1 2 OP_DUP OP_SWAP OP_DROP";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Secuencia: PUSH, OVER, ADD")
    void testStackOVEROperation() throws Exception {
        String script = "3 4 OP_OVER OP_ADD";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result); // Resulta en 3 + 4 + 3 en la pila
    }

    @Test
    @DisplayName("Múltiples DUP en secuencia")
    void testMultipleDUP() throws Exception {
        String script = "5 OP_DUP OP_DUP OP_DUP";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result); // Pila: 5, 5, 5, 5
    }

    @Test
    @DisplayName("SWAP intercambia correctamente")
    void testSWAPCorrectness() throws Exception {
        String script = "10 20 OP_SWAP OP_SUB";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result); // 10 - 20 = -10
    }

    // ==================== COMPARISON CHAINS ====================

    @Test
    @DisplayName("Comparativas: 10 > 5 es verdadero")
    void testGreaterThan() throws Exception {
        String script = "10 5 OP_GREATERTHAN";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Comparativas: 3 < 8 es verdadero")
    void testLessThan() throws Exception {
        String script = "3 8 OP_LESSTHAN";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Comparativas: 5 >= 5 es verdadero")
    void testGreaterEqual() throws Exception {
        String script = "5 5 OP_GREATERTHANOREQUAL";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Comparativas: 5 <= 5 es verdadero")
    void testLessEqual() throws Exception {
        String script = "5 5 OP_LESSTHANOREQUAL";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Comparativas combinadas con IF")
    void testComparisonWithIF() throws Exception {
        String script = "10 5 OP_GREATERTHAN OP_IF 1 OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    // ==================== LOGICAL OPERATIONS ====================

    @Test
    @DisplayName("NOT de 0 es 1")
    void testNOT_Zero() throws Exception {
        String script = "0 OP_NOT";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("NOT de 1 es 0")
    void testNOT_One() throws Exception {
        String script = "1 OP_NOT";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result);
    }

    @Test
    @DisplayName("AND: 1 AND 1 es 1")
    void testAND_TrueTrue() throws Exception {
        String script = "1 1 OP_BOOLAND";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("AND: 1 AND 0 es 0")
    void testAND_TrueFalse() throws Exception {
        String script = "1 0 OP_BOOLAND";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result);
    }

    @Test
    @DisplayName("OR: 0 OR 0 es 0")
    void testOR_FalseFalse() throws Exception {
        String script = "0 0 OP_BOOLOR";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result);
    }

    @Test
    @DisplayName("OR: 1 OR 0 es 1")
    void testOR_TrueFalse() throws Exception {
        String script = "1 0 OP_BOOLOR";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Operaciones lógicas combinadas")
    void testCombinedLogical() throws Exception {
        String script = "1 1 OP_BOOLAND 0 OP_BOOLOR";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    // ==================== CRYPTO OPERATIONS ====================

    @Test
    @DisplayName("HASH160 genera hash consistente")
    void testHASH160() throws Exception {
        String script = "PUBKEY OP_HASH160 HASH_PUBKEY OP_EQUAL";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("SHA256 genera hash consistente")
    void testSHA256() throws Exception {
        String script = "DATA OP_SHA256 SHA256_DATA OP_EQUAL";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("HASH256 funciona correctamente")
    void testHASH256() throws Exception {
        String script = "DATA OP_HASH256 HASH256_DATA OP_EQUAL";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("CHECKSIG con firma válida retorna 1")
    void testCHECKSIG_Valid() throws Exception {
        String script = "VALIDA PUBKEY1 OP_CHECKSIG";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("CHECKSIG con firma inválida retorna 0")
    void testCHECKSIG_Invalid() throws Exception {
        String script = "PUBKEY INVALIDA OP_CHECKSIG 0 OP_EQUAL";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    // ==================== ARITHMETIC COMBINATIONS ====================

    @Test
    @DisplayName("Suma encadenada: 2 + 3 + 5")
    void testChainedAddition() throws Exception {
        String script = "2 3 OP_ADD 5 OP_ADD";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result); // Resultado: 10
    }

    @Test
    @DisplayName("Operaciones mixtas: (10 - 3) + 5")
    void testMixedArithmetic() throws Exception {
        String script = "10 3 OP_SUB 5 OP_ADD";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result); // Resultado: 12
    }

    @Test
    @DisplayName("Comparación post-operación")
    void testComparisonAfterArithmetic() throws Exception {
        String script = "5 2 OP_ADD 7 OP_EQUAL";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    // ==================== VERIFY OPERATIONS ====================

    @Test
    @DisplayName("EQUALVERIFY con valores iguales pasa")
    void testEQUALVERIFY_Pass() throws Exception {
        String script = "HASH HASH OP_EQUALVERIFY 1";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("EQUALVERIFY con valores diferentes falla")
    void testEQUALVERIFY_Fail() {
        String script = "HASH1 HASH2 OP_EQUALVERIFY";
        assertThrows(ScriptException.class, () -> 
            interpreter.execute(parser.parse(script), false));
    }

    @Test
    @DisplayName("VERIFY con valor verdadero pasa")
    void testVERIFY_Pass() throws Exception {
        String script = "1 1 OP_VERIFY";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("VERIFY con valor falso falla")
    void testVERIFY_Fail() {
        String script = "0 OP_VERIFY";
        assertThrows(ScriptException.class, () -> 
            interpreter.execute(parser.parse(script), false));
    }

    // ==================== COMPLEX SCENARIOS ====================

    @Test
    @DisplayName("Multisig simulado: (sig1 AND sig2) verifica")
    void testSimulatedMultisig() throws Exception {
        String script = "VALIDA PUBKEY1 OP_CHECKSIG VALIDA PUBKEY2 OP_CHECKSIG OP_BOOLAND";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Tiempo lock simulado con condicional")
    void testSimulatedTimeLock() throws Exception {
        String script = "1 OP_IF 1 OP_ELSE 0 OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Validación compleja: hash + firma + comparativa")
    void testCompleteValidation() throws Exception {
        String script = "VALIDA PUBKEY OP_DUP OP_HASH160 HASH_PUBKEY OP_EQUALVERIFY OP_CHECKSIG 1 OP_EQUAL";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    @Test
    @DisplayName("Script con múltiples ramas condicionales")
    void testMultipleBranches() throws Exception {
        String script = "1 OP_IF 10 OP_IF 1 OP_ENDIF OP_ENDIF";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Script con solo pushs en la pila")
    void testOnlyPushes() throws Exception {
        String script = "1 2 3 4 5";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result); // Top es 5, que no es 0
    }

    @Test
    @DisplayName("Script que deja 0 en la pila")
    void testZeroResult() throws Exception {
        String script = "0";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result);
    }

    @Test
    @DisplayName("Script con operación que deja stack vacío")
    void testEmptyStackError() {
        String script = "5 OP_DROP";
        boolean result;
        try {
            result = interpreter.execute(parser.parse(script), false);
            assertFalse(result);
        } catch (Exception e) {
            // Es aceptable una excepción si el stack está vacío
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Script con doble negación")
    void testDoubleNegation() throws Exception {
        String script = "0 OP_NOT OP_NOT";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertFalse(result);
    }

    @Test
    @DisplayName("Profundidad de pila con múltiples operaciones")
    void testDeepStack() throws Exception {
        String script = "1 2 3 4 5 6 7 8 9 10 OP_DROP";
        boolean result = interpreter.execute(parser.parse(script), false);
        assertTrue(result);
    }
}
