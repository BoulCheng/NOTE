

- Unicode 是「字符集」 
    - 字符集：为每一个「字符」分配一个唯一的 ID（学名为码位 / 码点 / Code Point）
- UTF-8 是「编码规则」
    - 编码规则：将「码位」转换为字节序列的规则（编码/解码 可以理解为 加密/解密 的过程）
  
  
- ASCII 代表美国信息交换标准码。ASCII字符表
- Java实际上使用Unicode，其中包括ASCII和世界各地语言的其他字符


- char (Java) 两个字节
    - Most of the time, if you are using a single character value, you will use the primitive char type. For example:
        ```
        char ch = 'a'; 
        // Unicode for uppercase Greek omega character
        char uniChar = '\u03A9';
        // an array of chars
        char[] charArray = { 'a', 'b', 'c', 'd', 'e' };

        ```
      
     - 基本数据类型自动转化和ASCII
        ```
        
            public static void main(String[] args) {
        
                char b = 'b';
                System.out.println(((Character)b).hashCode());
                System.out.println((int) b);
                System.out.println();
        
                char a = 'a';
                System.out.println(a);
                System.out.println((int) a);
                System.out.println();
        
                int c = (int) 'c';
                // ASCII
                System.out.println(c);
                System.out.println();
        
                // java基本数据类型的自动转化(比较与强制转化)
                int d = 'd'; 
                System.out.println(d);
                System.out.println();
        
                int i = b - a;
                System.out.println(i);
        
            }
        ```
