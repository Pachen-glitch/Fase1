package com.edu.interpreter.opcodes;

import com.edu.interpreter.engine.ExecutionContext;
import com.edu.interpreter.exception.ScriptException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas de Opcodes")
public class OpcodeTests {

    private ExecutionContext context;
    private OpcodeRegistry registry;

    @BeforeEach
    void setUp() {
        context = new ExecutionContext();
        registry = new OpcodeRegistry();
        
        // Registrar todos los opcodes
        ArithmeticOpcodes.register(registry);
        ControlFlowOpcodes.register(registry);
        CryptoOpcodes.register(registry);
        LogicOpcodes.register(registry);
        StackOpcodes.register(registry);
    }

    // ==================== ARITHMETIC OPCODES ====================

    @Test
    @DisplayName("OP_0 debe pushear 0 a la pila")
    void testOP_0() throws ScriptException {
        Opcode op = registry.get("OP_0");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("0", new String(result));
    }

    @Test
    @DisplayName("OP_1 a OP_16 deben pushear sus valores")
    void testOP_1_to_OP_16() throws ScriptException {
        for (int i = 1; i <= 16; i++) {
            context = new ExecutionContext();
            registry = new OpcodeRegistry();
            ArithmeticOpcodes.register(registry);
            
            Opcode op = registry.get("OP_" + i);
            op.execute(context);
            
            byte[] result = context.getStack().pop();
            assertEquals(String.valueOf(i), new String(result));
        }
    }

    @Test
    @DisplayName("OP_ADD debe sumar dos números")
    void testOP_ADD() throws ScriptException {
        context.getStack().push("5".getBytes());
        context.getStack().push("3".getBytes());
        
        Opcode op = registry.get("OP_ADD");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("8", new String(result));
    }

    @Test
    @DisplayName("OP_SUB debe restar dos números")
    void testOP_SUB() throws ScriptException {
        context.getStack().push("10".getBytes());
        context.getStack().push("3".getBytes());
        
        Opcode op = registry.get("OP_SUB");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("7", new String(result));
    }

    @Test
    @DisplayName("OP_ADD con números negativos")
    void testOP_ADD_negative() throws ScriptException {
        context.getStack().push("-5".getBytes());
        context.getStack().push("3".getBytes());
        
        Opcode op = registry.get("OP_ADD");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("-2", new String(result));
    }

