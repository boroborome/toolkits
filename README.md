# toolkits

本项目是一个工具箱项目，提供一组不依赖于任何框架的工具

- TypeConverter类型转换工具
- EasyIterator迭代器工具，提供类似stream的操作方式，但额外提供了分组方法，因为是Iterator所以可以使用自己的代码一个一个的读取。
- combination组合工具，可以根据给出的维度信息，枚举所有可能的场景
- ConfigManager 根据数据类型自动加载配置的配置管理工具
- EasyPipe Pipe和Stream也很接近，主要差别是Stream是拉取数据，数据是一条线；而Pipe是推送式，允许数据因为条件不同而分叉走不同的逻辑。

详细使用方法参见 [https://boroborome.github.io/handbook/toolkits/](https://boroborome.github.io/handbook/toolkits/)
