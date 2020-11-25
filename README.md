# toolkits

Quick Start
-----------
## Maven/Gradle configuration

Add the Maven dependency:

```xml
<dependency>
    <groupId>com.happy3w</groupId>
    <artifactId>toolkits</artifactId>
    <version>0.0.5</version>
</dependency>
```

Add the Gradle dependency:

```groovy
implementation 'com.happy3w:toolkits:0.0.5'
```

## 组件介绍
- TypeConverter 数据转换工具
- MessageRecorder 负责记录各种操作过程中的错误消息
- combination 提供对多个维度数据进行排列组合功能
- EasyIterator 功能类似Stream，但是增加了对排序后数据进行流式group的能力
- utils 各种常用小工具的集合

---
### TypeConverter

这是一个负责转换常用数据类型的工具。当前支持的类型有Boolean,Date,Double,Enum,Integer,Long,String.TypeConverter是线程安全的数据转换器。

#### 一般用法
```java
Date d1 = TypeConverter.INSTANCE.convert("2019-09-01 12:00:00", Date.class);
Date d2 = TypeConverter.INSTANCE.convert(1567310400000l, Date.class);
Assert.assertEquals(d1, d2);
```
上面代码得到的两个日期是一样的。

#### 调整配置
```java
TypeConverter.INSTANCE
    .findTci(DateStrBiTci.class)            // 找到时间与字符串之间的双向TypeConvertItem
    .appendConfig("MM/dd/yyyy HH:mm:ss")    // 支持解析这种格式
    .defaultConfig("yyyy-MM-dd HH:mm:ss");  // 支持解析这种格式，同时配置时间转换成字符串使用这个格式

Date d1 = TypeConverter.INSTANCE.convert("2019-09-01 12:00:00", Date.class);
Date d2 = TypeConverter.INSTANCE.convert("09/01/2019 12:00:00", Date.class);
Assert.assertEquals(d1, d2);

Assert.assertEquals("2019-09-01 12:00:00", TypeConverter.INSTANCE
    .convert(d2, String.class));
```

#### 定制自己的转换规则

```java
TypeConverter.INSTANCE
    .regist(new ITypeConvertItem<SourceType, TargetType>(){...})
```
ITypeConvertItem类型仅仅负责数据的单向转换，如果要做双向转换需要实现接口IBiTypeConvertItem，他们都有对应的抽象类AbstractTci和AbstractBiTci。

#### 转换规则自动发现
这个工具提供了自动发现转换规则能力，比如在注册了Long与Date的相互转换，注册了Date和String的相互转换后，这个工具也可以完成Long和String的相互转换。这个工具会在已经有的转换规则中找到一条最短路径尝试转换。所以Long到String的转换是先把Long转换为Date，再把Date转换为String，Long会当做一个时间戳转换为String。

如果这个不是你期望的结果，那么直接注册一个Long和String的TypeConvertItem，系统会使用你注册的这个最短路径。

#### 多个转换器并存
TypeConverter是一个可以随意实例化的对象，所以可以创建多个TypeConverter，分别使用不同的转换规则。直接new出来的TypeConverter不包含任何规则，通过TypeConverter.INSTANCE.newCopy()得到新的TypeConverter是复制了转换规则的转换器。转换规则是通过引用到新的转换器的，因此配置变化会在两个TypeConverter上生效，如果想要在一个转换器上面生效，还是需要通过重新注册规则的方式操作。

***注意***
类似日期格式"yyyy-MM-dd"和"MM-dd-yyyy"不能同时设置，由于SimpleDateFormat在解析的时候没有检测具体数字位数，所以可能会解析结果错误。


### MessageRecorder 组件
这个组件负责在处理一个逻辑的过程中收集各种信息，最终可以将收集内容以Map的形式当做Response返回到client。一般的使用流程如下：

```java
/**
 * 1、Controller中接收到请求，调用Service功能后返回结果
 * 这是一个上传学生信息的样例
 */
public class StudentController {
    @ResponseBody
    @RequestMapping(value = "/_cmd/upload", method = RequestMethod.POST)
    public Map<String, List<String>> uploadData(
        @RequestParam(value = "file") MultipartFile file) {
        MessageRecorder messageRecorder = studentService.upload(file.getInputStream(), ignoreWarning);
        return messageRecorder.toResponse();     // 将返回的消息信息转换为Response。出错时的状态码为200，Http的状态码仅用于表示链路状态，不要用于表示业务信息。
    }
}

/**
 * 2、Service将接收到的文件解析，遇到的问题记录在MessageRecoder中，
 * 最后根据messageRecorder.isSuccess()决定是否入库。
 */
public class StudentService {
    public MessageRecorder upload(InputStream inputStream) {
         MessageRecorder messageRecorder = new MessageRecorder();
         
         // 解析文件
         ...
         // 过程中遇到问题，可以使用下面代码记录下来，可以记录Error，也可以记录Warning，还可以仅仅是一个提示信息info
         if (student.getAge() < 10) {
             messageRecorder.appendError("Age is too small. current age is:{0}", student.getAge());             
         }
         
         // 最后根据解析结果决定是否要继续处理
         if (messageRecorder.isSuccess()) {
             saveAllData(allStudents);
         }
         return messageRecorder;
    }
}
```

