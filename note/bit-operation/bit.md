



- 计算机中只有加法运算，且运算都是其补码运算！正数的补码 反码 原码 相同
- 负数的原码有负符号位，把符号位改为正符号位再按二进制计算可以得到对应负数的正值
- 负数在计算机中是以它的补码表示的
- 补码：1000,0001 反码：1111,1110 转回原码为：1111,1111 就是 -127
```
-2 << 1

-2用原码表示为10000000 00000000 00000000 00000010

-2用反码表示为11111111 11111111 11111111 11111101

-2用补码表示为11111111 11111111 11111111 11111110

-2 << 1，表示-2的补码左移一位后为 11111111 11111111 11111111 11111100

public static void main(String[] args) {
    System.out.println(0B11111111111111111111111111111100); //-4
}

11111111 11111111 11111111 11111100 是 -4 的补码

11111111 11111111 11111111 11111100先符号位不变取反再加1同样可以得到原码为10000000 00000000 00000000 00000100，表示-4的原码

同样 Integer.MIN_VALUE 也是补码并非原码，不过是十六进制的
```



### byte类型的最小值为什么是-128而非-127？

原因：计算机表示数值的方法有：原码、反码、补码，而现在计算机用补码存储整数数值

原码：用二进制表示的原始编码
反码：除了符号位外，其它位取反
补码：正数的补码等于原码、负数的补码等于反码加1
而byte的最大正数为二进制的01111111 = 2^7-1 = 127 (0为符号位)，这没有问题
对于负数而言，补码等于反码加1

1111,1111 补码就是 1000,0001 = -1
1111,1110的补码就是 1000,0010 = -2
依次类推…
1000,0001的补码就是 1111,1111 = -127
接下去 最大负数为 1000,0000 （十进制为 -0，但是就与+0重复，没有意义）不是负数的话，补码仍为 1000,0000，计算机就将这一数值表示为最大负数 -128

源码1000,0000 反码1111,1111 补码0000,0000 (-0), -0 表示为 -128





#### 类型转换溢出
- 1
```
public class demo1{
    public static void main(String[]args){
        byte num=127;
        num+=2;
//输出是-127，原理是因为byte类型是8位的数据
        System.out.println(num);
    }
}
```
分析：127 对应的二进制为 0111,1111，其补码仍为：0111,1111，(计算机中只有加法运算，且运算都是其补码运算！！！)
加上2之后-------> 补码是：1000,0001(加1为“1000,0000”再加1为“1000,0001”)
上述补码再转回原码为：1111,1111 就是 -127
注意：当数据类型为byte、short、char型时，相互之间不能转换，它们参与运算是先将数据转换为int类型进行运算

- 2
```
public class demo1{
    public static void main(String[]args){
        Int num1=2147483647;
        Int num2=2147483647;
        //输出是-2，原因是int容器太小，溢出
        System.out.println(num1+num2);
    }
}
```
分析：本来结果是 4294967294，因为int 之间的结果 还是int类型
4294967294的二进制及其补码都是 “1111,1111,1111,1111,1111,1111,1111,1110”转换为补码就是“1000,0000,0000,0000,0000,0000,0000,0010”
十进制结果就是 -2

- int转为byte类型的溢出处理
byte的存储范围是-128-127的整数范围，byte a = (byte)130；结果会是多少呢？java是如何处理强制类型转换的溢出处理呢？

在计算机中，所有数据都以补码形式存储，那么130首先当作int存储，32位，其二进制原码和补码都为：“0000,0000,0000,0000,0000,0000,1000,0010”；
转换为byte类型，进行截取，高字节部分去除，保留低字节部分（即留取后面部分），得到转换为byte的补码为：“1000,0010”
转回原码为：“1000,0010”—>“1000,0001”------>“1111,1110” 十进制表示“-126”
所以高容量向下转换存在数据溢出的情况

- 向下类型转换，进行截取，高字节部分去除，保留低字节部分，结果可能为正也可能为负 正负由截取后目标数据类型的位数的最高为值决定
```

    /**
     * 输出
     * 
     * 1152826961430048804
     * 111111111111101010100000001101110011100001111111000000100100
     * fffaa037387f024
     * 1938288676
     * 1110011100001111111000000100100
     * 7387f024
     * 
     * @param args
     */
    public static void main(String[] args) {

        int a = 1073697798;
        long lt = (long) a * a;
        System.out.println(lt);
        System.out.println(Long.toBinaryString(lt));
        System.out.println(Long.toHexString(lt));

        int it = a * a;
        System.out.println(it);
        System.out.println(Integer.toBinaryString(it));
        System.out.println(Integer.toHexString(it));

    }
```
- 循环了
```

    /**
     * 输出
     * ====1
     * -9223372036854775808
     * 1000000000000000000000000000000000000000000000000000000000000000
     * 8000000000000000
     * ====2
     * 9223372036854775807
     * 111111111111111111111111111111111111111111111111111111111111111
     * 7fffffffffffffff
     * ====3
     * -2147483648
     * 10000000000000000000000000000000
     * 80000000
     * ====4
     * 2147483647
     * 1111111111111111111111111111111
     * 7fffffff
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("====1");
        Long l = Long.MAX_VALUE + 1;
        System.out.println(l);
        System.out.println(Long.toBinaryString(l));
        System.out.println(Long.toHexString(l));

        System.out.println("====2");
        l = Long.MIN_VALUE - 1;
        System.out.println(l);
        System.out.println(Long.toBinaryString(l));
        System.out.println(Long.toHexString(l));

        System.out.println("====3");
        int i = Integer.MAX_VALUE + 1;
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toHexString(i));

        System.out.println("====4");
        i = Integer.MIN_VALUE - 1;
        System.out.println(i);
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toHexString(i));
    }
```

- java
```

    /**
     * 在多种不同数据类型的表达式中，类型会自动向范围表示大的值的数据类型提升
     *
     * () 运算符优先级比 * 更高
     * @param args
     * 
     * 输出
     * 1938288676
     * 1938288676
     * 1152826961430048804
     * 1152826961430048804
     */
    public static void main(String[] args) {

        long l = 1073697798 * 1073697798;
        System.out.println(l);
        l = (long)(1073697798 * 1073697798); // 这只是把高位截取后的已经不正确的int值赋给了long
        System.out.println(l);
        l = (long)1073697798 * 1073697798;
        System.out.println(l);
        l = (long)((long)1073697798 * 1073697798);
        System.out.println(l);
    }

```