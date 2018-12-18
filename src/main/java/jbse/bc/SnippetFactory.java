package jbse.bc;

import static jbse.bc.Opcodes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SnippetFactory {
    private final HashMap<Integer, Signature> signatures = new HashMap<>();
    private final HashMap<Integer, Integer> integers = new HashMap<>();
    private final HashMap<Integer, Long> longs = new HashMap<>();
    private final HashMap<Integer, Float> floats = new HashMap<>();
    private final HashMap<Integer, Double> doubles = new HashMap<>();
    private final HashMap<Integer, String> utf8s = new HashMap<>();
    private final HashMap<Integer, String> strings = new HashMap<>();    
    private final HashMap<Integer, String> classes = new HashMap<>();
    private final HashMap<Signature, Integer> signaturesInverse = new HashMap<>();
    private final HashMap<Integer, Integer> integersInverse = new HashMap<>();
    private final HashMap<Long, Integer> longsInverse = new HashMap<>();
    private final HashMap<Float, Integer> floatsInverse = new HashMap<>();
    private final HashMap<Double, Integer> doublesInverse = new HashMap<>();
    private final HashMap<String, Integer> utf8sInverse = new HashMap<>();
    private final HashMap<String, Integer> stringsInverse = new HashMap<>();    
    private final HashMap<String, Integer> classesInverse = new HashMap<>();
    private final ArrayList<Byte> bytecode = new ArrayList<>();
    private int nextIndex;
    
    public SnippetFactory() {
        this.nextIndex = 1; 
    }
    
    public SnippetFactory(ClassFile cf) {
        this.nextIndex = cf.constantPoolSize() + 1; 
    }
    
    private void addIndex(int index) {
        this.bytecode.add((byte) (index >>> 8));
        this.bytecode.add((byte) (index & 0x0000_0000_0000_00FF));
    }
    
    private <V> void addConstantPoolItem(Map<Integer, V> map, Map<V, Integer> mapInverse, V value) {
    	if (mapInverse.containsKey(value)) {
    		final int index = mapInverse.get(value);
    		addIndex(index);
    	} else {
    		map.put(this.nextIndex, value);
    		mapInverse.put(value, this.nextIndex);
    		addIndex(this.nextIndex);
    		++this.nextIndex;
    	}
    }
    
    private void addSignature(Signature sig) {
    	addConstantPoolItem(this.signatures, this.signaturesInverse, sig);
    }
    
    private void addInteger(int value) {
    	addConstantPoolItem(this.integers, this.integersInverse, value);
    }
    
    private void addLong(long value) {
    	addConstantPoolItem(this.longs, this.longsInverse, value);
    }
    
    private void addFloat(float value) {
    	addConstantPoolItem(this.floats, this.floatsInverse, value);
    }
    
    private void addDouble(double value) {
    	addConstantPoolItem(this.doubles, this.doublesInverse, value);
    }
    
    private void addUtf8(String value) {
    	addConstantPoolItem(this.utf8s, this.utf8sInverse, value);
    }
    
    private void addString(String value) {
    	addConstantPoolItem(this.strings, this.stringsInverse, value);
    }
    
    private void addClass(String value) {
    	addConstantPoolItem(this.classes, this.classesInverse, value);
    }
    
    public SnippetFactory op_dup() {
        this.bytecode.add(OP_DUP);
        return this;
    }
    
    private void op_invoke(byte bytecode, Signature methodSignature) {
        this.bytecode.add(bytecode);
        addSignature(methodSignature);
    }
    
    public SnippetFactory op_invokehandle(Signature methodSignature) {
        op_invoke(OP_INVOKEHANDLE, methodSignature);
        return this;
    }
    
    public SnippetFactory op_invokeinterface(Signature methodSignature) {
        op_invoke(OP_INVOKEINTERFACE, methodSignature);
        this.bytecode.add((byte) 1);
        this.bytecode.add((byte) 0);
        return this;
    }
    
    public SnippetFactory op_invokespecial(Signature methodSignature) {
        op_invoke(OP_INVOKESPECIAL, methodSignature);
        return this;
    }
    
    public SnippetFactory op_invokestatic(Signature methodSignature) {
        op_invoke(OP_INVOKESTATIC, methodSignature);
        return this;
    }
    
    public SnippetFactory op_invokevirtual(Signature methodSignature) {
        op_invoke(OP_INVOKEVIRTUAL, methodSignature);
        return this;
    }
    
    public SnippetFactory op_pop() {
        this.bytecode.add(OP_POP);
        return this;
    }
    
    public SnippetFactory op_return() {
        this.bytecode.add(OP_RETURN);
        return this;
    }
    
    public Snippet mk() {
        //no way to do it with streams or other conversion functions
        final byte[] bytecode = new byte[this.bytecode.size()];
        for (int i = 0; i < bytecode.length; ++i) {
            bytecode[i] = this.bytecode.get(i).byteValue();
        }
        return new Snippet(this.signatures, this.integers, this.longs, this.floats, 
        		this.doubles, this.utf8s, this.strings, this.classes, bytecode);
    }
}