    @Test
    @DisplayName("OP_GREATERTHAN: 10 > 5 debe retornar 1")
    void testOP_GREATERTHAN_true() throws ScriptException {
        context.getStack().push("10".getBytes());
        context.getStack().push("5".getBytes());
        
        Opcode op = registry.get("OP_GREATERTHAN");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    @Test
    @DisplayName("OP_GREATERTHAN: 3 > 5 debe retornar 0")
    void testOP_GREATERTHAN_false() throws ScriptException {
        context.getStack().push("3".getBytes());
        context.getStack().push("5".getBytes());
        
        Opcode op = registry.get("OP_GREATERTHAN");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("0", new String(result));
    }

    @Test
    @DisplayName("OP_LESSTHAN: 3 < 10 debe retornar 1")
    void testOP_LESSTHAN_true() throws ScriptException {
        context.getStack().push("3".getBytes());
        context.getStack().push("10".getBytes());
        
        Opcode op = registry.get("OP_LESSTHAN");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    @Test
    @DisplayName("OP_LESSTHAN: 10 < 3 debe retornar 0")
    void testOP_LESSTHAN_false() throws ScriptException {
        context.getStack().push("10".getBytes());
        context.getStack().push("3".getBytes());
        
        Opcode op = registry.get("OP_LESSTHAN");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("0", new String(result));
    }

    @Test
    @DisplayName("OP_GREATERTHANOREQUAL: 10 >= 10 debe retornar 1")
    void testOP_GREATERTHANOREQUAL_equal() throws ScriptException {
        context.getStack().push("10".getBytes());
        context.getStack().push("10".getBytes());
        
        Opcode op = registry.get("OP_GREATERTHANOREQUAL");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    @Test
    @DisplayName("OP_LESSTHANOREQUAL: 5 <= 10 debe retornar 1")
    void testOP_LESSTHANOREQUAL_true() throws ScriptException {
        context.getStack().push("5".getBytes());
        context.getStack().push("10".getBytes());
        
        Opcode op = registry.get("OP_LESSTHANOREQUAL");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    // ==================== STACK OPCODES ====================

    @Test
    @DisplayName("OP_DUP debe duplicar el top de la pila")
    void testOP_DUP() throws ScriptException {
        context.getStack().push("TEST".getBytes());
        
        Opcode op = registry.get("OP_DUP");
        op.execute(context);
        
        byte[] first = context.getStack().pop();
        byte[] second = context.getStack().pop();
        
        assertEquals("TEST", new String(first));
        assertEquals("TEST", new String(second));
    }

    @Test
    @DisplayName("OP_DROP debe eliminar el top de la pila")
    void testOP_DROP() throws ScriptException {
        context.getStack().push("KEEP".getBytes());
        context.getStack().push("DROP".getBytes());
        
        Opcode op = registry.get("OP_DROP");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("KEEP", new String(result));
        assertTrue(context.getStack().isEmpty());
    }

    @Test
    @DisplayName("OP_SWAP debe intercambiar los dos top elementos")
    void testOP_SWAP() throws ScriptException {
        context.getStack().push("A".getBytes());
        context.getStack().push("B".getBytes());
        
        Opcode op = registry.get("OP_SWAP");
        op.execute(context);
        
        byte[] first = context.getStack().pop();
        byte[] second = context.getStack().pop();
        
        assertEquals("A", new String(first));
        assertEquals("B", new String(second));
    }

    @Test
    @DisplayName("OP_OVER debe copiar el segundo elemento al top")
    void testOP_OVER() throws ScriptException {
        context.getStack().push("A".getBytes());
        context.getStack().push("B".getBytes());
        
        Opcode op = registry.get("OP_OVER");
        op.execute(context);
        
        byte[] first = context.getStack().pop();
        byte[] second = context.getStack().pop();
        byte[] third = context.getStack().pop();
        
        assertEquals("A", new String(first));
        assertEquals("B", new String(second));
        assertEquals("A", new String(third));
    }

    // ==================== LOGIC OPCODES ====================

    @Test
    @DisplayName("OP_EQUAL: valores iguales debe retornar 1")
    void testOP_EQUAL_true() throws ScriptException {
        context.getStack().push("HASH".getBytes());
        context.getStack().push("HASH".getBytes());
        
        Opcode op = registry.get("OP_EQUAL");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    @Test
    @DisplayName("OP_EQUAL: valores diferentes debe retornar 0")
    void testOP_EQUAL_false() throws ScriptException {
        context.getStack().push("HASH1".getBytes());
        context.getStack().push("HASH2".getBytes());
        
        Opcode op = registry.get("OP_EQUAL");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("0", new String(result));
    }

    @Test
    @DisplayName("OP_EQUALVERIFY: valores iguales no lanza excepción")
    void testOP_EQUALVERIFY_success() throws ScriptException {
        context.getStack().push("VALUE".getBytes());
        context.getStack().push("VALUE".getBytes());
        
        Opcode op = registry.get("OP_EQUALVERIFY");
        assertDoesNotThrow(() -> op.execute(context));
    }

    @Test
    @DisplayName("OP_EQUALVERIFY: valores diferentes lanza excepción")
    void testOP_EQUALVERIFY_failure() throws ScriptException {
        context.getStack().push("VALUE1".getBytes());
        context.getStack().push("VALUE2".getBytes());
        
        Opcode op = registry.get("OP_EQUALVERIFY");
        assertThrows(ScriptException.class, () -> op.execute(context));
    }

    @Test
    @DisplayName("OP_NOT: 0 debe retornar 1")
    void testOP_NOT_zero() throws ScriptException {
        context.getStack().push("0".getBytes());
        
        Opcode op = registry.get("OP_NOT");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    @Test
    @DisplayName("OP_NOT: 1 debe retornar 0")
    void testOP_NOT_one() throws ScriptException {
        context.getStack().push("1".getBytes());
        
        Opcode op = registry.get("OP_NOT");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("0", new String(result));
    }

    @Test
    @DisplayName("OP_BOOLAND: 1 AND 1 debe retornar 1")
    void testOP_BOOLAND_true() throws ScriptException {
        context.getStack().push("1".getBytes());
        context.getStack().push("1".getBytes());
        
        Opcode op = registry.get("OP_BOOLAND");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    @Test
    @DisplayName("OP_BOOLAND: 1 AND 0 debe retornar 0")
    void testOP_BOOLAND_false() throws ScriptException {
        context.getStack().push("1".getBytes());
        context.getStack().push("0".getBytes());
        
        Opcode op = registry.get("OP_BOOLAND");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("0", new String(result));
    }

    @Test
    @DisplayName("OP_BOOLOR: 0 OR 0 debe retornar 0")
    void testOP_BOOLOR_false() throws ScriptException {
        context.getStack().push("0".getBytes());
        context.getStack().push("0".getBytes());
        
        Opcode op = registry.get("OP_BOOLOR");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("0", new String(result));
    }

    @Test
    @DisplayName("OP_BOOLOR: 1 OR 0 debe retornar 1")
    void testOP_BOOLOR_true() throws ScriptException {
        context.getStack().push("1".getBytes());
        context.getStack().push("0".getBytes());
        
        Opcode op = registry.get("OP_BOOLOR");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    // ==================== CRYPTO OPCODES ====================

    @Test
    @DisplayName("OP_HASH160 debe hashear el valor pusheado")
    void testOP_HASH160() throws ScriptException {
        context.getStack().push("PUBKEY".getBytes());
        
        Opcode op = registry.get("OP_HASH160");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("HASH_PUBKEY", new String(result));
    }

    @Test
    @DisplayName("OP_CHECKSIG con firma válida debe retornar 1")
    void testOP_CHECKSIG_valid() throws ScriptException {
        context.getStack().push("VALIDA".getBytes());
        context.getStack().push("PUBKEY".getBytes());
        
        Opcode op = registry.get("OP_CHECKSIG");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("1", new String(result));
    }

    @Test
    @DisplayName("OP_CHECKSIG con firma inválida debe retornar 0")
    void testOP_CHECKSIG_invalid() throws ScriptException {
        context.getStack().push("INVALIDA".getBytes());
        context.getStack().push("PUBKEY".getBytes());
        
        Opcode op = registry.get("OP_CHECKSIG");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("0", new String(result));
    }

    @Test
    @DisplayName("OP_CHECKSIGVERIFY con firma válida no lanza excepción")
    void testOP_CHECKSIGVERIFY_success() throws ScriptException {
        context.getStack().push("VALIDA".getBytes());
        context.getStack().push("PUBKEY".getBytes());
        
        Opcode op = registry.get("OP_CHECKSIGVERIFY");
        assertDoesNotThrow(() -> op.execute(context));
    }

    @Test
    @DisplayName("OP_CHECKSIGVERIFY con firma inválida lanza excepción")
    void testOP_CHECKSIGVERIFY_failure() throws ScriptException {
        context.getStack().push("INVALIDA".getBytes());
        context.getStack().push("PUBKEY".getBytes());
        
        Opcode op = registry.get("OP_CHECKSIGVERIFY");
        assertThrows(ScriptException.class, () -> op.execute(context));
    }

    @Test
    @DisplayName("OP_SHA256 debe crear hash SHA256")
    void testOP_SHA256() throws ScriptException {
        context.getStack().push("DATA".getBytes());
        
        Opcode op = registry.get("OP_SHA256");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("SHA256_DATA", new String(result));
    }

    @Test
    @DisplayName("OP_HASH256 debe crear hash doble")
    void testOP_HASH256() throws ScriptException {
        context.getStack().push("DATA".getBytes());
        
        Opcode op = registry.get("OP_HASH256");
        op.execute(context);
        
        byte[] result = context.getStack().pop();
        assertEquals("HASH256_DATA", new String(result));
    }

    // ==================== CONTROL FLOW OPCODES ====================

    @Test
    @DisplayName("OP_IF con condición verdadera debe ejecutarse")
    void testOP_IF_true() throws ScriptException {
        context.getStack().push("1".getBytes());
        
        Opcode op = registry.get("OP_IF");
        op.execute(context);
        
        assertTrue(context.isExecuting());
    }

    @Test
    @DisplayName("OP_IF con condición falsa no debe ejecutarse")
    void testOP_IF_false() throws ScriptException {
        context.getStack().push("0".getBytes());
        
        Opcode op = registry.get("OP_IF");
        op.execute(context);
        
        assertFalse(context.isExecuting());
    }

    @Test
    @DisplayName("OP_NOTIF con condición falsa debe ejecutarse")
    void testOP_NOTIF_true() throws ScriptException {
        context.getStack().push("0".getBytes());
        
        Opcode op = registry.get("OP_NOTIF");
        op.execute(context);
        
        assertTrue(context.isExecuting());
    }

    @Test
    @DisplayName("OP_NOTIF con condición verdadera no debe ejecutarse")
    void testOP_NOTIF_false() throws ScriptException {
        context.getStack().push("1".getBytes());
        
        Opcode op = registry.get("OP_NOTIF");
        op.execute(context);
        
        assertFalse(context.isExecuting());
    }

    @Test
    @DisplayName("OP_ELSE debe invertir el estado de ejecución")
    void testOP_ELSE() throws ScriptException {
        // Primero OP_IF con true
        context.getStack().push("1".getBytes());
        registry.get("OP_IF").execute(context);
        assertTrue(context.isExecuting());
        
        // Luego OP_ELSE
        registry.get("OP_ELSE").execute(context);
        assertFalse(context.isExecuting());
    }

    @Test
    @DisplayName("OP_ELSE sin OP_IF debe lanzar excepción")
    void testOP_ELSE_without_IF() {
        Opcode op = registry.get("OP_ELSE");
        assertThrows(ScriptException.class, () -> op.execute(context));
    }

    @Test
    @DisplayName("OP_ENDIF debe cerrar bloque condicional")
    void testOP_ENDIF() throws ScriptException {
        context.getStack().push("1".getBytes());
        registry.get("OP_IF").execute(context);
        registry.get("OP_ENDIF").execute(context);
        
        assertTrue(context.isExecuting());
        assertTrue(context.getConditionStack().isEmpty());
    }

    @Test
    @DisplayName("OP_ENDIF sin OP_IF debe lanzar excepción")
    void testOP_ENDIF_without_IF() {
        Opcode op = registry.get("OP_ENDIF");
        assertThrows(ScriptException.class, () -> op.execute(context));
    }

    @Test
    @DisplayName("OP_VERIFY con valor válido no debe lanzar excepción")
    void testOP_VERIFY_success() throws ScriptException {
        context.getStack().push("1".getBytes());
        
        Opcode op = registry.get("OP_VERIFY");
        assertDoesNotThrow(() -> op.execute(context));
    }

    @Test
    @DisplayName("OP_VERIFY con valor 0 debe lanzar excepción")
    void testOP_VERIFY_failure() throws ScriptException {
        context.getStack().push("0".getBytes());
        
        Opcode op = registry.get("OP_VERIFY");
        assertThrows(ScriptException.class, () -> op.execute(context));
    }

    @Test
    @DisplayName("OP_RETURN debe lanzar excepción")
    void testOP_RETURN() {
        Opcode op = registry.get("OP_RETURN");
        assertThrows(ScriptException.class, () -> op.execute(context));
    }

    // ==================== NESTED CONDITIONALS ====================

    @Test
    @DisplayName("IF/ELSE/ENDIF anidados deben funcionar correctamente")
    void testNestedConditionals() throws ScriptException {
        // Outer IF true
        context.getStack().push("1".getBytes());
        registry.get("OP_IF").execute(context);
        assertTrue(context.isExecuting());
        
        // Inner IF true
        context.getStack().push("1".getBytes());
        registry.get("OP_IF").execute(context);
        assertTrue(context.isExecuting());
        
        // Inner ENDIF
        registry.get("OP_ENDIF").execute(context);
        assertTrue(context.isExecuting());
        
        // Outer ENDIF
        registry.get("OP_ENDIF").execute(context);
        assertTrue(context.isExecuting());
        assertTrue(context.getConditionStack().isEmpty());
    }

    @Test
    @DisplayName("IF/ELSE/ENDIF anidados con rama else")
    void testNestedConditionalsWithElse() throws ScriptException {
        // Outer IF true
        context.getStack().push("1".getBytes());
        registry.get("OP_IF").execute(context);
        assertTrue(context.isExecuting());
        
        // Inner IF false
        context.getStack().push("0".getBytes());
        registry.get("OP_IF").execute(context);
        assertFalse(context.isExecuting());
        
        // Inner ELSE
        registry.get("OP_ELSE").execute(context);
        assertTrue(context.isExecuting());
        
        // Inner ENDIF
        registry.get("OP_ENDIF").execute(context);
        assertTrue(context.isExecuting());
        
        // Outer ENDIF
        registry.get("OP_ENDIF").execute(context);
        assertTrue(context.isExecuting());
        assertTrue(context.getConditionStack().isEmpty());
    }
}
