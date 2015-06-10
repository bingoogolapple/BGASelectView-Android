:running:BGASelectView-Android v1.0.0:running:
============
产品要实现类似web开发中的select标签实现的级联效果，没研究出Android自带的Spinner如何定制右侧的箭头，所以就通过继承TextView，并监听其点击事件来弹出PopupWindow实现这种效果了。


#### 效果图
![Demo](https://raw.githubusercontent.com/bingoogolapple/BGASelectView-Android/master/screenshots/bgaselectview.gif)

>Gradle

```groovy
dependencies {
    compile 'cn.bingoogolapple:bga-selectview:1.0.0@aar'
}
```

##### 详细用法请查看[Demo](https://github.com/bingoogolapple/BGASelectView-Android/tree/master/demo):feet:

## License

    Copyright 2015 bingoogolapple

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
