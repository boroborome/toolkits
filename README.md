# toolkits
 
Quick Start
-----------
## Maven/Gradle configuration

Add the Maven dependency:

```xml
<dependency>
    <groupId>com.happy3w</groupId>
    <artifactId>toolkits</artifactId>
    <version>0.0.2</version>
</dependency>
```

Add the Gradle dependency:

```groovy
implementation 'com.happy3w:toolkits:0.0.2'
```

## 组件介绍

- combination 提供对多个维度数据进行排列组合功能
- EasyIterator 功能类似Stream，但是增加了对排序后数据进行流式group的能力
- utils 各种常用小工具的集合

### combination 组件

```java
List<CombineDetail<String, String>> details = CombinationGenerator.<String, String>builder()
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
功能类似Stream，但是增加了对排序后数据进行流式group的能力

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

