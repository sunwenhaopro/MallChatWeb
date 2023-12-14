 
  
class Token {  
    char type;  
    int value;  
    public Token(char type, int value) {  
        this.type = type;  
        this.value = value;  
    }  
}  
  
class E extends Token {  
    public E(int value) { super('E', value); }  
    public void parse() {   
        Token token = stack.pop();  
        if (token.type != '(') throw new IllegalArgumentException("Invalid expression");  
        stack.push(new T(value));  
        while (!stack.isEmpty() && stack.peek().type != ')') {  
            Token t = stack.pop();  
            if (t.type == '+') {  
                stack.push(new E(value + stack.pop().value));  
            } else throw new IllegalArgumentException("Invalid expression");  
        }  
        if (!stack.isEmpty()) stack.pop(); // Discard the ")" token  
    }  
}  
  
class T extends Token {  
    public T(int value) { super('T', value); }  
    public void parse() {   
        Token token = stack.pop();  
        if (token.type != '(') throw new IllegalArgumentException("Invalid expression");  
        stack.push(new F(value));  
        while (!stack.isEmpty() && stack.peek().type != ')') {  
            Token t = stack.pop();  
            if (t.type == '*') {  
                stack.push(new T(value * stack.pop().value));  
            } else throw new IllegalArgumentException("Invalid expression");  
        }  
        if (!stack.isEmpty()) stack.pop(); // Discard the ")" token  
    }  
}  
  
class F extends Token {  
    public F(int value) { super('F', value); }  
    public void parse() {   
        Token token = stack.pop();  
        if (token.type == '(') { // '(' token is already popped, get the expression from the token list and push it to the stack.  
            int value = 0;  
            while (!tokenList.isEmpty()) {  
                Token t = tokenList.removeLast(); // Remove the token from the end of the list and add it to the value.  
                if (t.type == '+') { // Check if it's a '+' token, if it is, add the rest of the tokens to the value and return.   
                    value += ((E) t).value;   
                    break;   
                } else if (t.type == '*') throw new IllegalArgumentException("Invalid expression"); // If it's a '*' token, throw an exception.   
                else if (t instanceof Token) value += ((Token) t).value; // If it's a number, add it to the value.   
            }   
            stack.push(new F(value));   
        } else if (token instanceof Token && ((Token) token).type != ')') throw new IllegalArgumentException("Invalid expression"); // If it's a number or ')', push it to the stack.   
        else throw new IllegalArgumentException("Invalid expression"); // If it's neither, throw an exception.   
    }   
}   
  
public class Main {  
    public static void main(String[] args) {  
        List<Token> tokenList = new ArrayList<>();  
         
        String expression = "98+99+80" 
        tokenList.add(new Token('E', Integer.parseInt(expression)));  
        Stack<Token> stack = new Stack<>();  
        for (Token token : tokenList) {  
            stack.push(token);  
        }  
        while (!stack.isEmpty()) {  
            Token token = stack.pop();  
            if (token instanceof Token) { // If it's a token, parse it.   
                ((Token) token).parse();   
            } else throw new IllegalArgumentException("Invalid expression"); // If it's not a token, throw an exception.   
        }   
        System.out.println("The result is: " + ((F) stack.pop()).value); // The result should be on the top of the stack as an F token.   
    } 
}