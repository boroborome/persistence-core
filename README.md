# persistence-core


Quick Start
-----------
## Maven/Gradle configuration

Add the Maven dependency:

```xml
<dependency>
    <groupId>com.happy3w</groupId>
    <artifactId>persistence-core</artifactId>
    <version>0.0.4</version>
</dependency>
```

Add the Gradle dependency:

```groovy
implementation 'com.happy3w:persistence-core:0.0.4'
```

## 组件介绍
- RdAssistant 行数据助理，负责在处理以行为单位的文件数据，比如excel,csv
- ObjRdTableDef 定义对象行数据表，就是将一个对象表示为一个一行数据的结构
- RdTableDef 一个自定义的行数据，对于没有对象，只是单纯的直接访问excel或者csv时，可以使用这个对象
- IDataPage 数据页接口。用于访问一个excel sheet或者一个csv文件。

### RdAssistant
行数据助手，提供从page读写行数据的常用方法，方法命名规则如下:{Action}{DataType}
- Action范围
  - read: 从page读取数据操作
  - write: 将数据写入page功能
- DataType范围
  - Value: 表示操作数据仅仅是一个简单数据，需要写入一个单元格中。此时如果传入的需要写入的数据为List，则将List中内容拼接为一个字符串输出
  - List:表示操作数据为列表，方法会遍历列表中每个值，执行对应writeValueXXX方法
  - Obj:表示操作数据为代表这一行数据的完整对象，需要根据对象定义从对象上获取需要输出的属性，这个输出
  - Row:表示操作的数据为代表行数据的一个Wrapper
- Tag范围
  - Cfg:表示需要通过特定配置输出
  

Demo可以参见Excel读取的Demo https://github.com/boroborome/persistence-excel

### ObjRdTableDef
这个名字是：ObjectRowDataTableDefinition的缩写。

这里将Excel或者CSV当做一个表，这个表的特点是，每行都是独立的一个数据，所以称这种表为：行数据表。

IRdTableDef是用于定义这种行数据表，ObjRdTableDef是将一个对象映射成一行数据的一种实现。此外还有RdTableDef，单独的直接凭空创建一个行数据表。这里着重讲ObjRdTableDef的使用

#### Demo
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NumFormat("#.00")                          // 配置在对象使用的默认数据格式为固定显示两位小数
public static class MyData {
    @ObjRdColumn(value = "名字")             // 配置这个字段文件中的列头名称
    @FillForegroundColor(HssfColor.RED)     // 配置在导出Excel时使用红色背景色（这个在库persistence-excel中）
    private String name;

    @ObjRdColumn(value = "年龄", required = false) // 配置列头时可以配置列是否必须出现在文件中
    @NumFormat("000")                       // 配置这列使用的数字格式
    private int age;
    
    // 配置列头时，同时配置了使用特定的getter和setter配置这个属性，getter和setter操作的数据类型可以和这个属性不同，但getter和setter必须一致
    @ObjRdColumn(value = "在校生", getter = "getEnabledText", setter = "setEnabledText")
    private boolean enabled;

    @ObjRdColumn("生日")
    @DateFormat("yyyy-MM-dd HH:mm:ss")      // 配置使用的时间格式
    private Date birthday;

    @ObjRdColumn("Favorite Date")
    @DateFormat("yyyy-MM-dd HH:mm:ss")
    @DateZoneId("UTC-8")                    // 配置读写文件时使用的时区
    private Long favoriteDate;              // 属性配置了格式类配置时，会先将数据通过TypeConverter转换为格式类配置期望的数据类型，再读写文件

    /**
     * 配置数据从文件加载后需要额外做的一些操作。比如年龄必须大于0，小于100的检测；名字可能带有不需要的前缀，需要去掉。
     * ObjRdPostAction对被注解的方法名称、参数个数、参数顺序都没有要求，但一个对象只能有一个postAction。工具根据需要自动注入
     * @param data 刚刚解析数据使用的行信息，包括page name，行数等信息
     * @param recorder 如果有需要返回给用户的消息，通过这个recorder记录下来
     */
    @ObjRdPostAction
    public void postInit(RdRowWrapper<MyData> data, MessageRecorder recorder) {
        if (age < 0 || age > 100) {
            recorder.appendError("Wrong age:{0}", age);
        }
        if (name.startsWith("Name:")) {
            name = name.substring(5);
        }
    }

