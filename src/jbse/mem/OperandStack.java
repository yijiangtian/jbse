package jbse.mem;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;

import jbse.mem.exc.OperandStackEmptyException;
import jbse.val.Value;

/**
 * Class that represent the JVM's operand stack.
 */
//TODO Suitably manage stack overflow (unexpected internal error)
class OperandStack implements Cloneable {
    private ArrayDeque<Value> valueStack;
    
    /**
     * Constructor of empty operand stack.
     * 
     */
    OperandStack() {
        this.valueStack = new ArrayDeque<Value>();
    }
    
    /**
     * Pushes a {@link Value} on the top of the operand stack.
     * @param item the {@link Value} to be pushed on the top 
     *             of the operand stack.
     */
    void push(Value item) {
        this.valueStack.push(item);
    }
    
    /**
     * Extracts and returns the element on the top of the operand stack. 
     * 
     * @return the {@link Value} on the top of the operand stack.
     * @throws OperandStackEmptyException if the operand stack is empty.
     */
    Value pop() throws OperandStackEmptyException {
    	if (this.valueStack.isEmpty()) {
    		throw new OperandStackEmptyException();
    	}
    	return this.valueStack.pop();
    }
    
    /**
     * Returns the topmost element without removing it.
     * 
     * @return the topmost {@link Value}.
     * @throws OperandStackEmptyException if the operand stack is empty.
     */
    Value top() throws OperandStackEmptyException {
    	if (this.valueStack.isEmpty()) {
    		throw new OperandStackEmptyException();
    	}
        return(this.valueStack.peek());
    }
    
    void clear() {
    	this.valueStack.clear();
    }
    
    Collection<Value> values() {
    	return Collections.unmodifiableCollection(this.valueStack);
    }
    
    /**
     * Returns a string representation for the operand stack
     */
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("{");
        boolean isFirst = true;
        for (Value v : this.valueStack) {
            if (isFirst) {
                isFirst = false;
            } else {
                buf.append(", ");
            }
            buf.append(v.toString());
        }
        buf.append("}");
        return buf.toString();
    }
    
    @Override
    public OperandStack clone() {
        final OperandStack o;
        try {
            o = (OperandStack) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }        
        o.valueStack = new ArrayDeque<Value>(this.valueStack);
        return o;
    }
}