对于某些出错就想抛出异常，但是接口是MessageRecorder的场景，可以使用MessageRecorder.errorExceptionRecorder()作为参数。
对于想要收集某一段操作都有哪些问题的场景，可以使用下面代码

```java
MessageRecorder messageRecorder = new MessageRecorder(ignoreWarning);

...
MessageFilter filter =  messageRecorder.startErrorFilter();
... // 一段业务逻辑
String errors = filter.getMessage();    // 得到过程中所有error消息
```


### combination 组件

```java
List<DimCombineDetail<String, String>> details = DimCombinationGenerator.<String, String>builder()
        .dimension("d1", "1", "2")          // 配置维度d1，名称不可省略，不可为空
        .dimension("d2", "d2v1", "d2v2")    // 配置维度d2
        .withNullDimension(true)            // 排列组合时，各个维度都增加null值
        .withOverRelation(true)             // 结果中增加over关系的计算
        .build()
        .generateDetail()                   // 枚举所有结果
        .collect(Collectors.toList());

Map<String, MyConfig> configMap = new HashMap<>();
List<MyConfig> myConfigs = details.stream()
        .map(d -> createConfig(d, configMap))   // 将返回结果映射为自己的配置信息
        .collect(Collectors.toList());

Assert.assertEquals(3 * 3, myConfigs.size());
Assert.assertTrue(
        Arrays.asList(
                "[[d1:null, d2:d2v1], [d1:1, d2:null], [d1:null, d2:null]]",
                "[[d1:1, d2:null], [d1:null, d2:d2v1], [d1:null, d2:null]]").contains(
                configMap.get("[d1:1, d2:d2v1]").subConfigs.toString()
        ));
```

Over关系：一个排列组合的结果如果完整的包含了另一个结果，则认为是Over。举例子如下


有如下数据
- a: [{'key': 'd1', 'value': 'd1v1'}, {'key': 'd2', 'value': 'd2v1'}]
- b: [{'key': 'd1', 'value': 'd1v1'}, {'key': 'd2', 'value': null}]
- c: [{'key': 'd1', 'value': 'd1v2'}, {'key': 'd2', 'value': null}]

此时Over关系如下

- a.isOver(b) == true
- a.isOver(c) == false
- a.isOver(a) == false


### EasyIterator 组件
功能类似Stream，但是增加入了如下能力：

- 对排序后数据进行流式group的能力
- 对流进行split

```java
Stream<Student> studentSteam = studentyRepository.queryAllByXxxOrderByGrade(...);   // 获得准备处理的数据
EasyIterator.from(studentStream)                                                    // 通过from方法创建一个EasyIterator。输入参数可以是Stream,Iterator
    .groupBy(Student::getGrade)                                                     // 按照某个属性分组。groupBy要求之前数据已经按照这个Key排序了，groupBy得到的结果也不是一个Map，而是一个Map.Entry的EasyIterator
    .map(gradeStudentEntry -> {                                                     // EasyIterator和Stream一样支持map,filter,flatMap,forEach等操作
        int grade = gradeStudentEntry.getKey();                                     // 前面groupBy后的entry.getKey()内容为计算的Key值
        List<Student> students = gradeStudentEntry.getValue();                      // 前面groupBy后的entry.getValue()内容为连续符合Key值的value列表。
        ...
    })
    .filter(...)
    .split(1000)                                                                    // 在Stream的基础上增加了split方法，通过这个方法将数据分组处理
    .flatMap(...)
    .toList();                                                                      // 直接转换为List
//  .toMap(v -> createKey(v));                                                      // 生成一个key,value的map，这里没有检测重复key，如果key重复最后一个出现的生效
//  .toMapList(v -> createKey(v));                                                  // 生成一个key,List<value>的map，key相同的value分组到一个列表中了

```

### utils 组件
各种常用小工具的集合
- StreamUtils 在Stream上操作的工具
  - split 将Stream分成指定大小的块
  - groupBy 将已经排好序的Stream分组，类似EasyIterator.groupBy
- ListUtils 在Collection上操作的工具
  - fixSizeList 初始化一个固定大小的集合
  - toMap 将集合转换为map
  - subList 更安全的获取一个集合的子集，如果索引越界则直接忽略错误的部分
  - newList 在已有的集合基础上增加新的数据，得到一个新的集合
- MapUtils 在Map上操作的工具
  - safeRead 在map中读取一个数据，如果没有按照指定逻辑创建一个
  - findByType 如果Map中key为Class类型，则建议使用这个方法查找Map，这个方法会检索集成关系
- TypeDescription 用于程序中创建一个具有泛型信息的Type。比如new TypeDescription(List.class, new Type[]{String.class}).toRealType(),得到了一个List<String>的Type类型。可以用于JSON转换等需要泛型的场景。
- ZoneIdCache 获得ZoneId的逻辑，他会缓存结果提升转换效率
- MapBuilder 协助创建一个Map
- Pair 提供key，value的组合，辅助计算
- SqlBuilder 辅助创建sql，同时搜索参数
- StepAction 固定步数执行指定操作的工具。可以执行类似每处理10000条记录就打印一条日志，这样的工作
- ...

### 其他
#### 原来的SimpleConverter变成了TypeConverter，使用方法类似