    public String getEnabledText() {
        return Boolean.toString(enabled);
    }

    // 列头注册的setter方法可以带有两个额外的参数，属性值必须在第一位，其他参数数量和顺序没有要求，工具自动注入
    public void setEnabledText(String enabled, RdRowWrapper<MyData> data, MessageRecorder recorder) {
        this.enabled = Boolean.parseBoolean(enabled);
    }
}
```

#### 配置规则
一般配置都既可以在属性上使用也可以在对象上使用，在属性上使用时表示这个配置只在某一列生效，在对象上配置时在所有列生效。

如果列上有和对象上一样类型的配置，则列上配置覆盖对象上配置。

#### 扩展注解
注解和配置是独立的，也就是说没有注解的配置，可以通过代码直接添加到IRdTableDef上一样生效。

为了使用方便一般都会成对出现。自定义注解方法可以参考：DateFormat和DateFormatImpl。
- DateFormat 这是一个注解，用于在ObjRdTableDef中配置时间格式
```java
@ObjRdConfigMap(DateFormatCfg.class)    // 配置这个注解对应的配置类型
public @interface DateFormat {
    String value();                     // 需要的配置
}
```
- DateFormatCfg 这是一个配置。为了让注解和配置能直观的配对，所以使用了相同的前缀
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateFormatCfg implements IAnnotationRdConfig<DateFormat> { // 这个而配置支持从注解生成，所以继承自IAnnotationRdConfig，如果只是单纯的使用一个配置可以继承IRdConfig
    private String format;              // 配置需要的属性
    
    // IAnnotationRdConfig接口的实现，负责从注解生成配置
    @Override
    public void initBy(DateFormat annotation) {
        this.format = annotation.value();
    }
    
    // 构建用于比较的Key字符串。如果两个Config的内容是相同的，则生成的Key相同，如果Config内容不同，则生成的Key也不同。不需要包含自己的类型信息
    @Override
    public void buildContentKey(StringBuilder builder) {
        builder.append(format);
    }
}
```

### RdTableDef
这是一个单纯的行数据表定义，直接添加需要管理的列以及需要的定制配置信息即可。访问文件时使用List<Object>作为一行数据。

适用于将统计数据输出到文件，或者自定制输出内容场景。

### IDataPage
这个接口是对文件的抽象，根据具体使用情况可以实现读接口IReadDataPage和写接口IWriteDataPage。

在persistence-excel中，SheetPage操作的是内存Dom所以同时实现了两个接口，但是在csv的访问中，会出现ReadPage和WritePage。

根据自己数据的特点可以实现自己的IDataPage。

```java
/**
 * 读文件接口
 * @param <T> 自身类型
 */
public interface IReadDataPage<T extends IReadDataPage<T>> extends IDataPage<T> {

    /**
     * 读取指定单元格数据
     * @param rowIndex 行索引
     * @param columnIndex 列索引
     * @param dataType 期望这个单元格的数据类型。返回类型应该和这个类型一致
     * @param extConfigs 这个单元格上的配置
     * @param <D> 返回数据类型
     * @return 返回读取到的数据
     */
    <D> D readValue(int rowIndex, int columnIndex, Class<D> dataType, ExtConfigs extConfigs);
}

/**
 * 写数据接口
 * @param <T> 自身类型
 */
public interface IWriteDataPage<T extends IWriteDataPage<T>> extends IDataPage<T> {

    /**
     * 换行。表示结束当前数据
     * @return 返回自己。这是为了方便操作
     */
    T newLine();

    /**
     * 写一个数据到当前位置，写完后向后移动一列。
     * @param value 需要写入的值
     * @param configs 写入数据使用的配置
     * @return 返回自己。这是为了方便操作
     */
    T writeValueCfg(Object value, ExtConfigs configs);
}
```

## 历史
### 0.0.4
- 修改过滤条件为正向命名
